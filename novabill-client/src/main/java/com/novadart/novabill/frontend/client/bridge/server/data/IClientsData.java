package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

import com.novadart.novabill.shared.client.dto.IClientDTO;

public interface IClientsData {
	List<IClientDTO> getClients();
	void setClients(List<IClientDTO> clients);
}