/**
  * Copyright 2018 bejson.com 
  */
package com.leslie.remote.recent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2018-07-13 18:3:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class HeWeather5 {

    private Basic basic;
    @JsonProperty("daily_forecast")
    private List<DailyForecast> dailyForecast;
    private String status;

}