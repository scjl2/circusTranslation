package javax.realtime;

import static javax.safetycritical.annotate.Level.LEVEL_0;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;


//@SCJAllowed(value = LEVEL_0, members = true)
public interface RawIntegralAccess {


  //@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
  public byte getByte(long offset);
/*         throws javax.realtime.OffsetOutOfBoundsException,
                javax.realtime.SizeOutOfBoundsException;*/
  

  //@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
  public void getBytes(long offset, byte[] bytes, int low, int number);
/*         throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
         
  
  //@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
  public void setByte(long offset, byte value);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
         
     
  //@SCJRestricted(mayAllocate = false, maySelfSuspend = false)
  public void setBytes(long offset, byte[] bytes, int low, int number);
   /*      throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
  

  /*
   * Andy had added this in (by accident) and then asked for it to be
   * removed.  It appears that all of this is still in flux.
   *
   * public void setLong(long i);
   * public long getLong();
   */

//  @SCJAllowed(LEVEL_1)
//  public int getInt(long offset);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
                javax.realtime.SizeOutOfBoundsException;*/
  
//  @SCJAllowed(LEVEL_1)
//  public void getInts(long offset, int[] ints, int low, int number);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
         
//  @SCJAllowed(LEVEL_1)
//  public long getLong(long offset);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
                javax.realtime.SizeOutOfBoundsException;*/
  
//  @SCJAllowed(LEVEL_1)
//  public void getLongs(long offset, long[] longs, int low, int number);
 /*        throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/

//  @SCJAllowed(LEVEL_1)
//  public short getShort(long offset);
 /*        throws javax.realtime.OffsetOutOfBoundsException,
                javax.realtime.SizeOutOfBoundsException;*/
                
//  @SCJAllowed(LEVEL_1)
//  public void getShorts(long offset, short[] shorts, int low, int number);
   /*      throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
      
//  @SCJAllowed(LEVEL_1)
//  public void setInt(long offset, int value);
   /*      throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
//  @SCJAllowed(LEVEL_1) 
//  public void setInts(long offset, int[] its, int low, int number);
   /*      throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/

//  @SCJAllowed(LEVEL_1)
//  public void setByte(long offset, long value);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
     
//  @SCJAllowed(LEVEL_1)    
//  public void setLongs(long offset, long[] longs, int low, int number);
   /*      throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
  
//  @SCJAllowed(LEVEL_1)   
//  public void setShort(long offset, short value);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
  
//  @SCJAllowed(LEVEL_1)  
//  public void setShorts(long offset, short[] shorts, int low, int number);
  /*       throws javax.realtime.OffsetOutOfBoundsException,
         javax.realtime.SizeOutOfBoundsException;*/
}
