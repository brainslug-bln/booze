<h1><g:message code="setting.temperatureSensor.installedTemperatureSensors" /></h1>
<ul>
  <g:if test="${it.listTemperatureSensors().size() < 1}">
    <li>
      <div><g:message code="setting.temperatureSensor.noTemperatureSensorsInfo" /></div>
    </li>
  </g:if>

  <g:each in="${it.listTemperatureSensors()}" var="temperatureSensor">
    <li>
      <div class="name" onclick="booze.setting.editDevice('temperatureSensor', {'temperatureSensor.id':'${temperatureSensor.id}', 'setting.id':'${it.id}'})"><g:fieldValue bean="${temperatureSensor}" field="name" /></div> <div class="deleteButton" onclick="booze.setting.deleteDevice('temperatureSensor', {'temperatureSensor.id':'${temperatureSensor.id}', 'setting.id':'${it.id}'}); return false;"><span class="ui-icon ui-icon-circle-minus"></span></div>
      <div class="driver" onclick="booze.setting.editDevice('temperatureSensor', {'temperatureSensor.id':'${temperatureSensor.id}', 'setting.id':'${it.id}'})">${temperatureSensor.driver.tokenize(".").last().encodeAsHTML()}</div>
    </li>
  </g:each>

  <li class="pagination">
    <div class="createLink" onclick="booze.setting.editDevice('temperatureSensor', {'setting.id':'${it.id}'});"><span class="left ui-icon ui-icon-circle-plus"></span> <span class="createText">Sensor hinzufügen</span></div>
    
  </li>
</ul>