package cn.jzteam.module.eureka.feign;

import cn.jzteam.work.model.ResponseResult;
import com.okcoin.ucenter.users.dto.KrKyc.KycAssetLevelOneMsgDTO;
import com.okcoin.ucenter.users.dto.admin.NexLexBatchAuthDTO;
import com.okcoin.ucenter.users.dto.admin.UserKycDetail4AssetDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by tiny on 2018/1/23.
 */
@FeignClient(value = "okcoin-users-service-zhujizhi", path = "/admin/v3/users/kyc")
public interface UsersKycServiceClient {


    // --------- 法币接口区 ------------
    /**
     * 根据userId的查询基础信息
     * 20180327 增加国家和地址两个字段
     * @param userId
     * @return
     */
    @GetMapping("/asset-userkycinfo-userid")
    ResponseResult<KycAssetLevelOneMsgDTO> getKycInfoForAssetRWC(@RequestParam("userId") final long userId);

    /**
     * 根据userId的查询kyc详细信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/asset-userkycinfo-detail-userid")
    ResponseResult<UserKycDetail4AssetDTO> getKycInfoDetailForAsset(@RequestParam("userId") final long userId);

    // ------------ risk接口区 ---------------
    @PostMapping("/risk-list/{startId}")
    ResponseResult<NexLexBatchAuthDTO> riskList(@PathVariable("startId") final Long startId, @RequestParam("limit") final Integer limit);

    // ----------- admin接口区 -------------


}
