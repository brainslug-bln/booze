/*
 * This File is part of the iowj-project   
 * $Id: IowFactory.java,v 1.9 2008/04/26 10:34:09 Thomas Wagner Exp $
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

import com.codemercs.iow.IowKit;

import java.io.ObjectStreamException;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * This class manages all the plugged IO-Warrior devices.
 *
 * @author Thomas Wagner
 */
public class IowFactory {

    /**
     * Count of available IO-Warrior devices.
     */
    private static long devCount;

    /**
     * Vector of all plugged IO-Warrior devices.
     */
    private static Vector iowDevices;

    /**
     * Marker for the library release 1.5.
     *
     * @since 0.9.5
     */
    protected static boolean libRel5;

    /**
     * Factory instance.
     */
    public final static IowFactory INSTANCE = new IowFactory();

    /**
     * Result of IowKit.openDevice() invokation.
     * Only needed for IowKit.closeDevice(handle).
     *
     * @since 0.9.6
     */
    private static long handle;

    /**
     * Private constructor.
     */
    private IowFactory() {
        iowDevices = new Vector();
        openAllDevices();
        libRel5 = getVersion().endsWith("1.5");
    }

    /**
     * Static factory method.
     *
     * @return the factory singelton.
     */
    public static IowFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Ensure Singleton class.
     */
    private Object readResolve() throws ObjectStreamException {
        return INSTANCE;
    }

    /**
     * Open all plugged IO-Warrior devices.
     *
     * @since 0.9.3
     */
    private void openAllDevices() {
        handle = IowKit.openDevice();
        devCount = IowKit.getNumDevs();
        for (int i = 0; i < devCount; i++) {
            long handle = IowKit.getDeviceHandle(i + 1);
            int id = (int) IowKit.getProductId(handle);
            String serial = IowKit.getSerialNumber(handle);
            int rev = (int) IowKit.getRevision(handle);
            if (id == AbstractIowDevice.IOW40ID) {
                Iow40 dev40 = new Iow40(handle, serial, rev);
                iowDevices.add(dev40);
            } else if (id == AbstractIowDevice.IOW24ID) {
                Iow24 dev24 = new Iow24(handle, serial, rev);
                iowDevices.add(dev24);
            } else if (id == AbstractIowDevice.IOW56ID) {
                Iow56 dev56 = new Iow56(handle, serial, rev);
                iowDevices.add(dev56);
            } else {
                System.out.println("Unknown IO-Warrior device!");
            }
        }
    }

    /**
     * Reopen all plugged IO-Warrior devices.
     *
     * @param rewritePorts If true it writes the old pin state again.
     * @since 0.9.6
     */
    public static void reopenAllDevices(boolean rewritePorts) throws UnsupportedOperationException {
        if (libRel5)
            throw new UnsupportedOperationException(
                    getVersion() + " does not support this reopen mechanism!");

        IowKit.closeDevice(handle);
        handle = IowKit.openDevice();
        devCount = IowKit.getNumDevs();
        for (int i = 0; i < devCount; i++) {
            long handle = IowKit.getDeviceHandle(i + 1);
            int id = (int) IowKit.getProductId(handle);
            String serial = IowKit.getSerialNumber(handle);
            int rev = (int) IowKit.getRevision(handle);
            if (id == AbstractIowDevice.IOW40ID) {
                Iow40 dev40 = new Iow40(handle, serial, rev);
                if (!iowDevices.contains(dev40)) {
                    iowDevices.add(dev40);
                } else {
                    Iow40 tempDev = (Iow40) iowDevices.get(iowDevices.indexOf(dev40));
                    if (!tempDev.isAlive())
                        tempDev.resumeDevice(rewritePorts);
                }
            } else if (id == AbstractIowDevice.IOW24ID) {
                Iow24 dev24 = new Iow24(handle, serial, rev);
                if (!iowDevices.contains(dev24)) {
                    iowDevices.add(dev24);
                } else {
                    Iow24 tempDev = (Iow24) iowDevices.get(iowDevices.indexOf(dev24));
                    if (!tempDev.isAlive())
                        tempDev.resumeDevice(rewritePorts);
                }
            } else if (id == AbstractIowDevice.IOW56ID) {
                Iow56 dev56 = new Iow56(handle, serial, rev);
                if (!iowDevices.contains(dev56)) {
                    iowDevices.add(dev56);
                } else {
                    Iow56 tempDev = (Iow56) iowDevices.get(iowDevices.indexOf(dev56));
                    if (!tempDev.isAlive())
                        tempDev.resumeDevice(rewritePorts);
                }
            } else {
                System.out.println("Unknown IO-Warrior device!");
            }
        }
    }

    /**
     * Returns the number of plugged IO-Warrior devices.
     *
     * @return Number of plugged IO-Warrior devices.
     * @since 0.9.3
     */
    public long getNumDevices() {
        return devCount;
    }

