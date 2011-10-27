/* 
 * This File is part of the iowj-project  
 * $Id: LCD16x16Pio.java,v 1.1 2006/03/25 21:29:13 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.lcd;

import de.wagner_ibw.iow.AbstractIowDevice;

/**
 * This class represents a generic 16 x 16 display.
 * <br>proved type(s):
 * <ul>
 * <li>TOSHIBA TLX-1391 (Pollin)</li>
 * </ul>
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class LCD16x16Pio extends AbstractLCD {

    private T6963CPio lcd;

    public LCD16x16Pio(AbstractIowDevice dev) {
        lcd = new T6963CPio(dev, T6963CPio.MODE_TEXT_ON, T6963CPio.FONT_8X8);
        rows = 16;
        cols = 16;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#clearLCD()
      */

    public void clearLCD() {
        lcd.cls();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorHome()
      */

    public void setCursorHome() {
        lcd.home();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setEntryMode(boolean, boolean)
      */

    public void setEntryMode(boolean moveForward, boolean shiftDisp) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setDisplayControl(boolean, boolean, boolean)
      */

    public void setDisplayControl(
            boolean dispOn,
            boolean cursorOn,
            boolean charBlinking) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setShiftControl(boolean, boolean)
      */

    public void setShiftControl(boolean shiftDisp, boolean shiftDir) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#writeLine(int, boolean, java.lang.String)
      * TODO clear
      */

    public void writeLine(int row, boolean clear, String str)
            throws IllegalArgumentException {
        lcd.writeStringLine(row - 1, str);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#writeLine(int, int, boolean, java.lang.String)
      * TODO col,clear
      */

    public void writeLine(int row, int col, boolean clear, String str)
            throws IllegalArgumentException {
        lcd.writeStringLine(row - 1, str);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#writeString(java.lang.String)
      */

    public void writeString(String str) {
        lcd.writeStringInc(str);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setDDRAMAddr(int)
      */

    public void setDDRAMAddr(int address) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCGRAMAddr(int)
      */

    public void setCGRAMAddr(int address) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursor(int, int)
      */

    public void setCursor(int row, int col) throws IllegalArgumentException {
        lcd.gotoxy(row - 1, col - 1);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorDispOn()
      */

    public void setCursorDispOn() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setDispOff()
      */

    public void setDispOff() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorOn()
      */

    public void setCursorOn() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorOff()
      */

    public void setCursorOff() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorleft()
      */

    public void setCursorleft() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorRight()
      */

    public void setCursorRight() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#check()
      */

    public void check() {
        char c = 'a';
        for (int i = 0; i < rows; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < cols; j++) {
                sb.append(c);
            }
            writeLine(i + 1, true, sb.toString());
            c++;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setSpecialChar(int, int[])
      */

    public void setSpecialChar(int code, int[] pattern)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#moveSprite(int, java.lang.String[], int)
      */

    public void moveSprite(int row, String[] sprites, int wait)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility(int, int, int)
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits(int)
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */

    public String getName() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public int[] getReportIds() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public boolean matchReportId(int reportId) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.AbstractIowDevice)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        throw new UnsupportedOperationException();
    }

}
