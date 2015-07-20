package com.wk.cms.mvc.json;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wk.cms.utils.CommonUtils;

public class BlobJsonSerializer extends JsonSerializer<Blob>  {

	@Override
	public void serialize(Blob value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		
		try {
			
			gen.writeString(CommonUtils.readStringFromIS(value.getBinaryStream(),"UTF-8"));
		} catch (SQLException e) {
			throw new IOException("Blob转换成String失败！！",e);
		}
	}


	
	
}
