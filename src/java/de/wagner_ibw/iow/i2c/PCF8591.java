/* 
 * This File is part of the iowj-project  
 * $Id: PCF8591.java,v 1.4 2008/06/29 11:51:28 Thomas Wagner Exp $
 * Copyright (C)2007 by Thomas Wagner
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
 * This is the implementation of the PCF8591 device, "8-bit A/D and D/A converter".
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class PCF8591 extends AbstractI2CDevice {


    /**
     * PCF8591 device specific constants.
     */
    public final static String NAME = "PCF8591";

    /**
     * Group 1 part of slave address for PCF8591
     */
    public final static int CLASS = 0x9;

    /**
     * Constant for 'Autoincrement Flag' (switched on if 1).
     */
    public final static int AUTOINCREMENT = 0x4;

    /**
     * Constant for 'Analogue Output Enable Flag' (analogue output active if 1).
     */
    public final static int ANALOGUE_OUTPUT_ENABLE = 0x40;

    /**
     * Constant for 'Analogue Input Programming': Four single ended inputs.
     */
    public final static int INPUT_MODE_0 = 0x00;

    /**
     * Constant for 'Analogue Input Programming': Three differential inputs.
     */
    public final static int INPUT_MODE_1 = 0x10;

    /**
     * Constant for 'Analogue Input Programming': Single ended and differential mixed.
     */
    public final static int INPUT_MODE_2 = 0x20;

    /**
     * Constant for 'Analogue Input Programming': Two differential inputs.
     */
    public final static int INPUT_MODE_3 = 0x30;


    private int lastChannel = -1;

    private int controlByte = 0;

    /**
     * Constructor.
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public PCF8591(int deviceAddress) throws Exception {
        super(NAME, CLASS, deviceAddress);
    }

    public void setAnalogueInputProgramming(int mode) throws IllegalArgumentException {
        if (mode == INPUT_MODE_0 ||
                mode == INPUT_MODE_1 ||
                mode == INPUT_MODE_2 ||
                mode == INPUT_MODE_3) {

            controlByte = mode;
        } else {
            throw new IllegalArgumentException("Invalid Input Mode : " + mode + "!");
        }
    }

    /**
     * Returns the value of the desired A/D channel.
     *
     * @param channel
     * @return channel value
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public int getChannel(int channel) throws Exception, IllegalArgumentException {
        int value = 0;
        checkChannel(channel);
        //write
        int wdata[] = {devAdr.getWriteAddress(), controlByte | channel};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        if (channel == lastChannel) {
            int temp[] = readI2C(rdata, 1);
            value = temp[0];
        } else {
            int temp[] = readI2C(rdata, 2);
            value = temp[1];
        }
        lastChannel = channel;

        //mark not used channels invalid
        if ((((controlByte & INPUT_MODE_1) == INPUT_MODE_1)
                || ((controlByte & INPUT_MODE_2) == INPUT_MODE_2))
                && channel == 3) {

            value = -1;
            lastChannel = -1;
        } else if (((controlByte & INPUT_MODE_3) == INPUT_MODE_3)
                && (channel == 2 || channel == 3)) {

            value = -1;
            lastChannel = -1;
        }
        return value;
    }

    /**
     * Returns the values of all A/D channels.
     *
     * @param channel
     * @return channel value
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public int[] getChannels() throws Exception, IllegalArgumentException {
        //write
        int wdata[] = {devAdr.getWriteAddress(), controlByte | AUTOINCREMENT};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        int ret[] = readI2C(rdata, 5);
        lastChannel = 0;

        //mark not used channels invalid
        if (((controlByte & INPUT_MODE_1) == INPUT_MODE_1)
                || (controlByte & INPUT_MODE_2) == INPUT_MODE_2) {
            ret[4] = -1;
            lastChannel = -1;
        } else if ((controlByte & INPUT_MODE_3) == INPUT_MODE_3) {
            ret[3] = -1;
            ret[4] = -1;
            lastChannel = -1;
        }

        int channels[] = {ret[1], ret[2], ret[3], ret[4]};
        return channels;
    }

    /**
     * Sets the control byte's AUTOINCREMENT FLAG.
     *
     * @since 0.9.6
     */
    public void enableAutoincrement() {
        controlByte = controlByte | AUTOINCREMENT;
    }

    /**
     * Clears the control byte's AUTOINCREMENT FLAG.
     *
     * @since 0.9.6
     */
    public void disableAutoincrement() {
        controlByte = controlByte & ~AUTOINCREMENT;
    }

    /**
     * Sets the control byte's ANALOGUE OUTPUR ENABLE FLAG.
     */
    public void enableAnalogueOutput() {
        controlByte = controlByte | ANALOGUE_OUTPUT_ENABLE;
    }

    /**
     * Clears the control byte's ANALOGUE OUTPUR ENABLE FLAG.
     *
     * @since 0.9.6 working ;-)
     */
    public void disableAnalogueOutput() {
        controlByte = controlByte & ~ANALOGUE_OUTPUT_ENABLE;
    }

    /**
     * Returns the current control byte.
     *
     * @return control byte state.
     * @since 0.9.6
     */
    public int getControlByte() {
        return controlByte;
    }

    /**
     * Sets the analogue output value for A/D conversion.
     * Use method enableAnalogueOutput() before!
     *
     * @param value desired output value
     */
    public void setAnalogueOutputValue(int value) throws Exception, IllegalArgumentException {
        checkValue(value);
        //write
        int wdata[] = {devAdr.getWriteAddress(), controlByte, value};
        writeI2C(wdata);
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        monitor = iow.getMonitor();
    }


    /**
     * Returns Information about this PCF8591 as String.
     *
     * @return String representation of information about this PCF8591.
     *         (slave address, temperatures, ...).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());
        sb.append(":");
        sb.append("SlaveAddress[");
        sb.append(devAdr);
        sb.append("]");
        //channel values ????
        return sb.toString();
    }

    /**
     * Indicates whether some other PCF8591 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof PCF8591))
            return false;

        PCF8591 pcf = (PCF8591) o;
        return this.devAdr.equals(pcf.devAdr);
    }

    /**
     * Returns a hash code value for this PCF8591 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }


    private void checkChannel(int channel) throws IllegalArgumentException {
        if ((channel < 0) || (channel > 3)) {
            throw new IllegalArgumentException("Channel index out of range (0 ... 3)");
		}
	}
	
	private void checkValue(int value) throws IllegalArgumentException {
		if( (value < 0) || (value > 255)) {
			throw new IllegalArgumentException("Value out of range (0 ... 255)");
		}
	}
}
