package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

import com.novadart.novabill.shared.client.dto.IClientDTO;

public class ClientsData implements IClientsData {
	
	private List<IClientDTO> clients;
	
	public List<IClientDTO> getClients() {
		return clients;
	}
	
	public void setClients(List<IClientDTO> clients) {
		this.clients = clients;
	}
}