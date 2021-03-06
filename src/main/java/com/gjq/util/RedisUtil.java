package com.gjq.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/*
 * Redis工具类
 */
@Component
public class RedisUtil {
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	/*
	 * 指定缓存失效时间
	 */
	public boolean expire(String key,long time){
		try {
			if (time>0) {
				//TimeUnit是java.util.concurrent包下面的一个类。，表示给定单元粒度的时间段
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * 获取过期时间
	 * 返回0代表永久有效
	 */
	public Long getExpire(String key){
		 return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}
	/*
	 * 判断key是否存在
	 * true:存在       false:不存在
	 */
	 public boolean hasKey(String key) {

	        try {
	            return redisTemplate.hasKey(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	 /*
	  * 删除缓存
	  */
	 @SuppressWarnings("unchecked")

	    public void del(String... key) {

	        if (key != null && key.length > 0) {
	            if (key.length == 1) {
	                redisTemplate.delete(key[0]);
	            } else {
	                redisTemplate.delete(CollectionUtils.arrayToList(key));
	            }
	        }
	    }
//  String类型
	 /*
	 *普通缓存获取 
	 */
	 public Object get(String key) {
	        return key == null ? null : redisTemplate.opsForValue().get(key);
	    }
	 /**
	     * 普通缓存放入
	     *
	     * @param key   键
	     * @param value 值
	     * @return true成功 false失败
	     */

	    public boolean set(String key, Object value) {

	        try {
	            redisTemplate.opsForValue().set(key, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * 普通缓存放入并设置时间
	     *
	     * @param key   键
	     * @param value 值
	     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	     * @return true成功 false 失败
	     */

	    public boolean set(String key, Object value, long time) {

	        try {
	            if (time > 0) {
	                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	            } else {
	                set(key, value);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /*
	     * 递增
	     */

	    public long incr(String key, long delta) {

	        if (delta < 0) {
	            throw new RuntimeException("递增因子必须大于0");
	        }
	        return redisTemplate.opsForValue().increment(key, delta);

	    }


	    /*
	     * 递减
	     *
	     */

	    public long decr(String key, long delta) {

	        if (delta < 0) {
	            throw new RuntimeException("递减因子必须大于0");
	        }
	        return redisTemplate.opsForValue().increment(key, -delta);

	    }
	//  Map
	    /**
	     * HashGet
	     *
	     * @param key  键 不能为null
	     * @param item 项 不能为null
	     * @return 值
	     */

	    public Object hget(String key, String item) {

	        return redisTemplate.opsForHash().get(key, item);

	    }


	    /**
	     * 获取hashKey对应的所有键值
	     *
	     * @param key 键
	     * @return 对应的多个键值
	     */

	    public Map<?, ?> hmget(String key) {

	        return redisTemplate.opsForHash().entries(key);

	    }


	    /**
	     * HashSet
	     *
	     * @param key 键
	     * @param map 对应多个键值
	     * @return true 成功 false 失败
	     */

	    public boolean hmset(String key, Map<?, ?> map) {

	        try {
	            redisTemplate.opsForHash().putAll(key, map);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * HashSet 并设置时间
	     *
	     * @param key  键
	     * @param map  对应多个键值
	     * @param time 时间(秒)
	     * @return true成功 false失败
	     */

	    public boolean hmset(String key, Map<?, ?> map, long time) {

	        try {
	            redisTemplate.opsForHash().putAll(key, map);
	            if (time > 0) {
	                expire(key, time);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * 向一张hash表中放入数据,如果不存在将创建
	     *
	     * @param key   键
	     * @param item  项
	     * @param value 值
	     * @return true 成功 false失败
	     */

	    public boolean hset(String key, String item, Object value) {

	        try {
	            redisTemplate.opsForHash().put(key, item, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * 向一张hash表中放入数据,如果不存在将创建
	     *
	     * @param key   键
	     * @param item  项
	     * @param value 值
	     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	     * @return true 成功 false失败
	     */

	    public boolean hset(String key, String item, Object value, long time) {

	        try {
	            redisTemplate.opsForHash().put(key, item, value);
	            if (time > 0) {
	                expire(key, time);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * 删除hash表中的值
	     *
	     * @param key  键 不能为null
	     * @param item 项 可以使多个 不能为null
	     */

	    public void hdel(String key, Object... item) {

	        redisTemplate.opsForHash().delete(key, item);

	    }


	    /**
	     * 判断hash表中是否有该项的值
	     *
	     * @param key  键 不能为null
	     * @param item 项 不能为null
	     * @return true 存在 false不存在
	     */

	    public boolean hHasKey(String key, String item) {

	        return redisTemplate.opsForHash().hasKey(key, item);

	    }


	    /**
	     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	     *
	     * @param key  键
	     * @param item 项
	     * @param by   要增加几(大于0)
	     * @return
	     */

	    public double hincr(String key, String item, double by) {

	        return redisTemplate.opsForHash().increment(key, item, by);

	    }


	    /**
	     * hash递减
	     *
	     * @param key  键
	     * @param item 项
	     * @param by   要减少记(小于0)
	     * @return
	     */

	    public double hdecr(String key, String item, double by) {

	        return redisTemplate.opsForHash().increment(key, item, -by);
	    }


	    // ============================set=============================

	    /**
	     * 根据key获取Set中的所有值
	     *
	     * @param key 键
	     * @return
	     */

	    public Set<?> sGet(String key) {

	        try {
	            return redisTemplate.opsForSet().members(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    /**
	     * 根据value从一个set中查询,是否存在
	     *
	     * @param key   键
	     * @param value 值
	     * @return true 存在 false不存在
	     */

	    public boolean sHasKey(String key, Object value) {

	        try {
	            return redisTemplate.opsForSet().isMember(key, value);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /**
	     * 将数据放入set缓存
	     *
	     * @param key    键
	     * @param values 值 可以是多个
	     * @return 成功个数
	     */

	    public long sSet(String key, Object... values) {

	        try {
	            return redisTemplate.opsForSet().add(key, values);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }


	    /**
	     * 将set数据放入缓存
	     *
	     * @param key    键
	     * @param time   时间(秒)
	     * @param values 值 可以是多个
	     * @return 成功个数
	     */

	    public long sSetAndTime(String key, long time, Object... values) {

	        try {
	            Long count = redisTemplate.opsForSet().add(key, values);
	            if (time > 0)
	                expire(key, time);
	            return count;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }


	    /**
	     * 获取set缓存的长度
	     *
	     * @param key 键
	     * @return
	     */

	    public long sGetSetSize(String key) {

	        try {
	            return redisTemplate.opsForSet().size(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }


	    /**
	     * 移除值为value的
	     *
	     * @param key    键
	     * @param values 值 可以是多个
	     * @return 移除的个数
	     */

	    public long setRemove(String key, Object... values) {

	        try {
	            Long count = redisTemplate.opsForSet().remove(key, values);
	            return count;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }

	    // ===============================list=================================


	    /*
	     * 获取list缓存的内容
	     *
	     */

	    public List<?> lGet(String key, long start, long end) {

	        try {
	            return redisTemplate.opsForList().range(key, start, end);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    /*
	     * 获取list缓存的长度
	     */

	    public long lGetListSize(String key) {

	        try {
	            return redisTemplate.opsForList().size(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }

	    /**
	     * 通过索引 获取list中的值
	     *
	     * @param key   键
	     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	     * @return
	     */

	    public Object lGetIndex(String key, long index) {
	        try {
	            return redisTemplate.opsForList().index(key, index);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    /*
	     * 将list放入缓存（从右插入）
	     */

	    public boolean lSet(String key, Object value) {
	        try {
	            redisTemplate.opsForList().rightPush(key, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /*
	     * 将list放入缓存（从左插入）
	     */

	    public boolean lSetLeft(String key, Object value) {
	        try {
	            redisTemplate.opsForList().leftPush(key, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /*
	     * 若当时从右插入则将list作为队列，先进先出，踢出最先进入的元素
	     * 若当时从左插入则将list作为栈，先进后出，踢出最后进入的元素
	     */
	    public boolean lLeftPop(String key) {
	        try {
	            redisTemplate.opsForList().leftPop(key);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /**
	     * 若当时从右插入则将list作为栈，先进后出，踢出最后进入的元素
	     * 若当时从左插入则将list作为队列，先进先出，踢出最先进入的元素
	     *
	     * @param key
	     * @return
	     */
	    public boolean lRightPop(String key) {
	        try {
	            redisTemplate.opsForList().rightPop(key);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /**
	     * 将list放入缓存（从右插入）
	     *
	     * @param key   键
	     * @param value 值
	     * @param time  时间(秒)
	     * @return
	     */

	    public boolean lSet(String key, Object value, long time) {
	        try {
	            redisTemplate.opsForList().rightPush(key, value);
	            if (time > 0)
	                expire(key, time);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /**
	     * 将list放入缓存（从左插入）
	     *
	     * @param key   键
	     * @param value 值
	     * @param time  时间(秒)
	     * @return
	     */

	    public boolean lSetLeft(String key, Object value, long time) {
	        try {
	            redisTemplate.opsForList().leftPush(key, value);
	            if (time > 0)
	                expire(key, time);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /**
	     * 将list放入缓存（整体）
	     *
	     * @param key   键
	     * @param value 值
	     * @return
	     */

	    public boolean lSetall(String key, List<?> value) {
	        try {
	            redisTemplate.opsForList().rightPushAll(key, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    /**
	     * 将list放入缓存（整体）
	     *
	     * @param key   键
	     * @param value 值
	     * @param time  时间(秒)
	     * @return
	     */

	    public boolean lSetall(String key, List<?> value, long time) {
	        try {
	            redisTemplate.opsForList().rightPushAll(key, value);
	            if (time > 0)
	                expire(key, time);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /*
	     * 根据索引修改list中的某条数据
	     *
	     */

	    public boolean lUpdateIndex(String key, long index, Object value) {
	        try {
	            redisTemplate.opsForList().set(key, index, value);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }


	    /*
	     * 移除N个值为value
	     */

	    public long lRemove(String key, long count, Object value) {
	        try {
	            Long remove = redisTemplate.opsForList().remove(key, count, value);
	            return remove;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }

	    // =============================Sortset(Zset)============================

	    /*
	     * 获取下标从start到end的值，按权值从小到大排序
	     *
	     */
	    public Set<?> range(String key, long start, long end){
	        try {
	            return redisTemplate.opsForZSet().range(key,start,end);
	        }catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    /*
	     * 获取下标从start到end的值，按权值从大到小排序	    
	     */
	    public Set<?> reverseRange(String key,long start,long end){
	        try {
	            return redisTemplate.opsForZSet().reverseRange(key,start,end);
	        }catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    /*
	     * 获取权值从start到end的值，按权值从小到大排序
	     */
	    public Set<?> rangebyscores(String key, long start, long end){
	        try {
	            return redisTemplate.opsForZSet().rangeByScore(key,start,end);
	        }catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    /*
	     * 获取权值从start到end的值，按权值从大到小排序
	     */
	    public Set<?> reverseRangebyscores(String key,long start,long end){
	        try {
	            return redisTemplate.opsForZSet().reverseRangeByScore(key,start,end);
	        }catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    /*
	     * 将ZSet数据放入缓存
	     */
	    public boolean zsSet(String key, Object value, double scores){
	        try {
	            redisTemplate.opsForZSet().add(key,value,scores);
	            return true;
	        }catch (Exception e) {
	            return false;
	        }
	    }

		    
}
