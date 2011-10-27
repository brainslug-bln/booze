/* 
 * This File is part of the iowj-project  
 * $Id$
 * Copyright (C)2008 by Thomas Wagner
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
 * This implementation of the LCD special mode function supports T6963C based
 * LCD display (it works only with the IO-Warrior 56 and uses its T6963 mode).
 *
 * @author Thomas Wagner
 * @since 0.9.6
 */
public class T6963CIow56 extends AbstractLCD {

    /**
     * Name of this special mode function.
     */
    final static String name = "LCD";

    public final static int MODE_ALL_OFF = 0x90;
    public final static int MODE_GRAPHICS_ON = 0x98;
    public final static int MODE_TEXT_ON = 0x94;
    public final static int MODE_CURSOR_ON = 0x92;
    public final static int MODE_CURSOR_BLINK = 0x91;

    public final static int CUR_POINTER_SET = 0x21;
    public final static int OFF_POINTER_SET = 0x22;
    public final static int ADR_POINTER_SET = 0x24;

    public final static int TEXT_HOME_ADR = 0x40;
    public final static int TEXT_AREA_SET = 0x41;
    public final static int GRAPHIC_HOME_ADR = 0x42;
    public final static int GRAPHIC_AREA_SET = 0x43;


    public final static int DATA_AUTO_WRITE = 0xb0;
    public final static int DATA_AUTO_READ = 0xb1;    //not in use
    public final static int AUTO_RESET = 0xb2;

    public final static int TEXT_MSB = 0x00;
    public final static int GRAPHIC_MSB = 0x40;

    public final static int OFFSET_LSB = 0x03;
    public final static int OFFSET_ADR = 0x1800;

    /**
     * Port for Font Select line.
     */
    private final static int CTRL = 4;

    /**
     * Bit for Font Select line.
     */
    private final static int FS = 5;

    private int textArea;
    private int displayMode = MODE_TEXT_ON;

    private final static boolean CLASS_DEBUG = true;
    private final static boolean CLASS_PROFILING = true;

    /**
     * Reference to the underlying IOW device.
     */
    private AbstractIowDevice iow;

    private void init() {
        long start = System.currentTimeMillis();

        iow.setDirection(CTRL, 0x00);        //all ctrl bits output  TODO not correct!!!

        if (cols == 16) {                    //FS=low and 8x8 dots font size
            iow.clearBit(CTRL, FS);
            textArea = 0x10;
        } else if (cols == 21) {            //FS=high and 6x8 dots font size
            iow.setBit(CTRL, FS);
            textArea = 0x16;
        }

        iow.writeIOPorts();                    //output FS initial state

        cmd(0x80);                            //mode set
        //cmd(0x88);
        cmd(0, 0, GRAPHIC_HOME_ADR);            //graphic home address set
        cmd(0x10, 0, GRAPHIC_AREA_SET);        //graphic area set
        cmd(0, TEXT_MSB, TEXT_HOME_ADR);    //text home address
        cmd(textArea, 0, TEXT_AREA_SET);    //text area set
        cmd(OFFSET_LSB, 0, OFF_POINTER_SET);//offset pointer sett

        //only if text enabled
        if ((displayMode & MODE_TEXT_ON) == MODE_TEXT_ON) {
            debug("T6963CIow56.init() cls/home ...");
            cls();
            home();
        }

        //only if graphic enabled
        if ((displayMode & MODE_GRAPHICS_ON) == MODE_GRAPHICS_ON) {
            debug("T6963CIow56.init() gCls ...");
            gCls();
        }

        cmd(0xa7);                            //cursor pattern select
        cmd(displayMode);                      //display mode
        cmd(0, 0, CUR_POINTER_SET);            //pointer set: cursor pointer
        profiling("T6963CIow56.init() took " + (System.currentTimeMillis() - start) + " ms");
    }

    //text based methodes

    private void cls() {
        long start = System.currentTimeMillis();
        cmd(0, TEXT_MSB, ADR_POINTER_SET);    //address pointer set
        cmd(DATA_AUTO_WRITE);                //data auto write set
        for (int i = 0; i < rows; i++) {
            int wbuf[] = new int[textArea + 2];
            wbuf[0] = 5;
            wbuf[1] = textArea;
            for (int j = 0; j < textArea; j++) {
                wbuf[j + 2] = 0;
            }
            iow.writeReport(1, wbuf);
        }
        cmd(AUTO_RESET);                    //auto reset
        profiling("T6963CIow56.cls() took " + (System.currentTimeMillis() - start) + " ms");
    }


