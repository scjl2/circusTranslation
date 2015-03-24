/**
 * @author Frank Zeyda, Kun Wei
 */
package cdx;

import javax.safetycritical.Mission;
import javax.safetycritical.AperiodicEvent;

/**
 * The single mission of the parallel CDx.
 *
 * The seven handlers of the mission are
 *   InputFrameHandler,
 *   ReducerHandler,
 *   DetectorHandler (four instances), and OutputCollisionsHandler.
 */
public class CDxMission extends Mission {
  /**
   * Records the current radar frame.
   */
  public RawFrame currentFrame;

  /**
   * Records previous aircraft positions.
   */
  public StateTable state;

  /**
   * Records a partition of the computational work as it is distributed
   * between the parallel detection handlers.
   */
  public Partition work;

  /**
   * Records the number of collisions calculated by the detector handlers.
   */
  public int collisions;

  /**
   * Control object used to orchestrate execution of the handlers.
   */
  public DetectorControl control;

  public CDxMission() {
    currentFrame = new RawFrame();
    state = new StateTable();
    work = new Partition(4);
    collisions = 0;
  }

  public void initialize() {
    /* The SCJ event reduce releases ReducerHandler. */
    AperiodicEvent reduce = new AperiodicEvent();

    /* The SCJ event detect releases all DetectorHandlers. */
    AperiodicEvent detect = new AperiodicEvent();

    /* The SCJ event output releases OutputCollisionsHandler. */
    AperiodicEvent output = new AperiodicEvent();

    control = new DetectorControl(output, 4);

    /* Note that the handler constructors have an additional parameter for
     * the bound event; such is only implicit in the SCJ Circus models. */ 

    /* InputFrameHandler reads radar frames; the only periodic handler. */
    InputFrameHandler h1 = new InputFrameHandler(this, reduce);

    /* ReducerHandler performs voxel hashing and division of work. */
    ReducerHandler h2 = new ReducerHandler(this, detect, control, reduce);

    /* DetectorHandler instances carry out the actual detection work. */
    DetectorHandler h3 = new DetectorHandler(this, control, 1, detect);

    /* DetectorHandler instances carry out the actual detection work. */
    DetectorHandler h4 = new DetectorHandler(this, control, 2, detect);

    /* DetectorHandler instances carry out the actual detection work. */
    DetectorHandler h5 = new DetectorHandler(this, control, 3, detect);

    /* DetectorHandler instances carry out the actual detection work. */
    DetectorHandler h6 = new DetectorHandler(this, control, 4, detect);

    /* OutputCollisionsHandler outputs the collisions results. */
    OutputCollisionsHandler h7 = new OutputCollisionsHandler(this, output);

    /* Register all handlers with the mission. */
    h1.register();
    h2.register();
    h3.register();
    h4.register();
    h5.register();
    h6.register();
    h7.register();
  }

  public long missionMemorySize() {
    return Constants.MISSION_MEMORY_SIZE;
  }

  /**
   * Method to get the current frame object.
   */
  public RawFrame getFrame() {
    return currentFrame;
  }

  /**
   * Method to set the current frame object.
   */
  public void setFrame(RawFrame frame) {
    currentFrame = frame;
  }

  /**
   * Method to get the shared partition variable.
   */
  public StateTable getState() {
    return state;
  }

  /**
   * Method to set the shared partition variable.
   */
  public void setState(StateTable stateArg) {
    state = stateArg;
  }

  /**
   * Method to get the shared work variable.
   */
  public Partition getWork() {
    return work;
  }

  /**
   * Method to set the shared work variable.
   */
  public void setWork(Partition workArg) {
    work = workArg;
  }

  /**
   * This method initialises the number of collisions by setting it to 0.
   */
  public synchronized void initColls() {
    collisions = 0;
  }

  /**
   * This method records a partial collisions result as it is generated by the
   * detection handlers.
   *
   * @param n Number of collisions to be recorded.
   */
  public synchronized void recColls(int n) {
    collisions += n;
  }

  /**
   * This method returns the cumulative number of collisions recorded.
   */
  public synchronized int getColls() {
    return collisions;
  }
}
