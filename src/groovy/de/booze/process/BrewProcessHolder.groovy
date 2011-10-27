/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.booze.process

/**
 *
 * @author akotsias
 */
class BrewProcessHolder {
  
  private static BrewProcessHolder instance = new BrewProcessHolder();
  
  private BrewProcess brewProcess;
  
	public BrewProcessHolder() {
    
  }
  
  public static BrewProcessHolder getInstance() {
    return instance;
  }
  
  public boolean hasBrewProcess() {
    return (brewProcess != null);
  }
  
  public BrewProcess getBrewProcess() {
    return brewProcess;
  }
  
  public void setBrewProcess(BrewProcess b) {
    this.brewProcess = b;
  }
  
  public flushBrewProcess() {
    this.brewProcess = null;
  }
}

