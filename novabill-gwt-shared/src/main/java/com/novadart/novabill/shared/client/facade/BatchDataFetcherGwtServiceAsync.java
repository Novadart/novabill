package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BatchDataFetcherGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewInvoiceForClientOpData( java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO,com.novadart.novabill.shared.client.dto.PaymentTypeDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewInvoiceFromEstimationOpData( java.lang.Long estimationID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.EstimationDTO,com.novadart.novabill.shared.client.dto.PaymentTypeDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewInvoiceFromTransportDocumentOpData( java.lang.Long transportDocumentID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.TransportDocumentDTO,com.novadart.novabill.shared.client.dto.PaymentTypeDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchCloneInvoiceOpData( java.lang.Long invoiceID, java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO,com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewEstimationForClientOpData( java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Pair<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchCloneEstimationOpData( java.lang.Long estimationID, java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO,com.novadart.novabill.shared.client.dto.EstimationDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewTransportDocumentForClientOpData( java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Pair<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchCloneTransportDocumentOpData( java.lang.Long transportDocID, java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Triple<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO,com.novadart.novabill.shared.client.dto.TransportDocumentDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewCreditNoteForClientOpData( java.lang.Long clientID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Pair<java.lang.Long,com.novadart.novabill.shared.client.dto.ClientDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BatchDataFetcherGwtService
     */
    void fetchNewCreditNoteFromInvoiceOpData( java.lang.Long invoiceID, AsyncCallback<com.novadart.novabill.shared.client.tuple.Pair<java.lang.Long,com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static BatchDataFetcherGwtServiceAsync instance;

        public static final BatchDataFetcherGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (BatchDataFetcherGwtServiceAsync) GWT.create( BatchDataFetcherGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
