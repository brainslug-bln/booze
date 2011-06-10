<div class="protocol">
  <g:form controller="protocol">

    <div class="protocolChart">
      <div class="chartImage temperature">
        <a href='${createLink(controller: "protocol", action: "showTemperatureChart", params: [id: protocolInstance.id, width: "700", height: "400"])}' rel="lightbox[charts]">
          <img src='${createLink(controller: "protocol", action: "showTemperatureChart", params: [id: protocolInstance.id, width: "350", height: "200"])}'/>
        </a>
      </div>
      <div class="chartImage pressure">
        <a href='${createLink(controller: "protocol", action: "showPressureChart", params: [id: protocolInstance.id, width: "700", height: "400"])}' rel="lightbox[charts]">
          <img src='${createLink(controller: "protocol", action: "showPressureChart", params: [id: protocolInstance.id, width: "350", height: "200"])}'/>
        </a>
      </div>
    </div>

    <div class="twoColumn">
      <div class="column editProtocolArea">
        <div class="yform">

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

              <label for="powerConsumption"><g:message code="protocol.powerConsumption"/></label>
              <div class="type-text">
                <g:message code="default.formatter.kilowatt" args="${[protocolInstance.powerConsumption?formatNumber(format:'####0.00', number: protocolInstance.powerConsumption?.div(1000)):0]}"/>
              </div>
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
                <label for="finalMeshingWort"><g:message code="protocol.finalMeshingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalMeshingWort', 'error')}">
                  <input type="text" name="finalMeshingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalMeshingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
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
                <label for="targetPreCookingWort"><g:message code="protocol.targetPreCookingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'targetPreCookingWort', 'error')}">
                  <input type="text" name="targetPreCookingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.targetPreCookingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalPreCookingWort"><g:message code="protocol.finalPreCookingWort"/></label>
                <div class="type-text ${hasErrors(bean: protocolInstance, field: 'finalPreCookingWort', 'error')}">
                  <input type="text" name="finalPreCookingWort" value="${formatNumber(format: '##0.0#', number: protocolInstance.finalPreCookingWort)}"/> <span class="unit"><g:message code="default.unit.plato"/></span>
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
                <label for="mainWaterVolume"><g:message code="protocol.mainWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="mainWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.mainWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="mainWaterTemperature"><g:message code="protocol.mainWaterTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="mainWaterTemperature" value="${formatNumber(format: '###0.##', number: protocolInstance.mainWaterTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
                </div>
              </div>
            </div>

            <div clas="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="targetSecondWaterVolume"><g:message code="protocol.targetSecondWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="targetSecondWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.targetSecondWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalSecondWaterVolume"><g:message code="protocol.finalSecondWaterVolume"/></label>
                <div class="type-text">
                  <input type="text" name="finalSecondWaterVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.finalSecondWaterVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
            </div>

            <div clas="clearfix"></div>

            <div class="twoColumn">
              <div class="column">
                <label for="meshTemperature"><g:message code="protocol.meshTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="meshTemperature" value="${formatNumber(format: '##0.##', number: protocolInstance.meshTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
                </div>
              </div>
              <div class="column">
                <label for="secondWaterTemperature"><g:message code="protocol.secondWaterTemperature"/></label>
                <div class="type-text">
                  <input type="text" name="secondWaterTemperature" value="${formatNumber(format: '##0.##', number: protocolInstance.secondWaterTemperature)}"/> <span class="unit"><g:message code="default.unit.degrees.celsius"/></span>
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
                <label for="targetFinalVolume"><g:message code="protocol.targetFinalVolume"/></label>
                <div class="type-text">
                  <input type="text" name="targetFinalVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.targetFinalVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
                </div>
              </div>
              <div class="column">
                <label for="finalVolume"><g:message code="protocol.finalVolume"/></label>
                <div class="type-text">
                  <input type="text" name="finalVolume" value="${formatNumber(format: '###0.##', number: protocolInstance.finalVolume)}"/> <span class="unit"><g:message code="default.unit.liter"/></span>
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
              <g:fieldValue field="yeastName" bean="${protocolInstance}"/> (<g:fieldValue field="yeastDescription" bean="${protocolInstance}"/>)
            </div>
          </fieldset>
        </div>
      </div>
      <div class="column">
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
          <div class="fieldsetHeadline"><g:message code="protocol.malt"/></div>
          <fieldset>
            <div class="type-table">
              <g:render template="/recipe/editMalt" model="${[containerInstance: protocolInstance]}"/>
            </div>
          </fieldset>

          <div class="fieldsetHeadline"><g:message code="protocol.rests"/></div>
          <fieldset>
            <div class="type-table">
              <g:render template="/recipe/editRests" model="${[containerInstance: protocolInstance, hideBrewParams: true]}"/>
            </div>
          </fieldset>


          <div class="fieldsetHeadline"><g:message code="protocol.hop"/></div>
          <fieldset>
            <div class="type-table">
              <g:render template="/recipe/editHops" model="${[containerInstance: protocolInstance]}"/>
            </div>
          </fieldset>

          <div class="fieldsetHeadline"><g:message code="protocol.additives"/></div>
          <fieldset>
            <div class="type-table">
              <g:render template="/recipe/editAdditives" model="${[containerInstance: protocolInstance]}"/>
            </div>
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

    </div>

    
  </g:form>
</div>