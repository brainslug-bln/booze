/* 
 * This File is part of the iowj-project  
 * $Id: T6963CPio.java,v 1.1 2006/03/25 21:29:13 Thomas Wagner Exp $
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
 * This is the implementation of the LCD special mode function
 * for the T6963C display controller, using plain input output.
 *
 * @author Thomas Wagner
 */
public class T6963CPio {

    public final static int MODE_ALL_OFF = 0x90;
    public final static int MODE_GRAPHICS_ON = 0x98;
    public final static int MODE_TEXT_ON = 0x94;
    public final static int MODE_CURSOR_ON = 0x92;
    public final static int MODE_CURSOR_BLINK = 0x91;

    public final static boolean FONT_6X8 = false;
    public final static boolean FONT_8X8 = true;

    ///*for IOW40 starterkit
    private final static int CTRL = 0;
    private final static int DATA = 1;

    private final static int WR = 0;
    private final static int RD = 1;
    private final static int CE = 2;
    private final static int CD = 3;
    private final static int RES = 4;
    private final static int FS = 5;
    //*/

    /*for IOW56 starterkit
     private final static int CTRL = 4;
     private final static int DATA = 3;

     private final static int WR   = 2;
     private final static int RD   = 3;
     private final static int CE   = 5;
     private final static int CD   = 1;
     private final static int RES  = 4;
     private final static int FS   = 6;
     */

    private final boolean HIGH = true;
    private final boolean LOW = false;

    private int charCols = 16;

    private final static boolean CLASS_DEBUG = true;
    private final static boolean CLASS_PROFILING = true;

    /**
     * Reference to the underlying IOW device.
     */
    private AbstractIowDevice iow;

    private boolean cdStatus = HIGH;


    public T6963CPio(AbstractIowDevice iow, int displayMode, boolean fontSelect) {
        this.iow = iow;
        init(displayMode, fontSelect);
        if (!fontSelect)
            charCols = 21;
    }


    private void init(int displayMode, boolean fontSelect) {
        long start = System.currentTimeMillis();

        iow.setDirection(CTRL, 0x00);    //all ctrl bits output
        iow.setDirection(DATA, 0x00);    //all data bits output
        iow.setPort(CTRL, 0xff);        //all ctrl bits high
        iow.setPort(DATA, 0x0);            //all data bits low
        iow.writeIOPorts();                //output initial state


        System.out.println("reset...");
        reset();

        if (fontSelect)
            iow.clearBit(CTRL, FS);
        write();

        System.out.println("init lcd...");
        cmd(0x80);    //mode set

        cmd(0, 0, 0x42);    //graphic home address set
        cmd(0x10, 0, 0x43);    //graphic area set
        cmd(0, 0x10, 0x40);    //text home address
        cmd(0x10, 0, 0x41);    //text area set

        //only if text enabled
        if ((displayMode & MODE_TEXT_ON) == MODE_TEXT_ON) {
            debug("T6963C.init() cls/home ...");
            cls();
            home();
        }

        //only if graphic enabled
        if ((displayMode & MODE_GRAPHICS_ON) == MODE_GRAPHICS_ON) {
            debug("T6963C.init() gCls ...");
            gCls();
        }

        cmd(0xa7);    //cursor pattern select

        cmd(displayMode);  //display mode

        cmd(0, 0, 0x21);    //pointer set: cursor pointer
        profiling("T6963C.init() took " + (System.currentTimeMillis() - start) + " ms");
    }


    private void reset() {
        iow.clearBit(CTRL, RES);
        write();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        iow.setBit(CTRL, RES);
        write();
    }

    //text based mathodes

