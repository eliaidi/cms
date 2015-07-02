package com.wk.cms.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonUtils {

	public static boolean isEmpty(List<?> list) {
		
		if(list==null||list.size()==0){
			return true;
		}
		return false;
	}

	public static <T> Set<T> list2Set(List<T> list) {
		
		if(isEmpty(list)) return null;
		
		Set<T> set = new HashSet<T>();
		for(T t : list){
			set.add(t);
		}
		return set;
	}

	public static boolean isEmpty(String[] siteNames) {

		if(siteNames==null||siteNames.length==0){
			return true;
		}
		return false;
	}

}
