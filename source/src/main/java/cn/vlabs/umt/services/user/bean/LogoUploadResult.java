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
package cn.vlabs.umt.services.user.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LogoUploadResult {
	private int clientId;
	private boolean success=true;
	private String desc;
	private String currTarget;
	private Map<String,Integer> targetClbId=new HashMap<String,Integer>();
	
	public String getCurrTarget() {
		return currTarget;
	}
	public void setCurrTarget(String currTarget) {
		this.currTarget = currTarget;
	}
	public int getClientId() {
		return clientId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public void add(String target,int clbId){
		targetClbId.put(target, clbId);
	}
	public JSONObject toJson(){
		JSONObject obj=new JSONObject();
		obj.put("clientId", clientId);
		obj.put("success", success);
		obj.put("currTarget", currTarget);
		obj.put("desc", desc);
		if(!targetClbId.isEmpty()){
			JSONArray array=new JSONArray();
			for(Entry<String,Integer> entry:targetClbId.entrySet()){
				JSONObject o =new JSONObject();
				o.put("target", entry.getKey());
				o.put("clbId", entry.getValue());
				array.add(o);
			}
			obj.put("updatedList", array);
		}
		return obj;
	}
	
}
