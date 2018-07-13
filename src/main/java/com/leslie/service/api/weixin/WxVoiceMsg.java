package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class WxVoiceMsg extends BaseWxMsg {
    @JsonProperty("MediaId")
    private String mediaId;
    @JsonProperty("Format")
    private String format;
    @JsonProperty("Recognition")
    private String recognition;
}
