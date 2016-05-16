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
package cn.vlabs.umt.services.certificate;

import java.util.Map;

public interface ICertificateService {
	/**
	 * 查询用户的证书
	 * @param cstnetId 被查询的用户ID
	 * @return 返回用户的证书
	 */
	DucklingCertificate getCertificate(String cstnetId);
	/**
	 * 保存或者更新证书
	 * @param cert 要更新的证书
	 */
	void saveCertificate(DucklingCertificate cert);
	/**
	 * 删除证书
	 * @param cstnetId 要删除的证书
	 */
	void deleteCertificate(String cstnetId);
	/**
	 * 查询用户是否已注册了证书
	 * @param ids
	 * @return
	 */
	Map<String, Integer> getCertificateStatus(String[] ids);
}
