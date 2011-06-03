<h1>
  <g:if test="${regulator.id}">
    <g:message code="setting.motorRegulator.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.motorRegulator.add" />
  </g:else>
</h1>

<div class="form">
  <form id="motorsTab_regulatorEditorForm">
    <g:if test="${regulator.id}">
      <input type="hidden" name="regulator.id" value="${regulator.id}" />
    </g:if>

    <input type="hidden" name="motor.id" value="${motor?.id}" />
    <input type="hidden" name="setting.id" value="${regulator.setting.id}" />

    <div class="row">
      <label for="regulator.name"><g:message code="motorRegulatorDevice.name.label" /></label>
      <div class="errors" id="motorsTab_errors_name">
         <g:renderErrors bean="${regulator}" field="name" as="list" />
      </div>
      <input type="text" name="regulator.name" value="${fieldValue(bean: regulator, field:'name')}" maxlength="254" onkeyup="$('#motorsTab_errors_name').slideUp(100)" />
    </div>
    
    <div class="row">
      <label for="regulator.softOn"><g:message code="motorRegulatorDevice.softOn.label" /></label>
      <div class="errors" id="motorsTab_errors_softOn">
         <g:renderErrors bean="${regulator}" field="softOn" as="list" />
      </div>
      <input type="text" name="regulator.softOn" value="${fieldValue(bean: regulator, field:'softOn')}" maxlength="254" onkeyup="$('#motorsTab_errors_softOn').slideUp(100)" />
    </div>

    <div class="row">
      <label for="regulator.driver"><g:message code="motorRegulatorDevice.driver.label" /></label>
      <div class="errors" id="motorsTab_errors_driver">
         <g:renderErrors bean="${regulator}" field="driver" as="list" />
      </div>
      <select id="motorsTab_regulator_driverSelector" name="regulator.driver" onclick="$('#motorsTab_errors_driver').slideUp(100)">
        <option value="" <g:if test="${!regulator.driver}">selected="selected"</g:if> ></option>
        <g:each in="${drivers}" var="driver">
          <option value="${driver.encodeAsHTML()}"
            <g:if test="${driver == regulator.driver}">
              selected="selected"
            </g:if> >
            ${driver.tokenize(".").last().encodeAsHTML()}
          </option>
        </g:each>
      </select>
    </div>
    
    <div id="motorsTab_regulator_driverOptions">
      <g:if test="${regulator.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: regulator.driver]}" />
      </g:if>
    </div>
    
    <div class="buttonbar">
      <input class="left ui-button ui-state-default" type="submit" name="saveMotorRegulator" value="Speichern" />
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditRegulator()" name="cancel" value="Abbrechen" />
    </div>
    
    <div class="buttonbar">
      <input class="right ui-button ui-state-default" type="button" onclick="booze.setting.deleteRegulator()" value="Löschen"/>
    </div>
  </form>
</div>

<g:javascript>
  $('#motorsTab_regulator_driverSelector').change({prefix: "regulator_"}, booze.setting.fetchDriverOptions)
  $('#motorsTab_regulatorEditorForm').submit({type: 'motor'}, booze.setting.saveRegulator);
  
</g:javascript>

  