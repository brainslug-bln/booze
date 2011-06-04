<h2><g:message code="recipe.edit.mashing" /></h2>

<div class="form">
  <form id="mashingForm" method="post" action="${createLink(controller:'recipe', action:'save')}">
  
    <input type="hidden" name="validate" value="RecipeMashingCommand" />
    <input type="hidden" name="id" value="${it.id}" />
    <input type="hidden" name="tab" value="mashing" />

    <div class="column50percent">
      <div class="row">
          <label for="recipe.mashingWaterVolume"><g:message code="recipe.mashingWaterVolume.label" /></label>
            <div class="errors" id="errors_mashingWaterVolume">
               <g:renderErrors bean="${it}" field="mashingWaterVolume" as="list" />
            </div>
          <input class="small" type="text" name="mashingWaterVolume" value="${formatNumber(format:'####.##', number:it?.mashingWaterVolume)}" maxlength="10" onkeyup="$('#errors_mashingWaterVolume').slideUp(100)" />
      </div>

      <div class="row">
          <label for="recipe.spargingWaterVolume"><g:message code="recipe.spargingWaterVolume.label" /></label>
            <div class="errors" id="errors_spargingWaterVolume">
               <g:renderErrors bean="${it}" field="spargingWaterVolume" as="list" />
            </div>
          <input class="small" type="text" name="spargingWaterVolume" value="${formatNumber(format:'####.##', number:it?.spargingWaterVolume)}" maxlength="10" onkeyup="$('#errors_spargingWaterVolume').slideUp(100)" />
      </div>

      <div class="row">
          <label for="recipe.spargingTemperature"><g:message code="recipe.spargingTemperature.label" /></label>
            <div class="errors" id="errors_spargingTemperature">
               <g:renderErrors bean="${it}" field="spargingTemperature" as="list" />
            </div>
          <input class="small" type="text" name="spargingTemperature" value="${formatNumber(format:'####.##', number:it?.spargingTemperature)}" maxlength="10" onkeyup="$('#errors_spargingTemperature').slideUp(100)" />
      </div>
    </div>


    <div class="column50percent">
      <div class="row">
          <label for="recipe.doColdMashing"><g:message code="recipe.doColdMashing.label" /></label>
            <div class="errors" id="errors_doColdMashing">
               <g:renderErrors bean="${it}" field="doColdMashing" as="list" />
            </div>
          <div class="radioset" id="doColdMashing">
            <input type="radio" onclick="$('#mashingTemperature').slideUp(200)" id="doColdMashingYes" name="doColdMashing" value="true" <g:if test="${it?.doColdMashing}">checked</g:if> /> <label for="doColdMashingYes"><g:message code="form.yes" /></label>
            <input type="radio" onclick="$('#mashingTemperature').slideDown(200)" id="doColdMashingNo" name="doColdMashing" value="false" <g:if test="${!(it?.doColdMashing)}">checked</g:if> /> <label for="doColdMashingNo"><g:message code="form.no" /></label>
          </div>
      </div>

      <div class="row">
        <div id="mashingTemperature" <g:if test="${it?.doColdMashing}">style="display:none;"</g:if> >
          <label for="recipe.mashingTemperature"><g:message code="recipe.mashingTemperature.label" /></label>
            <div class="errors" id="errors_mashingTemperature">
               <g:renderErrors bean="${it}" field="mashingTemperature" as="list" />
            </div>
          <input class="small" type="text" name="mashingTemperature" value="${formatNumber(format:'####.##', number:it?.mashingTemperature)}" maxlength="10" onkeyup="$('#errors_mashingTemperature').slideUp(100)" />
        </div>
      </div>

      <div class="row">
          <label for="recipe.lauterTemperature"><g:message code="recipe.lauterTemperature.label" /></label>
            <div class="errors" id="errors_lauterTemperature">
               <g:renderErrors bean="${it}" field="lauterTemperature" as="list" />
            </div>
          <input class="small" type="text" name="lauterTemperature" value="${formatNumber(format:'####.##', number:it?.lauterTemperature)}" maxlength="10" onkeyup="$('#errors_lauterTemperature').slideUp(100)" />
      </div>
    </div>

    <div class="clear"></div>

    <div class="column50percent">
      <div class="row">
          <label for="recipe.preSpargingWort"><g:message code="recipe.preSpargingWort.label" /></label>
            <div class="errors" id="errors_preSpargingWort">
               <g:renderErrors bean="${it}" field="preSpargingWort" as="list" />
            </div>
          <input class="small" type="text" name="preSpargingWort" value="${formatNumber(format:'####.##', number:it?.preSpargingWort)}" maxlength="10" onkeyup="$('#errors_preSpargingWort').slideUp(100)" />
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
          <label for="recipe.postSpargingWort"><g:message code="recipe.postSpargingWort.label" /></label>
            <div class="errors" id="errors_postSpargingWort">
               <g:renderErrors bean="${it}" field="postSpargingWort" as="list" />
            </div>
          <input class="small" type="text" name="postSpargingWort" value="${formatNumber(format:'####.##', number:it?.postSpargingWort)}" maxlength="10" onkeyup="$('#errors_postSpargingWort').slideUp(100)" />
      </div>
    </div>

    <div class="clear">&nbsp;</div>

    <table>
        <caption><g:message code="recipe.edit.malts" /></caption>
        <thead>
          <tr class="ui-widget-header">
            <th><g:message code="malt.name.label" /></th>
            <th style="width: 20%;"><g:message code="malt.ebc.label" /></th>
            <th style="width: 20%;"><g:message code="malt.amount.label" /></th>
            <th style="width: 4%;"><a href="#" onclick="booze.form.insertRow(this, 'maltTemplate'); return false;" class="ui-icon-circle-plus ui-icon" title="${message(code: 'recipe.edit.create')}"></a></th>
          </tr>
        </thead>
        <tbody>
          <g:if test="${it?.malts?.size() < 1}">
            <tr class="ui-widget-content">
              <td><input type="text" value="" name="malts[0].name" /></td>
              <td><input type="text" value="" name="malts[0].ebc" /></td>
              <td><input type="text" value="" name="malts[0].amount" /></td>
              <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
            </tr>
          </g:if>

          <g:each in="${it?.malts?.sort{it.name}}" var="malt" status="i">
            <%
              // We have to manually validate for some reason...
              malt?.validate()
            %>

            <tr class="ui-widget-content <g:hasErrors bean="${malt}">ui-state-error</g:hasErrors>" >
              <td><input type="text" value="${malt.name?.encodeAsHTML()}" name="malts[${i}].name" /></td>
              <td><input type="text" value="${formatNumber(format: '##0.0#', number: malt.ebc)}" name="malts[${i}].ebc" /></td>
              <td><input type="text" value="${formatNumber(format: '##0.0#', number: malt.amount)}" name="malts[${i}].amount" /></td>
              <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
            </tr>

          </g:each>
        </tbody>
    </table>

    <table>
        <caption><g:message code="recipe.edit.rests" /></caption>
        <thead>
          <tr class="ui-widget-header">
            <th><g:message code="rest.comment.label" /></th>
            <th style="width: 20%;"><g:message code="rest.temperature.label" /></th>
            <th style="width: 20%;"><g:message code="rest.duration.label" /></th>
            <th style="width: 4%;"><a href="#" onclick="booze.form.insertRow(this, 'restTemplate'); return false;" class="ui-icon-circle-plus ui-icon" title="${message(code: 'recipe.edit.create')}"></a></th>
          </tr>
        </thead>
        <tbody>
          <g:if test="${it?.rests?.size() < 1}">
            <tr class="ui-widget-content">
              <td>
                <input class="index" type="hidden" name="rests[0].indexInRests" value="0"/>
                <input type="text" value="" name="rests[0].comment" />
              </td>
              <td><input type="text" value="" name="rests[0].temperature" /></td>
              <td><input type="text" value="" name="rests[0].duration" /></td>
              <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
            </tr>
          </g:if>

          <g:each in="${it?.rests?.sort{it?.indexInRests}}" var="rest" status="i">
            <%
              // We have to manually validate for some reason...
              rest?.validate()
            %>

            <tr class="ui-widget-content <g:hasErrors bean="${rest}">ui-state-error</g:hasErrors>" >
              <td>
                <input type="text" value="${rest.comment?.encodeAsHTML()}" name="rests[${i}].comment" />
                <input class="index" type="hidden" name="rests[${i}].indexInRests" value="${i}"/>
              </td>
              <td><input type="text" value="${formatNumber(format: '##0.0#', number: rest.temperature)}" name="rests[${i}].temperature" /></td>
              <td><input type="text" value="${formatNumber(format: '##0.0#', number: rest.duration)}" name="rests[${i}].duration" /></td>
              <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
            </tr>

          </g:each>
        </tbody>
    </table>

    <div class="buttonbar">
      <g:if test="${it?.id}">
        <input class="ui-button ui-state-default" type="button" id="saveMashingButton" value="${message(code:'recipe.edit.save')}" />
      </g:if>
      <g:else>
        <input class="ui-button ui-state-default" type="submit" id="proceedMashingButton" value="${message(code:'recipe.create.next')}" />
      </g:else>    
    </div>
  </form>
