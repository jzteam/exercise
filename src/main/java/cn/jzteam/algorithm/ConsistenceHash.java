package cn.jzteam.algorithm;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistenceHash {
    
    private TreeMap<Long,String> circle = new TreeMap<>();
    
    {
        circle.put(1L, "node1");
        circle.put(6L, "node1");
        circle.put(12L, "node2");
        circle.put(24L, "node4");
        circle.put(36L, "node4");
        circle.put(48L, "node4");
    }

    public static void main(String[] args) {

        ConsistenceHash hash = new ConsistenceHash();
        
        String string = hash.get(8L);
        System.out.println("node="+string);
        

    }
    
    public String get(Long key){
        SortedMap<Long, String> tailMap = circle.tailMap(key);
        System.out.println("tailMap="+tailMap);
        Long resultKey = null;
        if(tailMap.isEmpty()){
            resultKey = circle.firstKey();
            System.out.print("tailMap为空，取circle第一个：");
        }else{
            resultKey = tailMap.firstKey();
            System.out.print("tailMap不为空，tailMap="+tailMap+",取tailMap第一个：");
        }
        System.out.println(resultKey);
        return circle.get(resultKey);
    }

}
