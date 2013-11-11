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
package cn.vlabs.umt.services.site.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.site.KeyDAO;

public class KeyDAOImpl implements KeyDAO {
	public KeyDAOImpl(DatabaseUtil du ){
		this.du=du;
	}
	public int create(String keycontent) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(INSERT_SQL);
			pst.setString(1, keycontent);
			pst.execute();
			rs = pst.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return 0;
	}

	public String getContent(int keyid) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(QUERY_SQL);
			pst.setInt(1, keyid);
			rs = pst.executeQuery();
			if (rs.next())
			{
				return rs.getString(1);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	
	private static final Logger LOGGER = Logger.getLogger(KeyDAOImpl.class);
	private static final String QUERY_SQL="select pubkey from umt_pubkeys where id=?";
	private static final String INSERT_SQL="insert into umt_pubkeys(pubkey) values(?)";
	private DatabaseUtil du;
}