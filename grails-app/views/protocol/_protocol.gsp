<div class="protocol">
  <g:form controller="protocol">
      <div class="column50percent editProtocolArea">

          <div class="fieldsetHeadline"><g:message code="protocol.fieldset.commonData"/></div>
          <fieldset>
            <label for="recipeName"><g:message code="protocol.recipeName"/></label>
            <div class="long type-text ${hasErrors(bean: protocolInstance, field: 'recipeName', 'error')}">
              <input type="text" name="recipeName" value="${fieldValue(bean: protocolInstance, field: 'recipeName')}"/>
            </div>

            <div class="clearfix"></div>

            <label for="recipeDescription"><g:message code="protocol.recipeDescription"/></label>
            <div class="long type-text ${hasErrors(bean: protocolInstance, field: 'recipeDescription', 'error')}">
              <input type="text" name="recipeDescription" value="${fieldValue(bean: protocolInstance, field: 'recipeDescription')}"/>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="dateStarted"><g:message code="protocol.dateStarted"/></label>
                <div class="type-text">
                  <g:formatDate formatName="default.dateTime.formatter" date="${protocolInstance.dateStarted}"/>
                </div>
              </div>
              <div class="column">
                <label for="dateFinished"><g:message code="protocol.dateFinished"/></label>
                <div class="type-text">
                  <g:if test="${protocolInstance.dateFinished}">
                    <g:formatDate formatName="default.dateTime.formatter" date="${protocolInstance.dateFinished}"/>
                  </g:if>
                  <g:else>
                    <g:message code="protocol.notFinished"/>
                  </g:else>
                </div>
              </div>

              <div class="clearfix"></div>
            </div>

            <div class="clearfix"></div>

          </fieldset>

          <div class="fieldsetHeadline"><g:message code="protocol.fieldset.outputData"/></div>
          <fieldset>
            <div class="twoColumn">
              <div class="column">
                <label for="ibu"><g:message code="protocol.ibu"/></label>
                <div class="type-text">
                  <g:formatNumber format="#0.0#" number="${protocolInstance.ibu}"/>
                </div>
              </div>
              <div class="column">
                <label for="alcohol"><g:message code="protocol.alcohol"/></label>
                <div class="type-text">
                  <input type="text" name="alcohol" value="${formatNumber(format: '##0.0#', number: protocolInstance.alcohol)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetOriginalWort"><g:message code="protocol.targetOriginalWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'targetOriginalWort', 'error')}">
                  <input type="text" name="targetOriginalWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetOriginalWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalOriginalWort"><g:message code="protocol.finalOriginalWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalOriginalWort', 'error')}">
                  <input type="text" name="finalOriginalWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalOriginalWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>

                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="finalPreSpargingWort"><g:message code="protocol.finalPreSpargingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalPreSpargingWort', 'error')}">
                  <input type="text" name="finalPreSpargingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalPreSpargingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
              <div class="column">
                <label for="ebc"><g:message code="protocol.ebc"/></label>
                <div class="type-text">
                  <input type="text" name="ebc" value="${formatNumber(format: '##0.0#', number: protocolInstance.ebc)}"/> <span class="unit"><g:message code="default.unit.ebc"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetPostSpargingWort"><g:message code="protocol.targetPostSpargingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'targetPostSpargingWort', 'error')}">
                  <input type="text" name="targetPostSpargingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetPostSpargingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalPostSpargingWort"><g:message code="protocol.finalPostSpargingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalPostSpargingWort', 'error')}">
                  <input type="text" name="finalPostSpargingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalPostSpargingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetBottlingWort"><g:message code="protocol.targetBottlingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'targetBottlingWort', 'error')}">
                  <input type="text" name="targetBottlingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetBottlingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalBottlingWort"><g:message code="protocol.finalBottlingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalBottlingWort', 'error')}">
                  <input type="text" name="finalBottlingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalBottlingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>
          </fieldset>

          <div class="fieldsetHeadline"><g:message code="protocol.fieldset.processData"/></div>
          <fieldset>
            <div class="twoColumn">
              <div class="column">
                <label for="mashingWaterVolume"><g:message code="protocol.mashingWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="mashingWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.mashingWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="mashingTemperature"><g:message code="protocol.mashingTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="mashingTemperature" value="${formatNumber(format: '###0.##', number: protocolInstance.mashingTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
                </div>
              </div>
            </div>

            <div clas="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetSpargingWaterVolume"><g:message code="protocol.targetSpargingWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="targetSpargingWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.targetSpargingWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalSpargingWaterVolume"><g:message code="protocol.finalSpargingWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="finalSpargingWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.finalSpargingWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
            </div>

            <div clas="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="lauterTemperature"><g:message code="protocol.lauterTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="lauterTemperature" value="${formatNumber(format: '##0.##', number: protocolInstance.lauterTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
                </div>
              </div>
              <div class="column">
                <label for="spargingTemperature"><g:message code="protocol.spargingTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="spargingTemperature" value="${formatNumber(format: '##0.##', number: protocolInstance.spargingTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="dilutionWaterVolume"><g:message code="protocol.dilutionWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="dilutionWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.dilutionWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                &nbsp;
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetCookingTime"><g:message code="protocol.targetCookingTime"/></label>
                <div class="type-text">
                  <input type="text" name="targetCookingTime" value="${formatNumber(format: '###0.##', number: protocolInstance.targetCookingTime)}"/> <span class="unit"><g:message code="default.unit.minutes"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalCookingTime"><g:message code="protocol.finalCookingTime"/></label>
                <div class="type-text">
                  <input type="text" name="finalCookingTime" value="${formatNumber(format: '###0.##', number: protocolInstance.finalCookingTime)}"/> <span class="unit"><g:message code="default.unit.minutes"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetFinalBeerVolume"><g:message code="protocol.targetFinalBeerVolume"/></label>
                <div class="type-text">
                  <input type="text" name="targetFinalBeerVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.targetFinalBeerVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalBeerVolume"><g:message code="protocol.finalBeerVolume"/></label>
                <div class="type-text">
                  <input type="text" name="finalBeerVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.finalBeerVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="fareVolume"><g:message code="protocol.fareVolume"/></label>
                <div class="type-text">
                  <input type="text" name="fareVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.fareVolume)}"/> <span class="unit"><g:message code="default.unit.mlperliter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="fareConcentration"><g:message code="protocol.fareConcentration"/></label>
                <div class="type-text">
                    <input type="text" name="fareConcentration" value="${formatNumber(format: '###0.##', number: protocolInstance.fareConcentration)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>

            <label for="yeast"><g:message code="protocol.yeast"/></label>
            <div class="type-text">
              <g:fieldValue field="yeast" bean="${protocolInstance}"/> 
            </div>
          </fieldset>
        </div>

      <div class="column50percent">
        <div class="yform">
          <div class="fieldsetHeadline"><g:message code="protocol.fieldset.messages"/></div>
          <fieldset>
            <div class="protocolContainer">
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

        <div class="yform">
          
          </fieldset>

        </div>

        <div class="clearfix"></div>
        <div class="yform">
          <div class="type-button">
            <input type="hidden" name="id" value="${protocolInstance.id.encodeAsHTML()}"/>
            <span style="float: right">
              <g:actionSubmit value="${message(code:'protocol.edit.save')}" action="update"/>
            </span>

            <span style="float: left">
              <g:actionSubmit value="${message(code:'protocol.edit.delete')}" action="delete"/>
            </span>
          </div>
        </div>

      </div>
    
  </g:form>
</div>