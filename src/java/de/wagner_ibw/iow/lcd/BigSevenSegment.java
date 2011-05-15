/*
 * This File is part of the iowj-project   
 * $Id$
 * Copyright (C)2004-2007 by Thomas Wagner
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

import java.util.Hashtable;

/**
 * Big seven segment display in 5 + 7 matrix.
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public class BigSevenSegment {

    private Hashtable charRom;

    private char[][] CHAR_8 = {{32, 15, 15, 15, 32},
            {15, 32, 32, 32, 15},
            {15, 32, 32, 32, 15},
            {32, 15, 15, 15, 32},
            {15, 32, 32, 32, 15},
            {15, 32, 32, 32, 15},
            {32, 15, 15, 15, 32}};


    /**
     *
     */
    public BigSevenSegment() {
        charRom = new Hashtable();
    }

}
