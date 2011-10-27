/*
 * This File is part of the iowj-project   
 * $Id: AbstractSPIDevice.java,v 1.3 2007/03/17 18:46:25 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.spi;

import de.wagner_ibw.iow.AbstractIowDevice;
import sun.misc.Queue;

/**
 * This is the abtract implementation of the <code>SPIDevice</code> interface.
 *
 * @author Thomas Wagner
 * @since 0.9.4.
 */
public abstract class AbstractSPIDevice implements SPIDevice {

    /**
     * Reference to the underlying IO-Warrior device.
     */
    protected AbstractIowDevice iow;

    /**
     * Device name.
     */
    private String name;

    /**
     * SPI data transfer flag.
     */
    private int flags = 0;

    protected Queue readQueue;

    public AbstractSPIDevice(String name, int flags) {
        this.name = name;
        this.flags = flags;
        readQueue = new Queue();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SPIDevice#getName()
      */

    public String getName() {
        return name;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SPIDevice#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        //*deb*/System.out.println("AbstractSPIDevice.reportReceived() received report: " + readBuffer[0]);
        readQueue.enqueue(readBuffer);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SPIDevice#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
    }

    /**
     * Transmit SPI data.
     *
     * @param in Data to write out.
     * @return Data which readed in.
     * @throws Exception If a SPI write or read error occured.
     */
    protected int[] transmit(int[] in) throws Exception {
        //*deb*/System.out.println("transmit() length: " +in.length);
        //IO-Warrior 56 needs special handling
        if (iow.getId() == AbstractIowDevice.IOW56ID) {
            return transmit56(in);
        }

        int length = in.length;
        int remain = length;
        int out[] = new int[length];
        int pointerInArray = 0;
        int pointerOutArray = 0;
        int partLength = 0;
        int flag = 0;

        while (remain > 0) {
            if (length > 6) {
                partLength = 6;
                remain = remain - 6;
                length = length - 6;
                flag = flags + partLength + 0x40; //with SSactive set
            } else {
                partLength = length;
                remain = remain - length;
                flag = flags + partLength; //with SSactive not set
            }

            int wbuf[] = {9, flag, 0, 0, 0, 0, 0, 0};
            for (int t = 0; t < partLength; t++) {
                wbuf[t + 2] = in[pointerInArray++];
            }

            //send report to spi
            //*deb*/printReport("out",wbuf);
            long ret = iow.writeReport(1, wbuf);
            if (ret != 8) {
                throw new Exception("SPI write error!");
            }

            //receive report from spi
            int rbuf[] = (int[]) readQueue.dequeue();
            //*deb*/printReport("in ",rbuf);
            if (rbuf.length == 0) {
                throw new Exception("SPI read error!");
            }
            int count = rbuf[1];
            if (count != partLength) {
                throw new Exception("SPI read error!");
            }

            for (int t = 0; t < partLength; t++) {
                out[pointerOutArray++] = rbuf[t + 2];
            }
        }
        //*deb*/printReport("ret ",out);
        return out;
    }

    /**
     * Transmit SPI data on IO-Warrior 56.
     *
     * @param in Data to write out.
     * @return Data which readed in.
     * @throws Exception If a SPI write or read error occured.
     */
    protected int[] transmit56(int[] in) throws Exception {
        //*deb*/System.out.println("transmit56() length: " +in.length);

        int length = in.length;
        int remain = length;
        int out[] = new int[length];
        int pointerInArray = 0;
        int pointerOutArray = 0;
        int partLength = 0;
        int flag = 0;

        while (remain > 0) {
            if (length > 61) {
                partLength = 61;
                remain = remain - 61;
                length = length - 61;
                flag = flags + 0x40; //with SSactive set
            } else {
                partLength = length;
                remain = remain - length;
                flag = flags; //with SSactive not set
            }

            int wbuf[] = new int[AbstractIowDevice.IOW56_SMF_REPORT_LENGTH];
            wbuf[0] = 9;
            wbuf[1] = partLength;
            wbuf[2] = flag;

            for (int t = 0; t < partLength; t++) {
                wbuf[t + 3] = in[pointerInArray++];
            }

            //send report to spi
            //*deb*/printReport("out",wbuf);
            long ret = iow.writeReport(1, wbuf);
            if (ret != 64) {
                throw new Exception("SPI write error!");
            }

            //receive report from spi
            int rbuf[] = (int[]) readQueue.dequeue();
            //*deb*/printReport("in ",rbuf);
            if (rbuf.length == 0) {
                throw new Exception("SPI read error!");
            }
            int count = rbuf[1];
            if (count != partLength) {
                throw new Exception("SPI read error!");
            }
            for (int t = 0; t < partLength; t++) {
                out[pointerOutArray++] = rbuf[t + 2];
            }
        }
        //*deb*/printReport("ret ",out);
        return out;
    }

    /**
     * Only for debug purposes.
     *
     * @param dir
     * @param rep
     */
    private void printReport(String dir, int[] rep) {
        System.out.print("Report (" + dir + "):");
        for (int i = 0; i < rep.length; i++) {
            if (i != 0) {
                System.out.print(",");
            }
            System.out.print(Integer.toHexString(rep[i]));
        }
        System.out.println();
    }

    /**
     * Returns the name of the spi device as String.
     *
     * @return String representation of information about this spi device.
     */
	public String toString() {
		return name;
	}
	
}
