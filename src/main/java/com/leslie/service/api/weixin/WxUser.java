package com.leslie.service.api.weixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WxUser {
    private int subscribe;
    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    @JsonProperty("subscribe_time")
    private long subscribeTime;
    private String unionid;
    private String remark;
    private int groupid;
    @JsonProperty("tagid_list")
    private Date tagidList;
    @JsonProperty("subscribe_scene")
    private String subscribeScene;
    @JsonProperty("qr_scene")
    private long qrScene;
    @JsonProperty("qrSceneStr")
    private String qr_scene_str;

}
