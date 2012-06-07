package com.novadart.novabill.frontend.client.ui.center;

import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface ClientView extends View {
	void setClient(ClientDTO client);
	void setDocumentsListing(DOCUMENTS docs);
}
