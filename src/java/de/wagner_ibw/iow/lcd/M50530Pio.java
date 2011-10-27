/* 
 * This File is part of the iowj-project  
 * $Id: M50530Pio.java,v 1.1 2006/03/25 21:29:13 Thomas Wagner Exp $
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
import de.wagner_ibw.iow.IowFactory;

/**
 * This is the implementation of the LCD special mode function
 * for the M50530 display controller, using plain input output.
 *
 * @author Thomas Wagner
 */
public class M50530Pio extends AbstractLCD {

    /**
     * Control port definitions.
     */
    private final static int CTRL = 0;
    private final static int DATA = 1;

    /**
     * Control pin definitions.
     */
    private final static int RW = 4;
    private final static int EX = 5;
    private final static int OC1 = 6;
    private final static int OC2 = 7;

    /**
     * Set function mode instruction (SF).
     * 8 lines I/O, 5x8 font
     */
    private final static int SF_INSTRUCTION = 0xfa;

    /**
     * Set entry mode instruction (SE).
     * Cursor increment, display start address constant
     */
    private final static int SE_INSTRUCTION = 0x50;

    /**
     * Set display mode instruction (SD).
     * Display on, cursor on and underline
     */
    private final static int SD_INSTRUCTION = 0x3e;

    /**
     * The display/address home instruction (MH)
     */
    private final static int MH_INSTRUCTION = 0x03;

    /**
     * The clear display/address home instruction (CH)
     */
    private final static int CH_INSTRUCTION = 0x01;

    /**
     * Reference to the underlying IOW device.
     */
    private AbstractIowDevice iow;

    /**
     * Display specific definitions
     */
    protected int rows = 8;
    protected int cols = 24;
    protected int[] lineStartAdr = {0, 48, 96, 144, 24, 72, 120, 168};

    /**
     * for further optimisation
     */
    private final boolean HIGH = true;
    private final boolean LOW = false;
    private boolean cdStatus = HIGH;

    private final static boolean CLASS_DEBUG = true;

    public M50530Pio(AbstractIowDevice iow) {
        this.iow = iow;
        init();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private void init() {

        long start = System.currentTimeMillis();

        iow.setDirection(CTRL, 0x00);    //all ctrl bits output
        iow.setDirection(DATA, 0x00);    //all data bits output
        iow.setPort(CTRL, 0x0);            //all ctrl bits low
        iow.setPort(DATA, 0x0);            //all data bits low
        iow.writeIOPorts();                //output initial state

        debug("init lcd...");

/* last
		writeCmdByte(SF_INSTRUCTION);
		writeCmdByte(SE_INSTRUCTION);	//entry mode
		writeCmdByte(SD_INSTRUCTION);	//display mode
		writeCmdByte(CH_INSTRUCTION);	//clear + home
*/

/* old*/

        writeCmdByte(0x01);
        writeCmdByte(0x00);
        writeCmdByte(0x0d);
        writeCmdByte(0x34);
//*/		


/*	fast ok	
        writeCmdByte(0xfa); //8bit, 4 lines, 5x8
        writeCmdByte(0x50); //
        //writeCmdByte(0x38); //
        writeCmdByte(0x3e); // cursor blinkt

        writeCmdByte(0x1); // clear/Home
        */
//		try {
//			Thread.sleep(1000);
//			writeCmdByte(0x18); // cursor inc
//			Thread.sleep(1000);
//			writeCmdByte(0x1c); // cursor dec
//			Thread.sleep(1000);
//			writeCmdByte(0x18);
//		} catch (Exception e) {
//		}


        debug("M50530.init() took " + (System.currentTimeMillis() - start) + " ms");
    }

    public void check() {
        for (int i = 0; i < rows; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < cols; j++) {
                sb.append(i + 1);
            }
            writeLine(i + 1, false, sb.toString());
        }
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

        if (row > rows)
            throw new IllegalArgumentException("Only row 1..." + rows + " allowed!");
        if (col > cols)
            throw new IllegalArgumentException("Only column 1..." + cols + " allowed!");

        if (clear) {
            StringBuffer sb = new StringBuffer();
            writeCursorAddr(lineStartAdr[row - 1] + col - 1);
            for (int i = 0 + col - 1; i < cols; i++) {
                sb.append(' ');
            }
            writeString(sb.toString());
        }

        int len = str.length();
        if (len > cols)
            len = cols;
        writeCursorAddr(lineStartAdr[row - 1] + col - 1);
        writeString(str.substring(0, len));
    }

