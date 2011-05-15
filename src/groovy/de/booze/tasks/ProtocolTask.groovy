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

package de.booze.tasks;


import org.hibernate.*;
import org.apache.log4j.Logger;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

import de.booze.process.BrewProcess
import de.booze.grails.*

/**
 * Periodically adds protocol values and events to the brew protocol
 */
class ProtocolTask extends TimerTask {

  private BrewProcess brewProcess;

  private Logger log = Logger.getLogger(getClass().getName())

  SessionFactory sessionFactory

  /**
   * Constructor
   */
  public ProtocolTask(BrewProcess bp) {
    this.brewProcess = bp;
  }

  /**
   * Default run method
   */
  public void run() {

    def ctx = AH.application.mainContext;
    sessionFactory = ctx.sessionFactory;

    Session session = sessionFactory.openSession()


    Protocol protocol = session.get(Protocol, this.brewProcess.protocolId); //Protocol.get(this.brewProcess.protocolId);

    if (!protocol) {
      log.error("Error getting protocol for ProtocolTask: ${e}")
      return
    }

    try {
      Double innerTemperature = this.brewProcess.innerTemperatureSensors.sum {it.readTemperatureImmediate()}?.div(this.brewProcess.innerTemperatureSensors.size())
      def ti = new ProtocolTemperatureValue(sensorPosition: ProtocolTemperatureValue.POSITION_INNER,
              value: innerTemperature,
              created: (new Date()).getTime(),
              protocol: protocol)
      session.save(ti)
    }
    catch (Exception e) {
      log.error("Error saving inner temperature value to protocol: ${e}")
    }

    try {
      Double outerTemperature = this.brewProcess.outerTemperatureSensors.sum {it.readTemperatureImmediate()}?.div(this.brewProcess.innerTemperatureSensors.size())
      def to = new ProtocolTemperatureValue(sensorPosition: ProtocolTemperatureValue.POSITION_OUTER,
              value: outerTemperature,
              created: (new Date()).getTime(),
              protocol: protocol)
      session.save(to)
    }
    catch (Exception e) {
      log.error("Error saving outer temperature value to protocol: ${e}")
    }

    try {
      Double pressure = this.brewProcess.pressureSensors.sum {it.readAveragePressure()}?.div(this.brewProcess.pressureSensors.size())
      def p = new ProtocolPressureValue(value: pressure,
              created: (new Date()).getTime(),
              protocol: protocol)
      session.save(p)
    }
    catch (Exception e) {
      log.error("Error saving pressure value to protocol: ${e}")
    }

    try {
      Double targetTemperature = this.brewProcess.temperatureRegulator.getTemperature();
      def tt = new ProtocolTargetTemperatureValue(value: targetTemperature,
              created: (new Date()).getTime(),
              protocol: protocol);

      session.save(tt)
    }
    catch (Exception e) {
      log.error("Error saving target temperature value to protocol: ${e}")
    }


    def events = this.brewProcess.getEventsForProtocol()
    events.each {
      try {
        ProtocolEvent ev = new ProtocolEvent(it.getEventDataForProtocol());
        ev.protocol = protocol
        session.save(ev)
      }
      catch (Exception e) {
        log.error("Error saving protocol event: ${e}")
      }
    }

    session.close()
  }
}

