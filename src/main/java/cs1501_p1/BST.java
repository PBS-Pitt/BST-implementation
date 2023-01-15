package cs1501_p1;

public class BST<T extends Comparable<T>>  implements BST_Inter<T> {

    public BTNode<T> root;

    public BST(){
        root = null;
    }

    @Override
    public void put(T key) {
        if (key == null) throw new IllegalArgumentException("the key is invalid");
        root = put_rec(key, root);
    }

    public BTNode<T> put_rec(T key, BTNode<T> node){
        if (node == null)  return new BTNode(key);
        if (key.compareTo(node.getKey()) < 0) node.setLeft(put_rec(key, node.getLeft()));
        else if (key.compareTo(node.getKey()) > 0) node.setRight(put_rec(key, node.getRight()));
        return node;
    }

    @Override
    public boolean contains(T key) {
        if (key == null) throw new IllegalArgumentException("the key is invalid");
        BTNode<T> curr = root;
        while(curr != null){
            if(key.compareTo(curr.getKey()) == 0) return true;
            else if (key.compareTo(curr.getKey()) < 0) curr = curr.getLeft();
            else if (key.compareTo(curr.getKey()) > 0) curr = curr.getRight();
        }
        return false;
    }

    public BTNode<T> findNodeBefore(T key){
        if (key == null) throw new IllegalArgumentException("the key is invalid");
        BTNode<T> curr = root;
        if (key.compareTo(curr.getKey()) == 0 ) return curr;
        while(curr != null){
            if(key.compareTo(curr.getLeft().getKey()) == 0 || key.compareTo(curr.getRight().getKey()) == 0) return curr;
            else if (key.compareTo(curr.getKey()) < 0) curr = curr.getLeft();
            else if (key.compareTo(curr.getKey()) > 0) curr = curr.getRight();
        }
        return null;
    }

    @Override
    public void delete(T key) {
        if (key == null) throw new IllegalArgumentException("the key is invalid");
        if (contains(key)){
            BTNode<T> prev = findNodeBefore(key);
            
            //If the root is the node that we want to delete
            if (prev == root && key.compareTo(root.getKey()) == 0) {
                if (prev.getRight() == null && prev.getLeft() == null){
                    root = null;
                }else{
                    BTNode<T> del = prev;
                    BTNode<T> curr; 
                    if (del.getRight() != null) curr = del.getRight();
                    else curr = del.getLeft();
                    if (curr.getLeft() != null){
                        boolean check = true;
                        while (curr != null && check){
                            if (curr.getLeft().getLeft() == null){
                                BTNode<T> replacement = curr.getLeft();
                                if (replacement.getRight() != null) curr.setLeft(replacement.getRight());
                                else curr.setLeft(null);
                                replacement.setLeft(del.getLeft());
                                replacement.setRight(del.getRight());
                                root = replacement;
                                check = false;
                            }
                            curr = curr.getLeft();
                        }
                    }else{
                        if (del.getLeft() != curr) curr.setLeft(del.getLeft());
                        root = curr;
                    }

                }
            }
            //if the node that we want to delete is the child of another node
            else{
            BTNode<T> del;
            boolean rightChild;
            if (key.compareTo(prev.getLeft().getKey()) == 0){
                del = prev.getLeft();
                rightChild = false;
            }
            else{
                del = prev.getRight();
                rightChild = true;
            }
            if (del.getRight() == null && del.getLeft() == null){
                if (rightChild) prev.setRight(null);
                else prev.setLeft(null);
            }else{
                BTNode <T> curr;
                if (del.getRight() != null) curr = del.getRight();
                else curr = del.getLeft();
                if (curr.getLeft() != null){
                    boolean check = true;
                    while (curr != null && check){
                        if (curr.getLeft().getLeft() == null){
                            BTNode<T> replacement = curr.getLeft();
                            if (replacement.getRight() != null) curr.setLeft(replacement.getRight());
                            else curr.setLeft(null);
                            if (rightChild) prev.setRight(replacement);
                            else prev.setLeft(replacement);
                            replacement.setLeft(del.getLeft());
                            replacement.setRight(del.getRight());
                            check = false;
                        }
                        curr = curr.getLeft();
                    }
                }else{
                    if (del.getLeft() != curr) curr.setLeft(del.getLeft());
                    if (rightChild) prev.setRight(curr);
                    else prev.setLeft(curr);
                }
            }
            }
        }
        else {
            System.out.println("Key not found in the tree");
        }
    }

    @Override
    public int height() {
        if (root == null) return 0;
        return height_rec(root); 
    }

    public int height_rec(BTNode<T> curr){
        if (curr == null) return 0;
        return 1 + Math.max(height_rec(curr.getLeft()), height_rec(curr.getRight()));
    }

    @Override
    public boolean isBalanced() {
        if (root == null) return true;
        return isBalanced_rec(root);
    }

    public boolean isBalanced_rec(BTNode<T> root){
        if (root != null){
            int hLeft = height_rec(root.getLeft());
            int hRight = height_rec(root.getRight());
            if (Math.abs(hLeft - hRight) > 1) return false;
            isBalanced_rec(root.getLeft());
            isBalanced_rec(root.getRight());
        }
        return true;
    }

    @Override
    public String inOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        sb = IOT_rec(root, sb);
        sb.deleteCharAt(sb.lastIndexOf(":"));
        return sb.toString();
    }

    public StringBuilder IOT_rec(BTNode<T> root, StringBuilder sb){
        if (root != null){
            sb = IOT_rec(root.getLeft(), sb);
            sb.append(root.getKey().toString()).append(":");
            sb = IOT_rec(root.getRight(), sb);
        }
        return sb;
    }

    @Override
    public String serialize() {
        if (root == null) return "X(NULL)";
        StringBuilder sb = new StringBuilder();
        sb.append("R(" + root.getKey().toString() + "),");
        sb = POT_rec(root.getLeft(), sb);
        sb = POT_rec(root.getRight(), sb);
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }

    public StringBuilder POT_rec(BTNode<T> node, StringBuilder sb){
        if(node != null){
            if(node.getLeft() == null && node.getRight() == null) sb.append("L(" + node.getKey().toString() + "),");
            else {
                sb.append("I(" + node.getKey().toString() + "),");
                if (node.getLeft() == null) sb.append("X(NULL),");
                else sb = POT_rec(node.getLeft(), sb);
                if (node.getRight() == null) sb.append("X(NULL),");
                else sb = POT_rec(node.getRight(), sb);
            }
        }
        return sb;
    }

    @Override
    public BST<T> reverse() {
        BST<T> revCopy = new BST();
        revCopy.root = new BTNode(this.root.getKey());
        copy(revCopy.root, this.root);
        return revCopy;
    }

    public void copy(BTNode<T> newNode, BTNode<T> oldNode){
        if (oldNode.getRight() != null){
            newNode.setLeft(new BTNode(oldNode.getRight().getKey()));
            copy(newNode.getLeft(), oldNode.getRight());
        }
        if (oldNode.getLeft() != null){
            newNode.setRight(new BTNode(oldNode.getLeft().getKey()));
            copy(newNode.getRight(), oldNode.getLeft());
        }
    }

    
}
