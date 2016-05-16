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
package cn.vlabs.umt.services.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import net.duckling.cloudy.common.CommonUtils;
import cn.vlabs.umt.common.datasource.CoreMailDBException;
import cn.vlabs.umt.common.datasource.CoreMailDataUtils;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;

public class CoremailDBDAOImpl implements ICoreMailDBDAO{
	private CoreMailDataUtils dataUtils;
	private static final Logger LOG = Logger.getLogger(CoremailDBDAOImpl.class);

	
	public CoremailDBDAOImpl(CoreMailDataUtils dataUtils){
		this.dataUtils=dataUtils;
	}
	@Override
	public CoreMailUserInfo authticate(String userName, String password)
			throws CoreMailDBException {
		if(CommonUtils.isNull(userName)||CommonUtils.isNull(password)){
			return null;
		}
		if(!userName.contains("@")){
			return null;
		}
		String[] split=userName.split("@");
		String uid=split[0];
		String domain=split[1];
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuffer sb=new StringBuffer();
		sb.append("select tu.*,cu.* ");
		sb.append(",CONCAT('{enc2}', md5(?)) actualEnc ");
		sb.append("from td_domain d,td_user tu,cm_user_info cu ");
		sb.append("where d.domain_name=? ");
		sb.append("and tu.user_id=? ");
		sb.append("and tu.domain_id=d.domain_id ");
		sb.append("and cu.user_id=tu.user_id ");
		sb.append("and cu.org_id=tu.org_id ");
		try{
			conn=dataUtils.getConnection();
			int index=0;
			pst=conn.prepareStatement(sb.toString());
			pst.setString(++index, CommonUtils.trim(password));
			pst.setString(++index, CommonUtils.trim(domain));
			pst.setString(++index, CommonUtils.trim(uid));

			rs=pst.executeQuery();
			if(rs.next()){
				//用户输入加密结果
				String actualEnc=rs.getString("actualEnc");
				//数据库加密结果
				String dbEnc=rs.getString("password");
				if(!dbEnc.startsWith("{enc2}")){
					LOG.info("validate["+userName+"] password use db  error , user password is not start with enc2");
					throw new CoreMailDBException(CoreMailDBException.WHY_PWD_ERROR);
				}
				if(!actualEnc.equals(dbEnc)){
					LOG.info("validate["+userName+"] password use db  error , user password is error");
					return null;
				}
				
				CoreMailUserInfo info=new CoreMailUserInfo();
				info.setEmail(userName);
				info.setExpireTime(rs.getString("user_expiry_date"));
				info.setStatus(rs.getString("user_status"));
				info.setTrueName(rs.getString("true_name"));
				return info;
			}
		}catch(SQLException|RuntimeException e){
			throw new CoreMailDBException(e.getMessage(),e);
		}finally{
			CoreMailDataUtils.closeAll(rs, pst, conn);
		}
		return null;
	}
	@Override
	public boolean isDomainExits(String domain) throws CoreMailDBException {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuffer sb=new StringBuffer();
		sb.append("select count(*) c ");
		sb.append("from td_domain ");
		sb.append("where d.domain_name=? ");
		try{
			conn=dataUtils.getConnection();
			int index=0;
			pst=conn.prepareStatement(sb.toString());
			pst.setString(++index, CommonUtils.trim(domain));
			rs=pst.executeQuery();
			if(rs.next()){
				return rs.getInt("c")>0;
			}
		}catch(SQLException|RuntimeException e){
			throw new CoreMailDBException(e.getMessage(),e);
		}finally{
			CoreMailDataUtils.closeAll(rs, pst, conn);
		}
		return false;
	}

}
