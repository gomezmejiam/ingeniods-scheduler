package com.ingeniods.scheduler.domain.type;

public enum ActionType {
	GET,
	POST,
	PUT,
	PATCH,
	DELETE,
	MESSAGE;
	
	public boolean isRest() {
		return !this.name().equals(MESSAGE.name());
	}
}
