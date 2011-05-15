/* 
 * This File is part of the iowj-project  
 * $Id: DS1803.java,v 1.4 2007/03/17 17:35:46 Thomas Wagner Exp $
 * Copyright (C)2006 by Thomas Wagner
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
 * Implemenation of the DS1803 device, "Two independently controlled Potentiometers". Each
 * patentiometer's wiper can be set to one of 256 positions.<br>
 * <p>Standard resistance values:
 * <ul>
 * <li>DS1803-010   10KOhm</li>
 * <li>DS1803-050   50KOhm</li>
 * <li>DS1803-100  100KOhm</li>
 * </ul></p>
 *
 * @author Thomas Wagner
 * @since 0.9.5.
 */
public class DS1803 extends AbstractI2CDevice {

    /**
     * PCF8570 device specific constants.
     */
    public final static String NAME = "DS1803";

    /**
     * Maximum position value.
     */
    public final int MAX_POSITION_VALUE = 256;


    /**
     * Command "Write Pot 0".
     */
    public final static int CMD_POT_0 = 0xA9;

    /**
     * Command "Write Pot 1".
     */
    public final static int CMD_POT_1 = 0xAA;

    /**
     * Command "Write Both Pots".
     */
    public final static int CMD_BOTH_POTS = 0xAF;

    /**
     * Group 1 part of slave address for PCF8570
     * (PCF8570 device specific constant).
     */
    public final static int CLASS = 0x05;

    private int pot0Position = -1;
    private int pot1Position = -1;


    /**
     * Constructor
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public DS1803(int deviceAddress) throws Exception {
        super(NAME, CLASS, deviceAddress);
    }

    /**
     * Reads the pot 0 position.
     *
     * @return pot 0 position (0...255)
     * @throws Exception If any transmission error occurred.
     */
    public int readPot0() throws Exception {
        readPots();
        return pot0Position;
    }

    /**
     * Reads the pot 1 position.
     *
     * @return pot 1 position (0...255)
     * @throws Exception If any transmission error occurred.
     */
    public int readPot1() throws Exception {
        readPots();
        return pot1Position;
    }

    /**
     * Reads both pots positions.
     *
     * @throws Exception If any transmission error occurred.
     */
    private void readPots() throws Exception {
        //read
        int wdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(wdata, 2);
        pot0Position = temp[0];
        pot1Position = temp[1];
    }


    /**
     * Sets the pot 0 position.
     *
     * @param position
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException if the position value is out of range
     */
    public void writePot0(int position) throws Exception, IllegalArgumentException {
        checkPosition(position);
        //write
        int wdata[] = {devAdr.getWriteAddress(), CMD_POT_0, position};
        writeI2C(wdata);
        pot0Position = position;
    }

    /**
     * Sets the pot 1 position.
     *
     * @param position
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException if the position value is out of range
     */
    public void writePot1(int position) throws Exception, IllegalArgumentException {
        checkPosition(position);
        //write
        int wdata[] = {devAdr.getWriteAddress(), CMD_POT_1, position};
        writeI2C(wdata);
        pot1Position = position;
    }

    /**
     * Sets the both pots position (same value).
     *
     * @param position
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException if the position value is out of range
     */
    public void writeBothPots(int position) throws Exception, IllegalArgumentException {
        checkPosition(position);
        //write
        int wdata[] = {devAdr.getWriteAddress(), CMD_BOTH_POTS, position};
        writeI2C(wdata);
        pot0Position = position;
        pot1Position = position;
    }

    /**
     * Sets the both pots position (different value).
     *
     * @param position
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException if the position value is out of range
     */
    public void writeBothPots(int position0, int position1) throws Exception, IllegalArgumentException {
        checkPosition(position0);
        checkPosition(position1);
        //write
        int wdata[] = {devAdr.getWriteAddress(), CMD_POT_0, position0, position1};
        writeI2C(wdata);
        pot0Position = position0;
        pot1Position = position1;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        monitor = iow.getMonitor();
    }

    /**
     * Returns Information about this DS1803 as String.
     *
     * @return String representation of information about this DS1803.
     *         (slave address, settings, ...).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());
        sb.append(":");
        sb.append("SlaveAddress[");
        sb.append(devAdr);
        sb.append("],");
        sb.append("Pot0[");
        sb.append(pot0Position);
        sb.append("],Pot1[");
        sb.append(pot1Position);
        sb.append("]");

        return sb.toString();
    }

    /**
     * Indicates whether some other DS1803 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof DS1803))
            return false;

        DS1803 pcf = (DS1803) o;
        return this.devAdr.equals(pcf.devAdr);
    }

    /**
     * Returns a hash code value for this DS1803 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }

    private void checkPosition(int position) throws IllegalArgumentException {
		if( (position <0) || (position > MAX_POSITION_VALUE -1)) {
			throw new IllegalArgumentException("Position out of range (1..."+ (MAX_POSITION_VALUE -1) +")");
		}
	}
}
