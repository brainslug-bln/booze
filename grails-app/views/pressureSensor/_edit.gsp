<h1>
  <g:if test="${pressureSensor.id}">
    <g:message code="setting.pressureSensor.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.pressureSensor.add" />
  </g:else>
</h1>

<div class="form">
  <form id="pressureSensorsTab_deviceEditorForm">
    <g:if test="${pressureSensor.id}">
      <input type="hidden" name="pressureSensor.id" value="${pressureSensor.id}" />
    </g:if>

    <input type="hidden" name="setting.id" value="${setting.id}" />

    <div class="row">
      <label for="pressureSensor.name"><g:message code="pressureSensorDevice.name.label" /></label>
      <div class="errors" id="pressureSensorsTab_errors_name">
         <g:renderErrors bean="${pressureSensor}" field="name" as="list" />
      </div>
      <input type="text" name="pressureSensor.name" value="${fieldValue(bean: pressureSensor, field:'name')}" maxlength="254" onkeyup="$('#pressureSensorsTab_errors_name').slideUp(100)" />
    </div>
    
    <div class="row">
      <label for="pressureSensor.pressureMaxLimit"><g:message code="pressureSensorDevice.pressureMaxLimit.label" /></label>
      <div class="errors" id="pressureSensorsTab_errors_pressureMaxLimit">
         <g:renderErrors bean="${pressureSensor}" field="pressureMaxLimit" as="list" />
      </div>
      <input type="text" name="pressureSensor.pressureMaxLimit" value="${fieldValue(bean: pressureSensor, field:'pressureMaxLimit')}" maxlength="254" onkeyup="$('#pressureSensorsTab_errors_pressureMaxLimit').slideUp(100)" />
    </div>

    <div class="row">
      <label for="pressureSensor.driver"><g:message code="pressureSensorDevice.driver.label" /></label>
      <div class="errors" id="pressureSensorsTab_errors_driver">
         <g:renderErrors bean="${pressureSensor}" field="driver" as="list" />
      </div>
      <select id="pressureSensorsTab_driverSelector" name="pressureSensor.driver" onclick="$('#pressureSensorsTab_errors_driver').slideUp(100)">
        <option value="" <g:if test="${!pressureSensor.driver}">selected="selected"</g:if> ></option>
        <g:each in="${drivers}" var="driver">
          <option value="${driver.encodeAsHTML()}"
            <g:if test="${driver == pressureSensor.driver}">
              selected="selected"
            </g:if> >
            ${driver.tokenize(".").last().encodeAsHTML()}
          </option>
        </g:each>
      </select>
    </div>

    <div id="pressureSensorsTab_driverOptions">
      <g:if test="${pressureSensor.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: pressureSensor.driver]}" />
      </g:if>
    </div>
    
    <div class="buttonbar">
      <input class="left ui-button ui-state-default" type="submit" name="savePressureSensor" value="Speichern" />
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditDevice('pressureSensor_')" name="cancel" value="Abbrechen" />
    </div>
  </form>
</div>

<g:javascript>
  $('#pressureSensorsTab_driverSelector').change(booze.setting.fetchDriverOptions)
  $('#pressureSensorsTab_deviceEditorForm').submit({type: 'pressureSensor'}, booze.setting.saveDevice);
  
</g:javascript>

  