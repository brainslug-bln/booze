/*
 * This File is part of the iowj-project   
 * $Id: AbstractLCD.java,v 1.1 2006/03/25 21:29:13 Thomas Wagner Exp $
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
 * This is the interface for using alpha numerical LCD.
 *
 * @author Thomas Wagner
 */
public abstract class AbstractLCD implements SpecialModeFunction {

    protected AbstractIowDevice iow;
    protected int physicalRows;
    protected int rows;
    protected int cols;
    protected int[] lineStartAdr;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Clears entire dislplay and sets DDRAM address 0 in address counter.
     */
    public abstract void clearLCD();

    /**
     * Sets DDRAM address 0 in address counter. Also returns display from being
     * shiftet to original position. DDRAM contents remain unchanged.
     */
    public abstract void setCursorHome();

    /**
     * Sets cursor move direction and specifies display shift.
     * These operations are performed during data write and read.
     *
     * @param moveForward true: increment, false: decrement
     * @param shiftDisp   true: display is shifted, false: display is not shifted
     */
    public abstract void setEntryMode(boolean moveForward, boolean shiftDisp);

    /**
     * Sets ON/OFF of all display (dispOn), cursor ON/OFF (cursorOn), and blink
     * of cursor position character (cursorBlink).
     *
     * @param dispOn       true: the display is on, false: display is off
     * @param cursorOn     true: cursor is displayed, false: cursor is not displayed
     * @param charBlinking true: the character indicated by the cursor blinks,
     *                     false: blinks not
     */
    public abstract void setDisplayControl(boolean dispOn, boolean cursorOn, boolean charBlinking);

    /**
     * Moves cursor and shifts display whitout changing DDRAM contents.
     *
     * @param shiftDisp true: display shift, false: cursor move;
     * @param shiftDir  true: shift to the right, false: shift to the left
     */
    public abstract void setShiftControl(boolean shiftDisp, boolean shiftDir);

    /**
     * Write the given String in row specified by parm row.
     * If parm clear is true the row will be cleared before output.
     * All characters exceeding cols will be truncated.
     *
     * @param row
     * @param clear
     * @param str
     */
    public abstract void writeLine(int row, boolean clear, String str)
            throws IllegalArgumentException;


    /**
     * Write the given String in the specified row and column.
     * If parm clear is true the row will be cleared before output.
     * All characters exceeding cols will be truncated.
     *
     * @param row
     * @param clear
     * @param str
     */
    public abstract void writeLine(int row, int col, boolean clear, String str)
            throws IllegalArgumentException;

    /**
     * Write the given string to LCD.
     *
     * @param str String to write
     */
    public abstract void writeString(String str);

    /** ?????
     * Set DDRAM address
     * Parameters:
     * Address - DDRAM address
     +/
     public long setDDRAMAddr(int address);
     */

    /** ?????
     * Set DDRAM address
     * Parameters:
     * Address - DDRAM address
     +/
     public long setCGRAMAddr(int address);
     */

    /**
     * Moves cursor to new Position.
     *
     * @param row display line (1...4)
     * @param col column in row (1...40)
     */
    public abstract void setCursor(int row, int col) throws IllegalArgumentException;

    /**
     * Convenient method: Turn LCD display on.
     */
    public abstract void setCursorDispOn();

    /**
     * Convenient method: Turn LCD display off.
     */
    public abstract void setDispOff();

    /**
     * Convenient method: Turn LCD cursor on.
     */
    public abstract void setCursorOn();

    /**
     * Convenient method: Turn LCD cursor off.
     */
    public abstract void setCursorOff();

    /**
     * Move LCD cursor to the left
     */
    public abstract void setCursorleft();

    /**
     * Move LCD cursor to the right
     */
    public abstract void setCursorRight();

    /**
     * Shows a test pattern on display.
     */
    public abstract void check();

    /**
     * Sets a special char in CGRAM.
     *
     * @param code
     * @param pattern
     * @throws IllegalArgumentException
     */
    public abstract void setSpecialChar(int code, int[] pattern) throws IllegalArgumentException;

    /**
     * @param row
     * @param sprites
     * @param wait
     * @throws IllegalArgumentException
     */
    public abstract void moveSprite(int row, String[] sprites, int wait) throws IllegalArgumentException;

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility(int, int, int)
      */

    public abstract String checkCompatibility(int id, int rev, int specialModes);

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public abstract int[] getDisableReport();

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public abstract int[] getEnableReport();

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits(int)
      */

    public abstract int[] getIowSpecialBits(int deviceIdentifier);

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */

    public abstract String getName();

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public abstract int[] getReportIds();

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public abstract int getSpecialModeFuncionId();

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public abstract boolean matchReportId(int reportId);

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public abstract void reportReceived(int[] readBuffer);

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.AbstractIowDevice)
      */
	public abstract void setIowDevice(AbstractIowDevice iow);
}
