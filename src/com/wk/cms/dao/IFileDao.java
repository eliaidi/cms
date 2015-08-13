package com.wk.cms.dao;

import java.util.List;

import com.wk.cms.model.File;

public interface IFileDao {

	File findById(String id);

	void save(File f);

	List<File> findByIds(String[] ids);

}
