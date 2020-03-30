package com.gold.kiwi.common.ad.bean;

public class AdMANBean extends AdBean
{
	private String publisherCode;
	private String mediaCode;
	private String sectionCode;

	public AdMANBean()
	{
	}

	public AdMANBean(String publisherCode, String mediaCode, String sectionCode)
	{
		this.publisherCode = publisherCode;
		this.mediaCode = mediaCode;
		this.sectionCode = sectionCode;
	}

	public String getPublisherCode()
	{
		return publisherCode;
	}

	public void setPublisherCode(String publisherCode)
	{
		this.publisherCode = publisherCode;
	}

	public String getMediaCode()
	{
		return mediaCode;
	}

	public void setMediaCode(String mediaCode)
	{
		this.mediaCode = mediaCode;
	}

	public String getSectionCode()
	{
		return sectionCode;
	}

	public void setSectionCode(String sectionCode)
	{
		this.sectionCode = sectionCode;
	}
}
