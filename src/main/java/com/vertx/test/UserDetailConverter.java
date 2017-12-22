package com.vertx.test;

import io.vertx.core.json.JsonObject;

public class UserDetailConverter {
	public static void fromJson(JsonObject json, UserDetail obj) {

		if (json.getValue("id") instanceof Long) {
			obj.setId((Long) json.getValue("id"));
		}
		if (json.getValue("userName") instanceof String) {
			obj.setUserName((String) json.getValue("userName"));
		}
		if (json.getValue("firstName") instanceof String) {
			obj.setFirstName((String) json.getValue("firstName"));
		}
		if (json.getValue("lastName") instanceof String) {
			obj.setLastName((String) json.getValue("lastName"));
		}
		if (json.getValue("gender") instanceof String) {
			obj.setGender((String) json.getValue("gender"));
		}
		if (json.getValue("password") instanceof String) {
			obj.setPassword((String) json.getValue("password"));
		}
		if (json.getValue("status") instanceof Boolean) {
			obj.setStatus((Boolean) json.getValue("status"));
		}
	}

	public static void toJson(UserDetail obj, JsonObject json) {
		if (obj.getId() != null) {
			json.put("id", obj.getId());
		}
		if (obj.getUserName() != null) {
			json.put("userName", obj.getUserName());
		}
		if (obj.getFirstName() != null) {
			json.put("firstName", obj.getFirstName());
		}
		if (obj.getLastName() != null) {
			json.put("lastName", obj.getLastName());
		}
		if (obj.getGender() != null) {
			json.put("gender", obj.getGender());
		}
		if (obj.getPassword() != null) {
			json.put("password", obj.getPassword());
		}
		if (obj.getStatus() != null) {
			json.put("status", obj.getStatus());
		}
	}
}
