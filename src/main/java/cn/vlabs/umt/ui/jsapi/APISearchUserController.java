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
package cn.vlabs.umt.ui.jsapi;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.umt.services.certificate.ICertificateService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 提供用户搜索功能 /api/users?q=query
 */
@Controller
@RequestMapping("/api/users")
public class APISearchUserController extends APIBaseServlet {
	@Autowired
    private UserService userService;
	@Autowired
    private ICertificateService certs;
    private Map<String,Integer> findUserCertificateState(Collection<User> users){
    	if (users!=null){
    		String[] ids =new String[users.size()]; 
    		int i=0;
    		for (User user :users){
    			ids[i]=user.getCstnetId();
    			i++;
    		}
    		return certs.getCertificateStatus(ids);
    	}else{
    		return null;
    	}
    }
    @RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (UMTContext.getLoginInfo(request.getSession(false)).getUser()==null){
			sayNoAccess(response, "必须登录以后才能访问该链接。");
			return;
		}
		if (ensureParamExist(request, response, "q")){
			String query = request.getParameter("q");
			Collection<User> users= userService.search(query, 0, 10);
			JSONArray array = new JSONArray();
			if (users!=null){
				Map<String, Integer> map = findUserCertificateState(users);
				for (User u :users){
					JSONObject obj = new JSONObject();
					obj.put("cstnetId", u.getCstnetId());
					obj.put("displayName", u.getTrueName());
					obj.put("certifcate", map!=null &&  map.get(u.getCstnetId())!=null);
					array.add(obj);
				}
			}
			saySuccess(response, array);
		}
	}
}
