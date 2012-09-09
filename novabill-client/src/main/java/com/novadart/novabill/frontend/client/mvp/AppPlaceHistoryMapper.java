package com.novadart.novabill.frontend.client.mvp;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;

@WithTokenizers({
	HomePlace.Tokenizer.class,
	InvoicePlace.Tokenizer.class,
	BusinessPlace.Tokenizer.class,
	ClientPlace.Tokenizer.class,
	EstimationPlace.Tokenizer.class,
	CreditNotePlace.Tokenizer.class,
	TransportDocumentPlace.Tokenizer.class
})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {

}
