package org.zirbes.dse

class Runner {

    static void main(String[] argv) {
        DsePlayground playground = new DsePlayground()
        try {
            playground.run()
        } finally {
            playground.close()
        }
    }
}
