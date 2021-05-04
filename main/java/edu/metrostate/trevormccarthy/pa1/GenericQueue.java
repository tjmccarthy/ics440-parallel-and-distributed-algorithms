/**The GenericQueue class is to be used in conjunction with the PA1Main class for Programming Assignment #1. The
 * assignment requires the development of a single-threaded, unbounded generic queue using our own implementation.
 * The class must have all functions needed to determine the queue's state, dequeue/enqueue etc. The program should 
 * perform 1000 of these operations to demonstrate correctness. The output for the program
 * as it iterates 1000 times should be similar to the following:
 * Dequeue - failed (queue empty).
 * Enqueue(3)
 * Enqueue(7)
 * Dequeue() = 3                                                                                                 */

package edu.metrostate.trevormccarthy.pa1;
import java.util.Iterator;

public class GenericQueue<Generic> implements Iterable<Generic> {
    public static boolean debug = false;
    private GenericNode<Generic> first;
    private GenericNode<Generic> last;


    public Iterator<Generic> iterator() {
        return new GenericQueueIterator();
    }

    class GenericQueueIterator implements Iterator<Generic> {
        private GenericNode<Generic> current = first;

        public boolean hasNext() {
            return (current != null);
        }

        public Generic next() {
            Generic element = current.getElement();
            current = current.getNext();
            return element;
        }
    }

    public GenericQueue() {
        first = null;
        last = null;
    }

    /**
     * The getFirst method returns a reference to the GenericNode that is the front of the queue.
     * @return first The GenericNode that is referenced by the "first" variable.               */
    public GenericNode getFirst() {
        return first;
    }

    /**
     * The getLast method returns a reference to the GenericNode that is the back of the queue.
     * @return last The GenericNode that is referenced by the "last" variable.               */
    public GenericNode getLast() {
        return last;
    }

    /**
     * The getNode method returns the GenericNode that matches the index.
     * @param index The number associated with where the GenericNode is located within the GenericQueue.
     * @return ref The GenericNode matching the index parameter.                                      */
    public GenericNode getNode(int index) {
        GenericNode ref = first;
        if (index >= 0 && first != null) {
            for (int i = 0; i < index; i++) ref = ref.next;
        }
        return ref;
    }

    /**
     * The size method recursively calls its overloaded counterpart.
     * @return size(first) Recursive call passing the "first" variable referencing the first GenericNode.  */
    public int size() {
        return size(first);
    }

    /**
     * This size method returns the total number of GenericNodes in the queue.
     * @return size(sizer.next) + 1 Returns the total number of elements in the GenericQueue object.       */
    public int size(GenericNode sizer) {
        if (sizer == null) return 0;
        return size(sizer.next) + 1;
    }

    /**
     * The toString method returns information about what is contained in the GenericQueue object.
     * @return str The string containing the GenericLinkedList content information.             */
    public String toString() {
        String str = "GenericQueue state:\n";
        GenericNode current;
        GenericNode next = first;

        while (next != null) {
            current = next;
            next = current.getNext();
            str += "[" + current + "]";
        }
        str += "";
        return str;
    }

    /**
     * The isEmpty() method checks the GenericQueue's front node to determine if the list is empty or not.
     * @return true if the GenericNode referencing the front node is empty, otherwise false.            */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * The copy() method creates a deep copy of the queue passed.
     * @param oldQueue The list object that is being copied.
     * @return newQueue The list referencing the newly copied GenericQueue object.                     */
    public GenericQueue<Generic> copy(GenericQueue<Generic> oldQueue) {
        GenericQueue<Generic> newQueue = new GenericQueue<>();
        for (Generic element : oldQueue) newQueue.enqueue((Generic) element);
        return newQueue;
    }

    /**
     * The enqueue method adds the GenericNode passed in the parameter to the first and last nodes if the GenericQueue
     * is empty and to last GenericNode and its "next reference if first is not null.                               */
    public void enqueue(Generic element) {
        GenericNode<Generic> addedNode = new GenericNode<>(element);
        if (first == null) {
            first = addedNode;
            last = addedNode;
        } else {
            last.updateNext(addedNode);
            last = addedNode;
        }
    }

    /**
     * The dequeue() function checks the GenericQueue's first to see if it has a next GenericNode linked to it.
     * If so, remove() is called with the contents of first as the argument. The remove() function preforms the
     * necessary de-referencing and updates the Queue's node order.
     * @return Returns the element stored in the dequeued node.                                             */
    public Generic dequeue() {
        Generic element;
        if (first == null) {
            element = first.getElement();
            return element;
        }
        if (first != last) {
            element = first.getElement();
            first = first.getNext();
            return element;
        }
        element = first.getElement();
        first = null;
        last = null;
        return element;
    }

