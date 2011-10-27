<div class="editProtocolDialog brew brewDialog" id="editProtocolDialog">
  <p><g:message code="brew.init.editProtocolInfo" /></p>

  <g:hasErrors bean="${protocol}">
    <p><g:message code="brew.editProtocolData.pleaseCorrectErrors" /></p>  
  </g:hasErrors>

  <div class="form">
    <form id="protocolDataForm">
      
      <fieldset>
        <div class="leftColumn">
          <div class="row">
            <label><g:message code="protocol.finalPreSpargingWort"/></label>
            
            <div class="errors" id="errors_finalPreSpargingWort">
             <g:renderErrors bean="${protocol}" field="finalPreSpargingWort" as="list" />
            </div>

            <input type="text" name="finalPreSpargingWort" onkeyup="$('#errors_finalPreSpargingWort').slideUp(100)" value="${formatNumber(format:'##0.0#', number: protocol.finalPreSpargingWort)}" /> 
          </div>
          
        </div>

        <div class="rightColumn">
            <label><g:message code="protocol.finalSpargingWaterVolume"/></label>
            <div class="errors" id="errors_finalSpargingWaterVolume">
             <g:renderErrors bean="${protocol}" field="finalSpargingWaterVolume" as="list" />
            </div>
            <input type="text" name="finalSpargingWaterVolume" value="${formatNumber(format:'###0.0#', number: protocol.finalSpargingWaterVolume)}" onkeyup="$('#errors_finalSpargingWaterVolume').slideUp(100)" /> 
        </div>
        
        <div class="singleColumn">
          <div class="row">
            <label><g:message code="protocol.finalPostSpargingWort"/></label>
            <div class="errors" id="errors_finalPostSpargingWort">
             <g:renderErrors bean="${protocol}" field="finalPostSpargingWort" as="list" />
            </div>

            <input type="text" name="finalPostSpargingWort" value="${formatNumber(format:'##0.0#', number: protocol.finalPostSpargingWort)}" onkeyup="$('#errors_finalPostSpargingWort').slideUp(100)"  />
          </div>
        </div>
        </fieldset>
        
        <fieldset>
        <div class="singleColumn">
          <div class="row">
	        <label><g:message code="protocol.iodineTest"/></label>
	        <div class="radioset ui-buttonset" id="iodineTest">
	          <input type="radio" id="iodineTest_positive" name="iodineTest" value="${0}" <g:if test="${protocol.iodineTest == 0}">checked</g:if> /><label for="iodineTest_positive"><g:message code="protocol.iodineTest.positive" /></label>
	          <input type="radio" id="iodineTest_negative" name="iodineTest" value="${1}" <g:if test="${protocol.iodineTest == 1}">checked</g:if> /><label for="iodineTest_negative"><g:message code="protocol.iodineTest.negative" /></label>
	        </div>
          </div>
          
          <script type="text/javascript">
			$(function() {
				console.log($("#iodineTest"));
				$( "#iodineTest" ).buttonset();
			});
		  </script>
        </div>
      </fieldset>

      <fieldset>
        <div class="leftColumn">
            <label><g:message code="protocol.dilutionWaterVolume"/></label>
            <div class="errors" id="errors_dilutionWaterVolume">
             <g:renderErrors bean="${protocol}" field="dilutionWaterVolume" as="list" />
            </div>
            <input type="text" onkeyup="$('#errors_dilutionWaterVolume').slideUp(100)" name="dilutionWaterVolume" value="${formatNumber(format:'###0.0#', number: protocol.dilutionWaterVolume)}" /> 

            <label><g:message code="protocol.finalBeerVolume"/></label>
            <div class="errors" id="errors_finalBeerVolume">
             <g:renderErrors bean="${protocol}" field="finalBeerVolume" as="list" />
            </div>
            <input type="text" onkeyup="$('#errors_finalBeerVolume').slideUp(100)" name="finalBeerVolume" value="${formatNumber(format:'###0.0#', number: protocol.finalBeerVolume)}" />
        </div>

        <div class="rightColumn">
            <label><g:message code="protocol.finalOriginalWort"/></label>
            <div class="errors" id="errors_finalOriginalWort">
             <g:renderErrors bean="${protocol}" field="finalOriginalWort" as="list" />
            </div>
            <input type="text" onkeyup="$('#errors_finalOriginalWort').slideUp(100)" name="finalOriginalWort" value="${formatNumber(format:'##0.0#', number: protocol.finalOriginalWort)}" /> 
        </div>
      </fieldset>

      <div class="clear"></div>
    </form>
  </div>

  <div class="clear"></div>

</div>