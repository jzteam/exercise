package cn.jzteam.algorithm;

import cn.jzteam.utils.MD5Util;


/**
 * Copyright (C) 2011-2021 liepin Inc.All Rights Reserved.
 * 
 * FileName:Test.java
 *
 * Description：简要描述本文件的内容
 *
 * History： 版本号 作者 日期 简要介绍相关操作 1.0 lihonghao 2015年6月26日
 *
 * 规则: select * from shard_config_comp where bucket_index = 9;
 */
public class ShardTable {

    public static void main(String[] args) {
        
        System.out.println(Integer.parseInt("9c1", 16));

        String objectId = "6634199";
        

        int[] indexs = hash(objectId);
        System.out.println("dbIndex = " + (indexs[0] % 2));
        System.out.println("tableIndex = " + (indexs[1]));

        // printSQL();
    }

    public static void printSQL() {

        for (int dbIndex = 0; dbIndex <= 15; dbIndex++) {
            for (int tableIndex = 0; tableIndex <= 15; tableIndex++) {
                String sql = "CREATE TABLE `freebird"
                        + dbIndex
                        + "`.`attendance_schedule_info_"
                        + tableIndex
                        + "`attendance_schedule_info_id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',`comp_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '企业id',"
                        + "";
                System.out.println(sql);
            }
        }
    }

    /**
     * 返回[桶、表]索引
     * 
     * @param objectId
     * @return
     */
    public static final int[] hash(String objectId) {

        String md5Encode = MD5Util.MD5Encode(objectId);
        String m = md5Encode.substring(0, 4);
        System.out.println("m="+m);
        int b = Integer.parseInt(m.substring(0, 3), 16);
        int t = Integer.parseInt(m.substring(3, 4), 16);
        System.out.println("b="+b+",t="+t);
        return new int[] { b, t };
    }
}
