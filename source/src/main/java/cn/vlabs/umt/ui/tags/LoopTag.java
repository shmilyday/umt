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
package cn.vlabs.umt.ui.tags;

import javax.servlet.jsp.tagext.TagSupport;

public class LoopTag extends TagSupport{
	public int doStartTag(){
		if (start<end){
			start++;
			return EVAL_BODY_INCLUDE;
		}else{
			return SKIP_BODY;
		}
	}
	
	public int doAfterBody(){
		if (start<end){
			start++;
			return EVAL_BODY_AGAIN;
		}else{
			return SKIP_BODY;
		}
	}
	
	public int doEndTag(){
		return EVAL_PAGE;
	}
	
	public void setEnd(int end){
		this.end=end;
	}
	
	public void setStart(int start){
		this.start=start;
	}
	
	private int end;
	private int start;
	private static final long serialVersionUID = 1L;
}