/* 
 * This File is part of the iowj-project  
 * $Id: RC5.java,v 1.5 2009/06/27 11:44:53 Thomas Wagner Exp $
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

package de.wagner_ibw.iow.rc5;

import de.wagner_ibw.iow.AbstractIowDevice;
import de.wagner_ibw.iow.SpecialModeFunction;

import java.util.Vector;

/**
 * This is the implementation of the RC5 special mode function.
 *
 * @author Thomas Wagner
 */
public class RC5 implements SpecialModeFunction {

    /**
     * Name of this special mode function.
     */
    final static String name = "RC5";

    /**
     * List of <code>RC5KeyChangeListener</code>.
     */
    private Vector listeners;

    /**
     * Reference to the underlying IOW device.
     */
    private AbstractIowDevice iow;

    /**
     * Constructor
     */
    public RC5() {
        listeners = new Vector();
    }

    /**
     * Adds an <code>RC5KeyChangeListener</code> to the internal list.
     *
     * @param kcl RC5KeyChangeListener that has added to the list.
     */
    public void addKeyChangeListener(RC5KeyChangeListener kcl) {
        if (!listeners.contains(kcl))
            listeners.add(kcl);
    }

    /**
     * Removes given <code>RC5KeyChangeListener</code> from the internal list.
     *
     * @param kcl RC5KeyChangeListener that has removed from the list.
     */
    public void removeKeyChangeListener(RC5KeyChangeListener kcl) {
        if (listeners.contains(kcl))
            listeners.remove(kcl);
    }
/*	
	public void setKeyData(int[] rbuf) {
		if (rbuf[0] == 0x0c) {
			RC5Code code = new RC5Code(rbuf);
			for(int i = 0; i < listeners.size(); i++) {
				RC5KeytChangeListener kcl = (RC5KeytChangeListener)listeners.get(i);
				(new NotifierThread(code, kcl)).start();
			}
		}
	}
*/

    /**
     * Returns Information about the RC5 implementaton as String.
     *
     * @return String representation of information about the RC5 implementation
     *         (count of registered listeners).
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("RC5[Listener[");
        sb.append(listeners.size());
        sb.append("]]");
        return sb.toString();
    }


    /**
     * Inner Class for a notifier thread. If the IO-Warrior's data changed (after the IO-Warrior
     * received a RC5 code), for each <code>RC5KeyChangeListener</code> will this thread created.
     * This guarantees a asyncrone notification.
     *
     * @author Thomas Wagner
     */
    class NotifierThread extends Thread {

        RC5Event event = null;
        RC5KeyChangeListener listener = null;

        NotifierThread(RC5Event event, RC5KeyChangeListener listener) {
            this.event = event;
            this.listener = listener;
        }

        public void run() {
            listener.keyChanged(event);
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getDisableReport()
      */

    public int[] getDisableReport() {
        int wbuf[] = {0xc, 0};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getEnableReport()
      */

    public int[] getEnableReport() {
        int wbuf[] = {0xc, 1};
        return wbuf;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getIowSpecialBits()
      */

    public int[] getIowSpecialBits(int deviceIdentifier) {
        if (deviceIdentifier == AbstractIowDevice.IOW24ID) {
            int masks[] = {0x01};
            return masks;
        } else {
            int masks[] = {}; //no special mode availiable
            return masks;
        }
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getReportIds()
      */

    public int[] getReportIds() {
        int ids[] = {0x0c};
        return ids;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getSpecialModeFuncionId()
      */

    public int getSpecialModeFuncionId() {
        return SpecialModeFunction.SMF_RC5_ID;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#matchReportId(int)
      */

    public boolean matchReportId(int reportId) {
        return (reportId == 0x0c);
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#reportReceived(int[])
      */

    public void reportReceived(int[] readBuffer) {
        RC5Event event = new RC5Event(readBuffer);
        for (int i = 0; i < listeners.size(); i++) {
            RC5KeyChangeListener kcl = (RC5KeyChangeListener) listeners.get(i);
            (new NotifierThread(event, kcl)).start();
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
        //only for IO-Warrior 24
        if (id != AbstractIowDevice.IOW24ID) return "Works only with IOW24";
        return null;
    }

    /* (non-Javadoc)
      * @see de.wagner_ibw.iow.SpecialModeFunction#getName()
      */
	public String getName() {
		return name;
	}

}