<h2><g:message code="recipe.edit.cooking" /></h2>

<div class="form">
  <form id="cookingForm">

    <input type="hidden" name="validate" value="RecipeCookingCommand" />
    <input type="hidden" name="id" value="${it.id}" />
    <input type="hidden" name="tab" value="cooking" />

    <div class="column50percent">
      <div class="row">
        <label for="recipe.cookingTime"><g:message code="recipe.cookingTime.label" /></label>
        <div class="errors" id="errors_cookingTime">
          <g:renderErrors bean="${it}" field="cookingTime" as="list" />
        </div>
        <input class="small" type="text" name="cookingTime" value="${formatNumber(format:'####', number:it?.cookingTime)}" maxlength="10" onkeyup="$('#errors_cookingTime').slideUp(100)" />
      </div>
      
      <div class="row">
        <label for="recipe.ibu"><g:message code="recipe.ibu.label" /></label>
        <span class="immutable"><g:formatNumber format="##0.0#" number="${it?.ibu}" /></span>
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="recipe.originalWort"><g:message code="recipe.originalWort.label" /></label>
        <div class="errors" id="errors_originalWort">
          <g:renderErrors bean="${it}" field="originalWort" as="list" />
        </div>
        <input class="small" type="text" name="originalWort" value="${formatNumber(format:'###0.0#', number:it?.originalWort)}" maxlength="10" onkeyup="$('#errors_originalWort').slideUp(100)" />
      </div>

      <div class="row">
        <label for="recipe.postIsomerization"><g:message code="recipe.postIsomerization.label" /></label>
        <div class="errors" id="errors_postIsomerization">
          <g:renderErrors bean="${it}" field="postIsomerization" as="list" />
        </div>
        <input class="small" type="text" name="postIsomerization" value="${formatNumber(format:'###0.0#', number:it?.postIsomerization)}" maxlength="10" onkeyup="$('#errors_postIsomerization').slideUp(100)" />
      </div>
    </div>

    <table>
      <caption><g:message code="recipe.edit.hops" /></caption>
      <thead>
        <tr class="ui-widget-header">
          <th><g:message code="hop.name.label" /></th>
      <th style="width: 15%;"><g:message code="hop.percentAlpha.label" /></th>
      <th style="width: 15%;"><g:message code="hop.amount.label" /></th>
      <th style="width: 15%;"><g:message code="hop.time.label" /></th>
      <th style="width: 4%;"><a href="#" onclick="booze.form.insertRow(this, 'hopTemplate'); return false;" class="ui-icon-circle-plus ui-icon" title="${message(code: 'recipe.edit.create')}"></a></th>
      </tr>
      </thead>
      <tbody>
      <g:if test="${it?.hops?.size() < 1}">
        <tr class="ui-widget-content">
          <td><input type="text" value="" name="hops[0].name" /></td>
          <td><input type="text" value="" name="hops[0].percentAlpha" /></td>
          <td><input type="text" value="" name="hops[0].amount" /></td>
          <td><input type="text" value="" name="hops[0].time" /></td>
          <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
        </tr>
      </g:if>

      <g:each in="${it?.hops?.sort{it?.time}}" var="hop" status="i">
<%
// We have to manually validate for some reason...
hop?.validate()
%>

        <tr class="ui-widget-content <g:hasErrors bean="${hop}">ui-state-error</g:hasErrors>" >
          <td>
            <input type="text" value="${hop.name?.encodeAsHTML()}" name="hops[${i}].name" />
          </td>
          <td><input type="text" value="${formatNumber(format: '##0.0#', number: hop.percentAlpha)}" name="hops[${i}].percentAlpha" /></td>
          <td><input type="text" value="${formatNumber(format: '##0', number: hop.amount)}" name="hops[${i}].amount" /></td>
          <td><input type="text" value="${formatNumber(format: '##0', number: hop.time)}" name="hops[${i}].time" /></td>
          <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
        </tr>

      </g:each>
      </tbody>
    </table>

    <div class="buttonbar">
      <g:if test="${it?.id}">
        <input class="ui-button ui-state-default right" type="button" id="submitCookingButton" value="${message(code:'recipe.edit.save')}" />
      </g:if>
      <g:else>
        <input class="ui-button ui-state-default right" type="button" id="submitCookingButton" value="${message(code:'recipe.create.next')}" />
      </g:else>    
    </div>

  </form>
</div>

<script type="text/javascript">
$(document).ready(function() {
  $('#cookingForm').submit(booze.recipe.submit);
  $('#submitCookingButton').click(booze.recipe.submit);
});

</script>

<script id="hopTemplate" type="text/x-jquery-tmpl">
  <tr class="ui-widget-content">
    <td><input type="text" value="" name="hops[{{= index }}].name" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].percentAlpha" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].amount" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].time" /></td>
    <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
  </tr>
</script>

