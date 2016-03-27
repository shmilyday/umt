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

import java.util.Random;

public class RandomUtil {
	public String getRandom(int length){
		long seed = System.currentTimeMillis()+Runtime.getRuntime().freeMemory();
		Random rand= new Random(seed);
		StringBuffer buffer = new StringBuffer();
		int index;
		for (int i=0;i<length;i++){
			index = rand.nextInt(CHARS.length());
			buffer.append(CHARS.charAt(index));
		}
		return buffer.toString();
	}
	private static final String CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	//---------------------- lvly ---------------------
	private static final Random RANDOM=new Random();
	private static final String[] RANDOM_ARRAY={
		"a","b","c","d","e",
		"f","g","h","i","j",
		"k","l","m","n","o",
		"p","q","r","s","t",
		"u","v","w","x","y",
		"z","A","B","C","D",
		"E","F","G","H","I",
		"J","K","L","M","N",
		"O","P","Q","R","S",
		"T","U","V","W","X",
		"Y","Z","1","2","3",
		"4","5","6","7","8",
		"9","0"}; 
	private static final String[] RANDOM_INT_ARRAY={
		"1","2","3",
		"4","5","6","7","8",
		"9"};
	
	public static String random(int length){
		int arrayLength=RANDOM_ARRAY.length;
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(RANDOM_ARRAY[(RANDOM.nextInt(arrayLength))]);
		}
		return sb.toString();
	}
	
	public static String randomInt(int length){
		int arrayLength=RANDOM_INT_ARRAY.length;
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(RANDOM_INT_ARRAY[(RANDOM.nextInt(arrayLength))]);
		}
		return sb.toString();
	}
}