    /**
     * The remove() method allows the GenericQueue to remove the GenericNode at the desired index by calling it's
     * overloaded remove() counterpart. This maintains the integrity of the GenericQueue's structure.
     * @param indexOfRemoveMe The index where the GenericNode to remove resides.
     * @return true After removal of the GenericNode; false if the GenericNode's index is a negative integer.        */
    public boolean remove(int indexOfRemoveMe) {
        if (indexOfRemoveMe < 0) return false;

        // First node's next is updated to be the new first.
        if (indexOfRemoveMe == 0) return remove(first);
        GenericNode<Generic> nextNode = first;

        while (indexOfRemoveMe != 0) {
            indexOfRemoveMe--;
            if (!nextNode.hasNext()) return false;
            nextNode = nextNode.getNext();
        }
        return remove(nextNode);
    }

    /**
     * This remove() function serves as a helper to the index based remove() method.
     * @param removeMe The GenericNode object to be removed.
     * @return true After removal of the GenericNode; false if the GenericNode's index is a negative integer.   */
    public boolean remove(GenericNode<Generic> removeMe) {
        if (removeMe == null) return false;
        if (removeMe.equals(first)) {
            first = first.getNext();
            removeMe.updateNext(null);
            return true;
        }
        GenericNode<Generic> nextNode = first;
        while (nextNode.getNext() != null) {

            if (nextNode.getNext().equals(removeMe)) {
                nextNode.updateNext(removeMe.getNext());
                removeMe.updateNext(null);
                return true;
            }
            nextNode = nextNode.getNext();
            System.out.println(nextNode);
        }
        return false;
    }
    /**
     * The removeLast() function severs the last GenericNode object from the GenericQueue.  */
    public void removeLast() {remove(last);}

    /**
     * The makeEmpty() method removes all pointer references for any GenericNode contained in the GenericQueue.  */
    public void makeEmpty() {
        int nodeCounter = 0;
        if (debug)
            System.out.println("GenericQueue.makeEmpty() status: " + ((first == null) ? "Empty" : "Not Empty"));

        GenericNode<Generic> currentOne;
        GenericNode<Generic> nextOne = first;

        while (nextOne != null) {
            currentOne = nextOne;
            nextOne = currentOne.getNext();
            if (debug) System.out.println("Found node #" + (++nodeCounter) + " as " + currentOne);

            currentOne.updateNext(null);
        }
        if (debug) System.out.println("makeEmpty() complete. " + nodeCounter + " nodes set for garbage collection.");
        first = null;
    }

    /**
     * The GenericNode is the data structure representing the elements that are composed to form the Queue. */
    public class GenericNode<Generic> {
        private Generic element;
        private GenericNode<Generic> next;

        /**
         * This constructor stores a generic object containing values while leaving the next instance variable null.
         * @param element The element stored in the node.                                                         */
        public GenericNode(Generic element) {
            this.element = element;
            next = null;
        }

        /**
         * This constructor stores a generic object containing values and uses the GenericNode passed to assigns a
         * reference next.
         * @param next The GenericNode object that is associated with this objects link.
         * @param element The element to be stored in the node.                                                      */
        public GenericNode(GenericNode<Generic> next, Generic element) {
            this.element = element;
            this.next = next;
        }

        /**
         * The toString method returns details about the element being stored in the GenericNode object.
         * @return str The string representation of the element's properties.                         */
        public String toString() {
            String str = "";
            str += element;
            return str;
        }

        /**
         * The getNext function returns a reference to the GenericNode that is next on the list.
         * @return next The object referencing the current GenericNode's successor.           */
        public GenericNode<Generic> getNext() {return next;}

        /**
         * The getElement method returns a reference to the GenericNode's element.
         * @return element The contents of associated GenericNode.              */
        public Generic getElement() {return element;}

        /**
         * The hasNext method checks the current GenericNode to see if it is linked to another with the "next" variable.
         * @return true if the GenericNode is linked to another GenericNode; otherwise false.                        */
        public boolean hasNext() {return (next != null);}

        /**
         * The updateNext method uses the parameter passed to create a reference to the "next" GenericNode.
         * @param next The GenericNode object that will be referenced by the current node as "next."     */
        public void updateNext(GenericNode<Generic> next) {this.next = next;}

        /**
         * The setElement method assigns the Generic argument passed to the GenericNode's element variable.
         * @param element The object referencing the element within the GenericNode.                     */
        public void setElement(Generic element) {this.element = element;}
    }
}