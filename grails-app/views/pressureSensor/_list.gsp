<h1><g:message code="setting.pressureSensor.installedPressureSensors" /></h1>
<ul>
  <g:if test="${it.pressureSensors.size() < 1}">
    <li>
      <div><g:message code="setting.pressureSensor.noPressureSensorsInfo" /></div>
    </li>
  </g:if>

  <g:each in="${it.pressureSensors}" var="pressureSensor">
    <li>
      <div class="name" onclick="booze.setting.editDevice('pressureSensor', {'pressureSensor.id':'${pressureSensor.id}', 'setting.id':'${it.id}'})"><g:fieldValue bean="${pressureSensor}" field="name" /></div> <div class="deleteButton" onclick="booze.setting.deleteDevice('pressureSensor', {'pressureSensor.id':'${pressureSensor.id}', 'setting.id':'${it.id}'}); return false;"><span class="booze-icon booze-icon-delete"></span></div>
      <div class="driver" onclick="booze.setting.editDevice('pressureSensor', {'pressureSensor.id':'${pressureSensor.id}', 'setting.id':'${it.id}'})">${pressureSensor.driver.tokenize(".").last().encodeAsHTML()}</div>
    </li>
  </g:each>

  <li class="pagination">
    <div class="createLink" onclick="booze.setting.editDevice('pressureSensor', {'setting.id':'${it.id}'});"><span class="left booze-icon booze-icon-add"></span> <span class="createText">Sensor hinzufügen</span></div>
  </li>
</ul>