package com.project.crud.crud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class BaseModel {

	@Id
	private String id;

	@Field("createtime")
	private long createdtimeStamp;

	@Field("lastModified")
	private long lastModified;
}
