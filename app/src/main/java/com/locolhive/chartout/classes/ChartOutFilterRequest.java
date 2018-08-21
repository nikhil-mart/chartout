package com.locolhive.chartout.classes;

import com.locolhive.chartout.statics.ChartOutOrderBy;

public class ChartOutFilterRequest {
	private ChartOutOrderBy orderBy;
	private String destination;
	private Integer skip;
	private Integer limit;
	private String tagLineFilterText;

	public ChartOutFilterRequest() {
	}

	public ChartOutOrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(ChartOutOrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getSkip() {
		return skip;
	}

	public void setSkip(Integer skip) {
		this.skip = skip;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getTagLineFilterText() {
		return tagLineFilterText;
	}

	public void setTagLineFilterText(String tagLineFilterText) {
		this.tagLineFilterText = tagLineFilterText;
	}
}
