package com.wk.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wk.cms.dao.IExtFieldDao;
import com.wk.cms.service.IExtFieldService;
import com.wk.cms.utils.PageInfo;

@Service
public class ExtFieldService implements IExtFieldService {

	@Autowired
	private IExtFieldDao extFieldDao;
	@Override
	public PageInfo find(String channelId,PageInfo pageInfo ,String query) {
		return extFieldDao.find(channelId,pageInfo,query);
	}

}
