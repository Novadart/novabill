package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EstimationGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.EstimationDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void getAllForClient( java.lang.Long clientID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.EstimationDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.EstimationDTO estimationDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long clientID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.EstimationDTO estimationDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void getNextEstimationId( AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void getAllForClientInRange( java.lang.Long clientID, java.lang.Integer year, int start, int length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.EstimationDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.EstimationGwtService
     */
    void getAllInRange( java.lang.Long businessID, java.lang.Integer year, int start, int length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.EstimationDTO>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static EstimationGwtServiceAsync instance;

        public static final EstimationGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (EstimationGwtServiceAsync) GWT.create( EstimationGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
