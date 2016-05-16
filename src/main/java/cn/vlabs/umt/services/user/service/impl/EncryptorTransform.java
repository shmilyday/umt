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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.util.Base64;
import cn.vlabs.duckling.common.util.Base64.DecodingException;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.bean.Digester;
import cn.vlabs.umt.services.user.exception.PwdEncryptorException;
import cn.vlabs.umt.services.user.service.ITransform;

public class EncryptorTransform implements ITransform {
	public static final String BEAN_ID = "transform";
	private static final Logger LOGGER = Logger
			.getLogger(EncryptorTransform.class);
	private String algorithm;

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setConfig(Config config) {
		algorithm = config.getStringProp("PASSWORDS_ENCRYPTION_ALGORITHM",
				Digester.DIGEST_ALGORITHM);
	}

	public String transform(String value) {
		try {
			return this.transform(value, null);
		} catch (PwdEncryptorException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return value;
	}

	private String transform(String clearTextPwd, String currentEncPwd)
			throws PwdEncryptorException {
		// /在umt中约定，对于空密码，不进行摘要
		if (clearTextPwd == null || clearTextPwd.equals("")) {
			return "";
		}
		if (TYPE_NONE.equals(algorithm)) {
			return clearTextPwd;
		} else if (TYPE_SSHA.equals(algorithm)) {
			byte[] saltBytes = getSaltFromSSHA(currentEncPwd);

			return encodePassword(algorithm, clearTextPwd, saltBytes);
		} else {
			return encodePassword(algorithm, clearTextPwd, null);
		}
	}

	private static String encodePassword(String algorithm, String clearTextPwd,
			byte[] saltBytes) throws PwdEncryptorException {

		try {
			if (TYPE_SSHA.equals(algorithm)) {
				byte[] clearTextPwdBytes = clearTextPwd
						.getBytes(Digester.ENCODING);

				// Create a byte array of salt bytes appeneded to password bytes

				byte[] pwdPlusSalt = new byte[clearTextPwdBytes.length
						+ saltBytes.length];

				System.arraycopy(clearTextPwdBytes, 0, pwdPlusSalt, 0,
						clearTextPwdBytes.length);

				System.arraycopy(saltBytes, 0, pwdPlusSalt,
						clearTextPwdBytes.length, saltBytes.length);

				// Digest byte array

				MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");

				byte[] pwdPlusSaltHash = sha1Digest.digest(pwdPlusSalt);

				// Appends salt bytes to the SHA-1 digest.

				byte[] digestPlusSalt = new byte[pwdPlusSaltHash.length
						+ saltBytes.length];

				System.arraycopy(pwdPlusSaltHash, 0, digestPlusSalt, 0,
						pwdPlusSaltHash.length);

				System.arraycopy(saltBytes, 0, digestPlusSalt,
						pwdPlusSaltHash.length, saltBytes.length);

				// Base64 encode and format string

				return Base64.encode(digestPlusSalt);
			} else {
				return Digester.digest(algorithm, clearTextPwd);
			}
		} catch (NoSuchAlgorithmException nsae) {
			throw new PwdEncryptorException(nsae.getMessage());
		} catch (UnsupportedEncodingException uee) {
			throw new PwdEncryptorException(uee.getMessage());
		}
	}

	private static byte[] getSaltFromSSHA(String sshaString)
			throws PwdEncryptorException {

		byte[] saltBytes = new byte[8];

		if (sshaString == null || sshaString.trim().length() == 0) {
			// Generate random salt
			Random random = new SecureRandom();

			random.nextBytes(saltBytes);
		} else {

			// Extract salt from encrypted password

			try {
				byte[] digestPlusSalt = Base64.decode(sshaString);
				byte[] digestBytes = new byte[digestPlusSalt.length - 8];

				System.arraycopy(digestPlusSalt, 0, digestBytes, 0,
						digestBytes.length);

				System.arraycopy(digestPlusSalt, digestBytes.length, saltBytes,
						0, saltBytes.length);
			} catch (DecodingException ioe) {
				throw new PwdEncryptorException(
						"Unable to extract salt from encrypted password: "
								+ ioe.getMessage());
			}
		}

		return saltBytes;
	}
}