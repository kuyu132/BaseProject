package com.kuyuzhiqi.utils.structure.tree;

import com.kuyuzhiqi.utils.common.ListUtils;
import com.kuyuzhiqi.utils.common.ObjectUtils;
import com.kuyuzhiqi.utils.debug.LogUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 通用树结构:采用逐层构建的策略
 *
 * @param <T> 实体类型
 * @param <K> ID类型
 */
public abstract class Tree<T, K> {
    private List<Node> rootNodeList;
    private List<T> mAllEntityList;
    private TreeListener listener;

    private List<K> limitCheckIdList = new ArrayList<>();  //限制能勾选的节点

    public void buildTree(T root, List<T> allNodeList) {
        List<T> rootList = new ArrayList<>();
        rootList.add(root);
        buildTree(rootList, allNodeList);
    }

    public void buildTree(List<T> rootList, List<T> allEntityList) {
        this.mAllEntityList = new ArrayList<>();
        this.mAllEntityList.addAll(allEntityList);
        //获取根结点
        allEntityList.removeAll(rootList);    //去掉根节点
        rootNodeList = new ArrayList<>();
        for (T entity : rootList) {
            boolean choosable = false;
            if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                choosable = true;
            }
            Node rootNode = new Node(entity, null, new ArrayList<Node>(), 1, choosable);
            rootNodeList.add(rootNode);
        }

        //其他结点加入树
        CopyOnWriteArrayList childrenNodeList = new CopyOnWriteArrayList();
        childrenNodeList.addAll(allEntityList);
        addToTreeBreadthFirst(childrenNodeList);
        /*
        for (Node rootNode : rootNodeList) {
            addToTreeDepthFirst(rootNode, childrenNodeList);
        }
        */

