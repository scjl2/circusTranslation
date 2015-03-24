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
import javax.realtime.MemoryArea;
import javax.safetycritical.*;

/**
 * Test nested private memories
 * 
 * @author Martin Schoeberl
 * 
 */
public class Handler extends PeriodicEventHandler {

    int cnt;

    Object o = new Object();

    public Handler () {
		super (new PriorityParameters(11),
        new PeriodicParameters(new RelativeTime(0, 0), new RelativeTime(500, 0)),
		new StorageParameters(10000, 1000, 1000), 500);
    }

	public void handleEvent() {
		System.out.println("Ping " + cnt);
		++cnt;
		MyRunnable r = new MyRunnable();

		// Generate more garbage
		for (int i=0; i<10; ++i) {
			// this would generate too much garbage in the initial private memory
			// r.run();
            ManagedMemory m = (ManagedMemory) MemoryArea.getMemoryArea(this);
			m.enterPrivateMemory(500, r);
		}
		if (cnt > 5) {
			// getCurrentMission is not yet working
            Mission.getCurrentMission().requestTermination();
		}

        o = new Object();

	}

	public final void register() { }

	public StorageParameters getThreadConfigurationParameters() {
		return null;
	}

}


class MyRunnable implements Runnable {
	public void run() {
		for (int i=0; i<3; ++i) {
            Object temp = new Object();
			System.out.print(" iter " + i);							
		}
	}
}
