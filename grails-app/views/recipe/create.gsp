<html>
    <head>
        <title><g:message code="recipe.create.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="recipe.create.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div id="leftnav">
            <ul>
              <li class="active"><a href="#" rel="#commonData"><g:message code="recipe.edit.commonData" /></a></li>
              <li class="disabled"><a href="#" rel="#mashing"><g:message code="recipe.edit.mashing" /></a></li>
              <li class="disabled"><a href="#" rel="#cooking"><g:message code="recipe.edit.cooking" /></a></li>
              <li class="disabled"><a href="#" rel="#fermentation"><g:message code="recipe.edit.fermentation" /></a></li>
              <li class="disabled"><a href="#" rel="#images"><g:message code="recipe.edit.images" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content">
          <div id="commonData">
            <g:render template="mainData" bean="${recipeInstance}" />
          </div>
          
          <div id="mashing" style="display: none;">
          </div>
          
          <div id="cooking" style="display: none;">
          </div>
          
          <div id="fermentation" style="display: none;">
          </div>
          <!--
          <div id="images" style="display: none;">
          </div>
          -->
        </div>
      </div>
    </body>
</html>
