package com.wk.cms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.dao.IPubLogDao;
import com.wk.cms.model.PubLog;
import com.wk.cms.service.IPubLogService;
import com.wk.cms.utils.PageInfo;

@Service
public class PubLogService implements IPubLogService {

	@Autowired
	private IPubLogDao pubLogDao;
	@Override
	public PubLog save(PubLog log) {
		return pubLogDao.save(log);
	}
	@Override
	public PageInfo find(String type, PageInfo pageInfo) {
		return pubLogDao.find(type,pageInfo);
	}
	@Override
	public PubLog noTransSave(PubLog log) {
		return pubLogDao.save(log);
	}
	@Override
	public void delete(String ids) {
		pubLogDao.delete(ids);
	}

}
