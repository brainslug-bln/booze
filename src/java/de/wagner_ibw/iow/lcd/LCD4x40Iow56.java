/* 
 * This File is part of the iowj-project  
 * $Id: LCD4x40Iow56.java,v 1.2 2008/05/10 18:09:57 Thomas Wagner Exp $
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
 * This class represents a generic 4 x 40 display (it works only with the IO-Warrior 56 and uses its Dual44780 mode).
 * <br>proved type(s):
 * <ul>
 * <li>L4044 (Segor)</li>
 * </ul>
 *
 * @author Thomas Wagner
 * @since 0.9.6
 */
public class LCD4x40Iow56 extends DoubleHD44780Iow56 {

    public LCD4x40Iow56() {
        cols = 40;
        rows = 4;
        physicalRows = 2;
        lineStartAdr = new int[]{0x00, 0x40, 0x00, 0x40};
    }
}
