package com.taotao.common.bean;

import java.util.List;

/**
 * @author Srhsw95
 * @version 2017年2月5日 下午5:11:44
 */
public class EasyUIResult {
    /**
     * 返回结果总数
     */
    private Long tatal;

    /**
     * 返回结果列表
     */
    private List<?> rows;

    /**
     * 无参构造
     */
    public EasyUIResult() {

    }

    /**
     * 有参构造
     * 
     * @param tatal
     * @param rows
     */
    public EasyUIResult(Long tatal, List<?> rows) {
        this.tatal = tatal;
        this.rows = rows;
    }

    public Long getTatal() {
        return tatal;
    }

    public void setTatal(Long tatal) {
        this.tatal = tatal;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
