package com.kuyuzhiqi.utils.structure.tree;

import java.util.List;

class Node<T> {
    private T entity;
    private Node father;
    private List<Node<T>> children;
    private boolean isLeaf;
    private boolean isChecked; //本项是否勾选，两种状态
    private Boolean childrenChecked;    //描述子节点的勾选状态，三种状态，true=选中，false=未选，null=半选
    private boolean choosable;  //是否可勾选
    private int level;

    public Node(T entity, Node father, List<Node<T>> children, int level, boolean choosable) {
        this.entity = entity;
        this.father = father;
        this.children = children;
        this.childrenChecked = false;
        this.isLeaf = true;
        this.level = level;
        this.choosable = choosable;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Node getFather() {
        return father;
    }

    public void setFather(Node father) {
        this.father = father;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Node<T>> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Boolean getChildrenChecked() {
        return childrenChecked;
    }

    public void setChildrenChecked(Boolean childrenChecked) {
        this.childrenChecked = childrenChecked;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isChoosable() {
        return choosable;
    }

    public void setChoosable(boolean choosable) {
        this.choosable = choosable;
    }


}
