/* 
 * This File is part of the iowj-project   
 * $Id: SwitchMatrixEvent.java,v 1.4 2009/06/28 16:02:28 Thomas Wagner Exp $
 * Copyright (C)2005, 2009 by Thomas Wagner
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

import java.util.Vector;

/**
 * This class represents a single switch matrix event, received by an IO-Warrior.
 *
 * @author Thomas Wagner
 * @since 0.9.4
 */
public class SwitchMatrixEvent {

    private int row;
    private int col;
    private int[] oldMatrix = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] newMatrix = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /*
      * Length of the key matrix.
      * @since 0.9.7
      */
    private int matrixLength = 8;


    /*
      * Array of recently pressed key(s).
      * @since 0.9.6
      */
    private int[] keysPressed;

    /*
      * Array of recently released key(s).
      * @since 0.9.6
      */
    private int[] keysReleased;

    public SwitchMatrixEvent(int[] newMatrix, int[] oldMatrix) {

        matrixLength = newMatrix.length;

        for (int i = 0; i < matrixLength; i++) {
            this.newMatrix[i] = newMatrix[i];
            this.oldMatrix[i] = oldMatrix[i];

            if (newMatrix[i] != 0) {
                row = i + 1;
                if (newMatrix[i] == 0x1)
                    col = 1;
                else if (newMatrix[i] == 0x2)
                    col = 2;
                else if (newMatrix[i] == 0x4)
                    col = 3;
                else if (newMatrix[i] == 0x8)
                    col = 4;
                else if (newMatrix[i] == 0x10)
                    col = 5;
                else if (newMatrix[i] == 0x20)
                    col = 6;
                else if (newMatrix[i] == 0x40)
                    col = 7;
                else if (newMatrix[i] == 0x80)
                    col = 8;
            }
        }

        Vector pressedV = new Vector();
        Vector releasedV = new Vector();

        for (int y = 0; y < matrixLength; y++) {    //row loop
            int newRow = newMatrix[y];
            int oldRow = oldMatrix[y];
            for (int x = 0; x < 8; x++) {    //col loop
                //mask row
                int mask = (int) Math.pow(2, x);
                if (((newRow & mask) == mask) != ((oldRow & mask) == mask)) {
                    if ((newRow & mask) == mask) { //new is 1
                        pressedV.add(new Integer(y * 8 + x));
                    } else {
                        releasedV.add(new Integer(y * 8 + x));
                    }
                }
            }
        }

        keysPressed = new int[pressedV.size()];
        for (int i = 0; i < pressedV.size(); i++) {
            keysPressed[i] = ((Integer) pressedV.get(i)).intValue();
        }

        keysReleased = new int[releasedV.size()];
        for (int i = 0; i < releasedV.size(); i++) {
            keysReleased[i] = ((Integer) releasedV.get(i)).intValue();
        }
    }

    /**
     * Returns the row number of the pressed or released key.
     * Valid if only one key was pressed or released.
     *
     * @return number of row (0 ... 7)
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column number of the pressed or released key.
     * Valid if only one key was pressed or released.
     *
     * @return number of column (0 ... 15)
     */
    public int getCol() {
        return col;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SwitchMatrix[Row[");
        sb.append(row);
        sb.append("],Col[");
        sb.append(col);
        sb.append("]]");
        sb.append("\tSMXnew[");
        for (int i = matrixLength - 1; i > -1; i--) {
            sb.append(newMatrix[i]);
            if (i > 0)
                sb.append(",");
            else
                sb.append("]");
        }
        sb.append("\tSMXold[");
        for (int i = matrixLength - 1; i > -1; i--) {
            sb.append(oldMatrix[i]);
            if (i > 0)
                sb.append(",");
            else
                sb.append("] pressed[");
        }
        if (keysPressed.length == 0) {
            sb.append("] released[");
        }
        for (int i = keysPressed.length; i > 0; i--) {
            sb.append(keysPressed[i - 1]);
            if (i > 1)
                sb.append(",");
            else
                sb.append("] released[");
        }
        for (int i = keysReleased.length; i > 0; i--) {
            sb.append(keysReleased[i - 1]);
            if (i > 1)
                sb.append(",");
            else
                sb.append("]");
        }
        if (keysReleased.length == 0) {
            sb.append("]");
        }
        return sb.toString();
    }

    /**
     * @return New (after key press) matrix array of int.
     */
    public int[] getNewMatrix() {
        return newMatrix;
    }

    /**
     * @return Old matrix (before key press) array of int.
     */
    public int[] getOldMatrix() {
        return oldMatrix;
    }

    /**
     * @return recently pressed key(s) array of int.
     * @since 0.9.6
     */
    public int[] getPressedKeys() {
        return keysPressed;
    }

    /**
     * @return recently released key(s) array of int.
     * @since 0.9.6
     */
    public int[] getReleasedKeys() {
        return keysReleased;
    }

    /**
     * @return the key matrix length. 8 for SMX or 16 for SMX16
     * @since 0.9.7
     */
    public int getMatrixLength() {
		return matrixLength;
	}

}
