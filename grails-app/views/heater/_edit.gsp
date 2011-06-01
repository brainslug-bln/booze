<h1>
  <g:if test="${heater.id}">
    <g:message code="setting.heater.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.heater.add" />
  </g:else>
</h1>

<div class="form">
  <form id="heatersTab_deviceEditorForm">
    <g:if test="${heater.id}">
      <input type="hidden" name="heater.id" value="${heater.id}" />
    </g:if>

    <input type="hidden" name="setting.id" value="${setting.id}" />

    <div class="row">
      <label for="heater.name"><g:message code="heaterDevice.name.label" /></label>
      <div class="errors" id="heatersTab_errors_name">
         <g:renderErrors bean="${heater}" field="name" as="list" />
      </div>
      <input type="text" name="heater.name" value="${fieldValue(bean: heater, field:'name')}" maxlength="254" onkeyup="$('#heatersTab_errors_name').slideUp(100)" />
    </div>

    <div class="row">
      <label for="heater.driver"><g:message code="heaterDevice.driver.label" /></label>
      <div class="errors" id="heatersTab_errors_driver">
         <g:renderErrors bean="${heater}" field="driver" as="list" />
      </div>
      <select id="heatersTab_driverSelector" name="heater.driver" onclick="$('#heatersTab_errors_driver').slideUp(100)">
        <option value="" <g:if test="${!heater.driver}">selected="selected"</g:if> ></option>
        <g:each in="${drivers}" var="driver">
          <option value="${driver.encodeAsHTML()}"
            <g:if test="${driver == heater.driver}">
              selected="selected"
            </g:if> >
            ${driver.tokenize(".").last().encodeAsHTML()}
          </option>
        </g:each>
      </select>
    </div>
    
    <div class="row">
      <label for="heater.regulator"><g:message code="heaterDevice.regulator.label" /></label>
      <div class="errors" id="heatersTab_errors_regulator">
         <g:renderErrors bean="${heater}" field="regulator" as="list" />
      </div>
      
      <a <g:if test="${!heater.regulator}">style="display: none"</g:if> id="heatersTab_regulatorNameHref" href='#' onclick="booze.setting.editRegulator('heater', $('#heatersTab_deviceEditorForm').serialize()); return false;"><g:fieldValue bean="${heater.regulator}" field="name" /></a>
      <a <g:if test="${heater.regulator}">style="display: none"</g:if> id="heatersTab_noRegulatorHref" href='#' onclick="booze.setting.editRegulator('heater'); return false;"><g:message code="setting.heater.edit.addRegulator" /></a>
      
      <input type="hidden" id="heatersTab_hasRegulatorField" name="heater.hasRegulator" value="<g:if test='${heater.regulator}'>1</g:if><g:else>0</g:else>" />
      <input type="hidden" id="heatersTab_regulatorNameField" name="regulator.name" value="${fieldValue(bean: heater.regulator, field:'name')}" />
      <input type="hidden" id="heatersTab_regulatorOptionsField" name="regulator.options" value="${fieldValue(bean: heater.regulator, field:'options')}" />
      <input type="hidden" id="heatersTab_regulatorDriverField" name="regulator.driver" value="${fieldValue(bean: heater.regulator, field:'driver')}" />
    </div>

    <div id="heatersTab_driverOptions">
      <g:if test="${heater.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: heater.driver]}" />
      </g:if>
    </div>
    
    <div class="buttonbar">
      <input class="left ui-button ui-state-default" type="submit" name="saveHeater" value="Speichern" />
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditDevice()" name="cancel" value="Abbrechen" />
    </div>
  </form>
</div>

<g:javascript>
  $('#heatersTab_driverSelector').change(booze.setting.fetchDriverOptions)
  $('#heatersTab_deviceEditorForm').submit({type: 'heater'}, booze.setting.saveDevice);
  
</g:javascript>

  