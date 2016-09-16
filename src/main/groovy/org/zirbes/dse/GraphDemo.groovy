package org.zirbes.dse

import com.datastax.driver.dse.graph.GraphNode
import com.datastax.driver.dse.graph.GraphResultSet
import com.datastax.driver.dse.graph.GraphStatement
import com.datastax.driver.dse.graph.SimpleGraphStatement
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Direction
import org.apache.tinkerpop.gremlin.structure.Vertex

@CompileStatic
@Slf4j
class GraphDemo implements Closeable {

    protected final GraphService service

    GraphDemo() {
        service = new GraphService()
    }

    @Override
    void close() {
        service.close()
    }

    void run() {
        log.info 'Resetting database'
        service.reset()

        log.info 'Loading Schema.'
        service.load('pnet-schema')

        log.info 'Loading Data'
        service.load('pnet-data')

        log.info 'Querying vertices.'
        String statement = "g.V().has('name', 'Dean Foods')"
        GraphResultSet rs = service.execute(statement)
        log.info "Graph Results:"

        rs.all().each { GraphNode node ->
            Vertex v = node.as(Vertex)
            log.info " * ${v.label()}" // todo: add properties here
            v.properties().each {
                log.info "   -(prop) ${it}"
            }
            v.edges(Direction.BOTH, )

        }
        log.info "Done."
    }

    GraphStatement query(Closure<GraphTraversalSource> cls) {
        new SimpleGraphStatement(cls.toString())
    }

}
