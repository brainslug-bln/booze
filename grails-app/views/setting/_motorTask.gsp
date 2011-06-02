<h1><g:message code="setting.motorTask.${type}" /></h1>

<div class="form">
  <form id="motorTask_${type}_editorForm">
   
    <div class="row">
      <label for="motorTask.active"><g:message code="motorTask.active.label" /></label>
      
      <div class="radioset" id="motorTask_${type}_active">
        <input type="radio" id="motorTask_${type}_active_true" name="motorTask.active" value="${true}" <g:if test="${setting[type] != null}">checked="checked"</g:if> /> <label for="motorTask_${type}_active_true"><g:message code="setting.active.true" /></label>
        <input type="radio" id="motorTask_${type}_active_false" name="motorTask.active" value="${false}" <g:if test="${setting[type] == null}">checked="checked"</g:if> /> <label for="motorTask_${type}_active_false"><g:message code="setting.active.false" /></label>
      </div>
    </div>
    
    <div class="row">
      <label for="motorTask.motor"><g:message code="motorTask.motor.label" /></label>
      <select name="motorTask.motor">
        <g:each in="${setting.motors}" var="motor">
          <option value="${motor.id}">${motor.name.encodeAsHTML()}</option>
        </g:each>
      </select>
    </div>
    
  </form>
</div>

<g:javascript>
$(function() {
    $( "#motorTask_${type}_active" ).buttonset();
});
</g:javascript>