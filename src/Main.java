import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        /* Queue test
        PriorityQ q = new PriorityQ();
        TreeNode tree = new TreeNode(65, 2);
        q.enQueue(tree);
        tree = new TreeNode(67, 4);
        q.enQueue(tree);
        tree = new TreeNode(70, 2);
        q.enQueue(tree);

        q.printQ();
         */

        //tree tests
        String s = "122 67 100 69 69 85 69 -1";
        //printTree(createHuffTree(getFrequencyMapTest(s)), 0);
        HuffTree tree = new HuffTree(s);
        tree.printTree();
    }
}
