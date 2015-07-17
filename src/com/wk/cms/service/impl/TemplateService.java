package com.wk.cms.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.model.Template;
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

	@Override
	public void save(Template template) {

		if (!StringUtils.hasLength(template.getId())) {

			template.getFile().setCrTime(new Date());
			template.getFile().setCrUser(null);

			templateDao.save(template);
		} else {

			Template pt = findById(template.getId());

			BeanUtils.copyProperties(template.getFile(), pt.getFile(),
					new String[] { "id", "crTime", "crUser" });
			BeanUtils.copyProperties(template, pt,
					new String[] { "id", "file" });

			templateDao.save(pt);
		}
	}

	@Override
	public Template findById(String id) {
		return templateDao.findById(id);
	}

	@Override
	public void delete(String[] ids) {

		for (String id : ids) {
			delete(id);
		}
	}

	@Override
	public void delete(String id) {

		templateDao.delete(id);
	}

}
