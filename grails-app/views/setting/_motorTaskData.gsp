<div class="row">
  <label for="setting.${type}.motor"><g:message code="motorTask.motor.label" /></label>
  <select name="setting.${type}.motor">
      <option onclick="$('#motorTask_${type}_regulationMode').hide()" value="" <g:if test="${ !setting[type]?.motor }">checked="checked"</g:if>></option>
    <g:each in="${setting.motors}" var="motor">
      <option onclick="<g:if test='${motor.regulator != null}'>$('#motorTask_${type}_regulationMode').show()</g:if><g:else>$('#motorTask_${type}_regulationMode').hide()</g:else>" value="${motor.id}" <g:if test="${motor.id == setting[type]?.motor?.id }">checked="checked"</g:if>>${motor.name.encodeAsHTML()}</option>
    </g:each>
  </select>
</div>

<div class="row">
  <label for="setting.${type}.mode"><g:message code="motorTask.mode.label" /></label>
  <div class="radioset" id="motorTask_${type}_mode">
    <input type="radio" onclick="$('#motorTask_${type}_interval').hide();" id="motorTask_${type}_mode_on" name="setting.${type}.mode.mode" value="${de.booze.backend.grails.MotorDeviceMode.MODE_ON}" <g:if test="${!setting[type]?.mode?.mode || setting[type]?.mode?.mode == de.booze.backend.grails.MotorDeviceMode.MODE_ON}">checked="checked"</g:if> /> <label for="motorTask_${type}_mode_on"><g:message code="motorDeviceMode.mode.${de.booze.backend.grails.MotorDeviceMode.MODE_ON}" /></label>
    <input type="radio" onclick="$('#motorTask_${type}_interval').show();" id="motorTask_${type}_mode_interval" name="setting.${type}.mode.mode" value="${de.booze.backend.grails.MotorDeviceMode.MODE_INTERVAL}" <g:if test="${setting[type]?.mode?.mode == de.booze.backend.grails.MotorDeviceMode.MODE_INTERVAL}">checked="checked"</g:if> /> <label for="motorTask_${type}_mode_interval"><g:message code="motorDeviceMode.mode.${de.booze.backend.grails.MotorDeviceMode.MODE_INTERVAL}" /></label>
  </div>
</div>

<div class="row" id="motorTask_${type}_interval" <g:if test="${setting[type]?.mode?.mode != de.booze.backend.grails.MotorDeviceMode.MODE_INTERVAL}">style="display: none"</g:if>>
  <div class="column50percent">
    <label for="setting.${type}.mode.onInterval"><g:message code="motorDeviceMode.onInterval.label" /></label>
    <input type="text" class="small" name="setting.${type}.mode.onInterval" value="${setting[type]?.mode?.onInterval}" />
  </div>
  <div class="column50percent">
    <label for="setting.${type}.mode.offInterval"><g:message code="motorDeviceMode.offInterval.label" /></label>
    <input type="text" class="small" name="setting.${type}.mode.offInterval" value="${setting[type]?.mode?.offInterval}" />
  </div>
</div>

<div class="row" id="motorTask_${type}_regulationMode" <g:if test="${!setting[type]?.motor?.regulator}">style="display: none"</g:if>>
  <label for="setting.${type}.regulationMode"><g:message code="motorTask.regulationMode.label" /></label>
  <div class="radioset" id="motorTask_${type}_regulationMode">
    <input type="radio" onclick="booze.setting.selectMotorTaskRegulationMode('${type}', 'off')" id="motorTask_${type}_regulationMode_off" name="setting.${type}.regulationMode" value="${de.booze.backend.grails.MotorTask.REGULATION_MODE_OFF}" <g:if test="${!setting[type]?.regulationMode || setting[type]?.regulationMode == de.booze.backend.grails.MotorTask.REGULATION_MODE_OFF}">checked="checked"</g:if> /> <label for="motorTask_${type}_regulationMode_off"><g:message code="motorTask.regulationMode.${de.booze.backend.grails.MotorTask.REGULATION_MODE_OFF}" /></label>
    <input type="radio" onclick="booze.setting.selectMotorTaskRegulationMode('${type}', 'speed')" id="motorTask_${type}_regulationMode_speed" name="setting.${type}.regulationMode" value="${de.booze.backend.grails.MotorTask.REGULATION_MODE_SPEED}" <g:if test="${setting[type]?.regulationMode == de.booze.backend.grails.MotorTask.REGULATION_MODE_SPEED}">checked="checked"</g:if> /> <label for="motorTask_${type}_regulationMode_speed"><g:message code="motorTask.regulationMode.${de.booze.backend.grails.MotorTask.REGULATION_MODE_SPEED}" /></label>
    <input type="radio" onclick="booze.setting.selectMotorTaskRegulationMode('${type}', 'temperature')" id="motorTask_${type}_regulationMode_temperature" name="setting.${type}.regulationMode" value="${de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}" <g:if test="${setting[type]?.regulationMode == de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}">checked="checked"</g:if> /> <label for="motorTask_${type}_regulationMode_temperature"><g:message code="motorTask.regulationMode.${de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}" /></label>
    <input type="radio" onclick="booze.setting.selectMotorTaskRegulationMode('${type}', 'pressure')" id="motorTask_${type}_regulationMode_pressure" name="setting.${type}.regulationMode" value="${de.booze.backend.grails.MotorTask.REGULATION_MODE_PRESSURE}" <g:if test="${setting[type]?.regulationMode == de.booze.backend.grails.MotorTask.REGULATION_MODE_PRESSURE}">checked="checked"</g:if> /> <label for="motorTask_${type}_regulationMode_pressure"><g:message code="motorTask.regulationMode.${de.booze.backend.grails.MotorTask.REGULATION_MODE_PRESSURE}" /></label>
  </div>
