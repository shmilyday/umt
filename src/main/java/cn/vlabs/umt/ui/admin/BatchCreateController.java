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
package cn.vlabs.umt.ui.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.common.xls.RecordFile;
import cn.vlabs.umt.common.xls.UserVO;
import cn.vlabs.umt.common.xls.UserXLSParser;
import cn.vlabs.umt.common.xls.XLSException;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;

@Controller
@RequestMapping("/admin/batchCreate.do")
@Deprecated
public class BatchCreateController {
	private static final Logger log = Logger.getLogger(BatchCreateController.class);
	private static final int recordPerFile=20;

	@Autowired
	private UserService us;
	@RequestMapping(params="act=save",method = RequestMethod.POST)
	public String save(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="file", required=false) MultipartFile formfile) {
		if (formfile!=null){
			try {
				readAllToUser(request.getSession(), formfile, request.getServletContext());
			} catch (Exception e) {
				request.setAttribute("message", e.getMessage());
				return "/admin/displayuser";
			}
		}
		return "redirect:/admin/batchCreate.do?act=changePage";
	}
	@RequestMapping(params="act=changePage")
	public String changePage(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Integer total = (Integer)session.getAttribute("batch.total");
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		if (total==null || records==null){
			return null;
		}
		
		PageBean bean = new PageBean(total);
		String page = request.getParameter("page");
		try{
			bean.setCurrentPage(Integer.parseInt(page));
		}catch(NumberFormatException e){
			bean.setCurrentPage(0);
		}
		
		try {
			bean.setItems(records.load(bean.getCurrentPage()));
			request.setAttribute("PageBean", bean);
			return "/admin/displayuser";
		} catch (IOException e) {
			request.setAttribute("message", e.getMessage());
			return "/admin/displayuser";
		}
	}
	private void checkExist(List<UserVO> users, UserService us){
		String[] usernames = new String[users.size()];
		for (int i=0;i<users.size();i++){
			usernames[i]=users.get(i).getUmtId();
		}
		
		Set<String> exists= us.isExist(usernames);
		
		for (UserVO user:users){
			if (exists.contains(user.getUmtId())){
				user.setExists(true);
			}else{
				user.setExists(false);
			}
		}
	}
	private void cleanup(HttpServletRequest request) {
		HttpSession session = request.getSession();
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		if (records!=null){
			String datadir=records.getDatadir();
			try {
				FileUtils.deleteDirectory(new File(datadir));
			} catch (IOException e) {
				log.info("删除临时文件失败:"+e.getMessage());
			}
			session.removeAttribute("batch.records");
			session.removeAttribute("batch.total");
		}
	}
	@RequestMapping(params="act=create")
	public String create(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		for (int i=0;i<records.getPageCount();i++){
			try {
				ArrayList<UserVO> users = records.load(i);
				ArrayList<User> toCreate=new ArrayList<User>();
				for (UserVO user:users){
					if (!user.isExists()){
						toCreate.add(user);
					}
				}
				us.create(toCreate);
			} catch (IOException e) {
			}
		}
		cleanup(request);
		return "/admin/batch";
	}
	@RequestMapping
	public String service(HttpServletRequest request, HttpServletResponse response) {
		return "/admin/batch";
	}
	@RequestMapping(params="act=discard")
	public String discard(HttpServletRequest request, HttpServletResponse response) {
		cleanup(request);
		return "/admin/batch";
	}
	
	private void readAllToUser(HttpSession session, MultipartFile file, ServletContext context) throws IOException, XLSException{
		RecordFile records = new RecordFile(context);
		
		int realsize = 0;
		InputStream in = null;
		try{
			in = file.getInputStream();
			UserXLSParser parser = new UserXLSParser(in);
			int total = parser.getCount();
			int page = total/recordPerFile+((total%recordPerFile)>0?1:0);
			for (int i=0;i<page;i++){
				List<UserVO> users = parser.readUsers(i*recordPerFile, recordPerFile);
				checkExist(users, us);
				records.save(i, users);
				realsize = realsize+users.size();
			}
		}finally{
			if (in!=null)
			{
				in.close();
			}
		}
		records.setPageCount(realsize/recordPerFile+((realsize%recordPerFile)>0?1:0));
		session.setAttribute("batch.records", records);
		session.setAttribute("batch.total", realsize);
	}

}