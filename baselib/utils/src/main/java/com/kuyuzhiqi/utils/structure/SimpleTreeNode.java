package com.kuyuzhiqi.utils.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树结构
 */
@Deprecated
public class SimpleTreeNode<T> implements Serializable {

    private T data;
    private SimpleTreeNode<T> parent;
    private List<SimpleTreeNode<T>> children;

    public SimpleTreeNode(T data) {
        this.data = data;
        this.children = new ArrayList<SimpleTreeNode<T>>();
    }

    public SimpleTreeNode<T> addChild(T child) {
        SimpleTreeNode<T> childNode = new SimpleTreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public void addChildren(List<T> children) {
        for (T child : children) {
            addChild(child);
        }
    }

    public SimpleTreeNode<T> addChild(SimpleTreeNode<T> childNode) {
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public T getData() {
        return data;
    }

    public SimpleTreeNode<T> getParent() {
        return parent;
    }

    public List<SimpleTreeNode<T>> getChildren() {
        return children;
    }
}
