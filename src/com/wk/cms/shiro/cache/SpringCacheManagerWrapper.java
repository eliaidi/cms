package com.wk.cms.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

public class SpringCacheManagerWrapper implements CacheManager {

	public class MyShiroCache implements Cache {

		private final org.springframework.cache.Cache cache;

		public MyShiroCache(org.springframework.cache.Cache cache2) {
			this.cache = cache2;
		}

		@Override
		public Object get(Object key) throws CacheException {
			
			Object val = cache.get(key);
			
			if(val instanceof SimpleValueWrapper){
				return ((SimpleValueWrapper)val).get();
			}
			return val;
		}

		@Override
		public Object put(Object key, Object value) throws CacheException {
			
			Object preObj = cache.get(key);
			cache.put(key, value);
			return preObj;
		}

		@Override
		public Object remove(Object key) throws CacheException {
			Object preObj = cache.get(key);
			cache.evict(key);
			return preObj;
		}

		@Override
		public void clear() throws CacheException {
			cache.clear();
		}

		@Override
		public int size() {
			
			if(cache.getNativeCache() instanceof net.sf.ehcache.Cache){
				return ((net.sf.ehcache.Cache)cache).getSize();
			}
			return 0;
		}

		@Override
		public Set keys() {
			if(cache.getNativeCache() instanceof net.sf.ehcache.Cache){
				
				return new HashSet(((net.sf.ehcache.Cache)cache.getNativeCache()).getKeys());
			}
			return null;
		}

		@Override
		public Collection values() {
			
			List<Object > vs = new ArrayList<Object>();
			Set<Object> ks = this.keys();
			
			if(ks==null) return null;
			for(Object k : ks){
				Object val = get(k);
				vs.add(val);
			}
			return vs;
		}

	}

	private org.springframework.cache.CacheManager cacheManager;
	
	public org.springframework.cache.CacheManager getCacheManager() {
		return cacheManager;
	}
	
	public void setCacheManager(
			org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new MyShiroCache(cacheManager.getCache(name));
	}
}
