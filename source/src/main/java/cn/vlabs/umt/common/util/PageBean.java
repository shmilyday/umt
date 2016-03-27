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

public class PageBean {
	public PageBean(int total){
		this.total=total;
	}
	
	public int getSize(){
		return total;
	}
	
	public int getPageCount(){
		int pagecount= total/recordsPerPage+((total%recordsPerPage)==0?0:1);
		if (pagecount==0)
		{
			pagecount=1;
		}
		return pagecount;
	}
	
	public int getRecordPerPage(){
		return recordsPerPage;
	}
	public Object getItems(){
		return items;
	}
	public boolean isFirstPage(){
		return currentPage==0;
	}
	
	public boolean isLastPage(){
		return currentPage==getPageCount()-1; 
	}
	public void setItems(Object items){
		this.items=items;
	}
	
	public int getStart(){
		return currentPage*recordsPerPage;
	}
	public void setCurrentPage(int page){
		if (page<0)
			this.currentPage=0;
		else if (page<this.getPageCount()){
			this.currentPage=page;
		}else
		{
			this.currentPage=this.getPageCount()-1;
		}
	}
	
	public int getCurrentPage(){
		return this.currentPage;
	}
	public void setRecordsPerPage(int records){
		recordsPerPage=records;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	private String query;
	private int currentPage=0;
	private Object items;
	private int recordsPerPage=20;
	private int total;
}