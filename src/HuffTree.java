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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

//todo comment class
public class HuffTree implements IHuffConstants{
    private TreeNode root;
    private int originalBits;
    private int[] counts;
    private int size;

    public HuffTree(InputStream in) throws IOException {
        counts = new int[ALPH_SIZE];
        TreeMap<Integer, Integer> freqMap = getFrequencyMapFile(in);
        //todo remove print
        System.out.println(freqMap);
        root = createHuffTree(freqMap);
    }

    //todo okay to overload here?
    public HuffTree(BitInputStream bin, int headerFormat) throws IOException {
        if(headerFormat == STORE_COUNTS) {
            TreeMap<Integer, Integer> freqMap = getFrequencyMapHeader(bin);
            root = createHuffTree(freqMap);
        } else if (headerFormat == STORE_TREE) {
            size = bin.readBits(BITS_PER_INT);
            root = treeRec(bin);
        }
        //todo implement something for custom?
    }

    // todo freq is val... change maybe
    private TreeNode treeRec(BitInputStream bin) throws IOException {
        int bit = bin.readBits(1);
        if (bit == 0) {
            // internal node
            TreeNode internal = new TreeNode(-1, 0);
            internal.setLeft(treeRec(bin));
            internal.setRight(treeRec(bin));
            return internal;
        } else if (bit == 1) {
            // leaf node
            int val = bin.readBits(BITS_PER_WORD + 1);
            return new TreeNode(val, 0);
        } else {
            // todo THROW AN ERROR
        }
       return null;
    }

    //todo delete after tests
    public HuffTree(String s) {
        TreeMap<Integer, Integer> freqMap = getFrequencyMapFile(s);
        System.out.println(freqMap);
        root = createHuffTree(freqMap);
    }

    //pre: in != null
    //post: returns a TreeMap of value frequency pairs based on each byte in the provided
    // inputStream, also updates counts[] for writing compressedFile later.
    private TreeMap<Integer, Integer> getFrequencyMapFile(InputStream in) throws IOException {
        HashMap<Integer, Integer> result = new HashMap<>();
        boolean done = false;
        BitInputStream bin = new BitInputStream(in);
        while (!done) {
            //returns int [0 to 255] NOT IN BINARY
            int bits = bin.readBits(BITS_PER_WORD);
            originalBits += BITS_PER_WORD;
            //todo check this counts[]
            if (bits == -1) {
                done = true;
            } else {
                counts[bits]++;
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
    private TreeMap<Integer, Integer> getFrequencyMapFile(String s) {
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

    //todo using list better herer?
    //todo pre post
    private TreeMap<Integer, Integer> getFrequencyMapHeader(BitInputStream bin) throws IOException {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < ALPH_SIZE; i++) {
            int freq = bin.readBits(BITS_PER_INT);
            if(freq > 0) {
                map.put(i, freq);
            }
        }
        return new TreeMap<Integer, Integer>(map);
    }

    //pre: freq != null
    //post: returns rootNode of the HuffTree obtained using freq.
    private TreeNode createHuffTree(TreeMap<Integer, Integer> freq) {
        //check precondition
        if(freq == null) {
            throw new IllegalArgumentException("Frequency Tree cannot be null.");
        }
        PriorityQ<TreeNode> queue = enqueueAll(freq);
        size = queue.size();
        while (queue.size() > 1) {
            TreeNode node1 = queue.deQueue();
            TreeNode node2 = queue.deQueue();
            TreeNode newNode =
                new TreeNode(-1,node1.getFrequency() + node2.getFrequency());
            newNode.setLeft(node1);
            newNode.setRight(node2);
            queue.enQueue(newNode);
            size++;
        }

        return queue.deQueue();
    }

    //pre: none
    //post: returns a Priority Queue with all value frequency pairs in map added after
    // wrapping them in TreeNodes, also adds a node with EOF value.
    private PriorityQ<TreeNode> enqueueAll(TreeMap<Integer, Integer> map) {
        Set<Integer> keys = map.keySet();
        PriorityQ<TreeNode> queue = new PriorityQ<>();
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

    //pre: none
    //post: prints out the tree sideways
    public void printTree() {
        printTreeHelper(root, 0);
    }

    //pre:none
    //post:prints out this level of the tree vertically
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

    //pre: none
    //post: writes STF header to bout file
    //todo print test delete
    public void writeSCF(BitOutputStream bout) {
        bout.writeBits(BITS_PER_INT, STORE_COUNTS);
        for(int i = 0; i < ALPH_SIZE; i++) {
            bout.writeBits(BITS_PER_INT, counts[i]);
        }
    }

    //pre: none
    //post: writes STF header to bout file
    //todo alex check
    public void writeSTF(BitOutputStream bout) {
        bout.writeBits(BITS_PER_INT, STORE_TREE);
        bout.writeBits(BITS_PER_INT, size);
        recursiveSTF(bout, root);
    }

    //pre: none
    //post: writes STF header data for n and its children
    //todo alex check
    private void recursiveSTF(BitOutputStream bout, TreeNode n) {
        if(n != null) {
            if(n.isLeaf()) {
                bout.writeBits(1, 1);
                bout.writeBits(BITS_PER_WORD + 1, n.getValue());
            } else {
                //pre-order traversal
                bout.writeBits(1, 0);
                recursiveSTF(bout, n.getLeft());
                recursiveSTF(bout, n.getRight());
            }
        }
    }

    //pre: none
    //post: returns originalBits
    public int OGBits() {
        return originalBits;
    }

    //pre: none
    //post: returns the root
    public TreeNode getRoot() {
        return root;
    }

    //pre: none
    //post: returns the counts
    public int[] getCounts() {
        return counts;
    }
}
