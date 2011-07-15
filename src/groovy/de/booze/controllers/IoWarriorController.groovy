/**
 * Booze - Software for micro breweries
 *
 * Copyright (C) 2010  Andreas Kotsias <akotsias@esnake.de>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * */

package de.booze.controllers

/*
 * Manages communication with the IOWarrior USB controller
 * Singleton
 *
 * @package de.booze.controllers
 */

import org.apache.log4j.Logger
import de.wagner_ibw.iow.AbstractIowDevice
import de.wagner_ibw.iow.IowFactory
import de.wagner_ibw.iow.IowPort
import de.wagner_ibw.iow.i2c.I2C
import de.wagner_ibw.iow.i2c.PCF8591

/**
 *
 * @author akotsias
 */
class IoWarriorController {

  /**
   * Singleton instance
   */
  private static IoWarriorController instance = new IoWarriorController();

  /**
   * IOWarrior Device instance
   */
  private AbstractIowDevice device

  /**
   * Map of pcf8591 a/d converters
   */
  private Map pcf8591 = [:];

  /**
   * I2C instance for i2c special mode functions
   */
  private I2C i2c;

  /**
   * Logger
   */
  private Logger log = Logger.getLogger(getClass().getName());

  /**
   * Constructor
   */
  private IoWarriorController() {}

  /**
   * Singleton instance getter
   *
   * Connects to the first IOWarrior found
   */
  synchronized public static IoWarriorController getInstance() throws Exception {
    instance.connect()
    return instance;
  }

  /**
   * Connects the IOWarrior
   */
  private synchronized boolean connect() throws Exception {
    if (!this.isConnected()) {
      this.pcf8591 = [:]
      this.i2c = new I2C();

      def iowf = IowFactory.getInstance()

      iowf.openAllDevices();

      if (iowf.getNumDevices() < 1) {
        log.error("No IOWarrior(s) connected")
        throw new Exception("IOWarrior not connected")
      }

      this.device = iowf.getIowDevice();
      for (int i = 0; i < this.instance.device.getPortCount(); i++) {
        this.instance.device.setDirection(i, 0);
      }
    }
  }

  /**
   * Disconnects the IOWarrior
   */
  public void disconnect() throws Exception {
    if (!this.device) {
      throw new Exception("Could not disconnect IOWarrior: not connected");
    }

    this.device.close()
  }

  /**
   * Returns true if an IOWarrior is currently connected
   */
  public boolean isConnected() {
    boolean connected = (this.device && this.device.isAlive())

    // Double check if device is alive by trying to get
    // port 0 bit 0
    if (connected) {
      try {
        this.device.getPort(0).isBitSet(0)
      }
      catch (Exception e) {
        connected = false
      }
    }

    // Reset instance if not connected
    if (!connected) {
      log.error("IOWarrior is dead!")
      this.pcf8591 = [:]
      def iowf = IowFactory.getInstance()
      iowf.closeAllDevices()
    }

    return connected;
  }

  /**
   * Returns a port on the IOWarrior
   *
   * @param int Port to return
   */
  public IowPort getPort(int port) throws Exception, IllegalArgumentException {
    try {
      this.connect();
    }
    catch (Exception e) {
      throw new Exception("Could not get port ${port}, device not alive")
    }

    if (port < 0) {
      throw new IllegalArgumentException("Tried to access a port number < 0")
    }
    if (port > (--this.device.getPortCount())) {
      throw new IllegalArgumentException("Tried to access port #${port} while there are only ${this.device.getPortCount()} available on the device")
    }

    return this.device.getPort(port)
  }

  /**
   * Sets a bit on the given port
   *
   * @param int Port to use
   * @param int Bit to set
   */
  public void setBit(int port, int bit) throws Exception {
    try {
      this.connect();
    }
    catch (Exception e) {
      throw new Exception("Could not set bit ${bit} on port ${port}, device not alive: ${e}")
    }

    this.device.setBit(port, bit)
    this.device.writeIOPorts();
  }

  /**
   * Clears a bit on the given port
   *
   * @param int Port to use
   * @param int Bit to clear
   */
  public void clearBit(int port, int bit) throws Exception {
    try {
      this.connect();
    }
    catch (Exception e) {
      throw new Exception("Could not clear bit ${bit} on port ${port}, device not alive: ${e}")
    }

    this.device.clearBit(port, bit)
    this.device.writeIOPorts();
  }

  /**
   * Checks if a bit on the given port is set
   *
   * @param int Port to check
   * @param int Bit to check
   */
  public boolean isBitSet(int port, int bit) throws Exception {
    try {
      this.connect();
    }
    catch (Exception e) {
      throw new Exception("Could not get bit status ${bit} on port ${port}, device not alive: ${e}")
    }

    return this.getPort(port).isBitSet(bit)
  }

  /**
   * Registers a pcf8591 a/d converter for this controller
   *
   * @param int Address to register at
   */
  public void registerPCF8591(int address) throws Exception {

    if (!this.pcf8591.containsKey(address)) {
      try {
        this.device.addSpecialModeFunctionImpl(this.i2c);
      } catch (IllegalArgumentException i) {
        this.i2c = (I2C) this.device.getSpecialModeFunctionImpl("I2C");
      }

      try {
        this.pcf8591[address] = new PCF8591(address);
        this.i2c.addI2CDevice(this.pcf8591[address]);
      }
      catch (Exception e) {
        throw new Exception("PCF8591 not found at address ${address}: " + e);
      }
    }
  }

  /**
   * Returns true if a pcf8591 is registered at the given address
   */
  public boolean isPCF8591Registered(int address) {
    return (this.pcf8591.containsKey(address));
  }

  /**
   * Returns a pcf8591 instance for the given address
   */
  public PCF8591 getPCF8591(int address) {
    return this.pcf8591[address]
  }

  /**
   * Returns the value of a pcf8591 channel
   *
   * @param int address
   * @param int channel
   */
  public synchronized int readPCF8592Channel(int address, int channel) {
    try {
      if (!this.isConnected()) {
        this.connect();
      }
      if (!this.isPCF8591Registered(address)) {
        this.registerPCF8591(address)
      }
      return this.getPCF8591(address).getChannel(channel) as Double;
    }
    catch (Exception e) {
      log.error("Could not read from PCF8591 at ${address}, channel ${channel} ${e}")
    }
  }

  /**
   * Sets the output value for a pcf8591
   *
   * @param int address
   * @param int value
   */
  public synchronized void setPcf8591OutputValue(int address, int value) {
      try {
          if (!this.isConnected()) {
            this.connect();
          }
          if (!this.isPCF8591Registered(address)) {
            this.registerPCF8591(address)
          }
          PCF8591 pcf = this.getPCF8591(address);
          pcf.enableAnalogueOutput();
          pcf.setAnalogueOutputValue(value);
          log.debug("set output value for pcf at address ${address} to ${value}");
        }
        catch (Exception e) {
          log.error("Could not set analogue output value for PCF8591 at ${address}, value ${value} ${e}")
        }
  }

  protected void finalize() throws Throwable {
    this.device.close();

    def iowf = IowFactory.getInstance()
    iowf.closeAllDevices()
  }
}

