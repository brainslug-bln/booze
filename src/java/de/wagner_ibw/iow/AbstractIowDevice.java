/*
 * This File is part of the iowj-project   
 * $Id: AbstractIowDevice.java,v 1.16 2008/05/19 22:08:19 Thomas Wagner Exp $
 * Copyright (C)2003-2006 by Thomas Wagner
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

import com.codemercs.iow.IowKit;
import de.wagner_ibw.iow.i2c.Monitor;
import sun.misc.Queue;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * This class provides default implementation for the IO-Warrior devices.
 *
 * @author Thomas Wagner
 */
public abstract class AbstractIowDevice {

    /**
     * Constant for IO-Warrior 40 device id: '0x1500'.
     * (decimal 5376)
     */
    public static final long IOW40ID = 0x1500;

    /**
     * Constant for IO-Warrior 24 device id: '0x1501'.
     * (decimal 5377)
     */
    public static final long IOW24ID = 0x1501;

    /**
     * Constant for IO-Warrior 56 device id: '0x1503'.
     * (decimal 5379)
     */
    public static final long IOW56ID = 0x1503;

    /**
     * Constant for IO-Warrior 40 device name: 'IOW40'.
     */
    public static final String IOW40NAME = "IOW40";

    /**
     * Constant for IO-Warrior 24 device name: 'IOW24'.
     */
    public static final String IOW24NAME = "IOW24";

    /**
     * Constant for IO-Warrior 56 device name: 'IOW56'.
     */
    public static final String IOW56NAME = "IOW56";

    /**
     * Constant for IO-Warrior 24 device special mode funtions's record length: '8'.
     */
    public static final int IOW24_SMF_REPORT_LENGTH = 8;

    /**
     * Constant for IO-Warrior 40 device special mode funtions's record length: '8'.
     */
    public static final int IOW40_SMF_REPORT_LENGTH = 8;

    /**
     * Constant for IO-Warrior 56 device special mode funtions's record length: '64'.
     */
    public static final int IOW56_SMF_REPORT_LENGTH = 64;


    /**
     * Device handle of this IO-Warrior device.
     */
    protected long handle;

    /**
     * Device identifier of this IO-Warrior device.
     */
    protected int id;

    /**
     * Revision identifier of this IO-Warrior device.
     */
    protected int rev;

    /**
     * Serial number of this IO-Warrior device.
     */
    protected String serial;

    /**
     * The ports.
     */
    protected IowPort[] ports = {
            new IowPort(0, "IIIIIIII"),
            new IowPort(1, "IIIIIIII"),
            new IowPort(2, "IIIIIIII"),
            new IowPort(3, "IIIIIIII"),
            new IowPort(4, "IIIIIIII"),
            new IowPort(5, "IIIIIIII"),
            new IowPort(6, "IIIIIIII")    //only 2 bits!!

    };

    /**
     * Queue for receiving the current pin status report.
     *
     * @since 0.9.3
     */
    protected Queue currentPinStatus;

    /**
     * Read thread for special mode function pipe.
     *
     * @since 0.9.5
     */
    protected SpecialModeReadThread smThread = null;

    /**
     * Read thread for pin io pipe.
     *
     * @since 0.9.5
     */
    protected PinReadThread pinThread = null;

    /**
     * Queue for receiving the current pin status report.
     *
     * @since 0.9.4
     */
    protected Monitor monitor;

    /**
     * Flag for all special mode functions. Each bit stands for one enabled or
     * disbaled special mode function. See SpecialModeFunction constants.
     *
     * @see de.wagner_ibw.iow.SpecialModeFuntion
     */
    protected int specialMode = 0;

    /**
     * Vector of special mode implementations.
     */
    protected Vector smfImplementations = new Vector();

    /**
     * Flag for autonomous mode.
     */
    protected boolean autonomous = false;

    /**
     * Flag for device state.
     *
     * @since 0.9.6
     */
    protected boolean life = true;


    /**
     * Count of ports (2 for IO-Warrior 24, 4 for IO-Warrior 40, 7 for IO-Warrior 56).
     */
    protected int portCount = 0;

    /**
     * Length of special mode function reports (8 for IO-Warrior 24 and IO-Warrior 40, 64 for IO-Warrior 56).
     *
     * @since 0.9.5
     */
    protected int smfReportLength = 0;

