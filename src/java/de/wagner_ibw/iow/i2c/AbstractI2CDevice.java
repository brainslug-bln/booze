/* 
 * This File is part of the iowj-project   
 * $Id: AbstractI2CDevice.java,v 1.3 2007/03/17 17:35:46 Thomas Wagner Exp $
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
import sun.misc.Queue;

/**
 * This is the abtract implementation of the <code>I2CDevice</code> interface.
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public abstract class AbstractI2CDevice implements I2CDevice {

    private final int GENERATE_START = 0x80;
    private final int GENERATE_STOP = 0x40;
    public final int MAX_I2C_LENGTH = 255;

    /**
     * Reference to the underlying IO-Warrior device.
     */
    protected AbstractIowDevice iow;

    /**
     * Reference to the i2ctransaction monitor.
     */
    protected Monitor monitor;

    /**
     * Slave address of this i2c device.
     */
    protected I2CAddress devAdr;

    /**
     * Device name.
     */
    private String name;

    protected Queue writeAckQueue;
    protected Queue readQueue;

    private final static boolean CLASS_DEBUG = false;

    /**
     * Write operation to an i2c device.
     *
     * @param in Array of int, data to write.
     * @throws Exception If any error occured.
     */
    public void writeI2C(int in[]) throws Exception {
        //IO-Warrior 56 needs special handling
        if (iow.getId() == AbstractIowDevice.IOW56ID) {
            writeI2C56(in);
            return;
        }

        monitor.beginTransmission(this);

        int length = in.length;
        int rbuf[] = null;

        int remain = length;
        int pointerInArray = 0;
        int partLength = 0;
        boolean first = true;

        while (remain > 0) {
            int flags = 0;
            if (length > 6) {
                partLength = 6;
                remain = remain - 6;
                length = length - 6;
                if (first) {
                    flags = flags + GENERATE_START;
                    first = false;
                }
                if (remain == 0) {
                    flags = flags + GENERATE_STOP;
                }
                flags = flags + partLength;
            } else { //gleich oder kleiner 6
                partLength = length;
                remain = remain - length;
                if (first) {
                    flags = flags + GENERATE_START;
                }
                flags = flags + GENERATE_STOP + partLength;
            }

            int wbuf[] = {2, flags, 0, 0, 0, 0, 0, 0};
            for (int t = 0; t < partLength; t++) {
                wbuf[t + 2] = in[pointerInArray++];
            }

            long ret = iow.writeReport(1, wbuf);
            /*deb*/
            debug("\nwriteI2C[" + monitor.getCurrentDevice() + "]: write " + ret + " byte(s)");

            rbuf = (int[]) writeAckQueue.dequeue();
            if (rbuf.length == 0) {
                monitor.abortTransmission(this);
                throw new Exception("Error in writeI2C[" + monitor.getCurrentDevice() + "]: Read acknowledge (" + this + ") failed!");
            }
            /*deb*/
            debug("\twriteI2C[" + monitor.getCurrentDevice() + "]: read " + rbuf.length + " byte(s)");
            flags = rbuf[1];
            if ((flags & 0x80) == 0x80) {
                /*deb*/
                debug("\twriteI2C[" + monitor.getCurrentDevice() + "]: acknowledge read failed");
                monitor.abortTransmission(this);
                throw new Exception("Error in writeI2C[" + monitor.getCurrentDevice() + "]: Error bit set!");
            }

            if ((flags & 0x0007) != partLength) {
                /*deb*/
                debug("\twriteI2C[" + monitor.getCurrentDevice() + "]: not all bytes acknowledged!");
                throw new Exception("Error in writeI2C[" + monitor.getCurrentDevice() + "]: Not all bytes acknowledged!");
            }
        }
        /*deb*/
        debug("\twriteI2C read acknowledgement ok");
        monitor.endTransmission(this);
    }

    /**
     * Write operation to an i2c device on IO-Warrior 56 device.
     *
     * @param in Array of int data to write.
     * @throws Exception If any error occured.
     */
    private void writeI2C56(int in[]) throws Exception {

        monitor.beginTransmission(this);

        int length = in.length;
        int rbuf[] = null;

        int remain = length;
        int pointerInArray = 0;
        int partLength = 0;
        boolean first = true;

        while (remain > 0) {
            int flags = 0;
            if (length > 62) {
                partLength = 62;
                remain = remain - 62;
                length = length - 62;
                if (first) {
                    flags = flags + GENERATE_START;
                    first = false;
                }
                if (remain == 0) {
                    flags = flags + GENERATE_STOP;
                }
                flags = flags + partLength;
            } else { //gleich oder kleiner 62
                partLength = length;
                remain = remain - length;
                if (first) {
                    flags = flags + GENERATE_START;
                }
                flags = flags + GENERATE_STOP + partLength;
            }

            int wbuf[] = new int[AbstractIowDevice.IOW56_SMF_REPORT_LENGTH];
            wbuf[0] = 2;
            wbuf[1] = flags;

            for (int t = 0; t < partLength; t++) {
                wbuf[t + 2] = in[pointerInArray++];
            }

            long ret = iow.writeReport(1, wbuf);
            /*deb*/
            debug("\nwriteI2C56[" + monitor.getCurrentDevice() + "]: write " + ret + " byte(s)");

            rbuf = (int[]) writeAckQueue.dequeue();
            if (rbuf.length == 0) {
                monitor.abortTransmission(this);
                throw new Exception("Error in writeI2C56[" + this + "]: Read acknowledge (" + this + ") failed!");
            }
            /*deb*/
            debug("\twriteI2C56[" + this + "]: read " + rbuf.length + " byte(s)");
            flags = rbuf[1];
            if ((flags & 0x80) == 0x80) {
                /*deb*/
                debug("\twriteI2C56[" + this + "]: acknowledge read failed");
                monitor.abortTransmission(this);
                throw new Exception("Error in writeI2C56[" + this + "]: Error bit set!");
            }

            if ((flags & 0x007F) != partLength) {
                /*deb*/
                debug("\twriteI2C56[" + this + "]: not all bytes acknowledged!");
                throw new Exception("Error in writeI2C56[" + this + "]: Not all bytes acknowledged!");
            }
        }
        /*deb*/
        debug("\twriteI2C56 read acknowledgement ok");
        monitor.endTransmission(this);
    }

    /**
     * Read operation from an i2c devices.
     *
     * @param in       (max 6 bytes long!)
     * @param expected
     * @return Readed values from i2c device as array.
     * @throws Exception
     */
    public int[] readI2C(int in[], int expected) throws Exception {

        monitor.beginTransmission(this);

        int rbuf[] = null;
        int length = in.length;
        int out[] = new int[expected];
        int pointerOutArray = 0;
        int remain = expected;

        int wbuf[] = {3, expected, 0, 0, 0, 0, 0, 0};
        for (int t = 0; t < length; t++) {
            wbuf[t + 2] = in[t];
        }

        long ret = iow.writeReport(1, wbuf);
        /*deb*/
        debug("\nreadI2C: write " + ret + " byte(s)");

        while (remain > 0) {
            rbuf = (int[]) readQueue.dequeue();
            if (rbuf.length == 0) {
                monitor.abortTransmission(this);
                throw new Exception("Error in readI2C: Read  (" + this + ") failed!");
            }
            /*deb*/
            debug("\treadI2C: read i2c " + rbuf.length + " byte(s)");
            int flags = rbuf[1];
            if ((flags & 0x80) == 0x80) {
                /*deb*/
                debug("\treadI2C:  read failed");
                monitor.abortTransmission(this);
                throw new Exception("Error in readI2C: Error bit set!");
            }

            int count = flags & 0x0007;
            /*deb*/
            debug("\treadI2C: read " + count + " bytes !");

            for (int t = 0; t < count; t++) {
                out[pointerOutArray++] = rbuf[t + 2];
            }
            remain = remain - count;
        }

        monitor.endTransmission(this);
        return out;
    }

    /**
     * Constructor
     *
     * @param deviceClass   Group 1 part of slave address.
     * @param deviceAddress Group 2 part of slave address (possible values 0...7).
     * @throws Exception If anything goes wrong.
     */
    public AbstractI2CDevice(String name, int deviceClass, int deviceAddress) throws Exception {
        this.name = name;
        //this.devClass = deviceClass;
        devAdr = new I2CAddress(deviceClass, deviceAddress);
        writeAckQueue = new Queue();
        readQueue = new Queue();
    }

    /**
     * Sets the reference to an i2c transaction monitor.
     *
     * @param monitor Instance of an i2c transaction monitor.
     */
    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Returns Information about this i2c device as String.
     *
     * @return String representation of this i2c device.
     */
    public String toString() {
        return name;
    }

    /**
     * Indicates whether some other i2c device object is "equal to" this one.
     * Criterias are the i2c device class and i2c device address (in I2CAddress).
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object o) {
        if (!(o instanceof AbstractI2CDevice))
            return false;

        AbstractI2CDevice lm = (AbstractI2CDevice) o;
        return this.devAdr.equals(lm.devAdr);
    }

    /**
     * Returns a hash code value for this i2c device object. It is derived from the i2c
     * device class and i2c device address.
     *
     * @return a hash code value for this object.
     * @since 0.9.4
     */
    public int hashCode() {
        return this.devAdr.hashCode();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#getI2cAddress()
      */

    public I2CAddress getI2cAddress() {
        return devAdr;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#getName()
      */

    public String getName() {
        return name;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        /*deb*/
        debug("abtractI2CDevice.reportReceived() received report: " + readBuffer[0]);
        if (readBuffer[0] == 0x02)
            writeAckQueue.enqueue(readBuffer);
        else
            readQueue.enqueue(readBuffer);

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.I2CDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public abstract void setIowDevice(AbstractIowDevice iow);

	private void debug(String msg){
		if(CLASS_DEBUG) {
			System.out.println(msg);	
		}
	}
	
}