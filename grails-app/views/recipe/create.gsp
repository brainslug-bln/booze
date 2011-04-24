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
              <li class="active"><a href="#" rel="#calculateFare"><g:message code="recipe.edit.commonData" /></a></li>
              <li class="disabled"><a href="#" rel="#correctWort"><g:message code="recipe.edit.meshing" /></a></li>
              <li class="disabled"><a href="#" rel="#calculateAlcohol"><g:message code="recipe.edit.cooking" /></a></li>
              <li class="disabled"><a href="#" rel="#calculatePressure"><g:message code="recipe.edit.fermentation" /></a></li>
              <li class="disabled"><a href="#" rel="#calculateFermentationRatio"><g:message code="recipe.edit.images" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content">
          <div id="commonData" class="form">
            <h2><g:message code="recipe.edit.commonData" /></h2>
            
            <div class="row">
                <label for="recipe.name"><g:message code="recipe.name" /></label>
                <input type="text" name="recipe.name" value="" maxlength="254" />
                <span class="error" id="error_name"></span>
            </div>
            
            <div class="row">
              <div class="column50percent">
                <label for="recipe.name"><g:message code="recipe.dateCreated" /></label>
                <span class="immutable">XX.XX.XXXX</span>
              </div>
              <div class="column50percent">
                <label for="recipe.author"><g:message code="recipe.author" /></label>
                <span class="immutable">AutorName</span>
              </div>
            </div>
            
            <div class="row">
              <label for="recipe.description"><g:message code="recipe.description" /></label>
              <textarea maxlength="5000" name="recipe.description"></textarea>
            </div>
          </div>
        </div>
      </div>
    </body>
</html>
