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
              <label class="${hasErrors(bean:protocol,field:'finalMeshingWort', 'error')}"><g:message code="protocol.finalMeshingWort"/></label>
              <div class="type-text">
                <input type="text" name="finalMeshingWort" value="${formatNumber(number:protocol.finalMeshingWort, format: '##0.00')}" /> <g:message code="default.unit.plato"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalPreCookingWort', 'error')}"><g:message code="protocol.finalPreCookingWort"/></label>
              <div class="type-text">
                <input type="text" name="finalPreCookingWort" value="${formatNumber(number:protocol.finalPreCookingWort, format: '##0.00')}" /> <g:message code="default.unit.plato"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalSecondWaterVolume', 'error')}"><g:message code="protocol.finalSecondWaterVolume"/></label>
              <div class="type-text">
                <input type="text" name="finalSecondWaterVolume" value="${formatNumber(number:protocol.finalSecondWaterVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
              </div>

            </fieldset>
          </div>
          <div class="column">
            <fieldset>
              <label class="${hasErrors(bean:protocol,field:'dilutionWaterVolume', 'error')}"><g:message code="protocol.dilutionWaterVolume"/></label>
              <div class="type-text">
                <input type="text" name="dilutionWaterVolume" value="${formatNumber(number:protocol.dilutionWaterVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
              </div>

              <label class="${hasErrors(bean:protocol,field:'finalVolume', 'error')}"><g:message code="protocol.finalVolume"/></label>
              <div class="type-text">
                <input type="text" name="finalVolume" value="${formatNumber(number:protocol.finalVolume, format: '##0.00')}" /> <g:message code="default.unit.liter"/>
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

    <div class="buttonArea">
      <input type="button" name="saveProtocolData" onclick="booze.brew.saveProtocolData()" value="${message(code: 'brew.init.saveProtocolData')}"/>
    </div>
  </div>

</div>

 </body>
 </html>