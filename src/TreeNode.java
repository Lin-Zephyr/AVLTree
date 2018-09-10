import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by LinGaunnan on 8/29/2018.
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TreeNode {
    private int elem;
    private TreeNode left, right;
    private TreeNode parent;
    private int balanceFactor;
    public TreeNode(int elem) {
        this.elem = elem;
        this.balanceFactor = 0;
    }

    @Override
    public String toString() {
        return "TreeNode(elem="+elem+", " +
                "left="+ (null == left ? "null" : left.getElem())+", " +
                "right="+ (null == right ? "null" : right.getElem())+"," +
                "parent="+ (null == parent ? "null" : parent.getElem())+"," +
                " balanceFactor=" +balanceFactor+ ")";
    }
}