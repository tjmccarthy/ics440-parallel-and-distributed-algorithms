/**The Packet class is to be used in conjunction with the Routing and Router classes to implement a system of
 * networked routers. Packet instances with random source/destination values (0-31) are created in the main
 * method and are assigned to their appropriate source router. The Router class processes these packets
 * concurrently in order for them to efficiently reach their destination router.                           */

package edu.metrostate.trevormccarthy.pa3;
import java.util.LinkedList;

public class Packet {
    private int destination;                                        // The final router in the packet's traversal.
    private int source;                                             // The origin router.
    private LinkedList<Integer> path = new LinkedList<Integer>();   // The order of traversal.

    /* Instantiate a packet, given source and destination. */
    Packet(int s, int d) {
        source = s;
        destination = d;
    }

    /* Return the packet's source. */
    public int getSource() {return source;}

    /* Return the packet's destination. */
    public int getDestination() {return destination;}

    /* Record the current location as the packet traverses the network. */
    public void record(int router) {path.add(router);}

    /* Print the route the packet took through the network. */
    public void printRoute() {
        System.out.println("Packet source=" + source + " destination=" + destination);
        System.out.print("    path: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(" " + path.get(i));
        }
        System.out.println();
    }
}