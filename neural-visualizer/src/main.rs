use bevy::{color::palettes::css::*, prelude::*};
use bevy_prototype_lyon::prelude::*;
use bevy_slinet::{
    ClientConfig,
    client::{self, ClientPlugin},
    packet_length_serializer::BigEndian,
    protocols::udp::UdpProtocol,
    serializer::SerializerAdapter,
    serializers::bincode::{BincodeSerializer, DefaultOptions},
};
use serde::{Deserialize, Serialize};
use std::sync::Arc;
struct Config;

#[derive(Serialize, Deserialize, Debug)]
enum ClientPacket {
    String(String),
}

#[derive(Serialize, Deserialize, Debug)]
enum ServerPacket {
    String(String),
}

impl ClientConfig for Config {
    type ClientPacket = ClientPacket;
    type ServerPacket = ServerPacket;
    type Protocol = UdpProtocol;
    type SerializerError = bincode::Error;
    type LengthSerializer = BigEndian<u8>;
    fn build_serializer()
    -> SerializerAdapter<Self::ServerPacket, Self::ClientPacket, Self::SerializerError> {
        SerializerAdapter::ReadOnly(Arc::new(BincodeSerializer::<DefaultOptions>::default()))
    }
}

fn main() {
    let server_addr = "127.0.0.1:6666";

    App::new()
        .add_plugins(DefaultPlugins)
        .add_plugins(ClientPlugin::<Config>::connect(server_addr))
        .add_observer(client_packet_receive_system)
        // .add_systems(Startup, startup)
        // .add_systems(Update, update)
        .run();
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

fn client_packet_receive_system(new_packet: Trigger<client::PacketReceiveEvent<Config>>) {
    match &new_packet.event().packet {
        ServerPacket::String(s) => {
            println!("Received string: {}", s);
        }
    }
}

fn update() {
    println!("Updating...");
}
