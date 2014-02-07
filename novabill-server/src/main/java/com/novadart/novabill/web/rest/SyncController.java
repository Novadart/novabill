package com.novadart.novabill.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.android.shared.data.SyncEntityType;
import com.novadart.novabill.android.shared.dto.ClientDTO;
import com.novadart.novabill.android.shared.dto.SyncDeltaObjectsDTO;
import com.novadart.novabill.android.shared.dto.SyncDeltaStateDTO;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.dto.factory.android.ClientDTOFactory;
import com.novadart.novabill.service.sync.SyncService;

@Controller
@RequestMapping("/rest/1/sync/delta")
public class SyncController {

	@Autowired
	private SyncService syncService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping("/state")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public SyncDeltaStateDTO getDeltaState(@RequestParam(value = "mark", required = false) Long mark){
		return syncService.getDeltaState(mark);
	}
	
	private <E> List<E> fetchEntities(Class<E> cls, List<Long> ids){
		String qlString = "from " + cls.getSimpleName() + " where id in (:ids)";
		TypedQuery<E> q = entityManager.createQuery(qlString, cls);
		q.setParameter("ids", ids);
		return q.getResultList();
	}
	
	@RequestMapping("/objects")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(readOnly = true)
	public SyncDeltaObjectsDTO getDeltaObjects(@RequestBody Map<SyncEntityType, List<Long>> ids){
		SyncDeltaObjectsDTO result = new SyncDeltaObjectsDTO();
		List<ClientDTO> clients = new ArrayList<>();
		for(Client client: fetchEntities(Client.class, ids.get(SyncEntityType.CLIENT)))
			clients.add(ClientDTOFactory.toDTO(client));
		result.setClients(clients);
		return result;
	}
	
	
}
