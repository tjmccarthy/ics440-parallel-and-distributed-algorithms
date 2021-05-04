package edu.metrostate.trevormccarthy.pa2;

public class PA2Main {
    public static boolean debug = false;                          // Used to get information about theQ's current state.

    public static void main(String[] args) {
        GenericQueue<Integer> collection = new GenericQueue<>();  // Instance of queue performing the operations.
        RequestProcessor processor;
        int totalMMs = 100000000;                                 // Total M&Ms and iteration count for main loop.
        int totalThreads = 5;                                     // Total threads used to process requests.

        for (int index = 0; index < totalMMs; index++) {
            int mm = ((int) (Math.random() * totalMMs)) % 5;
            Integer integ = new Integer(mm);
            collection.enqueue(integ);
        }
        processor = new RequestProcessor(collection);
        Thread[] threads = new Thread[totalThreads];

        for (int index = 0; index < threads.length; index++) {
            threads[index] = new Thread(processor);
            threads[index].start();
        }

        // Perform join on all 5 threads until all have been completed.
        try {
            for (int index = 0; index < threads.length; index++) {
                threads[index].join();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        ThreadStatisticsSetup.print();
    }
}