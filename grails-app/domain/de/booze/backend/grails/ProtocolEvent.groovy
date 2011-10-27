package de.booze.backend.grails

import grails.converters.JSON

class ProtocolEvent implements Serializable {

  String message
  String type
  String data
  Long created

  static belongsTo = [protocol: Protocol]

  static constraints = {
    message(nullable: false, blank: false, size: 1..255)
    type(nullable: false, blank: false, size: 1..255)
    data(nullable: true, size: 0..5000)
  }

  def readParsedData = {
    if (!data) {
      return []
    }

    return (List) JSON.parse(data)
  }
}
