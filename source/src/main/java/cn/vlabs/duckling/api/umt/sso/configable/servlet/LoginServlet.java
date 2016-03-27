/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.duckling.api.umt.sso.configable.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.UserPrincipalV7;
import cn.vlabs.duckling.api.umt.sso.SSOProperties;
import cn.vlabs.duckling.api.umt.sso.SessionUtil;
import cn.vlabs.duckling.api.umt.sso.configable.msg.Message;
import cn.vlabs.duckling.api.umt.sso.configable.msg.Reason;
import cn.vlabs.duckling.api.umt.sso.configable.service.ILoginHandle;
import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.duckling.common.http.WebSite;
import cn.vlabs.duckling.common.transmission.PublicKeyEnvelope;
import cn.vlabs.duckling.common.transmission.SignedEnvelope;
import cn.vlabs.duckling.common.transmission.UserCredentialEnvelopeV7;
import cn.vlabs.duckling.common.util.Base64Util;
import cn.vlabs.duckling.common.util.ClassUtil;

/**
 * 验证用户是否合法，主要包含解码的逻辑
 * @date 2013-02-05
 * @author LongYun Lv
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1876876234L;
	private static final Logger LOGGER = Logger.getLogger(LoginServlet.class);
	/**
	 * 常用变量
	 * */
	private static final String PARAM_WEB_INF = "WEB-INF";
	/**
	 * 常用变量
	 * */
	private static final String PARAM_CONF = "conf";
	/**
	 * 常用变量
	 * */
	private static final String SERVLET_CONFIG = "config";
	/**
	 * 常用变量,签署的证书
	 * */
	public static final String PARAM_SIGNED_CREDENTIAL = "signedCredential";
	/**
	 * 公钥存放文件名
	 * */
	private static final String DEFAULT_UMT_KEY_FILE_NAME = "umtpublickey.txt";
	/**
	 * 默认公钥下载地址
	 * */
	private static final String DEFAULT_DOWNLOAD_PUBLIC_KEY_URL = "http://localhost/umt/getUMTPublicKey";
	/**
	 * 公钥，先下载，后放到本地文件里面
	 * */
	private RSAKey umtKey;

	/**
	 * 下载公钥，初始化
	 * */
	private void initUmtKey() {
		if (umtKey == null) {
			this.downloadUMTKey();
			this.loadUMTKeyFromLocal();
		}
	}

	/**
	 * 获得用户自定义，扩展接口
	 * */
	private ILoginHandle getHandle() {
		String loginHandClass = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_LOGIN_EXTHANDLE_CLASS,ILoginHandle.DEFAULT_IMPL_PATH);
		return (ILoginHandle) ClassUtil.classInstance(loginHandClass);
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		initUmtKey();
		ILoginHandle handle = getHandle();
		String signedCredential = request.getParameter(PARAM_SIGNED_CREDENTIAL);
		//下载证书失败
		if (umtKey == null){
			logError(request, response, handle, new Message(Reason.PUB_KEY_ERROR,"failed:umtpublic key is not found in local app"));
			return;
		}
		//签名文件为空
		if (StringUtils.isBlank(signedCredential)) {
			logError(request, response, handle, new Message(Reason.SIGNED_CREDENTIAL_ERROR,"signedCredential is null!"));
			return;
		}
		signedCredential = Base64Util.decodeBase64(signedCredential);
		//签名文件错误，解析不出来
		if (StringUtils.isBlank(signedCredential)) {
			logError(request, response, handle, new Message(Reason.SIGNED_CREDENTIAL_ERROR,"signedCredential is invalid!"));
			return;
		}
		SignedEnvelope signedData = SignedEnvelope.valueOf(signedCredential);
		// 验证签名成功
		if (signedData.verify(umtKey)) {
			UserPrincipalV7 userPrincipal= UserCredentialEnvelopeV7.valueOf(signedData.getContent()).getUser();
			SessionUtil.setObject(request, SessionUtil.USER_CONTEXT,userPrincipal);
			if(handle!=null){
				handle.onLoginSuccess(request, response, userPrincipal);
			}
			return;
		}
		// 验证签名异常
		else {
			logError(request, response, handle, new Message(Reason.PUB_KEY_ERROR,"failed:umtpublickey verify"));
			return;
		}

	}

	private void logError(HttpServletRequest request, HttpServletResponse response, ILoginHandle handle, Message msg) {
		handle.onLoginFail(request, response, msg);
		LOGGER.error(msg);
	}

	/**
	 * 初始化配置文件，web.xml配置的配置文件
	 * */
	@Override
	public void init() throws ServletException {
		String file = getInitParameter(SERVLET_CONFIG);
		file = getServletContext().getRealPath(file);
		SSOProperties.getInstance().initProperties(file, true);
	}

	private void downloadUMTKey() {
		String umtPublicKeyContent = WebSite.getBodyContent(SSOProperties.getInstance().getProperty(
				ILoginHandle.UMT_PUBLICKEY_URL_KEY, DEFAULT_DOWNLOAD_PUBLIC_KEY_URL));
		String umtKeyFile = getUmtFilePath();
		try {
			FileUtils.writeStringToFile(new File(umtKeyFile), umtPublicKeyContent);
		} catch (IOException e) {
			LOGGER.error("failed:write umtpublickey to file(" + umtKeyFile + ")", e);
		}
	}

	private void loadUMTKeyFromLocal() {
		String umtKeyFile = getUmtFilePath();
		if (new File(umtKeyFile).exists()) {
			KeyFile keyFile = new KeyFile();
			try {
				umtKey = keyFile.loadFromPublicKeyContent(PublicKeyEnvelope.valueOf(
						FileUtils.readFileToString(new File(umtKeyFile))).getPublicKey());
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 获得公钥存放路径,本地的绝对路径
	 * */
	private String getUmtFilePath() {
		return getServletContext().getRealPath(PARAM_WEB_INF) + File.separator + PARAM_CONF + File.separator
				+ DEFAULT_UMT_KEY_FILE_NAME;
	}
}