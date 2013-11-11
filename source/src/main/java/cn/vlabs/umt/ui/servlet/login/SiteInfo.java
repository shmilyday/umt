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
package cn.vlabs.umt.ui.servlet.login;

import java.util.HashMap;
import java.util.Map;

public class SiteInfo {
	public SiteInfo(){
		 records=new HashMap<String, Map<String,String>>();
	}
	public void push(String appname, Map<String,String> siteInfo){
		records.put(appname, siteInfo);
	}
	public Map<String,String> get(String appname)
	{
		return records.get(appname);
	}
	public Map<String,String> pop(String appname){
		Map<String,String> siteInfo = records.get(appname);
		if (siteInfo!=null){
			records.remove(appname);
		}
		return siteInfo;
	}
	private HashMap<String, Map<String,String>> records;
}