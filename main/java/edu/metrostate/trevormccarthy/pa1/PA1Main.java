/**Main method for a single-threaded, unbounded generic queue implementation.                                   */
package edu.metrostate.trevormccarthy.pa1;

public class PA1Main {
    public static void main(String[] args) {
        GenericQueue theQ = new GenericQueue();             // Instance of queue performing the operations.
        int totalOps = 1000;                                // Control variable for outer loop.
        int randBehavior = 0;                               // Variable used for random behavior enqueue/dequeue.

        // Contents enqueued will be determined by iteration number.
        // Begin GenericQueue functionality demonstration.
        for (int i = 0; i < totalOps; i++) {
            randBehavior = (int) (100 * Math.random() % 2);    // 0 for enqueue and 1 for dequeue.

            if (randBehavior == 0) {
                System.out.println("Enqueue(" + i + ")");
                theQ.enqueue(new Integer(i));
                continue;
            }
            if (theQ.isEmpty() == false && randBehavior == 1) {
                System.out.println("Dequeue() = " + theQ.dequeue());
                continue;
            } else {
                System.out.println("Dequeue failed - (queue empty).");
            }
        }
        if (!theQ.isEmpty()) theQ.makeEmpty();
    }
}