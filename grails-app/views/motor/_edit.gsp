<h1>
  <g:if test="${motor.id}">
    <g:message code="setting.motor.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.motor.add" />
  </g:else>
</h1>

<div class="form">
  <form id="motorsTab_deviceEditorForm">
    <g:if test="${motor.id}">
      <input type="hidden" name="motor.id" value="${motor.id}" />
    </g:if>

    <input type="hidden" name="setting.id" value="${setting.id}" />

    <div class="row">
      <label for="motor.name"><g:message code="motorDevice.name.label" /></label>
      <div class="errors" id="motorsTab_errors_name">
         <g:renderErrors bean="${motor}" field="name" as="list" />
      </div>
      <input type="text" name="motor.name" value="${fieldValue(bean: motor, field:'name')}" maxlength="254" onkeyup="$('#motorsTab_errors_name').slideUp(100)" />
    </div>

    <div class="row">
      <label for="motor.driver"><g:message code="motorDevice.driver.label" /></label>
      <div class="errors" id="motorsTab_errors_driver">
         <g:renderErrors bean="${motor}" field="driver" as="list" />
      </div>
      <select id="motorsTab_driverSelector" name="motor.driver" onclick="$('#motorsTab_errors_driver').slideUp(100)">
        <option value="" <g:if test="${!motor.driver}">selected="selected"</g:if> ></option>
        <g:each in="${drivers}" var="driver">
          <option value="${driver.encodeAsHTML()}"
            <g:if test="${driver == motor.driver}">
              selected="selected"
            </g:if> >
            ${driver.tokenize(".").last().encodeAsHTML()}
          </option>
        </g:each>
      </select>
    </div>
    
    <div class="row">
      <label for="motor.regulator"><g:message code="motorDevice.regulator.label" /></label>
      <div class="errors" id="motorsTab_errors_regulator">
         <g:renderErrors bean="${motor}" field="regulator" as="list" />
      </div>
      
      <a <g:if test="${!motor.regulator}">style="display: none"</g:if> id="motorsTab_regulatorNameHref" href='#' onclick="booze.setting.editRegulator('motor', $('#motorsTab_deviceEditorForm').serialize()); return false;"><g:fieldValue bean="${motor.regulator}" field="name" /></a>
      <a <g:if test="${motor.regulator}">style="display: none"</g:if> id="motorsTab_noRegulatorHref" href='#' onclick="booze.setting.editRegulator('motor', $('#motorsTab_deviceEditorForm').serialize()); return false;"><g:message code="setting.motor.edit.addRegulator" /></a>
      
      <input type="hidden" id="motorsTab_hasRegulatorField" name="motor.hasRegulator" value="<g:if test='${motor.regulator}'>1</g:if><g:else>0</g:else>" />
      <input type="hidden" id="motorsTab_regulatorNameField" name="regulator.name" value="${fieldValue(bean: motor.regulator, field:'name')}" />
      <input type="hidden" id="motorsTab_regulatorSoftOnField" name="regulator.softOn" value="${fieldValue(bean: motor.regulator, field:'softOn')}" />
      <input type="hidden" id="motorsTab_regulatorOptionsField" name="regulator.options" value="${fieldValue(bean: motor.regulator, field:'options')}" />
      <input type="hidden" id="motorsTab_regulatorDriverField" name="regulator.driver" value="${fieldValue(bean: motor.regulator, field:'driver')}" />
    </div>

    <div id="motorsTab_driverOptions">
      <g:if test="${motor.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: motor.driver]}" />
      </g:if>
    </div>
    
    <div class="buttonbar">
      <input class="left ui-button ui-state-default" type="submit" name="saveMotor" value="Speichern" />
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditDevice()" name="cancel" value="Abbrechen" />
    </div>
  </form>
</div>

<g:javascript>
  $('#motorsTab_driverSelector').change(booze.setting.fetchDriverOptions)
  $('#motorsTab_deviceEditorForm').submit({type: 'motor'}, booze.setting.saveDevice);
  
</g:javascript>

  