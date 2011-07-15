<html>
    <head>
        <title><g:message code="protocol.temperatureChart.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="protocol.show.headline" args="${[protocolInstance.recipeName.encodeAsHTML(), formatDate(formatName: 'default.date.formatter', date: protocolInstance.dateStarted)]}"/> - <g:message code="protocol.temperatureChart.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div class="leftNav" id="protocolNav">
            <ul>
              <li onclick="window.location.href='${createLink(action:'edit', id:protocolInstance.id)}'"><a href="${createLink(action:'edit', id:protocolInstance.id)}"><g:message code="protocol.edit" /></a></li>
              <li class="active" onclick="${createLink(action:'temperatureChart', id:protocolInstance.id)}'"><a href="window.location.href='${createLink(action:'temperatureChart', id:protocolInstance.id)}"><g:message code="protocol.temperatureChart.headline" /></a></li>
              <li onclick="window.location.href='${createLink(action:'pressureChart', id:protocolInstance.id)}'"><a href="${createLink(action:'pressureChart', id:protocolInstance.id)}"><g:message code="protocol.pressureChart.headline" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content">
          <img style="width: 100%" src='${createLink(controller: "protocol", action: "showTemperatureChart", params: [id: protocolInstance.id, width: "600", height: "400"])}'/>
        </div>
      </div>
      
      
    </body>
</html>
