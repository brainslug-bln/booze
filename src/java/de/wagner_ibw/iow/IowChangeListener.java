/* 
 * This File is part of the iowj-project  
 * $Id: IowChangeListener.java,v 1.3 2007/03/17 16:29:40 Thomas Wagner Exp $
 * Copyright (C)2006 by Thomas Wagner
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

package de.wagner_ibw.iow;

/**
 * This Interface contains the call back method for the IO-Warrior change listener.
 *
 * @author Thomas Wagner
 * @since 0.9.5
 */
public interface IowChangeListener {
    public void iowChanged(long value);
}
