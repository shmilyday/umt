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
package cn.vlabs.umt.common.xls;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class UserXLSParser {
	public UserXLSParser(InputStream in) throws XLSException{
		HSSFWorkbook workbook;
		try {
			workbook = new HSSFWorkbook(in);
			sheet =workbook.getSheetAt(0);
			total=sheet.getLastRowNum();
		} catch (IOException e) {
			throw new XLSException(e.getMessage()); 
		} 
	}
	
	public int getCount(){
		return total;
	}
	/**
	 * 读取用户
	 * @param start 从0开始
	 * @param count 读取的用户数
	 * @return 用户信息
	 */
	public List<UserVO> readUsers(int start, int count){
		List<UserVO> users=new ArrayList<UserVO>();
		int i=0;
		int index=start+1;
		while (i<count && index<=total){
			UserVO u = parseLine(sheet, index);
			
			if (u.getUmtId()!=null && ! usernames.contains(u.getUmtId())){
				users.add(u);
				usernames.add(u.getUmtId());
				i++;
			}
			
			index++;
		}
		return users;
	}
	
	private UserVO parseLine(HSSFSheet sheet, int row){
		// 根据行数取得Sheet的一行
		HSSFRow rowline = sheet.getRow(row);
		
		// 获取当前行的列数
		UserVO u = new UserVO();
		u.setUmtId(readCellValue(rowline.getCell((short)0)));
		u.setTrueName(readCellValue(rowline.getCell((short)1)));
		u.setPassword(readCellValue(rowline.getCell((short)2)));
		u.setCstnetId(readCellValue(rowline.getCell((short)3)));
		if (StringUtils.isEmpty(u.getCstnetId()))
		{
			u.setCstnetId(u.getUmtId());
		}
		return u;
	}
	
	private String readCellValue(HSSFCell cell){
		if (cell!=null){
			String cellvalue = null;
			switch (cell.getCellType()){
			case HSSFCell.CELL_TYPE_NUMERIC:
				cellvalue = Integer.toString((int)cell.getNumericCellValue());
				break;
			default:
				cellvalue=cell.getRichStringCellValue().getString();
			break;
			}
			return cellvalue;
		}else
		{
			return null;
		}
	}
	private Set<String> usernames=new HashSet<String>();
	private int total;
	private HSSFSheet sheet ;
}