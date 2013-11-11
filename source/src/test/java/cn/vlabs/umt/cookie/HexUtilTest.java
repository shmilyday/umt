package cn.vlabs.umt.cookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.duckling.common.crypto.HexUtil;

public class HexUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToHexString() {
		String hex = HexUtil.toHexString(new byte[]{1,2});
		assertEquals("0102", hex);
		hex = HexUtil.toHexString(new byte[]{15,16});
		assertEquals("0F10", hex);
	}

	@Test
	public void testToBytes() {
		byte[] bytes = HexUtil.toBytes("0102");
		assertTrue(equal(new byte[]{1,2}, bytes));
		bytes = HexUtil.toBytes("0F10");
		assertTrue(equal(new byte[]{15,16}, bytes));
	}

	private boolean equal(byte[] a, byte[] b){
		if (a==b)
			return true;
		if (a!=null && b!=null){
			if (a.length==b.length){
				boolean match = true;
				for (int i=0;i<a.length;i++){
					if (a[i]!=b[i]){
						match=false;
						break;
					}
				}
				return match;
			}
		}
		return false;
	}
}
