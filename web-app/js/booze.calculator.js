/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2011  Andreas Kotsias <akotsias@esnake.de>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

/**
 * Booze calculator class
 * Various beer calculations taken from http://fabier.de/biercalcs.html
 * with friendly permission from Fabian Kaiser
 *
 * @author Andreas Kotsias <akotsias@esnake.de>
 * @copyright Fabian Kaiser <fabi@derzach.de>
 */
function BoozeCalculator() {

    // density data from "Zuckertechniker-Taschenbuch, Albert Bartens Verlage, Berlin, 1966, 7. Auflage"
    this.density_tbl = new Array(
                /* 0      5     10     15     20     25     30     35     40     g/100g */
      new Array(1.0002,1.0204,1.0418,1.0632,1.0851,1.1088,1.1323,1.1574,1.1840), /*  0°C, extrapolated, does not make much sense below 4°C */
      new Array(0.9997,1.0196,1.0402,1.0614,1.0835,1.1064,1.1301,1.1547,1.1802), /* 10°C */
      new Array(0.9982,1.0178,1.0381,1.0591,1.0810,1.1035,1.1270,1.1513,1.1764), /* 20°C */
      new Array(0.9957,1.0151,1.0353,1.0561,1.0777,1.1000,1.1232,1.1473,1.1723), /* 30°C */
      new Array(0.9922,1.0116,1.0316,1.0522,1.0737,1.0958,1.1189,1.1428,1.1676), /* 40°C */
      new Array(0.9881,1.0072,1.0271,1.0477,1.0690,1.0910,1.1140,1.1377,1.1624), /* 50°C */
      new Array(0.9832,1.0023,1.0221,1.0424,1.0636,1.0856,1.1085,1.1321,1.1568), /* 60°C */
      new Array(0.9778,0.9968,1.0165,1.0368,1.0579,1.0798,1.1026,1.1262,1.1507), /* 70°C */
      new Array(0.9718,0.9908,1.0104,1.0306,1.0517,1.0735,1.0963,1.1198,1.1443), /* 80°C */
      new Array(0.9653,0.9842,1.0038,1.0240,1.0450,1.0669,1.0896,1.1130,1.1375), /* 90°C */
    /*new Array(0.9591,0.9780,0.9975,1.0176,1.0386,1.0606,1.0832,1.1065,1.1309)     99°C, original data, extrapolated 100°C is used instead */
      new Array(0.9584,0.9773,0.9968,1.0169,1.0379,1.0599,1.0825,1.1058,1.1301)  /*100°C, extrapolated */
    );
}

// approximate the density function using ax^3+bx^2+cx+d and
// interpolate for a given x
BoozeCalculator.prototype.interpolate = function(x1, y1, x2, y2, x3, y3, x4, y4, x) {
  // lagrange interpolation
  var y =
    y1 * (((x-x2)*(x-x3)*(x-x4)) / ((x1-x2)*(x1-x3)*(x1-x4))) +
    y2 * (((x-x1)*(x-x3)*(x-x4)) / ((x2-x1)*(x2-x3)*(x2-x4))) +
    y3 * (((x-x1)*(x-x2)*(x-x4)) / ((x3-x1)*(x3-x2)*(x3-x4))) +
    y4 * (((x-x1)*(x-x2)*(x-x3)) / ((x4-x1)*(x4-x2)*(x4-x3)));

  return y;
}

// interpolates the density values for a given temperature
BoozeCalculator.prototype.interpolateTemperature = function(temperature) {
  var result = new Array();
  var lowest = Math.floor(temperature / 10) - 1;
  if (0 > lowest) {
    lowest = 0;
  }
  if (lowest > this.density_tbl.length - 4) {
    lowest = this.density_tbl.length - 4;
  }

  var temp1 = lowest;
  var temp2 = lowest + 1;
  var temp3 = lowest + 2;
  var temp4 = lowest + 3;

  for (var i=0; i<this.density_tbl[0].length; i++) {
    result[i] = this.interpolate(temp1*10, this.density_tbl[temp1][i], temp2*10, this.density_tbl[temp2][i], temp3*10, this.density_tbl[temp3][i], temp4*10, this.density_tbl[temp4][i], temperature);
  }

  return result;
}

