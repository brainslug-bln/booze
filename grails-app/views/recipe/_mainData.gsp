<h2><g:message code="recipe.edit.commonData" /></h2>

<div class="form">
  <div class="column50percent">
    <div class="row">
        <label for="recipe.name"><g:message code="recipe.name" /></label>
        <input type="text" name="recipe.name" value="" maxlength="254" />
        <span class="error" id="error_name"></span>
    </div>

    <div class="row">
        <label for="recipe.dateCreated"><g:message code="recipe.dateCreated" /></label>
        <span class="immutable">XX.XX.XXXX</span>
    </div>
    
    <div class="row">
      <label for="recipe.author"><g:message code="recipe.author" /></label>
      <span class="immutable">AutorName</span>
    </div>

    <div class="row">
      <label for="recipe.description"><g:message code="recipe.description" /></label>
      <textarea maxlength="5000" name="recipe.description"></textarea>
    </div>
  </div>
  
  <div class="column50percent">
    <div class="row">
      <label for="recipe.finalBeerVolume"><g:message code="recipe.finalBeerVolume" /></label>
      <span class="immutable">finalBeerVolume</span> 
    </div>
    
    <div class="row">
      <label for="recipe.originalWort"><g:message code="recipe.originalWort" /></label>
      <span class="immutable">originalWort</span> 
    </div>
    
    <div class="row">
      <label for="recipe.ibu"><g:message code="recipe.ibu" /></label>
      <span class="immutable">ibu</span> 
    </div>
    
    <div class="row">
      <label for="recipe.ebu"><g:message code="recipe.ebu" /></label>
      <span class="immutable">EBU</span> 
    </div>
    
    <div class="row">
      <label for="recipe.alcohol"><g:message code="recipe.alcohol" /></label>
      <span class="immutable">Alcohol</span> 
    </div>
  </div>
</div>