    /**
     * Returns the first plugged IO-Warrior device.
     *
     * @return Instance of an IO-Warrior device.
     * @throws NoSuchElementException If no plugged IO-Warrior device was found.
     * @since 0.9.5
     */
    public AbstractIowDevice getIowDevice() throws NoSuchElementException {
        AbstractIowDevice dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            dev = (AbstractIowDevice) iowDevices.get(i);
            found = true;
            break;
        }
        if (!found) {
            throw new NoSuchElementException("Cannot find any IO-Warrior device");
        }
        return dev;
    }

    /**
     * Returns the iow device according to the specified serial number.
     *
     * @param serial Serial number of the desired IO-Warrior device.
     * @return Instance of the desired IO-Warrior device.
     * @throws NoSuchElementException If no IO-Warrior device with desired serial number was found.
     * @since 0.9.5
     */
    public AbstractIowDevice getIowDevice(String serial)
            throws NoSuchElementException {
        AbstractIowDevice dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            dev = (AbstractIowDevice) iowDevices.get(i);
            if (dev.getSerial().equalsIgnoreCase(serial)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(
                    "No IOW device, serial="
                            + serial
                            + " found!");
        }
        return dev;
    }

    /**
     * Returns the first plugged IO-Warrior 40 device.
     *
     * @return Instance of an IO-Warrior 40 device.
     * @throws NoSuchElementException If no plugged IO-Warrior 40 device was found.
     */
    public Iow40 getIow40Device() throws NoSuchElementException {
        Iow40 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW40ID) {
                dev = (Iow40) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(AbstractIowDevice.IOW40NAME);
        }
        return dev;
    }

    /**
     * Returns the IO-Warrior 40 device according to the specified serial number.
     *
     * @param serial Serial number of the desired IO-Warrior 40 device.
     * @return Instance of the desired IO-Warrior 40 device.
     * @throws NoSuchElementException If no IO-Warrior 40 device with desired serial number was found.
     */
    public Iow40 getIow40Device(String serial) throws NoSuchElementException {
        Iow40 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW40ID
                    && iowd.getSerial().equalsIgnoreCase(serial)) {
                dev = (Iow40) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(
                    "No "
                            + AbstractIowDevice.IOW40NAME
                            + " device, serial="
                            + serial
                            + " found!");
        }
        return dev;
    }

    /**
     * Returns the first plugged IO-Warrior 24 device.
     *
     * @return Instance of an IO-Warrior 24 device.
     * @throws NoSuchElementException If no plugged IO-Warrior 24 device was found.
     */
    public Iow24 getIow24Device() throws NoSuchElementException {
        Iow24 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW24ID) {
                dev = (Iow24) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(AbstractIowDevice.IOW24NAME);
        }
        return dev;
    }

    /**
     * Returns the IO-Warrior 24 device according to the specified serial number.
     *
     * @param serial Serial number of the desired IO-Warrior 24 device.
     * @return Instance of the desired IO-Warrior 24 device.
     * @throws NoSuchElementException If no IO-Warrior 24 device with desired serial number was found.
     */
    public Iow24 getIow24Device(String serial) throws NoSuchElementException {
        Iow24 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW24ID
                    && iowd.getSerial().equalsIgnoreCase(serial)) {
                dev = (Iow24) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(
                    "No "
                            + AbstractIowDevice.IOW24NAME
                            + " device, serial="
                            + serial
                            + " found!");
        }
        return dev;
    }

    /**
     * Returns the first plugged IO-Warrior 56 device.
     *
     * @return Instance of an IO-Warrior 56 device.
     * @throws NoSuchElementException If no plugged IO-Warrior 56 device was found.
     * @since 0.9.5
     */
    public Iow56 getIow56Device() throws NoSuchElementException {
        Iow56 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW56ID) {
                dev = (Iow56) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(AbstractIowDevice.IOW56NAME);
        }
        return dev;
    }

    /**
     * Returns the IO-Warrior 56 device according to the specified serial number.
     *
     * @param serial Serial number of the desired IO-Warrior 56 device.
     * @return Instance of the desired IO-Warrior 56 device.
     * @throws NoSuchElementException If no iow 56 device with desired serial number was found.
     * @since 0.9.5
     */
    public Iow56 getIow56Device(String serial) throws NoSuchElementException {
        Iow56 dev = null;
        boolean found = false;
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            if (iowd.getId() == AbstractIowDevice.IOW56ID
                    && iowd.getSerial().equalsIgnoreCase(serial)) {
                dev = (Iow56) iowd;
                found = true;
                break;
            }
        }
        if (!found) {
            throw new NoSuchElementException(
                    "No "
                            + AbstractIowDevice.IOW56NAME
                            + " device, serial="
                            + serial
                            + " found!");
        }
        return dev;
    }

    /**
     * Close all the IO-Warrior devices.
     */
    public void closeAllDevices() {
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice dev = (AbstractIowDevice) iowDevices.get(0);
            dev.close();
            iowDevices.remove(0);
        }
        IowKit.closeDevice(0L);
        devCount = 0;
    }

    /**
     * Returns a string representation of the object. It consists of the
     * information about all plugged IO-Warrior devices.
     *
     * @return a string consisting of the device handle, id, revision and serial number of
     *         each plugged IO-Warrior device.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Number of plugged IO-Warrior device(s): " + devCount + "\n");
        for (int i = 0; i < devCount; i++) {
            AbstractIowDevice iowd = (AbstractIowDevice) iowDevices.get(i);
            sb.append("Device" + (i + 1) + ": ");
            sb.append(iowd.getName());
            sb.append(",Handle[");
            sb.append(iowd.getHandle());
            sb.append("],Id[");
            sb.append(iowd.getId());
            sb.append("],Rev[");
            sb.append(Integer.toHexString(iowd.getRev()));
            sb.append("],Serial[");
            sb.append(iowd.getSerial());
            sb.append("]\n");
        }
        return sb.toString();
    }

    /**
     * Closes all open IO-Warrior devices and terminates the jvm.
     *
     * @since 0.9.3
     */
    public void exit(int code) {
        closeAllDevices();
		//System.exit(code); No for use in J2EE Containern
	}

	/**
     * Returns the version of the loaded iowkit library.
     *
     * @return Version of the iowkit.dll.
     * @since 0.9.3
     */
	public static String getVersion() {
		return IowKit.version();
	}
}
