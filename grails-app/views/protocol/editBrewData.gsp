<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title><g:message code="protocol.show.headline" args="${[protocolInstance.recipeName.encodeAsHTML(), formatDate(formatName: 'default.date.formatter', date: protocolInstance.dateStarted)]}"/></title>
</head>
<body>

<div class="body">
  <h1><g:message code="protocol.show.headline" args="${[protocolInstance.recipeName.encodeAsHTML(), formatDate(formatName: 'default.date.formatter', date: protocolInstance.dateStarted)]}"/></h1>

  <div class="leftColumn">
    <div id="leftColumn_content" class="leftColumn_content">
      <div class="leftNav" id="protocolNav">
        <ul>
          <li class="active" onclick="window.location.href='${createLink(action:'editBrewData', id:protocolInstance.id)}'"><a href="${createLink(action:'editBrewData', id:protocolInstance.id)}"><g:message code="protocol.editBrewData" /></a></li>
          <li onclick="window.location.href='${createLink(action:'editFermentationData', id:protocolInstance.id)}'"><a href="${createLink(action:'editFermentationData', id:protocolInstance.id)}"><g:message code="protocol.editFermentationData" /></a></li>
          <li onclick="window.location.href='${createLink(action:'temperatureChart', id:protocolInstance.id)}'"><a href="${createLink(action:'temperatureChart', id:protocolInstance.id)}"><g:message code="protocol.temperatureChart.headline" /></a></li>
          <li onclick="window.location.href='${createLink(action:'pressureChart', id:protocolInstance.id)}'"><a href="${createLink(action:'pressureChart', id:protocolInstance.id)}"><g:message code="protocol.pressureChart.headline" /></a></li>
        </ul>
      </div>
      <div class="clear"></div>
    </div>
  </div>

  <div class="rightColumn">
    <div class="rightColumn_content">
  
      <g:render template="protocol" model="${[protocolInstance: protocolInstance]}"/>

      <g:renderErrors bean="${protocolInstance}" />
      
      <div class="clear"></div>
    </div>

  </div>
</div>

</body>
</html>
