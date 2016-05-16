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
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.SQLUtils;
import cn.vlabs.umt.services.user.bean.UnitDomain;
import cn.vlabs.umt.services.user.dao.IUnitDomainDAO;

public class UnitDomainDAOImpl implements IUnitDomainDAO {
	private static final Logger LOG = Logger.getLogger(UnitDomainDAOImpl.class);
	private DatabaseUtil du;
	private static String tableName="umt_unit_domain";
	private static String FIND_ALL_SQL="select * from "+tableName;
	private static String ADD_SQL="insert into "+tableName+"(name,rootDomain,mailDomain,symbol,enName) values(?,?,?,?,?)";
	private static String UPDATE_BY_ID_SQL="update "+tableName+" set name=?,rootDomain=?,mailDomain=?,symbol=?,enName=? where id=?";
	private static String DELETE_BY_ID_SQL="delete from "+tableName+" where id=?";
	private static String FIND_BY_ROOTDOMAIN=FIND_ALL_SQL+" where rootDomain = ?";
	private static String FIND_BY_ROOTDOMAIN_LIKE=FIND_ALL_SQL+" where rootDomain like ?";
	private static String FIND_BY_NAME=FIND_ALL_SQL+" where name = ?";
	private static String FIND_BY_NAME_LIKE=FIND_ALL_SQL+" where name like ?";
	private static String FIND_BY_MAILDOMAIN=FIND_ALL_SQL+" where mailDomain like ?";
	private static String FIND_BY_MAILDOMAIN_LIKE=FIND_ALL_SQL+" where mailDomain like ?";
	private static String FIND_BY_ID=FIND_ALL_SQL+" where id= ?";
	private static final  String QUERY_COUNT = "select count(*) from "+tableName+" where name like ? or enName like ? or rootDomain like ? or mailDomain like ?";
	private static final  String QUERY_UNITS = "select * from  "+tableName+" where name like ? or enName like ? or rootDomain like ? or mailDomain like ? limit ?, ?";
	private static final  String GET_UNITS="select * from "+tableName+" order by id limit ?, ?";
	private static final  String COUNT_SQL="select count(*) from "+tableName;

	
	
	@Override
	public List<UnitDomain> findAll() {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_ALL_SQL);
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}

	@Override
	public int add(UnitDomain unitDomain) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			int i = 1;
			st = conn.prepareStatement(ADD_SQL,Statement.RETURN_GENERATED_KEYS);
			st.setString(i++, unitDomain.getName());
			st.setString(i++, unitDomain.getRootDomain());
			
			st.setString(i++, unitDomain.getMailDomain());
			st.setString(i++, unitDomain.getSymbol());
			st.setString(i++, unitDomain.getEnName());
			st.execute();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				unitDomain.setId(id);
				return id;
			}
		} catch (SQLException e) {
			LOG.error("添加unit domain 错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}

	@Override
	public List<UnitDomain> findByRootDomain(String rootDomain) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_ROOTDOMAIN);
			int i=0;
			ps.setString(++i, rootDomain);
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}
	
	@Override
	public List<UnitDomain> findByRootDomainLike(String rootDomain) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_ROOTDOMAIN_LIKE);
			int index=0;
			ps.setString(++index, "%"+rootDomain+"%");
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}
	
	@Override
	public List<UnitDomain> findByName(String name) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_NAME);
			int i=0;
			ps.setString(++i, name);
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}
	
	@Override
	public List<UnitDomain> findByNameLike(String name) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_NAME_LIKE);
			int index=0;
			ps.setString(++index, "%"+name+"%");
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}

	@Override
	public List<UnitDomain> findByMailDomain(String mailDomain) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_MAILDOMAIN);
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}
	
	@Override
	public List<UnitDomain> findByMailDomainLike(String mailDomain) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_MAILDOMAIN_LIKE);
			int index=0;
			ps.setString(++index, "%"+mailDomain+"%");
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}

	@Override
	public UnitDomain findById(int id) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(FIND_BY_ID);
			int i=0;
			ps.setInt(++i, id);
			rs = ps.executeQuery();
			List<UnitDomain> result = new ArrayList<UnitDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result.get(0);
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}

	@Override
	public boolean deleteById(int id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_BY_ID_SQL);
			st.setInt(1, id);
			st.execute();
			return true;
		} catch (SQLException e) {
			LOG.error("delete unit domain error!", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	@Override
	public void delete(int[] ids) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(DELETE_BY_ID_SQL);
			for (int uid:ids){
				st.setInt(1, uid);
				st.addBatch();
			}
			st.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public boolean delete(UnitDomain unitDomain) {
		if(unitDomain!=null&&unitDomain.getId()>0){
			return false;
		}
		return deleteById(unitDomain.getId());
	}

	@Override
	public boolean update(UnitDomain unitDomain) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			int i = 1;
			st = conn.prepareStatement(UPDATE_BY_ID_SQL);
			st.setString(i++, unitDomain.getName());
			st.setString(i++, unitDomain.getRootDomain());
			st.setString(i++, unitDomain.getMailDomain());
			st.setString(i++, unitDomain.getSymbol());
			st.setString(i++, unitDomain.getEnName());
			st.setInt(i++, unitDomain.getId());
			st.execute();
			return true;
		} catch (SQLException e) {
			LOG.error("update unitDomain error!", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
		return false;
	}
	
	

	public UnitDomainDAOImpl(DatabaseUtil du) {
		this.du = du;
	}

	
	private UnitDomain readDomain(ResultSet rs) throws SQLException{
		UnitDomain od = new UnitDomain();
		od.setId(rs.getInt("id"));
		od.setName(rs.getString("name"));
		od.setRootDomain(rs.getString("rootDomain"));
		od.setMailDomain(rs.getString("mailDomain"));
		od.setSymbol(rs.getString("symbol"));
		od.setEnName(rs.getString("enName"));
		return od;
	}

	public int searchCount(String query){ 
		String q="%"+SQLUtils.quote(query)+"%";
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(QUERY_COUNT);
			int index=0;
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}
	
	public Collection<UnitDomain> search(String query, int start, int count) {
		String querySQL=QUERY_UNITS;
		String q="%"+SQLUtils.quote(query)+"%";
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<UnitDomain> users = new ArrayList<UnitDomain>();
		try {
			st = conn.prepareStatement(querySQL);
			int index=0;
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			st.setInt(++index, start);
			st.setInt(++index, count);
			rs = st.executeQuery();
			while (rs.next()){
				users.add(readDomain(rs));
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e);
			LOG.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		if (users.size()>0){
			return users;
		}else{
			return null;
		}
	}

	@Override
	public int getUnitsCount() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(COUNT_SQL);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}

	@Override
	public Collection<UnitDomain> getUnits(int start, int count) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<UnitDomain> units = new ArrayList<UnitDomain>();
		try {
			st = conn.prepareStatement(GET_UNITS);
			st.setInt(1, start);
			st.setInt(2, count);
			rs = st.executeQuery();
			while (rs.next()){
				units.add(readDomain(rs));
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
		if (units.size()>0)
			return units;
		else
			return null;
	}
	
}
