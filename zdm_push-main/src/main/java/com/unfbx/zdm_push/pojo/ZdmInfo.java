package com.unfbx.zdm_push.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Grt
 * @Date 2020-12-11
 */
@Data
public class ZdmInfo {

    private String url;
    private String name;


    private String image;
    private String text;

    private List<Map<String,String>> mapList;
}
