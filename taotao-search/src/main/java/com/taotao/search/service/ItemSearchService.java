package com.taotao.search.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;

/**
 * @author Srhsw95
 * @version 2017年2月19日 上午12:35:25
 */
@Service
public class ItemSearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;

    /**
     * 搜索服务
     * 
     * @param keyWord
     * @param page
     * @param rows
     * @return
     */
    public SearchResult search(String keywords, Integer page, Integer rows) {
        try {
            SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
            solrQuery.setQuery("title:" + keywords + " AND status:1"); // 搜索关键词
            // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
            solrQuery.setStart((Math.max(page, 1) - 1) * rows);
            solrQuery.setRows(rows);
            // 是否需要高亮
            boolean isHighlighting = !StringUtils.equals("*", keywords) && StringUtils.isNotEmpty(keywords);

            if (isHighlighting) {
                // 设置高亮
                solrQuery.setHighlight(true); // 开启高亮组件
                solrQuery.addHighlightField("title");// 高亮字段
                solrQuery.setHighlightSimplePre("<em>");// 标记，高亮关键字前缀
                solrQuery.setHighlightSimplePost("</em>");// 后缀
            }

            // 执行查询
            QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
            List<Item> items = queryResponse.getBeans(Item.class);
            if (isHighlighting) {
                // 将高亮的标题数据写回到数据对象中
                Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
                for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
                    for (Item item : items) {
                        if (!highlighting.getKey().equals(item.getId().toString())) {
                            continue;
                        }
                        //将需要高亮的商品标题设置高亮
                        item.setTitle(StringUtils.join(highlighting.getValue().get("title"), ""));
                        break;
                    }
                }
            }
            return new SearchResult(queryResponse.getResults().getNumFound(), items);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return new SearchResult(0L,null);
    }

}
