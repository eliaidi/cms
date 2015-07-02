package com.wk.cms.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.internal.BinaryStreamImpl;

public class MyBlob implements Blob {

	private byte[] bytes ;
	
	public MyBlob(){
		
	}
	public MyBlob(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	@Override
	public long length() throws SQLException {
		
		return bytes.length;
	}

	@Override
	public byte[] getBytes(long pos, int length) throws SQLException {
		
		if(pos<0||length<=0){
			throw new SQLException("长度错误！");
		}
		byte[] newByte = new byte[length];
		
		int newIndex = 0;
		for(int i=0;i<this.bytes.length&&newIndex<length;i++){
			if(i<pos){
				continue;
			}
			newByte[newIndex++] = this.bytes[i];
		}
		return newByte;
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setBytes(long pos, byte[] bytes) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setBytes(long pos, byte[] bytes, int offset, int len)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void truncate(long len) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void free() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getBinaryStream(long pos, long length)
			throws SQLException {
		return new BinaryStreamImpl(getBytes(pos, (int)length));
	}

}