</div>

<div id="motorTask_${type}_regulationModeData">
  <div id="motorTask_${type}_regulationModeData_speed" <g:if test="${setting[type]?.regulationMode != de.booze.backend.grails.MotorTask.REGULATION_MODE_SPEED}">style="display: none"</g:if>>
    <div class="row">
      <label for="setting.${type}.targetSpeed"><g:message code="motorTask.targetSpeed.label" /></label>
      <input type="text" name="setting.${type}.targetSpeed" value="${setting[type]?.targetSpeed}" />
    </div>
  </div>

  <div id="motorTask_${type}_regulationModeData_temperature" <g:if test="${setting[type]?.regulationMode != de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}">style="display: none"</g:if>>
    <div class="row">
      <div class="column50percent">
        <label for="setting.${type}.targetTemperature"><g:message code="motorTask.targetTemperature.label" /></label>
        <input type="text" name="setting.${type}.targetTemperature" value="${setting[type]?.targetTemperature}" />
      </div>
      <div class="column50percent">
        <label for="setting.${type}.temperatureRegulationDirection"><g:message code="motorTask.temperatureRegulationDirection.label" /></label>
        <div class="radioset" id="motorTask_${type}_regulationModeData_temperature_regulationDirection">
          <input type="radio" id="motorTask_${type}_temperature_regulation_up" name="setting.${type}.temperatureRegulationDirection" value="${true}" <g:if test="${setting[type]?.temperatureRegulationDirection == true}">checked="checked"</g:if> /> <label for="motorTask_${type}_temperature_regulation_up"><g:message code="motorTask.temperatureRegulationDirection.up" /></label>
          <input type="radio" id="motorTask_${type}_temperature_regulation_down" name="setting.${type}.temperatureRegulationDirection" value="${false}" <g:if test="${!setting[type]?.temperatureRegulationDirection || setting[type]?.temperatureRegulationDirection == false}">checked="checked"</g:if> /> <label for="motorTask_${type}_temperature_regulation_down"><g:message code="motorTask.temperatureRegulationDirection.down" /></label>
        </div>
      </div>
    </div>
    
    <div class="row">
      <label for="setting.${type}.temperatureSensors"><g:message code="motorTask.temperatureSensors.label" /></label>
      <select multiple="true" size="3" name="setting.${type}.temperatureSensors">
        <g:each in="${setting.temperatureSensors}" var="temperatureSensor">
          <option value="${temperatureSensor.id}">${temperatureSensor.name.encodeAsHTML()}</option>
        </g:each>
      </select>
    </div>
  </div>
  
  <div id="motorTask_${type}_regulationModeData_pressure" <g:if test="${setting[type]?.regulationMode != de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}">style="display: none"</g:if>>
    <div class="row">
      <div class="column50percent">
        <label for="setting.${type}.targetPressure"><g:message code="motorTask.targetPressure.label" /></label>
        <input type="text" name="setting.${type}.targetPressure" value="${setting[type]?.targetPressure}" />
      </div>
      <div class="column50percent">
        <label for="setting.${type}.pressureRegulationDirection"><g:message code="motorTask.pressureRegulationDirection.label" /></label>
        <div class="radioset" id="motorTask_${type}_regulationModeData_pressure_regulationDirection">
          <input type="radio" id="motorTask_${type}_pressure_regulation_up" name="setting.${type}.pressureRegulationDirection" value="${true}" <g:if test="${setting[type]?.pressureRegulationDirection == true}">checked="checked"</g:if> /> <label for="motorTask_${type}_pressure_regulation_up"><g:message code="motorTask.pressureRegulationDirection.up" /></label>
          <input type="radio" id="motorTask_${type}_pressure_regulation_down" name="setting.${type}.pressureRegulationDirection" value="${false}" <g:if test="${!setting[type]?.pressureRegulationDirection || setting[type]?.pressureRegulationDirection == false}">checked="checked"</g:if> /> <label for="motorTask_${type}_pressure_regulation_down"><g:message code="motorTask.pressureRegulationDirection.down" /></label>
        </div>
      </div>
    </div>
    
    <div class="row">
      <label for="setting.${type}.pressureSensors"><g:message code="motorTask.pressureSensors.label" /></label>
      <select multiple="true" size="3" name="setting.${type}.pressureSensors">
        <g:each in="${setting.pressureSensors}" var="pressureSensor">
          <option value="${pressureSensor.id}">${pressureSensor.name.encodeAsHTML()}</option>
        </g:each>
      </select>
    </div>
  </div>
  
</div>


<g:javascript>
$(function() {
    $( "#motorTask_${type}_mode" ).buttonset();
});
$(function() {
    $( "#motorTask_${type}_regulationMode" ).buttonset();
});
$(function() {
    $( "#motorTask_${type}_regulationModeData_temperature_regulationDirection" ).buttonset();
});
$(function() {
    $( "#motorTask_${type}_regulationModeData_pressure_regulationDirection" ).buttonset();
});
</g:javascript>