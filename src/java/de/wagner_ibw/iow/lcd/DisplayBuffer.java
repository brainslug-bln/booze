/*
 * This File is part of the iowj-project   
 * $Id$
 * Copyright (C)2004-2008 by Thomas Wagner
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
 * Display buffer.
 *
 * @author Thomas Wagner
 * @since 0.9.6
 */
public class DisplayBuffer {

    /**
     * Display buffer.
     */
    private char[][] buffer;
    private int rows;
    private int cols;
    private AbstractLCD lcd = null;

    /**
     * Constructor.
     *
     * @param rows number of rows
     * @param cols number of cols
     */
    public DisplayBuffer(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        buffer = new char[rows][cols];
        clear();
    }

    /**
     * Contructor.
     *
     * @param lcd
     */
    public DisplayBuffer(AbstractLCD lcd) {
        this(lcd.getRows(), lcd.getCols());
        this.lcd = lcd;
    }

    /**
     * @param lcd
     */
    public void setLcd(AbstractLCD lcd) {
        this.lcd = lcd;
    }

    /**
     * @param row
     * @return
     */
    public String getLine(int row) {
        //do error handling!!!
        return new String(buffer[row]);
    }

    /**
     * Sets a char in the display buffer.
     *
     * @param row Row of the desired char position.
     * @param col Column of the desired char position.
     * @param c   Char to set.
     */
    public void setChar(int row, int col, char c) {
        //do error handling!!!
        buffer[row][col] = c;
    }

    /**
     * Sets a line in display buffer.
     *
     * @param row Row of the desired line.
     * @param str String to set.
     */
    public void setLine(int row, char[] str) {
        //do error handling!!!
        int len = str.length;
        for (int i = 0; i < len; i++) {
            buffer[row - 1][i] = str[i];
        }
    }

    /**
     * Clears the display buffer.
     */
    public void clear() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                buffer[j][i] = ' ';
            }
        }
    }

    /**
     * @throws IllegalStateException
     */
    public void write() throws IllegalStateException {
        if (lcd == null) {
            throw new IllegalStateException("LCD not set!");
        }
        for (int i = 0; i < rows; i++) {
            lcd.writeLine(i + 1, false, new String(buffer[i]));
        }
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < cols + 2; i++) {
            sb.append('-');
        }
        sb.append("\n");
        for (int i = 0; i < rows; i++) {
            sb.append('|');
            for (int j = 0; j < cols; j++) {
                sb.append(buffer[i][j]);
            }
            sb.append(("|\n"));
        }
        for (int i = 0; i < cols + 2; i++) {
            sb.append('-');
		}
		sb.append("\n");
		return sb.toString();
	}
}
