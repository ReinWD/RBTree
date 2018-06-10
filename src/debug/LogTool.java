package debug;

import moe.reinwd.RedBlackTree;

import java.time.Instant;
import java.util.Date;

public class LogTool {

    public static void log(RedBlackTree.Node node, Action action){
        StringBuilder sb = new StringBuilder();

        addTimeStamp(sb);

        sb.append(action.toString());
        sb.append(getNodeInfo(node));
        sb.append(", left child ");
        sb.append(getNodeInfo(node.getLeftChild()));
        sb.append(", right child ");
        sb.append(getNodeInfo(node.getRightChild()));
        sb.append(", parent ");
        sb.append(getNodeInfo(node.getParent()));
        sb.append(", ");
        if (node.isRightChild())sb.append("Right child");
        else sb.append("LeftChild");
        System.out.println(sb.toString());
    }

    private static String getNodeInfo(RedBlackTree.Node node){
        return node==null?null:String.format("\"%s\",color %s",node.getValue(),getColor(node.getColor()));
    }

    private static String getColor(int color){
        if (color == RedBlackTree.BLACK)return "BLACK";
        else return "RED";
    }


    public static void logValidate(boolean isValidate,String cause){
        StringBuilder sb = new StringBuilder();
        addTimeStamp(sb);
        sb.append("Tree is ");
        if (!isValidate) sb.append("not ");
        sb.append("validate");
        if (!isValidate) sb.append(" ").append(cause);
        System.out.println(sb.toString());
    }

    static void addTimeStamp(StringBuilder sb){
        sb.append("[").append(Date.from(Instant.now())).append("] ");
    }

    public enum Action{
        ADD("Adding"),
        DELETE("Deleting");

        Action(String value){
        }
    }
}
