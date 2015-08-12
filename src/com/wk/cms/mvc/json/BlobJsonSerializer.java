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

	private String encode = "UTF-8";
	
	public BlobJsonSerializer(){}
	public BlobJsonSerializer(String encode2) {
		this.encode = encode2;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	@Override
	public void serialize(Blob value, JsonGenerator gen,
			SerializerProvider serializers) throws IOException,
			JsonProcessingException {
		
		try {
			gen.writeString(format(value));
		} catch (SQLException e) {
			throw new IOException("Blob转换成String失败！！",e);
		}
		
	}
	public String format(Blob s) throws IOException, SQLException {
		return CommonUtils.readStringFromIS(s.getBinaryStream(),encode);
	}
	
	
}
