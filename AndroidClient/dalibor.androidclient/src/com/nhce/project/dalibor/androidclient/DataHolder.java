package com.nhce.project.dalibor.androidclient;

public class DataHolder 
{
	private static String msMessage = "";
	private static int msDirection = 0;
	private static int msTurnLeft =  0;
	private static int msTurnRight = 0;
	private static int msPower = 0;
	
	public static String getMessage() {
		return msMessage;
	}
	public static void setMessage(String msMessage) {
		DataHolder.msMessage = msMessage;
	}
	public static int getDirection() {
		return msDirection;
	}
	public static void setDirection(boolean isFront) {
		if(isFront){
			DataHolder.msDirection = 0;
		}
		else {
			DataHolder.msDirection = 1;
		}
	}
	public static int getMsTurnLeft() {
		return msTurnLeft;
	}
	public static void setTurnLeft(boolean isLeft) {
		if(isLeft){
			DataHolder.msTurnLeft = 1;
			DataHolder.msTurnRight = 0;
		}
		else {
			DataHolder.msTurnLeft = 0;
		}
	}
	public static int getMsTurnRight() {
		return msTurnRight;
	}
	public static void setTurnRight(boolean isRight) {
		if(isRight){
			DataHolder.msTurnLeft = 0;
			DataHolder.msTurnRight = 1;
		}
		else {
			DataHolder.msTurnRight = 0;
		}
	}
	public static int getPower() {
		return msPower;
	}
	public static void setPower(boolean isOn) {
		if(isOn){
			DataHolder.msPower = 1;
		}
		else {
			DataHolder.msPower = 0;
		}
	}
	
	public static String getDirectionString() {
		return String.valueOf(msPower*1000+msDirection*100+msTurnLeft*10+msTurnRight);
	}
}
