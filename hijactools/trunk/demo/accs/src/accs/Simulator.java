/* REVIEWED */
package accs;

import java.io.*;

import java.util.*;

/* I polished and improved the code of the original Simulator. */

public class Simulator implements Iterator<CarEvent> {
  public static final String DEFAULT_FILE = "SCENARIO";

  private CarEvent[] events;
  private int next = 0;

  public Simulator(String scenario) {
    File file = new File(scenario);
    if (!file.exists()) {
      System.out.println("FILE NOT FOUND <" + scenario + ">");
      events = new CarEvent[0];
    }
    else {
      try {
        System.out.println("READING FILE <" + scenario + ">");
        String content = readFile(file);
        StringTokenizer tokenizer = new StringTokenizer(content);
        int num_events = tokenizer.countTokens() / 2;
        events = new CarEvent[num_events];
        for (int index = 0; index < num_events; index++) {
          CarEventType type = parseEvent(tokenizer.nextToken());
          int delay = Integer.parseInt(tokenizer.nextToken());
          events[index] = new CarEvent(type, delay);
        }
      }
      catch (IOException e) {
        System.out.println(e);
        events = new CarEvent[0];
      }
    }
  }

  private static String readFile(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    StringBuilder contents = new StringBuilder();
    String sep = System.getProperty("line.separator");
    try {
      String line = reader.readLine();
      for (; line != null; line = reader.readLine()) {
        contents.append(line + sep);
      }
    }
    finally {
      reader.close();
    }
    return contents.toString();
  }

  private static CarEventType parseEvent(String event) {
    event = event.toUpperCase();
    for(CarEventType value : CarEventType.values()) {
      if (event.equals(value.toString())) {
        return value;
      }
    }
    throw
      new AssertionError("UNKOWN EVENT \"" + event + "\" IN SCENARIO FILE");
  }

  public synchronized CarEvent nextEvent() {
    return next();
  }

  /* Implementation of the Iterator<CarEvent> interface. */

  public synchronized boolean hasNext() {
    return next < events.length;
  }

  public synchronized CarEvent next() {
    if (next == events.length) {
      throw new NoSuchElementException();
    }
    return events[next++];
  }

  public synchronized void remove() {
    throw new UnsupportedOperationException();
  }

  /* Some simple testing code. */
  public static void main(String[] args) {
    String filename =
      (args.length >= 1 ? args[0] : DEFAULT_FILE);
    Simulator sim = new Simulator(filename);
    while (sim.hasNext()) {
      System.out.println(sim.next());
    }
  }
}
