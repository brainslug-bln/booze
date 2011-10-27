<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="brew"/>

    <title><g:message code="manualMode.init.headline" /></title>
</head>
<body>

  <div id="bodyContainer" class="body brew manualMode">
    <div class="pageHeadline"><g:message code="manualMode.init.headline" /></div>

    <div class="aliveThrobber">
      <div id="aliveThrobber"></div>
    </div>

    <div class="recipeStartTime">
      <span id="timeElapsed">0</span> <g:message code="manualMode.init.elapsedMinutes"/>
    </div>

    <div class="clearfix"></div>

    <div class="twoColumn">

      <% /* Info column */ %>
      <div class="column">

        <div class="box temperatureSensorContainer">
          <div class="headline"><g:message code="manualMode.init.temperatureSensors.headline"/></div>
          <div class="boxContent">

            <% /* Inner temperature sensors */ %>
            <g:each in="${manualMode.innerTemperatureSensors?.sort{it.name}}" var="temperatureSensor">
              <div class="temperatureSensor" id="temperatureSensor_${temperatureSensor.id.encodeAsHTML()}">
                <div class="temperatureSensorName">${temperatureSensor.name?.encodeAsHTML()}</div>
                <div class="temperatureContainer">
                  <div class="temperature"></div>
                </div>
              </div>
            </g:each>

            <% /* Outer temperature sensors */ %>
            <g:each in="${manualMode.outerTemperatureSensors?.sort{it.name}}" var="temperatureSensor">
              <div class="temperatureSensor" id="temperatureSensor_${temperatureSensor.id.encodeAsHTML()}">
                <div class="temperatureSensorName">${temperatureSensor.name?.encodeAsHTML()}</div>
                <div class="temperatureContainer">
                  <div class="temperature"></div>
                </div>
              </div>
            </g:each>

          </div>
        </div>


        <% /* Pressure sensors */ %>
        <div class="box pressureSensorContainer">
          <div class="headline"><g:message code="manualMode.init.pressureSensors.headline"/></div>
          <div class="boxContent">
            <g:each in="${manualMode.pressureSensors?.sort{it.name}}" var="pressureSensor">
              <div class="pressureSensor" id="pressureSensor_${pressureSensor.id.encodeAsHTML()}">
                <div class="pressureSensorName">${pressureSensor.name?.encodeAsHTML()}</div>
                <div class="pressureContainer">
                  <div class="pressure"></div>
                </div>
              </div>
            </g:each>
          </div>
        </div>
      </div>

      <% /* Sensor/Regulation column**/ %>
      <div class="column" style="margin-left: 1%; width: 48%">
        <div class="box heaterBox">
          <div class="headline"><g:message code="manualMode.init.heaters.headline"/></div>
          <div class="boxContent">
            <g:each in="${manualMode.heaters?.sort{it.name}}" var="heater">
              <div class="heater" id="heater_${heater.id.encodeAsHTML()}">
                <div class="heaterName">${heater.name?.encodeAsHTML()}</div>
                <div class="heaterStatus" >
                  <input type="button" class="onIcon" onclick="booze.manualMode.toggleHeater('${heater.id.encodeAsHTML()}');" />
                  <input type="button" class="offIcon active" onclick="booze.manualMode.toggleHeater('${heater.id.encodeAsHTML()}');" />
                </div>
              </div>
              <div class="clearfix"></div>
            </g:each>
            <div class="clearfix"></div>
          </div>
        </div>

        <div class="box pumpBox">
          <div class="headline"><g:message code="manualMode.init.pump.headline"/></div>
          <div class="boxContent">
            <div class="pump" id="pump_${manualMode.pump.id.encodeAsHTML()}">
              <div class="pumpName">${manualMode.pump.name?.encodeAsHTML()}</div>

              <div class="pumpStatus">
                <div class="onIcon"></div>
                <div class="offIcon active"></div>
              </div>

              <div class="pumpMode">
                <input type="button" onclick="booze.manualMode.pumpModeSelector(event, this);
                return false;" class="mode_0" style="display: none">&nbsp;</a>
                <input type="button" onclick="booze.manualMode.pumpModeSelector(event, this);
                return false;" class="mode_1" style="display: none">&nbsp;</a>
                <input type="button" onclick="booze.manualMode.pumpModeSelector(event, this);
                return false;" class="mode_2">&nbsp;</a>

                <div class="pumpModeSelector" style="display: none;">
                  <ul>
                    <li class="headline"><span><g:message code="manualMode.init.setPumpMode"/></span></li>
                    <g:each in="${pumpModes}" var="pumpMode">
                      <li onclick="booze.manualMode.setPumpMode(${manualMode.pump.id.encodeAsHTML()}, ${pumpMode.id.encodeAsHTML()});
                      return false"><span><g:pumpModeName pumpMode="${pumpMode}"/></span></li>
                    </g:each>
                  </ul>
                </div>
              </div>
            </div>

            <div class="clearfix"></div>
          </div>
        </div>

        <div class="clearfix">&nbsp;</div>

        <div class="box buttonContainer">
          <div class="headline"><g:message code="manualMode.init.buttons.headline"/></div>
          <div class="boxContent buttonArea">

            <div class="cancelButtonContainer">
              <input type="button" onclick="booze.manualMode.stop()" id="cancelButton" value="${message(code: 'manualMode.buttons.cancel')}"/>
            </div>

            <div class="pauseButtonContainer">
              <input type="button" onclick="booze.manualMode.showCalculator()" id="showCalculatorButton" value="${message(code: 'manualMode.buttons.showCalculator')}"/>
            </div>

            <div class="clearfix"></div>
          </div>
        </div>
      </div>

      <div class="clearfix"></div>

    </div>
  </div>
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
    booze.manualMode.resumeLostSession('${manualMode.processId}');
    });
  </g:javascript>
</g:if>
<g:else>
  <g:javascript>
    Event.observe(window, 'load', function() {
    booze.manualMode.init('${manualMode.processId}');
    });
  </g:javascript>
</g:else>
</body>
</html>
