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
package cn.vlabs.umt.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtil {
	private FileUtil(){}
	public static final int ONE_KB=1024;
	public static void copy(InputStream from, OutputStream to)
			throws IOException {
		byte[] buff = new byte[ONE_KB];
		int count = -1;
		try {
			while ((count = from.read(buff)) != -1) {
				to.write(buff, 0, count);
			}
		} finally {
			from.close();
			to.close();
		}
	}

	public static void copy(InputStream from, String toFilename)
			throws IOException {
		FileOutputStream out = new FileOutputStream(toFilename);
		copy(from, out);
	}
}