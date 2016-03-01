package com.wk.cms.publish;

import java.util.List;

import com.wk.cms.model.Document;
import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.type.PublishType;

public interface IPublishServer {

	String TEMPFILE_FOLDER = "images";

	public String publish(Object obj,boolean isPreview, PublishType type) throws PublishException;

	public <T> void publishMulti(List<T> documents, boolean b,
			PublishType index);

}
