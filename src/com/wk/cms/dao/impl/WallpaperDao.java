package com.wk.cms.dao.impl;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IWallpaperDao;
import com.wk.cms.model.User;
import com.wk.cms.model.WallPaper;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Repository
public class WallpaperDao implements IWallpaperDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public void save(WallPaper w) {

		if (StringUtils.hasLength(w.getId())) {
			hibernateTemplate.merge(w);
		} else {
			w.getFile().setId(UUID.randomUUID().toString());
			
			hibernateTemplate.save(w.getFile());
			w.setId(UUID.randomUUID().toString());

			hibernateTemplate.save(w);
		}
	}

	@Override
	public PageInfo findByUser(User findByUsername) {

		List<WallPaper> wallPapers = (List<WallPaper>) hibernateTemplate
				.find("select w from WallPaper w where w.user=? order by w.crTime desc",
						findByUsername);
		return new PageInfo(wallPapers, (long) wallPapers.size());
	}

	@Override
	public void deleteById(String id) {

		hibernateTemplate.delete(findById(id));
	}

	@Override
	public WallPaper findById(String id) {
		return hibernateTemplate.get(WallPaper.class, id);
	}

	@Override
	public WallPaper findCurrent(String username) {

		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(WallPaper.class);

		List<WallPaper> wallPapers = c.createAlias("user", "u")
				.add(Restrictions.eq("u.username", username))
				.add(Restrictions.isNotNull("useTime"))
				.addOrder(Order.desc("useTime")).setMaxResults(1).list();
		if (CommonUtils.isEmpty(wallPapers)) {
			return null;
		}
		return wallPapers.get(0);
	}

}
