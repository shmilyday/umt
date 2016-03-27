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
package cn.vlabs.umt.common.util;

public final class SQLUtils {
	private SQLUtils(){}
	public static String quote(String val){
		if (val!=null && val.length()>0){
			StringBuffer buffer = new StringBuffer();
			for (int i=0;i<val.length();i++){
				char ch = val.charAt(i);
				switch (ch){
				case '\'':buffer.append("\\'");break;
				case '\"':buffer.append("\\\"");break;
				case '\n':buffer.append("\\n");break;
				case '\r':buffer.append("\\r");break;
				case '\t':buffer.append("\\t");break;
				case '\\':buffer.append("\\\\");break;
				case '%':buffer.append("\\%");break;
				case '_':buffer.append("\\_");break;
				default:
					buffer.append(ch);
					break;
				}
			}
			return buffer.toString();
		}
		return val;
	}
}