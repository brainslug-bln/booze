<h2><g:message code="setting.edit.heaters" /></h2>

<form id="heatersForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox itemList deviceList">
        <h1>Installierte Heizelemente</h1>
        <ul>
          <g:if test="${it.heaters.size() < 1}">
            <li>
              <div>In dieser Umgebung ist noch kein Heizelement angelegt.</div>
              <div>&nbsp;</div>
              <div><a href="#" onclick="booze.setting.createDevice('Heater')">Anlegen</a></div>
            </li>
          </g:if>
          
          <g:each in="${it.heaters}" var="heater">
            <li>
              <div class="name"><g:fieldValue bean="${heater}" field="name" /></div>
              <div class="driver"><g:fieldValue bean="${heater}" field="driver" /></div>
              <div class="regulator">
                <g:if test="${heater.regulator}">
                  <g:fieldValue bean="${heater.regulator}" field="name" />
                </g:if>
                <g:else>
                  Kein Regler installiert
                </g:else>
              </div>
            </li>
          </g:each>

          <li class="pagination">&nbsp;</li>
        </ul>
      </div>
    </div>
    
    <div class="column50percent">
      <div id="deviceEditor" class="contentbox deviceEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="clear">&nbsp;</div>
    
    <div class="buttonbar">
        <input class="ui-button ui-state-default" type="button" id="saveHeatersButton" onclick="booze.setting.update(this.form)" value="${message(code:'setting.edit.save')}" />
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="heaters" />
  </div>
  
</form>