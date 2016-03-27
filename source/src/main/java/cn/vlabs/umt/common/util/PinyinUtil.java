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

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.log4j.Logger;

/**
 * @date 2011-11-15
 * @author JohnX
 */
public final class PinyinUtil {
	private PinyinUtil() {
	}

	public static final char CH_START = '\u4e00';
	public static final char CH_END = '\u9fa5';
	public static final char SPLIT_CHAR = ';';
	public static final int CH_CODE_VALUE = 128;
	public static final int DB_CHNAME_LEN = 3;

	private static final Logger LOG = Logger.getLogger(PinyinUtil.class);
	/**
	 * 只获得拼音  比如"拼音"，获得pinyin
	 * */
	public static String getPinyinOnly(String str) {
		return getPart(str, 0);
	}
	/**
	 * 只获得缩写  比如"拼音" ，获得py
	 * */
	public static String getShortPinyin(String str) {
		return CommonUtils.isNull(getPart(str, 1)) ? getPart(str, 0) : getPart(str, 1);
	}

	private static String getPart(String str, int index) {
		String[] split = getPinyin(str).split(";");
		if (!CommonUtils.isNull(split) && split.length > index) {
			return split[index];
		}
		return "";
	}
	/**
	 * 获得拼音，如果不是汉字，则返回原字符串
	 * @param 需要转换成拼音的汉字
	 * @return pinyin;py
	 * */

	public static String getPinyin(String str) {
		if (CommonUtils.isNull(str)) {
			return ";";
		}
		HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		StringBuilder sb = new StringBuilder();
		appendPinyin(str, sb, outputFormat);
		appendFirstCharInCH(str, sb, outputFormat);
		return sb.toString();
	}
	
	/**
	 * 汉语人名 转为 拼音组成的英文人名
	 * 按英文人名规则，名在前，姓在后，以空格区分
	 * @param chines
	 *          汉字人名
	 * @return 拼音的英文人名
	 */
	public static String getPinyinMingXing(String chinesName){
		
		if(PinyinUtil.isEnName(chinesName)){
			return chinesName;
		}
		
		String lastName = "";
		StringBuilder firstName = new StringBuilder("");
		StringBuilder engNameBuff = new StringBuilder();
		String pinyinChar = "";
		char[] nameChar = chinesName.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);	
		int count = 0;
		int division = isDoubleLastName(chinesName)?2:1;  /** used for divide the lastname and firstname*/
		if(isMixChEn(nameChar)){
			return getMixChEn(nameChar);
		}
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > CH_CODE_VALUE) {
				count++;
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat);
					pinyinChar = (null != temp)? temp[0]:"";
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					LOG.error(e);
				}
				if(count<=division){
					lastName += pinyinChar;
				}else{
					/** 处理名中含u:特殊情况，名转换为u，姓默认为v */
	                if(pinyinChar.endsWith("v")){
	                	pinyinChar = pinyinChar.replace("v", "u");
					}
	                firstName.append(pinyinChar);
				}			
			}
		}
		engNameBuff.append(firstName).append(" ").append(lastName);
		return engNameBuff.toString();
	}
	/**
	 * 将str转换成拼音，并将转化结果插入到sb中
	 * @param str 待转换的中文串(可中英混合)
	 * @param sb 保存结果的StringBuilder对象
	 * @param outputFormat 拼音格式化对象
	 */
	private static void appendPinyin(String str, StringBuilder sb, HanyuPinyinOutputFormat outputFormat){
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch < PinyinUtil.CH_START || ch > PinyinUtil.CH_END) {
				sb.append(ch);
			} else {
				String[] pinyinArray = null;
				try {
					pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					LOG.error(e.getMessage());
				}
				if (pinyinArray != null && pinyinArray.length > 0) {
					sb.append(pinyinArray[0]);
				}
			}
		}
	}
	/**
	 * 将Str中的中文字符的首字母提取出来，并插入到sb中，与前面的字符串用";"隔开
	 * @param str 待转换的中文串(可中英文混合)
	 * @param sb 保存结果的StringBuilder对象
	 * @param outputFormat 拼音格式化对象
	 */
	private static void appendFirstCharInCH(String str, StringBuilder sb, HanyuPinyinOutputFormat outputFormat){
		boolean firstChineseChar = true;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch >= PinyinUtil.CH_START && ch <= PinyinUtil.CH_END) {
				if (firstChineseChar) {
					sb.append(SPLIT_CHAR);
					firstChineseChar = false;
				}
				String[] pinyinArray = null;
				try {
					pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch, outputFormat);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					LOG.error(e.getMessage());
				}
				if (pinyinArray != null && pinyinArray.length > 0) {
					sb.append(pinyinArray[0].charAt(0));
				}
			}
		}
		if (sb.indexOf(";") < 0) {
			sb.append(";");
		}
	}
	/**
	 * 判断字符数组array是否是中英文混合
	 * @param array
	 * @return
	 */
	private static boolean isMixChEn(char[] array){
		if(null == array || array.length<=0){
			return false;
		}
		boolean en = false;
		boolean zh = false;
		for(int i=0; i<array.length && !(en && zh); i++){
			if(array[i] > CH_CODE_VALUE){
				zh = true;
			}else{
				en = true;
			}
		}
		return en && zh;
	}
	/**
	 * 将中英文混合的array转换成英文字符串
	 * @param array
	 * @return
	 */
	private static String getMixChEn(char[] array){
		StringBuilder sb = new StringBuilder();
		if(null != array && array.length>0){
			for(int i=0; i<array.length; i++){
				if(array[i] > CH_CODE_VALUE){
					sb.append(getPinyinOnly(String.valueOf(array[i])));
				}else{
					sb.append(array[i]);
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 将中文姓名转化为拼音，结果由三部分组成：姓+空格+名
	 * @param chiName 中文名
	 * @return 英文名
	 */
	public static String getPinyinXingMing(String chiName){
		String mingXing = getPinyinMingXing(chiName);
		int space = mingXing.indexOf(' ');
		if(space>0){
			String ming = mingXing.substring(0, space);
			String xing = mingXing.substring(space+1, mingXing.length());
			return xing+" "+ming;
		}else{
			return mingXing;
		}
	}
	
	private static String[]  doubleNames = {"皇甫","公孙","慕容","欧阳","上官","司马","司徒","尉迟","长孙","诸葛"};
	/**判定是否为复姓
	 * @param chineseName 中文姓名
	 * @return boolean
	 * */
	public static boolean isDoubleLastName(String chinesName){
		if(chinesName.length() >= DB_CHNAME_LEN){
			for(int i=0;i<doubleNames.length;i++){
				if(chinesName.startsWith(doubleNames[i])){
					return true;
				}
			}
		}
		return false;
	}
	/**判定是否是中文
	 * @param  name 中文姓名
	 * @return boolean
	 * */
	public static boolean isChineseName(String name) {
		char[] nameChar = name.toCharArray();
		return nameChar[0] < CH_CODE_VALUE ? false : true;
	}

	
	/**判定是否是纯拼音（英文）名字
	 * @param  name 中文姓名
	 * @return boolean
	 * */
	public static boolean isEnName(String name) {
		char[] nameChar = name.toCharArray();
		if(null == nameChar || nameChar.length<=0){
			return false;
		}
		boolean en = false;
		boolean zh = false;
		for(int i=0; i<nameChar.length && !(en && zh); i++){
			if(nameChar[i] > CH_CODE_VALUE){
				zh = true;
			}else{
				en = true;
			}
		}
		return en && !zh;
	}


}
