/* 
 * This File is part of the iowj-project  
 * $Id: SPIDevice.java,v 1.3 2007/03/17 18:46:25 Thomas Wagner Exp $
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

import de.wagner_ibw.iow.AbstractIowDevice;

/**
 * Each SPI device implementation must implements this interface.
 *
 * @author Thomas Wagner
 */
public interface SPIDevice {

    /**
     * Returns the name of the spi device.
     *
     * @return Name of the spi device.
     */
    String getName();

    /**
     * Callback method is called when a matching report was received.
     *
     * @param readBuffer Read buffer from a received report.
     */
    void reportReceived(int[] readBuffer);

    /**
     * Sets the reference to an IO-Warrior device for write report operations.
     *
     * @param iow Instance of an IO-Warrior device.
     */
    void setIowDevice(AbstractIowDevice iow);
}