        //printTree("buildTree");
    }

    private void addToTreeBreadthFirst(CopyOnWriteArrayList<T> nodeList) {
        Queue<Node> queue = new ArrayDeque<>();
        for (Node root : rootNodeList) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            Node fatherNode = queue.poll();

            if (nodeList.isEmpty()) {
                return;
            }

            List<Node> children = new ArrayList<>();
            for (T entity : nodeList) {
                if (getFatherKey(entity).equals(getKey(fatherNode))) {
                    nodeList.remove(entity);
                    fatherNode.setLeaf(false);

                    boolean choosable = false;
                    if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                        choosable = true;
                    }
                    Node newNode = new Node(entity, fatherNode, new ArrayList<Node>(),
                            fatherNode.getLevel() + 1, choosable);
                    newNode.setLeaf(true);
                    queue.offer(newNode);

                    children.add(newNode);
                }
            }
            fatherNode.setChildren(children);
        }
    }

    /**
     * 逐层构建树，并且不需要全部节点
     */
    public void buildRootNode(List<T> rootList) {
        rootNodeList = new ArrayList<>();
        for (T entity : rootList) {
            boolean choosable = false;
            if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                choosable = true;
            }
            Node rootNode = new Node(entity, null, new ArrayList<Node>(), 1, choosable);
            rootNodeList.add(rootNode);
        }
    }

    /**
     * 构建子节点，每次只构建一层
     */
    public void addToTreeBreadthFirst2(List<Integer> mChildIndex, List<T> childNodeList) {
        Node fatherNode = null;
        Node tmpFatherNode;
        List<Node> tmpFatherNodeList = rootNodeList;
        for (int i = 0; i < mChildIndex.size(); i++) {
            tmpFatherNode = tmpFatherNodeList.get(mChildIndex.get(i));
            tmpFatherNodeList = tmpFatherNode.getChildren();
            if (i == (mChildIndex.size() - 1)) {
                if (ListUtils.isEmpty(tmpFatherNodeList)) {
                    fatherNode = tmpFatherNode;
                } else {
                    //最后索引如果有子节点，说明已经构建了
                    return;
                }
            }
        }
        if (fatherNode == null) {
            return;
        }

        List<Node> children = new ArrayList<>();
        for (T entity : childNodeList) {
            fatherNode.setLeaf(false);
            boolean choosable = false;
            if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                choosable = true;
            }
            Node newNode = new Node(entity, fatherNode, new ArrayList<Node>(),
                    fatherNode.getLevel() + 1, choosable);
            newNode.setChecked(fatherNode.isChecked());
            newNode.setChildrenChecked(fatherNode.isChecked());
            newNode.setLeaf(true);
            children.add(newNode);
        }
        fatherNode.setChildren(children);
    }

    /**
     * 根据key来构建子节点，每次只构建一层
     */
    public void addToTreeBreadthFirst2(K k, List<T> childNodeList) {
        Node fatherNode = findNodeByKey(k);

        if (fatherNode == null && !ListUtils.isEmpty(fatherNode.getChildren())) {
            return;
        }

        List<Node> children = new ArrayList<>();
        for (T entity : childNodeList) {
            fatherNode.setLeaf(false);
            boolean choosable = false;
            if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                choosable = true;
            }
            Node newNode = new Node(entity, fatherNode, new ArrayList<Node>(),
                    fatherNode.getLevel() + 1, choosable);
            newNode.setChecked(fatherNode.isChecked());
            newNode.setChildrenChecked(fatherNode.isChecked());
            newNode.setLeaf(true);
            children.add(newNode);
        }
        fatherNode.setChildren(children);
    }

    private void addToTreeDepthFirst(Node fatherNode, CopyOnWriteArrayList<T> nodeList) {
        if (nodeList.isEmpty()) {
            return;
        }

        List<Node> children = new ArrayList<>();
        for (T entity : nodeList) {
            if (getFatherKey(entity).equals(getKey(fatherNode))) {
                nodeList.remove(entity);
                fatherNode.setLeaf(false);

                boolean choosable = false;
                if (limitCheckIdList.isEmpty() || limitCheckIdList.contains(getKey(entity))) {
                    choosable = true;
                }
                Node newNode = new Node(entity, fatherNode, new ArrayList<Node>(),
                        fatherNode.getLevel() + 1, choosable);
                newNode.setLeaf(true);
                addToTreeDepthFirst(newNode, nodeList);

                children.add(newNode);
            }
        }
        fatherNode.setChildren(children);
    }

    public void refreshTreeChecked(List<K> checkedList) {
        if (null != checkedList) {
            for (K key : checkedList) {
                checkByKey(key, true, false);
            }
        }
    }

    /**
     * 是否叶结点
     */
    public boolean isLeaf(K key) {
        Node node = findNodeByKey(key);
        if (node != null) {
            return node.isLeaf();
        }
        return false;
    }

    /**
     * 根据key查询树结点
     */
    private Node findNodeByKey(K key) {
        if (rootNodeList == null) {
            return null;
        }

        List<Node> resultList = new ArrayList<>();
        for (Node rootNode : rootNodeList) {
            findNodeByKeyRecursion(rootNode, key, false, resultList);
            if (resultList.size() > 0) {
                return resultList.get(0);
            }
        }
        return null;
    }

    private void findNodeByKeyRecursion(Node currentNode, K targetKey, boolean includeChildren,
            List<Node> resultList) {
        if (getKey(currentNode).equals(targetKey)) {
            resultList.add(currentNode);
            if (!includeChildren) {
                return;
            }
        }

        List<Node> children = currentNode.getChildren();
        if (children.isEmpty()) {
            return;
        }

        for (Node child : children) {
            if (includeChildren) {
                resultList.add(child);
            }
            findNodeByKeyRecursion(child, targetKey, includeChildren, resultList);
        }
    }

    private void doCheck(Node currentNode, boolean isChecked, boolean includeChildren) {
        if (currentNode.isChecked() == isChecked
                && currentNode.getChildrenChecked() != null
                && currentNode.getChildrenChecked() == isChecked) {
            //本节点和子节点符合状态
            return;
        }

        Queue<Node> queue = new ArrayDeque();
        queue.add(currentNode);

        for (; ; ) {
            if (queue.isEmpty()) {
                break;
            }
            Node node = queue.poll();
            if (node.isChoosable()) {
                node.setChecked(isChecked);
            }

            //如果是叶节点，触发变更ChildrenChecked
            if (node.isLeaf()) {
                node.setChildrenChecked(isChecked);
            }
            changeFatherCheckedRecursion(node);

            if (!includeChildren) {
                continue;
            }

            List<Node> children = node.getChildren();
            if (null == children || children.isEmpty()) {
                continue;
            }

            for (Node child : children) {
                queue.add(child);
            }
        }

        printTree(getKey(currentNode));
    }

    /**
     * 勾选一项下的子项
     */
    public void checkSub(K key, boolean isChecked, boolean includeChildren) {
        Node currentNode = findNodeByKey(key);
        if (null == currentNode) {
            return;
        }

        List<Node> children = currentNode.getChildren();
        for (Node child : children) {
            doCheck(child, isChecked, includeChildren);
        }

        callListener();
    }

    /**
     * 勾选一项
     */
    public void checkByKey(K key, boolean isChecked, boolean includeChildren) {
        Node currentNode = findNodeByKey(key);
        if (null == currentNode) {
            return;
        }
        doCheck(currentNode, isChecked, includeChildren);

        callListener();
    }

    /**
     * 勾选全部
     */
    public void checkAll(boolean isChecked) {
        for (Node node : rootNodeList) {
            checkByKey(getKey(node), isChecked, true);
        }
    }

    private void callListener() {
        if (null != listener) {
            listener.onCheck();

            Boolean checkedState = getCheckedState();
            if (null != checkedState) {
                listener.onCheckAll(checkedState);
            }
        }
    }

    /**
     * 获取树的勾选情况 true=全选 null=半选 false=全不选
     */
    private Boolean getCheckedState() {
        if (rootNodeList == null) {
            return null;
        }

        boolean checkAll = true;
        boolean unCheckAll = true;

        Queue<Node> queue = new ArrayDeque<>();
        for (Node node : rootNodeList) {
            queue.add(node);
        }

        for (; ; ) {
            if (queue.isEmpty()) {
                break;
            }
            Node node = queue.poll();

            if (node.isChoosable()) {
                if (node.isChecked()) {
                    unCheckAll = false;
                } else {
                    checkAll = false;
                }
            }

            if (!unCheckAll && !checkAll) {
                //半选状态
                return null;
            }

            List<Node> children = node.getChildren();
            if (null == children) {
                continue;
            }
            for (Node child : children) {
                queue.add(child);
            }
        }
        if (checkAll) {
            return true;
        }
        if (unCheckAll) {
            return false;
        }
        return null;
    }

    public boolean isCheckedAll() {
        Boolean flag = getCheckedState();
        return flag != null && flag;
    }

    /**
     * 没选任何一个
     * @return
     */
    public boolean isUncheckedAll() {
        Boolean flag = getCheckedState();
        return flag != null && !flag;
    }

    /**
     * 获取下层节点的勾选情况
     */
    public Map<K, Boolean> getNextLevelChecked(K key) {
        Map<K, Boolean> result = new HashMap<>();
        if (key == null) {
            //为空，获取根结点情况
            for (Node child : rootNodeList) {
                result.put(getKey(child), child.isChecked());
            }
            return result;
        }

        Node node = findNodeByKey(key);
        List<Node> children = node.getChildren();

        for (Node child : children) {
            result.put(getKey(child), child.isChecked());
        }
        return result;
    }

    /**
     * 获取下层节点的ChildrenChecked
     */
    public Map<K, Boolean> getNextLevelChildrenChecked(K key) {
        Map<K, Boolean> result = new HashMap<>();
        if (key == null) {
            //为空，获取根结点情况
            for (Node child : rootNodeList) {
                result.put(getKey(child), child.getChildrenChecked());
            }
            return result;
        }

        Node node = findNodeByKey(key);
        List<Node> children = node.getChildren();

        for (Node child : children) {
            result.put(getKey(child), child.getChildrenChecked());
        }
        return result;
    }

    /**
     * 获取已勾选的路径节点
     */
    public List<K> getCheckedPath() {
        if (rootNodeList == null) {
            return null;
        }

        Queue<Node> queue = new ArrayDeque<>();
        for (Node rootNode : rootNodeList) {
            queue.add(rootNode);
        }

        List<Node> nodeResultList = new ArrayList<>();
        for (; ; ) {
            if (queue.isEmpty()) {
                break;
            }

            Node node = queue.poll();
            if (ObjectUtils.equals(node.getChildrenChecked(), Boolean.TRUE)) {
                nodeResultList.add(node);
            } else if (node.getChildrenChecked() == null) { //半选才查子层
                List<Node> children = node.getChildren();
                if (children.isEmpty()) {
                    continue;
                }
                for (Node child : children) {
                    queue.add(child);
                }
            }
        }

        return getAllKeyByNodeList(nodeResultList);
    }

    /**
     * 获取已勾选的叶节点
     */
    public List<K> getCheckedLeaf() {
        if (rootNodeList == null) {
            return null;
        }

        Queue<Node> queue = new ArrayDeque<>();
        for (Node rootNode : rootNodeList) {
            queue.add(rootNode);
        }

        List<Node> nodeResultList = new ArrayList<>();
        for (; ; ) {
            if (queue.isEmpty()) {
                break;
            }

            Node node = queue.poll();
            if (node.isLeaf()) {
                if (node.isChecked()) {
                    nodeResultList.add(node);
                }
            } else if (node.getChildrenChecked() == null
                    || node.getChildrenChecked() == true) {
                List<Node> children = node.getChildren();
                if (children.isEmpty()) {
                    continue;
                }
                for (Node child : children) {
                    queue.add(child);
                }
            }
        }

        return getAllKeyByNodeList(nodeResultList);
    }

    /**
     * 获取所有已勾选节点（父节点，子节点同时有效）
     */
    public List<K> getAllCheckedKey() {
        if (rootNodeList == null) {
            return null;
        }

        Queue<Node> queue = new ArrayDeque<>();
        for (Node rootNode : rootNodeList) {
            queue.add(rootNode);
        }

        List<Node> nodeResultList = new ArrayList<>();
        for (; ; ) {
            if (queue.isEmpty()) {
                break;
            }

            Node node = queue.poll();
            if (node.isChecked()) {
                nodeResultList.add(node);
            }
            //检查子层级
            List<Node> children = node.getChildren();
            if (children.isEmpty()) {
                continue;
            }
            for (Node child : children) {
                queue.add(child);
            }
        }

        return getAllKeyByNodeList(nodeResultList);
    }

    /**
     * 收集Node的Key
     */
    private List<K> getAllKeyByNodeList(List<Node> nodeList) {
        List<K> keyList = new ArrayList<>();
        for (Node node : nodeList) {
            keyList.add(getKey(node));
        }
        return keyList;
    }

    /**
     * 递归处理子节点的勾选变化
     */
    private void changeFatherCheckedRecursion(Node currentNode) {
        Node father = currentNode.getFather();
        if (father == null) {
            return;
        }

        //检查同层的勾选状态
        List<Node> children = father.getChildren();
        boolean isSame = true;

        for (Node child : children) {
            if (getKey(child).equals(getKey(currentNode))) {
                //同一个节点
                if (currentNode.isChoosable()) {
                    if (currentNode.getChildrenChecked() == null
                            || currentNode.getChildrenChecked() != currentNode.isChecked()) {
                        isSame = false;
                        break;
                    }
                }
            } else {
                if (currentNode.isChoosable() && child.isChoosable()) {
                    if (currentNode.isChecked() != child.isChecked()
                            || currentNode.getChildrenChecked() != child.getChildrenChecked()) {
                        isSame = false;
                        break;
                    }
                } else if (currentNode.isChoosable() && !child.isChoosable()) {
                    if ((child.getChildrenChecked() != null
                            && currentNode.isChecked() != child.getChildrenChecked())
                            || currentNode.getChildrenChecked() != child.getChildrenChecked()) {
                        isSame = false;
                        break;
                    }
                } else if (!currentNode.isChoosable() && child.isChoosable()) {
                    if ((currentNode.getChildrenChecked() != null
                            && currentNode.getChildrenChecked() != child.isChecked())
                            || currentNode.getChildrenChecked() != child.getChildrenChecked()) {
                        isSame = false;
                        break;
                    }
                } else {
                    if (currentNode.getChildrenChecked() != currentNode.getChildrenChecked()) {
                        isSame = false;
                        break;
                    }
                }
            }
        }

        //调整父节点的勾选状态
        boolean isFatherChange = false;
        if (isSame) {
            if (father.getChildrenChecked() == null
                    || father.getChildrenChecked() != currentNode.getChildrenChecked()) {
                if (currentNode.isChoosable()) {
                    father.setChildrenChecked(currentNode.isChecked());
                } else {
                    father.setChildrenChecked(currentNode.getChildrenChecked());
                }
                isFatherChange = true;
            }
        } else {
            if (father.getChildrenChecked() != null) {
                father.setChildrenChecked(null);
                changeFatherCheckedRecursion(father);
                isFatherChange = true;
            }
        }

        if (isFatherChange) {
            changeFatherCheckedRecursion(father);
        }
    }

    public List<T> getRootEntityList() {
        if (rootNodeList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        List<T> entityList = new ArrayList<>();
        for (Node<T> rootNode : rootNodeList) {
            entityList.add(rootNode.getEntity());
        }

        return entityList;
    }

    public List<T> getAllEntityList() {
        return mAllEntityList;
    }

    public List<T> getChildrenEntityByKey(K key) {
        Node node = findNodeByKey(key);
        List<Node> childrenNodeList = node.getChildren();

        List<T> resultList = new ArrayList<>();
        for (Node<T> n : childrenNodeList) {
            resultList.add(n.getEntity());
        }
        return resultList;
    }

    public K getKey(Node node) {
        T entity = (T) node.getEntity();
        return getKey(entity);
    }

    public abstract K getFatherKey(T entity);

    public abstract K getKey(T entity);

    public abstract String getName(K key);

    private void printTree(K key) {
        Node inputNode = findNodeByKey(key);
        int currentLevel = inputNode.getLevel();
        if (inputNode == null) {
            return;
        }

        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(inputNode);

        StringBuffer info = new StringBuffer();
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.getLevel() != currentLevel) {
                LogUtils.d(info.toString() + "__________________________________");
                info = new StringBuffer();
                currentLevel = node.getLevel();
            }

            info.append(getName(getKey(node))
                    + "-"
                    + node.isChecked()
                    + "-"
                    + node.getChildrenChecked()
                    + "   ");

            List<Node> children = node.getChildren();
            if (null != children) {
                for (Node child : children) {
                    queue.offer(child);
                }
            }
        }
        LogUtils.d(info.toString());
    }

    public void setLimitCheckIdList(List<K> limitCheckIdList) {
        this.limitCheckIdList = limitCheckIdList;
    }

    public interface TreeListener {
        void onCheck();

        void onCheckAll(boolean isChecked);
    }

    public void setListener(TreeListener listener) {
        this.listener = listener;
    }
}
