package com.wk.cms.service.impl;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.dao.IWallpaperDao;
import com.wk.cms.model.File;
import com.wk.cms.model.User;
import com.wk.cms.model.WallPaper;
import com.wk.cms.service.IWallpaperService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.PageInfo;

@Service
public class WallpaperService implements IWallpaperService {

	@Autowired
	private IWallpaperDao wallpaperDao;

	@Override
	public WallPaper save(MultipartFile file) throws ServiceException {

		if (!FileUtils.isPic(file.getOriginalFilename())) {
			throw new ServiceException("文件后缀名不合法，仅支持jpg,png格式");
		}
		File f = new File(null, file, null);

		WallPaper w = new WallPaper();
		w.setFile(f);
		w.setUser(User.findByUsername(SecurityUtils.getSubject().getPrincipal()
				.toString()));
		w.setCrTime(new Date());

		wallpaperDao.save(w);
		return w;
	}

	@Override
	public PageInfo findByUser() {
		return wallpaperDao.findByUser(User.findByUsername(SecurityUtils
				.getSubject().getPrincipal().toString()));
	}

	@Override
	public void deleteById(String id) {
		
		wallpaperDao.deleteById(id);
	}

	@Override
	public void assign(String id) {
		WallPaper wp = findById(id);
		wp.setUseTime(new Date());
		
		wallpaperDao.save(wp);
	}

	@Override
	public WallPaper findById(String id) {
		return wallpaperDao.findById(id);
	}

	@Override
	public WallPaper findCurrent() {
		return wallpaperDao.findCurrent(SecurityUtils
				.getSubject().getPrincipal().toString());
	}

}
