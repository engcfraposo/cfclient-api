package com.cloudfox.cfclient.resources.exception;

import java.time.Instant;
import java.util.Objects;

public class StandardError {

	private String error;
	private String message;
	private String path;
	private Integer status;
	private Instant timestamp;

	public StandardError() {
	}

	public StandardError(Instant timestamp, Integer status, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		StandardError other = (StandardError) obj;
		return Objects.equals(error, other.error) && Objects.equals(message, other.message)
				&& Objects.equals(path, other.path) && Objects.equals(status, other.status)
				&& Objects.equals(timestamp, other.timestamp);
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public Integer getStatus() {
		return status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(error, message, path, status, timestamp);
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

}