<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="brew" />

<title><g:message code="brew.init.headline"
		args="${[brewProcess.recipe.name?.encodeAsHTML()]}" /></title>

</head>
<body>

	<div id="bodyContainer" class="brew">
		<h1>
			<span class="recipeName">
				${brewProcess.recipe.name?.encodeAsHTML()}
			</span> <img id="aliveThrobber"
				src="${resource(dir:'images/icons/brew/throbber/0.png')}" />

			<div class="recipeStartTime">
				<span id="timeElapsed">0</span>
				<g:message code="brew.init.elapsedMinutes" />
			</div>
			<div class="clear">&nbsp;</div>
		</h1>



		<div class="clear"></div>

		<% /* Info column */ %>
		<div class="leftColumn">
			<div class="contentbox stepInfo" id="step">
				<h1>
					<g:message code="brew.init.stepInfo.headline" />
				</h1>

				<div class="content">
					<g:render template="stepInfo" />
				</div>
			</div>

			<div class="contentbox protocolContainer">
				<h1>
					<g:message code="brew.init.protocol.headline" />
				</h1>

				<div class="content">
					<ul class="protocolList" id="protocol">
						<g:if test="${resume}">
							<li><g:message code="brew.brewProcess.event"
									args="${[formatDate(date:(new Date()), formatName: 'default.time.formatter'), message(code:'brew.process.resumed')]}" /></li>
							<g:each in="${events?.reverse()}" var="event">
								<li>
									${event.getEventDataForFrontend(g)?.message}
								</li>
							</g:each>
						</g:if>
						<g:else>
							<li><g:message code="brew.brewProcess.event"
									args="${[formatDate(date:(new Date()), formatName: 'default.time.formatter'), message(code:'brew.process.initialized')]}" /></li>
						</g:else>
					</ul>
				</div>
				<div class="addComment">
					<input type="text" value="" id="brewCommentField" /> <input
						type="button" class="ui-button ui-state-default"
						onclick="booze.brew.addComment(); return false;"
						value="<g:message code='brew.init.addComment'/>">
				</div>

				<script type="text/javascript">
            $('#brewCommentField').keypress(function(event) { if (event.which == "13") { booze.brew.addComment(); event.preventDefault(); }});
          </script>
			</div>

			<div class="contentbox buttonContainer">
				<h1>
					<g:message code="brew.init.buttons.headline" />
				</h1>

				<div class="content">
					<div class="leftColumn">
						<input class="left ui-button ui-state-default" type="button"
							onclick="booze.brew.cancel()" id="cancelButton"
							value="${message(code: 'brew.buttons.cancel')}" /> <input
							class="left ui-button ui-state-default" type="button"
							onclick="booze.brew.showCalculator()" id="showCalculatorButton"
							value="${message(code: 'brew.buttons.showCalculator')}" /> <input
							class="right ui-button ui-state-default" type="button"
							onclick="booze.brew.showPressureChart()"
							id="showPressureChartButton"
							value="${message(code: 'brew.buttons.showPressureChart')}" />
					</div>

					<div class="rightColumn">
						<input class="ui-button ui-state-default"
							onclick="booze.brew.pause()" type="button" id="pauseButton"
							value="${message(code: 'brew.buttons.pause')}" /> <input
							class="ui-button ui-state-default" onclick="booze.brew.resume()"
							style="display: none" type="button" id="resumeButton"
							value="${message(code: 'brew.buttons.resume')}" /> <input
							class="right ui-button ui-state-default" type="button"
							onclick="booze.brew.editProtocolData()"
							id="editProtocoldataButton"
							value="${message(code: 'brew.buttons.editProtocolData')}" /> <input
							class="right ui-button ui-state-default" type="button"
							onclick="booze.brew.showTemperatureChart()"
							id="showTemperatureChartButton"
							value="${message(code: 'brew.buttons.showTemperatureChart')}" />
					</div>


					<div class="clear"></div>
				</div>
			</div>
		</div>

		<% /* Sensor/Regulation column**/ %>
		<div class="rightColumn">

			<div class="contentbox temperatureSensorContainer">
				<h1>
					<g:message code="brew.init.temperatureSensors.headline" />
				</h1>
				<div class="content">

					<% /* temperature sensors */ %>
					<g:each in="${brewProcess.temperatureSensors?.sort{it.name}}"
						var="temperatureSensor">
						<div class="temperatureSensor"
							id="temperatureSensor_${temperatureSensor.id.encodeAsHTML()}">
							<div class="temperatureSensorName">
								${temperatureSensor.name?.encodeAsHTML()}
								<div class="temperatureSensorUnit">°C</div>
								&nbsp;
								<div class="temperatureSensorValue">0</div>
								<div class="reference ui-icon ui-icon-star"
									style="display: none;">(R)</div>
							</div>
							<div class="progressbar"></div>
						</div>

						<script type="text/javascript">
                $(document).ready(function() {
                  $('#temperatureSensor_${temperatureSensor.id.encodeAsHTML()}').find('.progressbar').first().progressbar({value: 0});
                });
              </script>
					</g:each>

				</div>
			</div>

			<% /* Pressure sensors */ %>
			<div class="contentbox pressureSensorContainer">
				<h1>
					<g:message code="brew.init.pressureSensors.headline" />
				</h1>

				<div class="content">
					<g:each in="${brewProcess.pressureSensors?.sort{it.name}}"
						var="pressureSensor">
						<div class="pressureSensor"
							id="pressureSensor_${pressureSensor.id.encodeAsHTML()}">
							<div class="pressureSensorName">
								${pressureSensor.name?.encodeAsHTML()}
								<div class="pressureSensorUnit">mbar</div>
								&nbsp;
								<div class="pressureSensorValue">0</div>
							</div>
							<div class="progressbar"></div>
						</div>

						<script type="text/javascript">
                $(document).ready(function() {
                  $('#pressureSensor_${pressureSensor.id.encodeAsHTML()}').find('.progressbar').first().progressbar({value: 0});
                });
              </script>
					</g:each>
				</div>
			</div>

			<% /* Heater column */ %>
			<div class="leftColumn">
				<div class="contentbox heaterBox">
					<h1>
						<g:message code="brew.init.heaters.headline" />
					</h1>
					<div class="content">
						<g:each in="${brewProcess.heaters?.sort{it.name}}" var="heater">

							<div class="heater" id="heater_${heater.id.encodeAsHTML()}">

								<div id="heater_${heater.id}_regular">
									<div class="name">
										${heater.name.encodeAsHTML()}
									</div>

									<div class="statusIcon ui-state-default">
										<div class="ui-icon ui-icon-play"></div>
									</div>
									<div class="manualModeIcon ui-state-default">
										<div class="ui-icon ui-icon-person"
											onclick="booze.brew.toggleForceHeater('${heater.id}')"></div>
									</div>

									<g:if test="${heater.hasRegulator()}">
										<div class="clear"></div>
										<div id="heater_${heater.id}_progressbar">
											<div class="progressbar">
												<div class="progressbarPower">
													<div class="label">
														<g:message code="brew.init.power" />
													</div>
													<div class="indicator">0</div>
													<div class="unit">
														<g:message code="default.unit.percent" />
													</div>
												</div>
											</div>
										</div>
									</g:if>
								</div>

								<div id="heater_${heater.id}_forced" style="display: none">
									<div class="name">
										${heater.name.encodeAsHTML()}
									</div>

									<div class="statusIcon ui-state-default">
										<div class="ui-icon ui-icon-play"
											onclick="booze.brew.toggleForcedHeaterStatus('${heater.id}')"></div>
									</div>
									<div class="manualModeIcon ui-state-active">
										<div class="ui-icon ui-icon-person"
											onclick="booze.brew.toggleForceHeater('${heater.id}')"></div>
									</div>

									<g:if test="${heater.hasRegulator()}">
										<div class="clear"></div>
										<div class="label">
											<g:message code="brew.init.power" />
										</div>
										<div class="indicator">0</div>
										<div class="unit">
											<g:message code="default.unit.percent" />
										</div>

										<div class="clear"></div>

										<div class="booze-icon booze-icon-arrow-left sliderButton"
											onclick="$('#heater_${heater.id}_slider').slider('value', $('#heater_${heater.id}_slider').slider('value') - $('#heater_${heater.id}_slider').slider('option', 'step')); return false;"></div>

										<div class="slider"></div>
										<div class="booze-icon booze-icon-arrow-right sliderButton"
											onclick="$('#heater_${heater.id}_slider').slider('value', $('#heater_${heater.id}_slider').slider('value') + $('#heater_${heater.id}_slider').slider('option', 'step')); return false;"></div>
									</g:if>
								</div>

								<g:if test="${heater.hasRegulator()}">
									<script type="text/javascript">
                      $(document).ready(function() {
                        $('#heater_${heater.id}_progressbar').find('.progressbar').first().progressbar(
                          {
                            value: 0,
                            change: function(event, ui) { $('#heater_${heater.id}_regular').find('.indicator').first().html($(event.target).progressbar("value"));  }
                          }
                        );
                      });
                        
                      $(document).ready(function() {
                    	   $( "#heater_${heater.id}_forced" ).find(".slider").first().removeClass("sliding");
                         $( "#heater_${heater.id}_forced" ).find(".slider").first().slider({
                             value:0,
                             min: 0,
                             max: 100,
                             step: 1,
                             change: function( event, ui ) {
                                 if($( "#heater_${heater.id}_forced" ).find(".slider").first().hasClass("sliding") === false) {
                                	  $( "#heater_${heater.id}_forced" ).find('.indicator').first().html(ui.value);
                                 
			                              if(typeof(event.orginalEvent) != undefined) {
			                                booze.brew.setForcedHeaterPower('${heater.id}', ui.value);
			                              }
                                 }
                             },
                             slide: function( event, ui ) {
                             	$( "#heater_${heater.id}_forced" ).find('.indicator').first().html(ui.value);
                             },
                             start: function(event, ui) {
                                 $( "#heater_${heater.id}_forced" ).find(".slider").first().addClass("sliding");
                             },
                             stop: function(event, ui) {
                            	 $( "#heater_${heater.id}_forced" ).find(".slider").first().removeClass("sliding");
                             }
                         });
                        });
                    </script>
								</g:if>
							</div>
							<div class="clear"></div>
						</g:each>
						<div class="clear"></div>
					</div>
					<div class="clear">&nbsp;</div>
				</div>
			</div>

			<% /* motors column */ %>
			<div class="rightColumn">
				<div class="contentbox motorsBox">
					<h1>
						<g:message code="brew.init.motors.headline" />
					</h1>

					<div class="content">
						<g:each
							in="${['mashingPumpRegulator', 'mashingMixerRegulator', 'cookingPumpRegulator', 'cookingMixerRegulator', 'drainPumpRegulator']}"
							var="motor">
							<g:if test="${brewProcess[motor]}">
								<div class="motor" id="motor_${motor}">

									<div id="motor_${motor}_regular">
										<div class="name">
											<g:message code="brew.init.${motor}" />
										</div>

										<div class="statusIcon ui-state-default">
											<div class="ui-icon ui-icon-play"></div>
										</div>
										<div class="manualModeIcon ui-state-default">
											<div class="ui-icon ui-icon-person"></div>
										</div>

										<g:if test="${brewProcess[motor].getMotor().hasRegulator()}">
											<div class="clear"></div>
											<div id="motor_${motor}_progressbar">
												<div class="progressbar">
													<div class="progressbarPower">
														<div class="label">
															<g:message code="brew.init.power" />
														</div>
														<div class="indicator">0</div>
														<div class="unit">
															<g:message code="default.unit.percent" />
														</div>
													</div>
												</div>
											</div>
										</g:if>
									</div>

									<div id="motor_${motor}_forced" style="display: none">
										<div class="name">
											<g:message code="brew.init.${motor}" />
										</div>

										<div class="statusIcon ui-state-default">
											<div class="ui-icon ui-icon-play"></div>
										</div>
										<div class="manualModeIcon ui-state-active">
											<div class="ui-icon ui-icon-person"></div>
										</div>

										<g:if test="${brewProcess[motor].getMotor().hasRegulator()}">
											<div class="clear"></div>
											<div class="label">
												<g:message code="brew.init.power" />
											</div>
											<div class="indicator">0</div>
											<div class="unit">
												<g:message code="default.unit.percent" />
											</div>

											<div class="clear"></div>

											<div class="booze-icon booze-icon-arrow-left sliderButton"
												onclick="$('#motor_${motor}_slider').slider('value', $('#motor_${motor}_slider').slider('value') - $('#motor_${motor}_slider').slider('option', 'step')); return false;"></div>

											<div class="slider"></div>
											<div class="booze-icon booze-icon-arrow-right sliderButton"
												onclick="$('#motor_${motor}_slider').slider('value', $('#motor_${motor}_slider').slider('value') + $('#motor_${motor}_slider').slider('option', 'step')); return false;"></div>
										</g:if>
									</div>

									<g:if test="${brewProcess[motor].getMotor().hasRegulator()}">
										<script type="text/javascript">
                          $(document).ready(function() {
                            $('#motor_${motor}_progressbar').find('.progressbar').first().progressbar(
                              {
                                value: 0,
                                change: function(event, ui) { $('#motor_${motor}_regular').find('.indicator').first().html($(event.target).progressbar("value"));  }
                              }
                            );
                          });

                          $(document).ready(function() {
                                $( "#motor_${motor}_forced" ).find(".slider").first().slider({
                                    value:0,
                                    min: 0,
                                    max: 100,
                                    step: 1,
                                    change: function( event, ui ) {
                                      $('#motor_${motor}_slider').find('.indicator').first().html(ui.value);
                                      if(event.orginalEvent != undefined) {
                                        booze.brew.setMotorPower('${motor}', ui.value);
                                      }
                                    },
                                    slide: function( event, ui ) {
                                      $('#motor_${motor}_slider').find('.indicator').first().html(ui.value);
                                    }
                                });
                            });
                        </script>
                      </g:if>
                    </div>
                    <div class="clear"></div>
                </g:if>
              </g:each>
            </div>
