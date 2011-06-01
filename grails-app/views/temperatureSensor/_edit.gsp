<h1>
  <g:if test="${temperatureSensor.id}">
    <g:message code="setting.temperatureSensor.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.temperatureSensor.add" />
  </g:else>
</h1>

<div class="form">
  <form id="temperatureSensorsTab_deviceEditorForm">
    <g:if test="${temperatureSensor.id}">
      <input type="hidden" name="temperatureSensor.id" value="${temperatureSensor.id}" />
    </g:if>

    <input type="hidden" name="setting.id" value="${setting.id}" />

    <div class="row">
      <label for="temperatureSensor.name"><g:message code="temperatureSensorDevice.name.label" /></label>
      <div class="errors" id="temperatureSensorsTab_errors_name">
         <g:renderErrors bean="${temperatureSensor}" field="name" as="list" />
      </div>
      <input type="text" name="temperatureSensor.name" value="${fieldValue(bean: temperatureSensor, field:'name')}" maxlength="254" onkeyup="$('#temperatureSensorsTab_errors_name').slideUp(100)" />
    </div>

    <div class="row">
      <label for="temperatureSensor.driver"><g:message code="temperatureSensorDevice.driver.label" /></label>
      <div class="errors" id="temperatureSensorsTab_errors_driver">
         <g:renderErrors bean="${temperatureSensor}" field="driver" as="list" />
      </div>
      <select id="temperatureSensorsTab_driverSelector" name="temperatureSensor.driver" onclick="$('#temperatureSensorsTab_errors_driver').slideUp(100)">
        <option value="" <g:if test="${!temperatureSensor.driver}">selected="selected"</g:if> ></option>
        <g:each in="${drivers}" var="driver">
          <option value="${driver.encodeAsHTML()}"
            <g:if test="${driver == temperatureSensor.driver}">
              selected="selected"
            </g:if> >
            ${driver.tokenize(".").last().encodeAsHTML()}
          </option>
        </g:each>
      </select>
    </div>

    <div id="temperatureSensorsTab_driverOptions">
      <g:if test="${temperatureSensor.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: temperatureSensor.driver]}" />
      </g:if>
    </div>
    
    <div class="buttonbar">
      <input class="left ui-button ui-state-default" type="submit" name="saveTemperatureSensor" value="Speichern" />
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditDevice('temperatureSensor_')" name="cancel" value="Abbrechen" />
    </div>
  </form>
</div>

<g:javascript>
  $('#temperatureSensorsTab_driverSelector').change(booze.setting.fetchDriverOptions)
  $('#temperatureSensorsTab_deviceEditorForm').submit({type: 'temperatureSensor'}, booze.setting.saveDevice);
  
</g:javascript>

  