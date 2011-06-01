<h1>
  <g:if test="${heater.id}">
    <g:message code="setting.heater.edit" />
  </g:if>
  <g:else>
    <g:message code="setting.heater.add" />
  </g:else>
</h1>

<div class="form">
  <form id="deviceEditorForm">
    <g:if test="${heater.id}">
      <input type="hidden" name="heater.id" value="${heater.id}" />
    </g:if>

    <input type="hidden" name="setting.id" value="${setting.id}" />

    <div class="row">
      <label for="heater.name"><g:message code="heaterDevice.name.label" /></label>
      <div class="errors" id="errors_name">
         <g:renderErrors bean="${heater}" field="name" as="list" />
      </div>
      <input type="text" name="heater.name" value="${fieldValue(bean: heater, field:'name')}" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
    </div>

    <div class="row">
      <label for="heater.driver"><g:message code="heaterDevice.driver.label" /></label>
      <div class="errors" id="errors_driver">
         <g:renderErrors bean="${heater}" field="driver" as="list" />
      </div>
      <select id="driverSelector" name="heater.driver" onclick="$('#errors_driver').slideUp(100)">
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
      <div class="errors" id="errors_regulator">
         <g:renderErrors bean="${heater}" field="regulator" as="list" />
      </div>
      <g:if test="${heater.regulator}">
        <g:fieldValue bean="${heater.regulator}" field="regulator" />
      </g:if>
      <g:else>
        <a href='#'><g:message code="setting.heater.edit.addRegulator" /></a>
      </g:else>
      <input type="hidden" name="heater.regulator" value="${fieldValue(bean: heater, field:'regulator')}" maxlength="254" onkeyup="$('#errors_regulator').slideUp(100)" />
    </div>

    <div id="driverOptions">
      <g:if test="${heater.driver}">
        <g:render template="/setting/driverOptions" model="${[checkOptions: checkOptions, driverOptionValues: driverOptionValues, driver: heater.driver]}" />
      </g:if>
    </div>
    
    <input class="ui-button ui-state-default" type="submit" name="saveHeater" value="Speichern" />
    <input class="ui-button ui-state-default" type="button" onclick="booze.setting.cancelEditDevice()" name="cancel" value="Abbrechen" />
  </form>
</div>

<g:javascript>
  $('#driverSelector').change(booze.setting.fetchDriverOptions)
  $('#deviceEditorForm').submit({type: 'heater'}, booze.setting.saveDevice);
  
</g:javascript>

  