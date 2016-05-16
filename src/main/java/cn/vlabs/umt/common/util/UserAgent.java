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

public class UserAgent {  
    private String browserType;//浏览器类型  
    private String browserVersion;//浏览器版本  
    private String platformType;//平台类型  
    private String platformSeries;//平台系列  
    private String platformVersion;//平台版本  
      
    public UserAgent(){}  
      
    public UserAgent(String browserType, String browserVersion,  
            String platformType, String platformSeries, String platformVersion){  
        this.browserType = browserType;  
        this.browserVersion = browserVersion;  
        this.platformType = platformType;  
        this.platformSeries = platformSeries;  
        this.platformVersion = platformVersion;  
    }  
      
    public String getBrowserType() {  
        return browserType;  
    }  
    public void setBrowserType(String browserType) {  
        this.browserType = browserType;  
    }  
    public String getBrowserVersion() {  
        return browserVersion;  
    }  
    public void setBrowserVersion(String browserVersion) {  
        this.browserVersion = browserVersion;  
    }  
    public String getPlatformType() {  
        return platformType;  
    }  
    public void setPlatformType(String platformType) {  
        this.platformType = platformType;  
    }  
    public String getPlatformSeries() {  
        return platformSeries;  
    }  
    public void setPlatformSeries(String platformSeries) {  
        this.platformSeries = platformSeries;  
    }  
    public String getPlatformVersion() {  
        return platformVersion;  
    }  
    public void setPlatformVersion(String platformVersion) {  
        this.platformVersion = platformVersion;  
    }  
      
}  
