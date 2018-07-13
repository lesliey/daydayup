/**
 * Copyright 2018 bejson.com
 */
package com.leslie.remote.recent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Auto-generated: 2018-07-13 18:3:11
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Cond {
    @JsonProperty("code_d")
    private String codeD;
    @JsonProperty("code_n")
    private String codeN;
    @JsonProperty("txt_d")
    private String txtD;
    @JsonProperty("txt_n")
    private String txtN;

}