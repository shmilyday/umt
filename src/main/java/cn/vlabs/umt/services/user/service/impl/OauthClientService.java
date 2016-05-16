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
package cn.vlabs.umt.services.user.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.duckling.falcon.api.cache.ICacheService;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.ImageUtils;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.bean.LogoUploadResult;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IOauthClientDAO;
import cn.vlabs.umt.services.user.service.IClbService;
import cn.vlabs.umt.services.user.service.IOauthClientService;

public class OauthClientService implements IOauthClientService {
	private static final Logger LOGGER=Logger.getLogger(OauthClientService.class); 
	private ICacheService cacheService;
	private IOauthClientDAO oauthClientDAO;
	private MessageSender emailSender;
	private RoleService roleService; 
	private IClbService clbService;
	
	public void setClbService(IClbService clbService) {
		this.clbService = clbService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setEmailSender(MessageSender email) {
		this.emailSender = email;
	}

	public void setCacheService(ICacheService cacheService) {
		this.cacheService = cacheService;
	}

	public void setOauthClientDAO(IOauthClientDAO oauthClientDAO) {
		this.oauthClientDAO = oauthClientDAO;
	}

	@Override
	public int save(OauthClientBean bean,boolean needCache) {
		int i=oauthClientDAO.save(bean);
		if(i>0&&needCache){
			addToCache(bean);
		}
		return bean.getId();
	}

	private void addToCache(OauthClientBean bean){
		cacheService.set(getClientKey(bean), bean);
	}
	private void removeFromCache(OauthClientBean bean){
		cacheService.remove(getClientKey(bean.getClientId()));
	}
	
	private String getClientKey(OauthClientBean bean){
		return getClientKey(bean.getClientId());
	}
	private String getClientKey(String clientId) {
		return clientId+"-oauth_client_id";
	}

	@Override
	public OauthClientBean findByClientId(String clientId) {
		Object o = cacheService.get(getClientKey(clientId));
		if(o==null){
			OauthClientBean bean = oauthClientDAO.findByClientId(clientId);
			if(bean!=null){
				addToCache(bean);
			}
			return bean;
		}else{
			return (OauthClientBean)o;
		}
	}

	@Override
	public List<OauthClientBean> findByStatus(String id) {
		return oauthClientDAO.findByStatus(id);
	}
	@Override
	public List<OauthClientBean> findByUid(int userId,String type) {
		return oauthClientDAO.findByUid(userId,type);
	}

	@Override
	public OauthClientBean findById(int id) {
		return oauthClientDAO.findById(id);
	}

	@Override
	public void delete(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.delete(bean);
	}

	@Override
	public void delete(int id) {
		OauthClientBean bean = oauthClientDAO.findById(id);
		if(bean!=null){
			delete(bean);
		}
	}

	@Override
	public void update(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.update(bean);
		addToCache(bean);
	}

	@Override
	public List<OauthClientBean> getAll() {
		return oauthClientDAO.getAll();
	}

	@Override
	public void deleteFromCache(OauthClientBean bean) {
		removeFromCache(bean);
	}
	@Override
	public void sendAddMailtoAmin(Locale locale,OauthClientBean bean,User u) throws MailException {

		Collection<User> users=roleService.getRoleMembers("admin");
		Properties pro=new Properties();
		pro.setProperty("trueName", u.getTrueName());
		pro.setProperty("email", u.getCstnetId());
		pro.setProperty("appName", bean.getClientName());
		pro.setProperty("webSite", bean.getClientWebsite());
		pro.setProperty("returnUri", bean.getRedirectURI());
		pro.setProperty("description",bean.getDescription());
		pro.setProperty("applicant", bean.getApplicant());
		pro.setProperty("company", bean.getCompany());
		pro.setProperty("contactInfo", bean.getContactInfo());
		pro.setProperty("appType", bean.getAppTypeDesc());
		
		emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_OAUTH_ADD, pro);
		
	}
	private String[] getEmailList(Collection<User> col){
		String[] users=new String[col.size()];
		int index=0;
		for(User user:col){
			users[index++]=user.getCstnetId();
		}
		return users;
	}
	private String getLogoCustom(int docId){
		return "/logo?logId="+docId+"&size="+IClbService.LOGO_SMALL;
	}
	
