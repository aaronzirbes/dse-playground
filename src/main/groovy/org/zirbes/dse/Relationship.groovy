package org.zirbes.dse

class Relationship {

    Direction direction
    String label
    List<Entity> entities

    static Relationship incoming(String label, Entity entity) {
        new Relationship(
            direction: Direction.IN,
            label: label,
            entities: [ entity ]
        )
    }

    static Relationship outgoing(String label, Entity entity) {
        new Relationship(
            direction: Direction.OUT,
            label: label,
            entities: [ entity ]
        )
    }

}
