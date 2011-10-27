<h2><g:message code="setting.edit.pressureSensors" /></h2>

<form id="pressureSensorsForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox itemList deviceList" id="pressureSensorsTab_deviceList">
        <g:render template="/pressureSensor/list" bean="${it}" />
      </div>
      <div id="pressureSensorsTab_deviceEditor" class="contentbox deviceEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="column50percent">
      <div class="contentbox deviceEditor" id="pressureSensorsTab_regulatorEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="clear">&nbsp;</div>
    
    <div class="buttonbar">
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="pressureSensors" />
  </div>
  
</form>