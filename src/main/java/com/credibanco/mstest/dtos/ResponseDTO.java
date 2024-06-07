package com.credibanco.mstest.dtos;

import java.io.Serializable;

import org.springframework.http.HttpStatus;


public class ResponseDTO<T> implements Serializable {

	private static final long serialVersionUID = -4851253181716532026L;
	
	private HttpStatus responseCode;
	private String message;
	private transient T data;
	private String trackId;
	
	public HttpStatus getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(HttpStatus responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public ResponseDTO(HttpStatus responseCode, String message, T data, String trackId) {
		this.responseCode = responseCode;
		this.message = message;
		this.data = data;
		this.trackId = trackId;
	}

	public ResponseDTO() {
		
	}
	
}