</div>

<script id="maltTemplate" type="text/x-jquery-tmpl">
  <tr class="ui-widget-content">
    <td><input type="text" value="" name="malts[{{= index }}].name" /></td>
    <td><input type="text" value="" name="malts[{{= index }}].ebc" /></td>
    <td><input type="text" value="" name="malts[{{= index }}].amount" /></td>
    <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
  </tr>
</script>

<script id="restTemplate" type="text/x-jquery-tmpl">
  <tr class="ui-widget-content">
    <td>
      <input type="text" value="" name="rests[{{= index }}].comment" />
      <input class="index" type="hidden" name="rests[{{= index }}].indexInRests" value="{{= index }}"/>
    </td>
    <td><input type="text" value="" name="rests[{{= index }}].temperature" /></td>
    <td><input type="text" value="" name="rests[{{= index }}].duration" /></td>
    <td><a href="#" onclick="booze.form.deleteRow(this); return false;" class="ui-icon-circle-minus ui-icon" title="${message(code: 'recipe.edit.delete')}"></a></td>
  </tr>
</script>

<script type="text/javascript">
$(function() {
        $( "#doColdMashing" ).buttonset();
});


<g:if test="${it.id}">
  $(document).ready(function() {
    $('#mashingForm').submit(booze.recipeEdit.submit);
  });
</g:if>

<g:else>
  $(document).ready(function() {
    $('#mashingForm').submit(booze.recipeCreate.submit);
  });
</g:else>

</script>