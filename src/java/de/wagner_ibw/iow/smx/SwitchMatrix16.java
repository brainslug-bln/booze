/* 
 * This File is part of the iowj-project   
 * $Id$
 * Copyright (C)2009 by Thomas Wagner
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

package de.wagner_ibw.iow.smx;

import de.wagner_ibw.iow.AbstractIowDevice;
import de.wagner_ibw.iow.SpecialModeFunction;

/**
 * This is the implementation of the new IO Warrior 16x8 key matrix special mode function .
 *
 * @author Thomas Wagner
 * @since 0.9.7
 */
public class SwitchMatrix16 extends SwitchMatrix {

    /**
     * Name of this special mode function.
     */
    final static String name = "SMX16";

    /**
     * Current state of the switch matrix.
     */
    private int matrix[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int oldMatrix[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};


    /**
     * Constructor
     */
    public SwitchMatrix16() {
        super();
    }

    /**
     * Returns Information about the switch matrix as String.
     *
     * @return String representation of information about the switch matrix
     *         (row15,row14, ... row0).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SMX[");
        for (int i = 15; i > -1; i--) {
            sb.append(matrix[i]);
            if (i > 0)
                sb.append(",");
        }
        sb.append("],");
        long[] longValue = getLongValue();
        sb.append(Long.toHexString(longValue[1]));
        sb.append(",");
        sb.append(Long.toHexString(longValue[0]));
        sb.append(",");
        sb.append(",oSMX[");
        for (int i = 15; i > -1; i--) {
            sb.append(oldMatrix[i]);
            if (i > 0)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        int wbuf[] = {0x18, 2};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW40ID) {
            int masks[] = {0x00, 0xff, 0xff, 0xff};
            return masks;
        } else if (deviceIdentifier == AbstractIowDevice.IOW56ID) {
            int masks[] = {0xff, 0x00, 0xff};
            return masks;
        } else {
            int masks[] = {}; //no special mode availiable
            return masks;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      * IOW40 uses 0x19,0x1a for 8x8 or 0x19,0x1a,0x1b,0x1c for 16x8
      * IOW56 uses only 0x19
      */

    public int[] getReportIds() {
        int ids[] = {0x19, 0x1a, 0x1b, 0x1c};
        return ids;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_SMX16_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      * IOW40 16x8 uses 0x19,0x1a,0x1b,0x1c
      */

    public boolean matchReportId(int reportId) {
        return ((reportId == 0x19) || (reportId == 0x1a) || (reportId == 0x1b) || (reportId == 0x1c));
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {

        if (doScan) {
            currentMatrixStatus.enqueue(readBuffer);
        } else {
            setMatrixArray(readBuffer);
        }
    }


    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility()
      * @since 0.9.4
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        int atLeastRev = 0x1030;
        //not for IO-Warrior 24
        if (id == AbstractIowDevice.IOW24ID) return "Does not work with IOW24";
        //only for Revision 1.0.2.1 and newer
        if ((id == AbstractIowDevice.IOW40ID) && (rev < atLeastRev)) return "Revision is lesser than 1.0.3.0";
        //not at the same time with LCD
        if ((specialModes & SpecialModeFunction.SMF_LCD_ID) == SpecialModeFunction.SMF_LCD_ID)
            return "LCD is already activated";

        return null;
    }


    /*
      * @TODO add Javadoc
      */

    public int[] scanMatrix() {
        doScan = true;
        int wbuf[] = {0x19, 0, 0, 0, 0, 0, 0, 0};
        long ret = iow.writeReport(1, wbuf);

        try {
            setMatrixArray((int[]) currentMatrixStatus.dequeue());
            setMatrixArray((int[]) currentMatrixStatus.dequeue());
            setMatrixArray((int[]) currentMatrixStatus.dequeue());
            setMatrixArray((int[]) currentMatrixStatus.dequeue());
            doScan = false;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    /*
      * @TODO add Javadoc
      */

    public long[] getLongValue() {
        long ret[] = {0, 0};
        for (int i = 0; i < 4; i++) {
            long newval = (long) matrix[i] << (i * 8);
            ret[0] = ret[0] + newval;
        }
        ret[0] = ret[0] & 0xffffffff;
        for (int i = 3; i < matrix.length; i++) {
            long newval = (long) matrix[i] << (i * 8);
            ret[1] = ret[1] + newval;
        }
        ret[1] = ret[1] & 0xffffffff;
        return ret;
    }

    private void setMatrixArray(int[] readBuffer) {

        //1st report
        if (readBuffer[0] == 0x1a) {
            oldMatrix[4] = matrix[4];
            oldMatrix[5] = matrix[5];
            oldMatrix[6] = matrix[6];
            oldMatrix[7] = matrix[7];
            /*deb*/
            System.out.println("setting matrix[4...7]");
            matrix[4] = readBuffer[1];
            matrix[5] = readBuffer[2];
            matrix[6] = readBuffer[3];
            matrix[7] = readBuffer[4];
        }
        //2nd report
        else if (readBuffer[0] == 0x19) {
            oldMatrix[0] = matrix[0];
            oldMatrix[1] = matrix[1];
            oldMatrix[2] = matrix[2];
            oldMatrix[3] = matrix[3];
            /*deb*/
            System.out.println("setting matrix[0...3]");
            matrix[0] = readBuffer[1];
            matrix[1] = readBuffer[2];
            matrix[2] = readBuffer[3];
            matrix[3] = readBuffer[4];
        }
        //3rd report
        else if (readBuffer[0] == 0x1b) {
            oldMatrix[8] = matrix[8];
            oldMatrix[9] = matrix[9];
            oldMatrix[10] = matrix[10];
            oldMatrix[11] = matrix[11];
            /*deb*/
            System.out.println("setting matrix[8...11]");
            matrix[8] = readBuffer[1];
            matrix[9] = readBuffer[2];
            matrix[10] = readBuffer[3];
            matrix[11] = readBuffer[4];
        }
        //4th report
        else if (readBuffer[0] == 0x1c) {
            oldMatrix[12] = matrix[12];
            oldMatrix[13] = matrix[13];
            oldMatrix[14] = matrix[14];
            oldMatrix[15] = matrix[15];
            /*deb*/
            System.out.println("setting matrix[12...15]");
            matrix[12] = readBuffer[1];
            matrix[13] = readBuffer[2];
            matrix[14] = readBuffer[3];
            matrix[15] = readBuffer[4];
        }


        //create event
        if (!doScan) {
            if (iow.getId() == AbstractIowDevice.IOW40ID && readBuffer[0] == 0x19) {

                SwitchMatrixEvent event = new SwitchMatrixEvent(matrix, oldMatrix);
                for (int i = 0; i < listeners.size(); i++) {
                    SwitchMatrixChangeListener smcl = (SwitchMatrixChangeListener) listeners.get(i);
                    (new NotifierThread(event, smcl)).start();
                }
			}
		}
	}
}
