<div class="headline" id="stepInfoHeadline">
  <g:message code="brew.step.initialize"/>
</div>

<div class="stepInfo" id="stepInfo">

  <div id="targetMashingTemperatureInfo" style="display: none;">
    <p>Heize auf Einmaischtemperatur auf</p>

    <div class="row">
      <label>Beginn dieses Schritts</label>
      <div class="value" id="targetMashingTemperatureInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Einmaischtemperatur</label>
      <div class="value" id="targetMashingTemperatureInfo_targetTemperature">

      </div>
    </div>
    <div class="clearfix"></div>
  </div>

  <div id="mashingTemperatureReachedInfo" style="display: none;">
    <p>Warte auf Hinzufügen der Zutaten</p>
  </div>

  <div id="restInfo" style="display: none;">
    <div class="row">
      <label>Zieltemperatur</label>
      <div class="value" id="restInfo_targetTemperature">

      </div>
    </div>

    <div class="row">
      <label>Startzeit dieser Rast</label>
      <div class="value" id="restInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Restzeit für diese Rast</label>
      <div class="value" id="restInfo_timeToGo">

      </div>
    </div>

    <div class="row">
      <label>Rasttemperatur erreicht</label>
      <div class="value" id="restInfo_temperatureReached_true" style="display: none;">
        ja
      </div>

      <div class="value" id="restInfo_temperatureReached_false" style="display: none;">
        nein
      </div>
    </div>

    <div class="clearfix"></div>
  </div>

  <div id="mashingFinishedInfo" style="display: none;">
    <p>Alle Rasten abgeschlossen</p>
    <p>Heize auf Abmaischtemperatur auf</p>

    <div class="row">
      <label>Beginn dieses Schritts</label>
      <div class="value" id="mashingFinishedInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Abmaischtemperatur</label>
      <div class="value" id="mashingFinishedInfo_targetTemperature">

      </div>
    </div>
    <div class="clearfix"></div>
  </div>

  <div id="initCookingInfo" style="display: none;">
    <p>Abmaischtemperatur erreicht</p>
  </div>

  <div id="cookingInfo" style="display: none;">
    <div class="row">
      <label>Kochtemperatur</label>
      <div class="value" id="cookingInfo_targetTemperature">

      </div>
    </div>

    <div class="row">
      <label>Startzeit</label>
      <div class="value" id="cookingInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Restzeit</label>
      <div class="value" id="cookingInfo_timeToGo">

      </div>
    </div>

    <div class="row">
      <label>Kochtemperatur erreicht</label>
      <div class="value" id="cookingInfo_temperatureReached_true" style="display: none;">
        ja
      </div>

      <div class="value" id="cookingInfo_temperatureReached_false" style="display: none;">
        nein
      </div>
    </div>

    <div class="clearfix"></div>
  </div>

  <div id="elongateMashingInfo" style="display: none">

    <div class="row">
      <label>Zieltemperatur</label>
      <div class="value" id="elongateMashingInfo_targetTemperature">

      </div>
    </div>

    <div class="row">
      <label>Startzeit</label>
      <div class="value" id="elongateMashingInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Restzeit</label>
      <div class="value" id="elongateMashingInfo_timeToGo">

      </div>
    </div>
    <div class="clearfix"></div>
  </div>

  <div id="elongateCookingInfo" style="display: none">

    <div class="row">
      <label>Zieltemperatur</label>
      <div class="value" id="elongateCookingInfo_targetTemperature">

      </div>
    </div>

    <div class="row">
      <label>Startzeit</label>
      <div class="value" id="elongateCookingInfo_stepStartTime">

      </div>
    </div>

    <div class="row">
      <label>Restzeit</label>
      <div class="value" id="elongateCookingInfo_timeToGo">

      </div>
    </div>
    <div class="clearfix"></div>
  </div>
  
  <div id="coolingInfo" style="display: none;">
    <p>Pumpe ab</p>

    <div class="row">
      <label>Beginn dieses Schritts</label>
      <div class="value" id="coolingInfo_stepStartTime">

      </div>
    </div>

    <div class="clearfix"></div>
  </div>
</div>