/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008-2012, Martin Schoeberl (martin@jopdesign.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package examples.safetycritical;

import java.io.IOException;
import java.io.OutputStream;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.*;

/**
 * Test nested private memories
 * 
 * @author Martin Schoeberl
 * 
 */
public class MySafelet implements Safelet {

	public MissionSequencer getSequencer() {
		StorageParameters sp = new StorageParameters(100000, 1000, 1000);
		return new MyMissionSequencer(new PriorityParameters(13), sp);
	}

	public long immortalMemorySize() {
		return 1000;
	}

    public void setUp () {}

    public void tearDown() {}
}
