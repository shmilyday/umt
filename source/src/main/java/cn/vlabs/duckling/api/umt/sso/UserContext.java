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
package cn.vlabs.duckling.api.umt.sso;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

import cn.vlabs.commons.principal.UserPrincipal;

/**
 * Introduction Here.
 * @date 2010-6-29
 * @author Fred Zhang (fred@cnic.cn)
 */
public class UserContext implements Serializable{
	/**
	 * Brief Intro Here
	 */
	private static final long serialVersionUID = 1L;
	private boolean authenticated = false;
	private List<Principal> principals;
	private String name;
    private Locale locale;
    
	/**
	 * @return the principals
	 */
	public List<Principal> getPrincipals() {
		return principals;
	}
	/**
	 * @param principals the principals to set
	 */
	public void setPrincipals(List<Principal> principals) {
		this.principals = principals;
	}
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}
	/**
	 * @param authenticated the authenticated to set
	 */
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public UserPrincipal getUserPrincipal() {
		for(Principal p:principals)
		{
			if(p instanceof UserPrincipal)
			{				
				return (UserPrincipal)p;
			}
		}
		return null;
	}
}