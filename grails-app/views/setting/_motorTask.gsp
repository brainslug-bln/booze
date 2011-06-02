<h1><g:message code="setting.motorTask.${type}" /></h1>

<div class="form">
  <form id="motorTask_${type}_editorForm">
   
    <div class="row">
      <label for="motorTask.active"><g:message code="motorTask.active.label" /></label>
      
      <div class="radioset" id="motorTask_${type}_active">
        <input type="radio" onclick="booze.setting.createMotorTask({'type': '${type}', 'setting.id': '${setting.id}'});" id="motorTask_${type}_active_true" name="setting.${type}.active" value="${true}" <g:if test="${setting[type] != null}">checked="checked"</g:if> /> <label for="motorTask_${type}_active_true"><g:message code="setting.active.true" /></label>
        <input type="radio" onclick="$('#motorTask_${type}_data').hide().html('')" id="motorTask_${type}_active_false" name="setting.${type}.active" value="${false}" <g:if test="${setting[type] == null}">checked="checked"</g:if> /> <label for="motorTask_${type}_active_false"><g:message code="setting.active.false" /></label>
      </div>
    </div>
    
    <div id="motorTask_${type}_data">
      <g:if test="${setting[type] != null}">
        <g:render template="motorTaskData" model="${[type: type, setting: setting]}" />
      </g:if>
    </div>
  </form>
</div>

<g:javascript>
$(function() {
    $( "#motorTask_${type}_active" ).buttonset();
});
</g:javascript>