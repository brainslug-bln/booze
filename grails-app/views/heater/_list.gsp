<h1><g:message code="setting.heater.installedHeaters" /></h1>
<ul>
  <g:if test="${it.heaters.size() < 1}">
    <li>
      <div><g:message code="setting.heater.noHeatersInfo" /></div>
      <div>&nbsp;</div>
      <div><a href="#" onclick="booze.setting.editDevice('heater', {'setting.id': '${it.id}'})"><g:message code="setting.heater.add" /></a></div>
    </li>
  </g:if>

  <g:each in="${it.heaters}" var="heater">
    <li onclick="booze.setting.editDevice('heater', {'heater.id':'${heater.id}', 'setting.id':'${it.id}'})">
      <div class="name"><g:fieldValue bean="${heater}" field="name" /></div>
      <div class="driver">${heater.driver.tokenize(".").last().encodeAsHTML()}</div>
      <div class="regulator">
        <g:if test="${heater.regulator}">
          <g:message code="setting.heater.regulator" args="${[heater?.regulator?.name?.encodeAsHTML()]}" />
        </g:if>
        <g:else>
          <g:message code="setting.heater.noRegulatorInstalled" />
        </g:else>
      </div>
    </li>
  </g:each>

  <li class="pagination">&nbsp;</li>
</ul>