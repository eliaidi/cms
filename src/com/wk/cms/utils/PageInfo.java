package com.wk.cms.utils;

import java.util.List;

public class PageInfo {

	private Integer page;
	private Integer start = new Integer(1);
	private Integer limit = new Integer(20);
	private List<?> list;
	private Long totalCount;
	
	public PageInfo(){
		
	}
	public PageInfo(List<?> list, Long count) {
		this.list = list;
		this.totalCount = count;
	}
	
	
	public PageInfo(Integer start, Integer limit, List<?> list, Long totalCount) {
		super();
		this.start = start;
		this.limit = limit;
		this.list = list;
		this.totalCount = totalCount;
	}
	public PageInfo(int start, int limit) {
		this.start = start;
		this.limit = limit;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	
	
}
