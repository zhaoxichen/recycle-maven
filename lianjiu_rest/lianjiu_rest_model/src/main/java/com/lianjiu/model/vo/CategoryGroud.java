package com.lianjiu.model.vo;

import java.util.List;

import com.lianjiu.model.Category;

public class CategoryGroud {
	private Long parentId;
	private List<Category> childrens;

	public CategoryGroud() {
	}

	public CategoryGroud(Long parentId, List<Category> childrens) {
		this.parentId = parentId;
		this.childrens = childrens;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<Category> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Category> childrens) {
		this.childrens = childrens;
	}
}
