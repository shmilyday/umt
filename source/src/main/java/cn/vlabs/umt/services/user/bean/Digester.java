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



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.util.Base64;

/**
 * <a href="Digester.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public final class Digester {
	private Digester(){}
	public static final String ENCODING = "UTF-8";

	public static final String DIGEST_ALGORITHM = "SHA";

	public static String digest(String text) {
		return digest(DIGEST_ALGORITHM, text);
	}
    
	public static String digest(String algorithm, String text) {
		MessageDigest mDigest = null;
        boolean digestflag = false;
		try{
			mDigest = MessageDigest.getInstance(algorithm.trim());

			mDigest.update(text.getBytes(ENCODING));
			digestflag = true;
		}
		catch (NoSuchAlgorithmException nsae) {
			LOGGER.error(nsae, nsae);
			
		}
		catch (UnsupportedEncodingException uee) {
			LOGGER.error(uee, uee);
		}
        if(!digestflag)
        {
        	return text;
        }
		byte[] raw = mDigest.digest();

		return Base64.encode(raw);
	}

	private static final Logger LOGGER = Logger.getLogger(Digester.class);

}