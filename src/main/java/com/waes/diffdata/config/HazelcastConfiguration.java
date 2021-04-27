package com.waes.diffdata.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    public static final String DIFF = "diff";

    @Bean
    public Config getConfig() {
        return new Config()
                .addMapConfig(getMapConfig());
    }

    private MapConfig getMapConfig() {
        return new MapConfig(DIFF)
                .setEvictionConfig(getEvictionConfig())
                .setTimeToLiveSeconds(7200);
    }

    private EvictionConfig getEvictionConfig() {
        return new EvictionConfig()
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setMaxSizePolicy(MaxSizePolicy.PER_NODE);
    }
}