    /**
     * List of <code>IowChangeListener</code>.
     *
     * @since 0.9.5
     */
    protected Vector listeners = null;

    private final static boolean CLASS_DEBUG = false;

    /**
     * Constructor, called from sub class only.
     */
    public AbstractIowDevice() {
        currentPinStatus = new Queue();
        monitor = new Monitor();
        listeners = new Vector();
        pinThread = new PinReadThread();
        smThread = new SpecialModeReadThread();
        smThread.start();
    }

    /**
     * Adds given special mode function implementation to this IO-Warrior device.
     *
     * @param impl Special mode function implementation to add.
     * @throws UnsupportedOperationException if this IO-Warrior device does not support the given
     *                                       special mode function
     * @throws IllegalArgumentException      if the special mode function implementation is already added
     */
    public synchronized void addSpecialModeFunctionImpl(SpecialModeFunction impl)
            throws UnsupportedOperationException, IllegalArgumentException {

        String error = impl.checkCompatibility(id, rev, specialMode);
        if (error != null)
            throw new UnsupportedOperationException(
                    //AbstractIowDevice.IOW24NAME
                    getName()
                            + ", rev "
                            + Integer.toHexString(rev)
                            + " does not support "
                            + impl.getName()
                            + " at this time: "
                            + error
                            + "!");

        if (!smfImplementations.contains(impl)) {
            //set related special mode bit
            specialMode = specialMode | impl.getSpecialModeFuncionId();
            //enabel special mode function
            long ret = writeReport(1, impl.getEnableReport()); //check ret!
            //set special bits in bitmask
            for (int i = 0; i < impl.getIowSpecialBits(id).length; i++) {
                ports[i].setSpecial(
                        ports[i].getSpecial() | impl.getIowSpecialBits(id)[i]);
            }
            //set iow reference
            impl.setIowDevice(this);
            //add implementation to list
            smfImplementations.add(impl);
        } else {
            throw new IllegalArgumentException(
                    impl.getName() + " was alreay added!");
        }
    }

    /**
     * Removes given special mode function implementation from this IO-Warrior device.
     *
     * @param impl Special mode function implementation to remove.
     */
    public synchronized void removeSpecialModeFunctionImpl(SpecialModeFunction impl) {
        if (smfImplementations.contains(impl)) {
            //reset related special mode bit
            specialMode = specialMode & (255 - impl.getSpecialModeFuncionId());
            //disable special mode function
            long ret = writeReport(1, impl.getDisableReport()); //check ret!
            //reset special bits in bitmask
            for (int i = 0; i < impl.getIowSpecialBits(id).length; i++) {
                ports[i].setSpecial(
                        ports[i].getSpecial() & (255 - impl.getIowSpecialBits(id)[i]));
            }
            ;
            //delete iow reference
            impl.setIowDevice(null);
            //remove implementation from list
            smfImplementations.remove(impl);
        }
    }

    /**
     * Returns the desired special mode function implementation.
     *
     * @param smfName Name of the desired function.
     * @return Instance of the desired special mode function implementation.
     * @throws NoSuchElementException if the desired function was not added.
     * @since 0.9.4
     */
    public SpecialModeFunction getSpecialModeFunctionImpl(String smfName)
            throws NoSuchElementException {

        for (int i = 0, length = smfImplementations.size(); i < length; i++) {
            SpecialModeFunction smf =
                    (SpecialModeFunction) smfImplementations.get(i);
            if (smf.getName().equals(smfName))
                return smf;
        }
        throw new NoSuchElementException(
                "Special mode function " + smfName + " not found!");
    }

    /**
     * Returns the handle of this IO-Warrior device.
     *
     * @return Returns the handle of this IO-Warrior device.
     */
    public long getHandle() {
        return handle;
    }

    /**
     * Returns the serial of this IO-Warrior device.
     *
     * @return The serial number of this IO-Warrior device.
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Returns the device id of this IO-Warrior device.
     *
     * @return Returns the device id of this IO-Warrior device.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the length of the special mode function report of this IO-Warrior device.
     *
     * @return Returns the special mode record length of this IO-Warrior device.
     * @since 0.9.5
     */
    public int getSmfReportLength() {
        return smfReportLength;
    }

