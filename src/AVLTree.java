import lombok.*;
import lombok.experimental.Accessors;
import sun.reflect.generics.tree.Tree;

import java.beans.ConstructorProperties;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.temporal.Temporal;
import java.util.TreeMap;

/**
 * Created by LinGaunnan on 6/1/2018.
 */

@Data
public class AVLTree implements ITree {
    private TreeNode root;
    @Override
    public boolean insert(int elem) {
        TreeNode node = new TreeNode(elem);
        if (null == this.root) {
            this.root = node;
            return true;
        } else {
            return insertNode(this.root, node);
        }
    }
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

    @Override
    public void printTreeScheme() {
        System.out.println("-");
        printTree(this.root, "");
        System.out.println("-");
    }

    private void deleteNode(TreeNode node) {
        TreeNode parent = node.getParent();
        if (null == node.getLeft() && null == node.getRight()) {
            if (null == parent) {
                this.root = null;
            } else if (node == parent.getLeft()) {
                parent.setLeft(null);
                parent.setBalanceFactor(parent.getBalanceFactor() - 1);
            } else {
                parent.setRight(null);
                parent.setBalanceFactor(parent.getBalanceFactor() + 1);
            }
            deleteFixUp(parent);
        } else if (null == node.getLeft()) {
            TreeNode right = node.getRight();
            if (null == parent) {
                this.root = right;
            } else if (node == parent.getLeft()) {
                parent.setLeft(right);
                parent.setBalanceFactor(parent.getBalanceFactor() - 1);
            } else {
                parent.setRight(right);
                parent.setBalanceFactor(parent.getBalanceFactor() + 1);
            }
            if (null != right) {
                right.setParent(parent);
            }
            deleteFixUp(parent);
        } else if (null == node.getRight()) {
            TreeNode left = node.getLeft();
            if (null == parent) {
                this.root = left;
            } else if (node == parent.getLeft()) {
                parent.setLeft(left);
                parent.setBalanceFactor(parent.getBalanceFactor() - 1);
            } else {
                parent.setRight(left);
                parent.setBalanceFactor(parent.getBalanceFactor() + 1);
            }
            if (null != left) {
                left.setParent(parent);
            }
            deleteFixUp(parent);
        } else {
            TreeNode pre = node.getLeft();
            while (null != pre.getRight()) {
                pre = pre.getRight();
            }
            TreeUtils.swapTreeElem(pre, node);
            deleteNode(pre);
        }
    }

    /**
     * fix up tree from node after delete node
     * @param node
     */
    private void deleteFixUp(TreeNode node) {
        if (null == node || -1 == node.getBalanceFactor() || 1 == node.getBalanceFactor()) {
            return;
        } else {
            TreeNode parent = node.getParent();
            boolean isLeft = null != parent && parent.getLeft() == node ? true : false;
            if (-2 == node.getBalanceFactor()) {
                TreeNode right = node.getRight();
                if (-1 == right.getBalanceFactor()) {
                    rotateRRFix(node);
                } else {
                    rotateRLFix(node);
                }
            } else  if (2 == node.getBalanceFactor()) {
                TreeNode left = node.getLeft();
                if (1 == left.getBalanceFactor()) {
                    rotateLLFix(node);
                } else {
                    rotateLRFix(node);
                }
            }
            if (null != parent) {
                if (isLeft) {
                    parent.setBalanceFactor(parent.getBalanceFactor() - 1);
                } else {
                    parent.setBalanceFactor(parent.getBalanceFactor() + 1);
                }
                // up tracking until root
                deleteFixUp(parent);
            }
        }
    }
    private void printTree(TreeNode node, String indent) {
        if (null != node) {
            printTree(node.getRight(), indent + "    ");
            System.out.println(indent + node.getElem()+"[" + node.getBalanceFactor() + "]");
            printTree(node.getLeft(), indent + "    ");
        }
    }

    private boolean insertNode(TreeNode parent, TreeNode node) {
        if (parent.getElem() == node.getElem()) {
            return false;
        } else if (parent.getElem() > node.getElem()) {
            if (null == parent.getLeft()) {
                parent.setLeft(node);
                node.setParent(parent);
                insertFixUp(node);
                return true;
            } else {
                return insertNode(parent.getLeft(), node);
            }
        } else {
            if (null == parent.getRight()) {
                parent.setRight(node);
                node.setParent(parent);
                insertFixUp(node);
                return true;
            } else {
                return insertNode(parent.getRight(), node);
            }
        }
    }

