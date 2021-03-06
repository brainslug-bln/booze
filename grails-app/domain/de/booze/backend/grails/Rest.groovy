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
 **/

package de.booze.backend.grails

class Rest implements Comparable, Serializable {

  /**
   * Target temperature
   */
  Double temperature

  /**
   * Rest duration
   */
  Integer duration

  /**
   * Index within rests
   */
  Integer indexInRests

  /**
   * Optional comment
   */
  String comment

 
  static constraints = {
    temperature(min: 0.0 as Double, max: 100 as Double, nullable: false)
    duration(min: 0, max: 1000000, nullable: false)
    comment(size: 0..5000, nullable: true, blank: true)
    indexInRests(nullable: false, min: 0, max: 99999)
  }

  int compareTo(obj) {
    if (!obj || !indexInRests) {
      return -1
    }

    return indexInRests.compareTo(obj.indexInRests)
  }
}