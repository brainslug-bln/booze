/* 
 * This File is part of the iowj-project  
 * $Id: IowPort.java,v 1.4 2007/03/17 16:29:40 Thomas Wagner Exp $
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

package de.wagner_ibw.iow;

import java.util.Vector;

/**
 * A <code>BinaryPort</code> represents an IO-Warrior port consists of 8 bits.
 * The IO-Warrior 24 contains two of them, the IO-Warrior 40 four and
 * the IO-Warrior 56 seven of them (port 6 only bit 0 and 7).
 *
 * @author Thomas Wagner
 */
public class IowPort {

    /**
     * Data register of this port.
     * Contains the status of each bit (0-Low, 1-High).
     */
    private int data = 0x00;

    /**
     * Direction register of this port (io mask).
     * Marks bits as input or output.
     */
    private int direction = 0x00;

    /**
     * Special bit register of this port (specMask).
     * Marks bits as dedicated for special mode functions.
     */
    private int special = 0x00;

    /**
     * Contains the port number relating to an real IO-Warrior device that
     * this port belongs to (0-1 for IO Warrior 24, 0-3 for IO Warrior 40, 0-6 for IO Warrior 56).
     */
    private int index = 0x00;

    /**
     * List of <code>IowPortChangeListener</code>.
     */
    private Vector listeners;

    /**
     * Simple constructor (io mask as int).
     *
     * @param index  index for this port.
     * @param ioMask io mask for this port.
     */
    public IowPort(int index, int ioMask) {
        this.index = index;
        listeners = new Vector();
        direction = ioMask;
        data = ioMask; //preset depending on directions
    }

    /**
     * Simple constructor (io mask as int) with initial data.
     *
     * @param index       index for this port.
     * @param ioMask      io mask for this port.
     * @param initialData initial data fro this port.
     */
    public IowPort(int index, int ioMask, int initialData) {
        this.index = index;
        listeners = new Vector();
        direction = ioMask;
        int invertedMask = (Integer.MAX_VALUE - direction) & 0xff;
        data = initialData & invertedMask;
        listeners = new Vector();
    }

    /**
     * Simple constructor (io mask as String).
     *
     * @param index  index for this port.
     * @param ioMask io mask for this port.
     * @throws IllegalArgumentException if string ioMask is invalid.
     */
    public IowPort(int index, String ioMask) throws IllegalArgumentException {
        this.index = index;
        listeners = new Vector();
        data = convDirString(ioMask); //preset depending on directions
    }

    /**
     * Simple constructor (io mask as String) with initial data as String.
     *
     * @param index       index for this port.
     * @param ioMask      io mask for this port.
     * @param initialData initial data fro this port.
     * @throws IllegalArgumentException if string ioMask is invalid.
     */
    public IowPort(int index, String ioMask, String initialData)
            throws IllegalArgumentException {
        this.index = index;
        listeners = new Vector();
        convDirString(ioMask);
        int invertedMask = (Integer.MAX_VALUE - direction) & 0xff;
        data = convDataString(initialData) & invertedMask;
    }

    /**
     * Sets the direction for the port's pins (io mask as String).
     *
     * @param ioMask Describes the pin direction (MSB first):
     *               <ul><li>I - Input.</li><li>O - Output.</li></ul>
     * @throws IllegalArgumentException If ioMask's length != 8 or if ioMask
     *                                  contains invalid chars (valid are 'iIoO')
     */
    public void setDirection(String ioMask) throws IllegalArgumentException {
        convDirString(ioMask);
    }

    /**
     * Sets the direction for the port's pins (io mask as int).
     *
     * @param ioMask
     */
    public void setDirection(int ioMask) {
        direction = ioMask;
    }

    /**
     * Sets the entire port's dtat to the given integer value.
     * <br>Be aware of the fact that the new data are not automatically written to the
     * IO Warrior device! Use <code>writeIOPorts()</code> additionally to do this.
     *
     * @param value
     */
    public void setData(int value) {
        int old = data | direction;
        int invertetdMask = (Integer.MAX_VALUE - direction) & 0xff;
        data = value & invertetdMask;
        //*deb*/System.out.println("setPort()->  invMask: "+ invertetdMask +", old: "+old+", new: " + data);
    }

