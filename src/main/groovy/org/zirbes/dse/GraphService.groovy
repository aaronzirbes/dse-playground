package org.zirbes.dse

import com.datastax.driver.dse.DseCluster
import com.datastax.driver.dse.DseSession
import com.datastax.driver.dse.graph.GraphNode
import com.datastax.driver.dse.graph.GraphOptions
import com.datastax.driver.dse.graph.GraphResultSet
import com.datastax.driver.dse.graph.GraphStatement
import com.datastax.driver.dse.graph.SimpleGraphStatement
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory

import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph

@CompileStatic
@Slf4j
class GraphService implements Closeable {

    protected final DseSession session
    protected final DseCluster cluster
    protected final String graphName = 'zorg'
    protected final String hostname = 'dse-graph-dev.connectedfleet.io'

    GraphService() {
        log.info "Connecting to DSE Graph instance=${hostname} with graph name=${graphName}"
        GraphOptions options = new GraphOptions().setGraphName(graphName)
        cluster = DseCluster.builder()
                .addContactPoint(hostname)
                .withGraphOptions(options)
                .build()
        session = cluster.connect()
    }

    @Override
    void close() {
        log.info "Disconnecting from DSE Graph instance..."
        session?.close()
        cluster?.close()
        log.info "Disconnected from DSE Graph instance."
    }

    GraphResultSet execute(String statement) {
        log.info "executing query: ${statement}"
        session.executeGraph(statement)
    }

    GraphResultSet execute(String statement, Map<String, Object> params) {
        log.info "executing query: ${statement} with params: ${params}"
        session.executeGraph(statement, params)
    }

    void load(String resource) {
        execute SchemaLoader.loadScript("/${resource}.groovy")
    }

    void reset() {
        execute 'schema.clear()'
    }

    void allowScan() {
        execute 'schema.config().option("graph.allow_scan").set(true)'
    }

    void developmentMode() {
        execute 'schema.config().option("graph.schema_mode").set("Development")'
    }

}
