<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title><g:message code="protocol.list.headline"/></title>
</head>
<body>

<div class="body">
  <h1>
    <g:message code="protocol.list.headline"/>
  </h1>
  
  <div class="protocolList itemList scrollList">
    <ul>
      <g:each in="${protocolInstanceList}" var="protocol">
      <li onclick="window.location.href='${createLink(controller:"protocol", action:"edit", id:protocol.id)}'">
        <div class="recipeName" style="width: 78%">${fieldValue(bean: protocol, field: 'recipeName')}</div>
        <div class="dateCreated" style="width: 10%"><g:formatDate formatName="default.date.formatter" date="${protocol.dateStarted}"/></div>
        <div class="delete">
          <a class="ui-icon ui-icon-circle-close" href="${createLink(controller:'protocol', action: 'delete', id:protocol.id)}"></a>
        </div>
      </li>
      </g:each>
      
      <g:if test="${protocolInstanceList.size() < 1}">
          <li class="emptyList"><div><g:message code="protocol.list.noProtocolsAvailable" /></div></li>
        </g:if>
      <li></li>
    </ul>
  </div>
</div>


<g:if test="${flash.message}">
  <g:notification message="${flash.message}"/>
</g:if>
</body>
</html>
