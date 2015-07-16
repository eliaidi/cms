package com.wk.cms.dao;

import com.wk.cms.model.File;

public interface IFileDao {

	File findById(String id);

	void save(File f);

}
