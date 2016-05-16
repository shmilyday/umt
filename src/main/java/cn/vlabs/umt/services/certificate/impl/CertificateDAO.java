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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.certificate.DucklingCertificate;
/**
 * 
 * CREATE TABLE `umt_certificate` (
  `Id` int(11) NOT NULL auto_increment,
  `dn` varchar(255) NOT NULL default '',
  `cestnetId` varchar(255) NOT NULL default '',
  `pub_cert` text,
  `full_cert` text,
  PRIMARY KEY  (`Id`),
  UNIQUE KEY `cstnetId` (`cestnetId`),
  UNIQUE KEY `dn` (`dn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 * @author xiejj
 *
 */
public class CertificateDAO implements ICertificateDAO {
	private static Logger LOGGER=Logger.getLogger(CertificateDAO.class);
	private DatabaseUtil du;
	public CertificateDAO(DatabaseUtil du){
		this.du = du;
	}
	@Override
	public DucklingCertificate getCertificate(String cstnetId) {
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs = null;
		try {
			conn = du.getConnection();
			st = conn.prepareStatement("select * from umt_certificate where cstnetId=?");
			st.setString(1, cstnetId);
			rs = st.executeQuery();
			if (rs.next()){
				DucklingCertificate cert =new DucklingCertificate();
				cert.setCstnetId(cstnetId);
				cert.setDn(rs.getString("dn"));
				cert.setPubCert(rs.getString("pub_cert"));
				cert.setFullCert(rs.getString("full_cert"));
				cert.setRegistTime(rs.getTimestamp("regist_time"));
				return cert;
			}
		} catch (SQLException e) {
			LOGGER.error("Query certifcate failed", e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
		
		return null;
	}

	@Override
	public void createCertificate(DucklingCertificate cert) {
		Connection conn=null;
		PreparedStatement st=null;
		try {
			conn = du.getConnection();
			st = conn.prepareStatement("insert into umt_certificate(cstnetId, dn,regist_time, pub_cert, full_cert) values(?,?,?,?,?)");
			st.setString(1, cert.getCstnetId());
			st.setString(2, cert.getDn());
			st.setTimestamp(3, new Timestamp(cert.getRegistTime().getTime()));
			st.setString(4, cert.getPubCert());
			st.setString(5, cert.getFullCert());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error("Create certifcate failed", e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
	}

	@Override
	public void updateCertificate(DucklingCertificate cert) {
		Connection conn=null;
		PreparedStatement st=null;
		try {
			conn = du.getConnection();
			st = conn.prepareStatement("update umt_certificate set dn=?, pub_cert=?, full_cert=? , regist_time=? where cstnetId=?");
			st.setString(1, cert.getDn());
			st.setString(2, cert.getPubCert());
			st.setString(3, cert.getFullCert());
			st.setTimestamp(4, new Timestamp(cert.getRegistTime().getTime()));
			st.setString(5, cert.getCstnetId());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error("Create certifcate failed", e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
	}

	@Override
	public void deleteCertifcate(String cstnetId) {
		Connection conn=null;
		PreparedStatement st=null;
		try {
			conn = du.getConnection();
			st = conn.prepareStatement("delete from umt_certificate where cstnetId=?");
			st.setString(1, cstnetId);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error("Create certifcate failed", e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	@Override
	public Map<String, Integer> getCertificateStatus(String[] ids) {
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs = null;
		try {
			conn = du.getConnection();
			String queryString = getSetString(ids.length);
			st = conn.prepareStatement("select count(id) ,cstnetId from umt_certificate where cstnetId in ("+queryString+") group by cstnetId");
			for (int i=1;i<=ids.length;i++){
				st.setString(i, ids[i-1]);
			}
			
			rs = st.executeQuery();
			HashMap<String, Integer> map = new HashMap<String,Integer>();
			while(rs.next()){
				int count = rs.getInt(1);
				String cstnetId = rs.getString(2);
				map.put(cstnetId, count);
			}
			return map;
		} catch (SQLException e) {
			LOGGER.error("Create certifcate failed", e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private String getSetString(int count){
		StringBuilder builder = new StringBuilder();
		for (int i=0;i<count;i++){
			if (i==0){
				builder.append('?');
			}else{
				builder.append(",?");
			}
		}
		return builder.toString();
	}
}
