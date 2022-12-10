package com.unfbx.zdm_push.service;

import com.unfbx.zdm_push.api.ServerJPushApi;
import com.unfbx.zdm_push.api.ServerPushPlusApi;
import com.unfbx.zdm_push.constant.ServerJPushResponse;
import com.unfbx.zdm_push.constant.ServerPushPlusResponse;
import com.unfbx.zdm_push.constant.ServerResponse;
import com.unfbx.zdm_push.pojo.ZdmInfo;
import lombok.extern.java.Log;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */
@Service
@Log
public class ServerPush {


    @Resource
    private ServerJPushApi serverJPushApi;
    @Resource
    private ServerPushPlusApi serverPushPlusApi;
    /**
     * 私人密钥
     */
    @Value("${key.value}")
    private String keyValue;


    public ServerResponse pushJMsgToWechat(ZdmInfo zdmInfo){
        if(StringUtils.isBlank(keyValue)){
            return ServerResponse.createByError("为配置微信推送密钥，到application.yml配置");
        }
        ServerJPushResponse serverPushResponse = serverJPushApi.sendToServerJiang(keyValue,zdmInfo.getName(), zdmInfo.getUrl());

        if (serverPushResponse == null){
            log.info("推送失败：系统异常");
            return ServerResponse.createByError("推送失败");
        }
        if(serverPushResponse.isSuccess(serverPushResponse.getErrmsg())){
            return ServerResponse.createBySuccess("推送成功");
        }
        log.info("推送失败："+serverPushResponse.getErrmsg());
        return ServerResponse.createByError("推送失败");
    }

    public ServerResponse pushPushMsgToWechat(ZdmInfo zdmInfo){
        if(StringUtils.isBlank(keyValue)){
            return ServerResponse.createByError("为配置微信推送密钥，到application.yml配置");
        }
        Map<String,String> param = new HashMap<>();
        param.put("token",keyValue);
        param.put("title","近20条数据");

        List<Map<String, String>> mapList = zdmInfo.getMapList();
        String content = "";
        for(int i = 0; i< mapList.size(); i++){
            content+=i+"\n";
            Map<String, String> map = mapList.get(i);
            content +="线报url  "+map.get("线报url")+"\n";
            content +="线报标题  "+map.get("线报标题")+"\n";
            content +="详细内容  "+map.get("详细内容")+"\n";
            content +="图片地址  "+map.get("图片地址")+"\n";
        }

        param.put("content",content);
        param.put("template","html");
        //群组 不填推送给自己
        param.put("topic","奶");

        ServerPushPlusResponse serverPushResponse = serverPushPlusApi.sendToServerPushPlus(param);

        if (serverPushResponse == null){
            log.info("推送失败：系统异常");
            return ServerResponse.createByError("推送失败");
        }
        if(serverPushResponse.isSuccess(serverPushResponse.getCode())){
            return ServerResponse.createBySuccess("推送成功");
        }
        log.info("推送失败："+serverPushResponse.getMsg());
        return ServerResponse.createByError("推送失败");
    }
}
