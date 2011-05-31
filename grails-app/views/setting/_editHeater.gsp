<h1>Heizelement hinzufügen</h1>
        <div class="form">
          
          <label for="heater.name"><g:message code="heater.name.label" /></label>
          <div class="errors" id="errors_name">
             <g:renderErrors bean="${it}" field="name" as="list" />
          </div>
          <input type="text" name="heater.name" value="${fieldValue(bean: it, field:'name')}" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
        
        </div>