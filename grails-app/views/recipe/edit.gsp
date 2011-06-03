<html>
    <head>
        <title><g:message code="recipe.edit.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="recipe.edit.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div class="leftNav" id="recipeNav">
            <ul>
              <li class="active"><a href="#" rel="#mainData"><g:message code="recipe.edit.mainData" /></a></li>
              <li><a href="#" rel="#correctWort"><g:message code="recipe.edit.mashing" /></a></li>
              <li><a href="#" rel="#calculateAlcohol"><g:message code="recipe.edit.cooking" /></a></li>
              <li><a href="#" rel="#calculatePressure"><g:message code="recipe.edit.fermentation" /></a></li>
              <li><a href="#" rel="#calculateFermentationRatio"><g:message code="recipe.edit.images" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content" id="recipeTabContent">
          <div id="mainData">
            <g:render template="mainData" bean="${recipeInstance}" />
          </div>
          
          <div id="mashing" style="display: none;">
          </div>
          
          <div id="cooking" style="display: none;">
          </div>
          
          <div id="fermentation" style="display: none;">
          </div>
        </div>
      </div>
      
      <g:javascript>
        $(document).ready(function() {
          booze.recipe.initEdit('${recipeInstance.id}');
        });
      </g:javascript>
    
    </body>
</html>
