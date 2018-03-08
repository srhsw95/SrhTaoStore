package com.taotao.common.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * "小对象"
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月8日 下午9:01:29
 * we can not wait,we can not stop!
 */
public class ItemCatData  {
	
	@JsonProperty("u") // 序列化成json数据时为 u
	private String url;
	
	@JsonProperty("n")
	private String name;
	
	@JsonProperty("i")
	private List<?> items;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}
	
	

}
