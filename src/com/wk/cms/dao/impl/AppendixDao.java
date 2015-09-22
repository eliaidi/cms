package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IAppendixDao;
import com.wk.cms.model.Appendix;
import com.wk.cms.utils.PageInfo;

@Repository
public class AppendixDao implements IAppendixDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;
	@SuppressWarnings("unchecked")
	@Override
	public PageInfo find(String documentId, Integer type, PageInfo pageInfo) {
		
		List<Appendix > appendixs = (List<Appendix>) hibernateTemplate.find("select a from Appendix a where a.document.id=? "+(type==null?"":" and a.type="+type), documentId);
		return new PageInfo(appendixs,(long) appendixs.size());
	}

	@Override
	public void save(Appendix appendix) {
		
		if(!StringUtils.hasLength(appendix.getId())){
			appendix.setId(UUID.randomUUID().toString());
			
			hibernateTemplate.save(appendix);
		}else{
			hibernateTemplate.update(appendix);
		}
		
	}

	@Override
	public Appendix findById(String id) {
		
		return hibernateTemplate.get(Appendix.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Appendix> findByDocId(String documentId) {
		
		List<Appendix> appendixs = (List<Appendix>) hibernateTemplate.find("select a from Appendix a where a.document.id=? order by a.type,a.file.crTime ", documentId);
		return appendixs;
	}

	@Override
	public void delete(String id) {
		hibernateTemplate.bulkUpdate("delete from Appendix a where a.id=?", id);
		
	}

}
