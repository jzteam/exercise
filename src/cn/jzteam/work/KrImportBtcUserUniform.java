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
        String srcPath = "/Users/oker/Documents/work/2019/0424-韩国站/用户迁移/auth01.xlsx";
        final FileInputStream in = new FileInputStream(srcPath);
        final List<Map<String, String>> maps = ExcelUtil.readExcel(in, 0, 1);
        if(CollectionUtils.isEmpty(maps)){
            System.out.println("查询为空");
            return;
        }

        String descPath = "/Users/oker/Documents/work/2019/0424-韩国站/用户迁移/import_result01.sql";
        final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(descPath)));
        int num = 10000;
        for(int i=2;i<maps.size();i++){
            Map<String,String> map = maps.get(i);
            // 原始数据
            final Long krUserId = Long.valueOf(map.get("1")); // userId
            final String email = map.get("2"); // email
            final String realName = map.get("3"); // real_name
            final String phone = map.get("4"); // phone
            final String toptEncrypt = map.get("5"); // totp_encrypt
            final String createdDate = map.get("6"); // created_date
            final String updateTime = map.get("7"); // update_time
            if(krUserId == null){
                System.out.println("userId为空，放弃整行");
                continue;
            }
            
            StringBuilder origin = new StringBuilder();
            origin.append("-- ").append("userId:").append(krUserId)
                    .append(", email:").append(email)
                    .append(", phone:").append(phone)
                    .append(", toptEncrypt:").append(toptEncrypt)
                    .append(", realName:").append(realName)
                    .append(", createdDate:").append(createdDate)
                    .append(", updateTime:").append(updateTime);
            
            // 入库数据
            final String newEmail = StringUtils.isBlank(email) ? null : email; // email 不能为空串
            final String newPhone = StringUtils.isBlank(phone) ? null : phone; // phone 不能为空串
            final String newRealName = realName; 
            final String newNickName = WorkUtil.convertNickNme(phone); 
            final String newLoginPwdEncrypt = UUID.randomUUID().toString().replaceAll("-", ""); 
            final String newToptEncrypt = toptEncrypt; // 解密加密，跟我们一样的密钥，不用处理
            int newAuthTrade; // 手机google验证开关，绑定即打开。韩国站都是手机注册，但也有解绑的可能
            if(StringUtils.isEmpty(phone) && StringUtils.isEmpty(newToptEncrypt)){
                newAuthTrade = 0;
            } else if (StringUtils.isEmpty(phone)){
                newAuthTrade = 1;
            } else {
                newAuthTrade = 2;
            }
            final int newEmailValidateFlag = StringUtils.isEmpty(newEmail) ? 0 : 1; // 验证邮箱
            
            if(StringUtils.isBlank(email) && StringUtils.isBlank(phone)){
                System.out.println("email和phone都为空，放弃整行");
                continue;
            }

            StringBuilder sql = new StringBuilder();
            if(i == 2 || i % num == 0) {
                if(i > 2) {
                    bw.newLine();
                    bw.newLine();
                }
                
                // 每10000行写一次表名
                sql.append("INSERT INTO `btc_user_uniform` (`email`, `phone`, `real_name`, `nick_name`, " +
                        "`id_number`, `passport_num`, `passport_name`, " +
                        "`login_pwd`, `login_pwd_encrypt`, `trade_pwd`, `trade_pwd_encrypt`, `totp_encrypt`, `pwd_flag`, " +
                        "`version`, `delete_flag`, `master_account_id`, `user_from`, `from`, `channel_id`, `area_code`, `created_date`, " +
                        "`wallet_user_id`, `com_user_id`, `conflict_flag`, " +
                        "`auth_login`, `auth_trade`, `email_validate_flag`, `trade_pwd_flag`, `email_verify`, " +
                        "`update_time`, `remark`, `first_from`, `deleted`, `broker_id`)" +
                        "VALUES \n");
            }
            sql.append("(");
            if(StringUtils.isNotBlank(newEmail)){
                sql.append("'").append(newEmail).append("', ");
            }else {
                sql.append("NULL, ");
            }
            if(StringUtils.isNotBlank(newPhone)){
                sql.append("'").append(newPhone).append("', ");
            }else{
                sql.append("NULL, ");
            }
            sql.append("'");
            sql.append(newRealName).append("','").append(newNickName).append("', ")
                    .append("NULL, NULL, NULL, ")
                    .append("'','").append(newLoginPwdEncrypt).append("','','','").append(newToptEncrypt).append("', 1, ")
                    .append("0, 0, 0, 0, 4, 0, '82', '").append(createdDate).append("', ")
                    .append("-1, -1, 0, ")
                    .append("0,").append(newAuthTrade).append(", ").append(newEmailValidateFlag).append(", 0, NULL, ")
                    .append("'").append(updateTime).append("', 'kr import', 0, 0, 89").append(")");
            if(i % num == num - 1 || i == maps.size() - 1){
                sql.append(";");
            } else {
                sql.append(",");
            }
            
            System.out.println(sql.toString());
            // 注释：原始数据
            // bw.write(origin.toString());
            // bw.newLine();
            // sql：入库数据
            bw.write(sql.toString());
            bw.newLine();
        }

        bw.flush();
        bw.close();
        System.out.println("写入完成");

    }
}
