<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="brew"/>

  <title><g:message code="brew.init.headline" args="${[brewProcess.recipe.name?.encodeAsHTML()]}"/></title>

  <jawr:script src="/bundles/brew.js"/>
</head>
<body>

<div id="bodyContainer" class="body brew">
  <div class="recipeName">${brewProcess.recipe.name?.encodeAsHTML()}</div>

  <div class="aliveThrobber">
    <div id="aliveThrobber"></div>
  </div>

  <div class="recipeStartTime">
    <span id="timeElapsed">0</span> <g:message code="brew.init.elapsedMinutes"/>
  </div>

  <div class="clearfix"></div>

  <div class="twoColumn">

    <% /* Info column */ %>
    <div class="column">

      <div class="box stepInfo" id="step">
        <div class="headline"><g:message code="brew.init.stepInfo.headline"/></div>
        <div class="boxContent">
          <g:render template="stepInfo"/>
        </div>
      </div>

      <div class="box protocolContainer">
        <div class="headline"><g:message code="brew.init.protocol.headline"/></div>
        <div class="boxContent">
          <ul class="protocolList" id="protocol">
            <g:if test="${resume}">
              <li><g:message code="brew.brewProcess.event" args="${[formatDate(date:(new Date()), formatName: 'default.time.formatter'), message(code:'brew.process.resumed')]}"/></li>
              <g:each in="${events?.reverse()}" var="event">
                <li>${event.getEventDataForFrontend(g)?.message}</li>
              </g:each>
            </g:if>
            <g:else>
              <li><g:message code="brew.brewProcess.event" args="${[formatDate(date:(new Date()), formatName: 'default.time.formatter'), message(code:'brew.process.initialized')]}"/></li>
            </g:else>
          </ul>
        </div>
        <div class="addComment"><input type="text" value="" id="brewCommentField"/> <a href="#" onclick="booze.brew.addComment();
        return false;"><g:message code="brew.init.addComment"/></a></div>
      </div>

      <div class="box buttonContainer">
        <div class="headline"><g:message code="brew.init.buttons.headline"/></div>
        <div class="boxContent buttonArea">

          <div class="cancelButtonContainer">
            <input type="button" onclick="booze.brew.cancel()" id="cancelButton" value="${message(code: 'brew.buttons.cancel')}"/>
          </div>

          <div class="pauseButtonContainer">
            <input onclick="booze.brew.pause()" type="button" id="pauseButton" value="${message(code: 'brew.buttons.pause')}"/>
            <input onclick="booze.brew.resume()" style="display: none" type="button" id="resumeButton" value="${message(code: 'brew.buttons.resume')}"/>
          </div>

          <div class="clearfix" style="height: 15px; width: 100%; display: block; float: left;">&nbsp;</div>

          <div class="cancelButtonContainer">
            <input type="button" onclick="booze.brew.showCalculator()" id="showCalculatorButton" value="${message(code: 'brew.buttons.showCalculator')}"/>
          </div>
          
          <div class="editProtocolButtonContainer">
            <input type="button" onclick="booze.brew.editProtocolData()" id="editProtocoldataButton" value="${message(code: 'brew.buttons.editProtocolData')}"/>
          </div>

          <div class="clearfix"></div>
        </div>
      </div>
    </div>

    <% /* Sensor/Regulation column**/ %>
    <div class="column">

      <div class="box temperatureSensorContainer">
        <div class="headline"><g:message code="brew.init.temperatureSensors.headline"/></div>
        <div class="boxContent">

          <% /* Inner temperature sensors */ %>
          <g:each in="${brewProcess.innerTemperatureSensors?.sort{it.name}}" var="temperatureSensor">
            <div class="temperatureSensor" id="temperatureSensor_${temperatureSensor.id.encodeAsHTML()}">
              <div class="temperatureSensorName">${temperatureSensor.name?.encodeAsHTML()}</div>
              <div class="temperatureContainer">
                <div class="temperature"></div>
              </div>
            </div>
          </g:each>

          <% /* Outer temperature sensors */ %>
          <g:each in="${brewProcess.outerTemperatureSensors?.sort{it.name}}" var="temperatureSensor">
            <div class="temperatureSensor" id="temperatureSensor_${temperatureSensor.id.encodeAsHTML()}">
              <div class="temperatureSensorName">${temperatureSensor.name?.encodeAsHTML()}</div>
              <div class="temperatureContainer">
                <div class="temperature"></div>
              </div>
            </div>
          </g:each>

          <div class="temperatureReference">
            <g:message code="brew.init.referenceSensors"/> <span id="temperatureReference"></span>
          </div>
        </div>
      </div>

      <% /* Pressure sensors */ %>
      <div class="box pressureSensorContainer">
        <div class="headline"><g:message code="brew.init.pressureSensors.headline"/></div>
        <div class="boxContent">
          <g:each in="${brewProcess.pressureSensors?.sort{it.name}}" var="pressureSensor">
            <div class="pressureSensor" id="pressureSensor_${pressureSensor.id.encodeAsHTML()}">
              <div class="pressureSensorName">${pressureSensor.name?.encodeAsHTML()}</div>
              <div class="pressureContainer">
                <div class="pressure"></div>
              </div>
            </div>
          </g:each>
        </div>
      </div>

      <div class="twoColumn">
        <% /* Heater column */ %>
        <div class="column">
          <div class="box heaterBox">
            <div class="headline"><g:message code="brew.init.heaters.headline"/></div>
            <div class="boxContent">
              <g:each in="${brewProcess.heaters?.sort{it.name}}" var="heater">
                <div class="heater" id="heater_${heater.id.encodeAsHTML()}">
                  <div class="heaterName">${heater.name?.encodeAsHTML()}</div>
                  <div class="heaterStatus">
                    <div class="onIcon"></div>
                    <div class="offIcon active"></div>
                    <div class="forceOnIcon"></div>
                  </div>
                </div>
                <div class="clearfix"></div>
              </g:each>
              <div class="clearfix"></div>
            </div>
          </div>
        </div>

        <% /* Pump column */ %>
        <div class="column">
          <div class="box pumpBox">
            <div class="headline"><g:message code="brew.init.pump.headline"/></div>
            <div class="boxContent">
              <div class="pump" id="pump_${brewProcess.pump.id.encodeAsHTML()}">
                <div class="pumpName">${brewProcess.pump.name?.encodeAsHTML()}</div>

                <div class="pumpStatus">
                  <div class="onIcon"></div>
                  <div class="offIcon active"></div>
                </div>

                <div class="pumpMode">
                  <input type="button" onclick="booze.brew.pumpModeSelector(event, this);
                  return false;" class="mode_0" style="display: none">&nbsp;</a>
                  <input type="button" onclick="booze.brew.pumpModeSelector(event, this);
                  return false;" class="mode_1" style="display: none">&nbsp;</a>
                  <input type="button" onclick="booze.brew.pumpModeSelector(event, this);
                  return false;" class="mode_2">&nbsp;</a>
                  <span class="forced" style="display: none;" title="${message(code: 'brew.init.pumpModeForced')}">&nbsp;</span>

                  <div class="pumpModeSelector" style="display: none;">
                    <ul>
                      <li class="headline"><span><g:message code="brew.init.forcePumpMode"/></span></li>
                      <li style="display: none;" class="unforcePumpModeButton" onclick="booze.brew.unforcePumpMode(${brewProcess.pump.id.encodeAsHTML()});
                      return false;"><span><g:message code="brew.init.unforcePumpMode"/></span></li>

                      <g:each in="${pumpModes}" var="pumpMode">
                        <li onclick="booze.brew.forcePumpMode(${brewProcess.pump.id.encodeAsHTML()}, ${pumpMode.id.encodeAsHTML()});
                        return false"><span><g:pumpModeName pumpMode="${pumpMode}"/></span></li>
                      </g:each>
                    </ul>
                  </div>
                </div>
              </div>



              <div class="clearfix"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="clearfix"></div>

      <div class="twoColumn">
        <% /* Slider for heater hysteresis adaptation */ %>
        <div class="column">
          <div class="box heaterHysteresisRegulator">
            <div class="headline"><g:message code="brew.init.hysteresisRegulator.headline"/></div>
            <div class="boxContent">
              <div id="temperatureOffsetSlider">
                <div id="temperatureOffsetHandle">
                  ${brewProcess.setting.heatingTemperatureOffset.encodeAsHTML()}
                </div>
              </div>
              <div class="row" style="width: 100%;">
                <div class="cautious">
                  <g:message code="brew.init.cautious"/>
                </div>

                <div class="aggressive">
                  <g:message code="brew.init.aggressive"/>
                </div>

                <div class="clearfix"></div>
              </div>
              <input type="hidden" id="defaultTemperatureOffset" value="${brewProcess.setting.heatingTemperatureOffset.encodeAsHTML()}"/>
            </div>
          </div>
        </div>

        <% /* Slider for cooking temperature adaptation */ %>
        <div class="column">
          <div id="cookingTemperatureBox" class="box cookingTemperaturRegulator">
            <div class="headline"><g:message code="brew.init.cookingTemperatureRegulator.headline"/></div>
            <div class="boxContent">
              <div id="cookingTemperatureSlider">
                <div id="cookingTemperatureHandle">
                  ${brewProcess.setting.cookingTemperature.encodeAsHTML()}
                </div>
              </div>
              <div class="row" style="width: 100%;">
                <div class="low">
                  <g:message code="brew.init.low"/>
                </div>

                <div class="high">
                  <g:message code="brew.init.high"/>
                </div>

                <div class="clearfix"></div>
              </div>
              <input type="hidden" id="defaultCookingTemperature" value="${brewProcess.setting.cookingTemperature.encodeAsHTML()}"/>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<% /* Initialization window */ %>
