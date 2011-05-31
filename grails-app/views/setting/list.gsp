<html>
    <head>
        <title><g:message code="setting.list.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="setting.list.headline" /></h1>
      <div class="singleColumn itemList">
        <ul>
          <g:each in="${settings}" var="setting">
          <li class="ui-widget-content">
            <div class="name"><g:fieldValue bean="${setting}" field="name" /></div>
          </li>
          </g:each>
          
          <li class="pagination">
            &lt;&lt; 1 2 3 4 5 6 7 8 9 &gt;&gt;
          </li>
        </ul>
      </div>
    </body>
</html>
