package com.example.listview;

public class listCard_Item
{
	private int mImgID;
	private String mType;
	private String mDescreption;
	
	public listCard_Item(int imgID, String type, String Des)
	{
		setmImgID(imgID);
		setmType(type);
		setmDescreption(Des);
	}
	
	public int getmImgID()
	{
		return mImgID;
	}
	public void setmImgID(int mImgID)
	{
		this.mImgID = mImgID;
	}
	public String getmType()
	{
		return mType;
	}
	public void setmType(String mType)
	{
		this.mType = mType;
	}
	public String getmDescreption()
	{
		return mDescreption;
	}
	public void setmDescreption(String mDescreption)
	{
		this.mDescreption = mDescreption;
	}
	
	
}
