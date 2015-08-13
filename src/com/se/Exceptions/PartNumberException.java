package com.se.Exceptions;

public class PartNumberException extends Exception{

	@Override
	public String getMessage() {
		return "Not Valid SE PartNumber";
	}
}
