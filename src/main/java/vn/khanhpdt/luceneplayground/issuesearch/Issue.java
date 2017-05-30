package vn.khanhpdt.luceneplayground.issuesearch;

import java.util.Date;

class Issue {

	private String key;
	private String summary;
	private Date createdTime;

	String getKey() {
		return key;
	}

	void setKey(String key) {
		this.key = key;
	}

	String getSummary() {
		return summary;
	}

	void setSummary(String summary) {
		this.summary = summary;
	}

	Date getCreatedTime() {
		return createdTime;
	}

	void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
