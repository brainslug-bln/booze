<h1><g:message code="setting.pressureSensor.installedPressureSensors" /></h1>
<ul>
  <g:if test="${it.pressureSensors.size() < 1}">
    <li>
      <div><g:message code="setting.pressureSensor.noPressureSensorsInfo" /></div>
      <div>&nbsp;</div>
      <div><a href="#" onclick="booze.setting.editDevice('pressureSensor', {'setting.id': '${it.id}'})"><g:message code="setting.pressureSensor.add" /></a></div>
    </li>
  </g:if>

  <g:each in="${it.pressureSensors}" var="pressureSensor">
    <li onclick="booze.setting.editDevice('pressureSensor', {'pressureSensor.id':'${pressureSensor.id}', 'setting.id':'${it.id}'})">
      <div class="name"><g:fieldValue bean="${pressureSensor}" field="name" /></div>
      <div class="driver">${pressureSensor.driver.tokenize(".").last().encodeAsHTML()}</div>
    </li>
  </g:each>

  <li class="pagination">&nbsp;</li>
</ul>