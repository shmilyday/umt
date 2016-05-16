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
package cn.vlabs.umt.ui.controller;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;

import com.octo.captcha.Captcha;
import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
@RequestMapping("/jq/validate.do")
public class JQueryValidateController {
	@RequestMapping(params="act=validateCode")
	public void validateCode(HttpServletRequest request,HttpServletResponse response)throws Exception{
		HttpSession session=request.getSession();
		String validCode=CommonUtils.trim(request.getParameter("validcode"));
		ImageCaptchaService obj=(ImageCaptchaService)(SimpleImageCaptchaServlet.service);
		Field f=obj.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("store");
		f.setAccessible(true);
		FastHashMapCaptchaStore fs=(FastHashMapCaptchaStore)(f.get(obj));
		boolean result=false;
		if(fs!=null&&session!=null){
			Captcha cp=fs.getCaptcha(session.getId());
			if(cp!=null){
				result=(cp.validateResponse(validCode));
			}
			
		}
		response.getWriter().write(result+"");
	}

}
