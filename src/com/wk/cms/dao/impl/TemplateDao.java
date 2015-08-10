package com.wk.cms.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.wk.cms.cfg.SysCfg;
import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.model.File;
import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CallBack;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.FileUtils;
import com.wk.cms.utils.MyBlob;
import com.wk.cms.utils.PageInfo;

@Repository
public class TemplateDao implements ITemplateDao {

	@Autowired
	private HibernateTemplate hibernateTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TemplateDao.class);

	@SuppressWarnings("unchecked")
	@Override
	public PageInfo find(String siteId, PageInfo pageInfo, String query) {

		Criteria c = hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(Template.class)
				.add(Restrictions.eq("site.id", siteId));
		if (StringUtils.hasLength(query)) {
			c.add(Restrictions.or(Restrictions.like("name", query,
					MatchMode.ANYWHERE)));
		}
		long count = (Long) c.setProjection(Projections.rowCount())
				.uniqueResult();
		List<Template> list = c
				.setProjection(null)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				// .setProjection(HibernateUtils.getProjections(Template.class,
				// ShowArea.LIST))
				// .setResultTransformer(Transformers.aliasToBean(Template.class))
				.setFirstResult(pageInfo.getStart())
				.setMaxResults(pageInfo.getLimit()).list();

		return new PageInfo(list, count);
	}

	@Override
	public void save(Template template) throws ServiceException {

		analysizeTemplate2(template);

		if (!StringUtils.hasLength(template.getId())) {
			template.getFile().setId(UUID.randomUUID().toString());
			template.setId(UUID.randomUUID().toString());

			hibernateTemplate.save(template);
		} else {
			hibernateTemplate.update(template);
		}
	}
	
	private void analysizeTemplate2(Template template) throws ServiceException {
		
		boolean isFromRemote = StringUtils.hasLength(template.getRemoteUrl());
		Set<TempFile> tempFiles = template.getTempFiles()==null?new HashSet<TempFile>():template.getTempFiles();
		List<TempFile> siteFiles = new ArrayList<TempFile>();
		boolean hasFetchSiteFiles = false;
		StringBuffer sb = null;
		try {
			String con = CommonUtils.getContent(template.getFile().getContent().getBinaryStream());
			Map<String, String> parseTags = SysCfg.getSupportParseTags();
			String parseTagStr = CommonUtils.join(parseTags.keySet(), "|");
			Matcher m = null;
			while(true){
				sb = new StringBuffer();
				m = Pattern.compile("<("+parseTagStr+")\\s+(?!hasDone=true)([^>]+)>",Pattern.CASE_INSENSITIVE).matcher(con);
				if(!m.find()){
					break;
				}
				String tagName = m.group(1);
				String attrStr = m.group(2);
				
				String attrName = parseTags.get(tagName);
				Matcher attrMatcher = Pattern.compile(attrName+"\\s*=\\s*['|\"]?([^'\"\\s]+)['|\"]?").matcher(attrStr);
				String originVal = attrMatcher.find()?attrMatcher.group(1):"";
				String fileName = originVal.indexOf("/")>=0?originVal.substring(originVal.lastIndexOf("/")+1):originVal;
				
				if(StringUtils.hasLength(originVal)){
					String val = originVal;
					if (!(val.toLowerCase().contains("http://") || val
							.toLowerCase().contains("https://"))
							&& isFromRemote) {
						val = template.getRemoteUrl()
								.substring(
										0,
										template.getRemoteUrl()
												.lastIndexOf("/") + 1)
								+ val;
					}
					
					if(!hasFetchSiteFiles){
						siteFiles = findAllTempFileBySiteId(template.getSite().getId());
						hasFetchSiteFiles = true;
					}
					try {
						TempFile tempFile = CommonUtils.findFromList(siteFiles,new String[]{"file.fileName"},new Object[]{fileName});
						if (tempFile == null) {
							final File tf = new File(UUID.randomUUID().toString(),val);
							tempFile = new TempFile(UUID.randomUUID()
									.toString(), new HashSet<Template>(), tf,template.getSite());
							if("link".equalsIgnoreCase(tagName)){
								LOGGER.debug("找到LINK标签，开始解析CSS文件【"+val+"】中的模板附件~~");
								List<TempFile> cssInnerFiles = CommonUtils.downLoadCssInnerFiles(tf,val,template,siteFiles,new CallBack() {
									@Override
									public void doCallBack(Object[] objs)
											throws ServiceException {
										try {
											tf.setContent(new MyBlob(objs[0].toString()));
										} catch (UnsupportedEncodingException e) {
											throw new ServiceException("回写css文件内容失败！",e);
										}
									}
								});
								for(TempFile ctf : cssInnerFiles){
									tempFiles.add(ctf);
								}
							}
						}
						tempFile.getTemplates().add(template);
						tempFiles.add(tempFile);
					} catch (Exception e) {
						LOGGER.error("导入模板附件失败！URL="+val,e);
					}
				}
				m.appendReplacement(sb, "<"+tagName+" hasDone=true "+attrStr.replaceAll(originVal, fileName)+">");
				m.appendTail(sb);
				
				con = sb.toString();
			}
			template.getFile().setContent(new MyBlob(con));
			template.setTempFiles(tempFiles);
		} catch (Exception e) {
			throw new ServiceException("解析模板失败！template="+template,e);
		} 
	}

	private void analysizeTemplate(Template template) throws ServiceException {
		boolean isFromRemote = StringUtils.hasLength(template.getRemoteUrl());
		Set<TempFile> tempFiles = template.getTempFiles()==null?new HashSet<TempFile>():template.getTempFiles();
		List<TempFile> siteFiles = new ArrayList<TempFile>();
		boolean hasFetchSiteFiles = false;
		try {
			Document document = Jsoup.parse(new String(
					template.getFile()
							.getContent()
							.getBytes(
									0,
									(int) template.getFile().getContent()
											.length()), "UTF-8"));
			Map<String, String> parseTags = SysCfg.getSupportParseTags();
			
			Elements rEs = document.select(CommonUtils.join(parseTags.keySet(), ",")); 
			for (Element re : rEs) {
				boolean hasDone = "true".equalsIgnoreCase(re.attr("hasDone"));
				if (!hasDone) {
					try {
						String attrName = parseTags.get(re.tagName());
						String val = re.attr(attrName);
						if(!StringUtils.hasLength(val)){
							LOGGER.debug("此标签【"+attrName+"】属性值为空！！跳过~~");
							continue;
						}
						
						if (!(val.toLowerCase().contains("http://") || val
								.toLowerCase().contains("https://"))
								&& isFromRemote) {
							val = template.getRemoteUrl()
									.substring(
											0,
											template.getRemoteUrl()
													.lastIndexOf("/") + 1)
									+ val;
						}
						
						if(!hasFetchSiteFiles){
							siteFiles = findAllTempFileBySiteId(template.getSite().getId());
							hasFetchSiteFiles = true;
						}
						String fileName = val.indexOf("/")>=0?val.substring(val.lastIndexOf("/")+1):val;
						TempFile tempFile = CommonUtils.findFromList(siteFiles,new String[]{"file.fileName"},new Object[]{fileName});
						if (tempFile == null) {
							final File tf = new File(UUID.randomUUID().toString(),val);
							tempFile = new TempFile(UUID.randomUUID()
									.toString(), new HashSet<Template>(), tf,template.getSite());
							
							if("link".equalsIgnoreCase(re.tagName())){
								LOGGER.debug("找到LINK标签，开始解析CSS文件【"+val+"】中的模板附件~~");
								List<TempFile> cssInnerFiles = CommonUtils.downLoadCssInnerFiles(tf,val,template,siteFiles,new CallBack() {

									@Override
									public void doCallBack(Object[] objs)
											throws ServiceException {
										try {
											tf.setContent(new MyBlob(objs[0].toString()));
										} catch (UnsupportedEncodingException e) {
											throw new ServiceException("回写css文件内容失败！",e);
										}
									}
								});
								for(TempFile ctf : cssInnerFiles){
									tempFiles.add(ctf);
								}
							}
						}
						
						tempFile.getTemplates().add(template);
						tempFiles.add(tempFile);

						re.attr(attrName, fileName).attr("hasDone",
								"true");
					} catch (ServiceException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}

			}
			template.getFile().setContent(new MyBlob(document.html()));
			template.setTempFiles(tempFiles);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TempFile> findAllTempFileBySiteId(String id) {
		
		return (List<TempFile>) hibernateTemplate.find("from TempFile where site.id=?", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TempFile findBySiteIdAndFileName(String id, String fileName) {

		List<TempFile> tempFiles = (List<TempFile>) hibernateTemplate.find(
				"from TempFile where site.id=? and file.fileName=?", id,
				fileName);
		if (!CommonUtils.isEmpty(tempFiles)) {
			return tempFiles.get(0);
		}
		return null;
	}

	@Override
	public Template findById(String id) {
		return hibernateTemplate.get(Template.class, id);
	}

	@Override
	public void delete(String id) {

		hibernateTemplate.getSessionFactory().getCurrentSession()
				.delete(findById(id));
		removeFromSize(id);
	}

	@SuppressWarnings("unchecked")
	private void removeFromSize(String id) {
		
		List<Site> sites = (List<Site>) hibernateTemplate.find("from Site where tempIds like ?", "%"+id+"%");
		if(!CommonUtils.isEmpty(sites)){
			for(Site s : sites){
				String tids = s.getTempIds();
				String[] idsArr = tids.split(",");
				for(int i=0;i<idsArr.length;i++){
					if(idsArr[i].equals(id)){
						idsArr[i] = null;
					}
				}
				s.setTempIds(CommonUtils.join(idsArr,","));
				hibernateTemplate.update(s);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TempFile> findTempFiles(List<String> tempFileNames, Site site) {
		
		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(TempFile.class)
				.add(Restrictions.eq("site", site))
				.add(Restrictions.in("file.fileName", tempFileNames))
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TempFile> findTempFiles(Site site) {
		return hibernateTemplate.getSessionFactory().getCurrentSession()
				.createCriteria(TempFile.class)
				.add(Restrictions.eq("site", site))
				.list();
	}

	@Override
	public void search2Add(java.io.File tempfolder, Site site,String encode) throws ServiceException {
		
		List<TempFile> siteTempFiles = null;
		boolean hasFetchSiteTempFiles = false;
		if(tempfolder.isDirectory()){
			java.io.File[] children = tempfolder.listFiles();
			for(java.io.File child : children){
				search2Add(child, site,encode);
			}
		}else{
			try {
				String fileName = tempfolder.getName();
				String fileExt = FileUtils.getFileExt(fileName);
				if(fileExt==null||!CommonUtils.in(SysCfg.getSupportTemplateExt(),fileExt)){
					return;
				}
				Document document = Jsoup.parse(tempfolder, encode);
				Elements titleEs = document.select("title");
				Map<String, String> parseTags = SysCfg.getSupportParseTags();
				Elements es = document.select(CommonUtils.join(parseTags.keySet(), ","));
				
				Set<TempFile> tfs = new HashSet<TempFile>();
				Template template = new Template(null,(titleEs.size()>0?titleEs.get(0).text():fileName),"index",fileExt,site);
				for(Element ele : es){
					if("true".equals(ele.attr("hasdone"))){
						continue;
					}
					String attrName = parseTags.get(ele.tagName());
					String attrValue = ele.attr(attrName);
					if(!StringUtils.hasLength(attrValue)){
						ele.attr("hasdone", "true");
						continue;
					}
					
					String eFileFullName = tempfolder.getParent()+java.io.File.separator+attrValue;
					java.io.File eFile = new java.io.File(eFileFullName);
					if(!eFile.exists()){
						ele.attr("hasdone", "true");
						continue;
					}
					String eFileName = eFile.getName();
					if(!hasFetchSiteTempFiles){
						siteTempFiles = findAllTempFileBySiteId(site.getId());
					}
					TempFile tempFile = CommonUtils.findFromList(siteTempFiles,new String[]{"file.fileName"},new Object[]{eFileName});
					if(tempFile==null){
						Set<Template> temps = new HashSet<Template>();temps.add(template);
						
						final File tff = new File(UUID.randomUUID().toString(),eFile);
						tempFile = new TempFile(UUID.randomUUID().toString(), temps, tff, site);
						if("link".equalsIgnoreCase(ele.tagName())){
							List<TempFile> cssInnerFiles = CommonUtils.downLoadCssInnerFiles(tempFile.getFile(),eFileFullName,template,siteTempFiles,new CallBack() {
								@Override
								public void doCallBack(Object[] objs)
										throws ServiceException {
									try {
										tff.setContent(new MyBlob(objs[0].toString()));
									} catch (UnsupportedEncodingException e) {
										throw new ServiceException("回写css文件内容失败！",e);
									}
								}
							},false);
							for(TempFile ctf : cssInnerFiles){
								tfs.add(ctf);
							}
						}
						
					}else{
						tempFile.getTemplates().add(template);
					}
					tfs.add(tempFile);
					
					ele.attr(attrName, eFileName).attr("hasdone", "true");
				}
				byte[] tBytes = document.html().getBytes("UTF-8");
				template.setFile(new File(fileName, tBytes.length, fileExt, new MyBlob(tBytes)));
				template.setTempFiles(tfs);
				save(template);
			} catch (Exception e) {
				throw new ServiceException("解析文件失败！"+tempfolder, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Template findByName(String tName) {
		
		List<Template> templates = (List<Template>) hibernateTemplate.find("from Template where name=?", tName);
		if(CommonUtils.isEmpty(templates)) return null;
		
		return templates.get(0);
	}

}
