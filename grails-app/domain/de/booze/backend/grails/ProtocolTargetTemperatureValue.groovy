package de.booze.backend.grails

class ProtocolTargetTemperatureValue implements Serializable {

  Double value

  Long created

  static belongsTo = [protocol: Protocol]

  static constraints = {
    value(nullable: true)
    created(nullable: false)
  }
}
