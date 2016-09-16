package org.zirbes.dse

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Graph

import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph

@CompileStatic
@Slf4j
class LocalGraphService {

    /*
     * Plugins:
     *
     * plugin activated: tinkerpop.server
     * plugin activated: tinkerpop.utilities
     * plugin activated: tinkerpop.tinkergraph
     */

    void localGraph() {
        //Graph graph = GraphFactory.open('gremlin-config.yaml')
        Graph graph = TinkerGraph.open()
        GraphTraversalSource g = graph.traversal()
        GraphTraversal gv = g.V()
        log.info "Got result: ${gv}"

    }

}
