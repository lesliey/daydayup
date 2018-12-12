/**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2016，所有权利保留。
 * 项目名：	sas-service
 * 文件名：	AccessKey.java
 * 模块说明：
 * 修改历史：
 * 2018-05-28 - yanghaxiao- 创建。
 */
package com.leslie.common;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * APP登录信息保持器
 * 
 * @author yanghaixiao
 */
@Component
@Scope(value = SCOPE_REQUEST, proxyMode = TARGET_CLASS)
@Data
public class AccessKey implements Serializable {

  private static final long serialVersionUID = -5297029665867632470L;
  /** 来源用户openid */
  private String fromUserName;

}
