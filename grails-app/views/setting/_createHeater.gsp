<h1><g:message code="setting.heater.add" /></h1>

<div class="form">

  <div class="row">
    <label for="heater.name"><g:message code="heaterDevice.name.label" /></label>
    <div class="errors" id="errors_name">
       <g:renderErrors bean="${heater}" field="name" as="list" />
    </div>
    <input type="text" name="heater.name" value="${fieldValue(bean: heater, field:'name')}" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
  </div>
  
  <div class="row">
    <label for="heater.driver"><g:message code="heaterDevice.driver.label" /></label>
    <select id="driverSelector" name="heater.driver">
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
  
  <div id="driverOptions">
    <g:if test="${heater?.driver}">
      <g:render template="driverOptions" model="${[optionValues: optionValues, driver: heater.driver, options: Class.forName(heater.driver).availableOptions]}" />
        model: [optionValues: params.optionValues, options: driverClass.availableOptions, driver: params.driver] )] as JSON)
    </g:if>
  </div>
</div>

<g:javascript>
  $('#driverSelector').change(booze.setting.fetchDriverOptions)
</g:javascript>

  