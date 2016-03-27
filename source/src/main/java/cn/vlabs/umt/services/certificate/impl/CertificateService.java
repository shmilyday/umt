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
package cn.vlabs.umt.services.certificate.impl;

import java.util.Map;

import cn.vlabs.umt.services.certificate.DucklingCertificate;
import cn.vlabs.umt.services.certificate.ICertificateService;

public class CertificateService implements ICertificateService {
	private ICertificateDAO certificateDAO;
	public void setCertificateDAO(ICertificateDAO dao){
		this.certificateDAO = dao;
	}
	@Override
	public DucklingCertificate getCertificate(String cstnetId) {
		return certificateDAO.getCertificate(cstnetId);
	}

	@Override
	public void saveCertificate(DucklingCertificate cert) {
		DucklingCertificate oldCert = certificateDAO.getCertificate(cert.getCstnetId());
		if (oldCert!=null){
			certificateDAO.updateCertificate(cert);
		}else{
			certificateDAO.createCertificate(cert);
		}
	}

	@Override
	public void deleteCertificate(String cstnetId) {
		certificateDAO.deleteCertifcate(cstnetId);
	}
	
	@Override
	public Map<String, Integer> getCertificateStatus(String[] ids) {
		return certificateDAO.getCertificateStatus(ids);
	}
}
