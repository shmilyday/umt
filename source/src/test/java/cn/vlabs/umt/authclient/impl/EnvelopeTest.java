package cn.vlabs.umt.authclient.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.umt.client.impl.Envelope;

public class EnvelopeTest {
	private static String xml="<Credential><User><name>xiejj@cnic.cn</name><truename>谢建军</truename></User></Credential>";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToXML() {
		Envelope env = new Envelope(xml, "ABC");
		String result = env.toXML();
		System.out.println(result);
		Envelope env1 = Envelope.valueOf(result);
		assertNotNull(env1);
	}
}
