package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class WxShortVideoMsg extends BaseWxMsg {
    @JsonProperty("MediaId")
    private String mediaId;
    @JsonProperty("ThumbMediaId")
    private String thumbMediaId;
}
