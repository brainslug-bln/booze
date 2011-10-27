/* 
 * This File is part of the iowj-project   
 * $Id: RC5Code.java,v 1.1.1.1 2006/01/15 16:47:27 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.rc5;

/**
 * This class represents a single RC5 code, received by an IOW 24.
 *
 * @author Thomas Wagner
 */
public class RC5Code {

    private int address;
    private int code;
    private boolean startBit = false;
    private boolean toggleBit = false;
    private boolean c6CommandBit = false;

    public RC5Code(int[] rbuf) {
        address = rbuf[2] & 0x1f;
        code = rbuf[1];
        if ((rbuf[2] & 0x20) == 32)
            toggleBit = true;
        if ((rbuf[2] & 0x40) == 64)
            c6CommandBit = true;
        if ((rbuf[2] & 0x80) == 128)
            startBit = true;
    }

    public int getCode() {
        return code;
    }

    public int getAddress() {
        return address;
    }

    public boolean getToggleBit() {
        return toggleBit;
    }

    public boolean getC6CommandBit() {
        return c6CommandBit;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("RC5[Code[");
        sb.append(code);
        sb.append("],Address[");
        sb.append(address);
        sb.append("],StartBit[");
        if (startBit) sb.append("1");
        else sb.append("0");
        sb.append("],ToggleBit[");
        if (toggleBit) sb.append("1");
        else sb.append("0");
        sb.append("],C6CommandBit[");
        if (c6CommandBit) sb.append("1");
        else sb.append("0");
        sb.append("]]");
        return sb.toString();
    }
    /*
     private class RC5Mapping {
         private int c6 = 0;
         private int code = 0;
         private int adr = 0;


     }
     */
}