    /**
     * @param x starts 0
     * @param y starts 0
     */
    private void gotoxy(int x, int y) {
        cmd(x, y, CUR_POINTER_SET);            //pointer set: cursor pointer
        int addr = x + y * textArea;
        int low = addr & 0xff;
        int high = (addr & 0xff00) >> 8;
        cmd(low, high, ADR_POINTER_SET);    //address set
    }

    public void home() {
        cmd(0, 0, CUR_POINTER_SET);
        cmd(0, TEXT_MSB, ADR_POINTER_SET);
    }

    //to slow !!!

    public void writeCharInc(int c) {
        cmd(c, 0xc0);
    }

    //to slow !!!

    public void writeCharDec(int c) {
        cmd(c, 0xc2);
    }

    //	to slow !!!

    public void writeChar(int c) {
        cmd(c, 0xc4);
    }


    //str max 62 chars long. chek it!!!

    private void writeStringInc(String str) {
        cmd(DATA_AUTO_WRITE);                //data auto write set

        int wbuf[] = new int[str.length() + 2];
        wbuf[0] = 5;
        wbuf[1] = str.length();

        for (int i = 0; i < str.length(); i++) {
            int zeichen = str.charAt(i) - 0x20;
            if (zeichen < 0) {
                zeichen = str.charAt(i) + 0x80;
            }
            wbuf[i + 2] = zeichen;
            //wbuf[i+2] = str.charAt(i) - 0x20;
        }
        iow.writeReport(1, wbuf);
        cmd(AUTO_RESET);            //auto mode reset
    }

    //str max 62 chars long. chek it!!!
    //row anc col start with 0!

    private void writeStringLine(int row, int col, boolean clear, String str) {
        gotoxy(col, row);

        int availableLineLength = cols - col;
        int len = str.length();
        if (len > availableLineLength) {
            len = availableLineLength;
        }

        if (clear) {
            char cStr[] = new char[availableLineLength];
            for (int i = 0; i < availableLineLength; i++) {
                cStr[i] = ' ';
            }
            for (int i = 0; i < len; i++) {
                cStr[i] = str.charAt(i);
            }
            writeStringInc(new String(cStr));
        } else {
            writeStringInc(str.substring(0, len));
        }
    }

    public void writeScreen(char[] screen) {
        cmd(0, TEXT_MSB, ADR_POINTER_SET);    //address pointer set
        cmd(DATA_AUTO_WRITE);            //data auto write set
        for (int i = 0; i < 256; i++) {
            writeData(screen[i] - 0x20); //optimize IOW56
        }
        cmd(AUTO_RESET);            //auto mode reset
    }

    public char[] dumpText() {
        char[] dump = new char[256];
        cmd(0, TEXT_MSB, ADR_POINTER_SET);    //address pointer set
        cmd(0xb1);            //data auto read set
        for (int i = 0; i < 256; i++) {
            //read
        }
        cmd(AUTO_RESET);            //auto mode reset
        return dump;
    }

    //graphic based methods


