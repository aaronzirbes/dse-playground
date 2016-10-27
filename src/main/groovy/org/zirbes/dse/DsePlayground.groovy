package org.zirbes.dse

import groovy.transform.CompileStatic

@CompileStatic
class DsePlayground implements Closeable {

    protected final GraphDemo demo

    DsePlayground() {
        demo = new GraphDemo()
    }

    void run() {
        demo.run()
        //demo.runLocal()
    }

    @Override
    void close() {
        demo.close()
    }

}
