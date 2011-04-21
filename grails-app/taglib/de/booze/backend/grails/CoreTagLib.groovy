package de.booze.backend.grails

class CoreTagLib {
    static namespace = "core"

    /**
     * Dummy
     * Output content only if the community credentials are
     * valid
     */
    def communityEnabled = { attrs, body ->
        out << body()
    }
}
