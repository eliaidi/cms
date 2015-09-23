package com.wk.cms.publish.parser.impl;

import static  com.wk.cms.utils.PublishUtils.*;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Site;
import com.wk.cms.parser.HtmlTag;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.parser.AbstractTagParser;
import com.wk.cms.service.exception.ServiceException;

@Component("NavComp")
public class NavParser extends AbstractTagParser {

	@Override
	protected String parseInternal(Object obj) throws ServiceException {
		
		HtmlTag e = ctx.getTag();
		String target = e.attr("target");
		String extra = e.attr("extra");
		if(extra==null){
			extra = "";
		}
		if(!StringUtils.hasLength(target)){
			target = "_blank";
		}
		if(obj instanceof Site){
			Site s = (Site) obj;
			String url = getPath2Path(getDir(ctx.getBase()), getDir(s));
			return "<a href='"+url+"' target='"+target+"' "+extra+">"+s.getDescr()+"</a>";
		}else{
			
			String sp = e.attr("sp");
			if(!StringUtils.hasLength(sp)){
				sp = "&gt;";
			}
			Channel currChnl = null;
			if(obj instanceof Channel){
				currChnl = (Channel) obj;
			}else if(obj instanceof Document){
				currChnl = ((Document)obj).getChannel();
			}else{
				throw new PublishException("上下文环境错误！");
			}
			
			Channel loop = currChnl;
			String s = "";
			while(loop!=null){
				
				s =  "<a "+extra+" href='"+getPath2Path(getDir(ctx.getBase()), getDir(loop))+"' target='"+target+"'>"+loop.getDescr()+"</a>" +sp +s;
				loop = loop.getParent();
			}
			if(!"".equals(s)){
				s = s.substring(0, s.length()-sp.length());
			}
			s = "<a "+extra+" href='"+getPath2Path(getDir(ctx.getBase()), getDir(currChnl.getSite()))+"' target='"+target+"'>"+currChnl.getSite().getDescr()+"</a>"+sp+s;
			return s;
		}
	}

}
