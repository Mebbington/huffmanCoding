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
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleHuffProcessor implements IHuffProcessor {

    private IHuffViewer myViewer;
    //todo update this in preprocess
    private TreeNode huffRoot;
    /**
     * Preprocess data so that compression is possible ---
     * count characters/create tree/store state so that
     * a subsequent call to compress will work. The InputStream
     * is <em>not</em> a BitInputStream, so wrap it int one as needed.
     *
     * @param in           is the stream which could be subsequently compressed
     * @param headerFormat a constant from IHuffProcessor that determines what kind of
     *                     header to use, standard count format, standard tree format, or
     *                     possibly some format added in the future.
     * @return number of bits saved by compression or some other measure
     * Note, to determine the number of
     * bits saved, the number of bits written includes
     * ALL bits that will be written including the
     * magic number, the header format number, the header to
     * reproduce the tree, AND the actual data.
     * @throws IOException if an error occurs while reading from the input file.
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException {
        //todo can we remove these warnings?
        showString("Not working yet");
        myViewer.update("Still not working");
        //throw new IOException("preprocess not implemented");
        HashMap<Integer, Integer> freq = getFrequencyMap(in);
        return 0;
    }

    /**
     * Compresses input to output, where the same InputStream has
     * previously been pre-processed via <code>preprocessCompress</code>
     * storing state used by this call.
     * <br> pre: <code>preprocessCompress</code> must be called before this method
     *
     * @param in    is the stream being compressed (NOT a BitInputStream)
     * @param out   is bound to a file/stream to which bits are written
     *              for the compressed file (not a BitOutputStream)
     * @param force if this is true create the output file even if it is larger than the input file.
     *              If this is false do not create the output file if it is larger than the input file.
     * @return the number of bits written.
     * @throws IOException if an error occurs while reading from the input file or
     *                     writing to the output file.
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
        throw new IOException("compress is not implemented");
        //return 0;
    }

    private HashMap<Integer, Integer> getFrequencyMap(InputStream in) throws IOException {
        HashMap<Integer, Integer> result = new HashMap<>();
        //todo should we use bitInputStream?
        boolean done = false;
        while (!done) {
            //todo IMPORTANT returns [0 to 255] NOT IN BINARY
            int bits = in.read();
            //todo HowTo mentions a method that reads bits at a time? is this good?
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
        System.out.println(result);
        return result;
    }

    private TreeNode createHuffTree(HashMap<Integer, Integer> freq) {
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

    private PriorityQ enqueueAll(HashMap<Integer, Integer> map) {
        Set<Integer> keys = map.keySet();
        PriorityQ queue = new PriorityQ();
        for (Integer key : keys) {
            TreeNode node = new TreeNode(key, map.get(key));
            queue.enQueue(node);
        }
        return queue;
    }

    //todo should this be a map or hashmap
    private Map<Integer, String> getHuffCodeMap() {
        return null;
    }

    /**
     * Uncompress a previously compressed stream in, writing the
     * uncompressed bits/data to out.
     *
     * @param in  is the previously compressed data (not a BitInputStream)
     * @param out is the uncompressed file/stream
     * @return the number of bits written to the uncompressed file/stream
     * @throws IOException if an error occurs while reading from the input file or
     *                     writing to the output file.
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException {
        throw new IOException("uncompress not implemented");
        //return 0;
    }

    public void setViewer(IHuffViewer viewer) {
        myViewer = viewer;
    }

    private void showString(String s) {
        if (myViewer != null) {
            myViewer.update(s);
        }
    }
}
