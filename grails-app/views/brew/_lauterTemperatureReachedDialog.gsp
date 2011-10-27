<div class="brewLauterTemperatureReachedDialog brew brewDialog">
	<h4>
		<g:message code="brew.init.lauterTemperatureReached"
			args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.lauterTemperature)]}" />
	</h4>

	<g:if test="${brewProcess.recipe.postSpargingWort}">
		<p>
			<g:message code="brew.init.postSpargingWortInfo"
				args="${[formatNumber(format: '##0.0', number:brewProcess.recipe.postSpargingWort)]}" />
		</p>
	</g:if>

	<p>
		<g:message code="brew.init.elongateMashingInfo" />
	</p>

	<fieldset>
		<label for="brewLauterTemperatureReachedDialog_elongateMashingTime"><g:message
				code="brew.init.elongateMashingTime" /></label> <input type="text"
			name="elongateMashingTime"
			id="brewLauterTemperatureReachedDialog_elongateMashingTime" value="5" />

		<input class="ui-button ui-state-default" type="button"
			name="elongateMashing" onclick="booze.brew.elongateMashing();"
			value="${message(code: 'brew.init.elongateMashing')}" />
	</fieldset>

	<div class="clear"></div>

	<p>
		<g:message code="brew.init.enterAdditionalValueInfo" />
	</p>

  <form id="brewLauterTemperatureReachedForm">
		<fieldset>
			<div class="leftColumn">
				<label><g:message code="protocol.finalPreSpargingWort" /></label> <input
					type="text" name="finalPreSpargingWort" value="" /> <label><g:message
						code="protocol.finalPostSpargingWort" /></label> <input type="text"
					name="finalPostSpargingWort" value=""
					id="brewLauterTemperatureReachedDialog_finalPostSpargingWort" />
			</div>
	
			<div class="rightColumn">
				<label><g:message code="protocol.finalSpargingWaterVolume" /></label>
				<input type="text" name="finalSpargingWaterVolume" value="" />
	
	      <div class="row">
	        <label><g:message code="protocol.iodineTest" /></label>
	        <div class="radioset ui-buttonset" id="iodineTest">
	          <input type="radio" id="iodineTest_positive" name="iodineTest" value="${0}" />
	          <label for="iodineTest_positive"><g:message code="protocol.iodineTest.positive" /></label> 
	          
	          <input type="radio" id="iodineTest_negative" name="iodineTest" value="${1}" checked />
	          <label for="iodineTest_negative"><g:message code="protocol.iodineTest.negative" /></label>
	        </div>
	      </div>
	      
			</div>
		</fieldset>
	</form>

	<div class="clear"></div>

</div>
