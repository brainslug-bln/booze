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

package de.booze.backend.grails

class HeaterRegulatorDevice extends Device {

  static constraints = {
  }
  
  static belongsTo = [heater: HeaterDevice]
  
  static transients = ["forced", "realPower"]
  
  /**
   * True if the heater is in forced mode
   */
  private boolean forced = false
  
  /**
   * If the heater regulator is forced (^= manually controlled by the user)
   * the programmatically assigned power value is saved in this
   * variable
   */
  private int realPower = 0;
  
  /**
   * Sets the heater power in percent
   * of the maximum value
   * @param power Power in percent
   */
  public void writePower(int power) {
	if(this.forced) {
		this.realPower = power;
		return;
	}
    driverInstance.setPower(power);
  }
  
  /**
   * Sets the heater power in percent of 
   * the maximum power in forced mode
   * @param power
   */
  public void writeForcedPower(int power) {
	  driverInstance.setPower(power);
  }

  /**
   * Returns the heater power in percent
   * of the maximum value
   */
  public int readPower() {
    return driverInstance.getPower();
  }
  
  /**
   * Enables the forced mode for this heater regulator.
   * While in forced mode, the regulator power value
   * may only be modified by the writeForcedPower method
   */
  public void force() {
	  this.realPower = this.readPower();
	  this.forced = true;
  }
  
  /**
   * Disables the forced mode
   */
  public void unforce() {
	  this.forced = false;
	  this.writePower(this.realPower);
  }
}
