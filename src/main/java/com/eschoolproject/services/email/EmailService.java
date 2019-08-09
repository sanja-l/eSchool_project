package com.eschoolproject.services.email;

import com.eschoolproject.entities.email.EmailObject;

public interface EmailService {
		void sendSimpleMessage (EmailObject object);
		//void sendTemplateMessage (EmailObject object) throws Exception;
		//void sendMessageWithAttachment (EmailObject object, String pathToAttachment) throws Exception;


}
