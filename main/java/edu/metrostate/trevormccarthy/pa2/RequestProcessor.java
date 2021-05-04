/**The RequestProcessor class uses ThreadLocal when counting to get and update the thread's local value. 
                                                                                                      */
package edu.metrostate.trevormccarthy.pa2;
import java.util.concurrent.locks.ReentrantLock;

public class RequestProcessor extends Thread {
    private GenericQueue collection;           // Individual threads private area to store color counts.

    // Concurrency measure through requiring each thread to get a lock prior to reading and writing.
    private ReentrantLock collectionLock = new ReentrantLock(); // Each thread acquires before accessing the collection.
    private ReentrantLock outputListLock = new ReentrantLock(); // Threads acquire prior to storing output.

    // Get an instance of the ThreadStatisticsSetup as a local copy.
    private static final ThreadLocal<ThreadStatisticsSetup> localCollection = new ThreadLocal<ThreadStatisticsSetup>() {
        protected ThreadStatisticsSetup initialValue() {
            return new ThreadStatisticsSetup();
        }
    };

    //public static ThreadStatisticsSetup get() {return localCollection.get();}

    public RequestProcessor(GenericQueue collection) {
        this.collection = collection;
    }

    public void run() {
        boolean flag;
        Integer element = null;
        while (!collection.isEmpty()) {
            flag = false;
            collectionLock.lock();

            try {
                GenericQueue.GenericNode currentNode = collection.getFirst();
                element = (Integer) currentNode.getElement();
                flag = true;
                collection.dequeue();
            } catch (Exception e) {
            } finally {
                collectionLock.unlock();
            }
            // Uses a ThreadStatisticsSetup method on the element
            if (flag) {
                localCollection.get().updateStatistics(element);
            }
        }
        localCollection.get().mergeResults();
    }
}