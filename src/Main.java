public class Main {
    public static void main(String[] args) {
        PriorityQ q = new PriorityQ();
        TreeNode tree = new TreeNode(0, 2);
        q.enQueue(tree);
        tree = new TreeNode(2, 4);
        q.enQueue(tree);
        tree = new TreeNode(5, 2);
        q.enQueue(tree);

        q.printQ();
    }
}
