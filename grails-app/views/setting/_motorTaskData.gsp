<div class="row">
  <label for="setting.${type}.motor"><g:message code="motorTask.motor.label" /></label>
  <div class="errors" id="motorTasksTab_errors_${type}_motor">
     <g:renderErrors bean="${setting[type]}" field="motor" as="list" />
  </div>
  
  <select name="setting.${type}.motor.id">
      <option onclick="$('#motorTask_${type}_regulationMode').hide()" value="" <g:if test="${ !setting[type]?.motor }">checked="checked"</g:if>></option>
    <g:each in="${setting.motors}" var="motor">
      <option onclick="<g:if test='${motor.regulator != null}'>$('#motorTask_${type}_regulationMode').show()</g:if><g:else>$('#motorTask_${type}_regulationMode').hide()</g:else>" value="${motor.id}" <g:if test="${motor.id == setting[type]?.motor?.id }">selected="selected"</g:if>>${motor.name.encodeAsHTML()}</option>
    </g:each>
  </select>
</div>

<div class="row">
  <label for="setting.${type}.cyclingMode"><g:message code="motorTask.cyclingMode.label" /></label>
  <div class="radioset" id="motorTask_${type}_cyclingMode">
    <input type="radio" onclick="$('#motorTask_${type}_interval').hide();" id="motorTask_${type}_cyclingMode_on" name="setting.${type}.cyclingMode" value="${de.booze.backend.grails.MotorTask.CYCLING_MODE_ON}" <g:if test="${!setting[type]?.cyclingMode || setting[type]?.cyclingMode == de.booze.backend.grails.MotorTask.CYCLING_MODE_ON}">checked="checked"</g:if> /> <label for="motorTask_${type}_cyclingMode_on"><g:message code="motorTask.cyclingMode.${de.booze.backend.grails.MotorTask.CYCLING_MODE_ON}" /></label>
    <input type="radio" onclick="$('#motorTask_${type}_interval').show();" id="motorTask_${type}_cyclingMode_interval" name="setting.${type}.cyclingMode" value="${de.booze.backend.grails.MotorTask.CYCLING_MODE_INTERVAL}" <g:if test="${setting[type]?.cyclingMode == de.booze.backend.grails.MotorTask.CYCLING_MODE_INTERVAL}">checked="checked"</g:if> /> <label for="motorTask_${type}_cyclingMode_interval"><g:message code="motorTask.cyclingMode.${de.booze.backend.grails.MotorTask.CYCLING_MODE_INTERVAL}" /></label>
  </div>
</div>

