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

class Hop {

  String name
  Double percentAlpha
  Integer time
  Double amount

  static constraints = {
    name(size: 3..255, nullable: false, blank: false)
    percentAlpha(min: 0.0 as Double, max: 100.0 as Double, nullable: false)
    time(min: 0, max: 1000000, nullable: false)
    amount(min: 0.0 as Double, max: 1000.0 as Double, nullable: false)
  }
}
