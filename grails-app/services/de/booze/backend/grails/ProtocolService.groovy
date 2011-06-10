/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2010  Andreas Kotsias <akotsias@esnake.de>
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
 * */
package de.booze.backend.grails

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.io.File;
import org.jfree.data.time.Millisecond
import org.codehaus.groovy.grails.commons.GrailsResourceUtils;
import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.jfree.chart.plot.XYPlot;
import java.awt.Color;
import java.text.DecimalFormat;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.NumberTickUnit

class ProtocolService {

  boolean transactional = true

  /**
   * Builds a chart from the temperature values of a protocol
   */
  public JFreeChart createTemperatureChart(Protocol protocol, taglib) {
    // Temperature values
    Map temperatures = [:]
    TimeSeries tempInner = new TimeSeries(taglib.message(code: "protocol.chart.temperature.inner"), Millisecond.class);
    TimeSeries tempOuter = new TimeSeries(taglib.message(code: "protocol.chart.temperature.outer"), Millisecond.class);
    TimeSeries tempTarget = new TimeSeries(taglib.message(code: "protocol.chart.temperature.target"), Millisecond.class);

    protocol.targetTemperatureValues.each {
      tempTarget.add(new Millisecond(new Date(it.created)), it.value);
    }

    protocol.temperatureValues.each {
      if(!temperatures[it.sensorName]) {
        temperatures[it.sensorName] = new TimeSeries(taglib.message(code: "protocol.chart.temperatureSensor", args:[it.sensorName]), Millisecond.class);
      }
      temperatures[it.sensorName].add(new Millisecond(new Date(it.created)), it.value);
    }

    TimeSeriesCollection tempDataset = new TimeSeriesCollection();
    temperatures.each {
      tempDataset.addSeries(it);
    }
    tempDataset.addSeries(tempTarget);

    JFreeChart tempChart = ChartFactory.createTimeSeriesChart(
            null,
            taglib.message(code: "protocol.chart.time"),
            taglib.message(code: "protocol.chart.temperature"),
            tempDataset,
            false,
            false,
            false);

    // Set the background colour of the chart
    tempChart.setBackgroundPaint(Color.white);

    // Get the Plot object for a bar graph
    XYPlot p = tempChart.getXYPlot();

    // Change line color
    int i=0;
    for(i; i<temperatures.size(); i++) {
      p.getRenderer().setSeriesPaint(i, Color.red);
    }
    
    p.getRenderer().setSeriesPaint(++i, Color.green);

    // Modify the plot background
    p.setBackgroundPaint(Color.white);

    // Modify the colour of the plot gridlines Modify Chart Appearance Add extra bar
    p.setRangeGridlinePaint(Color.gray);

    // Change axis range and tick units
    ValueAxis axis = p.getDomainAxis();
    axis = p.getRangeAxis();
    ((NumberAxis) axis).setTickUnit(new NumberTickUnit(10));
    axis.setRange(0, 120);

    return tempChart;
  }

  /**
   * Builds a chart from the pressure values of a protocol
   */
  public JFreeChart createPressureChart(Protocol protocol, taglib) {
    // Pressure values
    Map pressures = [:]
    protocol.pressureValues.each {
      if(!pressures[it.sensorName]) {
        pressures[it.sensorName] = new TimeSeries(taglib.message(code: "protocol.chart.pressure", args:[it.sensorName]), Millisecond.class);
      }
      pressure[it.sensorName].add(new Millisecond(new Date(it.created)), it.value);
    }

    TimeSeriesCollection pressureDataset = new TimeSeriesCollection();
    pressures.each {
      pressureDataset.addSeries(it);
    }

    JFreeChart pressureChart = ChartFactory.createTimeSeriesChart(
            null,
            taglib.message(code: "protocol.chart.time"),
            taglib.message(code: "protocol.chart.pressure"),
            pressureDataset,
            false,
            false,
            false);

    // Set the background colour of the chart
    pressureChart.setBackgroundPaint(Color.white);

    // Get the Plot object for a bar graph
    XYPlot p = pressureChart.getXYPlot();

    for(int i=0; i<pressures.size(); i++) {
      p.getRenderer().setSeriesPaint(i, Color.red);
    }
    
    // Modify the plot background
    p.setBackgroundPaint(Color.white);

    // Modify the colour of the plot gridlines Modify Chart Appearance Add extra bar
    p.setRangeGridlinePaint(Color.gray);

    // Change axis range and tick units
    ValueAxis axis = p.getDomainAxis();
    axis = p.getRangeAxis();
    ((NumberAxis) axis).setTickUnit(new NumberTickUnit(100));
    axis.setRange(0, 1000);

    return pressureChart;
  }
}
