package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Content;


public interface ContentService {
	
	public abstract boolean contentExist(String contentsID);

	public abstract Content getContent(String contentsID);

	public abstract Content createContent(Content content);

	public abstract void saveContent(Content content);
	
	public abstract void deleteContent(String contentsID);
}