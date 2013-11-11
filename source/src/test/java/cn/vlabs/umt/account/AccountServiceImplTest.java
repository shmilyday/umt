package cn.vlabs.umt.account;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.account.StatisticBean;
import cn.vlabs.umt.tickets.impl.TestHelper;

public class AccountServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		TestHelper.close();
	}

	@Test
	public void testGetLoginCount() {
		IAccountService as = (IAccountService) TestHelper.getBeanFactory().getBean("AccountService");
		as.login("A", "http://localhost", 1, "159.226.10.63", new Date(), "Firefox/3.0", null);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.HOUR, -1);
		Collection<StatisticBean> stat= as.getLoginCount(calendar.getTime(), new Date());
		assertNotNull(stat);
		assertTrue(stat.size()>0);
	}

}
