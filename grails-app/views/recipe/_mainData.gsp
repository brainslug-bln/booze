<h2><g:message code="recipe.edit.mainData" /></h2>

<div class="form">
  <form id="mainDataForm">
    
    <input type="hidden" name="validate" value="RecipeMainDataCommand" />
    <input type="hidden" name="id" value="${it.id}" />
    <input type="hidden" name="tab" value="mainData" />

    <div class="column50percent">
      <div class="row">
          <label for="recipe.name"><g:message code="recipe.name.label" /></label>
            <div class="errors" id="errors_name">
               <g:renderErrors bean="${it}" field="name" as="list" />
            </div>
          <input type="text" name="name" value="${fieldValue(bean:it, field:'name')}" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
      </div>

      <div class="row">
          <label for="recipe.dateCreated"><g:message code="recipe.dateCreated.label" /></label>
          <span class="immutable"><g:formatDate format="dd.MM.yyyy" date="${it?.dateCreated?it.dateCreated:(new Date())}" /></span>
      </div>

      <div class="row">
        <label for="recipe.author"><g:message code="recipe.author.label" /></label>
        <span class="immutable"><g:if test="${recipe?.author}"><g:fieldValue bean="${it}" field="author" /></g:if><g:else><g:message code="recipe.author.self" /></g:else></span>
      </div>

      <div class="row">
        <label for="recipe.description"><g:message code="recipe.description.label" /></label>
          <div class="errors" id="errors_description">
             <g:renderErrors bean="${it}" field="description" as="list" />
          </div>
        <textarea maxlength="5000" rows="3" name="description" onkeyup="$('#errors_description').slideUp(100)"><g:fieldValue bean="${it}" field="description" /></textarea>
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="recipe.finalBeerVolume"><g:message code="recipe.finalBeerVolume.label" /></label>
        <span class="immutable">
          <g:if test="${it?.finalBeerVolume}">
            <g:formatNumber number="${it?.finalBeerVolume}" format="####.#" />
          </g:if>
          <g:else>
            <g:message code="recipe.valueNotComputable" />
          </g:else>
        </span> 
      </div>

      <div class="row">
        <label for="recipe.originalWort"><g:message code="recipe.originalWort" /></label>
        <span class="immutable">
          <g:if test="${it?.originalWort}">
            <g:formatNumber number="${it?.originalWort}" format="####.0#" />
          </g:if>
          <g:else>
            <g:message code="recipe.valueNotAvailable" />
          </g:else>
        </span> 
      </div>

      <div class="row">
        <label for="recipe.ibu"><g:message code="recipe.ibu.label" /></label>
        <span class="immutable">
          <g:if test="${it?.ibu}">
            <g:formatNumber number="${it?.ibu}" format="####.0#" />
          </g:if>
          <g:else>
            <g:message code="recipe.valueNotComputable" />
          </g:else>
        </span> 
      </div>

      <div class="row">
        <label for="recipe.ebc"><g:message code="recipe.ebc.label" /></label>
        <span class="immutable">
          <g:if test="${it?.ebc}">
            <g:formatNumber number="${it?.ebc}" format="####.0#" />
          </g:if>
          <g:else>
            <g:message code="recipe.valueNotComputable" />
          </g:else>
        </span> 
      </div>

      <div class="row">
        <label for="recipe.alcohol"><g:message code="recipe.alcohol.label" /></label>
        <span class="immutable">
          <g:if test="${it?.alcohol}">
            <g:formatNumber number="${it?.alcohol}" format="####.0#" />
          </g:if>
          <g:else>
            <g:message code="recipe.valueNotAvailable" />
          </g:else>
        </span> 
      </div>
    </div>
    
    <div class="buttonbar">
      <g:if test="${it?.id}">
        <input class="ui-button ui-state-default right" type="button" id="submitMainDataButton" value="${message(code:'recipe.edit.save')}" />
      </g:if>
      <g:else>
        <input class="ui-button ui-state-default right" type="button" id="submitMainDataButton" value="${message(code:'recipe.create.next')}" />
      </g:else>    
    </div>
  </form>
</div>

<script type="text/javascript">
$(document).ready(function() {
  $('#mainDataForm').submit(booze.recipe.submit);
  $('#submitMainDataButton').click(booze.recipe.submit);
});

</script>
