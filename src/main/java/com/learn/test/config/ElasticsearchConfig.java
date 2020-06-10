package com.elastic_demo.alpha.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ElasticsearchConfig {

    @Value("${es.hostList}")
    private String hostList;

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(RestClient.builder(
                Arrays.stream(hostList.split(";"))
                    .map(element -> {
                    String[] l = element.split(":");
                    return new HttpHost(l[0], Integer.parseInt(l[1]),"http");
                })
                    .toArray(HttpHost[]::new)
        ));
    }
}
