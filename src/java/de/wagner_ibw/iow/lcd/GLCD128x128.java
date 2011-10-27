/* 
 * This File is part of the iowj-project  
 * $Id: GLCD128x128.java,v 1.1 2006/03/12 19:29:57 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.lcd;

import de.wagner_ibw.iow.AbstractIowDevice;

/**
 * This class represents a generic 128 x 128 pixel graphic display.
 * <br>proved type(s):
 * <ul>
 * <li>TOSHIBA TLX-1391 (Pollin)</li>
 * </ul>
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class GLCD128x128 extends AbstractLCD {

    private T6963CPio lcd;

    private final int rows = 16;
    private final int cols = 16;

    public GLCD128x128(AbstractIowDevice dev, boolean text) {
        int mode = T6963CPio.MODE_GRAPHICS_ON;
        if (text)
            mode = mode | T6963CPio.MODE_TEXT_ON;
        lcd = new T6963CPio(dev, mode, T6963CPio.FONT_8X8);
    }


    public void rechteck(int xa, int ya, int xb, int yb) {
        for (int x = xa; x <= xb; x++) {
            lcd.pset(x, ya);
            lcd.pset(x, yb);
        }
        for (int y = ya; y <= yb; y++) {
            lcd.pset(xa, y);
            lcd.pset(xb, y);
        }
    }


    public void linie(int xa, int ya, int xb, int yb) {
        int y;
        float fx;
        float fy;
        float n;
        fy = yb - ya;
        fx = xb - xa;
        n = fy / fx;
        for (int x = xa; x <= xb; x++) {
            y = (int) n * x;
            y = y + ya;
            lcd.pset(x, y);
        }
    }


    public void circle(int xc, int yc, int g) {
        double n;
        for (n = 0; n < 6.29; n = n + 0.02) {
            double x = Math.sin(n);
            x = x * g;
            int xb = (int) x + xc;
            double y = Math.cos(n);
            y = y * g;
            int yb = (int) y + yc;
            lcd.pset(xb, yb);
        }
    }


    public void cls() {
        lcd.cls();
    }

    public void home() {
        lcd.home();
    }

    public void gCls() {
        lcd.gCls();
    }

    public void gHome() {
        lcd.gHome();
    }

    public void pset(int x, int y) {
        lcd.pset(x, y);
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#getRows()
      */

    public int getRows() {
        return rows;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#getCols()
      */

    public int getCols() {
        return cols;
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

    public long setDDRAMAddr(int address) {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCGRAMAddr(int)
      */

    public long setCGRAMAddr(int address) {
        // TODO Auto-generated method stub
        return 0;
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
