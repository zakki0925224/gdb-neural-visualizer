use config::Config;
use core::time::Duration;
use debug_info::{DebugInfo, Register};
use futures_util::{SinkExt, StreamExt};
use gdbmi::Gdb;
use nix::libc::{SIGINT, kill};
use std::{fs, process::Stdio};
use tokio::{net::TcpListener, time::sleep};
use tokio_tungstenite::tungstenite::Message;

mod config;
mod debug_info;

const CONFIG_PATH: &str = "../config.toml";

#[tokio::main]
async fn main() -> anyhow::Result<()> {
    // parse config
    let config: Config = toml::from_str(&fs::read_to_string(CONFIG_PATH)?)?;
    println!("Parsed configurations: {:?}", config);

    // create ws server
    let ws_listener = TcpListener::bind(format!("0.0.0.0:{}", config.ws_port)).await?;
    println!("Waiting for WebSocket client on port {}...", config.ws_port);

    let (ws_stream, addr) = ws_listener.accept().await?;
    println!("WebSocket client connected: {}", addr);
    let ws_stream = tokio_tungstenite::accept_async(ws_stream).await?;
    let (mut ws_sender, _) = ws_stream.split();

    let duration = Duration::from_secs_f32(config.gdb_timeout);
    let wait_booting = Duration::from_secs_f32(config.gdb_timeout_wait_booting);
    let wait_next = Duration::from_secs_f32(config.gdb_timeout_wait_next);

    let cmd = tokio::process::Command::new("gdb")
        .arg("--interpreter=mi3")
        .arg(config.gdb_symbol_file)
        .stdin(Stdio::piped())
        .stdout(Stdio::piped())
        .stderr(Stdio::piped())
        .spawn()?;
    let pid = cmd.id().unwrap();

    let mut gdb = Gdb::new(cmd, duration);

    println!("Connecting to GDB server on port {}...", config.gdb_port);
    gdb.raw_cmd(format!("target remote :{}", config.gdb_port))
        .await?;
    // can insert breakpoints here

    gdb.set_timeout(wait_booting);
    let _ = gdb.exec_continue().await; // timeout
    sleep(wait_booting).await;
    unsafe {
        kill(pid as i32, SIGINT); // send Ctrl-C
    }
    gdb.set_timeout(duration);
    let _ = gdb.exec_continue().await;

    loop {
        sleep(wait_next).await;
        unsafe {
            kill(pid as i32, SIGINT); // send Ctrl-C
        }

        let payload = gdb
            .raw_cmd("-data-list-register-values x")
            .await?
            .expect_result()?
            .expect_payload()?;
        let register_values = &payload.as_map()["register-values"];
        let list = register_values.clone().expect_list()?;

        let mut regs: Vec<Register> = Vec::new();
        for v in list.iter() {
            let dict_data = v.clone().expect_dict()?;
            let dict = dict_data.as_map();
            let number: u8 = dict["number"].clone().expect_string()?.parse()?;
            let value = match u64::from_str_radix(&dict["value"].clone().expect_string()?[2..], 16)
            {
                Ok(v) => v,
                Err(_) => {
                    continue;
                }
            };
            let reg: Register = (number, value).into();
            if !reg.is_other() {
                regs.push(reg);
            }
        }

        let frame = gdb.stack_info_frame().await?;
        let variables = gdb.stack_list_variables(false).await?;

        let debug_info = DebugInfo {
            regs,
            frame: frame.into(),
            variables: variables.into_iter().map(|v| v.into()).collect(),
        };
        let debug_info_json = serde_json::to_string(&debug_info)?;

        // send to ws client
        if ws_sender
            .send(Message::Text(debug_info_json.into()))
            .await
            .is_err()
        {
            println!("WebSocket send error");
            break;
        }

        if gdb.exec_continue().await.is_err() {
            println!("Error stepping through the code");
            break;
        }
    }
    gdb.exec_finish().await?;

    Ok(())
}
