package cn.jzteam.work;

import cn.jzteam.tools.poi.ExcelUtil;
import cn.jzteam.utils.WorkUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KrImportBtcUserUniform {

    public static void main(String[] args) throws Exception {
        String srcPath = "/Users/oker/Documents/work/2019/0424-韩国站/用户迁移/test.xlsx";
        final FileInputStream in = new FileInputStream(srcPath);
        final List<Map<String, String>> maps = ExcelUtil.readExcel(in, 0);
        if(CollectionUtils.isEmpty(maps)){
            System.out.println("查询为空");
            return;
        }

        String descPath = "/Users/oker/Documents/work/2019/0424-韩国站/用户迁移/import_1.sql";
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descPath)));
        for(int i=2;i<maps.size();i++){
            Map<String,String> map = maps.get(i);
            // 原始数据
            final Long kyUserId = Long.valueOf(map.get("1")); // userId
            final String email = map.get("2"); // email
            final String realName = map.get("3"); // real_name
            final String phone = map.get("4"); // phone
            final String totpEncrypt = map.get("5"); // totp_encrypt
            final String createdDate = map.get("6"); // created_date
            final String updateTime = map.get("7"); // update_time
            if(kyUserId == null){
                System.out.println("userId为空，放弃整行");
                continue;
            }
            
            // 入库数据
            final String newEmail = email; 
            final String newPhone = phone; 
            final String newRealName = realName; 
            final String newNickName = WorkUtil.convertNickNme(phone); 
            final String newLoginPwdEncrypt = UUID.randomUUID().toString().replaceAll("-", ""); 
            final String newToptEncrypt = totpEncrypt; // TODO 加密解密
            int newAuthTrade; // 手机google验证开关，绑定即打开。韩国站都是手机注册，但也有解绑的可能
            if(StringUtils.isEmpty(phone) && StringUtils.isEmpty(newToptEncrypt)){
                newAuthTrade = 0;
            } else if (StringUtils.isEmpty(phone)){
                newAuthTrade = 1;
            } else {
                newAuthTrade = 2;
            }
            final int newEmailValidateFlag = StringUtils.isEmpty(newEmail) ? 1 : 0; // 验证邮箱

            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO `btc_user_uniform` (" +
                    "`email`, `phone`, `real_name`, `nick_name`, " +
                    "`id_number`, `passport_num`, `passport_name`, " +
                    "`login_pwd`, `login_pwd_encrypt`, `trade_pwd`, `trade_pwd_encrypt`, `totp_encrypt`, `pwd_flag`, " +
                    "`version`, `delete_flag`, `master_account_id`, `user_from`, `from`, `channel_id`, `area_code`, `created_date`, " +
                    "`wallet_user_id`, `com_user_id`, `conflict_flag`, " +
                    "`auth_login`, `auth_trade`, `email_validate_flag`, `trade_pwd_flag`, `email_verify`, " +
                    "`update_time`, `remark`, `first_from`, `deleted`, `broker_id`)" +
                    "VALUES (")
                    .append("'").append(newEmail).append("', '").append(newPhone).append("','").append(newRealName).append("','").append(newNickName).append("', ")
                    .append("NULL, NULL, NULL, ")
                    .append("'','").append(newLoginPwdEncrypt).append("','','','").append(newToptEncrypt).append("', 1, ")
                    .append("0, 0, 0, 0, 4, 0, '82', '").append(createdDate).append("', ")
                    .append("-1, -1, 0, ")
                    .append("0,").append(newAuthTrade).append(", ").append(newEmailValidateFlag).append(", 0, NULL, ")
                    .append("'").append(updateTime).append("', NULL, 0, 0, 89").append(");");
            System.out.println(sb.toString());
            bw.write(sb.toString());
            bw.newLine();
        }

        bw.flush();
        bw.close();
        System.out.println("写入完成");

    }
}
