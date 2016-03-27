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
package cn.vlabs.duckling.api.umt.sso;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.duckling.common.http.WebSite;
import cn.vlabs.duckling.common.transmission.PublicKeyEnvelope;
import cn.vlabs.duckling.common.transmission.SignedEnvelope;
import cn.vlabs.duckling.common.transmission.UserCredentialEnvelope;
import cn.vlabs.duckling.common.util.Base64Util;
import cn.vlabs.duckling.common.util.ClassUtil;

/**
 * Introduction Here.
 * 
 * @date 2010-6-29
 * @author Fred Zhang (fred@cnic.cn)
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1876876234L;
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class);
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		if (umtKey == null) {
			this.downloadUMTKey();
			this.loadUMTKeyFromLocal();
		}
		String signedCredential = request.getParameter("signedCredential");
		if (umtKey == null) {
			LOGGER.error("failed:umtpublic key is not found in local app");
			return;
		}
		if(!StringUtils.isNotEmpty(signedCredential)){
			return;
		}
		signedCredential = Base64Util.decodeBase64(signedCredential);
		SignedEnvelope signedData = SignedEnvelope.valueOf(signedCredential);
		
		if(!signedData.verify(umtKey))
		{
			LOGGER.error("failed:umtpublickey verify");
			return;
		}
		UserContext userContext = new UserContext();
		userContext.setAuthenticated(true);
		String name = UserCredentialEnvelope.valueOf(signedData.getContent()).getUser().getName();
		userContext.setName(name);
		
		String loginHandClass = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_LOGIN_EXTHANDLE_CLASS);
		if(loginHandClass!=null)
		{
			Object object = ClassUtil.classInstance(loginHandClass);
			if(object!=null)
			{
				((ILoginHandle)object).initAfterLogin(request, response, userContext);
			}
		}
		
		SessionUtil.setUserContext(request, userContext);
		String returnUrl = SessionUtil.getUserRedirectUrl(request);
		response.sendRedirect(returnUrl);
				
	}
	public void init() throws ServletException {
		//loadUMTKeyFromLocal();
	}
	private void loadUMTKeyFromLocal()
	{
		String umtKeyFile = getServletContext().getRealPath("WEB-INF")+File.separator+"conf"+File.separator+"umtpublickey.txt";
	    if(new File(umtKeyFile).exists())
	    {
	    	KeyFile keyFile = new KeyFile();
	    	try {
	    		umtKey = keyFile.loadFromPublicKeyContent(PublicKeyEnvelope.valueOf(FileUtils.readFileToString(new File(umtKeyFile))).getPublicKey());
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
				throw new RuntimeException("");
			}
	    }
	}
    private RSAKey umtKey = null;
	private void downloadUMTKey()
	{
	    String umtPublicKeyContent = WebSite.getBodyContent(SSOProperties.getInstance().getProperty(ILoginHandle.UMT_PUBLICKEY_URL_KEY, "http://locahost/umt/getUMTPublicKey"));
	    String umtKeyFile = getServletContext().getRealPath("WEB-INF")+File.separator+"conf"+File.separator+"umtpublickey.txt";
	    try {
			FileUtils.writeStringToFile(new File(umtKeyFile), umtPublicKeyContent);
		} catch (IOException e) {
			LOGGER.error("failed:write umtpublickey to file("+umtKeyFile+")",e);
		}
	}
	
	
}