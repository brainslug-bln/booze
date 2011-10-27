<div class="form protocol">
  <g:form controller="protocol">
    <div class="column50percent editProtocolArea">
      <h3><g:message code="protocol.fieldset.commonData"/></h3>
      <fieldset>
        <div class="row">
          <label for="recipeName"><g:message code="protocol.recipeName"/></label>
          <span class="immutable">${fieldValue(bean: protocolInstance, field: 'recipeName')}</span>
        </div>

        <div class="clearfix"></div>

        <div class="column50percent">
          <div class="row">
            <label for="dateStarted"><g:message code="protocol.dateStarted"/></label>
            <span class="immutable"><g:formatDate formatName="default.dateTime.formatter" date="${protocolInstance.dateStarted}"/></span>
          </div>
        </div>

        <div class="column50percent">
          <div class="row">
            <label for="dateFinished"><g:message code="protocol.dateFinished"/></label>
            <span class="immutable">
              <g:if test="${protocolInstance.dateFinished}">
                <g:formatDate formatName="default.dateTime.formatter" date="${protocolInstance.dateFinished}"/>
              </g:if>
              <g:else>
                <g:message code="protocol.notFinished"/>
              </g:else>
            </span>
          </div>
        </div>
      </fieldset>
    </div>
    
    <div class="column50percent">
      <h3><g:message code="protocol.fieldset.messages"/></h3>
          <fieldset>
            <div class="protocolContainer" style="height: 10em">
              <ul>
                <g:each in="${protocolInstance.events.sort{it.created}}" var="event">
                  <li>
                  <g:if test="${event.type == 'BrewAddCommentEvent'}">
                    ${g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: new Date(event.created))), event.message.encodeAsHTML()])}
                  </g:if>
                  <g:else>
                    ${g.message(code: 'brew.brewProcess.event', args: [(g.formatDate(format: g.message(code: 'default.time.formatter'), date: new Date(event.created))), g.message(code: event.message, args: event.readParsedData())])}
                  </g:else>
                  </li>
                </g:each>
              </ul>
            </div>
          </fieldset>
      </div>
    
    <div class="clear"></div>
    
    <h3><g:message code="protocol.fieldset.outputData"/></h3>

    <div class="column50percent">
      <div class="row">
        <label for="ibu"><g:message code="protocol.ibu"/></label>
        <span class="immutable">
          <g:formatNumber format="##0.0#" number="${protocolInstance.ibu}"/>
        </span>
      </div>

      <div class="row">
        <label for="ebc"><g:message code="protocol.ebc"/></label>
        <span class="immutable">
          <g:formatNumber format="##0.0#" number="${protocolInstance.ebc}"/>
        </span>
      </div>

      <div class="row">
        <label for="targetOriginalWort"><g:message code="protocol.targetOriginalWort"/></label>
        <div id="errors_targetOriginalWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetOriginalWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetOriginalWort)}" name="targetOriginalWort">
      </div>

      <div class="row">
        <label for="targetPreSpargingWort"><g:message code="protocol.targetPreSpargingWort"/></label>
        <div id="errors_targetPreSpargingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetPreSpargingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetPreSpargingWort)}" name="targetPreSpargingWort">
      </div>

      <div class="row">
        <label for="targetPostSpargingWort"><g:message code="protocol.targetPostSpargingWort"/></label>
        <div id="errors_targetPostSpargingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetPostSpargingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetPostSpargingWort)}" name="targetPostSpargingWort">
      </div>

      <div class="row">
        <label for="targetBottlingWort"><g:message code="protocol.targetBottlingWort"/></label>
        <div id="errors_targetBottlingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetBottlingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetBottlingWort)}" name="targetBottlingWort">
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="alcohol"><g:message code="protocol.alcohol"/></label>
        <span class="immutable">
          <g:formatNumber format="##0.0#" number="${protocolInstance.alcohol}"/>
        </span>
      </div>

      <div class="row"></div>

      <div class="row">
        <label for="finalOriginalWort"><g:message code="protocol.finalOriginalWort"/></label>
        <div id="errors_finalOriginalWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalOriginalWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalOriginalWort)}" name="finalOriginalWort">
      </div>

      <div class="row">
        <label for="finalPreSpargingWort"><g:message code="protocol.finalPreSpargingWort"/></label>
        <div id="errors_finalPreSpargingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalPreSpargingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalPreSpargingWort)}" name="finalPreSpargingWort">
      </div>

      <div class="row">
        <label for="finalPostSpargingWort"><g:message code="protocol.finalPostSpargingWort"/></label>
        <div id="errors_finalPostSpargingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalPostSpargingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalPostSpargingWort)}" name="finalPostSpargingWort">
      </div>

      <div class="row">
        <label for="finalBottlingWort"><g:message code="protocol.finalBottlingWort"/></label>
        <div id="errors_finalBottlingWort" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalBottlingWort').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalBottlingWort)}" name="finalBottlingWort">
      </div>
    </div>
    
    <div class="clear"></div>

    <h3><g:message code="protocol.fieldset.processData"/></h3>

    <div class="column50percent">
      <div class="row">
        <label for="mashingWaterVolume"><g:message code="protocol.mashingWaterVolume"/></label>
        <div id="errors_mashingWaterVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_mashingWaterVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.mashingWaterVolume)}" name="mashingWaterVolume">
      </div>

      <div class="row">
        <label for="targetSpargingWaterVolume"><g:message code="protocol.targetSpargingWaterVolume"/></label>
        <div id="errors_targetSpargingWaterVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetSpargingWaterVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.targetSpargingWaterVolume)}" name="targetSpargingWaterVolume">
      </div>

      <div class="row">
        <label for="lauterTemperature"><g:message code="protocol.lauterTemperature"/></label>
        <div id="errors_lauterTemperature" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_lauterTemperature').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.lauterTemperature)}" name="lauterTemperature">
      </div>
      
      <div class="row">
        <label for="dilutionWaterVolume"><g:message code="protocol.dilutionWaterVolume"/></label>
        <div id="errors_dilutionWaterVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_dilutionWaterVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.dilutionWaterVolume)}" name="dilutionWaterVolume">
      </div>
      
      <div class="row">
        <label for="targetCookingTime"><g:message code="protocol.targetCookingTime"/></label>
        <div id="errors_targetCookingTime" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_targetCookingTime').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0', number: protocolInstance.targetCookingTime)}" name="targetCookingTime">
      </div>
      
      <div class="row">
        <label for="targetFinalBeerVolume"><g:message code="protocol.targetFinalBeerVolume"/></label>
        <span class="immutable">
          ${formatNumber(format: '###0.0#', number: protocolInstance.targetFinalBeerVolume)}
        </span>
      </div>
      
      <div class="row">
        <label for="fareVolume"><g:message code="protocol.fareVolume"/></label>
        <div id="errors_fareVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_fareVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.fareVolume)}" name="fareVolume">
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="mashingTemperature"><g:message code="protocol.mashingTemperature"/></label>
        <div id="errors_mashingTemperature" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_mashingTemperature').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.mashingTemperature)}" name="mashingTemperature">
      </div>
      
      <div class="row">
        <label for="finalSpargingWaterVolume"><g:message code="protocol.finalSpargingWaterVolume"/></label>
        <div id="errors_finalSpargingWaterVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalSpargingWaterVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.finalSpargingWaterVolume)}" name="finalSpargingWaterVolume">
      </div>
      
      <div class="row">
        <label for="spargingTemperature"><g:message code="protocol.spargingTemperature"/></label>
        <div id="errors_spargingTemperature" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_spargingTemperature').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0.0#', number: protocolInstance.spargingTemperature)}" name="spargingTemperature">
      </div>
      
      <div class="row">
      </div>
      
      <div class="row">
        <label for="finalCookingTime"><g:message code="protocol.finalCookingTime"/></label>
        <div id="errors_finalCookingTime" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalCookingTime').slideUp(100)" maxlength="254" value="${formatNumber(format: '##0', number: protocolInstance.finalCookingTime)}" name="finalCookingTime">
      </div>
      
      <div class="row">
        <label for="finalBeerVolume"><g:message code="protocol.finalBeerVolume"/></label>
        <div id="errors_finalBeerVolume" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_finalBeerVolume').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.finalBeerVolume)}" name="finalBeerVolume">
      </div>
      
      <div class="row">
        <label for="fareConcentration"><g:message code="protocol.fareConcentration"/></label>
        <div id="errors_fareConcentration" class="errors"> </div>
        <input class="small" type="text" onkeyup="$('#errors_fareConcentration').slideUp(100)" maxlength="254" value="${formatNumber(format: '###0.0#', number: protocolInstance.fareConcentration)}" name="fareConcentration">
      </div>
    </div>

    <div class="row">
      <label for="yeast"><g:message code="protocol.yeast"/></label>
      <div id="errors_yeast" class="errors"> </div>
      <input type="text" onkeyup="$('#errors_yeast').slideUp(100)" maxlength="254" value="${fieldValue(bean:protocolInstance, field:'yeast')}" name="yeast"></input>
    </div>

    <div class="clear"></div>
    <div class="type-button">
      <input type="hidden" name="id" value="${protocolInstance.id.encodeAsHTML()}"/>
      <span style="float: right">
        <g:actionSubmit value="${message(code:'protocol.edit.save')}" action="update"/>
      </span>

      <span style="float: left">
        <g:actionSubmit value="${message(code:'protocol.edit.delete')}" action="delete"/>
      </span>
    </div>
  </g:form>
</div>