package de.booze.backend.grails

class CoreTagLib {
    static namespace = "core"

    /**
     * Dummy
     * Output content only if the community credentials are
     * valid
     */
    def communityEnabled = { attrs, body ->
        out << ""
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
    
  def fieldValueIfExists = { attrs, body ->
    try {
      out << attrs.bean?.getAt(attrs.field)?.encodeAsHTML()
    }
    catch(Exception e) {
      out << ""
    }
  }
  
  def ifFieldExists = { attrs, body ->
    try {
      if(attrs.bean?.getAt(attrs.field) != null) {
        out << body()
      }
    }
    catch(Exception e) {}
  }
  
  def ifNotFieldExists = { attrs, body ->
    try {
      if(attrs.bean?.getAt(attrs.field) == null) {
        out << body()
      }
    }
    catch(Exception e) {
      out << body()
    }
  }
  
  def availableRecipeCount = { attrs, body ->
    out << Recipe.count()
  }
  
  def availableProtocolCount = { attrs, body ->
    out << Protocol.count()
  }
}
