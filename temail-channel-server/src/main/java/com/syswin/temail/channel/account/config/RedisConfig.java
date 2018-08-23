package com.syswin.temail.channel.account.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Created by juaihua on 2018/8/13. redis config
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  private static final byte[] EMPTY_ARRAY = new byte[0];

  private static final Charset REDIS_CHARSER = Charset.forName("utf-8");

  private static final Logger LOGGER = LoggerFactory.getLogger(CachingConfigurerSupport.class);

  @Autowired
  private RedisConnectionFactory redisConnectionFactory;


  /**
   * default key generate method
   */
  @Bean
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {
      @Override
      public Object generate(Object target, Method method, Object... params) {
        StringBuffer sb = new StringBuffer();
        sb.append(target.getClass().getName());
        sb.append(method.getName());
        for (Object obj : params) {
          sb.append(obj.toString());
        }
        return sb.toString();
      }
    };
  }


  @Override
  public CacheManager cacheManager() {
    RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(redisConnectionFactory);
    @SuppressWarnings("serial")
    Set<String> cacheNames = new HashSet<String>() {
      {
        add("channelConnectionCache");
      }
    };
    builder.initialCacheNames(cacheNames);
    return builder.build();
  }


  /**
   * serializer and deserializer for redis
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(redisConnectionFactory);

    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    template.setValueSerializer(jackson2JsonRedisSerializer);
    template.setHashValueSerializer(jackson2JsonRedisSerializer);
    RedisSerializer<?> stringSerializer = new StringRedisSerializer();

    template.setKeySerializer(stringSerializer);
    template.setHashKeySerializer(stringSerializer);
    template.afterPropertiesSet();

    return template;
  }


    /*@Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory ){
        RedisTemplate<String,Object> template = new RedisTemplate<String,Object>();
        template.setConnectionFactory(redisConnectionFactory);
        RedisSerializer<Object> objectRedisSerializer = new RedisSerializer<Object>() {
            @Override
            public byte[] serialize(@Nullable Object o) throws SerializationException {
                 try {
                     if( o == null){
                         return EMPTY_ARRAY;
                     }
                     return new Gson().toJson(o).getBytes(REDIS_CHARSER);
                 } catch (Exception e) {
                     LOGGER.error("redis serialize fail ", e);
                     throw new SerializationException(e.getMessage());
                 }
            }

            @Override
            public Object deserialize(@Nullable byte[] bytes) throws SerializationException {
                try {
                    if(bytes == null || bytes.length == 0){
                        return null;
                    }
                    return new Gson().fromJson(new String(bytes,REDIS_CHARSER),Object.class);
                } catch (Exception e) {
                    LOGGER.error("redis deserialize fail ", e);
                    throw new SerializationException(e.getMessage());
                }
            }
        };

        template.setValueSerializer(objectRedisSerializer);
        template.setHashValueSerializer(objectRedisSerializer);

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        template.afterPropertiesSet();
        return template;
    }*/


}
