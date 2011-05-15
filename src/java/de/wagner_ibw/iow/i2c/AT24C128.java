/* 
 * This File is part of the iowj-project  
 * $Id: AT24C128.java,v 1.1 2007/03/17 17:35:46 Thomas Wagner Exp $
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
 * This is the implementation of the AT24C128 device, "16 KByte EEPROM" by ATMEL.
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class AT24C128 extends AbstractI2CDevice {


    /**
     * AT24C128 device specific constants.
     */
    public final static String NAME = "AT24C128";

    /**
     * Capacity of this EEPROM in byte.
     */
    public final int EEPROM_SIZE = 16384;

    /**
     * Maximum page size for block write operations.
     */
    public final int MAX_PAGE_SIZE = 64;

    /**
     * Group 1 part of slave address for AT24C128
     * (AT24C128 device specific constant).
     */
    public final static int CLASS = 0xA;

    /**
     * Constructor
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public AT24C128(int deviceAddress) throws Exception {
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
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        //write
        int wdata[] = {devAdr.getWriteAddress(), addrHigh, addrLow};
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
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        //write
        int wdata[] = {devAdr.getWriteAddress(), addrHigh, addrLow, data};

        System.out.println("before writeI2C: " + wdata.toString());
        System.out.flush();
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
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        int length = data.length;
        if ((length == 0) || (length > MAX_I2C_LENGTH)) { //max page size ????????
            throw new IllegalArgumentException("Array size out of range (1..." + MAX_I2C_LENGTH + ")");
        }
        int dbuf[] = new int[length + 3];
        dbuf[0] = devAdr.getWriteAddress();
        dbuf[1] = addrHigh;
        dbuf[2] = addrLow;
        for (int i = 0; i < length; i++) {
            dbuf[i + 3] = data[i];
        }
        writeI2C(dbuf);
    }

    public int[] readPage(int address, int length) throws Exception, IllegalArgumentException {
        checkAddress(address);
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;

        //check length
        int rbuf[] = new int[length];
        int remain = length;
        int partLength = 0;
        int pointerOutArray = 0;

        int wdata[] = {devAdr.getWriteAddress(), addrHigh, addrLow};
        writeI2C(wdata);


        while (remain > 0) {
            if (length > 255) {
                partLength = 255;
                remain = remain - 255;
                length = length - 255;
            } else { //gleich oder kleiner 225
                partLength = length;
                remain = remain - length;
            }

            int rdata[] = {devAdr.getReadAddress()};
            int temp[] = readI2C(rdata, length);

            for (int t = 0; t < partLength; t++) {
                rbuf[pointerOutArray++] = temp[t];
            }
        }
        return rbuf;


        /*
          if( (length == 0) || (length > MAX_I2C_LENGTH)) {
              throw new IllegalArgumentException("Expected array size out of range (1..." + MAX_I2C_LENGTH + ")");
          }
          //write
          int wdata[] = {devAdr.getWriteAddress(),addrHigh,addrLow};
          writeI2C(wdata);
          //read
          int rdata[] = {devAdr.getReadAddress()};
          int temp[] = readI2C(rdata,length);
          return temp;
      */
    }


    /**
     * Fills the whole eeprom with the specified pattern.
     *
     * @param pattern Pattern that fills the ram.
     * @throws Exception If any transmission error occurred.
     */
    public void fillEeprom(int pattern) throws Exception {
        int pat[] = new int[MAX_PAGE_SIZE];
        for (int i = 0; i < MAX_PAGE_SIZE; i++) {
            pat[i] = pattern;
        }

        for (int i = 0, pages = EEPROM_SIZE / MAX_PAGE_SIZE; i < pages; i++) {
            //*deb*/System.out.println("fillEeprom i:" +i+"(pages:)"+(EEPROM_SIZE / MAX_PAGE_SIZE)+")");
            writePage(i * MAX_PAGE_SIZE, pat);
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
     * Returns Information about this AT24C128 as String.
     *
     * @return String representation of information about this AT24C128.
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
     * Indicates whether some other AT24C128 object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof AT24C128))
            return false;

        AT24C128 at = (AT24C128) o;
        return this.devAdr.equals(at.devAdr);
    }

    /**
     * Returns a hash code value for this AT24C128 object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }


    private void checkAddress(int address) throws IllegalArgumentException {
        if ((address < 0) || (address > EEPROM_SIZE -1)) {
			throw new IllegalArgumentException("Address out of range (1..."+ (EEPROM_SIZE -1) +")");
		}
	}
}