    /**
     * Sets the entire port's data to the given String value.
     * <br>Be aware of the fact that the new data are not automatically written to the
     * IO Warrior device! Use <code>writeIOPorts()</code> additionally to do this.
     *
     * @param value
     * @throws IllegalArgumentException
     */
    public void setData(String value) throws IllegalArgumentException {
        int old = data | direction;
        int invertetdMask = (Integer.MAX_VALUE - direction) & 0xff;
        data = convDataString(value) & invertetdMask;
    }

    /**
     * Sets the given bit to 1 (set).<br>
     * If this bit an input or if it is already 1 then nothing happend.
     * Otherwise the bit is set and dataChanged flag is set true.
     * <br>Be aware of the fact that the new data are not automatically written to the
     * IO Warrior device! Use <code>writeIOPorts()</code> additionally to do this.
     *
     * @param num Number of the bit which is set (1).
     * @throws IllegalArgumentException Is thrown if num contains an
     *                                  invalid bit number (valid numbers are 0...7).
     */
    public void setBit(int num) throws IllegalArgumentException {
        checkBit(num);
        int mask = (int) Math.pow(2, num);
        if ((direction & mask) != mask) { //if no input
            if ((data & mask) != mask) { //if this bit is not set
                data += (int) Math.pow(2, num);
            }
        }
    }

    /**
     * Sets the given bit to 0 (clear).<br>
     * If this bit an input or if it is already 0 then nothing happend.
     * Otherwise the bit is clear and dataChanged flag is set true.
     * <br>Be aware of the fact that the new data are not automatically written to the
     * IO Warrior device! Use <code>writeIOPorts()</code> additionally to do this.
     *
     * @param num Number of the bit which is clear (0).
     * @throws IllegalArgumentException Is thrown if num contains an
     *                                  invalid bit number (valid numbers are 0...7).
     */
    public void clearBit(int num) throws IllegalArgumentException {
        checkBit(num);
        int mask = (int) Math.pow(2, num);
        if ((direction & mask) != mask) { //if no input
            if ((data & mask) == mask) { //is this bit is set
                data -= (int) Math.pow(2, num);
            }
        }
    }

    /**
     * Returns true, if the given bit is set (1).
     *
     * @param num Number of the desired bit.
     * @return True, if the bit is set.
     * @throws IllegalArgumentException Is thrown if num contains an
     *                                  invalid bit number (valid numbers are 0...7).
     */
    public boolean isBitSet(int num) throws IllegalArgumentException {
        checkBit(num);
        int mask = (int) Math.pow(2, num);
        return ((data & mask) == mask);
    }

    /**
     * Returns true, if the given bit is clear (0).
     *
     * @param num Number of the desired bit.
     * @return True, if the bit is not set.
     * @throws IllegalArgumentException Is thrown if num contains an
     *                                  invalid bit number (valid numbers are 0...7).
     */
    public boolean isBitClear(int num) throws IllegalArgumentException {
        checkBit(num);
        return !isBitSet(num);
    }

    /**
     * Returns the content of this port prepared for write operation.
     * All inputs are set to high.
     *
     * @return Integer representation of data.
     */
    public int getDataToWrite() {
        return data | direction;
    }

    /**
     * Sets the port's data register after read report received.
     *
     * @param readData appropriate byte from read report.
     */
    public void setDataFromRead(int readData) {
        if (direction != 0)
            data = readData; //nur wenn input
        //data = readData & direction;

        for (int i = 0; i < listeners.size(); i++) {
            IowPortChangeListener pcl =
                    (IowPortChangeListener) listeners.get(i);
            (new NotifierThread(this, pcl)).start();
        }
    }

    /**
     * Setter for special bit mask.
     *
     * @param specialMask special bit mask to set.
     */
    public void setSpecial(int specialMask) {
        special = specialMask;
    }

    /**
     * Getter for special bit mask.
     *
     * @return special bit mask.
     */
    public int getSpecial() {
        return special;
    }

    /**
     * Returns the integer representation of this port.
     *
     * @return Integer representation.
     * @since 0.9.3
     */
    public int getData() {
        return data & 0x000000ff;
    }

