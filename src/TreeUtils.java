/**
 * Created by LinGaunnan on 8/30/2018.
 */
public class TreeUtils {
    public static void swapTreeElem(TreeNode node1, TreeNode node2) {
        int elem = node1.getElem();
        node1.setElem(node2.getElem());
        node2.setElem(elem);
    }

    public static void swapTreeBalanceFactor(TreeNode node1, TreeNode node2) {
        int bf = node1.getBalanceFactor();
        node1.setBalanceFactor(node2.getBalanceFactor());
        node2.setBalanceFactor(bf);
    }
}
