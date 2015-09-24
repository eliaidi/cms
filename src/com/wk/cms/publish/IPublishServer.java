package com.wk.cms.publish;

import com.wk.cms.publish.exceptions.PublishException;
import com.wk.cms.publish.type.PublishType;

public interface IPublishServer {

	String TEMPFILE_FOLDER = "images";

	public String publish(Object obj,boolean isPreview, PublishType type) throws PublishException;

}