    public void cls() {
        long start = System.currentTimeMillis();
        cmd(0, 0x10, 0x24);    //address pointer set
        cmd(0xb0);            //data auto write set
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < charCols; j++) {
                writeByte(LOW, 0);
            }
        }
        cmd(0xb2);        //auto reset
        profiling("T6963C.cls() took " + (System.currentTimeMillis() - start) + " ms");
    }


    /**
     * @param x starts 0
     * @param y starts 0
     */
    public void gotoxy(int x, int y) {
        cmd(x, y, 0x21);                //pointer set: cursor pointer
        cmd(x + y * 16, 0x10, 0x24); //address set
    }

    public void home() {
        cmd(0, 0, 0x21);
        cmd(0, 0x10, 0x24);
    }

    public void writeCharInc(int c) {
        cmd(c, 0xc0);
    }

    public void writeCharDec(int c) {
        cmd(c, 0xc2);
    }

    public void writeChar(int c) {
        cmd(c, 0xc4);
    }

    public void writeStringInc(String str) {
        char[] ca = str.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            cmd(ca[i] - 0x20, 0xc0);
        }
    }

    public void writeStringLine(int line, String str) {
        gotoxy(0, line);
        writeStringInc(str);
    }

    public void writeScreen(char[] screen) {
        cmd(0, 0x10, 0x24);    //address pointer set
        cmd(0xb0);            //data auto write set
        for (int i = 0; i < 256; i++) {
            writeByte(LOW, screen[i] - 0x20);
        }
        cmd(0xb2);            //auto mode reset
    }

    public char[] dumpText() {
        char[] dump = new char[256];
        cmd(0, 0x10, 0x24);    //address pointer set
        cmd(0xb1);            //data auto read set
        for (int i = 0; i < 256; i++) {
            //read
        }

        cmd(0xb2);            //auto mode reset
        return dump;
    }

    //graphic based methods


    public void gCls() {
        long start = System.currentTimeMillis();
        cmd(0, 0, 0x24);    //address pointer set
        cmd(0xb0);            //data auto write set
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 16; j++) {
                writeByte(LOW, 0);
            }
        }
        cmd(0xb2);        //auto reset
        profiling("T6963C.gCls() took " + (System.currentTimeMillis() - start) + " ms");
    }

    public void gHome() {
        cmd(0, 0, 0x24);
    }

//	public void rechteck(int xa,int ya,int xb,int yb){
//		for(int x=xa;x<=xb;x++){
//			pset(x,ya);
//			pset(x,yb);
//		}
//		for(int y=ya;y<=yb;y++){
//			pset(xa,y);
//			pset(xb,y);
//		}
//	}	
//	
//	
//	public void linie (int xa,int ya,int xb,int yb){
//		int y;
//		float fx;
//		float fy;
//		float n;
//		fy = yb - ya;
//		fx = xb - xa;
//		n = fy / fx;
//		for(int x=xa;x<=xb;x++){
//			y = (int) n * x;
//			y = y + ya;
//			pset(x,y);
//		}
//	}
//	

    public void pset(int x, int y) {
        int LCD_GRAPHIC_HOME = 0x0000;
        int LCD_GRAPHIC_AREA = 0x10;

        int adr = (y * LCD_GRAPHIC_AREA) + (x / 8) + 0x0000;

        cmd(adr & 0xff, adr >> 8, 0x24);
        cmd(0xf8 | 7 - (x & 0x7));
    }

//	public void circle(int xc,int yc,int g)	{
//		double n;
//		for (n=0;n<6.29;n=n+0.02){
//			double x = Math.sin(n);
//			x = x * g;
//			int xb = (int) x + xc;
//			double y = Math.cos(n);
//			y = y * g;
//			int yb = (int) y + yc;
//			pset(xb,yb);
//		}
//	}
//
//	

    //------------------------------------------------------------------


    private void bitSet(int pix) {
        cmd(0xf8 | pix);
    }

    private void cmd(int d1, int d2, int cmd) {
        writeByte(LOW, d1);
        writeByte(LOW, d2);
        writeByte(HIGH, cmd);
    }

    private void cmd(int d1, int cmd) {
        writeByte(LOW, d1);
        writeByte(HIGH, cmd);
    }

    private void cmd(int cmd) {
        writeByte(HIGH, cmd);
    }

    private void writeByte(boolean cd, int data) {
        if (cdStatus != cd) {
            if (cdStatus == LOW) {
                iow.setBit(CTRL, CD);
                cdStatus = HIGH;
            } else {
                iow.clearBit(CTRL, CD);
                cdStatus = LOW;
            }
            write();
        }
        iow.clearBit(CTRL, WR);
        iow.clearBit(CTRL, CE);
        iow.setPort(DATA, data);
        write();
        iow.setBit(CTRL, WR);
        iow.setBit(CTRL, CE);
        write();
    }

    private void write() {
        iow.writeIOPorts();
//		try {
//			Thread.sleep(10);
//		} catch (InterruptedException e) {
//		}
    }

//	private int status() {
//		iow.setBit(CTRL,CD);
//		write();
//		iow.setDirection(DATA, 0xff);	//all data bits intput
//		iow.clearBit(CTRL,RD);
//		iow.clearBit(CTRL,CE);
//		write();
//		//read status
//		iow.setDirection(DATA, 0x00);	//all data bits output
//		return 0;
//	}

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
