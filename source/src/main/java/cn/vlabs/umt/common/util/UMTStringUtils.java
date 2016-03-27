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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

public class UMTStringUtils {
	
	public static String getRandString(int length){
		//生成随机类
		Random random = new Random();
		// 取随机产生的认证码(4位数字)
		String codeList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder sRand=new StringBuilder();
		for (int i=0;i<length;i++){
		  int a=random.nextInt(codeList.length()-1);
		      String rand=codeList.substring(a,a+1);
		      sRand.append(rand);
		}
		return sRand.toString();
	}
	
	public static String readToString(Reader in) throws IOException{
		BufferedReader reader = new BufferedReader(in);
		StringBuffer buffer = new StringBuffer();
		String line;
		while ((line = reader.readLine())!=null){
			buffer.append(line);
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
