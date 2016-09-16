package org.zirbes.dse

import groovy.transform.CompileStatic

@CompileStatic
class SchemaLoader {

    static String loadScript() {
        loadScript('/schema.groovy')
    }

    static String loadScript(String resourcePath) {
        InputStream inputStream = this.class.getResourceAsStream(resourcePath)
        if (inputStream) { return inputStream.text }
        throw new FileNotFoundException(resourcePath)
    }

}
