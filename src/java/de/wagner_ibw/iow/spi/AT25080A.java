/*
 * This File is part of the iowj-project   
 * $Id: AT25080A.java,v 1.1 2006/10/28 16:50:12 Thomas Wagner Exp $
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
 * Implemenation of the AT25080A (8Kbit EEPROM) spi device.
 *
 * @author Thomas Wagner
 * @since 0.9.5.
 */
public class AT25080A extends AbstractSPIDevice {

    /**
     * M95020 device specific constants.
     */
    public final static String NAME = "AT25080A";

    /**
     * Maximum page size for block write operations.
     */
    public final int MAX_PAGE_SIZE = 32;

    /**
     * Capacity of this EEPROM in byte.
     */
    public final int EEPROM_SIZE = 1024;

    /**
     * Instruction "Write Enable".
     */
    public final static int CMD_WREN = 6;

    /**
     * Instruction "Write Disable".
     */
    public final static int CMD_WRDI = 4;

    /**
     * Instruction "Read Status Register".
     */
    public final static int CMD_RDSR = 5;

    /**
     * Instruction "Write Status Register".
     */
    public final static int CMD_WRSR = 1;

    /**
     * Instruction "Read to Memory Array".
     */
    public final static int CMD_READ = 3;

    /**
     * Instruction "Write to Memory Array".
     */
    public final static int CMD_WRITE = 2;

    /**
     * Prefered SPI special mode function flags for this device.
     */
    public final static int FLAGS = 0;

    /**
     * Constructor.
     */
    public AT25080A() {
        super(NAME, FLAGS);
    }

    /**
     * Enables write operations.
     *
     * @throws Exception If any transmission error occurred.
     */
    public void writeEnable() throws Exception {
        int dbuf[] = {CMD_WREN};
        transmit(dbuf);
    }

    /**
     * Disables write operations.
     *
     * @throws Exception If any transmission error occurred.
     */
    public void writeDisable() throws Exception {
        int dbuf[] = {CMD_WRDI};
        transmit(dbuf);
    }

    /**
     * Reads Memory Array at the specified address.
     *
     * @param address Read address.
     * @return The content at the specified address.
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException If address is invalid.
     */
    public int read(int address) throws Exception {
        checkAddress(address);
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        int dbuf[] = {CMD_READ, addrHigh, addrLow, 0};
        int temp[] = transmit(dbuf);
        return temp[3];
    }

    /**
     * Writes Memory Array at the specified address.
     *
     * @param address Write address.
     * @param data    Value to write.
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException If address is invalid.
     */
    public void write(int address, int data) throws Exception, IllegalArgumentException {
        checkAddress(address);
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        writeEnable();
        int dbuf[] = {CMD_WRITE, addrHigh, addrLow, data};
        transmit(dbuf);
    }

    /**
     * Writes more than one value to the Memory array.
     * Maximum page size is MAX_PAGE_SIZE (32).
     *
     * @param address Start write address.
     * @param data    Array of values to write.
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException If address is invalid
     *                                  or if array size is out of range (1...MAX_PAGE_SIZE).
     */
    public void writePage(int address, int data[]) throws Exception, IllegalArgumentException {
        checkAddress(address);
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        int length = data.length;
        if ((length == 0) || (length > MAX_PAGE_SIZE)) {
            throw new IllegalArgumentException("Array size out of range (1..." + MAX_PAGE_SIZE + ")");
        }

        int dbuf[] = new int[length + 3];
        dbuf[0] = CMD_WRITE;
        dbuf[1] = addrHigh;
        dbuf[2] = addrLow;
        for (int i = 0; i < length; i++) {
            dbuf[i + 3] = data[i];
        }
        writeEnable();
        transmit(dbuf);
    }

    /**
     * Reads more than one value from the Memory array.
     * Maximum page size is 1024 (whole eeprom).
     *
     * @param address Start read address.
     * @param length  How many values to read.
     * @return result Array of readed values.
     * @throws Exception                If any transmission error occurred.
     * @throws IllegalArgumentException If address is invalid.
     */
    public int[] readPage(int address, int length) throws Exception, IllegalArgumentException {
        checkAddress(address);
        int addrLow = address & 0xff;
        int addrHigh = (address & 0xff00) >> 8;
        int dbuf[] = new int[length + 3];
        dbuf[0] = CMD_READ;
        dbuf[1] = addrHigh;
        dbuf[2] = addrLow;
        int temp[] = transmit(dbuf);
        int rbuf[] = new int[length];
        for (int i = 0; i < length; i++) {
            rbuf[i] = temp[i + 3];
        }
        return rbuf;
    }

    /**
     * Writes a new value to the eeprom's status register.
     *
     * @param statusRegister New value for the register.
     * @throws Exception If any transmission error occurred.
     */
    public void writeStatusReg(int statusRegister) throws Exception {
        writeEnable();
        int dbuf[] = {CMD_WRSR, statusRegister};
        transmit(dbuf);
    }

    /**
     * Reads the eeprom's status register.
     * Bit 7...4 are always set to 1.
     *
     * @return value of the status register.
     * @throws Exception If any transmission error occurred.
     */
    public int readStatusReg() throws Exception {
        int dbuf[] = {CMD_RDSR, 0};
        int temp[] = transmit(dbuf);
        return temp[1];
    }


    /**
     * Fills the whole eeprom with the specified pattern.
     *
     * @param pattern Pattern that fills the eeprom.
     * @throws Exception If any transmission error occurred.
     */
    public void fillEeprom(int pattern) throws Exception {
        int pat[] = new int[MAX_PAGE_SIZE];
        for (int i = 0; i < MAX_PAGE_SIZE; i++) {
            pat[i] = pattern;
        }

        for (int i = 0, pages = EEPROM_SIZE / MAX_PAGE_SIZE; i < pages; i++) {
            writePage(i * MAX_PAGE_SIZE, pat);
        }
    }

    private void checkAddress(int address) throws IllegalArgumentException {
		if( (address <0) || (address > EEPROM_SIZE -1)) {
			throw new IllegalArgumentException("Address out of range (1..."+ (EEPROM_SIZE -1) +")");
		}
	}
}
