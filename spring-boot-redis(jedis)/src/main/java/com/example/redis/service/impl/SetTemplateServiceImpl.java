package com.example.redis.service.impl;

import com.example.redis.service.SetTemplateService;
import com.example.util.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set 数据类型 -相关操作
 *
 * @author mengqiang
 */
@Component
public class SetTemplateServiceImpl extends BaseTemplateImpl implements SetTemplateService {

	/**
	 * 向集合添加元素
	 * 被添加到集合中的新元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	@Override
	public Long sAdd(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.sadd(key.getBytes(), JsonSerializer.serialize(value));
		});
	}


	/**
	 * 移除集合中元素
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 */
	@Override
	public Long sRem(String key, Object value) {
		validateParam(key, value);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.srem(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 获取集合的成员数
	 * 被成功移除的元素的数量，不包括被忽略的元素。
	 *
	 * @param key
	 * @return 集合的数量。 当集合 key 不存在时，返回 0 。
	 */
	@Override
	public Long sCard(String key) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.scard(key.getBytes());
		});
	}

	/**
	 * 判断 value 元素是否是集合 key 的成员
	 * 如果成员元素是集合的成员，返回 1 。 如果成员元素不是集合的成员，或 key 不存在，返回 0 。
	 *
	 * @param key
	 */
	@Override
	public Boolean sisMember(String key, Object value) {
		validateKeyParam(key);
		return redisClient.invoke(jedisPool, jedis -> {
			return jedis.sismember(key.getBytes(), JsonSerializer.serialize(value));
		});
	}

	/**
	 * 返回集合中的所有成员
	 *
	 * @param key
	 */
	@Override
	public <T> Set<T> sMembers(String key, Class<T> clazz) {
		validateKeyParam(key);
		Set<T> result = redisClient.invoke(jedisPool, jedis -> {
			Set<byte[]> setResult = jedis.smembers(key.getBytes());
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (byte[] bytes : setResult) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				set.add(t);
			}
			return set;
		});
		return result;
	}

	/**
	 * 返回集合随机count个值
	 *
	 * @param key
	 */
	@Override
	public <T> Set<T> sRandMember(String key, Class<T> clazz, int count) {
		validateKeyParam(key);
		Set<T> result = redisClient.invoke(jedisPool, jedis -> {
			List<byte[]> setResult = jedis.srandmember(key.getBytes(), count);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (byte[] bytes : setResult) {
				T t = JsonSerializer.deserialize(bytes, clazz);
				set.add(t);
			}
			return set;
		});
		return result;
	}


	/**
	 * 返回给定集合的交集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sinter(Set<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sinter(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 返回给定集合的并集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sunion(Set<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sunion(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 返回给定集合的差集
	 *
	 * @param keys
	 */
	@Override
	public <T> Set<T> sDiff(List<String> keys, Class<T> clazz) {
		if (CollectionUtils.isEmpty(keys)) {
			return new HashSet<T>(ZERO);
		}
		String[] strKeys = new String[keys.size()];
		keys.toArray(strKeys);
		return redisClient.invoke(jedisPool, jedis -> {
			Set<String> setResult = jedis.sdiff(strKeys);
			if (CollectionUtils.isEmpty(setResult)) {
				return new HashSet<T>(ZERO);
			}
			Set<T> set = new HashSet<>(setResult.size());
			for (String str : setResult) {
				T t = JsonSerializer.deserialize(str.getBytes(), clazz);
				set.add(t);
			}
			return set;
		});
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 *
	 * @param key
	 */
	@Override
	public <T> T sPop(String key, Class<T> clazz) {
		validateKeyParam(key);
		byte[] value = redisClient.invoke(jedisPool, (jedis) -> jedis.spop(key.getBytes()));
		if (value != null) {
			return JsonSerializer.deserialize(value, clazz);
		}
		return null;
	}

}
