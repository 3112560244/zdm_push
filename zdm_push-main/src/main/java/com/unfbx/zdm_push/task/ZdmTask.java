package com.unfbx.zdm_push.task;

import com.unfbx.zdm_push.pipeline.KeyPipeline;
import com.unfbx.zdm_push.pipeline.ZdmPipeline;
import com.unfbx.zdm_push.processor.KeyPageProcessor;
import com.unfbx.zdm_push.processor.ZdmPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.java.Log;
import us.codecraft.webmagic.Spider;

import javax.annotation.Resource;

/**
 * @Description 定时拉取：十分钟一次
 * @Author Grt
 * @Date 2020-12-11
 */
@Component
@Log
public class ZdmTask {

    private int i;
    @Resource
    private ZdmPipeline zdmPipeline;

    @Resource
    private KeyPipeline keyPipeline;

    @Value("${blr.uids}")
    private String[] uids;

    @Value("${keys.keys}")
    private String[] keys;

    ZdmPageProcessor zdmPageProcessor = new ZdmPageProcessor();
    KeyPageProcessor keyPageProcessor = new KeyPageProcessor();
    /**
     * 十分钟执行一次
     */
    @Scheduled(cron = "${corn}")
    public void execute() {
//        String str = "奶";

        if(uids == null || uids.length <= 0){
            log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~没有配置需要监控的博主~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return;
        }
        for (String key : keys) {
            Spider.create(keyPageProcessor)
                    .addUrl("https://search.smzdm.com/?c=home&s=" + key + "&v=b&mx_v=a")
                    .addPipeline(keyPipeline)
                    .thread(1)
                    .run();
        }
//        for (String uid : uids){
//            Spider.create(zdmPageProcessor)
//                    .addUrl("https://zhiyou.smzdm.com/member/"+uid+"/baoliao/")
//                    .addPipeline(zdmPipeline)
//                    .thread(1)
//                    .run();
//        }




    }
}
