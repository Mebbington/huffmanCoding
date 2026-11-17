//todo add header

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

//todo comment class
public class HuffTree implements IHuffConstants{
    private TreeNode root;

    public HuffTree(InputStream in) throws IOException {
        TreeMap<Integer, Integer> freqMap = getFrequencyMap(in);
        System.out.println(freqMap);
        root = createHuffTree(freqMap);
    }

    public HuffTree(String s) {
        TreeMap<Integer, Integer> freqMap = getFrequencyMap(s);
        System.out.println(freqMap);
        root = createHuffTree(freqMap);
    }

    //todo pre post
    private TreeMap<Integer, Integer> getFrequencyMap(InputStream in) throws IOException {
        HashMap<Integer, Integer> result = new HashMap<>();
        boolean done = false;
        BitInputStream bin = new BitInputStream(in);
        while (!done) {
            //returns int [0 to 255] NOT IN BINARY
            //int bits = in.read();
            int bits = bin.readBits(BITS_PER_WORD);
            if (bits == -1) {
                done = true;
            } else {
                if (!result.containsKey(bits)) {
                    result.put(bits, 1);
                } else {
                    result.put(bits, result.get(bits) + 1);
                }
            }
        }
        return new TreeMap<>(result);
    }

    //todo JUST FOR TESTING
    private TreeMap<Integer, Integer> getFrequencyMap(String s) {
        HashMap<Integer, Integer> result = new HashMap<>();
        String[] bytes = s.split(" ");
        boolean done = false;
        int i = 0;
        while (!done) {
            //returns int [0 to 255] NOT IN BINARY
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
        return new TreeMap<>(result);
    }

    //todo pre post
    private TreeNode createHuffTree(TreeMap<Integer, Integer> freq) {
        PriorityQ<TreeNode> queue = enqueueAll(freq);
        while (queue.size() > 1) {
            TreeNode node1 = queue.deQueue();
            TreeNode node2 = queue.deQueue();
            TreeNode newNode =
                new TreeNode(-1,node1.getFrequency() + node2.getFrequency());
            newNode.setLeft(node1);
            newNode.setRight(node2);
            queue.enQueue(newNode);
        }

        return queue.deQueue();
    }

    //todo pre post
    private PriorityQ<TreeNode> enqueueAll(TreeMap<Integer, Integer> map) {
        Set<Integer> keys = map.keySet();
        PriorityQ<TreeNode> queue = new PriorityQ();
        for (Integer key : keys) {
            TreeNode node = new TreeNode(key, map.get(key));
            queue.enQueue(node);
        }
        //add EOF
        //todo does this need to be in specific spot in queue?
        TreeNode eof = new TreeNode(PSEUDO_EOF, 1);
        queue.enQueue(eof);
        return queue;
    }

    //todo pre post
    public void printTree() {
        printTreeHelper(root, 0);
    }

    //todo pre post
    private void printTreeHelper(TreeNode root, int level) {
        if (root == null) {
            return;
        }

        printTreeHelper(root.getRight(), level + 1);

        for (int i = 0; i < level; i++) {
            System.out.print("       ");
        }

        System.out.println(root.getFrequency() + " " + root.getValue());

        printTreeHelper(root.getLeft(), level + 1);
    }

    // todo pre post
    public TreeNode getRoot() {
        return root;
    }
}
