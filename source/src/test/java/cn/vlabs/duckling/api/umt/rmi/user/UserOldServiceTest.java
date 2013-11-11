package cn.vlabs.duckling.api.umt.rmi.user;

import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.UserExistException;

public class UserOldServiceTest {
	private UserService userService;
	@Before
	public void setUp() throws Exception {
		String serviceURL = "http://localhost/services";
		userService = new UserService(serviceURL);
	}

	@After
	public void tearDown() throws Exception {
		userService=null;
	}
	//测试用户创建，获取
	@Test
	public void testCreateUser() {
		Random random=new Random();
		String username=random.nextInt(1000000)+"test@root.umt";
		UMTUser user = new UMTUser(username, "匿名用户",
				username, "ChangeIt");
		try {
			userService.createUser(user);
		} catch (UserExistException e) {
			e.printStackTrace();
		}
		boolean isExist=false;
		try {
			userService.createUser(user);
		} catch (Exception e) {
			isExist=true;
		}
		Assert.assertTrue(isExist);
		user=userService.getUMTUser(username);
		Assert.assertNotNull(user);
	}
	//测试一下coreMail有，umt没有的用户，isExists()和getUMTUser()
	@Test
	public void testCoreMail()throws Exception{
		String username="zzxx@cstnet.cn";
		UMTUser user =userService.getUMTUser(username);
		if("11".equals(user.getTruename())){
			System.out.println("first coreMail getUMT");
		}else{
			Assert.assertEquals("hha", user.getTruename());
		}
		Assert.assertTrue(userService.isExist(username));
		user.setTruename("hha");
		userService.updateUser(user);
		user=userService.getUMTUser(username);
		Assert.assertEquals("hha", user.getTruename());
	}
	@Test
	public void testCount(){
		System.out.println(userService.getUserCount("1"));
	}
	@Test
	public void testLogin(){
		UserPrincipal up=(userService.login("haha@cstnet.cn", "fufyddns111"));
		Assert.assertEquals("123123", up.getDisplayName());
		Assert.assertEquals("haha@cstnet.cn",up.getEmail());
		Assert.assertEquals("haha@cstnet.cn",up.getName());
		Assert.assertEquals("", up.getAuthBy());
	}
	@Test 
	public void testIsExistsArray(){
		boolean[] result=userService.isExist(new String[]{"haha@cstnet.cn","zzxx@cstnet.cn","234"});
		Assert.assertTrue(result[0]);
		Assert.assertTrue(result[1]);
		Assert.assertFalse(result[2]);
	}
	@Test
	public void testSearch(){
		userService.searchUserLikeName("haha", 10);
	}

	

}
