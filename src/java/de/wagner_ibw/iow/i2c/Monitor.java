/* 
 * This File is part of the iowj-project   
 * $Id: Monitor.java,v 1.2 2007/03/17 17:35:46 Thomas Wagner Exp $
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

/**
 * Monitor for i2c bus operations.
 *
 * @author Thomas Wagner
 */
public class Monitor {

    /**
     * Current i2c device. If null, no pending operation.
     */
    private AbstractI2CDevice currentDevice = null;

    private final static boolean CLASS_DEBUG = false;

    /**
     * Returns the currentlye active i2c device.
     *
     * @return Active i2c device.
     */
    public synchronized AbstractI2CDevice getCurrentDevice() {
        return currentDevice;
    }

    /**
     * Begins the transmission.
     *
     * @param dev i2c which begins the transmission.
     * @throws Exception If it isn't possible to begin a transmission.
     */
    public void beginTransmission(AbstractI2CDevice dev) throws Exception {
        /*deb*/
        debug("beginTransmission(" + dev + ")...");

        int trials = 128;
        while (trials > 0) {
            if (setCurrentDevice(dev)) {
                /*deb*/
                debug("beginTransmission(" + dev + ") O.K.");
                break;
            }
            Thread.sleep(100);
            /*deb*/
            debug("beginTransmission(" + dev + ") Trials: " + trials);
            trials--;
        }
        if (trials == 0) {
            throw new Exception("[" + getCurrentDevice() + "]Unable to begin a transmission!");
        }
    }

    /**
     * Ends the transmission.
     *
     * @param dev i2c which ends the transmission.
     */
    public void endTransmission(AbstractI2CDevice dev) {
        /*deb*/
        debug("endTransmission(" + dev + ")...");
        resetCurrentDevice(dev);
    }

    /**
     * Aborts the transmission.
     *
     * @param dev i2c which aborts the transmission.
     */
    public void abortTransmission(AbstractI2CDevice dev) {
        /*deb*/
        debug("abortTransmission(" + dev + ")...");
        resetCurrentDevice(dev);
    }

    /**
     * Returns a string representation of the <code>Monitor</code> object. It consists of
     * different i2c device information.
     *
     * @return a string consisting of <code>Monitor</code> information.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Monitor[");
        if (currentDevice != null)
            sb.append(currentDevice);
        else
            sb.append("Null");
        sb.append("]");

        return sb.toString();
    }


    private synchronized boolean setCurrentDevice(AbstractI2CDevice dev) {
        /*deb*/
        debug("\tsetCurrentDevice(" + dev + ")...");
        if (currentDevice == null) {
            currentDevice = dev;
            /*deb*/
            debug("\tsetCurrentDevice(" + dev + ")\t\treturned true");
            return true;
        } else {
            /*deb*/
            debug("\tsetCurrentDevice(" + dev + ")\t\treturned false");
            return false;
        }
    }


    private synchronized boolean resetCurrentDevice(AbstractI2CDevice dev) throws IllegalStateException {
        /*deb*/
        debug("\tresetCurrentDevice(" + dev + ")...");
        if (currentDevice == null) {
            throw new IllegalStateException("[" + getCurrentDevice() + "] No transmission pending!");
        }

        if (currentDevice != dev) {
            throw new IllegalStateException("[" + getCurrentDevice() + "] Not the transmission owner!");
        }

        currentDevice = null;
        return true;
    }

    private void debug(String msg) {
        if(CLASS_DEBUG) {
			System.out.println(this + " " +msg);	
		}
	}
}
