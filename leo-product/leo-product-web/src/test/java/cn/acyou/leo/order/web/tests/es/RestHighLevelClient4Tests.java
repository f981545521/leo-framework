package cn.acyou.leo.order.web.tests.es;

import cn.acyou.leo.framework.util.DateUtil;
import cn.acyou.leo.framework.util.IdUtil;
import cn.acyou.leo.framework.util.RandomUtil;
import cn.acyou.leo.order.web.tests.es.bo.Order;
import cn.acyou.leo.order.web.tests.es.bo.Order2025;
import cn.acyou.leo.product.entity.Student;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youfang
 * @version [1.0.0, 2021-8-7]
 **/
public class RestHighLevelClient4Tests {


    private static RestHighLevelClient client;

    @BeforeAll
    public static void init(){
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
    }


    public static List<String> indexsList = Lists.newArrayList("order-2022","order-2023","order-2024","order-2025");

    //1. 创建索引请求
    @Test
    void testCreateIndexMapping() throws IOException {
        testDeleteIndex();
        for (String s : indexsList) {
            CreateIndexRequest request = new CreateIndexRequest(s);
            // 定义映射
            String mapping = "{\n" +
                    "  \"mappings\": {\n" +
                    "    \"properties\": {\n" +
                    "      \"id\": { \"type\": \"keyword\" },\n" +
                    "      \"createTime\": { \"type\": \"date\" },\n" +
                    "      \"userId\": { \"type\": \"long\" },\n" +
                    "      \"money\": { \"type\": \"scaled_float\", \"scaling_factor\": 100 },\n" +
                    "      \"memo\": { \"type\": \"text\" },\n" +
                    "      \"point\": { \"type\": \"integer\" },\n" +
                    "      \"phone\": { \"type\": \"keyword\" },\n" +
                    "      \"storeName\": { \"type\": \"text\" }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            request.source(mapping, XContentType.JSON);
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println("Index creation response: " + createIndexResponse.isAcknowledged());
        }
    }

    //2. 判断索引库是够存在
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest es = new GetIndexRequest("order-2025");
        boolean exists = client.indices().exists(es, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //2. 判断索引库是够存在
    @Test
    void testDeleteIndex() throws IOException {
        for (String indexName : indexsList) {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
            System.out.println(delete.isAcknowledged());
        }
    }

    @Test
    void testAddDocumentBatch() throws IOException {
        for (String name : indexsList) {
            BulkRequest bulkRequest = new BulkRequest();
            for (int i = 0; i < 10000; i++) {
                Order2025 order = new Order2025();
                order.setId(IdUtil.objectId());
                order.setCreateTime(DateUtil.randomRangeDate("1990-01-01", "2018-12-31"));
                order.setUserId(RandomUtil.randomRangeLong(10000L, 20000L));
                order.setMoney(new BigDecimal(RandomUtil.randomRangeNumber(1, 99) + "." + RandomUtil.randomRangeNumber(1, 99)));
                order.setMemo("");
                order.setPoint(RandomUtil.randomAge());
                order.setPhone(RandomUtil.randomTelephone());
                order.setStoreName(RandomUtil.randomUserName());
                if (i%20==0) {  // 1000->200(4*50)   , 2 : 10000 -> 2000(4*500)
                    order.setUserId(666L);
                    order.setPhone("18205166207");
                    order.setStoreName("南京中心仓");
                }
                IndexRequest indexRequest = new IndexRequest(name);
                indexRequest.id(order.getId());
                indexRequest.timeout(TimeValue.timeValueSeconds(60));
                indexRequest.timeout("60s");
                indexRequest.source(JSON.toJSONString(order), XContentType.JSON);
                bulkRequest.add(indexRequest);

                //批量保存
                if (bulkRequest.requests().size() == 100) {
                    doSaveDocumentBatch(bulkRequest);
                    bulkRequest = new BulkRequest();
                }
            }
            doSaveDocumentBatch(bulkRequest);
        }
    }

    void doSaveDocumentBatch(BulkRequest bulkRequest) throws IOException {
        if (CollectionUtil.isNotEmpty(bulkRequest.requests())) {
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                String errorMsg = bulkResponse.buildFailureMessage();
                System.err.println("批量插入失败: " + errorMsg);
            }
        }
    }

    @Test
    void testAddDocument() throws IOException {
        for (String name : indexsList) {
            for (int i = 0; i < 10000; i++) {
                Order2025 order = new Order2025();
                order.setId(IdUtil.objectId());
                order.setCreateTime(DateUtil.randomRangeDate("1990-01-01", "2018-12-31"));
                order.setUserId(RandomUtil.randomRangeLong(10000L, 20000L));
                order.setMoney(new BigDecimal(RandomUtil.randomRangeNumber(1, 99) + "." + RandomUtil.randomRangeNumber(1, 99)));
                order.setMemo("");
                order.setPoint(RandomUtil.randomAge());
                order.setPhone("18205166207");
                order.setStoreName(RandomUtil.randomUserName());
                IndexRequest indexRequest = new IndexRequest(name);
                indexRequest.id(order.getId());
                indexRequest.timeout(TimeValue.timeValueSeconds(60));
                indexRequest.timeout("60s");
                IndexRequest source = indexRequest.source(JSON.toJSONString(order), XContentType.JSON);
                IndexResponse index = client.index(source, RequestOptions.DEFAULT);
                System.out.println(index.toString());
                System.out.println(index.status());
            }
        }
    }

