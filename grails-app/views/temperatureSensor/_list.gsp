<h1><g:message code="setting.temperatureSensor.installedTemperatureSensors" /></h1>
<ul>
  <g:if test="${it.temperatureSensors.size() < 1}">
    <li>
      <div><g:message code="setting.temperatureSensor.noTemperatureSensorsInfo" /></div>
      <div>&nbsp;</div>
      <div><a href="#" onclick="booze.setting.editDevice('temperatureSensor', {'setting.id': '${it.id}'})"><g:message code="setting.temperatureSensor.add" /></a></div>
    </li>
  </g:if>

  <g:each in="${it.temperatureSensors}" var="temperatureSensor">
    <li onclick="booze.setting.editDevice('temperatureSensor', {'temperatureSensor.id':'${temperatureSensor.id}', 'setting.id':'${it.id}'})">
      <div class="name"><g:fieldValue bean="${temperatureSensor}" field="name" /></div>
      <div class="driver">${temperatureSensor.driver.tokenize(".").last().encodeAsHTML()}</div>
    </li>
  </g:each>

  <li class="pagination">&nbsp;</li>
</ul>