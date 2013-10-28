/**
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package savant.util;

/**
 *
 * @author tarkvara
 */
public enum DrawingMode {
    // Interval modes
    SQUISH,
    PACK,
    ARC,
    
    // BAM modes
    STANDARD,
    MISMATCH,
    SEQUENCE,
    STANDARD_PAIRED,
    ARC_PAIRED,
    SNP,
    STRAND_SNP,
    
    // Variant modes
    MATRIX,
    FREQUENCY;

    public String getDescription() {
        switch (this) {
            case SQUISH:
                return "Squish";
            case PACK:
                return "Pack";
            case ARC:
                return "Arc";
            case STANDARD:
                return "Standard";
            case MISMATCH:
                return "Mismatch";
            case SEQUENCE:
                return "Read Sequence";
            case STANDARD_PAIRED:
                return "Read Pair (Standard)";
            case ARC_PAIRED:
                return "Read Pair (Arc)";
            case SNP:
                return"SNP";
            case STRAND_SNP:
                return "Strand SNP";
            case MATRIX:
                return "Participants";
            case FREQUENCY:
                return "Frequency";
        }
        return null;
    }
}
