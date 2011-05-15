/*
 * This File is part of the iowj-project   
 * $Id: DoubleHD44780Ahw.java,v 1.4 2008/05/10 18:07:04 Thomas Wagner Exp $
 * Copyright (C)2005-2006 by Thomas Wagner
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
 * This implementation of the LCD special mode function supports double controller
 * LCD display (additional hardware (74HCT02) and one IO-pin is needed).
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public class DoubleHD44780Ahw extends HD44780 {

    private int currentEnable = 0;
    private int enablePort = 0;
    private int enableBit = 0;

    public DoubleHD44780Ahw(int enablePort, int enableBit) {
        this.enablePort = enablePort;
        this.enableBit = enableBit;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#clearLCD()
      */

    public void clearLCD() {
        writeDoubleCmd(1);
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

    public void setCursorHome() { //???????????????????????????
        writeCmd(2, NO_CTRL);
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

        writeDoubleCmd(cmd);
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

        writeDoubleCmd(cmd);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setShiftControl(boolean, boolean)
      */

    public void setShiftControl(boolean shiftDisp, boolean shiftDir) {
        int cmd = 0x10;
        if (shiftDir) cmd = cmd | 4;
        if (shiftDisp) cmd = cmd | 8;

        writeDoubleCmd(cmd);
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

        if (row < 3) {
            enableE1();
        } else {
            enableE2();
        }

        if (clear) {
            setDDRAMAddr(lineStartAdr[row - 1] + col - 1, NO_CTRL);
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

        setDDRAMAddr(lineStartAdr[row - 1] + col - 1, NO_CTRL);
        writeString(str.substring(0, len));
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setCursor(int, int)
      */

    public void setCursor(int row, int col) throws IllegalArgumentException {
        //*deb*/System.out.println("DoubleLCD.setCursor("+row+","+col+")");
        int address = 0;

        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        if (row < 3) {
            enableE1();
        } else {
            enableE2();
        }


        // Calculate DDRAM memory address
        address = lineStartAdr[row - 1] + col - 1;

        //Set DDRAM address and move cursor to new position
        setDDRAMAddr(address, NO_CTRL);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setCursorDispOn()
      */

    public void setCursorDispOn() {
        int cmd = 0x8;
        cmd = cmd | 4;
        if (charBlinking) cmd = cmd | 1;
        if (cursorOn) cmd = cmd | 2;

        writeDoubleCmd(cmd);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setDispOff()
      */

    public void setDispOff() {
        int cmd = 0x8;
        cmd = cmd | 0xfb;
        if (charBlinking) cmd = cmd | 1;
        if (cursorOn) cmd = cmd | 2;

        writeDoubleCmd(cmd);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setCursorOn()
      */

    public void setCursorOn() {
        int cmd = 0x8;
        cmd = cmd | 2;
        if (charBlinking) cmd = cmd | 1;
        if (dispOn) cmd = cmd | 4;

        writeDoubleCmd(cmd);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setCursorOff()
      */

    public void setCursorOff() {
        int cmd = 0x8;
        cmd = cmd | 0xfd;
        if (charBlinking) cmd = cmd | 1;
        if (dispOn) cmd = cmd | 4;

        writeDoubleCmd(cmd);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#setSpecialChar(int, int[])
      */

    public void setSpecialChar(int code, int[] pattern) throws IllegalArgumentException {
        if (code < 0 || code > 8)
            throw new IllegalArgumentException("Only code 0...8 allowed!");

        if (pattern.length != 8)
            throw new IllegalArgumentException("You must specified 8 lines!");

        int wbuf1[] = {5, 0x86, pattern[0], pattern[1], pattern[2], pattern[3], pattern[4], pattern[5]};
        int wbuf2[] = {5, 0x82, pattern[6], pattern[7], 0, 0, 0, 0};

        setCGRAMAddr(code * 8, NO_CTRL);
        iow.writeReport(1, wbuf1);
        iow.writeReport(1, wbuf2);

        toggleEnable();
        setCGRAMAddr(code * 8, NO_CTRL);
        iow.writeReport(1, wbuf1);
        iow.writeReport(1, wbuf2);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#moveSprite(int, java.lang.String[], int)
      */

    public void moveSprite(int row, String[] sprites, int wait) throws IllegalArgumentException {
        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        if (row < 3) {
            enableE1();
        } else {
            enableE2();
        }

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
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;

        iow.setDirection(enablePort, 0); //not correct !!!
        if (iow != null) {
            enableE1();
            initLCD(NO_CTRL);
            toggleEnable();
            initLCD(NO_CTRL);
        }
    }

    private void writeDoubleCmd(int cmd) {
        writeCmd(cmd, NO_CTRL);
        toggleEnable();
        writeCmd(cmd, NO_CTRL);
    }

    private void enableE1() {
        if (currentEnable != 1) {
            //*deb*/System.out.println("Enable E1...");
            iow.setBit(enablePort, enableBit);
            iow.writeIOPorts();
            currentEnable = 1;
        }
    }

    private void enableE2() {
        if (currentEnable != 2) {
            //*deb*/System.out.println("Enable E2...");
            iow.clearBit(enablePort, enableBit);
            iow.writeIOPorts();
            currentEnable = 2;
        }
    }

    private void toggleEnable() {
        if (currentEnable == 1)
            enableE2();
        else
            enableE1();
    }

}
