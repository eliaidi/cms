package com.wk.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.service.ITemplateService;
import com.wk.cms.utils.PageInfo;

@Service
public class TemplateService implements ITemplateService {

	@Autowired
	private ITemplateDao templateDao;

	@Override
	public PageInfo find(PageInfo pageInfo, String query) {
		return templateDao.find(pageInfo, query);
	}

}
