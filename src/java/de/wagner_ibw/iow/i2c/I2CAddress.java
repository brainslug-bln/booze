/*
* This File is part of the iowj-project
* $Id: I2CAddress.java,v 1.3 2007/03/17 17:35:46 Thomas Wagner Exp $
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

/**
 * This class represents an i2c slave address. It contains the
 * group 1 part (4 bit long, further called 'device class') and the group 2
 * part (3 bit long, further called device address) of the slave address.
 *
 * @author Thomas Wagner
 */
public class I2CAddress {

    /**
     * Group 1 part of i2c slave address (bit 7...4).
     */
    private int deviceClass;

    /**
     * Group 2 part of i2c slave address (bit 3...1).
     */
    private int deviceAddress;

    /**
     * Complete i2c slave address.
     */
    private int slaveAddress;

    /**
     * Creates an new <code>I2CAddress</code>.
     *
     * @param deviceClass   Group 1 part (device class) of the i2c slave address.
     * @param deviceAddress Group 2 part (device address) of the i2c slave address.
     */
    public I2CAddress(int deviceClass, int deviceAddress) throws IllegalArgumentException {
        if (deviceClass > 16)
            throw new IllegalArgumentException("Invalid I2C device class '" + deviceClass + "' (greater than 16)!");
        if (deviceAddress > 7)
            throw new IllegalArgumentException("Invalid I2C device address '" + deviceAddress + "' (greater than 7)!");

        this.deviceClass = deviceClass;
        this.deviceAddress = deviceAddress;
        slaveAddress = deviceClass << 4 | deviceAddress << 1;
    }

    /**
     * Returns the device class.
     *
     * @return Group 2 part of i2c slave address (bit 3...1).
     * @since 0.9.4
     */
    public int getDeviceClass() {
        return deviceClass;
    }

    /**
     * Returns the device address.
     *
     * @return Group 1 part of i2c slave address (bit 7...4).
     * @since 0.9.4
     */
    public int getDeviceAddress() {
        return deviceAddress;
    }

    /**
     * Returns the slave address for read operations (bit 0 is set).
     *
     * @return Read address.
     */
    public int getReadAddress() {
        return slaveAddress | 0x01;    //set bit0
    }

    /**
     * Returns the slave address for write operations (bit 0 is clear).
     *
     * @return Write address.
     */
    public int getWriteAddress() {
        return slaveAddress & 0xfe; //clear bit0
    }

    /**
     * Returns a string representation of the object. It consists of the
     * device class and device address information.
     *
     * @return a string consisting of the device class, the character
     *         '/' and the device address.
     */
    public String toString() {
        return deviceClass + "/" + deviceAddress;
    }

    /**
     * Indicates whether some other <code>I2CAddress</code> object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address.
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object o) {
        if (!(o instanceof I2CAddress))
            return false;

        I2CAddress adr = (I2CAddress) o;
        return this.deviceClass == adr.deviceClass && this.deviceAddress == adr.deviceAddress;
    }

    /**
     * Returns a hash code value for this <code>I2CAddress</code> object. It is derived from the
     * i2c device class and i2c device address.
     *
     * @return a hash code value for this object.
     * @since 0.9.4
     */
    public int hashCode() {
		int result = 17;
		result = 37 * result + deviceClass;
		result = 37 * result + deviceAddress;
		return result;
	}
}
