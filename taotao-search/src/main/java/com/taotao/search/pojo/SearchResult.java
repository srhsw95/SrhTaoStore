package com.taotao.search.pojo;

import java.util.List;



/**
 * @author  Srhsw95
 * @version 2017年2月19日 上午12:19:50
 */
public class SearchResult {
    private Long total;
    
    private List<?> list;

    public SearchResult(){
        
    }
    
    public SearchResult(Long total,List<?> list){
        this.total=total;
        this.list=list;
    }
    
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    } 
    
    
}


