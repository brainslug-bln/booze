/* 
 * This File is part of the iowj-project  
 * $Id: SD20.java,v 1.1 2007/03/17 17:35:46 Thomas Wagner Exp $
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
 * This is the implementation of the SD20 device "Servocontroller" by
 * Jörg Pohl (www.roboter-teile.de))
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class SD20 extends AbstractI2CDevice {


    /**
     * SD20 device specific constants.
     */
    public final static String NAME = "SD20";

    /**
     * Group 1 part of slave address for SD20
     */
    public final static int CLASS = 0xC;

    /**
     * Group 2 part of slave address for SD20
     * There is only one address possible.
     */
    public final static int DEVICE_ADDRESS = 0x1;

    /**
     * Maximum number of servos.
     */
    public final static int MAX_NUMBER_OF_SERVOS = 20;

    /**
     * First servo index.
     */
    public final static int FIRST_SERVO_INDEX = 1;

    /**
     * Constructor
     *
     * @throws Exception If anything goes wrong.
     */
    public SD20() throws Exception {
        super(NAME, CLASS, DEVICE_ADDRESS);
    }

    public int getRevision() throws Exception {
        int wdata[] = {devAdr.getWriteAddress(), 0};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(rdata, 1);
        return temp[0];

    }

    /**
     * @param servo
     * @return read value
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException
     */
    public int read(int servo) throws Exception, IllegalArgumentException {
        checkServo(servo);
        //write
        int wdata[] = {devAdr.getWriteAddress(), servo};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(rdata, 1);
        return temp[0];
    }


    /**
     * @param servo
     * @param data
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException
     */
    public void write(int servo, int data) throws Exception, IllegalArgumentException {
        checkServo(servo);
        //write
        int wdata[] = {devAdr.getWriteAddress(), servo, data};
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
     * Returns Information about this SD20 as String.
     *
     * @return String representation of information about this SD20.
     *         (slave address, temperatures, ...).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString());
        sb.append(":");
        sb.append("SlaveAddress[");
        sb.append(devAdr);
        sb.append("]");

        return sb.toString();
    }

    /**
     * Indicates whether some other SD20 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof SD20))
            return false;

        SD20 pcf = (SD20) o;
        return this.devAdr.equals(pcf.devAdr);
    }

    /**
     * Returns a hash code value for this SD20 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }


    private void checkServo(int servo) throws IllegalArgumentException {
        if ((servo < FIRST_SERVO_INDEX) || (servo > MAX_NUMBER_OF_SERVOS)) {
            throw new IllegalArgumentException("Servo index out of range ("
				+ FIRST_SERVO_INDEX + "..."+ (MAX_NUMBER_OF_SERVOS) +")");
		}
	}
}
