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
    
    def settingsFontSize = { attrs, body ->
        def fontSizeInc = 0
        def setting = Setting.findByActive(1)
        if(setting) {
            fontSizeInc = setting.frontendFontSize
        }
        out << "<style type='text/css'>"
        out << "html * { font-size: ${12 + fontSizeInc}px; }"
        out << "</style>"
    }
}
