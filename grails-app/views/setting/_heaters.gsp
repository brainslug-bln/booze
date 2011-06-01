<h2><g:message code="setting.edit.heaters" /></h2>

<form id="heatersForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox itemList deviceList" id="heatersTab_deviceList">
        <g:render template="/heater/list" bean="${it}" />
      </div>
      <div id="heatersTab_deviceEditor" class="contentbox deviceEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="column50percent">
      <div class="contentbox deviceEditor" id="heatersTab_regulatorEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="clear">&nbsp;</div>
    
    <div class="buttonbar">
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="heaters" />
  </div>
  
</form>