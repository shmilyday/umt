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
package cn.vlabs.umt.oauth.common.domain.client;

/**
 */
public final class BasicClientInfoBuilder {

    private BasicClientInfo info;

    private BasicClientInfoBuilder() {
        info = new BasicClientInfo();
    }

    public static BasicClientInfoBuilder clientInfo() {
        return new BasicClientInfoBuilder();
    }

    public BasicClientInfo build() {
        return info;
    }

    public BasicClientInfoBuilder setName(String value) {
        info.setName(value);
        return this;
    }

    public BasicClientInfoBuilder setClientId(String value) {
        info.setClientId(value);
        return this;
    }

    public BasicClientInfoBuilder setClientUrl(String value) {
        info.setClientUri(value);
        return this;
    }

    public BasicClientInfoBuilder setClientSecret(String value) {
        info.setClientSecret(value);
        return this;
    }

    public BasicClientInfoBuilder setIconUri(String value) {
        info.setIconUri(value);
        return this;
    }

    public BasicClientInfoBuilder setRedirectUri(String value) {
        info.setRedirectUri(value);
        return this;
    }

    public BasicClientInfoBuilder setDescription(String value) {
        info.setDescription(value);
        return this;
    }

    public BasicClientInfoBuilder setExpiresIn(Long value) {
        info.setExpiresIn(value);
        return this;
    }

    public BasicClientInfoBuilder setIssuedAt(Long value) {
        info.setIssuedAt(value);
        return this;
    }

}