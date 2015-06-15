package com.enseirb.telecom.dngroup.snapmail.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.snapmail.exception.NoSuchProperty;

/**
 * 
 *
 */
public interface MediaHomeFacade {

	/**
	 * take a bodypart from the mail and create a MH link from it
	 */
	public abstract String bodyPart2Link(InputStream inputStream,
			String filename, String type,User user,
			List<String> recipients) throws IOException;

	// TODO rename : getLinkFromBodyPart()
	/**
	 * for a specific user, retrieve his smtp parameters
	 * 
	 * @throws NoSuchProperty
	 */
	public abstract MailerProperties getSmtpParamORH(User user) throws NoSuchProperty;

	// TODO rename : getMailerPropertiesFromUser

	public abstract User getUserORH(String username, String password);
	
}