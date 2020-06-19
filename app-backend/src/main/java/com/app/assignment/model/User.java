package com.app.assignment.model;

import java.util.Date;
import java.util.List;

public class User {

	private String id;
	
	private Date created;
	
	private String about;
	
	private String karma;
	
	List<String> submitted;

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
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * @param about the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * @return the karma
	 */
	public String getKarma() {
		return karma;
	}

	/**
	 * @param karma the karma to set
	 */
	public void setKarma(String karma) {
		this.karma = karma;
	}

	/**
	 * @return the submitted
	 */
	public List<String> getSubmitted() {
		return submitted;
	}

	/**
	 * @param submitted the submitted to set
	 */
	public void setSubmitted(List<String> submitted) {
		this.submitted = submitted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((about == null) ? 0 : about.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((karma == null) ? 0 : karma.hashCode());
		result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
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
		User other = (User) obj;
		if (about == null) {
			if (other.about != null)
				return false;
		} else if (!about.equals(other.about))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (karma == null) {
			if (other.karma != null)
				return false;
		} else if (!karma.equals(other.karma))
			return false;
		if (submitted == null) {
			if (other.submitted != null)
				return false;
		} else if (!submitted.equals(other.submitted))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", created=" + created + ", about=" + about + ", karma=" + karma + ", submitted="
				+ submitted + "]";
	}
	
	
	
	
}
