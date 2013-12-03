package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InvoiceGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void get( java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.InvoiceDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void getAllInRange( java.lang.Long businessID, java.lang.Integer year, java.lang.Integer start, java.lang.Integer length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void getAllForClient( java.lang.Long clientID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.InvoiceDTO invoiceDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.InvoiceDTO invoiceDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void getNextInvoiceDocumentID( AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long clientID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void getAllForClientInRange( java.lang.Long clientID, java.lang.Integer year, java.lang.Integer start, java.lang.Integer length, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.InvoiceGwtService
     */
    void setPayed( java.lang.Long businessID, java.lang.Long clientID, java.lang.Long id, java.lang.Boolean value, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static InvoiceGwtServiceAsync instance;

        public static final InvoiceGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (InvoiceGwtServiceAsync) GWT.create( InvoiceGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
