<h2><g:message code="recipe.edit.fermentation" /></h2>

<div class="form">
  <input type="hidden" name="validate" value="RecipeFermentationCommand" />
  
  <div class="column50percent">
    <div class="row">
        <label for="recipe.yeast"><g:message code="recipe.yeast.label" /></label>
        <g:hasErrors bean="${it}" field="yeast">
          <div class="errors" id="errors_cookingTime">
             <g:renderErrors bean="${it}" field="yeast" as="list" />
          </div>
        </g:hasErrors>
        <input type="text" name="recipe.yeast" value="${it?.yeast?.encodeAsHTML()}" maxlength="255" onkeyup="$('#errors_yeast').slideUp(100)" />
    </div>
    
    <div class="row">
        <label for="recipe.fermentationTemperature"><g:message code="recipe.fermentationTemperature.label" /></label>
        <g:hasErrors bean="${it}" field="fermentationTemperature">
          <div class="errors" id="errors_fermentationTemperature">
             <g:renderErrors bean="${it}" field="fermentationTemperature" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.fermentationTemperature" value="${formatNumber(format:'###', number:it?.fermentationTemperature)}" maxlength="10" onkeyup="$('#errors_fermentationTemperature').slideUp(100)" />
    </div>
    
    <div class="row">
        <label for="recipe.alcohol"><g:message code="recipe.alcohol.label" /></label>
        <g:hasErrors bean="${it}" field="alcohol">
          <div class="errors" id="errors_alcohol">
             <g:renderErrors bean="${it}" field="alcohol" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.alcohol" value="${formatNumber(format:'###', number:it?.alcohol)}" maxlength="10" onkeyup="$('#errors_alcohol').slideUp(100)" />
    </div>
  </div>

  <div class="column50percent">
    <div class="row">
        <label for="recipe.bottlingWort"><g:message code="recipe.bottlingWort.label" /></label>
        <g:hasErrors bean="${it}" field="bottlingWort">
          <div class="errors" id="errors_bottlingWort">
             <g:renderErrors bean="${it}" field="bottlingWort" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.bottlingWort" value="${formatNumber(format:'####.0#', number:it?.bottlingWort)}" maxlength="10" onkeyup="$('#errors_bottlingWort').slideUp(100)" />
    </div>

    <div class="row">
        <label for="recipe.storingTime"><g:message code="recipe.storingTime.label" /></label>
        <g:hasErrors bean="${it}" field="storingTime">
          <div class="errors" id="errors_storingTime">
             <g:renderErrors bean="${it}" field="storingTime" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.storingTime" value="${formatNumber(format:'####', number:it?.storingTime)}" maxlength="10" onkeyup="$('#errors_storingTime').slideUp(100)" />
    </div>

    <div class="row">
        <label for="recipe.storingTemperature"><g:message code="recipe.storingTemperature.label" /></label>
        <g:hasErrors bean="${it}" field="storingTemperature">
          <div class="errors" id="errors_storingTemperature">
             <g:renderErrors bean="${it}" field="storingTemperature" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.storingTemperature" value="${formatNumber(format:'####', number:it?.storingTemperature)}" maxlength="10" onkeyup="$('#errors_storingTemperature').slideUp(100)" />
    </div>
  </div>
</div>
