package com.novadart.novabill.domain.dto.factory;

import com.novadart.novabill.domain.BankAccount;
import com.novadart.novabill.shared.client.dto.BankAccountDTO;

public class BankAccountDTOFactory {
	
	public static BankAccountDTO toDTO(BankAccount account){
		if(account == null)
			return null;
		BankAccountDTO accountDTO = new BankAccountDTO();
		accountDTO.setName(account.getName());
		accountDTO.setIban(account.getIban());
		return accountDTO;
	}

}
