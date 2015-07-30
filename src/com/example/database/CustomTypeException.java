package com.example.database;

/**
 * 由于sqlite没有boolean，所以是用int存储（0，1），防止在误操作导致不可预料的异常，
 * 所以定义此异常类
 * @author tom
 *
 */
public class CustomTypeException extends Exception
{
	public CustomTypeException()
	{
		super();
	}
	
	public CustomTypeException(String msg)
	{
		super(msg);
	}
}