<% /*
<div class="content">
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
 */ %>


            <div class="clear"></div>
          </div>
        </div>

        <div class="clear"></div>

<% /* Slider for heater hysteresis adaptation */ %>
        <div class="leftColumn">
          <div class="contentbox heaterHysteresisRegulator">
            <h1><g:message code="brew.init.hysteresisRegulator.headline"/></h1>
            <div class="content">
              <span id="hysteresisIndicator" class="sliderIndicator">${brewProcess.setting.hysteresis.encodeAsHTML()}</span>
              <div class="clear"></div>
              <div class="booze-icon booze-icon-arrow-left sliderButton" onclick="$('#hysteresisSlider').slider('value', $('#hysteresisSlider').slider('value') - $('#hysteresisSlider').slider('option', 'step')); return false;"></div>
              <div class="slider">
                <div id="hysteresisSlider"></div>
              </div>
              <div class="booze-icon booze-icon-arrow-right sliderButton" onclick="$('#hysteresisSlider').slider('value', $('#hysteresisSlider').slider('value') + $('#hysteresisSlider').slider('option', 'step')); return false;"></div>

              <div class="row" style="width: 100%;">
                <div class="cautious">
                  <g:message code="brew.init.aggressive"/>
                </div>

                <div class="aggressive">
                  <g:message code="brew.init.cautious"/>
                </div>

                <div class="clear"></div>
              </div>

              <script type="text/javascript">
              $(document).ready(function() {
                  $( "#hysteresisSlider" ).slider({
                      value:${brewProcess.setting.hysteresis},
                      min: 0.5,
                      max: 10,
                      step: 0.5,
                      change: function( event, ui ) {
                          $( "#hysteresisIndicator").html(String.replace(ui.value, ".", ","));
                          booze.brew.setHysteresis(ui.value);
                      },
                      slide: function( event, ui ) {
                          $( "#hysteresisIndicator").html(String.replace(ui.value, ".", ","));
                      }
                  });
              });
              </script>
            </div>
          </div>
        </div>

        <% /* Slider for cooking temperature adaptation */ %>
        <div class="rightColumn">
          <div id="cookingTemperatureBox" class="contentbox cookingTemperaturRegulator" style="display: none">
            <h1><g:message code="brew.init.cookingTemperatureRegulator.headline"/></h1>
            <div class="content">
              <span id="cookingTemperatureIndicator" class="sliderIndicator">${brewProcess.setting.cookingTemperature.encodeAsHTML()}</span>
              <div class="clear"></div>
              <div class="booze-icon booze-icon-arrow-left sliderButton" onclick="$('#cookingTemperatureSlider').slider('value', $('#cookingTemperatureSlider').slider('value') - $('#cookingTemperatureSlider').slider('option', 'step')); return false;"></div>
              <div class="slider">
                <div id="cookingTemperatureSlider"></div>
              </div>
              <div class="booze-icon booze-icon-arrow-right sliderButton" onclick="$('#cookingTemperatureSlider').slider('value', $('#cookingTemperatureSlider').slider('value') + $('#cookingTemperatureSlider').slider('option', 'step')); return false;"></div>

              <div class="row" style="width: 100%;">
                <div class="low">
                  <g:message code="brew.init.low"/>
                </div>

                <div class="high">
                  <g:message code="brew.init.high"/>
                </div>

                <div class="clear"></div>
              </div>

              <script type="text/javascript">
              $(document).ready(function() {
                  $( "#cookingTemperatureSlider" ).slider({
                      value:${brewProcess.setting.cookingTemperature},
                      min: 98,
                      max: 106,
                      step: 0.5,
                      change: function( event, ui ) {
                          $( "#cookingTemperatureIndicator").html(String.replace(ui.value, ".", ","));
                          booze.brew.setCookingTemperature(ui.value);
                      },
                      slide: function( event, ui ) {
                          $( "#cookingTemperatureIndicator").html(String.replace(ui.value, ".", ","));
                      }
                  });
              });
              </script>

            </div>
          </div>
        </div>
      </div>
      
      <div class="spacer"></div>
    </div>

    <% /* Initialization window */ %>
    <script id="initDialogTemplate" type="text/x-jquery-tmpl">
      <g:render template="brewInitDialog"/>
    </script>

  <% /* Fill temperature reached window */ %>
  <script id="mashingTemperatureReachedDialogTemplate" type="text/x-jquery-tmpl">
    <g:render template="mashingTemperatureReachedDialog"/>
  </script>


  <% /* Lauter temperature reached window */ %>
  <script id="lauterTemperatureReachedDialogTemplate" type="text/x-jquery-tmpl">
    <g:render template="lauterTemperatureReachedDialog"/>
  </script>

  <% /* Add hop window */ %>
  <script id="addHopDialogTemplate" type="text/x-jquery-tmpl">
    <g:render template="addHopDialog"/>
  </script>

  <% /* Cooking finished window */ %>
  <script id="cookingFinishedDialogTemplate" type="text/x-jquery-tmpl">
    <g:render template="cookingFinishedDialog"/>
  </script>
  
    <% /* Cooling window */ %>
  <script id="coolingDialogTemplate" type="text/x-jquery-tmpl">
    <g:render template="coolingDialog"/>
  </script>

  <% /* Brew calculator window */ %>
  <script id="brewCalculatorDialogTemplate" type="text/x-jquery-tmpl">
    <div class="brewCalculatorDialog">
      <g:render template="calculator"/>
    </div>
  </script>
  
  <% /* Temperature chart window */ %>
  <script id="temperatureChartDialogTemplate" type="text/x-jquery-tmpl">${createLink(controller: "protocol", action: "showTemperatureChart", params: [id: brewProcess.protocolId, width: "1000", height: "600"])}</script>
  
  <% /* Pressure chart window */ %>
  <script id="pressureChartDialogTemplate" type="text/x-jquery-tmpl">${createLink(controller: "protocol", action: "showPressureChart", params: [id: brewProcess.protocolId, width: "1000", height: "600"])}</script>

  <g:if test="${resume == true}">
    <g:javascript>
      $(document).ready(function() {
        booze.log.info('calling booze.brew.resumeLostSession');
        booze.brew.resumeLostSession('${brewProcess.processId}', {coolingStep:${brewProcess.drainPumpRegulator != null}, updateTimeout: ${brewProcess.setting.refreshInterval}});
      });
    </g:javascript>
  </g:if>
  <g:else>
    <g:javascript>
      $(document).ready(function() {
        booze.log.info('calling booze.brew.init');
        booze.brew.init('${brewProcess.processId}', {coolingStep:${brewProcess.drainPumpRegulator != null}, updateTimeout: ${brewProcess.setting.refreshInterval}});
      });
    </g:javascript>
  </g:else>
</body>
</html>
