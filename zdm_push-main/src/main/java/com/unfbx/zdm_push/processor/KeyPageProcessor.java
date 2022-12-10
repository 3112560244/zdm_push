package com.unfbx.zdm_push.processor;


import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */

public class KeyPageProcessor implements PageProcessor {

    private List<Map<String,String>> list = new ArrayList<Map<String, String>>();

    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
            .addCookie("Cookie","__ckguid=6OA317JwIwsr1b6QXrXJ1b6; sensorsdata2015jssdkcross={\"distinct_id\":\"181c9c509e2319-05a377bf33d225-26021b51-1327104-181c9c509e36c0\",\"first_id\":\"\",\"props\":{\"$latest_traffic_source_type\":\"自然搜索流量\",\"$latest_search_keyword\":\"未取到值\",\"$latest_referrer\":\"https://www.baidu.com/link\",\"$latest_landing_page\":\"https://www.smzdm.com/p/11943162/\"},\"$device_id\":\"181c9c509e2319-05a377bf33d225-26021b51-1327104-181c9c509e36c0\"}; Hm_lvt_9b7ac3d38f30fe89ff0b8a0546904e58=1656947543; device_id=295847661656947541670930125f9a913dc358a37d8afc4fbb2ca4f3; smzdm_user_source=515F4FEF84984E90EA441CC4EC9AA253; ss_ab=ss81; ssmx_ab=mxss13; s_his=奶粉,奶")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
            ;

    /**
     * 防止重复发送,比较上次发送数据
     */
    private Map<String,Integer> data = new HashMap<>();

    @Pointcut
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



        String name="";
        String url="";
        String toUrl = "";
        String image = "";
        String text = "";
        //判断是否为列表的url
        if(!page.getUrl().regex("https://www.smzdm.com/p/.*").match()) {
            list = new ArrayList<Map<String, String>>();
            for(int i=1;i<=20;i++){
//                url = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li[1]/div/div[2]/h5/a[1]").$("a","href").toString();
                url = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li["+i+"]/div/div[2]/h5/a[1]").$("a","href").toString();
                name = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li["+i+"]/div/div[2]/h5/a[1]").$("a","title").toString();


                toUrl = url;

                String price = page.getHtml().xpath("/html/body/div[1]/div/div/div[1]/div[2]/ul/li["+i+"]/div/div[2]/h5/a[2]/div/text()").toString();
                url+="\n"+"价格："+price;

                //跳转详情页
                Request request = new Request(toUrl);

                request.putExtra("url", url);
                request.putExtra("name", name);
                request.putExtra("num",i);
                //setSkip()  设置skip之后,这个页面的结果不会被Pipeline
                page.setSkip(true);

                page.addTargetRequest(request);
            }




        }else {
//            image = page.getHtml().xpath("/html/body/div[1]/div/section[1]/article/div[1]/div[3]/div[1]/div/div[1]/img").$("img","src").toString();
            image = page.getHtml().xpath("/html/body/div[1]/div/section[1]/article/div[1]/div[3]/div[1]/div/div[1]/img").$("img","ref").toString();
            text = page.getHtml().xpath("/html/body/div[1]/div/section[1]/article/div[1]/div[3]/article/div[1]/p/text()").toString();

            if(image ==null)
                image = "图片暂无";
            if(text ==null)
                text = "详情暂无";


            url = (String) page.getRequest().getExtra("url");
            name = (String) page.getRequest().getExtra("name");


//            //小于20  不走
            int num = (Integer) page.getRequest().getExtra("num");
            if(num<20){
                page.setSkip(true);
            }




//            page.putField("url",url);
//            page.putField("name",name);
//
//            page.putField("image",image);
//            page.putField("text",text);




            //server 酱推送限制长度256
            if(StringUtils.isNotBlank(name) && name.length() > 256){
                name = name.substring(0,200);
            }


            //默认不推送  筛选是否重复
            page.putField("flag",false);
            if(StringUtils.isNotBlank(name)){
                String[] split = name.split("/");
                //不存在 data中
                if(data.get(split[split.length-1]) == null){

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("线报url",url);
                    hashMap.put("线报标题",name);
                    hashMap.put("详细内容",text);
                    hashMap.put("图片地址",image);
                    //不存在存进去
                    data.put(split[split.length-1],1);
                    list.add(hashMap);
                }

                if(num<20){
                    page.putField("flag",false);
                    return;
                }

                if(list.size()>0){
                    page.putField("flag",true);
                }else {
                    page.putField("flag",false);
                }

                page.putField("list",list);


            }

        }

    }

    @Override
    public Site getSite() {
        return site;
    }


}
