package com.hejia.exception;

@SuppressWarnings("serial")
public class MyRuntimeException extends RuntimeException {
	public MyRuntimeException(String exception) {
		super(exception);
	}
}