    /**
     * Returns the revision of this IO-Warrior device.
     *
     * @return Returns the revision id of this IO-Warrior device.
     * @since 0.9.3
     */
    public int getRev() {
        return rev;
    }

    /**
     * Returns the number of ports of this IO-Warrior device.
     *
     * @return Returns the number of ports (IO-Warrior 24: 2, IO-Warrior 40: 4, IO-Warrior 56: 7).
     * @since 0.9.5
     */
    public int getPortCount() {
        return portCount;
    }

    /**
     * Returns IO-Warrior's device state.
     *
     * @return State of the IO-Warrior.
     * @since 0.9.6
     */
    public boolean isAlive() {
        return life;
    }

    /**
     * Sets IO-Warrior's device state to absent.
     *
     * @since 0.9.6
     */
    public void suspendDevice() {
        life = false;
    }

    /**
     * Sets IO-Warrior's device state to present.
     *
     * @param rewritePorts If true it writes the old pin state again.
     * @since 0.9.6
     */
    protected void resumeDevice(boolean rewritePorts) {
        life = true;
        if (rewritePorts)
            writeIOPorts();
        //restore device state: send all smf enable reports to device again
    }

    /**
     * Close this IO-Warrior device.
     * Stops the special mode function read thread and if necessary
     * the pin io read thread.
     */
    public void close() {
        if (smThread != null) {
            //*deb*/debug("AbstractIowDevice.close(): interrupt thread(s)");
            smThread.interrupt();
            smThread = null;
        }
        if (autonomous) {
            autonomous(false);
        }
    }

    /**
     * Set timeout for read operations.
     *
     * @param timeout Timeout value in ms.
     */
    public void setTimeout(long timeout) {
        IowKit.setTimeout(handle, timeout);
    }

    /**
     * Set timeout for write operations.
     *
     * @param timeout Timeout value in ms.
     * @since 0.9.3
     */
    public void setWriteTimeout(long timeout) {
        IowKit.setWriteTimeout(handle, timeout);

    }

    /**
     * Writes a report to this IO-Warrior device.
     *
     * @param pipe number of pipe.
     * @param wbuf Write buffer.
     * @return Count of written bytes.
     * @throws IllegalStateException If this IO-Warrior device is absent.
     * @since 0.9.6 with exception
     */
    public long writeReport(int pipe, int[] wbuf) throws IllegalStateException {
        checkDeviceState();

        debug("writeReport() lenght input array: " + wbuf.length + ", repId: " + Integer.toHexString(wbuf[0]));
        if (wbuf.length == getSmfReportLength()) {
            long ret = IowKit.write(handle, pipe, wbuf);
            if (ret == 0)
                suspendDevice();
            return ret;
        } else {
            //fill the report of
            debug("writeReport() fill the report");
            int tbuf[] = new int[getSmfReportLength()];
            for (int i = 0; i < wbuf.length; i++) {
                tbuf[i] = wbuf[i];
            }
            long ret = IowKit.write(handle, pipe, tbuf);
            if (ret == 0)
                suspendDevice();
            return ret;
        }
    }

    /**
     * Reads the iow ports and store them in the internal port array.
     * Attention! this method blocks until an io pin is changed.
     *
     * @return Port status in long representation.
     * @throws IllegalStateException if this IO-Warrior device is absent.
     * @since 0.9.6 with exception
     */
    public long readIOPorts() throws IllegalStateException {
        checkDeviceState();
        long ret = 0;
        int[] rbuf = IowKit.read(handle, 0, portCount + 1);
        for (int i = 0; i < rbuf.length - 1; i++) {
            int val = rbuf[i + 1] & 0xff;
            ports[i].setDataFromRead(val);
            long newval = (long) val << (i * 8);
            ret = ret + newval;
        }
        if (ret == 0)
            suspendDevice();
        return ret;
    }

