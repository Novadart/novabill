package com.novadart.novabill.shared.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommodityGwtServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void getAll( java.lang.Long businessID, AsyncCallback<java.util.List<com.novadart.novabill.shared.client.dto.CommodityDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void get( java.lang.Long businessID, java.lang.Long id, AsyncCallback<com.novadart.novabill.shared.client.dto.CommodityDTO> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void add( com.novadart.novabill.shared.client.dto.CommodityDTO paymentTypeDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void update( com.novadart.novabill.shared.client.dto.CommodityDTO paymentTypeDTO, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void remove( java.lang.Long businessID, java.lang.Long id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void searchCommodities( java.lang.Long businessID, java.lang.String query, int start, int offset, AsyncCallback<com.novadart.novabill.shared.client.dto.PageDTO<com.novadart.novabill.shared.client.dto.CommodityDTO>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void addOrUpdatePrice( java.lang.Long businessID, com.novadart.novabill.shared.client.dto.PriceDTO priceDTO, AsyncCallback<java.lang.Long> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see com.novadart.novabill.shared.client.facade.CommodityGwtService
     */
    void removePrice( java.lang.Long businessID, java.lang.Long priceListID, java.lang.Long commodityID, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static CommodityGwtServiceAsync instance;

        public static final CommodityGwtServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (CommodityGwtServiceAsync) GWT.create( CommodityGwtService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
