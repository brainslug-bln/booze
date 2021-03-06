/* 
 * This File is part of the iowj-project  
 * $Id$
 * Copyright (C)2008 by Thomas Wagner
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

package de.wagner_ibw.iow.lcd;


/**
 * This class represents a generic 16 x 16 display (it works only with the IO-Warrior 56 and uses its T6963 mode).
 * <br>proved type(s):
 * <ul>
 * <li>TOSHIBA TLX-1391 (Pollin)</li>
 * </ul>
 *
 * @author Thomas Wagner
 * @since 0.9.6
 */
public class LCD16x16Iow56 extends T6963CIow56 {

    public LCD16x16Iow56() {
        cols = 16;
        rows = 16;
    }
}
