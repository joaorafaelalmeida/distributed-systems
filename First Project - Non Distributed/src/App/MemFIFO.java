package App;
/**
 *    General description:
 *       definition of a FIFO memory of generic objects.
 *       It extends a generic memory data type.
 */

public class MemFIFO<R> extends MemObject<R>
{
  /**
   *  Characterization of the FIFO access discipline
   */

   private int inPnt,                                      // insertion pointer
               outPnt;                                     // retrieval pointer
   private boolean empty;                                  // signaling empty memory

  /**
   *  Constructor
   *
   *    @param storage storage area
   */

   public MemFIFO (R [] storage)
   {
     super (storage);
     inPnt = outPnt = 0;
     empty = true;
   }

  /**
   *  Writing a value in mutual exclusion regime.
   *
   *    @param val value to store
   */

   @Override
   public void write (R val)
   {
     if ((inPnt != outPnt) || empty)
        { mem[inPnt] = val;
          inPnt = (inPnt + 1) % mem.length;
          empty = false;
        }
   }

  /**
   *  Reading a value in mutual exclusion regime.
   *
   *    @return the retrieved value
   */

   @Override
   public R read ()
   {
     R val = null;                                         // retrieved value

     if (!empty)
        { val = mem[outPnt];
          outPnt = (outPnt + 1) % mem.length;
          empty = (inPnt == outPnt);
        }

     return val;
   }
}
