package org.zirbes.dse

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators

import org.apache.tinkerpop.gremlin.structure.Edge
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.VertexProperty

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator, property='id')
class Entity implements Comparable<Entity> {

    @JsonIgnore
    String vertexId

    UUID id
    String name
    String label

    @Override
    int compareTo(Entity other) {
        this.vertexId <=> other.vertexId
    }

    Map<String, Relationship> relationships = [:]

    static Entity from(Vertex vertex) {
        Entity entity = new Entity(
            label: vertex.label(),
            vertexId: getId(vertex)
        )

        VertexProperty prop = vertex.property('id')

        prop = vertex.property('id')
        if (prop.present) { entity.id = UUID.fromString(prop.value()) }

        prop = vertex.property('name')
        if (prop.present) { entity.name = prop.value() }

        return entity
    }

    void add(Relationship relation) {
        Relationship relationship = relationships[relation.label]
        if (!relationship) {
            relationships[relation.label] = relation
            return
        }

        if (relation.direction != relationship.direction) {
            String type = relation.label
            throw new IllegalStateException("conflicting relation directions for ${type} not supported by ${label}")
        }
        relationship.entities.addAll(relation.entities)

    }

    static Entity from(Edge edge) { return null }

    static String getId(Vertex vertex) {
        return getId(vertex.id())
    }

    static String getId(Long id) {
        return id.toString()
    }

    static String getId(Map id) {
        return "${id.'~label'}:${id.community_id}:${id.member_id}"
    }

}

