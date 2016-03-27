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
/**
 * 
 */
package cn.vlabs.umt.ui.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author lvly
 * @since 2013-4-15
 */
public class IsEqualsContainTag extends TagSupport{
	private static final long serialVersionUID = 1234435345942755L;
	private Object var;
	private Object[] array;
	
	public static void main(String[] args) {
		String a="[1,2,3,4,4]";
		System.out.println(a.substring(1,a.length()-1));
	}
	
	public Object getVar() {
		return var;
	}

	public void setVar(Object var) {
		this.var = var;
	}

	public Object[] getArray() {
		return array;
	}

	public void setArray(Object[] array) {
		if(array==null){
			this.array=null;
		}else{
			this.array = array.clone();
		}
	}

	@Override
	public int doStartTag() throws JspException {
		if(array==null){
			return SKIP_BODY;
		}
		for(Object obj:array){
			if(obj.equals(var)){
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}
}
