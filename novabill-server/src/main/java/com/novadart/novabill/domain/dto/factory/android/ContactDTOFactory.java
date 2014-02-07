package com.novadart.novabill.domain.dto.factory.android;

import com.novadart.novabill.android.shared.dto.ContactDTO;
import com.novadart.novabill.domain.Contact;

public class ContactDTOFactory {
	
	public static ContactDTO toDTO(Contact contact){
		ContactDTO contactDTO = new ContactDTO();
		contactDTO.setFirstName(contact.getFirstName());
		contactDTO.setLastName(contact.getLastName());
		contactDTO.setEmail(contact.getEmail());
		contactDTO.setMobile(contact.getMobile());
		contactDTO.setPhone(contact.getPhone());
		contactDTO.setFax(contact.getFax());
		return contactDTO;
	}
	
	public static void copyFromDTO(Contact contact, ContactDTO contactDTO){
		contact.setFirstName(contactDTO.getFirstName());
		contact.setLastName(contactDTO.getLastName());
		contact.setEmail(contactDTO.getEmail());
		contact.setMobile(contactDTO.getMobile());
		contact.setPhone(contactDTO.getPhone());
		contact.setFax(contactDTO.getFax());
	}

}
