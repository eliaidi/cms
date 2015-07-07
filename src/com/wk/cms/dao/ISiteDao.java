package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.Site;

public interface ISiteDao {

	List<Site> findAll();

	void save(Site site);

	Site findByName(String name);

	Site findById(String siteId);

	void deleteById(String siteId);
}
