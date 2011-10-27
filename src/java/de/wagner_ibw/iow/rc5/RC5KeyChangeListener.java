/* 
 * This File is part of the iowj-project  
 * $Id: RC5KeyChangeListener.java,v 1.1.1.1 2006/01/15 16:47:27 Thomas Wagner Exp $
 * Copyright (C)2005 by Thomas Wagner
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

package de.wagner_ibw.iow.rc5;

/**
 * This Interface contains the call back methods for the rc5 key change listener.
 *
 * @author Thomas Wagner
 */
public interface RC5KeyChangeListener {
    public void keyChanged(RC5Event event);
}
