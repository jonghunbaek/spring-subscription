package com.example.subscription.repo.redis;

import com.example.subscription.entity.redis.PassCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassCacheRepository extends CrudRepository<PassCache, Long> {

}
