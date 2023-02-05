package cn.jzteam.algorithm.pathplanning;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathPlanTest {

    private Map<String, Integer> weightMap = new HashMap<>();
    private Map<String, List<String>> routeMap = new HashMap<>();

    private Map<String, RouteNode> globalMap = new HashMap<>();

    private List<RouteNode> globalList = new ArrayList<>();

    public static void main(String[] args) {
        String[][] nodes = new String[6][2];
        nodes[0][0] = "A";
        nodes[0][1] = "1";
        nodes[1][0] = "B";
        nodes[1][1] = "2";
        nodes[2][0] = "C";
        nodes[2][1] = "3";
        nodes[3][0] = "D";
        nodes[3][1] = "2";
        nodes[4][0] = "E";
        nodes[4][1] = "5";
        nodes[5][0] = "F";
        nodes[5][1] = "4";

        String[][] routes = new String[8][2];
        routes[0][0] = "A";
        routes[0][1] = "B";
        routes[1][0] = "A";
        routes[1][1] = "C";
        routes[2][0] = "B";
        routes[2][1] = "C";
        routes[3][0] = "C";
        routes[3][1] = "D";
        routes[4][0] = "D";
        routes[4][1] = "B";
        routes[5][0] = "A";
        routes[5][1] = "E";
        routes[6][0] = "E";
        routes[6][1] = "C";
        routes[7][0] = "D";
        routes[7][1] = "F";

        String startNode = "A";

        new PathPlanTest().begin(nodes, routes, startNode);

    }

    /**
     * 传入n个节点及其权重nodeMap
     * 传入m个路径，逗号分割，-> 表示方向：A->B,B->C
     * 传入起点
     *
     * 计算得出权重之和最大的路径，返回其权重之和
     *
     * @param nodes 元素数组都只有2个元素index0->节点名, index1->权重
     * @param routes 元素数组都只有2个元素index0->路径起点, index1->路径终点
     * @param startNode
     * @return
     */
    public void begin(String[][] nodes, String[][] routes, String startNode){
        resolveNodeWeight(nodes);
        resolveRoutes(routes);

        buildNodeMap(startNode);

        int maxWeight = getMaxTotalWeight();

        System.out.println("---------结果如下：");
        System.out.println("最大权重："+maxWeight);
    }

    /**
     * 原始池子：Map<String , List<String>>  起点nodeName -> 终点nodeNameList
     * 起点：startNode
     * 目标池子：树状结构，遍历每一个叶子节点，从原始池子中获取终点nodeNameList，同时设置叶子节点为非叶子结点
     * 终点nodeNameList筛选规则：
     *      当前节点所在路径不包含该终点nodeName
     *
     */
    private void buildNodeMap(String startNode){
        List<RouteNode> nodeList = new ArrayList<>();
        final Integer w = weightMap.get(startNode);

        final RouteNode rootNode = new RouteNode(startNode, w);
        nodeList.add(rootNode);
//        globalMap.put(rootNode.getNodeName(), rootNode);
        globalList.add(rootNode);

        // 开始构建树结构
        find(nodeList);
    }

    private void find(List<RouteNode> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }

        // 需要寻找子节点的叶子节点
        List<RouteNode> tempNodeList = new ArrayList<>();

        for(RouteNode node : list){
            // 根据当前nodeName查找原始池子中的路径
            final List<String> nodeList = routeMap.get(node.getNodeName());
            if(CollectionUtils.isEmpty(nodeList)){
                continue;
            }
            // 找到了子路径，就设置当前节点为非叶子节点
            node.setLeafNode(false);

            for(String nodeName : nodeList){
                if(!node.check(nodeName)){
                    // 检查不通过就放弃：存在闭路环就是不通过
                    continue;
                }
                // 路径中的节点必须有权重，否则报错
                final RouteNode tempNode = new RouteNode(nodeName, weightMap.get(nodeName));
                tempNode.setPreNode(node); // 设置前置节点
                tempNodeList.add(tempNode); // 刚刚添加进来的叶子节点，需要放入tempNodeList待处理
                globalList.add(tempNode); // 保存tree中所有的节点


//                // 相同节点，只取最高权重的路径
//                final RouteNode exsistNode = globalMap.get(tempNode.getNodeName());
//                if(exsistNode == null) {
//                    tempNodeList.add(tempNode); // 刚刚添加进来的叶子节点，需要放入tempNodeList待处理
//                    globalMap.put(tempNode.getNodeName(), tempNode);
//                }else{
//                    if(exsistNode.getTotalWeight() < tempNode.getTotalWeight()){
//                        exsistNode.setPreNode(node);
//                    }else{
//                        // 同样的节点，权重没有已存在的节点高，就不必往globalMap里面放了
//                    }
//                }
            }
        }

        // 递归处理
        find(tempNodeList);

    }

    /**
     * 解析节点权重 成map结构
     * @param nodes
     * @return
     */
    private void resolveNodeWeight(String[][] nodes){
        if(nodes == null || nodes.length == 0){
            return;
        }

        Map<String, Integer> map = new HashMap<>();

        for(String[] arr : nodes){
            if(arr == null || arr.length != 2){
                continue;
            }
            String nodeName = arr[0];
            Integer weight = Integer.parseInt(arr[1]);

            map.put(nodeName, weight);
        }

        this.weightMap = map;
    }

    /**
     * 将routes解析成map结构
     *
     * @param routes
     * @return
     */
    private void resolveRoutes(String[][] routes){
        if(routes == null || routes.length == 0){
            return;
        }

        Map<String, List<String>> map = new HashMap<>();

        for(String[] arr : routes){
            if(arr == null || arr.length != 2){
                continue;
            }
            String startNodeName = arr[0];
            String endNodeName = arr[1];

            List<String> nodes = map.get(startNodeName);
            if(nodes == null){
                nodes = new ArrayList<>();
                map.put(startNodeName, nodes);
            }
            // 路径去重
            if(!contains(nodes, endNodeName)){
                nodes.add(endNodeName);
            }
        }

        this.routeMap = map;
    }

    private static boolean contains(List<String> list, String str){
        if(list == null || list.size() == 0){
            return false;
        }
        for(String s : list){
            if(s != null && s.equals(str)){
                return true;
            }
        }
        return false;
    }

    private int getMaxTotalWeight(){
        if(globalList.size() == 0){
            return 0;
        }

        int maxWeight = 0;
        RouteNode maxNode = null;

        for(RouteNode node : globalList){
            if(!node.isLeafNode()){
                continue;
            }
            final int totalWeight = node.getTotalWeight();
            if(totalWeight > maxWeight){
                maxWeight = totalWeight;
                maxNode = node;
            }
        }

        final StringBuilder sb = new StringBuilder();
        print(sb, maxNode);
        System.out.println("权重最大路径："+sb.substring(2));

        return maxWeight;
    }

    private void print(StringBuilder sb, RouteNode node){
        if(node == null){
            return;
        }
        print(sb, node.getPreNode());
        sb.append("->").append(node.getNodeName());

    }


    /**
     * 节点数据结构
     */
    static class RouteNode {
        // 节点名称
        String nodeName;
        // 节点权重
        int nodeWeight;
        // 是否是叶子节点
        boolean isLeafNode = true;

        // 前置节点
        RouteNode preNode;


        public boolean check(String nodeName){
            // 从当前节点向上追溯
            if(this.nodeName.equals(nodeName)){
                return false;
            }
            // 当前节点通过，就判断当前节点的前置节点
            if(preNode != null){
                // 先让前置节点来判断
                return preNode.check(nodeName);
            }
            // 没有匹配上，就是通过
            return true;
        }

        public int getTotalWeight(){
            int sum = nodeWeight;
            if(preNode == null){
                return sum;
            }
            sum += preNode.getTotalWeight();
            return sum;
        }

        public RouteNode(){}

        public RouteNode(String nodeName, int nodeWeight) {
            this.nodeName = nodeName;
            this.nodeWeight = nodeWeight;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public int getNodeWeight() {
            return nodeWeight;
        }

        public void setNodeWeight(int nodeWeight) {
            this.nodeWeight = nodeWeight;
        }

        public boolean isLeafNode() {
            return isLeafNode;
        }

        public void setLeafNode(boolean leafNode) {
            isLeafNode = leafNode;
        }

        public RouteNode getPreNode() {
            return preNode;
        }

        public void setPreNode(RouteNode preNode) {
            this.preNode = preNode;
        }
    }
}
