package com.novadart.novabill.frontend.client.ui.west;

import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface WestView extends View {

	void setClient(ClientDTO client);
	
}
