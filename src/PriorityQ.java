/*  Student information for assignment:
 *
 *  On OUR honor, Alex OR and <NAME2), this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used: 0
 *
 *  Student 1 (Student whose Canvas account is being used)
 *  UTEID: amo3635
 *  email address: amo3635@my.utexas.edu
 *  Grader name: Jason
 *
 *  Student 2
 *  UTEID:
 *  email address:
 *
 */

import java.util.ArrayList;

//implementation of a generic priority queue using an arraylist as the internal storage 
// container, the front of the arraylist is the back of the queue and the back of the arraylist 
// is the front of the queue. This queue breaks ties fairly, so if an element of equal priority 
// is enqueued, it is put behind elements of equal priority that have been in the queue for longer.
public class PriorityQ<E extends Comparable<E>> {

    private ArrayList<E> con;

    //pre: none
    //post: creates new empty priority queue
    public PriorityQ() {
        con = new ArrayList<>();
    }

    //pre: element != null
    //post: adds element in correct spot based on its frequency, breaking ties fairly based on
    // which element was there first.
    public void enQueue(E element) {
        //check precondition
        if(element == null) {
            throw new IllegalArgumentException("element to Enqueue cannot be null.");
        }
        boolean added = false;
        int ogSize = con.size();
        int index = 0;
        //search for spot to add not at end of queue
        while(index < con.size() && !added) {
            if (element.compareTo(con.get(index)) >= 0) {
                con.add(index, element);
                added = true;
            }
            index++;
        }
        //add if queue empty or need to add at end.
        if(ogSize == con.size()) {
            con.add(element);
        }
    }

    //pre: none
    //post: queue is empty
    public void clear() {
        con = new ArrayList<>();
    }

    //pre: size() != 0
    //post: returns element at front
    public E deQueue() {
        //check precondition
        if(size() == 0) {
            throw new IllegalStateException("Queue cannot be empty when deQueueing");
        }
        return con.remove(con.size() - 1);
    }

    //pre: none
    //post: returns con.size()
    public int size() {
        return con.size();
    }

    //pre: none
    //post: returns true if queue isEmpty, false otherwise
    public boolean isEmpty() {
        return con.size() == 0;
    }

    //pre:none
    //post: prints out queue
    public void printQ() {
        System.out.print("[");
        for (int i = con.size() - 1; i >= 0; i --) {
            E element = con.get(i);
            System.out.print("(" + element.toString()+ "), ");
        }
        System.out.println("]");
    }
}