<div style="display: none" id="initWindowContent">
  <g:render template="brewInitWindow"/>
</div>

<% /* Fill temperature reached window */ %>
<div style="display: none" id="fillTemperatureReachedWindowContent">
  <g:render template="fillWindow"/>
</div>


<% /* Meshing temperature reached window */ %>
<div style="display: none" id="meshingTemperatureReachedWindowTemplate">
  <g:render template="meshingTemperatureReachedWindow"/>
</div>

<% /* Add hop window */ %>
<div style="display: none" id="addHopWindowTemplate">
  <g:render template="addHopWindow"/>
</div>

<% /* Cooking finished window */ %>
<div style="display: none" id="cookingFinishedWindowTemplate">
  <g:render template="cookingFinishedWindow"/>
</div>

<% /* Brew calculator window */ %>
<div style="display: none" id="brewCalculatorWindowTemplate">
  <div class="brewCalculatorWindow">
    <g:render template="/calculator/calculator"/>
  </div>
</div>

<g:if test="${resume == true}">
  <g:javascript>
          Event.observe(window, 'load', function() {
              booze.brew.resumeLostSession('${brewProcess.processId}');
            });
  </g:javascript>
</g:if>
<g:else>
  <g:javascript>
          Event.observe(window, 'load', function() {
            booze.brew.init('${brewProcess.processId}');
          });
  </g:javascript>
</g:else>
</body>
</html>
