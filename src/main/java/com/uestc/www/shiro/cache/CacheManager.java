package com.uestc.www.shiro.cache;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.ehcache.EhCacheManager;

public class CacheManager<K, V> implements Cache<K, V> {

	private EhCacheManager cacheManager;
	private Cache<K, V> cache = null;

	public Cache<K, V> getCache() {
		try {
			if (cache == null) {
				cache = cacheManager.getCache("shiro_cache");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return cache;
	}

	@Override
	public void clear() throws CacheException {
		// TODO Auto-generated method stub
		getCache().clear();
	}

	@Override
	public V get(K arg0) throws CacheException {
		// TODO Auto-generated method stub
		return getCache().get(arg0);
	}

	@Override
	public Set<K> keys() {
		// TODO Auto-generated method stub
		return getCache().keys();
	}

	@Override
	public V put(K arg0, V arg1) throws CacheException {
		// TODO Auto-generated method stub
		return getCache().put(arg0, arg1);
	}

	@Override
	public V remove(K arg0) throws CacheException {
		// TODO Auto-generated method stub
		return getCache().remove(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return getCache().size();
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return getCache().values();
	}

	public EhCacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(EhCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setCache(Cache<K, V> cache) {
		this.cache = cache;
	}

}
