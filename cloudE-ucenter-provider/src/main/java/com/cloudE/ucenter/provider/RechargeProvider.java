package com.cloudE.ucenter.provider;

import com.alibaba.fastjson.JSON;
import com.cloudE.dto.BaseResult;
import com.cloudE.entity.User;
import com.cloudE.pay.client.ApplePayClient;
import com.cloudE.ucenter.manager.UserManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author vangao1989
 * @date 2017年7月26日
 */
@Api("RechargeProvider 控制器")
@RestController
public class RechargeProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RechargeProvider.class);

    @Resource
    private UserManager userManager;
    @Resource
    private ApplePayClient applePayClient;


    @ApiOperation(value = "充值接口方法",notes = "根据用户id,金额，类型充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long",paramType = "path"),
            @ApiImplicitParam(name = "amount", value = "金额", required = true, dataType = "Double",paramType = "path"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String",paramType = "path")
    })
    @HystrixCommand(fallbackMethod = "rechargeFallback")
    @PostMapping(value = {"/recharge"})
    //@RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public BaseResult<Boolean> recharge(@RequestParam(value = "userId") Long userId,
                                        @RequestParam(value = "amount") Double amount,
                                        @RequestParam(value = "type") String type) {
        User user = userManager.getUserByUserId(userId);
        LOGGER.info("user {} recharge {},type:{}", user.getUsername(), amount, type);
        BaseResult<Boolean> baseResult = applePayClient.recharge(userId, amount);
        LOGGER.info("user {} recharge  res:{}", user.getUsername(), JSON.toJSONString(baseResult));
        return baseResult;

    }

    private BaseResult<Boolean> rechargeFallback(Long useId, Double amount, String type, Throwable throwable) {
        LOGGER.error("user:{} recharge,amount:{},type:{}, fail:{}", useId, amount, type, throwable.getMessage(), throwable);
        return new BaseResult<>(false, throwable.getMessage());
    }

    @GetMapping(value = "/test/interface")
    public String testInterface(String mid, String userName) {
        System.out.println("mid = " + mid);
        System.out.println("userName = " + userName);
        return mid + " -- " + userName;
    }

}
