<h2><g:message code="recipe.edit.brew" /></h2>

<form id="brewForm">
    <input type="hidden" name="id" value="${it.id}" />
    <input type="hidden" name="tab" value="brew" />
  
</form>

<p style="float: left; width: 50%">"${it.name.encodeAsHTML()}" wirklich brauen?</p>

<div class="left" style="float: left; width: 30%;">
  <input type="button" style="float: right" class="ui-button ui-state-default right" onclick="window.location.href='${createLink(controller:"brew", action:"init", params:[recipe: it.id])}'" value="Ja, jetzt brauen"/>
</div>