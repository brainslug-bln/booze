<html>
    <head>
        <title><g:message code="setting.list.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="setting.list.headline" /></h1>
      <div class="itemList scrollList">
        <ul>
          <g:each in="${settings}" var="setting">
          <li onclick="window.location.href='${createLink(controller:'setting', action:'edit', id:setting.id)}'">
            <div class="name" style="width: 81%"><g:fieldValue bean="${setting}" field="name" /></div>
            <div class="active" style="width: 7%"><g:if test="${setting.active == true}">aktiv</g:if></div>
            <div class="delete">
              <a class="ui-icon ui-icon-circle-close" href="${createLink(controller:'setting', action: 'delete', id:setting.id)}"></a>
            </div>
          </li>
          </g:each>
          <li></li>
        </ul>
      </div>
    </body>
</html>
