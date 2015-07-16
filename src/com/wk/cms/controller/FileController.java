package com.wk.cms.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wk.cms.model.File;
import com.wk.cms.service.IFileService;
import com.wk.cms.service.exception.ServiceException;

@Controller
@RequestMapping("/file")
public class FileController {

	@Autowired
	private IFileService fileService;
	@RequestMapping("/app/{id}")
	public void readAppFile(@PathVariable("id") String id,HttpServletRequest request,HttpServletResponse response) throws ServiceException, SQLException, IOException{
		
		File file = fileService.findById(id);
		
		response.setHeader("Content-disposition", "attachment;filename="  
	              + file.getFileName());  
		response.setHeader("Content-Length", String.valueOf(file.getFileSize()));
		response.setContentType("application/octet-stream;charset=UTF-8");
	    
		
		InputStream is = file.getContent().getBinaryStream();
		OutputStream bos = response.getOutputStream();
		byte[] buff = new byte[10000];
		int len;
		while((len = is.read(buff))!=-1){
			bos.write(buff, 0, len);
		}
		
		bos.flush();
		is.close();
		bos.close();
		
	}
}
