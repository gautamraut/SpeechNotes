package com.microsoft.CognitiveServicesExample.model;

/**
 * Created by shrbansa on 8/21/17.
 */

public class Message
{
	private String mText;
	private boolean ifAccepted = false;

	public Message(String msg, String a, String b)
	{
		this.mText = msg;
		ifAccepted = false;
	}

	public boolean isAccepted() {
		return ifAccepted;
	}

	public void setAccepted(boolean accept)
	{
		ifAccepted = accept;
	}
	public String getMessage()
	{
		return this.mText;
	}
}
