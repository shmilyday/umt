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
package cn.vlabs.umt.services.user.dao;

import cn.vlabs.umt.services.user.bean.AssociateUser;

/**
 * @author lvly
 * @since 2013-3-12
 */
public interface IAssociateUserDAO {
	/**
	 * 增加关联账户
	 * @param uid
	 * @param associateUid 被关联的uid
	 * @param appList 应用列表 AssociateUser.APP_XXX逗号分隔
	 */
	void addAssociateUser(int uid,int associateUid,String appList);	
	
	/**
	 * 获取关联账户信息
	 * @param uid
	 * @return
	 */
	AssociateUser getAssociate(int uid);
	
	/**
	 * 更新关联账户
	 * @param id
	 * @param associateUid 被关联的uid
	 * @param appList 应用列表 AssociateUser.APP_XXX逗号分隔
	 */
	void updateAssociateUser(int id,int associateUid,String appList);
	
	/**
	 * 看此用户是否已经关联过别的用户，或者已经被关联
	 * @param uid
	 * @return 
	 * */
	boolean isAssociated(int uid);

}