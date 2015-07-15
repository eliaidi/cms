package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.ISiteDao;
import com.wk.cms.model.Site;
import com.wk.cms.utils.CommonUtils;

@Repository
public class SiteDaoImpl implements ISiteDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Site> findAll() {

		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createQuery("from Site order by crTime asc")
				.setCacheable(true).setCacheRegion("site").list();
	}

	@Override
	public void save(Site site) {

		if (StringUtils.hasLength(site.getId())) {
			hibernateTemplate.update(site);
		} else {
			site.setId(UUID.randomUUID().toString());
			hibernateTemplate.save(site);
		}
	}

	@Override
	public Site findByName(String name) {

		List<Site> sites = (List<Site>) hibernateTemplate.find(
				"select s from Site s where s.name=?", name);
		return CommonUtils.isEmpty(sites) ? null : sites.get(0);
	}

	@Override
	public Site findById(String siteId) {

		return hibernateTemplate.get(Site.class, siteId);
	}

	@Override
	public void deleteById(String siteId) {

		Session s = hibernateTemplate.getSessionFactory().getCurrentSession();

		s.delete(findById(siteId));
		// hibernateTemplate.bulkUpdate("delete from Site s where s.id=?",
		// siteId);
	}

}
