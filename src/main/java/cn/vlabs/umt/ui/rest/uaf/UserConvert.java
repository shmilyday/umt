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
package  cn.vlabs.umt.ui.rest.uaf;

import java.util.HashMap;
import java.util.Map;

import cn.cnic.uaf.common.trans.UnifiedUser;
import cn.vlabs.commons.principal.UserPrincipal;

public final class UserConvert {
    private UserConvert(){
    	
    }
    public static UnifiedUser convert2UnifiedUser(UserPrincipal up) {
        if(up == null){
        	return null;
        }
        UnifiedUser uu = new UnifiedUser();
        uu.setDept("cerc");
        uu.setEmail(up.getEmail());
        uu.setNickName(up.getDisplayName());
        uu.setUserName(up.getName());
        Map<String, String> extInfo = new HashMap<String, String>();
        extInfo.put("authby", up.getAuthBy());
        uu.setExtInfo(extInfo);
        return uu;
    }

}