/*
 * This File is part of the iowj-project   
 * $Id: Iow24.java,v 1.8 2008/04/25 14:47:31 Thomas Wagner Exp $
 * Copyright (C)2004-2006 by Thomas Wagner
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

package de.wagner_ibw.iow;

import com.codemercs.iow.IowKit;

/**
 * This class represents the IO-Warrior 24 device.
 *
 * @author Thomas Wagner
 */
public class Iow24 extends AbstractIowDevice {

    /**
     * Constructor.
     *
     * @param handle device handle of this IO-Warrior 24 device .
     * @param serial serial number of this IO-Warrior 24 device.
     * @param rev    revision of this IO-Warrior 24 device.
     */
    public Iow24(long handle, String serial, int rev) {
        portCount = 2;
        id = (int) IOW24ID;
        smfReportLength = IOW24_SMF_REPORT_LENGTH;
        this.handle = handle;
        this.serial = serial;
        this.rev = rev;

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#scanPorts()
      */

    public long scanPorts() {
        long ret = 0;
        int rbuf[] = getCurrentPinStatus();
        if (rbuf.length > 0) {
            ports[0].setDataFromRead(rbuf[0] & 0xff);
            ports[1].setDataFromRead(rbuf[1] & 0xff);
            ret = rbuf[0] & 0xff;
            ret = ret + ((rbuf[1] & 0xff) << 8);
        }
        return ret;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#writeIOPorts()
      */

    public long writeIOPorts() throws IllegalStateException {
        checkDeviceState();
        int[] wbuf = {0, 0, 0};
        wbuf[1] = ports[0].getDataToWrite();
        wbuf[2] = ports[1].getDataToWrite();
        long ret = IowKit.write(handle, 0, wbuf);
        //*deb*/System.out.println("[Iow24.writeIOPorts() ret=" +ret);
        if (ret == 0)
            suspendDevice();
        return ret;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#writeIOPorts()
      */

    public long writeIOPorts(long value) throws IllegalStateException {
        checkDeviceState();
        int[] wbuf = {0, 0, 0};
        wbuf[1] = (int) (value & 0xff);
        wbuf[2] = (int) ((value >> 8) & 0xff);
        long ret = IowKit.write(handle, 0, wbuf);
        //*deb*/System.out.println("[Iow24.writeIOPorts() ret=" +ret);
        if (ret == 0)
            suspendDevice();
        return ret;
    }


    /**
     * Returns a <code>String</code> object representing this <code>Iow24</code> object.
     * It contains information about device handle, id , revision,
     * serial number and the status of port 0 ... port 1.
     */
    public String toString() {
        boolean i2c = false;
        boolean spi = false;

        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(":Handle[");
        sb.append(getHandle());
        sb.append("],Id[");
        sb.append(getId());
        sb.append("],Rev[");
        sb.append(Integer.toHexString(getRev()));
        sb.append("],Serial[");
        sb.append(getSerial());
        sb.append("],Listener[");
        sb.append(listeners.size());
        sb.append("],SpecialMode: ");
        if ((specialMode & SpecialModeFunction.SMF_LCD_ID)
                == SpecialModeFunction.SMF_LCD_ID)
            sb.append("LCD ");
        if ((specialMode & SpecialModeFunction.SMF_RC5_ID)
                == SpecialModeFunction.SMF_RC5_ID)
            sb.append("RC5 ");
        if ((specialMode & SpecialModeFunction.SMF_CPS_ID)
                == SpecialModeFunction.SMF_CPS_ID)
            sb.append("CPS ");
        if ((specialMode & SpecialModeFunction.SMF_CTI_ID)
                == SpecialModeFunction.SMF_CTI_ID)
            sb.append("CTI ");
        if ((specialMode & SpecialModeFunction.SMF_SPI_ID)
                == SpecialModeFunction.SMF_SPI_ID) {
            sb.append("SPI ");
            spi = true;
        }
        if ((specialMode & SpecialModeFunction.SMF_I2C_ID)
                == SpecialModeFunction.SMF_I2C_ID) {
            sb.append("I2C");
            i2c = true;
        }
        sb.append("\n\t");
        sb.append(ports[0].toString());
        sb.append("\n\t");
        sb.append(ports[1].toString());
        if (spi) {
            sb.append("\n\t");
            for (int i = 0; i < smfImplementations.size(); i++) {
                SpecialModeFunction smf =
                        (SpecialModeFunction) smfImplementations.get(i);
                if (smf.getSpecialModeFuncionId()
                        == SpecialModeFunction.SMF_SPI_ID) {
                    sb.append(smf.toString());
                    break;
                }
            }
        }
        if (i2c) {
            sb.append("\n\t");
            for (int i = 0; i < smfImplementations.size(); i++) {
                SpecialModeFunction smf =
                        (SpecialModeFunction) smfImplementations.get(i);
                if (smf.getSpecialModeFuncionId()
                        == SpecialModeFunction.SMF_I2C_ID) {
                    sb.append(smf.toString());
                    break;
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#getName()
      */

    public String getName() {
        return IOW24NAME;
    }

    /**
     * Indicates whether some other <code>Iow24</code> object is "equal to" this one.
     * Criteria is the serial number.
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Iow24))
            return false;

        Iow24 dev = (Iow24) obj;
        return (this.serial.equals(dev.serial)) && (this.id == dev.id);
	}
	
}