    /**
     * Returns the port index of this port.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Convenient method for converting data information from String (i.e. 'XX010011')
     * to int. 'X' or 'x' means 'don't care' and is the same as 0 (for what is this feature? ;-).
     *
     * @param bin
     * @return
     * @throws IllegalArgumentException
     */
    private int convDataString(String bin) throws IllegalArgumentException {
        char[] ca = bin.toCharArray();
        int out = 0;
        if (ca.length != 8)
            throw new IllegalArgumentException(
                    "Length (" + ca.length + ") of input parameter is not 8");
        for (int i = 0; i < 8; i++) {
            int value = (int) Math.pow(2, 7 - i);
            if (ca[i] == '0') { /*nothing todo*/
            } else if (ca[i] == '1') {
                out += value;
            } else if (ca[i] == 'x' || ca[i] == 'X') { /* don't care */
            } else
                throw new IllegalArgumentException(
                        "Invalid char '" + ca[i] + "' in input parameter");
        }
        return out;
    }

    /**
     * Convenient method for converting direction information from String (i.e. "IIIIOOOO")
     * to int. 'I' or 'i' means input, 'O' or 'o' means output.
     *
     * @param bin
     * @return
     * @throws IllegalArgumentException
     */
    private int convDirString(String bin) throws IllegalArgumentException {
        char[] ca = bin.toCharArray();
        if (ca.length != 8)
            throw new IllegalArgumentException(
                    "Length (" + ca.length + ") of input parameter is not 8");
        direction = 0;
        for (int i = 0; i < 8; i++) {
            int value = (int) Math.pow(2, 7 - i);
            if (ca[i] == 'O' || ca[i] == 'o') { /*do nothing*/
            } else if (ca[i] == 'I' || ca[i] == 'i')
                direction += value;
            else
                throw new IllegalArgumentException(
                        "Invalid char '" + ca[i] + "' in input parameter");
        }
        return direction;
    }

    /**
     * Adds an <code>IowPortChangeListener</code> to the internal list.
     *
     * @param pcl IowPortChangeListener that has added to the list.
     */
    public void addPortChangeListener(IowPortChangeListener pcl) {
        if (!listeners.contains(pcl))
            listeners.add(pcl);
    }

    /**
     * Removes an <code>IowPortChangeListener</code> from the internal list.
     *
     * @param pcl IowPortChangeListener that has removed from the list.
     */
    public void removePortChangeListener(IowPortChangeListener pcl) {
        if (listeners.contains(pcl))
            listeners.remove(pcl);
    }

    /**
     * Validate the given bit number.
     *
     * @param bit
     * @throws IllegalArgumentException Is thrown if num contains an
     *                                  invalid bit number (valid numbers are 0...7).
     * @since 0.9.3
     */
    private void checkBit(int bit) throws IllegalArgumentException {
        if (bit < 0 || bit > 7)
            throw new IllegalArgumentException(
                    "Invalid bit number (" + bit + ")");
    }

    /**
     * Returns the String represenation of this port.
     * 'IoMask' describes the pin direction (MSB first):
     * <ul>
     * <li>I - Input.</li>
     * <li>O - Output.</li>
     * <li>S - Dedicated by special mode function.</li>
     * </ul>
     * 'Data' describes the current pin status (MSB first):
     * <ul>
     * <li>0 - Low.</li>
     * <li>1 - High.</li>
     * <li>X - Don't care (dedicated by special mode function).</li>
     * </ul>
     *
     * @return String representation.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("P" + index + "[");
        sb.append("IoMask[");
        for (int i = 7; i > -1; i--) {
            int mask = (int) Math.pow(2, i);
            if ((special & mask) == mask)
                sb.append('S'); //for special bit
            else if ((direction & mask) == mask)
                sb.append('I'); //for input
            else
                sb.append('O');
        }
        sb.append("],Data[");
        for (int i = 7; i > -1; i--) {
            int mask = (int) Math.pow(2, i);
            if ((special & mask) == mask)
                sb.append('X'); //for special bit
            else if ((data & mask) == mask)
                sb.append('1');
            else
                sb.append('0');
        }
        sb.append("],Listener[");
        sb.append(listeners.size());
        sb.append("]]");
        return sb.toString();
    }

    /**
     * Internal Class for a notifier thread. If the port's data changed (after the IO-Warrior
     * device was read), for each <code>IowPortChangeListener</code> will this thread created.
     * This guarantees a asyncrone notification.
     *
     * @author Thomas Wagner
     */
	class NotifierThread extends Thread {

		IowPort port = null;
		IowPortChangeListener listener = null;

		NotifierThread(IowPort port, IowPortChangeListener listener) {
			this.port = port;
			this.listener = listener;
		}

		public void run() {
			listener.portChanged(port);
		}
	}
}