package cn.vlabs.umt.user.xls;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.umt.common.xls.UserVO;
import cn.vlabs.umt.common.xls.UserXLSParser;
import cn.vlabs.umt.common.xls.XLSException;
import cn.vlabs.umt.services.user.bean.User;

public class UserXLSParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadUsers() throws IOException, XLSException {
		FileInputStream in=null;
		try{
			in = new FileInputStream("c:\\users.xls");
			UserXLSParser parser = new UserXLSParser(in);
			List<UserVO> users = parser.readUsers(0, parser.getCount());
			for (User user:users){
				System.out.println(user.getUmtId());
			}
		}finally{
			if (in!=null)
				in.close();
		}
	}

}
