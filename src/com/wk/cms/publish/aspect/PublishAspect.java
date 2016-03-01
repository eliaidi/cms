package com.wk.cms.publish.aspect;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.PubLog;
import com.wk.cms.model.Site;
import com.wk.cms.service.IPubLogService;
import com.wk.cms.utils.BeanFactory;
import com.wk.cms.utils.CommonUtils;

@Component
@Aspect
public class PublishAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(PublishAspect.class);
	
	@Pointcut("execution(  * com.wk.cms.publish.server.PublishServer.publish*(..) ) ")
	private void t(){}
	
	@Around("t()")
	public Object record(ProceedingJoinPoint jp) throws Throwable{
		
		Object[] args = jp.getArgs();
		if(Boolean.TRUE.equals(args[1])){
			return jp.proceed();
		}
		
		Throwable ex = null;
		Object result = null;
		PubLog log = doBefore(jp);
		
		try {
			result = jp.proceed();
		} catch (Throwable e) {
			ex = e;
		}finally{
			doAfter(log,result,ex);
		}
		return result;
	}

	private void doAfter(PubLog log, Object result, Throwable ex) {
		
		log.setEndTime(new Date());
		log.setSuccess(ex==null);
		log.setException(ex==null?null:ex.getClass()+"::"+ex.getMessage());
		log.setDescr("对象【"+log.getObj()+"，ID:"+log.getId()+"】（类型【"+log.getObjType()+"】）发布"+(log.getSuccess()?"成功":"失败")+"，总耗时"+(log.getEndTime().getTime() - log.getStartTime().getTime())/1000+"秒。"+(log.getSuccess()?"":log.getException()));
		
		LOGGER.debug("对象【"+log.getObj()+"】发布完成~~");
		LOGGER.debug(log.getDescr());
		
		IPubLogService pubLogService = BeanFactory.getBean(IPubLogService.class);
		pubLogService.noTransSave(log);
	}

	private PubLog doBefore(ProceedingJoinPoint jp) {
		Object[] args = jp.getArgs();
		LOGGER.debug("开始发布，发布对象【"+args[0]+"】，发布类型【"+args[1]+"】");
		LOGGER.debug("当前线程：："+Thread.currentThread());
		PubLog log = new PubLog();
		Object pubObj = args[0];
		log.setObj(pubObj.toString());
		log.setObjId(getObjId(pubObj));
		log.setObjType(getObjType(pubObj));
		log.setStartTime(new Date());
		log.setPubUser(SecurityUtils.getSubject().getPrincipal().toString());
		
		IPubLogService pubLogService = BeanFactory.getBean(IPubLogService.class);
		return pubLogService.noTransSave(log);
	}

	private String getObjType(Object pubObj) {
		
		if(List.class.isAssignableFrom(pubObj.getClass())){
			List<Object> objects = (List<Object>) pubObj;
			if(CommonUtils.isEmpty(objects)){
				return null;
			}
			return getObjType(objects.get(0));
		}
		
		if(pubObj instanceof Document){
			return "文档";
		}else if(pubObj instanceof Channel){
			return "栏目";
		}else if(pubObj instanceof Site ){
			return "站点";
		}
		return null;
	}

	private String getObjId(Object pubObj) {
		
		if(List.class.isAssignableFrom(pubObj.getClass())){
			StringBuilder sb = new StringBuilder();
			List<Object> objects = (List<Object>) pubObj;
			for(Object o : objects){
				sb.append(","+getObjId(o));
			}
			if(sb.length()<=0){
				return null;
			}
			return sb.delete(0, 1).toString();
		}
		if(pubObj instanceof Document){
			return ((Document)pubObj).getId();
		}else if(pubObj instanceof Channel){
			return ((Channel)pubObj).getId();
		}else if(pubObj instanceof Site ){
			return ((Site)pubObj
					).getId();
		}
		return null;
	}
}
