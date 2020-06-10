package com.elastic_demo.alpha.controller;


import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.ml.PostDataRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@RestController
public class ElasticController {
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @GetMapping("/search/{index}/{id}")
    public GetResponse elasticTest(@PathVariable String index,
                                   @PathVariable String id) throws IOException, InterruptedException {
        GetRequest getRequest = new GetRequest(index,id);
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true,new String[]{"first_name","last_name"}, Strings.EMPTY_ARRAY);
        getRequest.fetchSourceContext(fetchSourceContext);



        /*GetResponse res = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);*/

        final GetResponse[] res = {null};

        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {
                System.out.println("==========================>");
                System.out.println(documentFields.getSourceAsString());
                res[0] = documentFields;
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("++++++++++++++++++++++++++++>");
                e.printStackTrace();
            }
        };
        restHighLevelClient.getAsync(getRequest,RequestOptions.DEFAULT,listener);
        Thread.sleep(1000);
        return res[0];
    }
    @PostMapping("add/{index}/{id}/{source}")
    public IndexResponse elasticTest1(@PathVariable String index,
                                    @PathVariable String id,
                                    @PathVariable String source) throws IOException {
        IndexRequest indexRequest = new IndexRequest();


        indexRequest.index(index).id(id).source("name",source);

        indexRequest.create(true);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest,RequestOptions.DEFAULT);
        indexResponse.getResult();

        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
        Arrays.stream(shardInfo.getFailures()).forEach(element->{
            System.out.println(element.reason());
        });



        return  indexResponse;
    }
    @PostMapping("/update")
    public UpdateResponse elasticTest2() throws IOException {

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("book").id("2").doc(new HashMap<String,Object>(){{
            put("first_name","test");
        }});
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);

        updateResponse.getResult()  ;
        return updateResponse;
    }
    @GetMapping("/bulk")
    public BulkResponse elasticTest3() throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest().index("book").create(true).source("name","testBook"));
        bulkRequest.add(new IndexRequest().index("book").create(true).source("name","testBook"));


        BulkResponse res = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);

        System.out.println("res.hasFailures:"+res.hasFailures());
        Arrays.stream(res.getItems()).forEach(element->{
            System.out.println("=========>index:"+ element.getIndex()+";id:"+element.getId());
            System.out.println("OpType:"+element.getOpType());
            System.out.println("isFailed:"+element.isFailed());
            System.out.println("Result:"+element.getResponse().getResult());
        });
        return res;
    }

}
