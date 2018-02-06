package com.lianjiu.model.vo;

public class SearchObjecVo {
	private Object model;
	// 应付一般的id查询
	private Object id;
	// limit限制
	private int begin;
	private int pageTotalNum;

	public SearchObjecVo() {

	}

	public SearchObjecVo(Object model) {
		this.model = model;
	}

	public SearchObjecVo(int begin, int pageTotalNum) {
		this.begin = begin;
		this.pageTotalNum = pageTotalNum;
	}

	public SearchObjecVo(int begin, int pageTotalNum, Object model) {
		this.model = model;
		this.begin = begin;
		this.pageTotalNum = pageTotalNum;
	}

	public SearchObjecVo(Object id, int begin, int pageTotalNum) {
		this.id = id;
		this.begin = begin;
		this.pageTotalNum = pageTotalNum;
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getPageTotalNum() {
		return pageTotalNum;
	}

	public void setPageTotalNum(int pageTotalNum) {
		this.pageTotalNum = pageTotalNum;
	}

}
