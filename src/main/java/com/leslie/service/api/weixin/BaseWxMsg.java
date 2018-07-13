package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class BaseWxMsg {
    public static final String TEXT_TYPE = "text", IMAGE_TYPE = "image", VOICE_TYPE = "voice", VIDEO_TYPE = "video", SHORT_VIDEO_TYPE = "shortvideo", EVENT_TYPE = "event";
    @JsonProperty("ToUserName")
    private String toUserName;
    @JsonProperty("FromUserName")
    private String fromUserName;
    @JsonProperty("CreateTime")
    private String createTime;
    @JsonProperty("MsgType")
    private String msgType;
    @JsonProperty("MsgId")
    private String msgId;
}
