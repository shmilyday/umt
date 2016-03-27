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
package cn.vlabs.umt.oauth.as.validator;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.umt.oauth.common.OAuth;
import cn.vlabs.umt.oauth.common.error.OAuthError;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.oauth.common.validators.AbstractValidator;


/**
 *
 *
 *
 */
public class CodeTokenValidator extends AbstractValidator<HttpServletRequest> {

    public CodeTokenValidator() {
        requiredParams.add(OAuth.OAUTH_RESPONSE_TYPE);
        requiredParams.add(OAuth.OAUTH_CLIENT_ID);
        requiredParams.add(OAuth.OAUTH_REDIRECT_URI);
    }

    @Override
    public void validateMethod(HttpServletRequest request) throws OAuthProblemException {
        String method = request.getMethod();
        if (!method.equals(OAuth.HttpMethod.GET) && !method.equals(OAuth.HttpMethod.POST)) {
            throw OAuthProblemException.error(OAuthError.CodeResponse.INVALID_REQUEST)
                .description("Method not correct.");
        }
    }

    @Override
    public void validateContentType(HttpServletRequest request) throws OAuthProblemException {
    }
}