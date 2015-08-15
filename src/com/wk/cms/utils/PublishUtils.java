package com.wk.cms.utils;

import java.io.File;




import org.springframework.util.StringUtils;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.model.Template;
import com.wk.cms.service.ITemplateService;

public class PublishUtils {

	public static Site getSite(Object obj) {
		if(obj instanceof Site) return (Site)obj;
		if(obj instanceof Channel) return ((Channel)obj).getSite();
		if(obj instanceof Document) return ((Document) obj).getSite();
		return null;
	}

	public static String getDir(Object obj) {
		Site currSite = PublishUtils.getSite(obj);
		String dir = "";
		String sep = File.separator;
		if (obj instanceof Site) {
		} else if (obj instanceof Channel) {
			Channel chnl = (Channel) obj;
			while (chnl != null) {
				dir = sep + chnl.getFolder() + dir;
				chnl = chnl.getParent();
			}
		} else if (obj instanceof Document) {
			Document doc = (Document) obj;
			Channel currChnl = doc.getChannel();
			dir = getDir(currChnl);
		}
		dir = sep + currSite.getFolder() + dir;
		return dir;
	}
	
	public static String getPath2Path(String f, String t) {
		
		String[] fArr = f.split("\\"+File.separator);
		String[] tArr = t.split("\\"+File.separator);
		
		String p = "";
		for(int i=0;i<fArr.length;i++){
			if(!StringUtils.hasLength(fArr[i])) continue;
			if(tArr.length>=(i+1)&&fArr[i].equalsIgnoreCase(tArr[i])) continue;
			p += ".."+File.separator;
		}
		for(int i=0;i<tArr.length;i++){
			if(!StringUtils.hasLength(tArr[i])) continue;
			if(fArr.length>=(i+1)&&fArr[i].equalsIgnoreCase(tArr[i])) continue;
			
			p = p+tArr[i]+File.separator;
		}
		return p;
	}
	
	public static String getPubFileName(Object obj, Template template) {
		
		if(obj instanceof Document){
			return template.getPrefix()+"-"+((Document)obj).getId()+"."+template.getExt();
		}
		return template.getPrefix()+"."+template.getExt();
	}
	
	public static String getPreviewDir(Object obj) {
		String dir = PublishUtils.getDir(obj);
		dir = CommonUtils.getAppPath("cms") + File.separator + ITemplateService.PREVIEW_FOLDER + dir;
		
		File f = new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		return dir;
	}

	public static String getPublishDir(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
