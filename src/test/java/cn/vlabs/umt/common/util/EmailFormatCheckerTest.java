package cn.vlabs.umt.common.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EmailFormatCheckerTest {
	@Test
	public void test() {
		assertTrue(EmailFormatChecker.isValidEmail("test@cnic.cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test-ntarl@cnic.cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test@cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test2233@cnic-tee.cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test_2233@cnic.cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test.test@cn"));
		assertTrue(EmailFormatChecker.isValidEmail("test.test@163.com"));
		assertTrue(EmailFormatChecker.isValidEmail("test@gmail.com"));
		assertTrue(EmailFormatChecker.isValidEmail("test@gmailllllllllllllll.comddddddd"));
		assertTrue(EmailFormatChecker.isValidEmail("test@gmai.gmai.gmai.gmailllllllllllllll.comddddddd"));
		assertTrue(EmailFormatChecker.isValidEmail(""));
	}

}