    /**
     * Reads the iow ports non blocking and store them in the internal port array.
     *
     * @return Port status in long representation.
     * @throws IllegalStateException if this IO-Warrior device is absent.
     * @since 0.9.6 with exception
     */
    public long readIOPortsNonBlocking() throws IllegalStateException {
        if (!IowFactory.libRel5)
            throw new UnsupportedOperationException("'readIOPortsNonBlocking' requires iowkit library version 1.5!");

        checkDeviceState();

        int[] ri = IowKit.readNonBlocking(handle, 0, portCount + 1);
        /*deb*/
        debug("IowKit.readNonBlocking() returns " + ri.length + " byte");
        long ret = 0;
        if (ri.length > 0) {
            /*deb*/
            debug("Ports[0..." + (portCount - 1) + "]:");
            for (int i = 0; i < ri.length - 1; i++) {
                /*deb*/
                debug(" " + ri[i + 1]);
                ports[i].setDataFromRead(ri[i + 1]);
                long newval = (long) ri[i + 1] << (i * 8);
                ret = ret + newval;
            }
            /*deb*/
            debug("");
        } else {
            //*deb*/debug("readIOPortsNonBlocking()-> no result!");
        }
        /*deb*/
        debug("readIOPortsNonBlocking() returns " + Long.toHexString(ret));
        return ret;
    }

    /**
     * Reads the iow ports and store them in the internal port array (none blocking).
     * Attention! This method does not work with IO-Warrior 56).
     *
     * @return Port status in long representation.
     * @throws UnsupportedOperationException if this IO-Warrior device is a IO-Warrior 56.
     * @throws IllegalStateException         if this IO-Warrior device is absent.
     * @since 0.9.6 with exceptions and not abstract any longer
     *        Thanks to Markus Gebhard for bug reports
     */
    public long readIOPortsImmediate() throws UnsupportedOperationException, IllegalStateException {
        if (id == IOW56ID)
            throw new UnsupportedOperationException(
                    "This method does not support the IO-Warrior 56 device!");

        checkDeviceState();

        int[] ri = IowKit.readImmediate(handle);
        long ret = 0;
        if (ri.length > 0) {
            /*deb*/
            debug("AbstractIowDevice.read immediate()->" + ri[0] + "," + ri[1] + "," + ri[2] + "," + ri[3]);
            for (int i = 0; i < portCount; i++) {
                ports[i].setDataFromRead(ri[i]);
                long newval = (long) ri[i] << (i * 8);
                ret = ret + newval;
            }
        } else {
            /*deb*/
            debug("AbstractIowDevice.read immediate()-> no result!");
        }
        return ret;
    }

    /**
     * Gets the current pin status.
     *
     * @return Port status in long representation.
     * @throws UnsupportedOperationException if this IO-Warrior device does not support the
     *                                       get curent pin status special mode function.
     * @since 0.9.3
     */
    public abstract long scanPorts() throws UnsupportedOperationException;

