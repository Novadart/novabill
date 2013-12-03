package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CreditNoteGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.CreditNoteDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void getAllInRange( java.lang.Long businessID, java.lang.Integer year, java.lang.Integer start, java.lang.Integer length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.CreditNoteDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void getAllForClient( java.lang.Long clientID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.CreditNoteDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void getAllForClientInRange( java.lang.Long id, java.lang.Integer year, int start, int length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.CreditNoteDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.CreditNoteDTO creditNoteDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long clientID, java.lang.Long creditNoteID, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.CreditNoteDTO creditNoteDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CreditNoteGwtService
     */
    void getNextCreditNoteDocumentID( AsyncCallback<java.lang.Long> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static CreditNoteGwtServiceAsync instance;

        public static final CreditNoteGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (CreditNoteGwtServiceAsync) GWT.create( CreditNoteGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
