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
package cn.vlabs.umt.common.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import cn.vlabs.umt.common.util.RandomUtil;

import com.thoughtworks.xstream.XStream;

public class RecordFile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7770071508295648294L;
	public RecordFile(ServletContext app){
		this.datadir=mktmp(app);
		this.stream=getXStream();
	}
	
	
	public void save(int page, List<UserVO> users) throws IOException{
		String xmlfile = datadir+"/"+page+".xml";
		Writer writer=null;
		try{
		writer = new OutputStreamWriter(new FileOutputStream(xmlfile),"UTF-8");
		stream.toXML(users, writer);
		}finally{
			if (writer!=null)
			{
				writer.close();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<UserVO> load(int page) throws IOException{
		XStream tStream = new XStream();
		Reader reader =null;
		try{
		 reader= new InputStreamReader(new FileInputStream(datadir+"/"+page+".xml"), "UTF-8");
		return (ArrayList<UserVO>)tStream.fromXML(reader);
		}finally{
			if (reader!=null)
			{
				reader.close();
			}
		}
	}
	
	public String getDatadir(){
		return datadir;
	}
	
	public int getPageCount(){
		return totalPage;
	}
	public void setPageCount(int pageCount){
		this.totalPage=pageCount;
	}
	private String mktmp(ServletContext app){
		RandomUtil ru = new RandomUtil();
		
		String random = null;
		File f = null;
		String dir = null;
		do {
			random = ru.getRandom(5);
			dir = app.getRealPath("/WEB-INF/tmp/"+random);
			f= new File(dir);
		}while (f.exists());
		f.mkdirs();
		return dir;
	}
	
	private XStream getXStream(){
		XStream stream = new XStream();
		return stream;
	}
	private int totalPage;
	private XStream stream;
	private String datadir;
}