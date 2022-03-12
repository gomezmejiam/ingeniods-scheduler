package com.ingeniods.scheduler.domain.type;

public enum CronExpresion {
	EVERY_HOUR_STARTED_AT_45_MINUTES ( "0 45 * * * *"),
	EVERY_HOUR_STARTED_AT_30_MINUTES ( "0 30 * * * *"),
	EVERY_HOUR_STARTED_AT_15_MINUTES ( "0 15 * * * *"),
	EVERY_HOUR_STARTED_AT_05_MINUTES ( "0 05 * * * *"),
	EVERY_HOUR ( "0 0 * ? * * *"),
	EVERY_SIX_HOURS_STARTED_AT_5AM ( "0 0 5,11,17,23 * * *"),
	EVERY_FOUR_HOURS_STARTED_AT_1AM ( "0 0 1,5,9,13,17,21 * * *"),
	EVERY_TWO_HOURS_STARTED_AT_1AM ( "0 0 1,3,5,7,9,11,13,15,17,19,21,23 * * *"),
	EVERY_SIX_HOURS_STARTED_AT_6AM ( "0 0 6,12,18,0 * * *"),
	EVERY_FOUR_HOURS_STARTED_AT_2AM ( "0 0 2,6,12,16,18,0 * * *"),
	EVERY_TWO_HOURS_STARTED_AT_2AM ( "0 0 2,4,6,8,10,12,14,16,18,20,22,0 * * *"),
	EVERY_TWELVE_HOURS_STARTED_AT_1AM ( "0 0 1,13 * * *"),
	EVERYDAY_AT_1AM ( "0 0 1 * * *"),
	EVERYDAY_AT_3AM ( "0 0 3 * * *"),
	EVERYDAY_AT_5AM ( "0 0 5 * * *"),
	EVERYDAY_AT_7AM ( "0 0 7 * * *"),
	EVERYDAY_AT_1AM_2AM_3AM_4AM_5AM ( "0 0 1,2,3,4,5 ? * *"),
	EVERY_ONE_MINUTES ( "0 */1 * ? * *"),
	EVERY_FIVE_MINUTES ( "0 */5 * ? * *"),
	EVERY_TEN_MINUTES ( "0 */10 * ? * *"),
	EVERY_FIFTEEN_MINUTES ( "0 */15 * ? * *"),
	EVERY_TWO_HOURS_STARTED_AT_0AM ( "0 0 0,2,4,6,8,10,12,14,16,18,20,22 ? * *"),
	EVERY_FIVE_SECONDS ( "*/5 * * * * *"),
	EVERY_THIRTY_SECONDS ( "*/30 * * * * *");
	
	private String expresion;
	
	private CronExpresion(String value) {
		this.expresion = value;
	}
	
	public String getExpresion() {
		return expresion;
	}
}
