<html>
    <head>
        <title>Rezeptliste</title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="recipe.list.headline" /></h1>
      <div class="singleColumn itemList recipeList">
        <ul>
          <g:each in="${recipeInstanceList}" var="recipe">
          <li class="ui-widget-content" onclick="window.location.href='${createLink(controller:"recipe", action:"edit", id:recipe.id)}'">
            <div class="name"><g:fieldValue bean="${recipe}" field="name" /></div>
            <div class="author"><g:fieldValue bean="${recipe}" field="author" /></div>
            <div class="dateCreated"><g:formatDate format="dd.MM.yyyy" date="${recipe.dateCreated}"/></div>
          </li>
          </g:each>

          <li class="pagination">
            &lt;&lt; 1 2 3 4 5 6 7 8 9 &gt;&gt;
          </li>
        </ul>
      </div>
    </body>
</html>
