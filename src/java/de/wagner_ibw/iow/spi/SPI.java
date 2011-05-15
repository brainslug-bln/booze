/* 
 * This File is part of the iowj-project  
 * $Id: SPI.java,v 1.3 2007/03/17 18:46:25 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.spi;

import de.wagner_ibw.iow.AbstractIowDevice;
import de.wagner_ibw.iow.SpecialModeFunction;

/**
 * Implementation of the SPI special mode function.
 *
 * @author Thomas Wagner
 */
public class SPI implements SpecialModeFunction {

    /**
     * Name of this special mode function.
     */
    final static String name = "SPI";

    private int spiMode = 0;
    private int spiClock = 0;    //new for IO-Warrior 56

    private AbstractSPIDevice device = null;
    private AbstractIowDevice iow;

    /**
     * Constructor for IO-Warrior 24.
     *
     * @param mode SPI special mode function mode byte.
     */
    public SPI(int mode) {
        spiMode = mode;
    }

    /**
     * Constructor for IO-Warrior 56.
     *
     * @param mode  SPI special mode function mode byte.
     * @param clock SPI sepcial mode function clock byte.
     * @since 0.9.5
     */
    public SPI(int mode, int clock) {
        spiMode = mode;
        spiClock = clock;
    }

    /**
     * Adds the spi device implementation (e.g. an instance of M95020.java).
     *
     * @param dev Instance of an spi device implemenation.
     */
    public void setSPIDevice(AbstractSPIDevice dev) {
        device = dev;
        dev.setIowDevice(iow);
    }

    /**
     * Removes the spi device implementation.
     */
    public void removeSPIDevice() {
        if (device != null) {
            device.setIowDevice(null);
            device = null;
        }
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SPI[");
        sb.append(device.toString());
        sb.append("]");

        return sb.toString();
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        int wbuf[] = {8, 0, 0, 0};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        //int wbuf[] = {8,1,spiMode};
        int wbuf[] = {8, 1, spiMode, spiClock}; //I hope, the IO-Warrior 24 will not be confused by the spiClock byte
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW24ID) {
            int masks[] = {0xf8};
            return masks;
        } else if (deviceIdentifier == AbstractIowDevice.IOW56ID) {
            int masks[] = {0, 0, 0, 0, 0, 0x1f};
            return masks;
        } else {
            int masks[] = {}; //no special mode availiable
            return masks;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public int[] getReportIds() {
        int ids[] = {0x09};
        return ids;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_SPI_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public boolean matchReportId(int reportId) {
        return (reportId == 0x09);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        if (device != null) {
            device.reportReceived(readBuffer);
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

    public String checkCompatibility(int id, int Revision, int specialModes) {
        //not for IO-Warrior 40
        if (id == AbstractIowDevice.IOW40ID) return "Works only with IOW24 and IOW56";
        //not at the same time with LCD on IO-Warrior 24
        if ((id == AbstractIowDevice.IOW24ID) &&
                ((specialModes & SpecialModeFunction.SMF_LCD_ID) == SpecialModeFunction.SMF_LCD_ID))
            return "LCD is already activated";
        return null;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */

    public String getName() {
		return name;
	}

}