package com.unfbx.zdm_push.processor;


import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */
public class ZdmPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    /**
     * 防止重复发送,比较上次发送数据
     */
    private Map<String,Integer> data = new HashMap<>();
    @Override
    public void process(Page page) {


        /*从页面中获取指定标题和url

        <a target="_blank" href="https://www.smzdm.com/p/67462716/">
                        智利车厘子 JJJ级 2.5kg 礼盒装 果径约30-32mm　181.85元                    </a>

         复制xpath
        title
        /html/body/div[1]/div[1]/div[3]/div[2]/div[1]/a/text()
        url
        /html/body/div[1]/div[1]/div[3]/div[2]/div[1]/a
         */

        String url = page.getHtml().xpath("/html/body/div[1]/div[1]/div[3]/div[2]/div[1]/a").$("a","href").toString();
        String name = page.getHtml().xpath("/html/body/div[1]/div[1]/div[3]/div[2]/div[1]/a/text()").toString();
        //server 酱推送限制长度256


        //关键词筛选
        if(name.contains("奶"));
        if (name.contains("奶")) {
            page.putField("flag", true);
        }else {
            //不推送
            page.putField("flag",false);
            return;
        }


        if(StringUtils.isNotBlank(name) && name.length() > 256){
            name = name.substring(0,200);
        }
        page.putField("url",url);
        page.putField("name",name);
        //默认不推送
        page.putField("flag",false);
        if(StringUtils.isNotBlank(page.getHtml().xpath("/html/body/div[1]/div[1]/div[3]/div[2]/div[1]/a").$("a","href").toString())){
            String[] split = url.split("/");
            //存在不推送
            if(data.get(split[split.length-1]) != null){
                page.putField("flag",false);
                return;
            }
            page.putField("flag",true);
            //不存在存进去
            data.put(split[split.length-1],1);
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

}
