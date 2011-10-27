/*
 * This File is part of the iowj-project   
 * $Id: GenericSPIDevice.java,v 1.2 2007/03/17 18:46:25 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.spi;

/**
 * Implementation of a generic spi device.
 * Tested successfully with AvrMega8.
 *
 * @author Achim Stoesgen, Thomas Wagner
 * @since 0.9.4.
 */
public class GenericSPIDevice extends AbstractSPIDevice {

    /**
     * Constructor.
     *
     * @param name     Name of the spi device.
     * @param spiFlags Flags for SPI bus.
     */
    public GenericSPIDevice(String name, int spiFlags) {
        super(name, spiFlags);
    }


    /**
     * Reads values from device.
     *
     * @param number of values to read.
     * @return values from devices.
     * @throws Exception If any transmission error occurred.
     */
    public int[] read(int anz) throws Exception {
        int dbuf[] = new int[anz];
        int temp[] = transmit(dbuf);
        return temp;
    }

    /**
     * Writes values to device.
     *
     * @param data values to write.
     * @throws Exception If any transmission error occurred.
     */
    public void write(int data[]) throws Exception {
		transmit( data );
	}

}
