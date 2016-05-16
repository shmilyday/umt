package cn.vlabs.umt.authclient.impl;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.duckling.common.http.WebSite;

public class WebClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetHttpClient() throws HttpException, IOException {
		WebSite web = new WebSite("https://umt.escience.cn");
		GetMethod method = web.createGetMethod("/sso/ssoLogin");
		web.exec(method);
		web.close();
		System.out.println(method.getResponseBodyAsString());
	}

}
