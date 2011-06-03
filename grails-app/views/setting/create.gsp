<html>
    <head>
        <title><g:message code="setting.create.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="setting.create.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div id="settingNav" class="leftNav">
            <ul>
              <li class="active" id="mainDataTab"><a href="#"><g:message code="setting.edit.mainData" /></a></li>
              <li id="heatersTab"><a href="#"><g:message code="setting.edit.heaters" /></a></li>
              <li id="motorsTab"><a href="#"><g:message code="setting.edit.motors" /></a></li>
              <li id="motorTasksTab"><a href="#" rel="motorTasks"><g:message code="setting.edit.motorTasks" /></a></li>
              <li id="temperatureSensorsTab"><a href="#"><g:message code="setting.edit.temperatureSensors" /></a></li>
              <li id="pressureSensorsTab"><a href="#"><g:message code="setting.edit.pressureSensors" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content">
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
          </div>
        </div>
      </div>
      
      <script type="text/javascript">
        $(document).ready(function() {
          booze.setting.initCreate();
        });
      </script>
    </body>
</html>
