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
package cn.vlabs.umt.oauth.as.issuer;


import cn.vlabs.umt.oauth.common.exception.OAuthSystemException;

/**
 *
 *
 *
 */
public class OAuthIssuerImpl implements OAuthIssuer {

    private ValueGenerator vg;

    public OAuthIssuerImpl(ValueGenerator vg) {
        this.vg = vg;
    }

    public String accessToken() throws OAuthSystemException {
        return vg.generateValue();
    }

    public String refreshToken() throws OAuthSystemException {
        return vg.generateValue();
    }

    public String authorizationCode() throws OAuthSystemException {
        return vg.generateValue();
    }
}