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
          <li id="recipeList_item_${recipe.id}">
            <div style="width: 60%"><g:fieldValue bean="${recipe}" field="name" /></div>
            <div style="width: 15%"><g:fieldValue bean="${recipe}" field="author" />&nbsp;</div>
            <div style="width: 10%"><g:formatDate format="dd.MM.yyyy" date="${recipe.dateCreated}"/></div>
            <div class="delete">
              <a class="booze-icon booze-icon-delete" href="#" onclick="return false;"></a>
            </div>
            
            <script type="text/javascript" language="javascript">
              var li = $('#recipeList_item_${recipe.id}');
              li.click(function() {window.location.href='${createLink(controller:"recipe", action:"edit", id:recipe.id)}'});
              li.find('.delete').first().click(function(e) { 
                e.stopPropagation();
                booze.notifier.confirm('Soll das Rezept \'${recipe.name.encodeAsHTML()}\' wirklich gelöscht werden?', {modal: true, proceedCallback: function() {window.location.href='${createLink(controller:'recipe', action: 'delete', id:recipe.id)}'}})
              
              });
            </script>
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
