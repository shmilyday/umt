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
package cn.vlabs.umt.services.user.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.dao.IBindThirdPartyDAO;


/**
 * @author lvly
 * @since 2013-2-25
 */
public class BindThirdPartyDAO  implements IBindThirdPartyDAO{
	private static final Logger LOGGER=Logger.getLogger(BindThirdPartyDAO.class);
	
	private DatabaseUtil du;
	
	private static final String INSERT_SQL="insert into `umt_third_party_bind`(`uid`,`open_id`,`type`,`true_name`,`url`) values(?,?,?,?,?)";
	private static final String SELECT_SQL="select * from `umt_third_party_bind` where 1=1 ";
	private static final String DELETE_SQL="delete from `umt_third_party_bind` where 1=1 ";
	private static final String BY_UID=" and `uid`=? ";
	private static final String BY_ID=" and `id`=? ";
	public BindThirdPartyDAO(DatabaseUtil du){
		this.du=du;
	}
	@Override
	public void deleteBindByUid(int[] uids) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		Statement st=null;
		try{
			StringBuffer uidsStr=new StringBuffer();
			for(int uid:uids){
				uidsStr.append(uid).append(",");
			}
			st = conn.createStatement();
			String sql=DELETE_SQL+" and `uid` in("+CommonUtils.format(uidsStr.toString())+")";
			st.execute(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
	}
	@Override
	public void bindThirdParty(BindInfo bindInfo) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(INSERT_SQL);
			int index=0;
			st.setInt(++index, bindInfo.getUid());
			st.setString(++index, bindInfo.getOpenId());
			st.setString(++index, bindInfo.getType());
			st.setString(++index, bindInfo.getTrueName());
			st.setString(++index, bindInfo.getUrl());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public List<BindInfo> getBindInfosByUid(int uid){
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		List<BindInfo> infos=new ArrayList<BindInfo>();
		try{
			String sql ="select b.*, a.name from umt_third_party_bind b left join umt_third_auth a on a.code=b.type  where  b.uid=?";
			st = conn.prepareStatement(sql);
			int index=0;
			st.setInt(++index, uid);
			rs=st.executeQuery();
			while(rs.next()){
				infos.add(readBindInfo(rs));
			}
			return infos;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return infos;
	}
	/**
	 * @param rs
	 * @return
	 */
	private BindInfo readBindInfo(ResultSet rs) {
		BindInfo info=new BindInfo();
		try {
			info.setId(rs.getInt("id"));
			info.setOpenId(rs.getString("open_id"));
			info.setUid(rs.getInt("uid"));
			info.setType(rs.getString("type"));
			info.setTrueName(rs.getString("true_name"));
			info.setUrl(rs.getString("url"));
			info.setTypeName(rs.getString("name"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return info;
	}
	@Override
	public void deleteBindById(int bindId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(DELETE_SQL+BY_ID);
			int index=0;
			st.setInt(++index, bindId);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
}