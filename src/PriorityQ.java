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

//todo do i have to comment this class
import java.util.ArrayList;

public class PriorityQ {
    private ArrayList<TreeNode> con;

    public PriorityQ() {
        con = new ArrayList<>();
    }

    // todo do we need to prioritize certain vals? tie breaker ambiguity if not
    public void enQueue(TreeNode newNode) {
        //todo when empty, when not empty, when not empty and we don't find place to add
        int ogSize = con.size();
        for (int i = 0; i < con.size(); i++) {
            if (newNode.getFrequency() >= con.get(i).getFrequency()) {
                con.add(i, newNode);
                break;
                //todo can we break jason
            }
        }
        if(ogSize == con.size()) {
            con.add(newNode);
        }
    }


    public TreeNode deQueue() {
        return con.remove(con.size() - 1);
    }

    public int size() {
        return con.size();
    }

    public boolean isEmpty() {
        return con.size() == 0;
    }

    public void printQ() {
        System.out.print("[");
        for (TreeNode node : con) {
            System.out.print("(" + node.getValue() + ", " + node.getFrequency() + "), ");
        }
        System.out.println("]");
    }
}
