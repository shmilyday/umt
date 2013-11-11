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
package cn.vlabs.umt.ui.servlet;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import cn.vlabs.duckling.common.crypto.HexUtil;

/**
 * 负责操作Cookie, 产生加密Cookie
 * 
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 3, 2009 5:03:40 PM
 */
public class PCookie {
	private UMTCredential keys;

	public PCookie(UMTCredential cred) {
		this.keys = cred;
	}

	public String encrypt(String message) {
		if (message != null) {
			try {
				byte[] encrypted = keys.encrypt(message.getBytes("UTF-8"));
				return HexUtil.toHexString(encrypted);
			} catch (UnsupportedEncodingException e) {}
		}
		return null;
	}

	public String decrypt(String str) {
		if (StringUtils.isNotBlank(str)){
			byte[] encrypted = HexUtil.toBytes(str);
			byte[] decrypted = keys.decrypt(encrypted);
			try {
				if (decrypted != null) {
					return new String(decrypted, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {}
		}
		return null;
	}
}