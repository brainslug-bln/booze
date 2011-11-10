<div class="form protocol">
	<g:form controller="protocol">
	   <h3>
        <g:message code="protocol.fieldset.fermentation" />
      </h3>
		<div class="column50percent editProtocolArea">
			
			<div class="row">
				<label for="yeast"><g:message code="protocol.yeast" /></label>
				<div id="errors_yeast" class="errors"></div>
				<input type="text" onkeyup="$('#errors_yeast').slideUp(100)"
					maxlength="254"
					value="${fieldValue(bean:protocolInstance, field:'yeast')}"
					name="yeast"></input>
			</div>

			<div class="row">
				<label for="fermentationTemperature"><g:message
						code="protocol.fermentationTemperature" /></label>
				<div id="fermentationTemperature" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_fermentationTemperature').slideUp(100)"
					maxlength="254"
					value="${formatNumber(format: '##0.0#', number: protocolInstance.fermentationTemperature)}"
					name="fermentationTemperature">
			</div>

			<div class="row">
				<label for="targetBottlingWort"><g:message
						code="protocol.targetBottlingWort" /></label>
				<div id="errors_targetBottlingWort" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_targetBottlingWort').slideUp(100)"
					maxlength="254"
					value="${formatNumber(format: '##0.0#', number: protocolInstance.targetBottlingWort)}"
					name="targetBottlingWort">
			</div>
		</div>

		<div class="column50percent">

			<div class="row">
				<label for="fermentationDuration"><g:message
						code="protocol.fermentationDuration" /></label>
				<div id="errors_fermentationDuration" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_fermentationDuration').slideUp(100)"
					maxlength="254"
					value="${formatNumber(format: '##0.0#', number: protocolInstance.fermentationDuration)}"
					name="fermentationDuration">
			</div>

			<div class="row">
				
			</div>

			<div class="row">
				<label for="finalBottlingWort"><g:message
						code="protocol.finalBottlingWort" /></label>
				<div id="errors_finalBottlingWort" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_finalBottlingWort').slideUp(100)"
					maxlength="254"
					value="${formatNumber(format: '##0.0#', number: protocolInstance.finalBottlingWort)}"
					name="finalBottlingWort">
			</div>
		</div>
		
		<div class="clear"></div>

    <h3><g:message code="protocol.fieldset.storage" /></h3>
		<div class="column50percent">
			<div class="row">
				<label for="fareVolume"><g:message
						code="protocol.fareVolume" /></label>
				<div id="errors_fareVolume" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_fareVolume').slideUp(100)" maxlength="254"
					value="${formatNumber(format: '###0.0#', number: protocolInstance.fareVolume)}"
					name="fareVolume">
			</div>

			<div class="row">
				<label for="alcohol"><g:message code="protocol.alcohol" /></label> <span
					class="immutable"> <g:formatNumber format="##0.0#"
						number="${protocolInstance.alcohol}" />
				</span>
			</div>
		</div>

    
    

		<div class="column50percent">
		
		
			<div class="row">
				<label for="fareConcentration"><g:message
						code="protocol.fareConcentration" /></label>
				<div id="errors_fareConcentration" class="errors"></div>
				<input class="small" type="text"
					onkeyup="$('#errors_fareConcentration').slideUp(100)"
					maxlength="254"
					value="${formatNumber(format: '###0.0#', number: protocolInstance.fareConcentration)}"
					name="fareConcentration">
			</div>
		</div>

		<div class="clear"></div>
		<div class="type-button">
			<input type="hidden" name="id"
				value="${protocolInstance.id.encodeAsHTML()}" /> <span
				style="float: right"> <g:actionSubmit class="ui-button ui-state-default"
					value="${message(code:'protocol.edit.save')}" action="update" />
			</span> <span style="float: left"> <g:actionSubmit class="ui-button ui-state-default"
					value="${message(code:'protocol.edit.delete')}" action="delete" />
			</span>
		</div>
	</g:form>
</div>