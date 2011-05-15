<h2><g:message code="recipe.edit.mainData" /></h2>

<div class="form">
  <input type="hidden" name="validate" value="RecipeMainDataCommand" />
  
  <div class="column50percent">
    <div class="row">
        <label for="recipe.name"><g:message code="recipe.name.label" /></label>
        <g:hasErrors bean="${it}" field="name">
          <div class="errors" id="errors_name">
             <g:renderErrors bean="${it}" field="name" as="list" />
          </div>
        </g:hasErrors>
        <input type="text" name="recipe.name" value="" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
    </div>

    <div class="row">
        <label for="recipe.dateCreated"><g:message code="recipe.dateCreated.label" /></label>
        <span class="immutable"><g:formatDate format="dd.MM.yyyy" date="${it?.date?it.date:(new Date())}" /></span>
    </div>
    
    <div class="row">
      <label for="recipe.author"><g:message code="recipe.author.label" /></label>
      <span class="immutable"><g:if test="${recipe?.author}"><g:fieldValue bean="${it}" field="author" /></g:if><g:else><g:message code="recipe.author.self" /></g:else></span>
    </div>

    <div class="row">
      <label for="recipe.description"><g:message code="recipe.description.label" /></label>
      <g:hasErrors bean="${it}" field="description">
          <div class="errors" id="errors_description">
             <g:renderErrors bean="${it}" field="description" as="list" />
          </div>
        </g:hasErrors>
      <textarea maxlength="5000" rows="3" name="recipe.description" onkeyup="$('#errors_description').slideUp(100)">
        <g:fieldValue bean="${it}" field="description" />
      </textarea>
    </div>
  </div>
  
  <div class="column50percent">
    <div class="row">
      <label for="recipe.finalBeerVolume"><g:message code="recipe.finalBeerVolume.label" /></label>
      <span class="immutable">
        <g:if test="${$it?.finalBeerVolume}">
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
        <g:if test="${$it?.originalWort}">
          <g:formatNumber number="${it?.originalWort}" format="####.00" />
        </g:if>
        <g:else>
          <g:message code="recipe.valueNotAvailable" />
        </g:else>
      </span> 
    </div>
    
    <div class="row">
      <label for="recipe.ibu"><g:message code="recipe.ibu.label" /></label>
      <span class="immutable">
        <g:if test="${$it?.ibu}">
          <g:formatNumber number="${it?.ibu}" format="####.00" />
        </g:if>
        <g:else>
          <g:message code="recipe.valueNotComputable" />
        </g:else>
      </span> 
    </div>
    
    <div class="row">
      <label for="recipe.ebu"><g:message code="recipe.ebc.label" /></label>
      <span class="immutable">
        <g:if test="${$it?.ebc}">
          <g:formatNumber number="${it?.ebc}" format="####.00" />
        </g:if>
        <g:else>
          <g:message code="recipe.valueNotComputable" />
        </g:else>
      </span> 
    </div>
    
    <div class="row">
      <label for="recipe.alcohol"><g:message code="recipe.alcohol.label" /></label>
      <span class="immutable">
        <g:if test="${$it?.alcohol}">
          <g:formatNumber number="${it?.alcohol}" format="####.00" />
        </g:if>
        <g:else>
          <g:message code="recipe.valueNotAvailable" />
        </g:else>
      </span> 
    </div>
  </div>
</div>
