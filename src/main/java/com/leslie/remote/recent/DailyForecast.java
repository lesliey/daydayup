/**
  * Copyright 2018 bejson.com 
  */
package com.leslie.remote.recent;
import lombok.Data;

import java.util.Date;

/**
 * Auto-generated: 2018-07-13 18:3:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class DailyForecast {

    private Astro astro;
    private Cond cond;
    private String date;
    private String hum;
    private String pcpn;
    private String pop;
    private String pres;
    private Tmp tmp;
    private String uv;
    private String vis;
    private Wind wind;

}