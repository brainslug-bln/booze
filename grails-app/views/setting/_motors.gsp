<h2><g:message code="setting.edit.motors" /></h2>

<form id="motorsForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox itemList deviceList" id="motorsTab_deviceList">
        <g:render template="/motor/list" bean="${it}" />
      </div>
      <div id="motorsTab_deviceEditor" class="contentbox deviceEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="column50percent">
      
      <div id="motorsTab_tabOptions">
        <div class="contentbox deviceEditor">
          <g:render template="motorTask" model="${[setting: it, type: 'mashingPump']}" />
        </div>
        
        <div class="contentbox deviceEditor">
          <g:render template="motorTask" model="${[setting: it, type: 'mashingMixer']}" />
        </div>
        
        <div class="contentbox deviceEditor">
          <g:render template="motorTask" model="${[setting: it, type: 'cookingPump']}" />
        </div>
        
        <div class="contentbox deviceEditor">
          <g:render template="motorTask" model="${[setting: it, type: 'cookingMixer']}" />
        </div>
        
        <div class="contentbox deviceEditor">
          <g:render template="motorTask" model="${[setting: it, type: 'drainPump']}" />
        </div>
      </div>
      
      <div class="contentbox deviceEditor" id="motorsTab_regulatorEditor" style="display: none">
        
      </div>
    </div>
    
    <div class="clear">&nbsp;</div>
    
    <div class="buttonbar">
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="motors" />
  </div>
  
</form>