/*
 * This File is part of the iowj-project   
 * $Id: HD44780.java,v 1.4 2008/05/10 18:07:04 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.lcd;

import de.wagner_ibw.iow.AbstractIowDevice;
import de.wagner_ibw.iow.SpecialModeFunction;

/**
 * This is the implementation of the LCD special mode function.
 * It supports only single controller LCD display.
 *
 * @author Thomas Wagner
 */
public class HD44780 extends AbstractLCD {

    /**
     * Constant for the first controller.
     */
    final protected static int FIRST_CTRL = 0;

    /**
     * Constant for the second controller.
     */
    final protected static int SECOND_CTRL = 0x40;

    /**
     * Constant for redundant information of controller.
     */
    final protected static int NO_CTRL = 0;

    /**
     * Name of this special mode function.
     */
    final static String name = "LCD";

    protected boolean dispOn = true;
    protected boolean cursorOn = false;
    protected boolean charBlinking = false;

    /**
     * OK
     * Writes the given command byte to the LCD.
     *
     * @param cmd  command to write
     * @param ctrl LCD's controller. Always FIRST_CTRL for single controller displays,
     *             FIRTS_CTRL for the first and SECOND_CTRL for the second controller in double controller displays.
     * @return Number of written bytes (8 expected for a successful operation).
     */
    protected long writeCmd(int cmd, int ctrl) {
        int wbuf[] = {5, 1 | ctrl, cmd};
        return iow.writeReport(1, wbuf);
    }

    /**
     * Writes the given data byte to the LCD.
     *
     * @param data data byte to write
     * @param ctrl LCD's controller. Always FIRST_CTRL for single controller displays,
     *             FIRST_CTRL for the first and SECOND_CTRL for the second controller in double controller displays.
     * @return Number of written bytes (8 expected for a successful operation).
     */
    protected long writeData(int data, int ctrl) {
        int wbuf[] = {5, 0x81 | ctrl, data};
        return iow.writeReport(1, wbuf);
    }

