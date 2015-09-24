package com.wk.cms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.wk.cms.dao.IDocumentDao;
import com.wk.cms.exception.ParseException;
import com.wk.cms.model.Channel;
import com.wk.cms.model.Document;
import com.wk.cms.model.Field.Type;
import com.wk.cms.model.FieldValue;
import com.wk.cms.model.Site;
import com.wk.cms.publish.IPublishServer;
import com.wk.cms.publish.type.PublishType;
import com.wk.cms.service.IAppendixService;
import com.wk.cms.service.IChannelService;
import com.wk.cms.service.IDocumentService;
import com.wk.cms.service.exception.ServiceException;
import com.wk.cms.utils.CommonUtils;
import com.wk.cms.utils.PageInfo;

@Service
@Lazy
public class DocumentService implements IDocumentService {

	@Autowired
	private IDocumentDao documentDao;

	@Autowired
	private IChannelService channelService;

	@Autowired
	private IAppendixService appendixService;
	
	@Autowired
	private IPublishServer publishServer;

	@Override
	public PageInfo find(String channelId, PageInfo pageInfo, String query)
			throws ServiceException {

		return documentDao.find(channelId, pageInfo, query);
	}

	@Override
	public void save(Document document, String channelId)
			throws ServiceException {

		if (!StringUtils.hasLength(document.getId())) {

			if (!StringUtils.hasLength(channelId)) {
				throw new ServiceException("参数错误！docId和channelId至少穿入一个");
			}

			Channel channel = channelService.findById(channelId);
			if (channel == null) {
				throw new ServiceException("参数错误！channelId为【" + channelId
						+ "】的栏目不存在");
			}
			document.setStatus(1);
			document.setChannel(channel);
			document.setSite(channel.getSite());
			document.setCrTime(new Date());
			document.setCrUser(null);
			document.setSort(documentDao.findMaxSortOf(channel) + 1);
			if (!CommonUtils.isEmpty(document.getFieldValues())) {
				for (FieldValue fv : document.getFieldValues()) {
					fv.setId(UUID.randomUUID().toString());
					fv.setDocument(document);
				}
			}

			documentDao.save(document);
		} else {

			Document persistDoc = findById(document.getId());
			if (persistDoc == null) {
				throw new ServiceException("未找到id为【" + document.getId()
						+ "】的文档！");
			}
			removeFieldsFrom(persistDoc);

			BeanUtils.copyProperties(document, persistDoc, new String[] { "id",
					"channel", "site", "crUser", "crTime","sort" });
			for (FieldValue fv : document.getFieldValues()) {
				fv.setId(UUID.randomUUID().toString());
				fv.setDocument(persistDoc);
			}

			persistDoc.setStatus(2);
			documentDao.save(persistDoc);
		}

	}

	@Override
	public void removeFieldsFrom(Document persistDoc) {
		documentDao.removeFields(persistDoc);
	}

	@Override
	public Document findById(String id) throws ServiceException {

		Document document = documentDao.findById(id);
		if (document == null) {
			throw new ServiceException("未找到ID为【" + id + "】的文档对象！！！");
		}
		return document;
	}

	@Override
	public void deleteById(String id) throws ServiceException {

		documentDao.deleteById(id);
	}

	@Override
	public void deleteByIds(String ids) throws ServiceException {

		String[] idArr = ids.split(",");
		for (String id : idArr) {
			deleteById(id);
		}
	}

	@Override
	public List<Document> findByIds(String ids) throws ServiceException {

		return documentDao.findByIds(ids.split(","));
	}

	@Override
	public Document loadRemoteDoc(String url) throws ParseException,
			ServiceException {

		return CommonUtils.loadRemoteDoc(url);

	}

	@Override
	public void copy(Channel channel, Channel newChannel)
			throws ServiceException {

		List<Document> documents = findAll(channel);

		if (!CommonUtils.isEmpty(documents)) {
			for (Document document : documents) {
				copy(document, newChannel);
			}
		}
	}

	@Override
	public void copy(Document document, Channel newChannel)
			throws ServiceException {

		// 拷贝本文档
		Document newDoc = new Document();
		BeanUtils.copyProperties(document, newDoc, new String[] { "id",
				"crTime", "crUser", "appendixs","fieldValues" });
		newDoc.setStatus(1);
		save(newDoc, newChannel);

		// 拷贝本文档下所有的附件
		appendixService.copy(document, newDoc);
	}

	@Override
	public void save(Document newDoc, Channel newChannel) {

		newDoc.setChannel(newChannel);
		newDoc.setSite(newChannel.getSite());
		newDoc.setCrTime(new Date());
		newDoc.setCrUser(null);
		newDoc.setSort(documentDao.findMaxSortOf(newChannel) + 1);
		documentDao.save(newDoc);
	}

	@Override
	public List<Document> findAll(Channel channel) {
		return documentDao.findAll(channel, MAX_FETCH_SIZE,
				DetachedCriteria.forClass(Document.class));
	}

	@Override
	public void refreshBy(Channel channel) {

		documentDao.refresh(channel);
	}

