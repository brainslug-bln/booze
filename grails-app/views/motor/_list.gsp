<h1><g:message code="setting.motor.installedMotors" /></h1>
<ul>
  <g:if test="${it.motors.size() < 1}">
    <li>
      <div><g:message code="setting.motor.noMotorsInfo" /></div>
      <div>&nbsp;</div>
      <div><a href="#" onclick="booze.setting.editDevice('motor', {'setting.id': '${it.id}'})"><g:message code="setting.motor.add" /></a></div>
    </li>
  </g:if>

  <g:each in="${it.motors}" var="motor">
    <li onclick="booze.setting.editDevice('motor', {'motor.id':'${motor.id}', 'setting.id':'${it.id}'})">
      <div class="name"><g:fieldValue bean="${motor}" field="name" /></div>
      <div class="driver">${motor.driver.tokenize(".").last().encodeAsHTML()}</div>
      <div class="regulator">
        <g:if test="${motor.regulator}">
          <g:message code="setting.motor.regulator" args="${[motor?.regulator?.name?.encodeAsHTML()]}" />
        </g:if>
        <g:else>
          <g:message code="setting.motor.noRegulatorInstalled" />
        </g:else>
      </div>
    </li>
  </g:each>

  <li class="pagination">&nbsp;</li>
</ul>