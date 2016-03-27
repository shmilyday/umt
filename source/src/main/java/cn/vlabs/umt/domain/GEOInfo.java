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
package cn.vlabs.umt.domain;

import cn.vlabs.umt.common.util.CommonUtils;

public class GEOInfo {
	private String country;
	private String province;
	private String city;
	private String ip;
	private String unitName;
	private boolean isCstnetUnit;
	private boolean fromDip;
	public boolean isFromDip() {
		return fromDip;
	}
	public void setFromDip(boolean fromDip) {
		this.fromDip = fromDip;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public boolean isCstnetUnit() {
		return isCstnetUnit;
	}
	public void setCstnetUnit(boolean isCstnetUnit) {
		this.isCstnetUnit = isCstnetUnit;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String contry) {
		this.country = contry;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 是否为空？返回刚new的对象
	 * */
	public boolean isNull(){
		return CommonUtils.isNull(this.city)&&CommonUtils.isNull(this.province)&&CommonUtils.isNull(this.country);
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GEOInfo)){
			return false;
		}
		GEOInfo info=(GEOInfo)obj;
		return CommonUtils.killNull(info.getCity()).equals(CommonUtils.killNull(this.getCity()))
		&&CommonUtils.killNull(info.getCountry()).equals(CommonUtils.killNull(this.getCountry()))
		&&CommonUtils.killNull(info.getProvince()).equals(CommonUtils.killNull(this.getProvince()));
	}
}