    /* OK (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#writeString(java.lang.String)
      */

    public synchronized void writeString(String str) {
        int len = str.length();

        if (len == 0)
            return;

        for (int i = 0; i < len; i++) {
            writeDataByte(str.charAt(i));
        }
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

        //Set cursor address and move cursor to new position
        writeCursorAddr(address);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.LCD#clearLCD()
      */

    public void clearLCD() { /*OK*/
        writeCmdByte(0x1);
    }

    public void writeChar(int c) {
        writeDataByte(c);
    }

    public void writeStringInc(String str) {
        char[] ca = str.toCharArray();
        for (int i = 0; i < ca.length; i++) {
//			cmd(ca[i]-0x20,0xc0);
        }
    }

    public void writeStringLine(int line, String str) {
//		gotoxy(0, line);
        writeStringInc(str);
    }

    // OK write cursor address

    public void writeCursorAddr(int addr) {
        iow.setBit(CTRL, OC1);
        iow.setBit(CTRL, OC2);
        iow.setPort(DATA, addr);
        write();
        iow.setBit(CTRL, EX);
        write();
        iow.clearBit(CTRL, EX);
        write();
        iow.clearBit(CTRL, OC1);
        iow.clearBit(CTRL, OC2);
        write();
    }

    /* OK output characters to display*/

    private void writeDataByte(int data) {
        iow.setBit(CTRL, OC2);
        iow.setPort(DATA, data);
        write();
        iow.setBit(CTRL, EX);
        write();
        iow.clearBit(CTRL, EX);
        write();
        iow.clearBit(CTRL, OC2);
        write();
    }

    /* OK output control data to display*/

    private void writeCmdByte(int data) {
        iow.setPort(DATA, data);
        write();
        iow.setBit(CTRL, EX);
        write();
        iow.clearBit(CTRL, EX);
        write();
    }


    private void write() {
        iow.writeIOPorts();
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
        }
    }


    private void writeO() {
        try {
            iow.writeIOPorts();
            while (!iow.isAlive()) {
                System.out.println("waiting vor IOW devices ...");
                Thread.sleep(2000);
                IowFactory.reopenAllDevices(true);
                if (iow.isAlive())
                    init();
            }
        } catch (UnsupportedOperationException u) {
            u.printStackTrace();
        } catch (IllegalStateException i) {
            //i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void debug(String msg) {
        if (CLASS_DEBUG) {
            System.out.println(msg);
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#moveSprite(int, java.lang.String[], int)
      */

    public void moveSprite(int row, String[] sprites, int wait)
            throws IllegalArgumentException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorDispOn()
      */

    public void setCursorDispOn() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorHome()
      */

    public void setCursorHome() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorleft()
      */

    public void setCursorleft() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorOff()
      */

    public void setCursorOff() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorOn()
      */

    public void setCursorOn() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setCursorRight()
      */

    public void setCursorRight() {
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
      * @see de.wagner_ibw.iow.lcd.LCD#setDispOff()
      */

    public void setDispOff() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setEntryMode(boolean, boolean)
      */

    public void setEntryMode(boolean moveForward, boolean shiftDisp) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setShiftControl(boolean, boolean)
      */

    public void setShiftControl(boolean shiftDisp, boolean shiftDir) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.lcd.LCD#setSpecialChar(int, int[])
      */

    public void setSpecialChar(int code, int[] pattern)
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

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
		}
	}

}
