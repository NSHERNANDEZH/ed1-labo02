package ed.lab;

public class E01InvertBT {
    public TreeNode<Integer> invertTree(TreeNode<Integer> root) {
        if (root == null) {
            return null;
        }
        TreeNode<Integer> left = invertTree(root.left);
        TreeNode<Integer> right = invertTree(root.right);

        root.left = right;
        root.right = left;

        return root;
    }


}
