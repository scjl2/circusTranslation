/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import javacp.util.ArrayList;
import javacp.util.HashSet;
import javacp.util.List;

/**
 * Collects collisions in lists and then returns a list of collisions where no two are equal.
 * 
 * @author Filip Pizlo
 */
public class CollisionCollector {
    /** A hash set of collisions. */
    private HashSet collisions = new HashSet();

    /** Add some collisions. */
    public void addCollisions(List collisions) {
    }

    /** Get the list of collisions. */
    public ArrayList getCollisions() {
        return new ArrayList(collisions);
    }
}
