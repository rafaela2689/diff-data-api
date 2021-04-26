package com.waes.diffdata.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();

        config.setInstanceName("hazelcast-instance");

        MapConfig mapConfig = new MapConfig();
        mapConfig.setName("configuration");
        mapConfig.setEvictionConfig(
                new EvictionConfig().setEvictionPolicy(EvictionPolicy.LRU));
        mapConfig.setTimeToLiveSeconds(-1);

        config.addMapConfig(mapConfig);

        return config;
    }
}