	@Override
	public void sendUpdateMailtoAmin(Locale locale, OauthClientBean beforeBean, OauthClientBean afterBean, User u) throws MailException {
		Collection<User> users=roleService.getRoleMembers("admin");
		Properties pro=new Properties();
		pro.setProperty("trueName", u.getTrueName());
		pro.setProperty("email", u.getCstnetId());
		pro.setProperty("orgAppName", beforeBean.getClientName());
		pro.setProperty("orgwebSite", beforeBean.getClientWebsite());
		pro.setProperty("orgreturnUri", beforeBean.getRedirectURI());
		pro.setProperty("orgdescription",beforeBean.getDescription());
		pro.setProperty("orgapplicant", beforeBean.getApplicant());
		pro.setProperty("orgcompany", beforeBean.getCompany());
		pro.setProperty("orgcontactInfo",beforeBean.getContactInfo());
		pro.setProperty("orgappType", beforeBean.getAppTypeDesc());
		
		pro.setProperty("afterAppName", afterBean.getClientName());
		pro.setProperty("afterwebSite", afterBean.getClientWebsite());
		pro.setProperty("afterreturnUri", afterBean.getRedirectURI());
		pro.setProperty("afterdescription",afterBean.getDescription());
		pro.setProperty("afterapplicant", afterBean.getApplicant());
		pro.setProperty("aftercompany", afterBean.getCompany());
		pro.setProperty("aftercontactInfo",afterBean.getContactInfo());
		pro.setProperty("afterappType", afterBean.getAppTypeDesc());
		
		emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_OAUTH_UPDATE, pro);
	}
	
	@Override
	public void sendAdminToDevelop(Locale locale, OauthClientBean bean,User user){
		Properties pro=new Properties();
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("webSite", CommonUtils.killNull(bean.getClientWebsite()));
		pro.setProperty("returnUri", CommonUtils.killNull(bean.getRedirectURI()));
		pro.setProperty("description",CommonUtils.killNull(bean.getDescription()));
		pro.setProperty("applicant", CommonUtils.killNull(bean.getApplicant()));
		pro.setProperty("company", CommonUtils.killNull(bean.getCompany()));
		pro.setProperty("status",CommonUtils.killNull(bean.getStatusDisplay()));
		pro.setProperty("contact", CommonUtils.killNull(bean.getContactInfo()));
		pro.setProperty("appType", CommonUtils.killNull(bean.getAppTypeDesc()));
		try {
			if(user!=null){
				emailSender.send(locale, user.getCstnetId(), EmailTemplate.NOTICE_USER_OAUTH_UPDATE, pro);
			}
		} catch (MailException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	@Override
	public void updateDevelop(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.updateDevelop(bean);
		addToCache(findById(bean.getId()));
	}

	@Override
	public OauthClientBean findAcceptByClientId(String clientId) {
		OauthClientBean result = findByClientId(clientId);
		if(result!=null&&result.isAccept()){
			return result;
		}
		return null;
	}
	@Override
	public List<OauthClientBean> searchClientByKey(String key,int offset,int size) {
		return oauthClientDAO.searchClientByKey(key,offset,size);
	}
	@Override
	public void uploadLogoDefault(OauthClientBean bean,File tmpFile) throws FileNotFoundException {
		int logo100Id=clbService.upload(new FileInputStream(tmpFile), System.currentTimeMillis()+".fn.png");
		boolean flag100=false;
		boolean flag64=false;
		boolean flag32=false;
		boolean flag16=false;
		if(bean.is100LogoDefault()){
			flag100=true;
			bean.setLogo100m100(logo100Id);
		}
		if(bean.is64LogoDefault()){
			flag64=true;
			bean.setLogo64m64(logo100Id);
		}
		if(bean.is32LogoDefault()){
			flag32=true;
			bean.setLogo32m32(logo100Id);
		}
		if(bean.is16LogoDefault()){
			flag16=true;
			bean.setLogo16m16(logo100Id);
		}
		
		bean.setDefaultLogo(logo100Id);
		bean.setLogoCustom(getLogoCustom(logo100Id));
		oauthClientDAO.updateLogo(bean,flag100,flag64,flag32,flag16);
	}
	@Override
	public void updateDefaultLogoChange(OauthClientBean bean, File file) throws FileNotFoundException {
		int logo100Id=clbService.upload(new FileInputStream(file), System.currentTimeMillis()+"fn.jpg");
		boolean update100=false;
		boolean update64=false;
		boolean update32=false;
		boolean update16=false;
		if(bean.getDefaultLogo()==bean.getLogo100m100()){
			bean.setLogo100m100(logo100Id);
			update100=true;
		}if(bean.getDefaultLogo()==bean.getLogo64m64()){
			bean.setLogo64m64(logo100Id);
			update64=true;
		}if(bean.getDefaultLogo()==bean.getLogo32m32()){
			bean.setLogo32m32(logo100Id);
			update32=true;
		}if(bean.getDefaultLogo()==bean.getLogo16m16()){
			bean.setLogo16m16(logo100Id);
			update16=true;
		}
		bean.setDefaultLogo(logo100Id);
		bean.setLogoCustom(getLogoCustom(logo100Id));
		oauthClientDAO.updateLogo(bean,update100,update64,update32,update16);
	}
	@Override
	public LogoUploadResult uploadLogo(OauthClientBean bean,File tmpFile,String uploadName, String target) throws IOException {
		LogoUploadResult result=new LogoUploadResult();
		if(uploadName==null){
			uploadName="untitled.png";
		}
		result.setCurrTarget(target);
		result.setClientId(bean.getId());
		File defaultCutFile=ImageUtils.defaultCut(tmpFile);
		boolean is100Updated=false;
		boolean is64Updated=false;
		boolean is32Updated=false;
		boolean is16Updated=false;
		BufferedImage src = ImageIO.read(new FileInputStream(tmpFile));
		int height=src.getHeight();
		int width=src.getWidth();
		switch(target){
			case "100p":{
				int logo100Id=0;
				if(height==width){
					logo100Id=clbService.upload(new FileInputStream(tmpFile), target+"."+uploadName);
				}else{
					logo100Id=clbService.upload(new FileInputStream(ImageUtils.scale(defaultCutFile, 100, 100)), target+"."+uploadName);
				}
				bean.setLogoCustom(getLogoCustom(logo100Id));
				is100Updated=true;
				bean.setLogo100m100(logo100Id);
				result.add("100p", logo100Id);
				boolean is64LogoDefault=bean.is64LogoDefault();
				boolean is32LogoDefault=bean.is32LogoDefault();
				boolean is16LogoDefault=bean.is16LogoDefault();
				if(is64LogoDefault){
					int logo64Id=clbService.upload(new FileInputStream(ImageUtils.scale(defaultCutFile, 64,64)), target+"."+uploadName);
					is64Updated=true;
					bean.setLogo64m64(logo64Id);
					result.add("64p", logo64Id);
				}
				if(is32LogoDefault){
					int logo32Id=clbService.upload(new FileInputStream(ImageUtils.scale(defaultCutFile, 32, 32)), target+"."+uploadName);
					is32Updated=true;
					bean.setLogo32m32(logo32Id);
					result.add("32p", logo32Id);
				}
				if(is16LogoDefault){
					int logo16Id=clbService.upload(new FileInputStream(ImageUtils.scale(defaultCutFile, 16, 16)), target+"."+uploadName);
					is16Updated=true;
					bean.setLogo16m16(logo16Id);
					result.add("16p", logo16Id);
				}
				
				break;
			}
			case "64p":{
				if(height!=64||width!=64){
					result.setSuccess(false);
					result.setDesc("请上传64*64像素的图片");
					return result;
				}
				int logo64Id=clbService.upload(new FileInputStream(tmpFile), target+"."+uploadName);
				is64Updated=true;
				bean.setLogo64m64(logo64Id);
				bean.setLogoCustom(getLogoCustom(logo64Id));
				result.add("64p", logo64Id);
				break;
			}
			case "32p":{
				if(height!=32||width!=32){
					result.setSuccess(false);
					result.setDesc("请上传32*32像素的图片");
					return result;
				}
				int logo32Id=clbService.upload(new FileInputStream(tmpFile), target+"."+uploadName);
				is32Updated=true;
				bean.setLogo32m32(logo32Id);
				bean.setLogoCustom(getLogoCustom(logo32Id));
				result.add("32p", logo32Id);
				break;
			}
			case "16p":{
				if(height!=16||width!=16){
					result.setSuccess(false);
					result.setDesc("请上传16*16像素的图片");
					return result;
				}
				int logo16Id=clbService.upload(new FileInputStream(tmpFile), target+"."+uploadName);
				is16Updated=true;
				bean.setLogo16m16(logo16Id);
				result.add("16p", logo16Id);
				break;
			}
			default:{
				LOGGER.error("target Error,"+target);
				result.setSuccess(false);
				result.setDesc("targetError");
			}
		}
		oauthClientDAO.updateLogo(bean,is100Updated,is64Updated,is32Updated,is16Updated);
		//success
		return result;
		
	}
	@Override
	public void removeLogo(OauthClientBean bean, boolean is100Updated,
			boolean is64Updated, boolean is32Updated, boolean is16Updated) {
		oauthClientDAO.removeLogo(bean,is100Updated,is64Updated,is32Updated,is16Updated);
	}
	@Override
	public List<OauthClientBean> findEnableAppAndAccepted(String type) {
		return oauthClientDAO.findEnableAppAndAccepted(type);
	}
	@Override
	public List<String> getAllCallBack() {
		return oauthClientDAO.getAllCallBack();
	}

}