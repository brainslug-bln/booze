/* 
 * This File is part of the iowj-project   
 * $Id: I2C.java,v 1.4 2007/03/17 17:35:46 Thomas Wagner Exp $
 * Copyright (C)2005 by Thomas Wagner
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
import de.wagner_ibw.iow.SpecialModeFunction;

import java.util.Vector;

/**
 * Implementation of the I2C special mode function.
 *
 * @author Thomas Wagner
 */
public class I2C implements SpecialModeFunction {

    /**
     * Name of this special mode function.
     */
    final static String name = "I2C";

    public final static int I2C_SPEED_100KHZ = 0;
    public final static int I2C_SPEED_400KHZ = 1;
    public final static int I2C_SPEED_50KHZ = 2;

    private int i2cSpeed = I2C_SPEED_100KHZ;

    /**
     * Reference to the underlying IOW device.
     */
    private AbstractIowDevice iow;

    /**
     * Vector of i2c devices.
     */
    private Vector devices;

    /**
     * Constructor
     */
    public I2C() {
        devices = new Vector();
    }

    /**
     * Constructor.
     *
     * @param speed I2C special mode function speed byte (only for IOW56).
     */
    public I2C(int speed) {
        devices = new Vector();
        i2cSpeed = speed;
    }

    /**
     * Adds i2c device implementation (e.g. an instance of LM75.java).
     *
     * @param dev Instance of an i2c device implemenation.
     * @throws IllegalArgumentException if the i2c slave address was already assigned.
     */
    public void addI2CDevice(I2CDevice dev) throws IllegalArgumentException {
        if (!devices.contains(dev)) {
            devices.add(dev);
            dev.setIowDevice(iow);
        } else {
            throw new IllegalArgumentException("Slave address " + dev.getI2cAddress() + " was alreay assigned!");
        }
    }

    /**
     * Removes i2c device implementation.
     *
     * @param dev Instance of an i2c device implemenation.
     */
    public void removeI2CDevice(I2CDevice dev) {
        if (devices.contains(dev)) {
            devices.remove(dev);
            dev.setIowDevice(null);
        }
    }

    /**
     * Indicates whether some other I2C object is "equal to" this one.
     * Criteria is the special mode function's name.
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object o) {
        if (!(o instanceof I2C))
            return false;

        I2C i2c = (I2C) o;
        return this.getName().equals(i2c.getName());
    }

    /**
     * Returns a hash code value for this special mode function object.
     * It is derived from the special mode funtion's name.
     *
     * @return a hash code value for this object.
     * @since 0.9.4
     */
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * Returns a string representation of the object. It consists of
     * a list of added i2c devices.
     *
     * @return a string consisting of an i2c devices list.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("I2C:");
        for (int i = 0; i < devices.size(); i++) {
            I2CDevice dev = (I2CDevice) devices.get(i);
            String name = dev.getName();
            if (i != 0)
                sb.append(",");
            sb.append(name);
            sb.append("[");
            sb.append(dev.getI2cAddress().toString());
            sb.append("]");
        }

        return sb.toString();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        int wbuf[] = {1, 0};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        int wbuf[] = {1, 1, i2cSpeed};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW24ID) {
            int masks[] = {0x06};
            return masks;
        } else if (deviceIdentifier == AbstractIowDevice.IOW56ID) {
            int masks[] = {0, 0xa0};
            return masks;
        } else {
            int masks[] = {0xc0};
            return masks;
        }
    }
    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public int[] getReportIds() {
        int ids[] = {0x02, 0x03};
        return ids;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_I2C_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public boolean matchReportId(int reportId) {
        return ((reportId == 0x02) || (reportId == 0x03));
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        //*deb*/System.out.println("i2c.reportReceived() received report: " + readBuffer[0]);

        for (int i = 0; i < devices.size(); i++) {
            AbstractI2CDevice dev = (AbstractI2CDevice) devices.get(i);
            //*deb*/System.out.println("\tcurr:"+currentDevice+"i:"+dev);
            if (dev.equals(iow.getMonitor().getCurrentDevice())) {
                dev.reportReceived(readBuffer);
                //*deb*/System.out.println("\tequals");
                break;
            } else {
                //*deb*/System.out.println("\tnot equals");
            }
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility()
      * @since 0.9.4
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        //no limitations
        return null;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */
	public String getName() {
		return name;
	}
}
