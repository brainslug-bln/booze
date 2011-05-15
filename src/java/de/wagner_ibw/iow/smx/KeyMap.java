/* 
 * This File is part of the iowj-project   
 * $Id: KeyMap.java,v 1.2 2009/06/16 21:38:18 Thomas Wagner Exp $
 * Copyright (C)2005,2009 by Thomas Wagner
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

package de.wagner_ibw.iow.smx;

/**
 * This is a simple implementation of a key map for the key matrix special mode function.
 * Key map means that each key is assigned a value.
 *
 * @author Thomas Wagner
 * @since 0.9.6
 */

public class KeyMap {

    private String[][] label = new String[8][8];
    private int xs = 8;
    private int ys = 8;

    public KeyMap(String[][] map) {

        int x, y = 0;

        for (x = 0; x < map.length; x++) {
            for (y = 0; y < map[x].length; y++) {
                label[x][y] = map[x][y];
            }
        }
        xs = x--;
        ys = y--;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("KeyMatrix[x=");
        sb.append(xs);
        sb.append(",y=");
        sb.append(ys);
        sb.append("]:\n");
        for (int x = 0; x < xs; x++) {
            for (int y = 0; y < ys; y++) {
                sb.append(label[x][y] + "\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getKeyLabel(SwitchMatrixEvent event) {
        int[] matrix = event.getNewMatrix();
        int x = -1;
        int y = -1;
        for (y = 0; y < ys; y++) {
            if (matrix[y] != 0) {
                if ((matrix[y] & 0x01) == 0x01) x = 0;
                else if ((matrix[y] & 0x02) == 0x02) x = 1;
                else if ((matrix[y] & 0x04) == 0x04) x = 2;
                else if ((matrix[y] & 0x08) == 0x08) x = 3;
                else if ((matrix[y] & 0x10) == 0x10) x = 4;
                else if ((matrix[y] & 0x20) == 0x20) x = 5;
                else if ((matrix[y] & 0x40) == 0x40) x = 6;
                else if ((matrix[y] & 0x80) == 0x80) x = 7;
                break;    //found
            }
        }
        if (x != -1 && y != -1) {
            String retLabel = label[x][y];
            if (retLabel == null) {
                retLabel = "no label defined";
            }
            /*deb*/
            System.out.println("found (" + x + "," + y + "): " + retLabel);
            return retLabel;
        } else {
            return "no key pressed";
        }
    }
}
