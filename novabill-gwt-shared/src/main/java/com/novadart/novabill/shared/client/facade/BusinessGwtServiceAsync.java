package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BusinessGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void countClients( java.lang.Long businessID, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void countInvoicesForYear( java.lang.Long BusinessID, java.lang.Integer year, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getTotalAfterTaxesForYear( java.lang.Long businessID, java.lang.Integer year, AsyncCallback<java.math.BigDecimal> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getStats( java.lang.Long businessID, AsyncCallback<com.novadart.novabill.shared.client.dto.BusinessStatsDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.BusinessDTO businessDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void generatePDFToken( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void generateExportToken( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getInvoices( java.lang.Long businessID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.InvoiceDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getCreditNotes( java.lang.Long businessID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.CreditNoteDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getEstimations( java.lang.Long businessID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.EstimationDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getTransportDocuments( java.lang.Long businessID, java.lang.Integer year, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.TransportDocumentDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getClients( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.ClientDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getCommodities( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.CommodityDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void getPaymentTypes( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.PaymentTypeDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void get( java.lang.Long businessID, AsyncCallback<com.novadart.novabill.shared.client.dto.BusinessDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void updateNotesBitMask( java.lang.Long notesBitMask, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void generateLogoOpToken( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.BusinessGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.BusinessDTO businessDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static BusinessGwtServiceAsync instance;

        public static final BusinessGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (BusinessGwtServiceAsync) GWT.create( BusinessGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
