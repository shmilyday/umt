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
/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
package cn.vlabs.umt.ui.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.vlabs.umt.services.auth.IAuthService;
import cn.vlabs.umt.services.auth.ThirdPartyAuth;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;

@Controller
@RequestMapping("/admin/manageAuth.do")
public class ManageAuthController {
	@Autowired
	private IAuthService authService;
	@RequestMapping
	public String show(HttpServletRequest request){
		request.setAttribute("auths", authService.getAll());
		return "admin/manageAuth";
	}
	
	@RequestMapping(params="act=add")
	public String add(@ModelAttribute ThirdPartyAuth auth){
		authService.create(auth);
		return "redirect:/admin/manageAuth.do";
	}
	@RequestMapping(params="act=remove")
	public String remove(@RequestParam("code")String code){
		authService.remove(code);
		return "redirect:/admin/manageAuth.do";
	}
	@RequestMapping(params="act=update")
	public String update(@ModelAttribute ThirdPartyAuth auth){
		authService.update(auth);
		return "redirect:/admin/manageAuth.do";
	}
	@ResponseBody
	@RequestMapping(params="act=codeValid")
	public boolean codeValid(@RequestParam("code") String code){
		if (authService.existAuth(code)||BindInfo.isBuildinThirdParty(code)||(User.isBuildinAuthBy(code))){
			return false;
		}else{
			return true;
		}
	}
	@ResponseBody
	@RequestMapping(params="act=load")
	public ThirdPartyAuth load(@RequestParam("code") String code){
		return authService.find(code);
	}
}