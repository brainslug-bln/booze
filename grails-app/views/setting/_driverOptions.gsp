<setting:eachDriverOption driver="${driver}">
  <div class="row">
    <label for="${it.name.encodeAsHTML()}"><g:message code="${it.messageCode}" /></label>
    <g:if test="${checkOptions}">
      <div class="errors" id="errors_${it.name}">
         <setting:checkDriverOption driver="${driver}" option="${it}" optionValues="${driverOptionValues}" />
      </div>
    </g:if>
    <input type="text" name="driverOptionValues.${it.name}" value="${setting.driverOptionValue(option: it, values: driverOptionValues)}" maxlength="254" onkeyup="$('#errors_${it.name}').slideUp(100)" />
  </div>
</setting:eachDriverOption>