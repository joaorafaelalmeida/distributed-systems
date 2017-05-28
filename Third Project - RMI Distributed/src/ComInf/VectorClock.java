
package ComInf;

import java.io.Serializable;


public class VectorClock implements Serializable
{
    /**
     * Logic Scalar Clocks of the different threads.
     */
    public int timestamp[];

    /**
     * ID of the thread that holds this VCK.
     */
    public int id;
    
    /**
     * Access in regime of mutual exclusion 
     */
    private final Semaphore access;
    
    /**
     * Constructor of the LogicVectorClock data type.
     *
     * @param size number of processes
     * @param id ID of the process that instantiates it
     */
    public VectorClock(int size, int id) 
    {
       	timestamp = new int[size];
        this.id = id;
        access = new Semaphore();
        access.up();
    }

    /**
     * Increment the value of the clock
     */
    public void increment() 
    {
        access.down();
        timestamp[id]++;
        access.up();
    }

    /**
     * Update the clock value
     *
     * @param clock another clock
     */
    public void update(VectorClock clock) 
    {
        access.down();
        id = clock.id;
        for (int i = 0; i < timestamp.length; i++) 
            if (!(timestamp[i] > clock.timestamp[i]))
                timestamp[i] = clock.timestamp[i];
        access.up();
    }
    
    public VectorClock getCopy() 
    {
        access.down();
        VectorClock copy = new VectorClock(timestamp.length, id);
        for (int i = 0; i < timestamp.length; i++)
            copy.timestamp[i] = timestamp[i];
        access.up();
        return copy;
    }
    
    public int[] getVectorClock()
    {
        return timestamp;
    }
    
    /**
     * Increment the value of the clock
     * @param id id
     */
    public void increment(int id) 
    {
        access.down();
        timestamp[id]++;
        access.up();
    }
    
    @Override
    public String toString()
    {
        String tmp = "";
        for (int i = 0; i < timestamp.length; i++)
            tmp += String.format("%3d", timestamp[i]);
        return tmp;
    }
    
}
