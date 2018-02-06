package com.lianjiu.rest.web.test.user;

import java.util.Map;

import org.junit.Test;

import com.lianjiu.common.utils.Util;
import com.lianjiu.model.UserAddress;

public class UserControllerTest {

	@Test
	public void convert2MapTest() {
		UserAddress address = new UserAddress();
		address.setUserAddressId("dsadsadsad");
		address.setUserCity("dsadsd");
		Map<String, String> param = Util.convert2Map(address);
		for (String key : param.keySet()) {
			System.out.print(key + ":");
			System.out.println(param.get(key));
		}
	}
}