    /**
     * fix up tree from node after insert node
     * @param node
     */
    private void insertFixUp(TreeNode node) {
        TreeNode parent = node.getParent();
        while (null != parent) { // track to root when parent is not null
            if (node == parent.getLeft()) {
                parent.setBalanceFactor(parent.getBalanceFactor() + 1);
            } else {
                parent.setBalanceFactor(parent.getBalanceFactor() - 1);
            }
            if (0 == parent.getBalanceFactor()) {
                break;
            }
            if (-2 == parent.getBalanceFactor() || 2 == parent.getBalanceFactor()) {
                if (2 == parent.getBalanceFactor()) {
                    TreeNode left = parent.getLeft();
                    if (-1 == left.getBalanceFactor()) {
                        rotateLRFix(parent);
                    } else {
                        rotateLLFix(parent);
                    }
                } else {
                    TreeNode right = parent.getRight();
                    if (1 == right.getBalanceFactor()) {
                        rotateRLFix(parent);
                    } else {
                        rotateRRFix(parent);
                    }
                }
                break;
            }
            node = parent;
            parent = node.getParent();
        }
    }

    private void rotateLLFix(TreeNode parent) {
        TreeNode left = parent.getLeft();
        rotateRight(parent);
        // update the balance factor
        parent.setBalanceFactor(0);
        left.setBalanceFactor(0);
    }
    private void rotateLRFix(TreeNode parent) {
        TreeNode left = parent.getLeft();
        TreeNode grandchild = parent.getRight();
        rotateLeft(left);
        rotateRight(parent);
        // update the balance factor
        if (0 == grandchild.getBalanceFactor()) {
            parent.setBalanceFactor(0);
            left.setBalanceFactor(0);
        } else if (-1 == grandchild.getBalanceFactor()) {
            parent.setBalanceFactor(0);
            left.setBalanceFactor(-1);
        } else {
            left.setBalanceFactor(0);
            parent.setBalanceFactor(-1);
        }
        grandchild.setBalanceFactor(0);
    }
    private void rotateRRFix(TreeNode parent) {
        TreeNode right = parent.getRight();
        rotateLeft(parent);
        parent.setBalanceFactor(0);
        right.setBalanceFactor(0);
    }
    private void rotateRLFix(TreeNode parent) {
        TreeNode right = parent.getRight();
        TreeNode grandchild = right.getLeft();
        rotateRight(right);
        rotateLeft(parent);
        if (0 == grandchild.getBalanceFactor()) {
            parent.setBalanceFactor(0);
            right.setBalanceFactor(0);
        } else if (-1 == grandchild.getBalanceFactor()) {
            parent.setBalanceFactor(1);
            right.setBalanceFactor(0);
        } else {
            parent.setBalanceFactor(0);
            right.setBalanceFactor(-1);
        }
        grandchild.setBalanceFactor(0);
    }

    /**
     * left rotate the child tree base on pivot node
     * @param pivot
     */
    private void rotateLeft(TreeNode pivot) {
        TreeNode rightChild = pivot.getRight();
        TreeNode grandChildLeft = rightChild.getLeft();
        TreeNode parent = pivot.getParent();
        if (null == parent) {
            // pivot node is root
            this.root = rightChild;
        } else if(pivot == parent.getLeft()) {
            parent.setLeft(rightChild);
        } else {
            parent.setRight(rightChild);
        }
        rightChild.setParent(parent);

        pivot.setRight(grandChildLeft);
        if (null != grandChildLeft) {
            grandChildLeft.setParent(pivot);
        }

        rightChild.setLeft(pivot);
        pivot.setParent(rightChild);
    }

    /**
     * right rotate the child tree base on pivot node
     * @param pivot
     */
    private void rotateRight(TreeNode pivot) {
        TreeNode leftChild = pivot.getLeft();
        TreeNode grandChildRight = leftChild.getRight();
        TreeNode parent = pivot.getParent();
        if (null == parent) {
            this.root = leftChild;
        } else if (pivot == parent.getLeft()) {
            parent.setLeft(leftChild);
        } else {
            parent.setRight(leftChild);
        }
        leftChild.setParent(parent);

        pivot.setLeft(grandChildRight);
        if (null != grandChildRight) {
            grandChildRight.setParent(pivot);
        }

        leftChild.setRight(pivot);
        pivot.setParent(leftChild);
    }

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
