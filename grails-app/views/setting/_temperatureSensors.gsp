<h2><g:message code="setting.edit.temperatureSensors" /></h2>

<form id="temperatureSensorsForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox itemList deviceList" id="temperatureSensorsTab_deviceList">
        <g:render template="/temperatureSensor/list" bean="${it}" />
      </div>
      <div id="temperatureSensorsTab_deviceEditor" class="contentbox deviceEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="column50percent">
      <div class="contentbox deviceEditor" id="temperatureSensorsTab_regulatorEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="clear">&nbsp;</div>
    
    <div class="buttonbar">
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="temperatureSensors" />
  </div>
  
</form>