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
package cn.vlabs.umt.services.user.service.impl;

import java.io.UnsupportedEncodingException;

import cn.vlabs.umt.common.digest.MD4;
import cn.vlabs.umt.services.user.service.ITransform;

public class NTHashTransform implements ITransform {
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private static byte[] toUnicode(String message) {
		try {
			byte[] bytes = message.getBytes("UTF-8");
			byte[] result = new byte[bytes.length * 2];
			for (int i = 0; i < bytes.length; i++) {
				result[i * 2] = bytes[i];
				result[i * 2 + 1] = 0;
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			return new byte[0];
		}

	}

	@Override
	public String transform(String value) {
		MD4 md4=new MD4();
		byte[] bytes = md4.digest(toUnicode(value));
		return bytesToHex(bytes);
	}
}
