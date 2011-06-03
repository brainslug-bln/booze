<h1><g:message code="setting.heater.installedHeaters" /></h1>
<ul>
  <g:if test="${it.listHeaters().size() < 1}">
    <li>
      <div><g:message code="setting.heater.noHeatersInfo" /></div>
    </li>
  </g:if>

  <g:each in="${it.listHeaters()}" var="heater">
    <li>
      <div class="name" onclick="booze.setting.editDevice('heater', {'heater.id':'${heater.id}', 'setting.id':'${it.id}'})"><g:fieldValue bean="${heater}" field="name" /></div> <div class="deleteButton" onclick="booze.setting.deleteDevice('heater', {'heater.id':'${heater.id}', 'setting.id':'${it.id}'}); return false;"><span class="ui-icon ui-icon-circle-minus"></span></div>
      <div class="driver" onclick="booze.setting.editDevice('heater', {'heater.id':'${heater.id}', 'setting.id':'${it.id}'})">${heater.driver.tokenize(".").last().encodeAsHTML()}</div>
      <div class="regulator" onclick="booze.setting.editDevice('heater', {'heater.id':'${heater.id}', 'setting.id':'${it.id}'})">
        <g:if test="${heater.regulator}">
          <g:message code="setting.heater.regulator" args="${[heater?.regulator?.name?.encodeAsHTML()]}" />
        </g:if>
        <g:else>
          <g:message code="setting.heater.noRegulatorInstalled" />
        </g:else>
      </div>
    </li>
  </g:each>

  <li class="pagination">
    <div class="createLink" onclick="booze.setting.editDevice('heater', {'setting.id':'${it.id}'});"><span class="left ui-icon ui-icon-circle-plus"></span> <span class="createText">Heizelement hinzufügen</span></div>
  </li>
</ul>