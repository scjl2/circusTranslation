/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
package cdx;

import collision.Vector3d;

/**
 * Represents a definite collision that has occured.
 * 
 * @author Filip Pizlo
 */
class Collision {
    /** The aircraft that were involved. */
    // private ArrayList aircraft;

    private Aircraft _one, _two;

    /** The location where the collision happened. */
    private Vector3d location;

    /** Construct a Collision with a given set of aircraft and a location. */

    /** Construct a Coollision with two aircraft an a location. */
    public Collision(Aircraft one, Aircraft two, Vector3d locationArg) {
        location = locationArg;
        _one = one;
        _two = two;
    }

    public boolean hasAircraft(Aircraft a) {
        if (_one.equals(a)) return true;
        if (_two.equals(a)) return true;
        return false;
    }

    public Aircraft first() {
        return _one;
    }

    public Aircraft second() {
        return _two;
    }



    /** Returns the location of the collision. You are not to modify this location. */
    public Vector3d getLocation() {
        return location;
    }

    /** Returns a hash code for this object. It is based on the hash codes of the aircraft. */

    public int hashCode() {
        int ret = 0;
        ret += _one.hashCode();
        ret += _two.hashCode();
        return ret;
    }

    /** Determines collision equality. Two collisions are equal if they have the same aircraft. */

    public boolean equals(Object _other) {
        if (_other == this) return true;
        if (!(_other instanceof Collision)) return false;
        Collision other = (Collision) _other;
        if (_one != other._one) return false;
        if (_two != other._two) return false;
        return true;
    }

    /** Returns a helpful description of this object. */

    public String toString() {
        StringBuffer buf = new StringBuffer("Collision between ");
        boolean first = true;
        buf.append(_one.toString() + ", " + _two.toString());
        buf.append(" at ");
        buf.append(location.toString());
        return buf.toString();
    }
}
