import lombok.Cleanup;
import sun.text.resources.th.BreakIteratorInfo_th;

import java.util.Scanner;

/**
 * Created by LinGaunnan on 9/3/2018.
 */
public class Main {
    public static void main(String[] args) {
        ITree tree = null;
        tree = new BST();
        tree = new AVLTree();
        @Cleanup Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("--------------------------------------------------------------");
            System.out.println("1. insert");
            System.out.println("2. delete");
            System.out.println("3. search");
            System.out.println("4. print");
            System.out.println("5. exit");
            System.out.print("->");
            int cmd = scanner.nextInt();
            if (5 == cmd) {
                break;
            }
            switch (cmd) {
                case 1:
                    while (true) {
                        System.out.print("->");
                        int elem = scanner.nextInt();
                        if (-1 == elem) {
                            break;
                        }
                        System.out.println("insert " + (tree.insert(elem) ? "success" : "fail"));
                    }
                    break;
                case 2:
                    System.out.print("->");
                    int elem = scanner.nextInt();
                    System.out.println("delete " + (tree.delete(elem) ? "success" : "fail"));
                    break;
                case 3:
                    System.out.print("->");
                    elem = scanner.nextInt();
                    System.out.println(tree.search(elem) ? "can be found" : "can not be found");
                    break;
                case 4:
                    tree.printTreeScheme();
                default:break;
            }
        }
    }
}
