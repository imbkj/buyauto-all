package com.business.service.products.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.products.api.IProductsTempApi;
import com.buyauto.entity.products.TProductsTemp;
import com.buyauto.mapper.products.TProductsTempMapper;

@Service
public class IProductsTempImpl implements IProductsTempApi {
	@Autowired
	private TProductsTempMapper tProductsTemp;
	
	@Override
	public Integer insertProductTemp(TProductsTemp Ptemp) {
		return tProductsTemp.insert(Ptemp);
	}

}
