package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PriceListGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PriceListGwtService
     */
    void getAll( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.PriceListDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PriceListGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.PriceListDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PriceListGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.PriceListDTO priceListDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PriceListGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.PriceListDTO priceListDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PriceListGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static PriceListGwtServiceAsync instance;

        public static final PriceListGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (PriceListGwtServiceAsync) GWT.create( PriceListGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
