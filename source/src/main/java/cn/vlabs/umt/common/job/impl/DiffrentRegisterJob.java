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
package cn.vlabs.umt.common.job.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.job.Jobable;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.GeoIPUtils;
import cn.vlabs.umt.domain.GEOInfo;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountDAO;
import cn.vlabs.umt.services.account.IUMTLogDAO;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;

public class DiffrentRegisterJob implements Jobable {
	private static final Logger LOGGER = Logger
			.getLogger(DiffrentRegisterJob.class);
	private MessageSender mailSender;
	private UserService userService;
	private IAccountDAO accountDAO;
	private IUMTLogDAO umtLogDAO;
	private int uid;
	private int logId;

	public DiffrentRegisterJob(MessageSender ms, UserService us,
			IAccountDAO accountDAO, IUMTLogDAO umtLogDAO,int uid, int logId) {
		this.mailSender = ms;
		this.userService = us;
		this.accountDAO = accountDAO;
		this.uid = uid;
		this.logId = logId;
		this.umtLogDAO = umtLogDAO;
	}

	public boolean isCommonGEO(UMTLog log) {
		List<UMTLog> common = accountDAO.getMyPreference(uid);
		if (CommonUtils.isNull(common)) {
			return false;
		}
		for (UMTLog c : common) {
			GEOInfo commonGEO = c.getGEOInfo();
			if (commonGEO.equals(log.getGEOInfo())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void doJob() {
		UMTLog log = umtLogDAO.getLogById(uid, logId);
		UMTLog second = umtLogDAO.getLogSecondFromId(uid, logId);
		if (log == null ) {
			return;
		}
		GEOInfo cityName = GeoIPUtils.getGEOInfo(log.getUserIp());
		if (cityName == null) {
			return;
		}
		log.setCountry(cityName.getCountry());
		log.setProvince(cityName.getProvince());
		log.setCity(cityName.getCity());
		log.setCstnetUnit(cityName.isCstnetUnit());
		log.setUnitName(cityName.getUnitName());
		log.setFromDip(cityName.isFromDip());
		umtLogDAO.updateGEOInfo(log);
		if(second==null){
			return;
		}
		// 常用地登录
		if (isCommonGEO(log)) {
			return;
		}

		GEOInfo lastGEO = new GEOInfo();
		lastGEO.setCity(log.getCity());
		lastGEO.setCountry(log.getCountry());
		lastGEO.setProvince(log.getProvince());
		lastGEO.setIp(log.getUserIp());
		lastGEO.setCstnetUnit(log.isCstnetUnit());
		lastGEO.setUnitName(log.getUnitName());
		lastGEO.setFromDip(log.isFromDip());
		GEOInfo secondGEO = new GEOInfo();
		secondGEO.setCity(second.getCity());
		secondGEO.setCountry(second.getCountry());
		secondGEO.setProvince(second.getProvince());
		secondGEO.setIp(second.getUserIp());
		secondGEO.setUnitName(second.getUnitName());
		secondGEO.setCstnetUnit(second.isCstnetUnit());
		secondGEO.setFromDip(second.isFromDip());
		// 如果本次登录跟上次登录地有异，则发送邮件
		if (canSend(lastGEO, secondGEO)) {
			User u = userService.getUserByUid(uid);
			Properties pro = new Properties();
			pro.setProperty("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()));
			pro.setProperty("ip1", second.getUserIp());
			pro.setProperty("geoInfo1", second.displayGEO());
			pro.setProperty("app1", getAppName(second.getAppName()));

			pro.setProperty("ip2", log.getUserIp());
			pro.setProperty("geoInfo2", log.displayGEO());
			pro.setProperty("app2", getAppName(log.getAppName()));
			pro.setProperty("cstnetId", u.getCstnetId());
			try {
				if(u.isSendGEOEmailSwitch()){
					mailSender.send(new Locale("zh_CN"), "duckling-umt@cstnet.cn",
						EmailTemplate.NOTICE_DIFF_REGISTER, pro);
				}else{
					LOGGER.info("user["+u.getCstnetId()+"] geo email switch off!");
				}
				umtLogDAO.updateSendWarnMail(log.getId(), uid);
			} catch (MailException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private boolean canSend(GEOInfo last, GEOInfo second) {
		if (!last.isFromDip() || !second.isFromDip()) {
			return false;
		}
		boolean isIPChanged = isIPChanged(last.getIp(), second.getIp());
		// ip一致，则不考虑后面
		if (!isIPChanged) {
			return false;
		}
		// 如果这次是院内不发
		if (last.isCstnetUnit()) {
			return false;
		}
		// 如果都为未知，则发邮件
		if (last.isNull() && second.isNull()) {
			return true;
		}
		// 都没命中，则比对归属地，不一致则发
		return !last.equals(second);
	}

	public static boolean isIPV4(String ip) {
		Pattern liP = Pattern
				.compile("^((25[0-5]|2[0-4]\\d|[0-1]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[0-1]?\\d\\d?)$");
		Matcher mt = liP.matcher(CommonUtils.trim(ip));
		return mt.matches();
	}

	private boolean isIPChanged(String ip1, String ip2) {
		if (!isIPV4(ip1) || !isIPV4(ip2)) {
			return false;
		}
		String[] ip1S = ip1.split("\\.");
		String[] ip2S = ip2.split("\\.");
		return !(ip1S[0] + "." + ip1S[1]).equals(ip2S[0] + "." + ip2S[1]);
	}

	private String getAppName(String app) {
		switch (app) {
		case "umt": {
			return "中国科技网通行证";
		}
		case "qq": {
			return "QQ";
		}
		case "sina": {
			return "新浪微博";
		}
		case "cashq": {
			return "院机关统一认证平台";
		}
		default: {
			return app;
		}
		}
	}

	@Override
	public String getJobId() {
		return "diff.regist." + uid + "." + System.currentTimeMillis();
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return false;
	}

}
