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
package cn.vlabs.duckling.ca;

import java.io.IOException;
import java.io.Reader;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XPathParser {
	public static String parse(Reader reader, String xpath) {
		DOMParser parser = new DOMParser();
		try {
			parser.setFeature("http://xml.org/sax/features/namespaces", false);
			parser.parse(new InputSource(reader));
			Document doc = parser.getDocument();
			Node node = XPathAPI.selectSingleNode(doc, xpath);
			if (node!=null){
				return node.getTextContent().trim();
			}
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getNodeValue(Reader reader, String xpath,String attrName) {
		DOMParser parser = new DOMParser();
		try {
			parser.setFeature("http://xml.org/sax/features/namespaces", false);
			parser.parse(new InputSource(reader));
			Document doc = parser.getDocument();
			Node node = XPathAPI.selectSingleNode(doc, xpath);
			if (node!=null){
				return node.getAttributes().getNamedItem(attrName).getNodeValue();
			}
		} catch (SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		return "";
	}
}
