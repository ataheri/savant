/*
 *    Copyright 2010 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*
 * PointTest.java
 * Created on Jan 7, 2010
 */

package savant.data.types;

import junit.framework.TestCase;
import savant.data.types.Point;

/**
 * A test to test the basic functions of Point, e.g. construction, equals, hashCode, and toString
 *
 * @author vwilliams
 */
public class PointTest extends TestCase {

    private Point a, b, c, d;

    public void setUp() {

        a = Point.valueOf(10);
        b = Point.valueOf(10);
        c = Point.valueOf(10);
        d = Point.valueOf(20);

    }

    public void testConstruct() {
        try {
            // This point is invalid and should fail
            Point e = Point.valueOf(-1);
            fail("Expected IllegalArgumentException.");
        } catch (Exception success) {}
        try {
            // This point is invalid and should fail
            Point e = Point.valueOf(10);
            fail("Expected IllegalArgumentException.");
        } catch (Exception success) {}
    }
    
    public void testEquals() {

        try {
            // reflexivity: A = A
            assertTrue(a.equals(a));

            // symmetry: A = B & B = A
            assertTrue(a.equals(b));
            assertTrue(b.equals(a));

            // transitivity: A = B & B = C & A = C
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));

            // inequality: A <> D
            assertFalse(a.equals(d));

            // A <> null
            assertFalse(a.equals(null));

            // A <> object of another type
            assertFalse(a.equals(new Object()));

        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }

    }

    public void testHashCode() {

        try {
            // objects which are equal have same hash
            assertEquals(a.hashCode(), b.hashCode());

            // unequal objects have unequal hashes
            assertFalse(a.hashCode() == d.hashCode());
        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }

    public void testToString() {

        // make sure no null pointers are thrown
        try {
            a.toString();
        }
        catch(Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }
}