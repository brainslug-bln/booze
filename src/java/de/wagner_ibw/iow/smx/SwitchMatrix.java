/* 
 * This File is part of the iowj-project   
 * $Id: CaptureTimers.java,v 1.4 2009/06/28 14:21:27 Thomas Wagner Exp $
 * Copyright (C)2005 by Thomas Wagner
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
import sun.misc.Queue;

import java.util.Hashtable;
import java.util.Vector;

/**
 * This is the implementation of the IO Warrior key matrix special mode function.
 * <p><b>(unfinished!)</b></p>
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public class SwitchMatrix implements SpecialModeFunction {

    /**
     * Name of this special mode function.
     */
    final static String name = "SMX";

    /**
     * List of <code>SwitchMatrixChangeListener</code>.
     */
    protected Vector listeners;

    /**
     * Reference to the underlying IOW device.
     */
    protected AbstractIowDevice iow;

    /**
     * Current state of the switch matrix.
     */
    private int matrix[] = {0, 0, 0, 0, 0, 0, 0, 0};
    private int oldMatrix[] = {0, 0, 0, 0, 0, 0, 0, 0};

    protected Queue currentMatrixStatus;
    protected boolean doScan = false;

    private Hashtable keyMap = null;

    /**
     * Constructor
     */
    public SwitchMatrix() {
        listeners = new Vector();
        currentMatrixStatus = new Queue();
    }

    /**
     * Adds an <code>SwitchMatrixChangeListener</code> to the internal list.
     *
     * @param pcl SwitchMatrixChangeListener that has added to the list.
     */
    public void addSwitchMatrixChangeListener(SwitchMatrixChangeListener smcl) {
        if (!listeners.contains(smcl))
            listeners.add(smcl);
    }

    /**
     * Removes given <code>SwitchMatrixChangeListener</code> from the internal list.
     *
     * @param pcl SwitchMatrixChangeListener that has removed from the list.
     */
    public void removeSwitchMatrixChangeListener(SwitchMatrixChangeListener smcl) {
        if (listeners.contains(smcl))
            listeners.remove(smcl);
    }

    /**
     * Inner Class for a notifier thread. If the IO-Warrior's data changed (after the IO-Warrior
     * received the switch matrix reports ), for each <code>SwitchMatrixChangeListener</code> will this thread created.
     * This guarantees a asyncrone notification.
     *
     * @author Thomas Wagner
     */
    class NotifierThread extends Thread {

        SwitchMatrixEvent event = null;
        SwitchMatrixChangeListener listener = null;

        NotifierThread(SwitchMatrixEvent event, SwitchMatrixChangeListener listener) {
            this.event = event;
            this.listener = listener;
        }

        public void run() {
            listener.matrixChanged(event);
        }
    }

    /**
     * Returns Information about the switch matrix as String.
     *
     * @return String representation of information about the switch matrix
     *         (row7,row6, ... row0).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SMX[");
        for (int i = 7; i > -1; i--) {
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
        for (int i = 7; i > -1; i--) {
            sb.append(oldMatrix[i]);
            if (i > 0)
                sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        int wbuf[] = {0x18, 0};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        int wbuf[] = {0x18, 1};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW40ID) {
            int masks[] = {0x00, 0x00, 0xff, 0xff};
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
      * IOW40 uses 0x19,0x1a
      * IOW56 uses only 0x19
      */

    public int[] getReportIds() {
        if (iow.getId() == AbstractIowDevice.IOW40ID) {
            int ids[] = {0x19, 0x1a};
            return ids;
        } else {
            int ids[] = {0x19};
            return ids;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_SMX_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      * IOW40 uses 0x19,0x1a
      * IOW56 uses only 0x19
      */

    public boolean matchReportId(int reportId) {
        if (iow.getId() == AbstractIowDevice.IOW40ID) {
            return ((reportId == 0x19) || (reportId == 0x1a));
        } else {
            return (reportId == 0x19);
        }
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
      * @see de.wagner_ibw.iow.SpecialModeFunction#setIowDevice(de.wagner_ibw.iow.IowInterface)
      */

    public void setIowDevice(AbstractIowDevice iow) {
        this.iow = iow;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#checkCompatibility()
      * @since 0.9.4
      */

    public String checkCompatibility(int id, int rev, int specialModes) {
        int atLeastRev = 0x1021;
        //not for IO-Warrior 24
        if (id == AbstractIowDevice.IOW24ID) return "Does not work with IOW24";
        //only for Revision 1.0.2.1 and newer
        if ((id == AbstractIowDevice.IOW40ID) && (rev < atLeastRev)) return "Revision is lesser than 1.0.2.1";
        return null;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */

    public String getName() {
        return name;
    }

    /*
      * @TODO add Javadoc
      */

    public int[] scanMatrix() {
        doScan = true;
        int wbuf[] = {0x19, 0, 0, 0, 0, 0, 0, 0};
        long ret = iow.writeReport(1, wbuf);
        if (ret == 8) {
            //IOW40 report handling
            try {
                setMatrixArray((int[]) currentMatrixStatus.dequeue());
                setMatrixArray((int[]) currentMatrixStatus.dequeue());
                doScan = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (ret == 64) {
            //IOW56 report handling
            try {
                setMatrixArray((int[]) currentMatrixStatus.dequeue());
                doScan = false;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        if (iow.getId() == AbstractIowDevice.IOW40ID) { //IOW40
            //first report
            if (readBuffer[0] == 0x1a) {
                oldMatrix[4] = matrix[4];
                oldMatrix[5] = matrix[5];
                oldMatrix[6] = matrix[6];
                oldMatrix[7] = matrix[7];
                //*deb*/System.out.println("setting matrix[4...7]");
                matrix[4] = readBuffer[1];
                matrix[5] = readBuffer[2];
                matrix[6] = readBuffer[3];
                matrix[7] = readBuffer[4];
            }
            //second report
            else if (readBuffer[0] == 0x19) {
                oldMatrix[0] = matrix[0];
                oldMatrix[1] = matrix[1];
                oldMatrix[2] = matrix[2];
                oldMatrix[3] = matrix[3];
                //*deb*/System.out.println("setting matrix[0...3]");
                matrix[0] = readBuffer[1];
                matrix[1] = readBuffer[2];
                matrix[2] = readBuffer[3];
                matrix[3] = readBuffer[4];
            }
        } else { //IOW56
            for (int i = 0; i < 8; i++) {
                oldMatrix[i] = matrix[i];
            }
            for (int i = 0; i < 8; i++) {
                matrix[i] = readBuffer[i + 1];
            }
        }

        //create event
        if (!doScan) {
            if ((iow.getId() == AbstractIowDevice.IOW40ID && readBuffer[0] == 0x19) ||
                    iow.getId() == AbstractIowDevice.IOW56ID) {

//				//copy matrix to oldMatrix					
//				for (int i = 0; i < 8; i++) {
//					oldMatrix[i] = matrix[i];
//				}
//						
                SwitchMatrixEvent event = new SwitchMatrixEvent(matrix, oldMatrix);
                for (int i = 0; i < listeners.size(); i++) {
                    SwitchMatrixChangeListener smcl = (SwitchMatrixChangeListener) listeners.get(i);
                    (new NotifierThread(event, smcl)).start();
				}
			}
		}
	}
}
