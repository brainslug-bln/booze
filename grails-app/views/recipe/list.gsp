<html>
    <head>
        <title>Rezeptliste</title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="recipe.list.headline" /></h1>
      <div class="itemList recipeList scrollList">
        <ul>
          <g:each in="${recipes}" var="recipe">
          <li onclick="window.location.href='${createLink(controller:"recipe", action:"edit", id:recipe.id)}'">
            <div style="width: 60%"><g:fieldValue bean="${recipe}" field="name" /></div>
            <div style="width: 15%"><g:fieldValue bean="${recipe}" field="author" />&nbsp;</div>
            <div style="width: 10%"><g:formatDate format="dd.MM.yyyy" date="${recipe.dateCreated}"/></div>
            <div class="delete">
              <a class="ui-icon ui-icon-circle-close" href="${createLink(controller:'recipe', action: 'delete', id:recipe.id)}"></a>
            </div>
          </li>
          </g:each>
          
          <g:if test="${recipes.size() < 1}">
            <li class="emptyList" onclick="window.location.href='${createLink(controller:'recipe', action:'create')}'"><div><g:message code="recipe.list.noRecipesAvailable" /></div></li>
          </g:if>
          
          <li></li>
        </ul>
      </div>
    </body>
</html>
