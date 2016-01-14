package com.wk.cms.dao;

import com.wk.cms.model.User;
import com.wk.cms.model.WallPaper;
import com.wk.cms.utils.PageInfo;

public interface IWallpaperDao {

	void save(WallPaper w);

	PageInfo findByUser(User findByUsername);

	void deleteById(String id);

	WallPaper findById(String id);

	WallPaper findCurrent(String username);

}
