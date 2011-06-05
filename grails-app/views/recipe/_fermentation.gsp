<h2><g:message code="recipe.edit.fermentation" /></h2>

<div class="form">
  <form id="fermentationForm">
    <input type="hidden" name="validate" value="RecipeFermentationCommand" />
    <input type="hidden" name="id" value="${it.id}" />
    <input type="hidden" name="tab" value="fermentation" />

    <div class="column50percent">
      <div class="row">
        <label for="recipe.yeast"><g:message code="recipe.yeast.label" /></label>
          <div class="errors" id="errors_cookingTime">
            <g:renderErrors bean="${it}" field="yeast" as="list" />
          </div>
        <input type="text" name="yeast" value="${it?.yeast?.encodeAsHTML()}" maxlength="255" onkeyup="$('#errors_yeast').slideUp(100)" />
      </div>

      <div class="row">
        <label for="recipe.fermentationTemperature"><g:message code="recipe.fermentationTemperature.label" /></label>
          <div class="errors" id="errors_fermentationTemperature">
            <g:renderErrors bean="${it}" field="fermentationTemperature" as="list" />
          </div>
        <input class="small" type="text" name="fermentationTemperature" value="${formatNumber(format:'###', number:it?.fermentationTemperature)}" maxlength="10" onkeyup="$('#errors_fermentationTemperature').slideUp(100)" />
      </div>

      <div class="row">
        <label for="recipe.alcohol"><g:message code="recipe.alcohol.label" /></label>
          <div class="errors" id="errors_alcohol">
            <g:renderErrors bean="${it}" field="alcohol" as="list" />
          </div>
        <input class="small" type="text" name="alcohol" value="${formatNumber(format:'###', number:it?.alcohol)}" maxlength="10" onkeyup="$('#errors_alcohol').slideUp(100)" />
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="recipe.bottlingWort"><g:message code="recipe.bottlingWort.label" /></label>
          <div class="errors" id="errors_bottlingWort">
            <g:renderErrors bean="${it}" field="bottlingWort" as="list" />
          </div>
        <input class="small" type="text" name="bottlingWort" value="${formatNumber(format:'####.0#', number:it?.bottlingWort)}" maxlength="10" onkeyup="$('#errors_bottlingWort').slideUp(100)" />
      </div>

      <div class="row">
        <label for="recipe.storingTime"><g:message code="recipe.storingTime.label" /></label>
          <div class="errors" id="errors_storingTime">
            <g:renderErrors bean="${it}" field="storingTime" as="list" />
          </div>
        <input class="small" type="text" name="storingTime" value="${formatNumber(format:'####', number:it?.storingTime)}" maxlength="10" onkeyup="$('#errors_storingTime').slideUp(100)" />
      </div>

      <div class="row">
        <label for="recipe.storingTemperature"><g:message code="recipe.storingTemperature.label" /></label>
          <div class="errors" id="errors_storingTemperature">
            <g:renderErrors bean="${it}" field="storingTemperature" as="list" />
          </div>
        <input class="small" type="text" name="storingTemperature" value="${formatNumber(format:'####', number:it?.storingTemperature)}" maxlength="10" onkeyup="$('#errors_storingTemperature').slideUp(100)" />
      </div>
    </div>

    <div class="buttonbar">
      <g:if test="${it?.id}">
        <input class="ui-button ui-state-default right" type="button" name="fermentationSubmitButton" id="submitFermentationButton" value="${message(code:'recipe.edit.save')}" />
      </g:if>
      <g:else>
        <input type="hidden" name="finalSave" value="0" />
        <input class="ui-button ui-state-default right" type="button" name="fermentationSubmitButton" id="submitFermentationButton" value="${message(code:'recipe.edit.save')}" />
      </g:else>    
    </div>
  </form>
</div>

<script type="text/javascript">
$(document).ready(function() {
try {
  $('#fermentationForm').submit(booze.recipe.submit);
  <g:if test="${it.id}">
    $('#submitFermentationButton').click(booze.recipe.submit);
  </g:if>
  <g:else>
    $('#submitFermentationButton').click({finalSave: true}, booze.recipe.submit);
  </g:else>
}catch(e){console.log(e);}
});

</script>