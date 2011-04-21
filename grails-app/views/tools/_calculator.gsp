<div id="calculator">
  <div>
    <div id="calculateFare">
      <h2><g:message code="calculator.selector.calculateFare" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateFare.temperature" /></label>
        <input id="calculateFareTemperature" name="calculateFareTemperature" value="10" onkeyup="booze.calculator.calculateFare();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateFare.targetCo2" /></label>
        <input id="calculateFareTargetCo2" name="calculateFareTargetCo2" value="5,5" onkeyup="booze.calculator.calculateFare();" />
      </div>

      <div class="row">
        <label style="width: auto"><g:message code="calculator.calculateFare.usedFare" /></label>
        <div class="radioset" id="usedFareRadios">
          <input type="radio" onclick="booze.calculator.calculateFare();" id="calculateFareUsedFareWort" name="calculateFareUsedFare" value="wort" checked /> <label for="calculateFareUsedFareWort"><g:message code="calculator.calculateFare.wort" /></label>
          <input type="radio" onclick="booze.calculator.calculateFare();" id="calculateFareUsedFareSugar" name="calculateFareUsedFare" value="wort" /> <label for="calculateFareUsedFareSugar"><g:message code="calculator.calculateFare.sugar" /></label>
          <input type="radio" onclick="booze.calculator.calculateFare();" id="calculateFareUsedFareFructose" name="calculateFareUsedFare" value="wort" /> <label for="calculateFareUsedFareFructose"><g:message code="calculator.calculateFare.fructose" /></label>
        </div>
      </div>

      <div id="calculateFareWort">
        <div class="row">
          <label><g:message code="calculator.calculateFare.originalWort" /></label>
          <input id="calculateFareOriginalWort" name="calculateFareOriginalWort" value="12" onkeyup="booze.calculator.calculateFare();" />
        </div>

        <div class="row">
          <label><g:message code="calculator.calculateFare.actualExtract" /></label>
          <input id="calculateFareActuallExtract" name="calculateFareActuallExtract" value="3" onkeyup="booze.calculator.calculateFare();" />
        </div>

        <div class="row result">
          <label><g:message code="calculator.calculateFare.neededFareWort" /></label>
          <input id="calculateFareNeededFareWort" name="calculateFareNeededFare" value="88,55" disabled="true" />
        </div>
      </div>

      <div id="calculateFareSugar" style="display: none">
        <div class="row result">
          <label><g:message code="calculator.calculateFare.neededFareSugar" /></label>
          <input id="calculateFareNeededFareSugar" name="calculateFareNeededFare" value="88,55" disabled="true" />
        </div>
      </div>

    </div>

    <div id="correctWort" style="display: none">
      <h2><g:message code="calculator.selector.correctWort" /></h2>
      <div class="row">
        <label><g:message code="calculator.correctWort.calibrationTemperature" /></label>
        <input id="correctWortCalibrationTemperature" name="correctWortCalibrationTemperature" value="20" onkeyup="booze.calculator.correctWort();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.correctWort.temperature" /></label>
        <input id="correctWortTemperature" name="correctWortTemperature" value="40" onkeyup="booze.calculator.correctWort();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.correctWort.originalWort" /></label>
        <input id="correctWortOriginalWort" name="correctWortOriginalWort" value="12" onkeyup="booze.calculator.correctWort();" />
      </div>

      <div class="row result">
        <label><g:message code="calculator.correctWort.correctedWort" /></label>
        <input id="correctWortCorrectedWort" name="correctWortCorrectedWort" value="13,61" disabled="true" />
      </div>
    </div>

    <div id="calculateAlcohol" style="display: none;">
      <h2><g:message code="calculator.selector.calculateAlcohol" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateAlcohol.originalWort" /></label>
        <input id="calculateAlcoholOriginalWort" name="calculateAlcoholOriginalWort" value="11" onkeyup="booze.calculator.calculateAlcohol();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.correctWort.remainingWort" /></label>
        <input id="calculateAlcoholRemainingWort" name="calculateAlcoholRemainingWort" value="3" onkeyup="booze.calculator.calculateAlcohol();" />
      </div>

      <div class="row result">
        <label><g:message code="calculator.correctWort.alcohol" /></label>
        <input id="calculateAlcoholAlcohol" name="calculateAlcoholAlcohol" value="4,08" disabled="true" />
      </div>
    </div>

    <div id="calculatePressure" style="display: none;">
      <h2><g:message code="calculator.selector.calculatePressure" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculatePressure.targetCo2" /></label>
        <input id="calculatePressureTargetCo2" name="calculatePressureTargetCo2" value="5" onkeyup="booze.calculator.calculatePressure();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculatePressure.temperature" /></label>
        <input id="calculatePressureTemperature" name="calculatePressureTemperature" value="11" onkeyup="booze.calculator.calculatePressure();" />
      </div>

      <div class="row result">
        <label><g:message code="calculator.calculatePressure.neededPressure" /></label>
        <input id="calculatePressureNeededPressure" name="calculatePressureNeededPressure" value="1,29" disabled="true" />
      </div>
    </div>

    <div id="calculateFermentationRatio" style="display: none">
      <h2><g:message code="calculator.selector.calculateFermentationRatio" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateFermentationRatio.originalWort" /></label>
        <input id="calculateFermentationRatioOriginalWort" name="calculateFermentationRatioOriginalWort" value="12" onkeyup="booze.calculator.calculateFermentationRatio();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateFermentationRatio.remainingWort" /></label>
        <input id="calculateFermentationRatioRemainingWort" name="calculateFermentationRatioRemainingWort" value="3" onkeyup="booze.calculator.calculateFermentationRatio();" />
      </div>

      <div class="result">
        <div class="row ">
          <label><g:message code="calculator.calculateFermentationRatio.apparentFermentationRatio" /></label>
          <input id="calculateFermentationRatioApparentFermentationRatio" name="calculateFermentationRatioApparentFermentationRatio" value="60,75" disabled="true" />
        </div>

        <div class="row ">
        <label><g:message code="calculator.calculateFermentationRatio.realFermentationRatio" /></label>
        <input id="calculateFermentationRatioRealFermentationRatio" name="calculateFermentationRatioRealFermentationRatio" value="75" disabled="true" />
      </div>
      </div>
    </div>

    <div id="calculateVolume" style="display: none">
      <h2><g:message code="calculator.selector.calculateVolume" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateVolume.height" /></label>
        <input id="calculateVolumeHeight" name="calculateVolumeHeight" value="10" onkeyup="booze.calculator.calculateVolume();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateVolume.diameter" /></label>
        <input id="calculateVolumeDiameter" name="calculateVolumeDiameter" value="10" onkeyup="booze.calculator.calculateVolume();" />
      </div>

      <div class="row result">
        <label><g:message code="calculator.calculateVolume.volume" /></label>
        <input id="calculateVolumeVolume" name="calculateVolumeVolume" value="0,79" disabled="true" />
      </div>
    </div>

    <div id="calculateMix" style="display: none">
      <h2><g:message code="calculator.selector.calculateMix" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateMix.mainVolume" /></label>
        <input id="calculateMixMainVolume" name="calculateMixMainVolume" value="20" onkeyup="booze.calculator.calculateMix();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateMix.mainTemperature" /></label>
        <input id="calculateMixMainTemperature" name="calculateMixMainTemperature" value="40" onkeyup="booze.calculator.calculateMix();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateMix.secondTemperature" /></label>
        <input id="calculateMixSecondTemperature" name="calculateMixSecondTemperature" value="80" onkeyup="booze.calculator.calculateMix();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateMix.targetTemperature" /></label>
        <input id="calculateMixTargetTemperature" name="calculateMixTargetTemperature" value="60" onkeyup="booze.calculator.calculateMix();" />
      </div>

      <div class="result row">
          <label><g:message code="calculator.calculateMix.neededVolume" /></label>
          <input id="calculateMixNeededVolume" name="calculateMixNeededVolume" value="20" disabled="true" />
      </div>
    </div>

    <div id="calculateYield" style="display: none">
      <h2><g:message code="calculator.selector.calculateYield" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateYield.originalWort" /></label>
        <input id="calculateYieldOriginalWort" name="calculateYieldOriginalWort" value="12" onkeyup="booze.calculator.calculateYield();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateYield.volume" /></label>
        <input id="calculateYieldVolume" name="calculateYieldVolume" value="20" onkeyup="booze.calculator.calculateYield();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateYield.fill" /></label>
        <input id="calculateYieldFill" name="calculateYieldFill" value="4" onkeyup="booze.calculator.calculateYield();" />
      </div>

      <div class="row">
        <label style="width: 75%"><g:message code="calculator.calculateYield.temperature" /></label>

        <div class="radioset" id="yieldTemperature">
          <input type="radio" onclick="booze.calculator.calculateYield();" id="calculateYieldTemperature20" name="calculateYieldTemperature" value="20" checked /> <label for="calculateYieldTemperature20"><g:message code="calculator.calculateYield.20" /></label>
          <input type="radio" onclick="booze.calculator.calculateYield();" id="calculateYieldTemperature100" name="calculateYieldTemperature" value="100" /> <label for="calculateYieldTemperature100"><g:message code="calculator.calculateYield.100" /></label>
        </div>
      </div>

      <div class="result row">
          <label><g:message code="calculator.calculateYield.yield" /></label>
          <input id="calculateYieldYield" name="calculateYieldYield" value="62,78" disabled="true" />
      </div>
    </div>

    <div id="calculateWaterHardness" style="display: none">
      <h2><g:message code="calculator.selector.calculateWaterHardness" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateWaterHardness.calcium" /></label>
        <input id="calculateWaterHardnessCalcium" name="calculateWaterHardnessCalcium" value="68" onkeyup="booze.calculator.calculateWaterHardness();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateWaterHardness.magnesium" /></label>
        <input id="calculateWaterHardnessMagnesium" name="calculateWaterHardnessMagnesium" value="31,3" onkeyup="booze.calculator.calculateWaterHardness();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateWaterHardness.acidCapacity" /></label>
        <input id="calculateWaterHardnessAcidCapacity" name="calculateWaterHardnessAcidCapacity" value="6,17" onkeyup="booze.calculator.calculateWaterHardness();" />
      </div>

      <div class="result">
        <div class="row">
          <label><g:message code="calculator.calculateWaterHardness.calciumHardness" /></label>
          <input id="calculateWaterHardnessCalciumHardness" name="calculateWaterHardnessCalciumHardness" value="9,35" disabled="true" />
        </div>

        <div class="row">
          <label><g:message code="calculator.calculateWaterHardness.magnesiumHardness" /></label>
          <input id="calculateWaterHardnessMagnesiumHardness" name="calculateWaterHardnessMagnesiumHardness" value="7,22" disabled="true" />
        </div>

        <div class="row">
          <label><g:message code="calculator.calculateWaterHardness.carbonateHardness" /></label>
          <input id="calculateWaterHardnessCarbonateHardness" name="calculateWaterHardnessCarbonateHardness" value="17,28" disabled="true" />
        </div>

        <div class="row">
          <label><g:message code="calculator.calculateWaterHardness.remainingAlkalinity" /></label>
          <input id="calculateWaterHardnessRemainingAlkalinity" name="calculateWaterHardnessRemainingAlkalinity" value="13,57" disabled="true" />
        </div>
      </div>

      <div class="clearfix">&nbsp;</div>

      <div class="row">
        <label><g:message code="calculator.calculateWaterHardness.destinationRemainingAlkalinity" /></label>
        <input id="calculateWaterHardnessDestinationRemainingAlkalinity" name="calculateWaterHardnessDestinationRemainingAlkalinity" value="0" onkeyup="booze.calculator.calculateWaterHardness();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateWaterHardness.waterVolume" /></label>
        <input id="calculateWaterHardnessWaterVolume" name="calculateWaterHardnessWaterVolume" value="20" onkeyup="booze.calculator.calculateWaterHardness();" />
      </div>

      <div class="result row">
          <label><g:message code="calculator.calculateWaterHardness.lacticAcid" /></label>
          <input id="calculateWaterHardnessLacticAcid" name="calculateWaterHardnessLacticAcid" value="9,05" disabled="true" />
      </div>
    </div>

    <div id="calculateAlternativeHop" style="display: none">
      <h2><g:message code="calculator.selector.calculateAlternativeHop" /></h2>
      <div class="row">
        <label><g:message code="calculator.calculateAlternativeHop.originalAlphaAcid" /></label>
        <input id="calculateAlternativeHopOriginalAlphaAcid" name="calculateAlternativeHopOriginalAlphaAcid" value="10" onkeyup="booze.calculator.calculateAlternativeHop();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateAlternativeHop.originalHopAmount" /></label>
        <input id="calculateAlternativeHopOriginalHopAmount" name="calculateAlternativeHopOriginalHopAmount" value="30" onkeyup="booze.calculator.calculateAlternativeHop();" />
      </div>

      <div class="row">
        <label><g:message code="calculator.calculateAlternativeHop.destinationAlphaAcid" /></label>
        <input id="calculateAlternativeHopDestinationAlphaAcid" name="calculateAlternativeHopDestinationAlphaAcid" value="10" onkeyup="booze.calculator.calculateAlternativeHop();" />
      </div>

      <div class="result row">
          <label><g:message code="calculator.calculateAlternativeHop.alternativeHopAmount" /></label>
          <input id="calculateAlternativeHopAlternativeHopAmount" name="calculateAlternativeHopAlternativeHopAmount" value="30" disabled="true" />
      </div>
    </div>
  </div>

  <div class="copyright">
    <g:message code="calculator.copyright" />
  </div>
</div>

<script>
$(function() {
        $( "#usedFareRadios" ).buttonset();
        $( "#yieldTemperature" ).buttonset();
});
</script>

