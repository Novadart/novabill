package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PaymentTypeGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PaymentTypeGwtService
     */
    void getAll( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.PaymentTypeDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PaymentTypeGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.PaymentTypeDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PaymentTypeGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.PaymentTypeDTO paymentTypeDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PaymentTypeGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.PaymentTypeDTO paymentTypeDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.PaymentTypeGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static PaymentTypeGwtServiceAsync instance;

        public static final PaymentTypeGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (PaymentTypeGwtServiceAsync) GWT.create( PaymentTypeGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
