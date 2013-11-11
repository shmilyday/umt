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
package cn.vlabs.umt.client.impl;

import com.thoughtworks.xstream.XStream;

public class Envelope {
	public Envelope(String content, String signature){
		this.content=content;
		this.signature=signature;
	}
	
	public String toXML(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<Envelope><content><![CDATA[");
		buffer.append(content);
		buffer.append("]]></content><signature>");
		buffer.append(signature);
		buffer.append("</signature></Envelope>");
		return buffer.toString();
	}
	
	public static Envelope valueOf(String xml){
		if (xml==null){
			return null;
		}
		return (Envelope) stream.fromXML(xml);
	}
	
	public String getContent(){
		return this.content;
	}
	public String getSignature(){
		return this.signature;
	}
	private String content;
	private String signature;
	
	private static XStream stream;
	
	static{
		stream = new XStream();
		stream.alias("Envelope", Envelope.class);
	}
}