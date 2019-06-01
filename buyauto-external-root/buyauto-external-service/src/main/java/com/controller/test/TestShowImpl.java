package com.controller.test;

import org.springframework.stereotype.Service;

import com.external.test.api.ITestShowApi;

@Service("testShowImpl")
public class TestShowImpl implements ITestShowApi {

	@Override
	public void alertName(String name) {
		System.out.println(name);
	}

}
