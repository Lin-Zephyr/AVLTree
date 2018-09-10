import javax.smartcardio.TerminalFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by LinGaunnan on 8/29/2018.
 */
public class BST implements ITree {
    private TreeNode root;

    @Override
    public boolean insert(int elem) {
        TreeNode node = new TreeNode(elem);
        if (null == this.root) {
            this.root = node;
            return true;
        } else {
            return insertChild(this.root, node);
        }
    }
    @Override
    public void printTreeScheme() {
        System.out.println("-");
        printTree(this.root, "");
        System.out.println("-");
    }
    private void printTree(TreeNode node, String indent) {
        if (null != node) {
            printTree(node.getRight(), indent + "    ");
            System.out.println(indent + node.getElem());
            printTree(node.getLeft(), indent + "    ");
        }
    }
    /**
     * delete the node which element equals to parameter elem
     * @param elem
     * @return true if the node can be found and delete otherwise false
     */
    @Override
    public boolean delete(int elem) {
        if (null == this.root) {
            return false;
        } else {
            TreeNode node = this.root;
            // find out the node need to be deleted
            while (null != node) {
                if (node.getElem() == elem) {
                    deleteNode(node);
                    return true;
                } else if (node.getElem() > elem) {
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            }
            return false;
        }
    }
    @Override
    public boolean search(int elem) {
        if (null == this.root) {
            return false;
        } else {
            return searchTree(this.root, elem);
        }
    }
    private void deleteNode(TreeNode node) {
        if (null == node.getLeft() && null == node.getRight()) {
            // deleted node is a leave
            if (null  == node.getParent()) {
                // deleted node is root
                this.root = null;
            } else if (node == node.getParent().getLeft()) {
                // deleted node is the left child of its parent
                node.getParent().setLeft(null);
            } else {
                // deleted node is the right child of its parent
                node.getParent().setRight(null);
            }
        } else if (null == node.getLeft()) {
            // deleted node only hae right child
            if (null  == node.getParent()) {
                this.root = node.getRight();
            } else if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(node.getRight());
            } else {
                node.getParent().setRight(node.getRight());
            }
            node.getRight().setParent(node.getParent());
        } else if (null == node.getRight()) {
            // deleted node only have left child
            if (null  == node.getParent()) {
                this.root = node.getLeft();
            } else if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(node.getLeft());
            } else {
                node.getParent().setRight(node.getLeft());
            }
            node.getLeft().setParent(node.getParent());
        } else {
            // deleted node have both left & right children
            // find out the precursor of deleted node
            // the precursor node replace the position of deleted node
            TreeNode pre = node.getLeft();
            while (null != pre.getRight()) {
                pre  = pre.getRight();
            }
            // swap the elem of precursor node and deleted node
            // then delete the precursor node
            TreeUtils.swapTreeElem(pre, node);
            deleteNode(pre);
        }
    }
    /**
     * insert the newNode to the child position of parent node
     * @param parent
     * @param newNode
     * @return true if the newNode insert successfully or false if the newNode have been exist
     */
    private boolean insertChild(TreeNode parent, TreeNode newNode) {
        if (parent.getElem() == newNode.getElem()) {
            return false;
        } else if (parent.getElem() > newNode.getElem()) {
            if (null  == parent.getLeft()) {
               parent.setLeft(newNode);
               newNode.setParent(parent);
               return true;
            } else {
               return insertChild(parent.getLeft(), newNode);
            }
        } else {
            if (null == parent.getRight()) {
               parent.setRight(newNode);
               newNode.setParent(parent);
               return true;
            } else {
               return insertChild(parent.getRight(), newNode);
            }
        }
    }

    /**
     * downward from parent node search the BST of elem
     * @param parent
     * @param elem
     * @return true if the elem can be found while false if the elem can not be found
     */
    private boolean searchTree(TreeNode parent, int elem) {
        if (null == parent) {
            return false;
        } else {
            if (parent.getElem() == elem) {
                return true;
            } else if (parent.getElem() > elem) {
                return searchTree(parent.getLeft(), elem);
            } else {
                return searchTree(parent.getRight(), elem);
            }
        }
    }
}
