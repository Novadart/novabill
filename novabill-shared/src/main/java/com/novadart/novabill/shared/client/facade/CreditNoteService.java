package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;


@XsrfProtect
@RemoteServiceRelativePath("creditnote.rpc")
public interface CreditNoteService extends RemoteService {

}
