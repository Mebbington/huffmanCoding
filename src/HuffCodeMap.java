import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// TODO NOTE: THIS IS JUST A PROOF OF CONCEPT CLASS

public class HuffCodeMap {
    Map<Integer, String> map;

    public HuffCodeMap(HuffTree ht) {
        TreeNode huffRoot = ht.getRoot();
        Map<Integer, String> huffMap = new HashMap<>();
        fillHuffCodeMap(huffMap, huffRoot, "");
        map = huffMap;
    }

    private void fillHuffCodeMap(Map<Integer, String> map, TreeNode n, String s) {
        if(n != null) {
            if (n.isLeaf()) {
                map.put(n.getValue(), s);
            } else {
                StringBuilder sb = new StringBuilder(s);
                fillHuffCodeMap(map, n.getLeft(), new String(sb.append("0")));
                fillHuffCodeMap(map, n.getRight(), new String(sb.append("1")));
            }
        }
    }

    private void inOrderTrav(TreeNode n) {
        if (n != null) {
            inOrderTrav(n.getLeft());
            System.out.println(n);
            inOrderTrav(n.getRight());
        }
    }

    public void printMap() {
        Set<Integer> keys = map.keySet();
        for (Integer key : keys) {
            System.out.println("ASCII: " + key + ", Code: " + map.get(key));
        }
    }

    public Map<Integer, String> getMap(){
        return map;
    }

    public int writeOut(String s) {
        int count = 0;
        String[] bytes = s.split(" ");
        boolean done = false;
        int i = 0;
        while (!done) {
            int bits = Integer.parseInt(bytes[i]);
            if (bits == -1) {
                done = true;
            } else {
                // Parse -> loss of leading zeros. Must go bit by bit.
                String huffCode = map.get(bits);
                for (int z = 0; z < huffCode.length(); z++) {
                    System.out.println(huffCode.charAt(z));
                    count++;
                }
            }
            i++;
        }
        System.out.println();
        return count;
    }
}
