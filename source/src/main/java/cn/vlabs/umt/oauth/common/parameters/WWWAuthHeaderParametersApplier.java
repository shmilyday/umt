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
package cn.vlabs.umt.oauth.common.parameters;

import java.util.Map;


import cn.vlabs.umt.oauth.common.OAuth;
import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;
import cn.vlabs.umt.oauth.common.message.OAuthMessage;
import cn.vlabs.umt.oauth.common.utils.OAuthUtils;

/**
 *
 *
 *
 */
public class WWWAuthHeaderParametersApplier implements OAuthParametersApplier {

    public OAuthMessage applyOAuthParameters(OAuthMessage message, Map<String, Object> params)
        throws OAuthSystemException {
        String header = OAuthUtils.encodeOAuthHeader(params);
        message.addHeader(OAuth.HeaderType.WWW_AUTHENTICATE, header);
        return message;
    }
}