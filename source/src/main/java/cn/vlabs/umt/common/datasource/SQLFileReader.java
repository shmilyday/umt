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
package cn.vlabs.umt.common.datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SQLFileReader {
	/**
	 * 读取 SQL 语句队列
	 * @param in
	 * @return
	 * @throws IOException
	 */
    public List<String> loadSql(InputStream in) throws IOException {
    	if (in==null)
    	{
    		return null;
    	}
    	ArrayList<String> sqls= new ArrayList<String>();
    	String delimiter=";";
    	
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line = null;
			StringBuffer sql=new StringBuffer();
			while ((line=reader.readLine())!=null){
				line = line.trim();
				
				if (line.length()!=0){
					if (isDelimiter(line)){
						delimiter=parseDelimiter(line);
						continue;
					}
					
					sql.append("\n");
					
					//每行的最后一个字母是;的话，视为一句结束
					if (line.endsWith(delimiter)){
						line =line.replace(delimiter, "");
						sql.append(line);
						sqls.add(sql.toString());
						sql= new StringBuffer();
					}else{
						sql.append(line);
					}
				}

			}
			if (sql.length()>0){
				sqls.add(sql.toString());
			}
			return sqls;
		}finally{
			in.close();
		}
    }
    
	private boolean isDelimiter(String line){
		String newLine = line.toLowerCase();
		return newLine.startsWith("delimiter");
	}
	private String parseDelimiter(String line){
		String newLine= line.toLowerCase();
		String result = newLine.replace("delimiter", "");
		return result.trim();
	}
}