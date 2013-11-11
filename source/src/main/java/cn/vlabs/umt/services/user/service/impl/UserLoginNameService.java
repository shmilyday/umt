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
package cn.vlabs.umt.services.user.service.impl;

import java.util.List;

import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.dao.IUserLoginNameDAO;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;

/**
 * @author lvly
 * @since 2013-3-4
 */
public class UserLoginNameService implements IUserLoginNameService{
	private IUserLoginNameDAO loginNameDAO;
	public UserLoginNameService(IUserLoginNameDAO loginNameDAO){
		this.loginNameDAO=loginNameDAO;
	}
	@Override
	public LoginNameInfo getALoginNameInfo(int uid, String loginName) {
		return loginNameDAO.getALoginNameInfo(uid,loginName);
	}
	@Override
	public List<LoginNameInfo> getLoginNameInfo(int uid, String type) {
		return loginNameDAO.getLoginNameInfo(uid,type);
	}
	@Override
	public void updateLoginName(int uid,String oldLoginName,String newLoginName) {
		loginNameDAO.updateToLoginName(uid, oldLoginName, null);
		loginNameDAO.updateLoginName(uid,oldLoginName, newLoginName);
	}
	@Override
	public LoginNameInfo getLoginNameInfoById(int loginNameInfoId) {
		return loginNameDAO.getLoginNameInfoById(loginNameInfoId);
	}
	@Override
	public int createLoginName(String loginName, int uid, String type,String status) {
		return loginNameDAO.createLoginName(loginName, uid, type,status);
	}
	@Override
	public String getValidSecondaryEmailStr(int uid) {
		StringBuffer result=new StringBuffer();
		List<String> secondarys=loginNameDAO.getLoginNameInfos(uid, LoginNameInfo.LOGINNAME_TYPE_SECONDARY,LoginNameInfo.STATUS_ACTIVE);
		for(String str:secondarys){
			result.append(str).append(";");
		}
		
		return result.toString();
	}
	@Override
	public void removeSecondaryEmail(int uid, String email) {
		loginNameDAO.removeSecondaryLoginName(uid,email);
	}
	@Override
	public void updateToLoginName(int uid, String oldLoginName, String newLoginName) {
		loginNameDAO.updateToLoginName(uid,oldLoginName,newLoginName);
	}
	@Override
	public void toActive(int loginInfoId) {
		LoginNameInfo nameInfo=getLoginNameInfoById(loginInfoId);
		loginNameDAO.toActive(loginInfoId);
		loginNameDAO.removeSameTmpEmail(nameInfo.getLoginName());
	}
	@Override
	public void toActive(int uid, String loginName, String type) {
		loginNameDAO.toActive(uid,loginName,type);
		loginNameDAO.removeSameTmpEmail(loginName);
	}
	@Override
	public void removeLoginNameById(int loginNameInfoId) {
		loginNameDAO.removeLoginName(loginNameInfoId);
		
	}
	@Override
	public int getLoginNameId(int uid, String loginName, String type) {
		return loginNameDAO.getLoginNameId(uid,loginName,type);
	}
	@Override
	public boolean isUsedByMe(int uid,String loginName){
		return loginNameDAO.isUsedByMe(uid,loginName);
	}
	@Override
	public void removeLoginNamesByUid(int uid) {
		loginNameDAO.removeLoginNamesByUid(new int[]{uid});
		
	}
	

}