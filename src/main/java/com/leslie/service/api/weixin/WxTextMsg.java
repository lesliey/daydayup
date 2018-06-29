package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class WxTextMsg extends BaseWxMsg {
    @JsonProperty("Content")
    private String content;
}
