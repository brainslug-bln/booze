/* 
 * This File is part of the iowj-project   
 * $Id: LM75.java,v 1.2 2006/09/15 20:55:15 Thomas Wagner Exp $
 * Copyright (C)2004 by Thomas Wagner
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package de.wagner_ibw.iow.i2c;

import de.wagner_ibw.iow.AbstractIowDevice;

/**
 * This is the implemenation of the LM75 device , "Digital Temperature
 * Sensor and Thermal Watchdog with Two-Wire Interface", by National
 * Semiconductors.<br>
 * <p>This class almost supports features of the device:
 * <ul>
 * <li>Read the current temperature.</li>
 * <li>Setting OS and HYST temperature.</li>
 * <li>Setting Comparator/Interrupt mode for the O.S. pin.</li>
 * <li>Setting O.S. Polarity</li>
 * </ul>
 * Unsupported features are:
 * <ul>
 * <li>Resetting the device.</li>
 * <li>Setting the device to low power shutdown mode.</li>
 * <li>Reporting the Fault Queue.</li>
 * </ul>
 * </p>
 *
 * @author Thomas Wagner
 */
public class LM75 extends AbstractI2CDevice {

    /**
     * PCF8570 device specific constants.
     */
    public final static String NAME = "LM75";

    /**
     * Group 1 part of slave address for LM75
     * (LM75 device specific constant).
     */
    public final static int CLASS = 0x9;

    /**
     * Constant for register pointer for 'Temperature' register
     * (LM75 device specific constant).
     */
    public final static int REG_TEMP = 0;

    /**
     * Constant for register pointer for 'Configuration' register
     * (LM75 device specific constant).
     */
    public final static int REG_CONFIG = 1;

    /**
     * Constant for register pointer for 'THyst Set Point' register
     * (LM75 device specific constant).
     */
    public final static int REG_TEMP_HYST = 2;

    /**
     * Constant for register pointer for 'TOS Set Point' register
     * (LM75 device specific constant).
     */
    public final static int REG_TEMP_OS = 3;

    /**
     * Constant for 'Comparator' mode.
     */
    public final static int MODE_COMPARATOR = 0;

    /**
     * Constant for 'Interrupt' mode.
     */
    public final static int MODE_INTERRUPT = 1;

    /**
     * Constant for O.S. output polarity 'active low'.
     */
    public final static int POLARITY_ACTIVE_LOW = 0;

    /**
     * Constant for O.S. output polarity 'active high'.
     */
    public final static int POLARITY_ACTIVE_HIGH = 1;

    /**
     * Current setted OS temperature (initialized with factory default value).
     */
    private double tOs = 80.0;

    /**
     * Current setted THYST temperature (initialized with factory default value).
     */
    private double tHyst = 75.0;

    /**
     * Current temperature (measured by LM75).
     */
    private double tCurrent = 0;

    /**
     * Current mode (initialized with factory default value, read from LM75).
     */
    private int mode = 0;

    /**
     * Current polarity (initialized with factory default value, read from LM75).
     */
    private int polarity = 0;

    /**
     * Flag for last register access.
     */
    private int lastRegisterPointer = -1;

    /**
     * Constructor
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public LM75(int deviceAddress) throws Exception {
        super(NAME, CLASS, deviceAddress);
    }


    private void init() throws Exception {
//		System.out.println("LM75.init()...");
//		iow.addI2CDevice(devAdr,NAME); 	 
        //check adr im Bereich (manche chips nur ein bit

        //check adr schon vergeben (in dieser device classe, also devAdr checken?

        //set the register pointer to the temperature register

        getT();
        getTOs();
        getTHyst();
//		System.out.println("LM75.init()end, "+this.toString());
    }


    /**
     * Returns Current temperature measured by this LM75.
     *
     * @return Temperature as double value from this LM75.
     * @throws Exception If anything goes wrong.
     */
    public double getT() throws Exception {
        setRegisterPointer(REG_TEMP);
        tCurrent = getTemperature();
        return tCurrent;
    }