    public void gCls() {
        long start = System.currentTimeMillis();
        cmd(0, GRAPHIC_MSB, ADR_POINTER_SET);    //address pointer set
        cmd(DATA_AUTO_WRITE);            //data auto write set
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 16; j++) {
                writeData(0); //optimize iow56
            }
        }
        cmd(AUTO_RESET);        //auto reset
        profiling("T6963C.gCls() took " + (System.currentTimeMillis() - start) + " ms");
    }

    public void gHome() {
        cmd(0, GRAPHIC_MSB, ADR_POINTER_SET);
    }

    public void pset(int x, int y) {
        int LCD_GRAPHIC_HOME = 0x0000;
        int LCD_GRAPHIC_AREA = 0x10;

        int adr = (y * LCD_GRAPHIC_AREA) + (x / 8) + 0x0000;

        cmd(adr & 0xff, adr >> 8, ADR_POINTER_SET);
        cmd(0xf8 | 7 - (x & 0x7));
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
        int wbuf[] = {4, 1, 0x0E};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW56ID) {
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

        if (iow != null) {
            init();
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility()
      * @since 0.9.4
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        //T6963 mode works with IO-Warrior 56 only
        if (id != AbstractIowDevice.IOW56ID) return "T6963 mode works with IOW56 only";
        return null;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */

    public String getName() {
        return name;
    }

//------------------ TODO --------------------------------------------------

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#setEntryMode(boolean, boolean)
      */

    public void setEntryMode(boolean moveForward, boolean shiftDisp) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#clearLCD()
      */

    public void clearLCD() {
        cls();
    }

    /* (non-Javadoc)
    * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorleft()
    */

    public void setCursorleft() {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorRight()
 */

    public void setCursorRight() {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorHome()
 */

    public void setCursorHome() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursor(int, int)
      */

    public void setCursor(int row, int col) throws IllegalArgumentException {
        gotoxy(row - 1, col - 1);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#check()
       */

    public void check() {
        char c = 'a';
        for (int i = 0; i < rows; i++) {
            StringBuffer sb = new StringBuffer(cols);
            for (int j = 0; j < cols; j++) {
                sb.append(c);
            }
            writeLine(i + 1, true, sb.toString());
            c++;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#writeLine(int, boolean, java.lang.String)
      */

    public void writeLine(int row, boolean clear, String str)
            throws IllegalArgumentException {
        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        //row and col start with 1!
        writeStringLine(row - 1, 0, clear, str);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#writeLine(int, int, boolean, java.lang.String)
      */

    public void writeLine(int row, int col, boolean clear, String str)
            throws IllegalArgumentException {
        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");

        if (col > cols)
            throw new IllegalArgumentException("Only column 1..." + cols + " allowed!");

        //row and col start with 1!
        writeStringLine(row - 1, col - 1, clear, str);
    }

/* (non-Javadoc)
* @see de.wagner_ibw.iow.lcd.AbstractLCD#setDisplayControl(boolean, boolean, boolean)
*/

    public void setDisplayControl(
            boolean dispOn,
            boolean cursorOn,
            boolean charBlinking) {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#writeString(java.lang.String)
 */

    public void writeString(String str) {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#setDispOff()
 */

    public void setDispOff() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#setSpecialChar(int, int[])
      */

    public void setSpecialChar(int code, int[] pattern)
            throws IllegalArgumentException {

        if (code < 0 || code > 8)
            throw new IllegalArgumentException("Only code 0...8 allowed!");

        if (pattern.length != 8)
            throw new IllegalArgumentException("You must specified 8 lines!");

        code = code + 0x80;

        int addr = OFFSET_ADR + (code * 8);
        int low = addr & 0xff;
        int high = (addr & 0xff00) >> 8;
        cmd(low, high, ADR_POINTER_SET);
        cmd(DATA_AUTO_WRITE);                //data auto write set

        int wbuf[] = new int[10];
        wbuf[0] = 5;
        wbuf[1] = 8;
        for (int i = 0; i < 8; i++) {
            wbuf[i + 2] = pattern[i];
        }
        iow.writeReport(1, wbuf);

        cmd(AUTO_RESET);
    }

/* (non-Javadoc)
* @see de.wagner_ibw.iow.lcd.AbstractLCD#setShiftControl(boolean, boolean)
*/

    public void setShiftControl(boolean shiftDisp, boolean shiftDir) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.AbstractLCD#moveSprite(int, java.lang.String[], int)
      */

    public void moveSprite(int row, String[] sprites, int wait)
            throws IllegalArgumentException {
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
* @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorDispOn()
*/

    public void setCursorDispOn() {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorOff()
 */

    public void setCursorOff() {
        // TODO Auto-generated method stub

    }
/* (non-Javadoc)
 * @see de.wagner_ibw.iow.lcd.AbstractLCD#setCursorOn()
 */

    public void setCursorOn() {
        // TODO Auto-generated method stub

    }


    //------------------------------------------------------------------


    private void bitSet(int pix) {
        cmd(0xf8 | pix);
    }

    private void cmd(int d1, int d2, int cmd) {
        writeData2(d1, d2);
        writeCmd(cmd);
    }

    private void cmd(int d1, int cmd) {
        writeData(d1);
        writeCmd(cmd);
    }

    private void cmd(int cmd) {
        writeCmd(cmd);
    }

    protected long writeCmd(int cmd) {
        int wbuf[] = {5, 0x81, cmd};
        return iow.writeReport(1, wbuf);
    }

    protected long writeData(int data) {
        int wbuf[] = {5, 1, data};
        return iow.writeReport(1, wbuf);
    }

    protected long writeData2(int data1, int data2) {
        int wbuf[] = {5, 2, data1, data2};
        return iow.writeReport(1, wbuf);
    }

    private void debug(String msg) {
        if (CLASS_DEBUG) {
            System.out.println(msg);
        }
    }

    private void profiling(String msg) {
        if (CLASS_PROFILING) {
            System.out.println(msg);
        }
	}
}