    /**
     * OK
     * Initialize the display and switch in 1 or 2 line mode depending on physicalRows.
     *
     * @return
     */
    protected long initLCD(int ctrl) {
        int wbuf[] = {5, 3 | ctrl, 0x34, 1, 0xf};
        if (physicalRows > 1) {
            wbuf[2] = wbuf[2] | 0x08;
        }
        return iow.writeReport(1, wbuf);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#clearLCD()
      */

    public void clearLCD() {
        writeCmd(1, 0);
        try {
            Thread.sleep(2);
        }
        catch (Exception e) {
            //nothing to do
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setCursorHome()
      */

    public void setCursorHome() {
        writeCmd(2, FIRST_CTRL);
        try {
            Thread.sleep(2);
        }
        catch (Exception e) {
            //nothing to do
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setEntryMode(boolean, boolean)
      */

    public void setEntryMode(boolean moveForward, boolean shiftDisp) {
        int cmd = 0x4;
        if (moveForward) cmd = cmd | 2;
        if (shiftDisp) cmd = cmd | 1;
        writeCmd(cmd, FIRST_CTRL);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setDisplayControl(boolean, boolean, boolean)
      */

    public void setDisplayControl(boolean dispOn, boolean cursorOn, boolean charBlinking) {
        this.dispOn = dispOn;
        this.cursorOn = cursorOn;
        this.charBlinking = charBlinking;
        int cmd = 0x8;
        if (charBlinking) cmd = cmd | 1;
        if (cursorOn) cmd = cmd | 2;
        if (dispOn) cmd = cmd | 4;
        writeCmd(cmd, FIRST_CTRL);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setShiftControl(boolean, boolean)
      */

    public void setShiftControl(boolean shiftDisp, boolean shiftDir) {
        int cmd = 0x10;
        if (shiftDir) cmd = cmd | 4;
        if (shiftDisp) cmd = cmd | 8;
        writeCmd(cmd, FIRST_CTRL);
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#writeLine(int, boolean, java.lang.String)
      */

    public synchronized void writeLine(int row, boolean clear, String str)
            throws IllegalArgumentException {

        writeLine(row, 1, clear, str);
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#writeLine(int, int, boolean, java.lang.String)
      */

    public synchronized void writeLine(int row, int col, boolean clear, String str)
            throws IllegalArgumentException {
        StringBuffer sb = new StringBuffer();

        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        if (col > cols)
            throw new IllegalArgumentException("Only column 1..." + cols + " allowed!");

        if (clear) {
            setDDRAMAddr(lineStartAdr[row - 1] + col - 1, FIRST_CTRL);
            for (int i = 0 + col - 1; i < cols; i++) {
                sb.append(' ');
            }
            writeString(sb.toString());
        }

        int availableLineLength = cols - (col - 1);
        int len = str.length();
        if (len > availableLineLength) {
            len = availableLineLength;
        }
        setDDRAMAddr(lineStartAdr[row - 1] + col - 1, FIRST_CTRL);
        writeString(str.substring(0, len));
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#writeString(java.lang.String)
      */

    public synchronized void writeString(String str) {
        writeString(str, FIRST_CTRL);
    }

    public synchronized void writeString(String str, int ctrl) {
        int payload = iow.getSmfReportLength() - 2;
        int len = str.length();
        int left = len;
        int current = 0;

        if (len == 0) {
            return;
        }

        while (left > 0) {
            int[] wbuf = new int[iow.getSmfReportLength()];
            wbuf[0] = 5;
            wbuf[1] = 0x80 | ctrl;
            int count = payload;

            if (left >= payload) {
                left = left - payload;
            } else {
                count = left;
                left = 0;
            }

            wbuf[1] = wbuf[1] | count;
            for (int i = 0; i < count; i++) {
                wbuf[i + 2] = str.charAt(current);
                current++;
            }
            iow.writeReport(1, wbuf);
        }
    }

    /**
     * Set DDRAM address
     * Parameters:
     * Address - DDRAM address
     */
    public long setDDRAMAddr(int address, int ctrl) {
        int cmd = 0x80 | address;
        return writeCmd(cmd, ctrl);
    }

    /**
     * Set DDRAM address
     * Parameters:
     * Address - DDRAM address
     */
    public long setCGRAMAddr(int address, int ctrl) {
        int cmd = 0x40 | address;
        return writeCmd(cmd, ctrl);
    }

    /**
     * OK
     * Moves cursor to new Position.
     *
     * @param row display line (1...4)
     * @param col column in row (1...40)
     */
    public void setCursor(int row, int col) throws IllegalArgumentException {
        int address = 0;

        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        // Calculate DDRAM memory address
        address = lineStartAdr[row - 1] + col - 1;

        //Set DDRAM address and move cursor to new position
        setDDRAMAddr(address, FIRST_CTRL);
    }

    /**
     * Convenient method: Turn LCD display on.
     */
    public void setCursorDispOn() {
        int cmd = 0x8;
        cmd = cmd | 4;
        if (charBlinking) cmd = cmd | 1;
        if (cursorOn) cmd = cmd | 2;
        writeCmd(cmd, FIRST_CTRL);
    }

    /**
     * OK
     * Convenient method: Turn LCD display off.
     *
     * @return Number of written bytes (8 expected for a successful operation).
     */
    public void setDispOff() {
        int cmd = 0x8;
        cmd = cmd | 0xfb;
        if (charBlinking) cmd = cmd | 1;
        if (cursorOn) cmd = cmd | 2;
        writeCmd(cmd, FIRST_CTRL);
    }

    /**
     * OK
     * Convenient method: Turn LCD cursor on.
     *
     * @return Number of written bytes (8 expected for a successful operation).
     */
    public void setCursorOn() {
        int cmd = 0x8;
        cmd = cmd | 2;
        if (charBlinking) cmd = cmd | 1;
        if (dispOn) cmd = cmd | 4;
        writeCmd(cmd, FIRST_CTRL);
    }

    /**
     * OK
     * Convenient method: Turn LCD cursor off.
     *
     * @return Number of written bytes (8 expected for a successful operation).
     */
    public void setCursorOff() {
        int cmd = 0x8;
        cmd = cmd | 0xfd;
        if (charBlinking) cmd = cmd | 1;
        if (dispOn) cmd = cmd | 4;
        writeCmd(cmd, FIRST_CTRL);
    }

    /**
     * Move LCD cursor to the left
     *
     * @return Number of written bytes (8 expected for a successful operation).
     */
    public void setCursorleft() {
        writeCmd(0x10, FIRST_CTRL);
    }

    /**
     * Move LCD cursor to the right
     *
     * @return Number of written bytes (8 expected for a successful operation).
     */
    public void setCursorRight() {
        writeCmd(0x14, FIRST_CTRL);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#check()
      */

    public void check() {
        for (int i = 0; i < rows; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < cols; j++) {
                sb.append(i + 1);
            }
            writeLine(i + 1, true, sb.toString());
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setSpecialChar(int, int[])
      */

    public void setSpecialChar(int code, int[] pattern) throws IllegalArgumentException {
        if (code < 0 || code > 8)
            throw new IllegalArgumentException("Only code 0...8 allowed!");

        if (pattern.length != 8)
            throw new IllegalArgumentException("You must specified 8 lines!");

        setCGRAMAddr(code * 8, FIRST_CTRL);
        int wbuf1[] = {5, 0x86, pattern[0], pattern[1], pattern[2], pattern[3], pattern[4], pattern[5]};
        iow.writeReport(1, wbuf1);
        int wbuf2[] = {5, 0x82, pattern[6], pattern[7], 0, 0, 0, 0};
        iow.writeReport(1, wbuf2);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#moveSprite(int, java.lang.String[], int)
      */

    public void moveSprite(int row, String[] sprites, int wait) throws IllegalArgumentException {
        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        int countSteps = sprites.length;

        writeLine(row, true, "");

        int step = 0;
        String space = "";
        for (int i = 0; i < cols + 1; i++) {
            if (i != 0) space = " " + space;
            writeLine(row, false, space + sprites[step]);
            step++;
            if (step == countSteps) step = 0;

            try {
                Thread.sleep(wait);
            }
            catch (Exception e) {
                //nothing to do
            }
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        int wbuf[] = {4, 0};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        int wbuf[] = {4, 1};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW24ID) {
            int masks[] = {0xf0, 0xff};
            return masks;
        } else if (deviceIdentifier == AbstractIowDevice.IOW40ID) {
            int masks[] = {0x3c, 0xff};
            return masks;
        } else if (deviceIdentifier == AbstractIowDevice.IOW56ID) {
            int masks[] = {0x00, 0x00, 0x00, 0xff, 0x01f};
            return masks;
        }
        int masks[] = {};
        return masks;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public int[] getReportIds() {
        int ids[] = {0x06};
        return ids;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_LCD_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public boolean matchReportId(int reportId) {
        return false; //not interested in reports
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        // nothing to do
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
        if (iow != null)
            initLCD(FIRST_CTRL);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility()
      * @since 0.9.4
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        //not at the same time with SPI
        if ((specialModes & SpecialModeFunction.SMF_SPI_ID) == SpecialModeFunction.SMF_SPI_ID)
            return "SPI is already activated";
        //not at the same time with LED
        if ((specialModes & SpecialModeFunction.SMF_LCD_ID) == SpecialModeFunction.SMF_LCD_ID)
            return "LCD is already activated";
        //not at the same time with SMX16
        if ((specialModes & SpecialModeFunction.SMF_SMX16_ID) == SpecialModeFunction.SMF_SMX16_ID)
            return "SMX16 is already activated";
        return null;
	}

	/* (non-Javadoc)
	 * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
	 */
	public String getName() {
		return name;
	}

}
