package com.novadart.novabill.domain.dto.transformer;

import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.shared.client.dto.NotificationDTO;

public class NotificationDTOTransformer {
	
	public static NotificationDTO toDTO(Notification notification){
		NotificationDTO notificationDTO = new NotificationDTO();
		notificationDTO.setId(notification.getId());
		notificationDTO.setCreationTime(notification.getCreationTime());
		notificationDTO.setMessage(notification.getMessage());
		notificationDTO.setType(notification.getType());
		return notificationDTO;
	}

}
