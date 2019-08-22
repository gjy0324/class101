package cn.itcast;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class EsManager {
    //准备客户端
    private TransportClient client=null;

     //创建客户端      tcp比http  更快速更简洁
    @Before   //在执行测试代码之前执行
    public void init()throws Exception{
        //初始化客户端，此代码是官网提供的
        client  = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
    }

    @Test
   public void testSearch(){
        SearchResponse response =client.prepareSearch("heima")                 //指定索引库
       // .setQuery(QueryBuilders.termQuery("color","红"))        //Query设置查询方式---term查询
                //  .setQuery(QueryBuilders.matchAllQuery())          //Query设置查询方式----查询所有：matchAll
      // .setQuery(QueryBuilders.matchQuery("goodsName","小米手机"))    //Query设置查询方式---分词查询  matche  分为小米  手机
              //  .setQuery(QueryBuilders.wildcardQuery("goodsName","*手*"))  //Queru设置查询方式 ---模糊查询    wildcard  *
                //.setQuery(QueryBuilders.fuzzyQuery("goodsName","大米").fuzziness(Fuzziness.ONE))  //Query设置查询方式---容错查询  fuzzy  如果查询中文，需要将默认的容错值由0，5 改成1  1代码两个字节 一个中文
             // .setQuery(QueryBuilders.rangeQuery("price").gte(1000).lte(2000))  //Query设置查询方式----范围查询 rang   gte  大于等于 lte小于等于
            //    .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("goodsName","小米")).mustNot(QueryBuilders.termQuery("goodsName","华为")))      Query查询法方式----聚合查询   bool     must：必须包含
                //.setPostFilter(QueryBuilders.rangeQuery("price").from(10000).to(30000))     //过滤查询
        .setFrom(0).setSize(60)    //分页
        .get();        //执行 查询

        SearchHits searchHits = response.getHits();
        System.out.println("查询总条数："+searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
        }
    }

    //客户端关闭
    @After  //在测试执行完后执行
    public void end(){
        //关闭客户端
        client.close();
    }
}
