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
package cn.vlabs.umt.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
02
 *
03
 * @author cc
04
 * 20120307
05
 */

public class BrowseUtils {
	private final static String IE10="MSIE 10.0";
    private final static String IE9="MSIE 9.0";

    private final static String IE8="MSIE 8.0";

    private final static String IE7="MSIE 7.0";

    private final static String IE6="MSIE 6.0";

    private final static String MAXTHON="Maxthon";

   private final static String QQ="QQBrowser";

    private final static String GREEN="GreenBrowser";

    private final static String SE360="360SE";

    private final static String FIREFOX="Firefox";

    private final static String OPERA="Opera";

    private final static String CHROME="Chrome";

   private final static String SAFARI="Safari";

    private final static String OTHER="其它";

     

     

    public String checkBrowse(String userAgent){

        if(regex(OPERA, userAgent)){
        	return OPERA;
        }

        if(regex(CHROME, userAgent)){
        	return CHROME;
        }

        if(regex(FIREFOX, userAgent)){
        	return FIREFOX;
        }

        if(regex(SAFARI, userAgent)){
        	return SAFARI;
        }

        if(regex(SE360, userAgent)){
        	return SE360;
        }

        if(regex(GREEN,userAgent)){
        	return GREEN;
        }

        if(regex(QQ,userAgent)){
        	return QQ;
        }

        if(regex(MAXTHON, userAgent)){
        	return MAXTHON;
        }

       if(regex(IE9,userAgent)){
    	   return IE9;
       }

       if(regex(IE8,userAgent)){
    	   return IE8;
       }

        if(regex(IE7,userAgent)){
        	return IE7;
        }

       if(regex(IE6,userAgent)){
    	   return IE6;
       }
       if(regex(IE10,userAgent)){
    	   return IE10;
       }

        return OTHER;

    }

    public boolean regex(String regex,String str){

        Pattern p =Pattern.compile(regex,Pattern.MULTILINE);

        Matcher m=p.matcher(str);

        return m.find();

    }
}