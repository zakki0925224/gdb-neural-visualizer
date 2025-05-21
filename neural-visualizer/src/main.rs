use bevy::{color::palettes::css::*, prelude::*};
use bevy_prototype_lyon::prelude::*;
use bevy_simple_networking::*;
use gdb_logger::debug_info::DebugInfo;
use std::net::{SocketAddr, UdpSocket};

fn main() -> anyhow::Result<()> {
    let server_addr: SocketAddr = "127.0.0.1:6666".parse()?;
    let socket = UdpSocket::bind("[::]:0")?;
    socket.connect(server_addr)?;
    socket.set_nonblocking(true)?;

    App::new()
        .insert_resource(SocketAddrResource::new(server_addr))
        .insert_resource(UdpSocketResource::new(socket))
        .add_plugins((DefaultPlugins, ClientPlugin))
        .add_systems(Startup, udp_startup)
        .add_systems(Update, udp_update)
        .add_systems(Startup, startup)
        .run();

    Ok(())
}

fn udp_startup(remote_addr: Res<SocketAddrResource>, mut transport: ResMut<Transport>) {
    transport.send(**remote_addr, b"abc");
}

fn udp_update(mut events: EventReader<NetworkEvent>) {
    for event in events.read() {
        match event {
            NetworkEvent::Message(_, bytes) => {
                let json = String::from_utf8_lossy(bytes);
                info!("{:?}", json);
                match serde_json::from_str::<DebugInfo>(&json) {
                    Ok(debug_info) => {
                        info!("{:?}", debug_info);
                    }
                    _ => (),
                }
            }
            NetworkEvent::Connected(socket_addr) => info!("Connected to {}", socket_addr),
            NetworkEvent::Disconnected(socket_addr) => info!("Disconnected from {}", socket_addr),
            NetworkEvent::RecvError(error) => error!("Receive error: {:?}", error),
            NetworkEvent::SendError(error, message) => {
                error!("Send error: {:?} for payload {:?}", error, message.payload)
            }
        }
    }
}

fn startup(mut commands: Commands) {
    let input_positions = [
        Vec2::new(-300.0, 100.0),
        Vec2::new(-300.0, 0.0),
        Vec2::new(-300.0, -100.0),
    ];
    for pos in input_positions {
        let shape = shapes::Circle {
            radius: 20.0,
            center: pos,
        };

        commands.spawn((
            ShapeBundle {
                path: GeometryBuilder::build_as(&shape),
                ..default()
            },
            Fill::color(DARK_CYAN),
            Stroke::new(BLACK, 10.0),
        ));
    }
}
