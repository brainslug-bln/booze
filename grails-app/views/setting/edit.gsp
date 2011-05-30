<html>
    <head>
        <title><g:message code="setting.edit.headline" args="${[settingInstance.name.encodeAsHTML()]}" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="setting.edit.headline" args="${[settingInstance.name.encodeAsHTML()]}" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div id="leftnav">
            <ul>
              <li class="active" id="main"><a href="#" rel="main"><g:message code="setting.edit.mainData" /></a></li>
              <li id="heatersTab"><a href="#" rel="heaters"><g:message code="setting.edit.heaters" /></a></li>
              <li id="motorsTab"><a href="#" rel="motors"><g:message code="setting.edit.motors" /></a></li>
              <li id="temperatureSensorsTab"><a href="#" rel="temperatureSensors"><g:message code="setting.edit.temperatureSensors" /></a></li>
              <li id="pressureSensorsTab"><a href="#" rel="pressureSensors"><g:message code="setting.edit.pressureSensors" /></a></li>
              <li id="frontendTab"><a href="#" rel="frontend"><g:message code="setting.edit.frontend" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div id="settingTabsContent" class="rightColumn_content">
          <div id="mainTabContent">
              <g:render template="main" bean="${settingInstance}" />
          </div>
          
          <div id="heatersTabContent" style="display: none;">
          </div>
          
          <div id="motorsTabContent" style="display: none;">
          </div>
          
          <div id="temperatureSensorsTabContent" style="display: none;">
          </div>
          
          <div id="pressureSensorsTabContent" style="display: none;">
          </div>
          
          <div id="frontendTabContent" style="display: none;">
            <g:render template="frontend" bean="${settingInstance}" />
          </div>
        </div>
      </div>
      
      <script type="text/javascript">
        $(document).ready(function() {
          booze.setting.initEdit();
        });
      </script>
    </body>
</html>
