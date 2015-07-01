package com.wk.cms.dao;

import com.wk.cms.model.Appendix;
import com.wk.cms.utils.PageInfo;

public interface IAppendixDao {

	PageInfo find(String documentId, Integer type, PageInfo pageInfo);

	void save(Appendix appendix);

	Appendix findById(String id);

}
