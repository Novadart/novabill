package com.novadart.novabill.frontend.client;

import java.util.Comparator;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class SharedComparators {
	
	public static final Comparator<ClientDTO> CLIENT_COMPARATOR = new Comparator<ClientDTO>() {
		
		@Override
		public int compare(ClientDTO o1, ClientDTO o2) {
			
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	
	public static final Comparator<PaymentTypeDTO> PAYMENT_COMPARATOR = new Comparator<PaymentTypeDTO>() {
		
		@Override
		public int compare(PaymentTypeDTO o1, PaymentTypeDTO o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	//comparator returns inverse ordering
	public static final Comparator<AccountingDocumentDTO> DOCUMENT_COMPARATOR = new Comparator<AccountingDocumentDTO>() {
		
		@Override
		public int compare(AccountingDocumentDTO o1, AccountingDocumentDTO o2) {
			Integer year1 = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(o1.getAccountingDocumentDate()));
			Integer year2 = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(o2.getAccountingDocumentDate()));
			
			if(!year1.equals(year2)) {
				return -year1.compareTo(year2);
			} else {
				return -o1.getDocumentID().compareTo(o2.getDocumentID());
			}
		}
	};
	
}
