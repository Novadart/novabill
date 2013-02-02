package com.novadart.novabill.frontend.client.view.center;

import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface ClientView extends View {
	void setClient(ClientDTO client);
	void setDocumentsListing(DOCUMENTS docs);
}
