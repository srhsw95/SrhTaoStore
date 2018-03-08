package com.taotao.common.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * "大对象"
 * @author srhsw95_Administrator
 * @email  srhsw95@163.com
 * @version 2017年2月8日 下午9:01:06
 * we can not wait,we can not stop!
 */
public class ItemCatResult {
    
	@JsonProperty("data")
	private List<ItemCatData> itemCats = new ArrayList<ItemCatData>();

	public List<ItemCatData> getItemCats() {
		return itemCats;
	}

	public void setItemCats(List<ItemCatData> itemCats) {
		this.itemCats = itemCats;
	}

}
