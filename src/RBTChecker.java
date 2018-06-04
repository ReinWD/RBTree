class RBTChecker{
    int count;
    boolean en = true;

    public String getCause() {
        return cause;
    }
    private boolean setCause(String cause) {
        this.cause = cause;
        return false;
    }
    String cause;

    public boolean checkRoute(RedBlackTree.Node root){
        count = 0;
        return isRouteValidate(root,count);
    }

    private boolean isRouteValidate(RedBlackTree.Node node, int count){
        if (!en)return false;
        if (node==null){
            return onFinishRoute(++count);
        }else if (node.color == RedBlackTree.BLACK){
            count++;
        }else {
            if (node.hasLeftChild()&&node.leftChild.color==RedBlackTree.RED)return setCause("RED node \""+node.value+"\"'s left child is RED");
            else if (node.hasRightChild()&&node.rightChild.color == RedBlackTree.RED)return setCause("RED node \""+node.value+"\"'s right child is RED");
        }
        return isRouteValidate(node.leftChild,count)&&isRouteValidate(node.rightChild,count);
    }

    private boolean onFinishRoute(int count){
        if(this.count==0){
            if (count==0){
                en = false;
                return true;
            }else this.count = count;
        }
        if (this.count != count) {
            en = false;
            return setCause("there's a route contains more (" + count + ")blackthan others(" + this.count +").");
        } else return true;
    }

}