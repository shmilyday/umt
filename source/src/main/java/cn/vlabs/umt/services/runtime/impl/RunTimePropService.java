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
package cn.vlabs.umt.services.runtime.impl;

import cn.vlabs.umt.services.runtime.IRunTimePropDAO;
import cn.vlabs.umt.services.runtime.IRunTimePropService;
import cn.vlabs.umt.services.runtime.bean.RunTimeProp;

/**
 * @author lvly
 * @since 2013-3-8
 */
public class RunTimePropService implements IRunTimePropService {
	private IRunTimePropDAO propDAO;
	public RunTimePropService(IRunTimePropDAO propDAO){
		this.propDAO=propDAO;
	}
	@Override
	public RunTimeProp getValueByName(String propName) {
		return propDAO.getValueByName(propName);
	}
	@Override
	public void createProp(String propName, String value) {
		propDAO.createProp(propName, value);
	}
	@Override
	public void updateProp(String propName, String toValue) {
		propDAO.updateProp(propName, toValue);
		
	}
	@Override
	public void deleteProp(String propName) {
		propDAO.deleteProp(propName);
	}
}