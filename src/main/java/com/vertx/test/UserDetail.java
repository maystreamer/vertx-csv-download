package com.vertx.test;

import io.vertx.core.json.JsonObject;

public class UserDetail {
	private Long id;

	private String userName;

	private String firstName;

	private String lastName;

	private String gender;

	private String password;

	private Boolean status;

	public UserDetail() {

	}

	public UserDetail(JsonObject jsonObject) {
		UserDetailConverter.fromJson(jsonObject, this);
	}

	public Long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGender() {
		return gender;
	}

	public String getPassword() {
		return password;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		UserDetailConverter.toJson(this, json);
		return json;
	}
}