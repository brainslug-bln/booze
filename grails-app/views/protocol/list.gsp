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

  <div class="protocolList">
    <p>Protokolle werden automatisch während des Brauvorgangs erstellt. Erfasst werden im Abstand von jeweils 30 Sekunden die gemittelten Temperaturwerte von Innen- bzw. Außensensoren, der Druck sowie der Stromverbrauch. <br/>
      Während des Brauvorgangs und auch nachträglich können diverse Messwerte gespeichert werden.
    </p>
    <table>
      <thead>
      <tr>
        <g:sortableColumn class="recipeName" property="recipeName" title="${message(code:'protocol.recipeName')}"/>
        <g:sortableColumn class="dateStarted" property="dateStarted" title="${message(code:'protocol.dateStarted')}"/>
      </tr>
      </thead>
      <tbody>
      <g:if test="${protocolInstanceList.size() > 0}">
        <g:each in="${protocolInstanceList}" status="i" var="protocolInstance">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}" onclick="window.location.href = '${createLink(action:"edit", id:protocolInstance.id)}';">

            <td>${fieldValue(bean: protocolInstance, field: 'recipeName')}</td>

            <td><g:formatDate formatName="default.date.formatter" date="${protocolInstance.dateStarted}"/></td>
          </tr>
        </g:each>
      </g:if>
      <g:else>
        <tr><td colspan="2" class="noProtocolAvailable"><g:message code="protocol.list.noProtocolAvailable"/></td></tr>
      </g:else>
      </tbody>
    </table>
  </div>
  <div class="paginateButtons">
    <g:paginate total="${protocolInstanceTotal}"/>
  </div>
</div>


<g:if test="${flash.message}">
  <g:notification message="${flash.message}"/>
</g:if>
</body>
</html>
