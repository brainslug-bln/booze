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

package de.booze.backend.grails

class Malt implements Comparable, Serializable {

  String name
  Double amount
  Double ebc

  static constraints = {
    name(size: 3..255, nullable: false, blank: false)
    amount(min: 0.0 as Double, max: 100 as Double, nullable: false)
    ebc(min: 0.0 as Double, max: 10000 as Double, nullable: false)
  }
  
  int compareTo(obj) {
    if (!obj || !id) {
      return -1
    }

    return id.compareTo(obj.id)
  }
}
