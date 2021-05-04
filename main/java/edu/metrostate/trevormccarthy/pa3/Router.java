/**Router.java is to be in conjunction with the Packet and Routing classes to satisfy the objectives of programming
 * assignment 3. The Router class contains all the logic necessary to concurrently process packets using interrupt
 * communication between threads.                                                                                */

package edu.metrostate.trevormccarthy.pa3;
import java.util.LinkedList;

public class Router implements Runnable {
    private LinkedList<Packet> list = new LinkedList<Packet>();
    private int routes[];
    private Router routers[];
    private int routerNum;
    private boolean end = false;

    /**
     * @param rts Contains the routingTable's information specific to a particular Router.
     * @param rtrs A reference to the static table of routers on the network.
     * @param num The number associated with a particular router within the network.    */
    Router(int rts[], Router rtrs[], int num){
        routes = rts;
        routers = rtrs;
        routerNum = num;
    }

    /**
     * The addWork function adds a packet to the router. Although the packet is added to the tail
     * of the list, this may not necessarily mean that it won't be forwarded to the appropriate
     * router before another packet that is polled.
     * @param p The packet to be added.                                                        */
    public void addWork(Packet p) {
        synchronized(list){
            list.offerLast(p);                          // Add the packet to the tail of the list.
            // Condition signal is necessary here otherwise packet processing never starts as the threads are
            // started before any calls to addWork are made.                         
            list.notifyAll();
        }
    }

    /**
     * The end function is called by the Routing class indicating that there will be no more packets added to the network. */
    public synchronized void end() {
        // Awakens any threads waiting for a packet that will never arrive allowing it to fall through the run method's 
        // outermost while loop upon failing the end != true condition. 
        synchronized(list){
        end = true;
        list.notifyAll();
        }
    }

    /**
     * The networkEmpty function is called by the routing class indicating that there are no packets waiting to be processed
     * currently but the network could still receive more later.                                                          */
    public synchronized void networkEmpty() {
        synchronized(list){
        list.notifyAll();
        }
    }

    /**
     * The run function contains all the logic necessary to concurrently process packets. This                   */
    public void run() {
        Packet currentPkt = null;                   // References a packet in the LinkedList of packets requiring work. 
        // Threads (or "a thread" in the case of PA4) being run in each Router instance remain in this outermost loop
        // until the Routing class initiates a call to the Router class's end method.
        while (end != true){
            // While packets exist in the list, they are processed. 
            if (list.size() > 0){
                synchronized(list){
                    currentPkt = list.pollFirst();
                }
                currentPkt.record(routerNum);

                // If the packet being processed has arrived at the appropriate router, decrement the atomic counter. 
                // If not, use the packet's destination field and an addWork call to the router that the routingTable
                // lists as the closest path to the packet's destination.
                if (currentPkt.getDestination() == routerNum){Routing.decPacketCount();}
                else {routers[(routes[currentPkt.getDestination()])].addWork(currentPkt);}

                // When a thread makes it here, the packet has already been decremented or forwarded to the appropriate
                // router so we continue to process another packet.
                continue;
            }
            // Otherwise the thread goes into a waiting state. This applies for all 32 threads initially as the threads
            // are started before packets are added to the routers. 
            else {
                synchronized(list){
                    try {list.wait();}
                    catch (Exception ex){ex.printStackTrace();}
                }
            }
        }
    }
}