	@Override
	public void copy(String[] objIds, String channelId) throws ServiceException {

		List<Document> documents = findByIds(objIds);
		Channel channel = channelService.findById(channelId);
		if (channel == null) {
			throw new ServiceException("未找到ID为【" + channelId + "】的栏目！！");
		}
		if (!CommonUtils.isEmpty(documents)) {
			for (Document document : documents) {
				copy(document, channel);
			}
		}
	}

	@Override
	public List<Document> findByIds(String[] objIds) {
		return documentDao.findByIds(objIds);
	}

	@Override
	public void cut(String[] objIds, String channelId) throws ServiceException {
		List<Document> documents = findByIds(objIds);
		Channel channel = channelService.findById(channelId);
		if (channel == null) {
			throw new ServiceException("未找到ID为【" + channelId + "】的栏目！！");
		}
		if (!CommonUtils.isEmpty(documents)) {
			for (Document document : documents) {
				cut(document, channel);
			}
		}
	}

	@Override
	public void cut(Document document, Channel channel) {

		document.setChannel(channel);
		document.setSite(channel.getSite());
		document.setSort(documentDao.findMaxSortOf(channel));
		documentDao.save(document);
	}

	@Override
	public List<Document> findCanPub(Channel currChnl, int pageSize,
			String where, String order, Object[] params) {
		String hql = " from Document where channel.id='" + currChnl.getId()
				+ "'  ";

		if (StringUtils.hasLength(currChnl.getSite().getCanPubSta())) {
			hql += " and status in (" + currChnl.getSite().getCanPubSta() + ")";
		} else {
			hql += " and status <> 5";
		}
		if (StringUtils.hasLength(where)) {
			hql += " and " + where;
		}
		if (StringUtils.hasLength(order)) {
			hql += " order by " + order;
		}
		return documentDao.find(hql, params, new PageInfo(1, pageSize, null,
				null));
	}

	@Override
	public PageInfo findByMap(Channel currChnl, Map<String, String> params) {

		return documentDao.findByMap(currChnl, params);
	}

	@Override
	public void move(String currId, String targetId) throws ServiceException {

		// documentDao.move(currId,targetId);
		Document currDoc = findById(currId);
		Document targetDoc = findById(targetId);
		documentDao.move(currDoc, targetDoc);
	}

	@Override
	public Object getDocProperty(Document doc,String field,Integer index) throws ServiceException {
		
		Object val = null;
		try {
			val = CommonUtils.getDeepFieldValue(doc, field);
		} catch (ServiceException e) {
			
			if(e.getCause() instanceof NoSuchMethodException){
				List<FieldValue> fvs = doc.getFieldValues();
				String[] fields = field.split("\\.");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(fields.length==1){
					for(FieldValue fv : fvs ){
						if(!fv.getExtField().getField().isCustom()&&fv.getExtField().getField().getName().equalsIgnoreCase(fields[0])){
							
							if(fv.getValue()!=null){
								if(Type.INT.equals(fv.getField().getType())){
									val = Integer.parseInt(fv.getValue());
								}else if(Type.FLOAT.equals(fv.getField().getType())){
									val = Float.parseFloat(fv.getValue());
								}else if(Type.DATE.equals(fv.getField().getType())){
									try {
										val = sdf.parse(fv.getValue());
									} catch (java.text.ParseException e1) {
										throw new ServiceException(e.getMessage(),e);
									}
								}else{
									val = fv.getValue();
								}
							}
							
						}
					}
				}else if(fields.length==2){
					List<Object> vl = null;
					if(index==null){
						vl= new ArrayList<Object>();
					}
					for(FieldValue fv : fvs ){
						if(fv.getExtField().getField().isCustom()&&fv.getExtField().getField().getName().equalsIgnoreCase(fields[0])&&fv.getField().getName().equalsIgnoreCase(fields[1])){
							Object v = null;
							if(fv.getValue()!=null){
								if(Type.INT.equals(fv.getField().getType())){
									v = Integer.parseInt(fv.getValue());
								}else if(Type.FLOAT.equals(fv.getField().getType())){
									v = Float.parseFloat(fv.getValue());
								}else if(Type.DATE.equals(fv.getField().getType())){
									try {
										v = sdf.parse(fv.getValue());
									} catch (java.text.ParseException e1) {
										throw new ServiceException(e.getMessage(),e);
									}
								}else{
									v = fv.getValue();
								}
								
								if(index==null){
									vl.add(v);
								}else{
									if(index==fv.getGroup()){
										val = v;
									}
								}
							}
						}
					}
					
					if(index==null){
						val = vl;
					}
				}
				
			}else{
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return val;
	}

	@Override
	public PageInfo findRollDocuments(Site site,Map<String, String> params) throws ServiceException {
		String cNames = params.get("channels");
		String pName = params.get("parent");
		
		
		if(StringUtils.hasLength(cNames)){
			
			return documentDao.findByChnlNames(site,params);
			
		}else if(StringUtils.hasLength(pName)){
			String level = params.get("level");
			List<Channel> channels = channelService.findSubChannels(site,pName,level);
			
			return documentDao.findByChannels(site,channels,params);
		}
		throw new ServiceException("channels属性和parent属性至少需要一个！");
	}

	@Override
	public String preview(String id) throws  ServiceException {
		return publishServer.publish(findById(id), true, PublishType.INDEX);
	}

	@Override
	public void changeStatus(Document obj, Integer status) {
		documentDao.changeStatus(obj,status);
	}

}