// reads the density values for a given temperature from the data table
// values for non existing temperatures are interpolated
BoozeCalculator.prototype.densityAtBaseTemp = function(base, plato) {
  var density_base = this.interpolateTemperature(base);//density_tbl[20 / 10];

  var lowest = Math.floor(plato / 5) - 1;
  if (0 > lowest) {
    lowest = 0;
  }
  if (lowest > density_base.length - 4) {
    lowest = density_base.length - 4;
  }

  var plato1 = lowest;
  var plato2 = lowest + 1;
  var plato3 = lowest + 2;
  var plato4 = lowest + 3;

  return this.interpolate(plato1*5, density_base[plato1], plato2*5, density_base[plato2], plato3*5, density_base[plato3], plato4*5, density_base[plato4], plato);
}

BoozeCalculator.prototype.densityAtX = function(platoMeasure, temperature, calibrationTemp) {
  var dAt20 = this.densityAtBaseTemp(calibrationTemp, platoMeasure);
  var tmp = this.interpolateTemperature(temperature);

  // find density interval
  var minimalIndex = 0;
  for (i=0; i<tmp.length; i++) {
    if (dAt20 > tmp[i]) {
      minimalIndex = i;
    }
  }
  var lowest = minimalIndex - 1;
  if (0 > lowest) {
    lowest = 0;
  }
  if (lowest > tmp.length - 4) {
    lowest = tmp.length - 4;
  }

  var plato1 = lowest;
  var plato2 = lowest + 1;
  var plato3 = lowest + 2;
  var plato4 = lowest + 3;

  return this.interpolate(tmp[plato1], plato1*5, tmp[plato2], plato2*5, tmp[plato3], plato3*5, tmp[plato4], plato4*5, dAt20);
}

// replace "," by "." and force handling as float
BoozeCalculator.prototype.parseFloat = function(s) {
  return s.replace(/\,/g, ".") * 1.0;
}

// round: 2 digits and replace "." by ","
BoozeCalculator.prototype.round = function(v) {
  return ("" + (Math.round(v * 100) / 100)).replace(/\./g, ",");
}

//// transform Plato to SG
//function Plato2SG(p) {
//  return 1.00001 + (3.8661E-3 * p) + (1.3488E-5 * p * p) + (4.3074E-8 * p * p * p);
//}

//// transform SG to Plato
//function SG2Plato(sg) {
//  return -668.962 + (1262.45 * sg) - (776.43 * sg * sg) + (182.94 * sg * sg * sg);
//}

//// calculate SG value based on 60 Farenheit
//function SG60(t) {
//  return 1.00130346 - (1.34722124E-4 * t) + (2.04052596E-6 * t * t) - (2.32820948E-9 * t * t * t);
//}

//// transform Celsius to Farenheit
//function Cel2Far(c) {
//  return c * 1.8 + 32;
//}

BoozeCalculator.prototype.correctWort = function() {
  var wort = parseFloat($('#correctWortOriginalWort').val().replace(',','.'));
  var temp = parseFloat($('#correctWortTemperature').val().replace(',','.'));
  var calibTemp = parseFloat($('#correctWortCalibrationTemperature').val().replace(',','.'));
  var result = this.densityAtX(wort, temp, calibTemp);

  $('#correctWortCorrectedWort').val(this.round(result));

//  var wort_sg = Plato2SG(parseFloat(field_wort.val()));
//  var temp_far = Cel2Far(parseFloat(field_temp.val()));
//  var t_far = Cel2Far(20.0);
//  var result = wort_sg * SG60(temp_far) / SG60(t_far);
//  var destEl = document.getElementById(id_result);
//  destEl.innerHTML = Round(SG2Plato(result));
}

BoozeCalculator.prototype.calculateHop = function(field_alpha, field_utilization, field_ibu,
                  field_liq_quantum, id_hop_quantum, id_total_hop) {
  var alpha = parseFloat(field_alpha.val());
  var utilization = parseFloat(field_utilization.val());
  var ibu = parseFloat(field_ibu.val());
  var liq_quantum = parseFloat(field_liq_quantum.val());
  var destEl = $("#"+id_hop_quantum);

  destEl.innerHTML = round((10 * ibu) / (alpha * utilization));

  destEl = $("#"+id_total_hop);
  destEl.innerHTML = round((10 * ibu) / (alpha * utilization) * liq_quantum);
}


