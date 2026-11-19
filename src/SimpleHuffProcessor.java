/*  Student information for assignment:
 *
 *  On OUR honor, Alex OR and <NAME2), this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used: 0
 *
 *  Student 1 Alexander Olivares-Reed
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
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
//todo fix imports ALL FILES

public class SimpleHuffProcessor implements IHuffProcessor {

    private IHuffViewer myViewer;
    private HuffTree huffTree;
    private int compressedBits;
    private int originalBits;
    private int headerFormat;
    private Map<Integer, String> huffMap;

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
        compressedBits = BITS_PER_INT; // start with MAGIC_NUM num of bits
        //create variables for compress
        huffTree = new HuffTree(in);
        huffMap = createCodeMap(huffTree);
        originalBits = huffTree.OGBits();
        this.headerFormat = headerFormat;
        // todo include header in compressed bits
        if (this.headerFormat == STORE_COUNTS) {

        } else if (this.headerFormat == STORE_TREE) {

        }
        //System.out.println("Prepro: " + (originalBits - compressedBits));
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
    //todo TESTTTTT and comment
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
        if(originalBits < compressedBits && !force) {
            myViewer.showError("Compression results in more bits.");
            return -1;
        }
        // Header of File
        BitOutputStream bout = new BitOutputStream(out);
        bout.writeBits(BITS_PER_INT, MAGIC_NUMBER);
        if (headerFormat == STORE_COUNTS) {
            //Write header for SCF
            huffTree.writeSCF(bout);
        } else if (headerFormat == STORE_TREE){
            //Write header for STF
            huffTree.writeSTF(bout);
        }

        boolean done = false;
        BitInputStream bin = new BitInputStream(in);
        while (!done) {
            int bits = bin.readBits(BITS_PER_WORD);
            if (bits == -1) {
                done = true;
            } else {
                String huffCode = huffMap.get(bits);
                for (int i = 0; i < huffCode.length(); i++) {
                    //System.out.println(Integer.parseInt("" + huffCode.charAt(i)));
                    bout.writeBits(1,Integer.parseInt(""+huffCode.charAt(i)));
                    //out.write(Integer.parseInt(huffCode.substring(i,i+1)));
                    // todo check stress tests
                }
            }
        }
        for (char c : huffMap.get(PSEUDO_EOF).toCharArray()) {
            bout.writeBits(1,Integer.parseInt(""+c));
        }
        bout.close();
        //System.out.println("Compr: " + (compressedBits));
        Set<Integer> keys = huffMap.keySet();
        for (int key : keys) {
            //System.out.println("value: "+key+", equivalent char: "+(char)(key)+
                //", frequency: "+"NOT OBTAINABLE"+", new code: "+huffMap.get(key));
        }
        //huffTree.printTree();
        return compressedBits;
    }

    private Map<Integer, String> createCodeMap(HuffTree ht) {
        TreeNode huffRoot = ht.getRoot();
        //todo hash or tree?
        Map<Integer, String> huffMap = new TreeMap<>();
        fillHuffCodeMap(huffMap, huffRoot, "");
        return huffMap;
    }

    private void fillHuffCodeMap(Map<Integer, String> map, TreeNode n, String s) {
        if(n != null) {
            if (n.isLeaf()) {
                compressedBits += s.length() * n.getFrequency();
                map.put(n.getValue(), s);
            } else {
                fillHuffCodeMap(map, n.getLeft(), s + "0");
                fillHuffCodeMap(map, n.getRight(), s + "1");
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
        //check if file has valid Huffman header
        BitInputStream bin = new BitInputStream(in);
        int magic = bin.readBits(BITS_PER_INT);
        if (magic != MAGIC_NUMBER) {
            myViewer.showError("Wrong MAGIC_NUMBER");
            return -1;
        }

        //build tree based off of header
        int headerFormat = bin.readBits(BITS_PER_INT);
        if(headerFormat != STORE_COUNTS && headerFormat != STORE_TREE) {
            myViewer.showError("Illegal Header Format, this program does not implement " +
                "custom header format");
            return -1;
        }
        huffTree = new HuffTree(bin, headerFormat, myViewer);

        //write out using constructed HuffTree
        BitOutputStream bout = new BitOutputStream(out);
        boolean done = false;
        TreeNode current = huffTree.getRoot();
        int totalBits = 0;
        while (!done) {
            //step one 'read in one bit'
            //update current based on it
            //check if leaf
            int dir = bin.readBits(1);
            if(dir == 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
            if(current.isLeaf()) {
                int val = current.getValue();
                if(val == PSEUDO_EOF) {
                    done = true;
                } else {
                    if (val == -1) {
                        // BADDDD ERROR ON DECOMP
                        myViewer.showError("Decomp issue, -1 error");
                    }
                    bout.writeBits(BITS_PER_WORD, val);
                    totalBits+=BITS_PER_WORD;
                }
                current = huffTree.getRoot();
            }
        }
        bout.close();
        return totalBits;
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
