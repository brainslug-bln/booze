/*
 * This File is part of the iowj-project   
 * $Id$
 * Copyright (C)2007 by Thomas Wagner
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

/**
 * Implementation of a RFM01 module driver.
 * RFM01 is a 433MHz ISM band FSK receiver module by www.hoperf.com.
 *
 * @author Thomas Wagner
 * @since 0.9.5rc2
 */
public class RFM01 extends AbstractSPIDevice {

    /**
     * RFM01 device specific constants.
     */
    public final static String NAME = "RFM01";

    /**
     * Constants for CLK pin frequency.
     */
    public final static int CLK_1_MHZ = 0xc200;
    public final static int CLK_1_25_MHZ = 0xc220;
    public final static int CLK_1_66_MHZ = 0xc240;
    public final static int CLK_2_MHZ = 0xc260;
    public final static int CLK_2_5_MHZ = 0xc280;
    public final static int CLK_3_33_MHZ = 0xc2a0;
    public final static int CLK_5_MHZ = 0xc2c0;
    public final static int CLK_10_MHZ = 0xc2e0;


    public final static int AFC_SETTINGS = 0xc69b;
    public final static int OPEN_RX = 0xc081;

    /**
     * Prefered SPI special mode function flags for this device.
     * IO-Warrior 56: 0 means ......
     */
    public final static int FLAGS = 0;

    /**
     * Constructor.
     */
    public RFM01() {
        super(NAME, FLAGS);
    }

    public void init() {

    }

    public void setFrequency(int frequency) {

    }

    /**
     * Writes one 16 bit value to device.
     *
     * @param data values to write.
     * @throws Exception If any transmission error occurred.
     */
    public void write(int data) throws Exception {
        int byteLow = data & 0xff;
        int byteHigh = (data & 0xff00) >> 8;
        int dbuf[] = {byteHigh, byteLow};
        transmit(dbuf);
    }


    /**
     * Reads values from device.
     *
     * @param number of values to read.
     * @return values from devices.
     * @throws Exception If any transmission error occurred.
     */
    public int[] status() throws Exception {
        int dbuf[] = {0, 0};
        int temp[] = transmit(dbuf);
		return temp;
	}

}
