use bevy::{color::palettes::css::*, prelude::*};
use bevy_prototype_lyon::prelude::*;

fn main() {
    App::new()
        .add_systems(Startup, startup)
        .add_systems(Update, update)
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

fn update() {
    println!("Updating...");
}
