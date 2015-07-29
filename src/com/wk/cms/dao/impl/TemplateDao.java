package com.wk.cms.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import com.wk.cms.dao.ITemplateDao;
import com.wk.cms.model.File;
import com.wk.cms.model.Site;
import com.wk.cms.model.TempFile;
import com.wk.cms.model.Template;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
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

		analysizeTemplate(template);

		if (!StringUtils.hasLength(template.getId())) {
			template.getFile().setId(UUID.randomUUID().toString());
			template.setId(UUID.randomUUID().toString());

			hibernateTemplate.save(template);
		} else {
			hibernateTemplate.update(template);
		}
	}

	private void analysizeTemplate(Template template) throws ServiceException {
		boolean isFromRemote = StringUtils.hasLength(template.getRemoteUrl());
		Set<TempFile> tempFiles = new HashSet<TempFile>();
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
			Elements rEs = document.select("script,link,img");
			for (Element re : rEs) {
				boolean hasDone = "true".equalsIgnoreCase(re.attr("hasDone"));
				if (!hasDone) {
					try {
						String attrName = CommonUtils
								.getRemoteAttrNameByTagName(re.tagName());
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
							File tf = new File(UUID.randomUUID().toString(),val);
							tempFile = new TempFile(UUID.randomUUID()
									.toString(), new HashSet<Template>(), tf,template.getSite());
							
							if("link".equalsIgnoreCase(re.tagName())){
								LOGGER.debug("找到LINK标签，开始解析CSS文件【"+val+"】中的模板附件~~");
								List<TempFile> cssInnerFiles = CommonUtils.downLoadCssInnerFiles(tf,val,template,siteFiles);
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

}