BoozeCalculator.prototype.calculateAlternativeHop = function() {
  var alphaAlt = parseFloat($('#calculateAlternativeHopOriginalAlphaAcid').val().replace(',','.')) * parseFloat($('#calculateAlternativeHopOriginalHopAmount').val().replace(',','.')) / parseFloat($('#calculateAlternativeHopDestinationAlphaAcid').val().replace(',','.'));

  $('#calculateAlternativeHopAlternativeHopAmount').val(this.round(alphaAlt));
}


BoozeCalculator.prototype.calculateVolume = function() {
  var height = parseFloat($('#calculateVolumeHeight').val().replace(',','.'));
  var radius = parseFloat($('#calculateVolumeDiameter').val().replace(',','.')) / 2.0;
  $('#calculateVolumeVolume').val(this.round(Math.PI * radius * radius * height / 1000.0));
}

BoozeCalculator.prototype.calculateAlcohol = function() {
  var wort = parseFloat($('#calculateAlcoholOriginalWort').val().replace(',','.'));
  var remaining_wort = parseFloat($('#calculateAlcoholRemainingWort').val().replace(',','.'));

  var fermSuggar = 0.81 * (wort - remaining_wort);
  var alcWeight = fermSuggar * 0.5;
  var alcVol = alcWeight / 0.795;

  $('#calculateAlcoholAlcohol').val(this.round(alcVol));
}

BoozeCalculator.prototype.calculateAlocoholOld = function(field_wort, field_rem_wort, id_result) {
  var wort = parseFloat(field_wort.val());
  var remaining_wort = parseFloat(field_rem_wort.val());

  var density  = 261.1 / (261.56 - remaining_wort);
  var dprocalc = 81.92 * (wort - remaining_wort) / (206.65 -1.0665 * wort)
  var wprocalc = density = dprocalc / 0.794;

  var destEl = $("#"+id_result);
  destEl.innerHTML = round(wprocalc);
}

BoozeCalculator.prototype.calculatePressure = function() {
  var co2 = parseFloat($('#calculatePressureTargetCo2').val().replace(',','.')) / 10.0;
  var temp = parseFloat($('#calculatePressureTemperature').val().replace(',','.'));

  var p = (co2 / Math.pow(Math.E, -10.73797 + (2617.25 / (temp + 273.15)))) - 1.013;
  $('#calculatePressureNeededPressure').val(this.round(p));
}

BoozeCalculator.prototype.calculatePressureInv = function(field_pressure, field_temp, id_result) {
  var temp = parseFloat(field_temp.val());
  var pressure = parseFloat(field_pressure.val());

  var co2 = (pressure + 1.013) * Math.pow(Math.E, -10.73797 + (2617.25 / (temp + 273.15)));
  $("#"+id_result).html(round(co2));
}

BoozeCalculator.prototype.calculateMix = function() {
  var given_vol = parseFloat($('#calculateMixMainVolume').val().replace(',','.'));
  var given_temp = parseFloat($('#calculateMixMainTemperature').val().replace(',','.'));
  var sup_temp = parseFloat($('#calculateMixSecondTemperature').val().replace(',','.'));
  var dest_temp = parseFloat($('#calculateMixTargetTemperature').val().replace(',','.'));

  var x = (-given_vol + (given_vol * given_temp) / dest_temp) / (1 - (sup_temp / dest_temp));
  $('#calculateMixNeededVolume').val(this.round(x));
}

BoozeCalculator.prototype.calculateFermentationRatio = function() {
  var wort = parseFloat($('#calculateFermentationRatioOriginalWort').val().replace(',','.'));
  var rest = parseFloat($('#calculateFermentationRatioRemainingWort').val().replace(',','.'));

  $('#calculateFermentationRatioApparentFermentationRatio').val(this.round(81 * (1 - (rest / wort))));
  $('#calculateFermentationRatioRealFermentationRatio').val(this.round(100 * (1 - (rest / wort))));
}

