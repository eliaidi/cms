package com.wk.cms.utils;

import com.wk.cms.service.exception.ServiceException;

public interface CallBack {

	public void doCallBack(Object[] objs) throws ServiceException;

}
