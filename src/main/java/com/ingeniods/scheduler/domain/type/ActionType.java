package com.ingeniods.scheduler.domain.type;

import lombok.Getter;

public enum ActionType {
	GET(PortType.REST),
	POST(PortType.REST),
	PUT(PortType.REST),
	PATCH(PortType.REST),
	DELETE(PortType.REST),
	MESSAGE(PortType.MESSAGE_QUEUE);
	
	@Getter
	private PortType port;
	
	private ActionType(PortType port){
		this.port=port;
	}
	
	
	public boolean isRest() {
		return !this.name().equals(MESSAGE.name());
	}
	
	public boolean isMessage() {
		return this.name().equals(MESSAGE.name());
	}
}
