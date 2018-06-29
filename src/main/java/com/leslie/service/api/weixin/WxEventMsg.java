package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class WxEventMsg extends BaseWxMsg {
    public static final String SUBSCRIBE = "subscribe", UNSUBSCRIBE = "unsubscribe";
    @JsonProperty("Event")
    private String event;
    @JsonProperty("EventKey")
    private String eventKey;
}
