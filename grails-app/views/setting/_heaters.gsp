<h2><g:message code="setting.edit.heaters" /></h2>

<form id="heatersForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      
    </div>
    
    <div class="column50percent">
      
    </div>
    
    <div class="buttonbar">
        <input class="ui-button ui-state-default" type="button" id="saveHeatersButton" onclick="booze.setting.update(this.form)" value="${message(code:'setting.edit.save')}" />
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="heaters" />
  </div>
  
</form>