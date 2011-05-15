/*
 * This File is part of the iowj-project   
 * $Id: SpecialModeFunction.java,v 1.4 2009/08/29 11:07:16 Thomas Wagner Exp $
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

/**
 * Interface for special mode function implementations.
 *
 * @author Thomas Wagner
 */
public interface SpecialModeFunction {

    /**
     * Special mode function id for LCD special mode function.
     */
    final static int SMF_LCD_ID = 1;

    /**
     * Special mode function id for RC5 special mode function.
     */
    final static int SMF_RC5_ID = 2;

    /**
     * Special mode function id for I2C special mode function.
     */
    final static int SMF_I2C_ID = 4;

    /**
     * Special mode function id for current pin status special mode function.
     */
    final static int SMF_CPS_ID = 8;

    /**
     * Special mode function id for SPI special mode function.
     */
    final static int SMF_SPI_ID = 16;

    /**
     * Special mode function id for Switch Matrix (SMX) special mode function.
     *
     * @since 0.9.4
     */
    final static int SMF_SMX_ID = 32;

    /**
     * Special mode function id for Led Matrix special mode function.
     * (Prepared for further use).
     *
     * @since 0.9.x
     */
    final static int SMF_LED_ID = 64;

    /**
     * Special mode function id for Switch Matrix (SMX16) special mode function.
     *
     * @since 0.9.7
     */
    final static int SMF_SMX16_ID = 128;

    /**
     * Special mode function id for Capture Timer (CTI) special mode function.
     *
     * @since 0.9.7
     */
    final static int SMF_CTI_ID = 256;

    /**
     * Returns the special mode function id for this implementation.
     *
     * @return Special mode function id (SMF_LCD_ID,SMF_RC5_ID,SMF_I2C_ID,SMF_CPS_ID,SMF_SPI_ID,SMF_SMX_ID,SMF_SMX16_ID,SMF_LED_ID).
     */
    int getSpecialModeFuncionId();


    /**
     * Returns the report id(s) which the implementation is interested in.
     *
     * @return Int array of special mode function id(s).
     */
    int[] getReportIds();

    /**
     * Callback method is called when a matching report was received.
     *
     * @param readBuffer Read buffer from a received report.
     */
    void reportReceived(int[] readBuffer);

    /**
     * Checks if this implementation is interested in this report.
     *
     * @param reportId Report id of the received report.
     * @return True if it fits or false if not.
     */
    boolean matchReportId(int reportId);

    /**
     * Sets the reference to the underlying iow device for write report operations.
     *
     * @param iow Instance of an iow device.
     */
    void setIowDevice(AbstractIowDevice iow);

    /**
     * Returns an array of bitmask(s) for port 0...1, 0...3 or 0...6 if this special mode
     * function runs on this iow device. Returns a array of lenght zero if no bits used by
     * this special mode function.
     * 1 means this bit is dedicated by special mode function. 0 means this bit is free for
     * normal pin io operations.
     *
     * @param deviceIdentifier Iow device identifier (<code>AbstractIowDevice.IOW24ID<(code>,
     *                         <code>AbstractIowDevice.IOW24ID</code> or <code>AbstractIowDevice.IOW56ID</code>).
     * @return Array of int that contains bitmask(s) for special mode function bits.
     * @since 0.9.4
     */
    int[] getIowSpecialBits(int deviceIdentifier);

    /**
     * Returns the report that enables the special mode function.
     *
     * @return Special mode function enable report.
     */
    int[] getEnableReport();

    /**
     * Returns the report that disables the special mode function.
     *
     * @return Special mode function disable report.
     */
    int[] getDisableReport();

    /**
     * Checks if it is possible to activate this special mode function.
     * This depends on id and/or rev of the iow device and/or already
     * activated special mode functions.
     *
     * @param id           Id of the current IO-Warrior device.
     * @param rev          Rev of the current IO-Warrior device.
     * @param specialModes Special modes from the current IO-Warrior device.
     * @return Error string if special mode function is not available.
     *         Returns null if special mode function is available.
     * @since 0.9.4
     */
    String checkCompatibility(int id, int rev, int specialModes);
	
	/**
     * Returns the name of the special mode function.
     *
     * @return Name of the special mode function.
     * @since 0.9.4
     */
	String getName();
	
}
