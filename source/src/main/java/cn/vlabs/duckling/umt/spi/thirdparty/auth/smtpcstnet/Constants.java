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
package cn.vlabs.duckling.umt.spi.thirdparty.auth.smtpcstnet;

public final class Constants {
	private Constants(){}
	public static final String AUTH_HOST_KEY = "smtp.auth.host";
	public static final String AUTH_EHLO_KEY = "smtp.auth.ehlo";
	public static final String AUTH_HOST_PORT_KEY = "smtp.auth.host.port";
	public static final String AUTH_HOSTS_CONNECT_TIMES_KEY = "smtp.auth.host.connect.times";
	public static final String AUTH_HOST_TIMEOUT_KEY = "smtp.auth.host.connect.timeout";
	public static final String AUTH_HOST_CONNECT_ERROR_WAITING_KEY = "smtp.auth.host.connect.error.waitingtime";
}