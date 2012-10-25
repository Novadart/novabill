package com.novadart.novabill.frontend.client.ui.center;

import com.novadart.novabill.frontend.client.ui.View;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public interface EstimationView extends View {
	
	public void setDataForNewEstimation(ClientDTO client, Long progressiveId);
	
	public void setDataForNewEstimation(ClientDTO client, Long progressiveId, EstimationDTO estimation);
	
	public void setEstimation(EstimationDTO estimation);
}
