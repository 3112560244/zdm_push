package com.unfbx.zdm_push.pipeline;

import com.unfbx.zdm_push.constant.KeyType;
import com.unfbx.zdm_push.constant.ServerResponse;
import com.unfbx.zdm_push.pojo.ZdmInfo;
import com.unfbx.zdm_push.service.ServerPush;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */
@Service
@Log
public class KeyPipeline implements Pipeline {

    @Autowired
    private ServerPush serverPush = new ServerPush();
    /**
     * 微信推送类型
     */
    @Value("${key.type}")
    private String keyType;

    @Override
    public void process(ResultItems resultItems, Task task) {
        ZdmInfo zdmInfo = new ZdmInfo();
//        zdmInfo.setName(resultItems.get("name"));
//        zdmInfo.setUrl(resultItems.get("url"));
        zdmInfo.setMapList(resultItems.get("list"));


//        if(null != resultItems.get("image")){
//            zdmInfo.setImage(resultItems.get("image"));
//        }else {
//            zdmInfo.setImage("暂无");
//        }
//
//        if(null != resultItems.get("text")){
//            zdmInfo.setText(resultItems.get("text"));
//        }else {
//            zdmInfo.setText("暂无");
//        }


        if (resultItems.get("flag")){
            if(StringUtils.isBlank(keyType)){
                log.info("~~~~~~~~~~~~~~~~为配置微信推送类型，到application.yml配置~~~~~~~~~~~~~~~~");
            }
            ServerResponse serverResponse = null;
            if(KeyType.SERVER_J.getValue().equals(keyType)){
                serverResponse = serverPush.pushJMsgToWechat(zdmInfo);
            }
            if(KeyType.PUSH_PLUS.getValue().equals(keyType)){
                serverResponse = serverPush.pushPushMsgToWechat(zdmInfo);
            }
            log.info("~~~~~~~~~~~~~~~~"+serverResponse.getMsg()+"~~~~~~~~~~~~~~~~");
        }else {
            log.info("~~~~~~~~~~~~~~~~暂无更新数据~~~~~~~~~~~~~~~~");
        }
    }
}