BoozeCalculator.prototype.calculateFare = function() {
  var temperature = parseFloat($('#calculateFareTemperature').val().replace(',','.'));
  var carbwish = parseFloat($('#calculateFareTargetCo2').val().replace(',','.'));
  var wort = parseFloat($('#calculateFareOriginalWort').val().replace(',','.'));
  var rest = parseFloat($('#calculateFareActuallExtract').val().replace(',','.'));

  var co2_0pressure = 10.13 * Math.pow(Math.E, -10.73797 + (2617.25 / (temperature + 273.15)));
  var co2_difference = carbwish - co2_0pressure;

  // calc wort (g/l)
  var wortsuggar = wort * 8.1 * (1 - (rest / wort))

  // calc suggar (1g suggar -> ca. 0.5g CO2)
  var puresuggar = co2_difference;
  if ($('#calculateFareUsedFareWort').attr("checked")) {
    $('#calculateFareWort').show();
    $('#calculateFareSugar').hide();
    //puresuggar = puresuggar / 0.44;
    puresuggar = puresuggar / 0.50;
  } else if ($('#calculateFareUsedFareSugar').attr("checked")) {
    $('#calculateFareWort').hide();
    $('#calculateFareSugar').show();
    puresuggar = puresuggar / 0.51;
  } else {
    $('#calculateFareWort').hide();
    $('#calculateFareSugar').show();
    puresuggar = puresuggar / 0.44;
  }

  $('#calculateFareNeededFareWort').val(this.round(1000 * puresuggar / wortsuggar));
  $('#calculateFareNeededFareSugar').val(this.round(puresuggar));
}

//function calc_carbozuck(field_carbtemp, field_carbwish, field_carbspst, field_carbspen, id_carbozuck, id_carbospeis) {
//  var temp = parseFloat(field_carbtemp.val());
//  var carbwish = parseFloat(field_carbwish.val());
//  var co2 = 10.13 * Math.pow(Math.E, -10.73797 + (2617.25 / (temp + 273.15)));
//  var speis = parseFloat(field_carbspst.val())-parseFloat(field_carbspen.val());
//  var benzuck = 2 * (carbwish - co2);
//  var benspeis = benzuck / speis * 100;

//  var destEl = document.getElementById(id_carbozuck);
//  destEl.innerHTML = round (benzuck);
//  var destSp = document.getElementById(id_carbospeis);
//  destSp.innerHTML = round (benspeis);
//}

BoozeCalculator.prototype.calculateWaterHardness = function() {
  var ca = parseFloat($('#calculateWaterHardnessCalcium').val().replace(',','.'));
  var mg = parseFloat($('#calculateWaterHardnessMagnesium').val().replace(',','.'));
  var ac = parseFloat($('#calculateWaterHardnessAcidCapacity').val().replace(',','.'));
  var water_vol = parseFloat($('#calculateWaterHardnessWaterVolume').val().replace(',','.'));
  var dest_ra = parseFloat($('#calculateWaterHardnessDestinationRemainingAlkalinity').val().replace(',','.'));

  // convert to °dH
  var ca_dh = (ca / 40.8) / 0.1783;
  var mg_dh = (mg / 24.3) / 0.1783;
  var ac_dh = ac * 2.8;

  var alkalinity = ac_dh - ((ca_dh + 0.5*mg_dh) / 3.5);

  var acid2add = ((alkalinity - dest_ra) * water_vol) / 30;

  $('#calculateWaterHardnessCalciumHardness').val(this.round(ca_dh));
  $('#calculateWaterHardnessMagnesiumHardness').val(this.round(mg_dh));
  $('#calculateWaterHardnessCarbonateHardness').val(this.round(ac_dh));
  $('#calculateWaterHardnessRemainingAlkalinity').val(this.round(alkalinity));
  $('#calculateWaterHardnessLacticAcid').val(this.round(acid2add));
}

BoozeCalculator.prototype.calculateYield = function() {
  var wort = parseFloat($('#calculateYieldOriginalWort').val().replace(',','.'));
  var volume = parseFloat($('#calculateYieldVolume').val().replace(',','.'));
  var mass = parseFloat($('#calculateYieldFill').val().replace(',','.'));
  var tempFactor = 1.0;
  if ($('#calculateYieldTemperature100').attr("checked")) {
    tempFactor = 0.96;
  }
  var mixed = this.densityAtBaseTemp(20, wort) * wort;

  $('#calculateYieldYield').val(this.round(tempFactor * mixed * volume / mass));
}



booze.calculator = new BoozeCalculator();
