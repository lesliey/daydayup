package com.leslie.remote;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2018，所有权利保留。<br/>
 * 项目名：	com.leslie.remote<br/>
 * 文件名：	TodayWeather.java<br/>
 * 模块说明：<br/>
 * 修改历史：2018年07月13日 - yanghaixiao - 创建。<br/>
 */

@Data
public class TodayWeatherInfo {
    @JsonProperty("city")
    private String city;
    private String cityid;
    private String temp;
    @JsonProperty("WD")
    private String WD;
    @JsonProperty("WS")
    private String WS;
    @JsonProperty("SD")
    private String SD;
    @JsonProperty("WSE")
    private String WSE;
    private String time;
    private String isRadar;
    @JsonProperty("Radar")
    private String Radar;
    @JsonProperty("njd")
    private String njd;
    private String qy;
    private String rain;
}
