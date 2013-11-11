package cn.vlabs.umt.crypto.impl;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;

public class KeyFileTest {
	private KeyFile keyfile;
	@Before
	public void setUp() throws Exception {
		keyfile =new KeyFile();
	}

	@After
	public void tearDown() throws Exception {
		keyfile=null;
	}
	@Test
	public void testSave(){
		RSAKey key = new RSAKey();
		key.generate();
		
		keyfile.saveKey("c:\\tmp\\full.txt", key);
		
		RSAKey newKey = keyfile.load("c:\\tmp\\full.txt");
		
		byte[] raw = newKey.encrypt("ABC".getBytes());
		byte[] raw2 = key.encrypt("ABC".getBytes());
		assertTrue(HexUtil.isEqual(raw, raw2));
		
		byte[] decrypted1 = newKey.decrypt(raw);
		byte[] decrypted2= key.decrypt(raw);
		assertTrue(HexUtil.isEqual(decrypted1, decrypted2));
	}
	@Test
	public void testSaveKeyStringRSAPrivateKey() {
		RSAKey key = new RSAKey();
		key.generate();
		keyfile.savePrivate("c:\\tmp\\public.cer", key);
		RSAKey rsakey = keyfile.load("c:\\tmp\\public.cer");
		byte[] raw = key.encrypt("ABC".getBytes());
		byte[] raw2 = rsakey.decrypt(raw);
		assertTrue(HexUtil.isEqual("ABC".getBytes(), raw2));
	}

	@Test
	public void testSaveKeyStringRSAPublicKey() {
		RSAKey key = new RSAKey();
		key.generate();
		keyfile.savePublic("c:\\tmp\\public.cer", key);
		RSAKey rsakey = keyfile.load("c:\\tmp\\public.cer");
		byte[] raw = rsakey.encrypt("ABC".getBytes());
		byte[] raw2 = key.encrypt("ABC".getBytes());
		assertTrue(HexUtil.isEqual(raw, raw2));
	}

}
