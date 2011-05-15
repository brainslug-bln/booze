/* 
 * This File is part of the iowj-project  
 * $Id: LCD1x24.java,v 1.3 2006/09/15 21:10:31 Thomas Wagner Exp $
 * Copyright (C)2004-2006 by Thomas Wagner
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
 * This class represents a generic 1 x 24 display.
 * <br>proved type(s):
 * <ul>
 * <li>TOSHIBA TLC-691 (Pollin)</li>
 * </ul>
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public class LCD1x24 extends HD44780 {

    public LCD1x24() {
        cols = 24;
        rows = 1;
        physicalRows = 2;
        lineStartAdr = new int[]{0x00};
    }
}

