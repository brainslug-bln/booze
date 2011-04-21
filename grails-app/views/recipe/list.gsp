<html>
    <head>
        <title>Rezeptliste</title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="recipe.list.headline" /></h1>
      <div class="singleColumn recipeList">
        <ul>
          <g:each in="${recipes}">
          <li class="ui-widget-content">
            <div class="name"><g:fieldValue bean="${recipe}" field="name" /></div>
            <div class="author"><g:fieldValue bean="${recipe.owner}" field="author.name" /></div>
            <div class="dateCreated"><g:formatDate format="dd.MM.yyyy" date="${recipe.dateCreated}"/></div>
          </li>
          </g:each>

          <li class="ui-widget-content">
            <div class="name">Rezept 1</div>
            <div class="author">Autor 1</div>
            <div class="dateCreated">12.12.2011</div>
          </li>

          <li class="pagination">
            &lt;&lt; 1 2 3 4 5 6 7 8 9 &gt;&gt;
          </li>
        </ul>
      </div>
    </body>
</html>