    public void getConfiguration() throws Exception {
        setRegisterPointer(REG_CONFIG);

        int data[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(data, 1);
        int mode = (temp[0] & 0x20) >> 1;
        int polarity = (temp[0] & 0x40) >> 2;
        //*deb*/System.out.println("Mode: "+mode+", Polarity: "+polarity);
    }


    /**
     * Returns THYST set point stored in this LM75.
     *
     * @return HYST temperature as double value from this LM75.
     * @throws Exception If anything goes wrong.
     */
    public double getTHyst() throws Exception {
        setRegisterPointer(REG_TEMP_HYST);
        tHyst = getTemperature();
        return tHyst;
    }


    /**
     * Returns TOS set point stored in this LM75.
     *
     * @return OS temperature as double value from this LM75.
     * @throws Exception If anything goes wrong.
     */
    public double getTOs() throws Exception {
        setRegisterPointer(REG_TEMP_OS);
        tOs = getTemperature();
        return tOs;
    }


    public void setConfiguration(int mode, int polarity) throws Exception {
        int conf = (mode << 1) | (polarity << 2);
        setRegister(REG_CONFIG, 1, conf, 0);
        //this.mode = mode;
        //this.polarity = polarity;
    }


    /**
     * Sets THYST set point in this LM75.
     *
     * @param value Desired HYST temperature as doubel value.
     */
    public void setTHyst(double value) throws Exception {
        LM75Temp temp = new LM75Temp(value);
        this.tHyst = temp.getTemp();
        setRegister(REG_TEMP_HYST, 2, temp.getMsdb(), temp.getLsdb());
    }


    /**
     * Sets TOS set point in this LM75.
     *
     * @param value Desired OS temperature as doubel value.
     */
    public void setTOs(double value) throws Exception {
        LM75Temp temp = new LM75Temp(value);
        this.tOs = temp.getTemp();
        //*deb*/System.out.println("setTOS("+value+") called... ("+temp.getMsdb()+","+temp.getLsdb()+")");
        setRegister(REG_TEMP_OS, 2, temp.getMsdb(), temp.getLsdb());
    }


    /**
     * Getter for mode of this LM75 device.
     *
     * @return Mode of this I2C device.
     */
    public int getMode() {
        return mode;
    }


    /**
     * Getter for polarity of this LM75 device.
     *
     * @return Polarity of this I2C device.
     */
    public int getPolarity() {
        return polarity;
    }


    /**
     * Support method for setting register pointer.
     *
     * @param pointer Desired register (possible values:
     *                REG_TEMP (0), REG_CONFIG (1), REG_TEMP_HYST (2), REG_TEMP_OS (3)).
     * @throws Exception If anything goes wrong.
     */
    private void setRegisterPointer(int pointer) throws Exception {
        if (pointer == lastRegisterPointer) return; //nothing to do

        int data[] = {devAdr.getWriteAddress(), pointer};
        writeI2C(data);
        lastRegisterPointer = pointer;
    }


    /**
     * Support method for getting temperature value for register.
     * specified by preseted register pointer.
     *
     * @return Temperature as double value.
     * @throws Exception If anything goes wrong.
     */
    private double getTemperature() throws Exception {
        int data[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(data, 2);
        LM75Temp temperature = new LM75Temp(temp[0], temp[1]);
        return temperature.getTemp();
    }


    /**
     * Support method for setting writeable registers. Possibly registers are
     * CONFIG, TOS and THYST.
     *
     * @param regAdr     Desired register (possible values: REG_CONFIG (1), REG_TEMP_HYST (2), REG_TEMP_OS (3)).
     * @param count      Counter of bytes of pay load (possible values: 1 or 2).
     * @param firstByte  First byte of the pay load (must set).
     * @param secondByte Second byte of the pay load (if counter is 1 this param must be set 0).
     * @throws Exception If anything goes wrong.
     */
    private void setRegister(int regAdr, int count, int firstByte, int secondByte) throws Exception {
        int data[] = {devAdr.getWriteAddress(), regAdr, firstByte, secondByte};
        writeI2C(data);
        lastRegisterPointer = regAdr;
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        if (iow != null) {
            try {
                monitor = iow.getMonitor();
                init();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Returns Information about this LM75 as String.
     *
     * @return String representation of information obout this LM75
     *         (slave address, temperatures, ...).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());
        sb.append(":");
        sb.append("SlaveAddress[");
        sb.append(devAdr);
        sb.append("],tCurr[");
        sb.append(tCurrent);
        sb.append("],tOs[");
        sb.append(tOs);
        sb.append("],tHyst[");
        sb.append(tHyst);
        sb.append("]");

        return sb.toString();
    }

    /**
     * Indicates whether some other LM75 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object o) {
        if (!(o instanceof LM75))
            return false;

        LM75 lm = (LM75) o;
        return this.devAdr.equals(lm.devAdr);
    }

    /**
     * Returns a hash code value for this LM75 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     * @since 0.9.4
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }


    /**
     * Inner class for LM75 temperature representation.
     */
    private class LM75Temp {

        /**
         * Constant for minimum temperature valid for LM75.
         */
        final static double TEMP_MIN = -55.0;

        /**
         * Constant for maximum temperature valid for LM75.
         */
        final static double TEMP_MAX = 125.0;

        /**
         * Most significant data byte of this temperature.
         */
        int msdb;

        /**
         * Most significant data byte of this temperature.
         */
        int lsdb;

        /**
         * Double representation of this temperature.
         */
        double temp;


        /**
         * Constructor for temperature seperated in msdb (most significant data byte)
         * and lsdb (least significant data byte).
         *
         * @param msdb Most significant data byte read from LM75.
         * @param lsdb Least significant data byte read from LM75.
         */
        public LM75Temp(int msdb, int lsdb) {
            this.msdb = (byte) msdb; //save the sign!
            this.lsdb = lsdb & 0x80;
            temp = (byte) msdb; //save the sign!
            if ((lsdb & 0x80) == 0x80) {
                if ((msdb & 0x80) == 0x80) temp = temp - 0.5;
                else temp = temp + 0.5;
            }
        }


        /**
         * Constructor for temperature as double value.
         *
         * @param temp Temperature as double value.
         */
        public LM75Temp(double temp) {
            if (temp > TEMP_MAX) temp = TEMP_MAX;
            if (temp < TEMP_MIN) temp = TEMP_MIN;

            this.temp = temp;

            double fest;
            double rest;

            if (temp >= 0) {
                fest = Math.floor(temp);
                rest = temp - fest;
                msdb = (int) fest;
                if (rest == 0) lsdb = 0x0;
                else if (rest < 0.25) lsdb = 0x0;
                else if (rest >= 0.26 && rest < 0.75) lsdb = 0x80;
                else {
                    lsdb = 0;
                    msdb++;
                }
            } else {
                fest = Math.ceil(temp);
                rest = temp - fest;
                msdb = (int) fest;
                if (rest == 0) lsdb = 0x0;
                else if (rest > -0.25) lsdb = 0x0;
                else if (rest <= -0.26 && rest > -0.75) lsdb = 0x80;
				else { lsdb = 0; msdb --;}
			}
		}

		
		/**
		 * Getter for msdb.
		 */ 
		int getMsdb(){
			return msdb;
		}
		
		
		/**
		 * Getter for lsdb.
		 */
		int getLsdb(){
			return lsdb;
		}

		
		/**
		 * Getter for double value.
		 */
		double getTemp(){
			return temp;
		}
	}
}
