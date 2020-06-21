package com.app.assignment.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.app.assignment.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Story implements Serializable {
	
	private String id;
	
	private String title;
	
	private String url;
	
	private int score;
	
	@JsonProperty("createdBy")
	private String by;
	
	@JsonProperty("createdDate")
	private Date time;
	
	@JsonIgnore
	private List<Integer> kids;
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the by
	 */
	public String getBy() {
		return by;
	}

	/**
	 * @param by the by to set
	 */
	public void setBy(String by) {
		this.by = by;
	}

	/**
	 * @return the time
	 */
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the kids
	 */
	public List<Integer> getKids() {
		return kids;
	}

	/**
	 * @param kids the kids to set
	 */
	public void setKids(List<Integer> kids) {
		this.kids = kids;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((by == null) ? 0 : by.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((kids == null) ? 0 : kids.hashCode());
		result = prime * result + score;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Story other = (Story) obj;
		if (by == null) {
			if (other.by != null)
				return false;
		} else if (!by.equals(other.by))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kids == null) {
			if (other.kids != null)
				return false;
		} else if (!kids.equals(other.kids))
			return false;
		if (score != other.score)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Story [id=" + id + ", title=" + title + ", url=" + url + ", score=" + score + ", by=" + by + ", time="
				+ time + ", kids=" + kids + "]";
	}
	
}
