package com.wk.cms.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wk.cms.controller.vo.Message;
import com.wk.cms.model.WallPaper;
import com.wk.cms.service.IWallpaperService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.PageInfo;

@Controller
@RequestMapping("/wallpaper")
public class WallpaperController {

	@Autowired
	private IWallpaperService wallpaperService;
	
	@RequestMapping("/save")
	public @ResponseBody Message save(@RequestParam("f") MultipartFile file) throws ServiceException{
		
		WallPaper wp = wallpaperService.save(file);
		wp.getFile().setContent(null);
		return new Message(true, "保存成功！", wp);
	}
	
	@RequestMapping("/list")
	public @ResponseBody PageInfo list() throws ServiceException{
		
		return wallpaperService.findByUser();
	}
	
	@RequestMapping("/delete")
	public @ResponseBody Message delete(String id) throws ServiceException{
		
		wallpaperService.deleteById(id);
		return new Message(true, "删除成功", "");
	}
	
	@RequestMapping("/use")
	public @ResponseBody Message use(String id) throws ServiceException{
		
		wallpaperService.assign(id);
		return new Message(true, "设置成功", "");
	}
	
	@RequestMapping("/current")
	public void current(HttpServletRequest request,HttpServletResponse response) throws ServiceException{
		
		WallPaper wp = wallpaperService.findCurrent();
		
		if(wp==null) throw new ServiceException("当前用户未设置WallPaper!");
		try {
			WebUtils.issueRedirect(request, response, "/file/app/"+wp.getFile().getId());
		} catch (IOException e) {
			throw new ServiceException(e.getMessage(),e);
		}
	}
}
