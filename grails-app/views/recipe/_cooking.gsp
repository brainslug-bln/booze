<h2><g:message code="recipe.edit.cooking" /></h2>

<div class="form">
  <input type="hidden" name="validate" value="RecipeCookingCommand" />
  
  <div class="column50percent">
    <div class="row">
        <label for="recipe.cookingTime"><g:message code="recipe.cookingTime.label" /></label>
        <g:hasErrors bean="${it}" field="cookingTime">
          <div class="errors" id="errors_cookingTime">
             <g:renderErrors bean="${it}" field="cookingTime" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.cookingTime" value="${formatNumber(format:'####', number:it?.cookingTime)}" maxlength="10" onkeyup="$('#errors_cookingTime').slideUp(100)" />
    </div>
  </div>

  <div class="column50percent">
    <div class="row">
        <label for="recipe.originalWort"><g:message code="recipe.originalWort.label" /></label>
        <g:hasErrors bean="${it}" field="originalWort">
          <div class="errors" id="errors_originalWort">
             <g:renderErrors bean="${it}" field="originalWort" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.originalWort" value="${formatNumber(format:'####.0#', number:it?.originalWort)}" maxlength="10" onkeyup="$('#errors_originalWort').slideUp(100)" />
    </div>

    <div class="row">
        <label for="recipe.postIsomerization"><g:message code="recipe.postIsomerization.label" /></label>
        <g:hasErrors bean="${it}" field="postIsomerization">
          <div class="errors" id="errors_postIsomerization">
             <g:renderErrors bean="${it}" field="postIsomerization" as="list" />
          </div>
        </g:hasErrors>
        <input class="small" type="text" name="recipe.postIsomerization" value="${formatNumber(format:'####.0#', number:it?.postIsomerization)}" maxlength="10" onkeyup="$('#errors_postIsomerization').slideUp(100)" />
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
            <td><input type="text" value="" name="hops[0].comment" /></td>
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
          
          <tr class="ui-widget-content <g:hasError bean="${hop}">ui-state-error</g:hasError>" >
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
</div>

<script id="hopTemplate" type="text/x-jquery-tmpl">
  <tr class="ui-widget-content">
    <td><input type="text" value="" name="hops[{{= index }}].name" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].percentAlpha" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].amount" /></td>
    <td><input type="text" value="" name="hops[{{= index }}].time" /></td>
    <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
  </tr>
</script>