<g:each in="${options}" var="option">
  <div class="row">
    <label for="${option.name.encodeAsHTML()}"><g:message code="${option.messageCode}" /></label>
    <g:if test="${checkOptions}">
      <div class="errors" id="errors_${option.name}">
         <setting:checkDriverOption driver="${driver}" option="${option}" />
      </div>
    </g:if>
    <input type="text" name="driverOptionValues.${option.name}" maxlength="254" onkeyup="$('#errors_${option.name}').slideUp(100)" />
  </div>
</g:each>