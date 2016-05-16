package cn.vlabs.umt.tickets.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.umt.services.ticket.InternalTicket;
import cn.vlabs.umt.services.ticket.Ticket;
import cn.vlabs.umt.services.ticket.impl.TicketDAOImpl;

public class TicketDAOImplTest {
	private TicketDAOImpl td;
	@Before
	public void setUp() throws Exception {
		td = (TicketDAOImpl) TestHelper.getBeanFactory().getBean("TicketDAO");
	}

	@After
	public void tearDown() throws Exception {
		TestHelper.close();
		td= null;
	}


	@Test
	public void testSave() {
		InternalTicket ticket = new InternalTicket();
		ticket.setCreateTime(new Date());
		ticket.setExtra("ABC");
		ticket.setRandom("1234567");
		ticket.setSessionid("1234");
		ticket.setType(Ticket.PROXY_TICKET);
		long id =td.save(ticket);
		assertTrue(id>0);
		ticket=null;
		
		ticket=td.load(id);
		assertNotNull(ticket);
		
		td.remove(id);
		
		ticket=td.load(id);
		assertTrue(ticket==null);
	}

	
	
}
