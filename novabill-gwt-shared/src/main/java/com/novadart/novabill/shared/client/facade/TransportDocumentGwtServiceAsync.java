package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TransportDocumentGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.TransportDocumentDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void getAllForClient( java.lang.Long clientID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.TransportDocumentDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.TransportDocumentDTO transportDocDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long clientID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.TransportDocumentDTO transportDocDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void getNextTransportDocId( AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void getAllForClientInRange( java.lang.Long clientID, java.lang.Integer year, java.lang.Integer start, java.lang.Integer length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.TransportDocumentDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.TransportDocumentGwtService
     */
    void getAllInRange( java.lang.Long businessID, java.lang.Integer year, java.lang.Integer start, java.lang.Integer length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.TransportDocumentDTO>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static TransportDocumentGwtServiceAsync instance;

        public static final TransportDocumentGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (TransportDocumentGwtServiceAsync) GWT.create( TransportDocumentGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
