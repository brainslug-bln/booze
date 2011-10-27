/* 
 * This File is part of the iowj-project  
 * $Id: PCF8570.java,v 1.3 2007/03/17 17:35:46 Thomas Wagner Exp $
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
 * This is the implementation of the PCF8570P device, "256 x 8-bit static low-voltage RAM".
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public class PCF8570 extends AbstractI2CDevice {


    /**
     * PCF8570 device specific constants.
     */
    public final static String NAME = "PCF8570";

    /**
     * Capacity of this RAM.
     */
    public final int RAM_SIZE = 256;

    /**
     * Group 1 part of slave address for PCF8570
     * (PCF8570 device specific constant).
     */
    public final static int CLASS = 0xA;

    /**
     * Constructor
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public PCF8570(int deviceAddress) throws Exception {
        super(NAME, CLASS, deviceAddress);
    }

    /**
     * @param address
     * @return read value
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException
     */
    public int read(int address) throws Exception, IllegalArgumentException {
        checkAddress(address);
        //write
        int wdata[] = {devAdr.getWriteAddress(), address};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(rdata, 1);
        return temp[0];
    }


    /**
     * @param address
     * @param data
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException
     */
    public void write(int address, int data) throws Exception, IllegalArgumentException {
        checkAddress(address);
        //write
        int wdata[] = {devAdr.getWriteAddress(), address, data};
        writeI2C(wdata);
    }

    /**
     * @param address
     * @param data
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException
     */
    public void writePage(int address, int data[]) throws Exception, IllegalArgumentException {
        checkAddress(address);
        int length = data.length;
        if ((length == 0) || (length > MAX_I2C_LENGTH)) {
            throw new IllegalArgumentException("Array size out of range (1..." + MAX_I2C_LENGTH + ")");
        }
        int dbuf[] = new int[length + 2];
        dbuf[0] = devAdr.getWriteAddress();
        dbuf[1] = address;
        for (int i = 0; i < length; i++) {
            dbuf[i + 2] = data[i];
        }
        writeI2C(dbuf);
    }

    public int[] readPage(int address, int length) throws Exception, IllegalArgumentException {
        checkAddress(address);
        if ((length == 0) || (length > MAX_I2C_LENGTH)) {
            throw new IllegalArgumentException("Expected array size out of range (1..." + MAX_I2C_LENGTH + ")");
        }
        //write
        int wdata[] = {devAdr.getWriteAddress(), address};
        writeI2C(wdata);
        //read
        int rdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(rdata, length);
        return temp;
    }


    /**
     * Fills the whole ram with the specified pattern.
     *
     * @param pattern Pattern that fills the ram.
     * @throws Exception If any transmission error occurred.
     */
    public void fillRam(int pattern) throws Exception {
        int pat[] = new int[MAX_I2C_LENGTH];
        for (int i = 0; i < MAX_I2C_LENGTH; i++) {
            pat[i] = pattern;
        }

        for (int i = 0, pages = RAM_SIZE / MAX_I2C_LENGTH; i < pages; i++) {
            writePage(i * MAX_I2C_LENGTH, pat);
        }
    }
    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        monitor = iow.getMonitor();
    }


    /**
     * Returns Information about this PCF8570 as String.
     *
     * @return String representation of information about this PCF8570.
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
     * Indicates whether some other PCF8570 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof PCF8570))
            return false;

        PCF8570 pcf = (PCF8570) o;
        return this.devAdr.equals(pcf.devAdr);
    }

    /**
     * Returns a hash code value for this PCF8570 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }


    private void checkAddress(int address) throws IllegalArgumentException {
        if ((address < 0) || (address > RAM_SIZE -1)) {
			throw new IllegalArgumentException("Address out of range (1..."+ (RAM_SIZE -1) +")");
		}
	}
}
