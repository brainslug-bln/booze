package de.booze.backend.grails

class ProtocolPressureValue implements Serializable {

  Double value
  Long created
  String sensorName
  
  static belongsTo = [protocol: Protocol]

  static constraints = {
    value(nullable: true)
    created(nullable: false)
    sensorName(nullable: false, size: 1..254)
  }
}