<div class="row" id="motorTask_${type}_interval" <g:if test="${setting[type]?.cyclingMode != de.booze.backend.grails.MotorTask.CYCLING_MODE_INTERVAL}">style="display: none"</g:if>>
  <div class="column50percent">
    <label for="setting.${type}.onInterval"><g:message code="motorTask.onInterval.label" /></label>
    <div class="errors" id="motorTasksTab_errors_${type}_onInterval">
       <g:renderErrors bean="${setting[type]}" field="onInterval" as="list" />
    </div>
    <input type="text" class="small" name="setting.${type}.onInterval" value="${setting[type]?.onInterval}" />
  </div>
  <div class="column50percent">
    <label for="setting.${type}.offInterval"><g:message code="motorTask.offInterval.label" /></label>
    <div class="errors" id="motorTasksTab_errors_${type}_offInterval">
       <g:renderErrors bean="${setting[type]}" field="offInterval" as="list" />
    </div>
    <input type="text" class="small" name="setting.${type}.offInterval" value="${setting[type]?.offInterval}" />
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
      <div class="errors" id="motorTasksTab_errors_${type}_targetSpeed">
         <g:renderErrors bean="${setting[type]}" field="targetSpeed" as="list" />
      </div>
      <label for="setting.${type}.targetSpeed"><g:message code="motorTask.targetSpeed.label" /></label>
      <input type="text" name="setting.${type}.targetSpeed" value="${setting[type]?.targetSpeed}" />
    </div>
  </div>

  <div id="motorTask_${type}_regulationModeData_temperature" <g:if test="${setting[type]?.regulationMode != de.booze.backend.grails.MotorTask.REGULATION_MODE_TEMPERATURE}">style="display: none"</g:if>>
    <div class="row">
      <div class="column50percent">
        <div class="errors" id="motorTasksTab_errors_${type}_targetTemperature">
           <g:renderErrors bean="${setting[type]}" field="targetTemperature" as="list" />
        </div>
        <label for="setting.${type}.targetTemperature"><g:message code="motorTask.targetTemperature.label" /></label>
        <input type="text" name="setting.${type}.targetTemperature" value="${formatNumber(number:setting[type]?.targetTemperature, format:'###.##')}" />
      </div>
      <div class="column50percent">
        <label for="setting.${type}.temperatureRegulationDirection"><g:message code="motorTask.temperatureRegulationDirection.label" /></label>
        <div class="radioset" id="motorTask_${type}_regulationModeData_temperature_regulationDirection">
          <input type="radio" id="motorTask_${type}_temperature_regulation_up" name="setting.${type}.temperatureRegulationDirection" value="${de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_UP}" <g:if test="${setting[type]?.temperatureRegulationDirection == de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_UP}">checked="checked"</g:if> /> <label for="motorTask_${type}_temperature_regulation_up"><g:message code="motorTask.temperatureRegulationDirection.up" /></label>
          <input type="radio" id="motorTask_${type}_temperature_regulation_down" name="setting.${type}.temperatureRegulationDirection" value="${de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_DOWN}" <g:if test="${!setting[type]?.temperatureRegulationDirection || setting[type]?.temperatureRegulationDirection == de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_DOWN}">checked="checked"</g:if> /> <label for="motorTask_${type}_temperature_regulation_down"><g:message code="motorTask.temperatureRegulationDirection.down" /></label>
        </div>
      </div>
    </div>
    
    <div class="row">
      <label for="setting.${type}.temperatureSensors"><g:message code="motorTask.temperatureSensors.label" /></label>
      <select multiple="true" size="3" name="setting.${type}.temperatureSensors">
        <g:each in="${setting.temperatureSensors}" var="temperatureSensor">
          <option value="${temperatureSensor.id}" <setting:sensorSelected selectedSensors="${setting[type]?.temperatureSensors}" sensor="${temperatureSensor}" /> >${temperatureSensor.name.encodeAsHTML()}</option>
        </g:each>
      </select>
    </div>
  </div>
  
  <div id="motorTask_${type}_regulationModeData_pressure" <g:if test="${setting[type]?.regulationMode != de.booze.backend.grails.MotorTask.REGULATION_MODE_PRESSURE}">style="display: none"</g:if>>
    <div class="row">
      <div class="column50percent">
        <div class="errors" id="motorTasksTab_errors_${type}_targetPressure">
           <g:renderErrors bean="${setting[type]}" field="targetPressure" as="list" />
        </div>
        <label for="setting.${type}.targetPressure"><g:message code="motorTask.targetPressure.label" /></label>
        <input type="text" name="setting.${type}.targetPressure" value="${formatNumber(number:setting[type]?.targetPressure, format:'###.##')}" />
      </div>
      <div class="column50percent">
        <label for="setting.${type}.pressureRegulationDirection"><g:message code="motorTask.pressureRegulationDirection.label" /></label>
        <div class="radioset" id="motorTask_${type}_regulationModeData_pressure_regulationDirection">
          <input type="radio" id="motorTask_${type}_pressure_regulation_up" name="setting.${type}.pressureRegulationDirection" value="${de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_UP}" <g:if test="${setting[type]?.pressureRegulationDirection == de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_UP}">checked="checked"</g:if> /> <label for="motorTask_${type}_pressure_regulation_up"><g:message code="motorTask.pressureRegulationDirection.up" /></label>
          <input type="radio" id="motorTask_${type}_pressure_regulation_down" name="setting.${type}.pressureRegulationDirection" value="${de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_DOWN}" <g:if test="${!setting[type]?.pressureRegulationDirection || setting[type]?.pressureRegulationDirection == de.booze.backend.grails.MotorTask.REGULATION_DIRECTION_DOWN}">checked="checked"</g:if> /> <label for="motorTask_${type}_pressure_regulation_down"><g:message code="motorTask.pressureRegulationDirection.down" /></label>
        </div>
      </div>
    </div>
    
    <div class="row">
      <label for="setting.${type}.pressureSensors"><g:message code="motorTask.pressureSensors.label" /></label>
      <select multiple="true" size="3" name="setting.${type}.pressureSensors">
        <g:each in="${setting.pressureSensors}" var="pressureSensor">
          <option value="${pressureSensor.id}" <setting:sensorSelected selectedSensors="${setting[type]?.pressureSensors}" sensor="${pressureSensor}" />>${pressureSensor.name.encodeAsHTML()}</option>
        </g:each>
      </select>
    </div>
  </div>
  
</div>


<g:javascript>
$(function() {
    $( "#motorTask_${type}_cyclingMode" ).buttonset();
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