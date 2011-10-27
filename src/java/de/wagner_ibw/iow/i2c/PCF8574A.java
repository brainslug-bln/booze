/* 
 * This File is part of the iowj-project  
 * $Id: PCF8574A.java,v 1.1 2007/03/17 17:35:46 Thomas Wagner Exp $
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
import de.wagner_ibw.iow.IowPort;
import de.wagner_ibw.iow.IowPortChangeListener;

/**
 * This is the implemenation of the PCF8574A device, "Remote 8-Bit I/O Expander".
 * !!!! under construction !!!!
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class PCF8574A extends AbstractI2CDevice implements IowPortChangeListener {


    /**
     * PCF8574A device specific constants.
     */
    public final static String NAME = "PCF8574A";

    /**
     * Group 1 part of slave address for PCF8574A
     * (PCF8574A device specific constant).
     */
    public final static int CLASS = 0x7;


    private int intPort = 0;
    private int intBit = 0;

    int i = 0;

    /**
     * Constructor
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public PCF8574A(int deviceAddress) throws Exception {
        super(NAME, CLASS, deviceAddress);
    }

    /**
     * Constructor for interrupt using.
     *
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @param intPort       port number for interrupt signal.
     * @param intBit        bit number for interrupt signal.
     * @throws Exception If anything goes wrong.
     */
    public PCF8574A(int deviceAddress, int intPort, int intBit) throws Exception {
        super(NAME, CLASS, deviceAddress);
        this.intPort = intPort;
        this.intBit = intBit;

    }

    private void init() {
        iow.setDirection(intPort, 0xff); //alle ein
        iow.getPort(intPort).addPortChangeListener(this);
        iow.autonomous(true);
    }

    /**
     * Reads the port.
     *
     * @return read value
     * @throws Exception If any transmission error occurred.
     */
    public int readPort() throws Exception {
        int rdata[] = {devAdr.getReadAddress()};
        int temp[] = readI2C(rdata, 1);
        return temp[0];
    }


    /**
     * Writes the port.
     *
     * @param data
     * @throws Exception If any transmission error occurred.
     */
    public void writePort(int data) throws Exception {
        int wdata[] = {devAdr.getWriteAddress(), data};
        writeI2C(wdata);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        monitor = iow.getMonitor();
        init();
    }

    /**
     * Returns Information about this PCF8574A as String.
     *
     * @return String representation of information about this PCF8574A.
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
     * Indicates whether some other PCF8574A object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (!(o instanceof PCF8574A))
            return false;

        PCF8574A pcf = (PCF8574A) o;
        return this.devAdr.equals(pcf.devAdr);
    }

    /**
     * Returns a hash code value for this PCF8574A object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.IowPortChangeListener#portChanged(de.wagner_ibw.iow.IowPort)
      */

    public void portChanged(IowPort port) {
        System.out.println("Port changed " + i++);
        if (port.isBitClear(intBit)) {
            try {
                //readPort();
                System.out.println("Int received, read " + Integer.toBinaryString(readPort()));
			} catch (Exception e) {
				
			}
		}
	}

}
