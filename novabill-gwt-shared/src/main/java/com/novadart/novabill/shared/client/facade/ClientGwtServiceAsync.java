package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.ClientGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.ClientGwtService
     */
    void add( java.lang.Long businessID, com.novadart.novabill.shared.client.dto.ClientDTO clientDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.ClientGwtService
     */
    void update( java.lang.Long businessID, com.novadart.novabill.shared.client.dto.ClientDTO clientDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.ClientGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.ClientDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.ClientGwtService
     */
    void searchClients( java.lang.Long businessID, java.lang.String query, int start, int offset, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.ClientDTO>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ClientGwtServiceAsync instance;

        public static final ClientGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ClientGwtServiceAsync) GWT.create( ClientGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