    /**
     * Gets the current pin status. Call from sub classes only.
     *
     * @return Port status as array of int.
     * @throws UnsupportedOperationException if this IO-Warrior device does not support the
     *                                       get curent pin status special mode function.
     * @since 0.9.3
     */
    protected int[] getCurrentPinStatus()
            throws UnsupportedOperationException {
        int wbuf[] = {0xff, 0, 0, 0, 0, 0, 0, 0};

        if (id != IOW56ID && rev < 0x1011) {
            throw new UnsupportedOperationException(
                    "Release "
                            + Integer.toHexString(rev)
                            + " does not support the current pin status report");
        }

        long ret = writeReport(1, wbuf);
        if (ret == 8) {
            int rbuf[] = {0, 0, 0, 0};
            try {
                int[] resbuf = (int[]) currentPinStatus.dequeue();
                for (int i = 0; i < 4; i++) {
                    rbuf[i] = resbuf[i + 1];
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return rbuf;
        } else if (ret == 64) {
            int rbuf[] = {0, 0, 0, 0, 0, 0, 0};
            try {
                int[] resbuf = (int[]) currentPinStatus.dequeue();
                for (int i = 0; i < 7; i++) {
                    rbuf[i] = resbuf[i + 1];
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return rbuf;
        } else {
            //*deb*/System.out.println("getCurrentPinStatus() writeReport returns != 8");
            return new int[0];
        }
    }

    /**
     * Writes the presetted ports to this IO-Warrior device.
     *
     * @return Count of written bytes.
     * @throws IllegalStateException if this IO-Warrior device is absent.
     * @since 0.9.6 with retval and exception
     */
    public abstract long writeIOPorts() throws IllegalStateException;

    /**
     * Writes the value to this IO-Warrior device's ports.
     *
     * @param value output pattern
     * @return Count of written bytes.
     * @throws IllegalStateException if this IO-Warrior device is absent.
     * @since 0.9.6 with retval and exception
     */
    public abstract long writeIOPorts(long value) throws IllegalStateException;

    /**
     * Sets the autonomous mode either on or off.
     * Autonomous mode means, that class uses readIOPorts() in a loop and in a thread.
     *
     * @param status true switches the mode on, false switches the mode off.
     */
    public void autonomous(boolean status) {
        if (status) {
            if (!autonomous) {
                autonomous = true;
                pinThread.start();
            }
        } else {
            if (autonomous) {
                autonomous = false;
                pinThread.interrupt();
            }
        }
    }

    /**
     * Adds an <code>IowChangeListener</code> to the internal list.
     *
     * @param pcl IowChangeListener that has added to the list.
     * @since 0.9.5
     */
    public void addIowChangeListener(IowChangeListener icl) {
        if (!listeners.contains(icl))
            listeners.add(icl);
    }

    /**
     * Removes given <code>IowChangeListener</code> from the internal list.
     *
     * @param pcl IowChangeListener that has removed from the list.
     * @since 0.9.5
     */
    public void removeIowChangeListener(IowChangeListener icl) {
        if (listeners.contains(icl))
            listeners.remove(icl);
    }

    /**
     * Returns the name of this IO-Warrior device.
     *
     * @return Returns the name of this IO-Warrior device.
     */
    public abstract String getName();

    /**
     * Returns the desired port of this IO-Warrior device.
     *
     * @param port desired port number.
     * @return <code>IowPort</code> instance of the desired port.
     * @throws IllegalArgumentException if the port number is out of range.
     */
    public IowPort getPort(int port) throws IllegalArgumentException {
        checkPort(port);
        return ports[port];
    }

    /**
     * Set the given bit from the given port.
     *
     * @param port Port number (0...1 or 0...3).
     * @param bit  Bit number (0...7).
     * @throws IllegalArgumentException if the port number is out of range.
     */
    public void setBit(int port, int bit)
            throws IllegalArgumentException {
        checkPort(port);
        ports[port].setBit(bit);
    }

    /**
     * Clear the given bit from a the given port.
     *
     * @param port Port number (0...1 or 0...3).
     * @param bit  Bit number (0...7).
     * @throws IllegalArgumentException if the port number is out of range.
     */
    public void clearBit(int port, int bit)
            throws IllegalArgumentException {
        checkPort(port);
        ports[port].clearBit(bit);
    }

    /**
     * Set the pattern to the given port.
     *
     * @param port    Port number (0...3).
     * @param pattern
     * @throws IllegalArgumentException if the port number is out of range.
     */
    public void setPort(int port, int in)
            throws IllegalArgumentException {
        checkPort(port);
        ports[port].setData(in);
    }

    /**
     * Set the direction mask to the given port.
     *
     * @param port      Port number (0...3 or 0...1).
     * @param direction 0-output, 1-input
     * @throws IllegalArgumentException if the port number is out of range.
     * @since 0.9.3
     */
    public void setDirection(int port, int direction)
            throws IllegalArgumentException {
        checkPort(port);
        ports[port].setDirection(direction);
    }

    /**
     * Returns the <code>Monitor</code> reference (for internal I2C control purpose only).
     *
     * @return <code>Monitor</code> reference
     */
    public Monitor getMonitor() {
        return monitor;
    }

    /**
     * Convenient method for checking valid port numbers.
     *
     * @param port port number to check.
     * @throws IllegalArgumentException if the port number is out of range.
     */
    protected void checkPort(int port) throws IllegalArgumentException {
        if (port < 0 || port > portCount - 1)
            throw new IllegalArgumentException(
                    "Invalid port number (" + port + ")");
    }

    /**
     * Convenient method for checking device state.
     *
     * @throws IllegalStateException if this IO-Warrior device is absent.
     */
    protected void checkDeviceState() throws IllegalStateException {
        if (!life) {
            StringBuffer sb = new StringBuffer();
            sb.append(getName());
            sb.append(",Handle[");
            sb.append(getHandle());
            sb.append("],Id[");
            sb.append(getId());
            sb.append("],Rev[");
            sb.append(Integer.toHexString(getRev()));
            sb.append("],Serial[");
            sb.append(getSerial());
            sb.append("] is absent!");
            throw new IllegalStateException(sb.toString());
        }
    }

    /**
     * Returns a hash code value for this Iow object. It is derived from the
     * device id and serial number.
     *
     * @return a hash code value for this object.
     * @since 0.9.4
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + id;
        result = 37 * result + serial.hashCode();
        return result;
    }

    /**
     * Returns a <code>String</code> object representing this Iow object.
     * It contains information about device handle, id , revision,
     * serial number and the status of ports.
     */
    public abstract String toString();

    /**
     * Indicates whether some other <code>IO-Warrior 24, IO-Warrior 40 or IO-Warrior 65</code> object is "equal to" this object.
     * Criteria is the serial number.
     *
     * @param obj The reference object with which to compare.
     * @return True if this object is the same as the obj argument; false otherwise.
     * @since 0.9.4
     */
    public abstract boolean equals(Object o);

    private void debug(String msg) {
        if (CLASS_DEBUG) {
            System.out.println(msg);
        }
    }

    /**
     * Read thread for pin io reports.
     *
     * @author Thomas Wagner
     * @since 0.9.5
     */
    class PinReadThread extends Thread {

        /* (non-Javadoc)
           * @see java.lang.Runnable#run()
           */

        public void run() {
            do {
                if (Thread.interrupted()) {
                    /*deb*/
                    debug("SpecialModeReadThread.run(): interrupted");
                    IowKit.cancelIo(handle, 0);
                    break;
                }
                /*deb*/
                debug("PinReadThread.run(): before read");
                long value = readIOPorts();
                for (int i = 0; i < listeners.size(); i++) {
                    IowChangeListener icl = (IowChangeListener) listeners.get(i);
                    (new NotifierThread(value, icl)).start();
                }
                /*deb*/
                debug("PinReadThread.run(): after read");
            } while (true);
        }
    }


    /**
     * Read thread for special mode function reports.
     *
     * @author Thomas Wagner
     */
    class SpecialModeReadThread extends Thread {

        /* (non-Javadoc)
           * @see java.lang.Runnable#run()
           */

        public void run() {
            do {
                if (Thread.interrupted()) {
                    debug("SpecialModeReadThread.run(): interrupted");
                    IowKit.cancelIo(handle, 1);
                    break;
                }

                int blen = 8;
                if (id == IOW56ID)
                    blen = 64;
                int rbuf[] = IowKit.read(handle, 1, blen);

                if (rbuf.length == 0) {
                    debug("SpecialModeReadThread.run(): read returns 0 bytes!");
                    try {
                        sleep(2000);
                    }
                    catch (InterruptedException e) {
                        break; //new in 0.9.6, usefull?
                    }

                    continue;
                }

                debug("SpecialModeReadThread.run(): read " + rbuf.length + ", repId: " + Integer.toHexString(rbuf[0]));
                for (int i = 0, len = rbuf.length; i < len; i++) {
                    rbuf[i] = rbuf[i] & 0xff;
                }

                if (rbuf.length > 0) {
                    for (int i = 0; i < smfImplementations.size(); i++) {
                        SpecialModeFunction smf =
                                (SpecialModeFunction) smfImplementations.get(i);
                        if (smf.matchReportId(rbuf[0])) {
							smf.reportReceived(rbuf);
							break;
						}
					}
					if (rbuf[0] == 0xff) {
						currentPinStatus.enqueue(rbuf);
					}
				}
			} while (true);
		}
	}

	/**
     * Inner Class for a notifier thread. If the IO-Warrior's data changed (after the IO-Warrior
     * device was read), for each <code>IowChangeListener</code> will this thread created.
     * This guarantees a asyncrone notification.
     *
     * @author Thomas Wagner
     * @since 0.9.5
     */
	class NotifierThread extends Thread {
	
		long value;
		IowChangeListener listener = null;
	
		NotifierThread(long value, IowChangeListener listener) {
			this.value = value;
			this.listener = listener;
		}
	
		public void run() {
			listener.iowChanged(value);
		}
	}
}