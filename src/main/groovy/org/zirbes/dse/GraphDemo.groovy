package org.zirbes.dse

import com.datastax.driver.dse.graph.GraphNode
import com.datastax.driver.dse.graph.GraphResultSet
import com.datastax.driver.dse.graph.GraphStatement
import com.datastax.driver.dse.graph.SimpleGraphStatement

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import groovy.util.logging.Slf4j

import java.time.Duration
import java.time.Instant

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Edge
import org.apache.tinkerpop.gremlin.structure.Element
import org.apache.tinkerpop.gremlin.structure.Vertex

@CompileStatic
@Slf4j
class GraphDemo implements Closeable {

    protected final GraphService service
    protected final LocalGraphService local

    protected final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)

    GraphDemo() {
        service = new GraphService()
        local = new LocalGraphService()
    }

    @Override
    void close() {
        service?.close()
    }

    void runLocal() {
        local.localGraph()
    }

    void reload() {
        log.info 'Resetting database'
        service.reset()

        log.info 'Loading Schema.'
        service.load('pnet-schema')

        log.info 'Loading Data'
        service.load('pnet-data')
    }

    void run() {
        // reload()

        log.info 'Querying vertices.'

        UUID id = UUID.fromString('84f34a84-8969-4aa1-891a-fd35fbc8fa6a')
        String label = 'organization'

        Map<String, Object> params = [:]
        params.vertexLabel = label
        params.vertexId = id

        List<Element> elements = []

        //String statement = "g.V().has('name', 'Dean Foods')"
        String edgeQuery = "g.V().has(T.label, vertexLabel).has('id', vertexId).bothE()"
        String vertexQuery = "g.V().has(T.label, vertexLabel).has('id', vertexId).bothE().bothV()"


        Instant start = Instant.now()
        elements.addAll(getElements(vertexQuery, params))
        elements.addAll(getElements(edgeQuery, params))
        Instant finish = Instant.now()

        Duration duration = Duration.between(start, finish)
        log.info "Took: ${duration}"

        log.info "Got: ${elements.size()} things."

        Collection<Entity> entities = mapFrom(elements)
        log.info "Got: ${entities.size()} entities."

        Entity entity = root(entities, id, label)

        String json = objectMapper.writeValueAsString(entity)
        log.info "Final Product:\n${json}"
    }

    Entity root(Collection<Entity> entities, UUID id, String label) {
        entities.find { it.label == label && it.id == id }
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    Collection<Entity> mapFrom(List<Element> elements) {

        elements.each { println " * class: ${it.class} " }

        List<Vertex> vertices = elements.findAll { Vertex.isAssignableFrom(it.class) }
        List<Edge> edges = elements.findAll { Edge.isAssignableFrom(it.class) }
        Map<String,Entity> entities = [:]
        vertices.each {
            Entity e = Entity.from(it)
            entities[e.vertexId] = e
        }

        edges.each { Edge edge ->
            String label = edge.label()

            String inV = Entity.getId(edge.inVertex())
            String outV = Entity.getId(edge.outVertex())

            Entity inE = entities[inV]
            Entity outE = entities[outV]

            inE.add(Relationship.incoming(label, outE))
            outE.add(Relationship.outgoing(label, inE))
        }

        return entities.values() as List

    }

    List<Element> getElements(String query, Map<String, Object> params) {
        List<Element> elements = []
        GraphResultSet rs = service.execute(query, params)
        rs.all().each { GraphNode node ->
            if (node.edge) {
                log.info "Found Edge."
                Edge e = node.as(Edge)
                String inV = Entity.getId(e.inVertex())
                String outV = Entity.getId(e.outVertex())
                log.info " [${inV}] ==(${e.label()})==> [${outV}]"
                elements << e
            } else if (node.vertex) {
                log.info "Found Vertex."
                Vertex v = node.as(Vertex)
                elements << v
            } else {
                log.warn "Found something else?!"
                log.warn " * ${node}"
            }
        }
        log.info "Done."
        return elements
    }

    GraphStatement query(Closure<GraphTraversalSource> cls) {
        new SimpleGraphStatement(cls.toString())
    }

}
