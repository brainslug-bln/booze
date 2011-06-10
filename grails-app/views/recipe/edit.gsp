<html>
    <head>
        <title><g:message code="recipe.edit.headline" /></title>
        <meta name="layout" content="main" />
        <g:javascript src="booze.recipe.js" />

        <script type="text/javascript">
          $(document).ready(function() {
            booze.recipe = new BoozeRecipe('update');
          });
        </script>
    </head>
    <body>
      <h1><g:message code="recipe.edit.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div class="leftNav" id="recipeNav">
            <ul>
              <li class="active"><a href="#" rel="mainData"><g:message code="recipe.edit.mainData" /></a></li>
              <li><a href="#" rel="mashing"><g:message code="recipe.edit.mashing" /></a></li>
              <li><a href="#" rel="cooking"><g:message code="recipe.edit.cooking" /></a></li>
              <li><a href="#" rel="fermentation"><g:message code="recipe.edit.fermentation" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content" id="recipeTabsContent">
          <div id="mainData">
            <g:render template="mainData" bean="${recipeInstance}" />
          </div>
          
          <div id="mashing" style="display: none;">
            <g:render template="mashing" bean="${recipeInstance}" />
          </div>
          
          <div id="cooking" style="display: none;">
            <g:render template="cooking" bean="${recipeInstance}" />
          </div>
          
          <div id="fermentation" style="display: none;">
            <g:render template="fermentation" bean="${recipeInstance}" />
          </div>
          
          <!--
          <div id="images" style="display: none;">
          </div>
          -->
        </div>
      </div>
      
      
    </body>
</html>
