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
public class KeyPageProcessor implements PageProcessor {

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





        String url = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li[1]/div/div[2]/h5/a[2]").$("a","href").toString();
        String name = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li[1]/div/div[2]/h5/a[2]").$("a","title").toString();
        //server 酱推送限制长度256
        String price = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li[1]/div/div[2]/h5/a[2]/div/text()").toString();
        url+="\n"+price;



        if(StringUtils.isNotBlank(name) && name.length() > 256){
            name = name.substring(0,200);
        }
        page.putField("url",url);
        page.putField("name",name);
        //默认不推送
        page.putField("flag",false);
        if(StringUtils.isNotBlank(url)){
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
