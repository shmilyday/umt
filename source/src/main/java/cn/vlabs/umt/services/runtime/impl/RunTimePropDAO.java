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
package cn.vlabs.umt.services.runtime.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.runtime.IRunTimePropDAO;
import cn.vlabs.umt.services.runtime.bean.RunTimeProp;

/**
 * @author lvly
 * @since 2013-3-8
 */
public class RunTimePropDAO implements IRunTimePropDAO{
	
	private static final Logger LOGGER = Logger.getLogger(RunTimePropDAO.class);
	private DatabaseUtil du;
	public RunTimePropDAO(DatabaseUtil du){
		this.du=du;
	}
	private static final String TABLE_NAME=" `umt_runtime_prop` ";
	private static final String WHERE_SQL=" where 1=1 ";
	private static final String SELECT="select * from "+TABLE_NAME+WHERE_SQL;
	private static final String UPDATE="update "+TABLE_NAME+" set `prop_value`=?,`last_modify_time`=now() "+WHERE_SQL;
	private static final String INSERT="insert into "+TABLE_NAME+"(`prop_name`,`prop_value`,`last_modify_time`) values(?,?,now())";
	private static final String DELETE="delete from "+TABLE_NAME+WHERE_SQL;
	
	private static final String BY_PROP_NAME=" and `prop_name`=? ";
	private RunTimeProp readRunTimeProp(ResultSet rs){
		RunTimeProp runTime=null;
		try {
			runTime=new RunTimeProp();
			runTime.setId(rs.getInt("id"));
			runTime.setLastModifyTime(rs.getTimestamp("last_modify_time"));
			runTime.setPropName(rs.getString("prop_name"));
			runTime.setPropValue(rs.getString("prop_value"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}
		
		return runTime;
	}
	@Override
	public RunTimeProp getValueByName(String propName) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st=conn.prepareStatement(SELECT+BY_PROP_NAME);
			int index=0;
			st.setString(++index, propName);
			rs=st.executeQuery();
			if(rs.next()){
				return readRunTimeProp(rs);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public void createProp(String propName, String value) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st=conn.prepareStatement(INSERT);
			int index=0;
			st.setString(++index, propName);
			st.setString(++index, value);
			st.execute();
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void updateProp(String propName, String toValue) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st=conn.prepareStatement(UPDATE+BY_PROP_NAME);
			int index=0;
			
			st.setString(++index, propName);
			st.setString(++index, toValue);
			st.execute();
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void deleteProp(String propName) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st=conn.prepareStatement(DELETE+BY_PROP_NAME);
			int index=0;
			st.setString(++index, propName);
			st.execute();
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
}