/*
 * This File is part of the iowj-project   
 * $Id: Iow40.java,v 1.9 2009/08/29 11:05:24 Thomas Wagner Exp $
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
 * This class represents the IO-Warrior 40 device.
 *
 * @author Thomas Wagner
 */
public class Iow40 extends AbstractIowDevice {

    /**
     * Constructor.
     *
     * @param handle device handle of this IO-Warrior 40 device.
     * @param serial serial number of this IO-Warrior 40 device.
     * @param rev    revision of this IO-Warrior 40 device.
     */
    public Iow40(long handle, String serial, int rev) {
        portCount = 4;
        id = (int) IOW40ID;
        smfReportLength = IOW40_SMF_REPORT_LENGTH;
        this.handle = handle;
        this.serial = serial;
        this.rev = rev;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#scanPorts()
      */

    public long scanPorts() throws UnsupportedOperationException {
        long ret = 0;
        int rbuf[] = getCurrentPinStatus();
        if (rbuf.length > 0) {
            for (int i = 0; i < rbuf.length; i++) {
                int val = rbuf[i] & 0xff;
                ports[i].setDataFromRead(val);
                long newval = (long) val << (i * 8);
                ret = ret + newval;
            }
        }
        return ret;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#writeIOPorts()
      */

    public long writeIOPorts() throws IllegalStateException {
        checkDeviceState();
        int[] wbuf = {0, 0, 0, 0, 0};
        wbuf[1] = ports[0].getDataToWrite();
        wbuf[2] = ports[1].getDataToWrite();
        wbuf[3] = ports[2].getDataToWrite();
        wbuf[4] = ports[3].getDataToWrite();
        long ret = IowKit.write(handle, 0, wbuf);
        //*deb*/System.out.println("[Iow40.writeIOPorts() ret=" +ret);
        if (ret == 0)
            suspendDevice();
        return ret;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#writeIOPorts()
      */

    public long writeIOPorts(long value) throws IllegalStateException {
        checkDeviceState();
        int[] wbuf = {0, 0, 0, 0, 0};
        wbuf[1] = (int) (value & 0xff);
        wbuf[2] = (int) ((value >> 8) & 0xff);
        wbuf[3] = (int) ((value >> 16) & 0xff);
        wbuf[4] = (int) ((value >> 24) & 0xff);
        long ret = IowKit.write(handle, 0, wbuf);
        //*deb*/System.out.println("[Iow40.writeIOPorts() ret=" +ret);
        if (ret == 0)
            suspendDevice();
        return ret;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.AbstractIowDevice#getName()
      */

    public String getName() {
        return IOW40NAME;
    }

    /**
     * Returns a <code>String</code> object representing this <code>Iow40</code> object.
     * It contains information about device handle, id , revision,
     * serial number and the status of port 0 ... port 3.
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
        if ((specialMode & SpecialModeFunction.SMF_CPS_ID)
                == SpecialModeFunction.SMF_CPS_ID)
            sb.append("CPS ");
        if ((specialMode & SpecialModeFunction.SMF_SMX_ID)
                == SpecialModeFunction.SMF_SMX_ID)
            sb.append("SMX ");
        if ((specialMode & SpecialModeFunction.SMF_SMX16_ID)
                == SpecialModeFunction.SMF_SMX16_ID)
            sb.append("SMX16 ");
        if ((specialMode & SpecialModeFunction.SMF_I2C_ID)
                == SpecialModeFunction.SMF_I2C_ID) {
            sb.append("I2C");
            i2c = true;
        }
        sb.append("\n\t");
        sb.append(ports[0].toString());
        sb.append("\n\t");
        sb.append(ports[1].toString());
        sb.append("\n\t");
        sb.append(ports[2].toString());
        sb.append("\n\t");
        sb.append(ports[3].toString());
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

    /**
     * Indicates whether some other <code>Iow40</code> object is "equal to" this one.
     * Criteria is the serial number.
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public boolean equals(Object o) {
        if (!(o instanceof Iow40))
            return false;

        Iow40 dev = (Iow40) o;
        return (this.serial.equals(dev.serial)) && (this.id == dev.id);
	}

}
