package cn.vlabs.umt.crypto.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;

public class RSAKeyTest {
	private RSAKey rsa;
	@Before
	public void setUp() throws Exception {
		rsa = new RSAKey();
		rsa.generate();
	}

	@After
	public void tearDown() throws Exception {
		rsa=null;
	}

	@Test
	public void testSign() {
		byte[] bytes =rsa.sign("Not yet implemented".getBytes());
		System.out.println(HexUtil.toHexString(bytes));
		assertTrue(rsa.verify("Not yet implemented".getBytes(), bytes));
	}

	@Test
	public void testEncrypt() throws UnsupportedEncodingException {
		byte[] bytes =rsa.encrypt("ABC".getBytes("UTF-8"));
		System.out.println(HexUtil.toHexString(bytes));
		byte[] charbytes = rsa.decrypt(bytes);
		System.out.println(HexUtil.toHexString(charbytes));
		String result = new String(charbytes, "UTF-8");
		System.out.println(result);
		assertEquals(result, "ABC");
	}

}
