import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        /*
        PriorityQ q = new PriorityQ();
        TreeNode tree = new TreeNode(0, 2);
        q.enQueue(tree);
        tree = new TreeNode(2, 4);
        q.enQueue(tree);
        tree = new TreeNode(5, 2);
        q.enQueue(tree);
         */

        String s = "122 67 100 69 69 85 69 -1";
        printTree(createHuffTree(getFrequencyMapTest(s)), 0);

    }

    private static HashMap<Integer, Integer> getFrequencyMapTest(String s) {
        //todo does inputstream.read()
        HashMap<Integer, Integer> result = new HashMap<>();
        String[] bytes = s.split(" ");
        boolean done = false;
        int i = 0;
        while (!done) {
            //todo IMPORTANT returns [0 to 255] NOT IN BINARY
            int bits = Integer.parseInt(bytes[i]);
            if (bits == -1) {
                done = true;
            } else {
                if (!result.containsKey(bits)) {
                    result.put(bits, 1);
                } else {
                    result.put(bits, result.get(bits) + 1);
                }
            }
            i++;
        }
        System.out.println(result);
        return result;
    }

    private static PriorityQ enqueueAll(HashMap<Integer, Integer> map) {
        Set<Integer> keys = map.keySet();
        PriorityQ queue = new PriorityQ();
        for (Integer key : keys) {
            TreeNode node = new TreeNode(key, map.get(key));
            queue.enQueue(node);
        }
        return queue;
    }

    private static TreeNode createHuffTree(HashMap<Integer, Integer> freq) {
        PriorityQ queue = enqueueAll(freq);
        while (queue.size() > 1) {
            TreeNode node1 = queue.deQueue();
            TreeNode node2 = queue.deQueue();
            // todo DANGEROUS NODE -1
            TreeNode newNode =
                new TreeNode(-1,node1.getFrequency() + node2.getFrequency());
            newNode.setLeft(node1);
            newNode.setRight(node2);
            queue.enQueue(newNode);
        }

        return queue.deQueue();
    }

    private static void TreeNodeInOrder(TreeNode n) {
        if(n != null) {
            TreeNodeInOrder(n.getLeft());
            System.out.print(n);
            TreeNodeInOrder(n.getRight());
        }
    }

    public static void printTree(TreeNode root, int level) {
        if (root == null) {
            return;
        }

        printTree(root.getRight(), level + 1);

        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }

        System.out.println(root.getFrequency());

        printTree(root.getLeft(), level + 1);
    }
}
