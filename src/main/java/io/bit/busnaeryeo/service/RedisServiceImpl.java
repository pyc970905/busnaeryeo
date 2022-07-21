package io.bit.busnaeryeo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 키-벨류 설정
    public void setValues(String username, String token){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
//        values.set(name, age);
        values.set(username, token, Duration.ofMinutes(30));  // 30분 뒤 메모리에서 삭제된다.
    }

    // 키값으로 벨류 가져오기
    public String getValues(String username){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(username);
    }

    // 키-벨류 삭제
    public void delValues(String username) {
        redisTemplate.delete(username);
    }

    public void setValuesWithExp(String name, String token, Long time){

        redisTemplate.opsForValue().set(name,token,time, TimeUnit.MILLISECONDS);
    }
}