    @Test
    public void testAddDocument2() throws IOException {
        Order order = new Order();
        order.setFid(IdUtil.objectId());
        order.setCustomerPhone("18205166201");
        order.setMoney(new BigDecimal("-15.00"));
        order.setPoint(10);
        IndexRequest indexRequest = new IndexRequest("order-2025");
        indexRequest.id(order.getFid());
        indexRequest.timeout(TimeValue.timeValueSeconds(60));
        indexRequest.timeout("60s");
        IndexRequest source = indexRequest.source(JSON.toJSONString(order), XContentType.JSON);
        IndexResponse index = client.index(source, RequestOptions.DEFAULT);
        System.out.println(index.toString());
        System.out.println(index.status());
    }

    //5. 查询文档是否存在
    @Test
    void testIsExistDocument() throws IOException {
        GetRequest getRequest = new GetRequest("es-boot", "1");
        //不获取返回的 _source 的上下文了
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //6. 获取文档内容
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("es-boot", "1");
        //不获取返回的 _source 的上下文了
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    //7. 更新文档
    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("es-boot", "1");
        updateRequest.timeout("10s");
        Student user = new Student(2, "王二小", 18, null);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(update.status());
        System.out.println(update);
    }

    //8. 删除文档
    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("es-boot", "1");
        deleteRequest.timeout("10s");
        DeleteResponse delete = client.delete(deleteRequest, RequestOptions.DEFAULT);

        System.out.println(delete.status());
        System.out.println(delete);
    }

    //9. 批量处理
    @Test
    void testBulkRequest() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        List<Student> userList = new ArrayList<>();
        userList.add(new Student(2, "王2小", 18, null));
        userList.add(new Student(2, "王3小", 18, null));
        userList.add(new Student(2, "王4小", 18, null));
        userList.add(new Student(2, "王5小", 18, null));
        int idx = 1;
        for (Student user : userList) {
            //批量修改或者删除
            bulkRequest.add(new IndexRequest("es-boot")
                    .id("" + idx)//不放ID会生成随机ID
                    .source(JSON.toJSONString(user), XContentType.JSON));
            idx++;
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
        System.out.println(bulk.status());
    }

    //10. 查询
    @Test
    void testSearch() throws IOException {
        //搜索请求
        SearchRequest searchRequest = new SearchRequest("es-boot");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //构建查询条件
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "xxx");
        //QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(20));

        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        for (SearchHit hit : hits.getHits()) {
            System.out.println(JSON.toJSONString(hit));
        }
    }

    //11. 查询2
    @Test
    void testSearch2() throws IOException {
        //搜索请求
        SearchRequest searchRequest = new SearchRequest("order-*");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        final BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("phone"))
                //.must(QueryBuilders.termQuery("phone", "18205166207"))
                .mustNot(QueryBuilders.termQuery("phone", ""))
                ;
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("group_by_phone")
                .field("phone")                   // 按name分组‌:ml-citation{ref="1" data="citationList"}
                .size(1000);                            // 设置足够大的分组数
        //{"error":{"root_cause":[{"type":"too_many_buckets_exception","reason":"Trying to create too many buckets. Must be less than or equal to: [10000] but was [10001]. This limit can be set by changing the [search.max_buckets] cluster level setting.","max_buckets":10000}
        aggregation.subAggregation(
                AggregationBuilders.sum("sum_money").field("money")  // sum(score)计算‌:ml-citation{ref="4" data="citationList"}
        );
        sourceBuilder.query(queryBuilder);
        sourceBuilder.timeout(TimeValue.timeValueSeconds(20));
        sourceBuilder.aggregation(aggregation);// 设置聚合和返回参数
        sourceBuilder.size(0);  // 不返回原始文档‌:ml-citation{ref="3" data="citationList"}
        sourceBuilder.trackTotalHits(true);
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        for (SearchHit hit : hits.getHits()) {
            System.out.println(JSON.toJSONString(hit));
        }
    }

    @Test
    void testSearch3() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("phone", "18205166207"))
                ;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .from(0)
                .size(100);
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = new SearchRequest("order-*").source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        System.out.println("end");
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            System.out.println(json);
        }
    }


    //11. 查询4
    @Test
    void testSearch4() throws IOException {
        List<CompositeValuesSourceBuilder<?>> sources = new ArrayList<>();
        sources.add(new TermsValuesSourceBuilder("group_by_phone").field("phone"));
        CompositeAggregationBuilder compositeAggs = AggregationBuilders.composite(
                        "composite_aggs", sources)
                .subAggregation(
                        AggregationBuilders.sum("sum_money").field("money")
                )
                .size(100);
        //term      不分词     精确匹配    无相关性评分                  keyword、数值、日期等
        //精确匹配，不对查询内容进行分词，直接使用原始值匹配倒排索引。例如：查询 "apple pie" 时，只会匹配完整包含 "apple pie" 的文档‌
        //match     分词      模糊匹配    基于匹配度计算相关性评分         text 等需要分词的字段
        //全文检索，先对查询内容分词，再用分词结果匹配倒排索引。例如：查询 "apple pie" 会被拆分为 "apple" 和 "pie"，匹配包含任一词汇的文档‌2
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("phone"))
                .mustNot(QueryBuilders.termQuery("phone", ""))
                ;
        //must  所有条件必须同时满足，类似 AND 逻辑
        //should  条件满足其一即可，类似 OR 逻辑，
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(boolQueryBuilder)
                .size(0)
                .trackTotalHits(true)
                .aggregation(compositeAggs);

        SearchRequest searchRequest = new SearchRequest("order-*");
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        System.out.println(JSON.toJSONString(hits));
        for (SearchHit hit : hits.getHits()) {
            System.out.println(JSON.toJSONString(hit));
        }
    }
}
