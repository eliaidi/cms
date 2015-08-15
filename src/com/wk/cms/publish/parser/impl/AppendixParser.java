package com.wk.cms.publish.parser.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Appendix;
import com.wk.cms.model.Document;
import com.wk.cms.model.Appendix.Type;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.PublishUtils;

@Component("AppendixComp")
public class AppendixParser extends AbstractTagParser {

	@Autowired
	private IAppendixService appendixService;
	@Override
	protected String parseInternal(Object obj)
			throws ServiceException {
		
		HtmlTag e = getTag();
		Object base = ctx.getBase();
		String field = e.attr("field");
		String index = e.attr("index");
		String type = e.attr("type");
		
		if(!(obj instanceof Document)){
			throw new ServiceException("当前上下文环境错误！"+obj);
		}
		Document doc = (Document) obj;
		List<Appendix> appendixs = appendixService.find(doc,Type.valueOf(type));
		if(CommonUtils.isEmpty(appendixs)){
			return "";
		}
		List<String> objList = new ArrayList<String>();
		
		for(int i=0;i<appendixs.size();i++){
			Appendix app = appendixs.get(i);
			if(StringUtils.hasLength(index)&&i!=(Integer.parseInt(index)-1)){
				continue;
			}
			String val = null;
			if(!StringUtils.hasLength(field)){
				String link = getPubDir(base, obj)+app.getFile().getFileName();
				val = app.getFile().isPic()?"<img src=\""+link+"\" title=\""+app.getAddition()+"\" />":"<a href=\""+link+"\" alt=\""+app.getAddition()+"\"></a>";
			}else if("_url".equalsIgnoreCase(field)){
				val = getPubDir(base, obj)+app.getFile().getFileName();
			}else{
				val = CommonUtils.getDeepFieldValue(app, field).toString();
			}
			downLoadApp(app);
			objList.add(StringEscapeUtils.escapeJava(val));
		}
		return CommonUtils.join(objList, "<br/>");
	}
	private void downLoadApp(Appendix app) {
		String fileStr = ctx.isPreview()?PublishUtils.getPreviewDir(app.getDocument()):PublishUtils.getPublishDir(app.getDocument());
//		fileStr += app.getFile().getFileName();
		
		File destFolder = new File(fileStr);
		File destFile = new File(destFolder,app.getName());
		if(!destFile.exists()||"true".equalsIgnoreCase(ctx.getTag().attr("force"))){
			try {
				FileUtils.writeFile(app.getFile(), destFolder);
			} catch (ServiceException e) {
				LOGGER.error("下载文档附件失败！"+app,e);
			}
		}
	}

}
