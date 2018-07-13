package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class WxImageMsg extends BaseWxMsg {
    @JsonProperty("PicUrl")
    private String picUrl;
    @JsonProperty("MediaId")
    private String mediaId;
}
