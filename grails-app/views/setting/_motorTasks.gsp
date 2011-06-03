<h2><g:message code="setting.edit.motorTasks" /></h2>

<form id="motorTasksForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <input type="hidden" name="setting.id" value="${it?.id}" />
  <input type="hidden" name="tab" value="motorTasks" />
  
  <div class="form">  
    <div class="column50percent">
      <div class="contentbox deviceEditor">
        <g:render template="motorTask" model="${[setting: it, type: 'mashingPump']}" />
      </div>

      <div class="contentbox deviceEditor">
        <g:render template="motorTask" model="${[setting: it, type: 'mashingMixer']}" />
      </div>

      <div class="contentbox deviceEditor">
        <g:render template="motorTask" model="${[setting: it, type: 'drainPump']}" />
      </div>
    </div>
    
    <div class="column50percent">
      <div class="contentbox deviceEditor">
        <g:render template="motorTask" model="${[setting: it, type: 'cookingPump']}" />
      </div>
      
      <div class="contentbox deviceEditor">
        <g:render template="motorTask" model="${[setting: it, type: 'cookingMixer']}" />
      </div>
    </div>
  </div>
</form>