package com.wcrs.common.orm;

import java.util.List;

/**
 * 查询结果对象
 * 
 * @author yongtree
 * @date 2009-4-30 上午09:00:12
 * @version 1.0
 */
public class QueryResult<T> {
	private List<T> resultlist;
	private Long totalrecord;

	public List<T> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}

	public Long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(Long totalrecord) {
		this.totalrecord = totalrecord;
	}
}