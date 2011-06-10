 <html>
 <head>
   <meta name="layout" content="empty"/>
 </head>

 <body>
<div class="editProtocolWindow">
  <h4><g:message code="brew.init.editProtocolHeadline"/></h4>
  <p><g:message code="brew.init.editProtocolInfo" /></p>

  <div class="editProtocolArea">

    <g:hasErrors bean="${protocol}">
      <p><g:message code="brew.editProtocoldata.pleaseCorrectErrors" /></p>  
    </g:hasErrors>
    
    <div class="yform">
      <form name="protocolDataForm" action="${createLink(action:'saveProtocolData', controller:'brew')}" id="protocolDataForm" method="POST">

        <div class="twoColumn">
          <div class="column">
            <fieldset>
              <label class="${hasErrors(bean:protocol,field:'finalPreSpargingWort', 'error')}"><g:message code="protocol.finalPreSpargingWort"/></label>
              <div class="type-text">
                <input type="text" name="finalPreSpargingWort" value="${formatNumber(number:protocol.finalPreSpargingWort, format: '##0.00')}" /> <g:message code="default.unit.plato"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalPostSpargingWort', 'error')}"><g:message code="protocol.finalPostSpargingWort"/></label>
              <div class="type-text">
                <input type="text" name="finalPostSpargingWort" value="${formatNumber(number:protocol.finalPostSpargingWort, format: '##0.00')}" /> <g:message code="default.unit.plato"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalSpargingWaterVolume', 'error')}"><g:message code="protocol.finalSpargingWaterVolume"/></label>
              <div class="type-text">
                <input type="text" name="finalSpargingWaterVolume" value="${formatNumber(number:protocol.finalSpargingWaterVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
              </div>

            </fieldset>
          </div>
          <div class="column">
            <fieldset>
              <label class="${hasErrors(bean:protocol,field:'dilutionWaterVolume', 'error')}"><g:message code="protocol.dilutionWaterVolume"/></label>
              <div class="type-text">
                <input type="text" name="dilutionWaterVolume" value="${formatNumber(number:protocol.dilutionWaterVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalBeerVolume', 'error')}"><g:message code="protocol.finalVolume"/></label>
              <div class="type-text">
                <input type="text" name="finalBeerVolume" value="${formatNumber(number:protocol.finalBeerVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalOriginalWort', 'error')}"><g:message code="protocol.finalOriginalWort"/></label>
              <div class="type-text">
                <input type="text" name="finalOriginalWort" value="${formatNumber(number:protocol.finalOriginalWort, format: '##0.00')}" /> <g:message code="default.unit.plato"/>
              </div>
            </fieldset>
          </div>
        </div>
        <div class="clearfix"></div>
      </form>
    </div>

    <div class="clearfix"></div>
  </div>

</div>

 </body>
 </html>