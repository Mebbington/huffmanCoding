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
import java.util.*;

public class SimpleHuffProcessor implements IHuffProcessor {

    private IHuffViewer myViewer;
    private HuffTree huffTree;
    private int compressedBits;
    private int originalBits;
    private int headerFormat;
    private Map<Integer, String> huffMap;
    //todo update this in preprocess
    //todo private final int MAGIC_NUM =
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
        // reset bit count back to 0 before filling map
        compressedBits = 0;
        huffTree = new HuffTree(in);
        huffMap = createCodeMap(huffTree);
        originalBits = huffTree.OGBits();
        this.headerFormat = headerFormat;
        return originalBits - compressedBits;
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
    //todo check force using class vars and
    // also use class var format to decide between SCF and STF
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
        //throw new IOException("compress is not implemented");
        // Header of File
        BitOutputStream bout = new BitOutputStream(out);
        bout.writeBits(BITS_PER_INT, MAGIC_NUMBER);
        //bout.writeBits(BITS_PER_INT, STORE_COUNTS);
        //bout.writeBits(BITS_PER_INT, STORE_TREE);

        ArrayList<Integer> toWrite = new ArrayList<>();
        boolean done = false;
        BitInputStream bin = new BitInputStream(in);
        while (!done) {
            int bits = bin.readBits(BITS_PER_WORD);
            if (bits == -1) {
                done = true;
            } else {
                String huffCode = huffMap.get(bits);
                for (int i = 0; i < huffCode.length(); i++) {
                    //out.write(Integer.parseInt(""+huffCode.charAt(i)));
                    toWrite.add(Integer.parseInt(""+huffCode.charAt(i)));
                }
            }
        }
        /*
        if (count > inCount && !force) {
            myViewer.showError("Compression results in more bits."); // todo show error or show message?
        }
         */
        for (int bit : toWrite) {
            out.write(bit); // <-
        }
        return compressedBits;
    }

    private Map<Integer, String> createCodeMap(HuffTree ht) {
        TreeNode huffRoot = ht.getRoot();
        Map<Integer, String> huffMap = new HashMap<>();
        fillHuffCodeMap(huffMap, huffRoot, "");
        return huffMap;
    }

    private void fillHuffCodeMap(Map<Integer, String> map, TreeNode n, String s) {
        if(n != null) {
            if (n.isLeaf()) {
                compressedBits += s.length() * n.getFrequency();
                map.put(n.getValue(), s);
            } else {
                StringBuilder sb = new StringBuilder(s); //TODO FIX SB
                fillHuffCodeMap(map, n.getLeft(), new String(sb.append("0")));
                fillHuffCodeMap(map, n.getRight(), new String(sb.append("1")));
            }
        }
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
        //throw new IOException("uncompress not implemented");
        // TODO:
        // todo check magic num
        BitInputStream bin = new BitInputStream(in);
        int magic = bin.readBits(BITS_PER_INT);
        if (magic != MAGIC_NUMBER) {
            myViewer.showError("Wrong MAGIC_NUMBER");
            return -1;
        }

        boolean done = false;
        while (!done) {
            int bits = bin.readBits(1);
            if (bits == PSEUDO_EOF) { // TODO FIX THIS READ METHOD
                done = true;
            } else {
                if (bits == -1) {
                    myViewer.showError("-1 eror msg"); // TODO WRITE MSG
                }

            }
        }
        return 0;
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
