package cn.jzteam.work;

import cn.jzteam.tools.poi.ExcelUtil;
import cn.jzteam.utils.AESUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

public class KrKycInfoEncrypt {

    public static void main(String[] args) throws Exception {
//        String srcPath = "/Users/oker/Desktop/222.xls";
        String srcPath = "/Users/oker/Desktop/userKycInfo_enterprise_encrypt.xls";
        final FileInputStream in = new FileInputStream(srcPath);
        final List<Map<String, String>> maps = ExcelUtil.readExcel(in, 0);
        if(CollectionUtils.isEmpty(maps)){
            System.out.println("查询为空");
            return;
        }

        String descPath = "/Users/oker/Desktop/result_1.sql";
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descPath)));
        for(Map<String,String> map : maps){
            final Long id = Long.valueOf(map.get("1"));
            if(id == 16757L){
                continue;
            }
            final String idCard = map.get("5");
            final String kycInfo = map.get("6");
            if(id == null){
                System.out.println("id为空");
                continue;
            }
            String idCardResult = AESUtil.encrypt(idCard);
            String kycInfoResult = AESUtil.encrypt(kycInfo);

            StringBuilder sb = new StringBuilder();
            sb.append("update `user_kyc_info` set `id_card`='")
                    .append(idCardResult).append("', `kyc_info`='")
                    .append(kycInfoResult)
                    .append("' where `id`=").append(id).append(";");
            System.out.println(sb.toString());
            bw.write(sb.toString());
            bw.newLine();
        }

        bw.flush();
        bw.close();
        System.out.println("写入完成");

    }
}
