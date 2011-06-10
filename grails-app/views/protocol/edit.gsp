<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title><g:message code="protocol.show.headline" args="${[protocolInstance.recipeName.encodeAsHTML(), formatDate(formatName: 'default.date.formatter', date: protocolInstance.dateStarted)]}"/></title>
  <jawr:script src="/bundles/recipe.js"/>
</head>
<body>

<div class="body">
  <h3>
    <g:message code="protocol.show.headline" args="${[protocolInstance.recipeName.encodeAsHTML(), formatDate(formatName: 'default.date.formatter', date: protocolInstance.dateStarted)]}"/>
  </h3>

  <g:render template="protocol" model="${[protocolInstance: protocolInstance]}"/>

  <g:renderErrors bean="${protocolInstance}" />

</div>


<g:if test="${flash.message}">
  <g:notification message="${flash.message}"/>
</g:if>

<g:hasErrors bean="${protocolInstance}">
  <g:notification message="${g.message(code:'protocol.edit.pleaseCorrectErrors')}" duration="5000"/>
</g:hasErrors>

</body>
</html>
