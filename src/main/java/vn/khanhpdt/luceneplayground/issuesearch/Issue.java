package vn.khanhpdt.luceneplayground.issuesearch;

import java.util.Date;

class Issue {

	private String key;
	private String summary;
	private String description;
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

	String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	Date getCreatedTime() {
		return createdTime;
	}

	void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
