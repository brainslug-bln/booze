<h1><g:message code="setting.motor.installedMotors" /></h1>
<ul>
  <g:if test="${it.motors.size() < 1}">
    <li>
      <div><g:message code="setting.motor.noMotorsInfo" /></div>
    </li>
  </g:if>

  <g:each in="${it.motors}" var="motor">
    <li>
      <div class="name" onclick="booze.setting.editDevice('motor', {'motor.id':'${motor.id}', 'setting.id':'${it.id}'})"><g:fieldValue bean="${motor}" field="name" /></div> <div class="deleteButton" onclick="booze.setting.deleteDevice('motor', {'motor.id':'${motor.id}', 'setting.id':'${it.id}'}); return false;"><span class="ui-icon ui-icon-circle-minus"></span></div>
      <div class="driver" onclick="booze.setting.editDevice('motor', {'motor.id':'${motor.id}', 'setting.id':'${it.id}'})">${motor.driver.tokenize(".").last().encodeAsHTML()}</div>
      <div class="regulator" onclick="booze.setting.editDevice('motor', {'motor.id':'${motor.id}', 'setting.id':'${it.id}'})">
        <g:if test="${motor.regulator}">
          <g:message code="setting.motor.regulator" args="${[motor?.regulator?.name?.encodeAsHTML()]}" />
        </g:if>
        <g:else>
          <g:message code="setting.motor.noRegulatorInstalled" />
        </g:else>
      </div>
    </li>
  </g:each>

  <li class="pagination">
    <div class="createLink" onclick="booze.setting.editDevice('motor', {'setting.id':'${it.id}'});"><span class="left ui-icon ui-icon-circle-plus"></span> <span class="createText">Motor hinzufügen</span></div>
  </li>
</ul>