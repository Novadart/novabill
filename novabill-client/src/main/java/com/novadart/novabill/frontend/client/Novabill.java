package com.novadart.novabill.frontend.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.mvp.AppPlaceHistoryMapper;
import com.novadart.novabill.frontend.client.mvp.CenterActivityMapper;
import com.novadart.novabill.frontend.client.mvp.WestActivityMapper;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class Novabill implements EntryPoint {

	private Place defaultPlace = new HomePlace();
    
    public Novabill() {
	}

	public void onModuleLoad() {
		
		Configuration.init(new WrappedAsyncCallback<Void>() {
			
			@Override
			public void onSuccess(Void result) {
				
				ServerFacade.business.getStats(Configuration.getBusinessId(), new WrappedAsyncCallback<BusinessStatsDTO>() {

					@Override
					public void onSuccess(BusinessStatsDTO result) {
						if(result == null){
							Notification.showMessage(I18N.INSTANCE.errorLoadingAppConfiguration());
							return;
						}
						
						Configuration.setStats(result);
						
						MainWidget main = new MainWidget();
						
						// Create ClientFactory using deferred binding so we can replace with different
						// implementations in gwt.xml
						ClientFactory clientFactory = GWT.create(ClientFactory.class);
						EventBus eventBus = clientFactory.getEventBus();
						PlaceController placeController = clientFactory.getPlaceController();

						// Start ActivityManager for the main widget with our ActivityMapper
						ActivityMapper activityMapper = new CenterActivityMapper(clientFactory);
						ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
						activityManager.setDisplay(main.getCenterContainer());
						
						// Start ActivityManager for the main widget with our ActivityMapper
						activityMapper = new WestActivityMapper(clientFactory);
						activityManager = new ActivityManager(activityMapper, eventBus);
						activityManager.setDisplay(main.getWestContainer());

						// Start PlaceHistoryHandler with our PlaceHistoryMapper
						AppPlaceHistoryMapper historyMapper= GWT.create(AppPlaceHistoryMapper.class);
						PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
						historyHandler.register(placeController, eventBus, defaultPlace);
						
						main.setPlaceController(placeController);
						main.setEventBus(clientFactory.getEventBus());
						RootLayoutPanel.get().add(main);
						// Goes to place represented on URL or default place
						historyHandler.handleCurrentHistory();
						
					}

					@Override
					public void onException(Throwable caught) {
						Notification.showMessage(I18N.INSTANCE.errorLoadingAppConfiguration());
					}
				});
				
			}
			
			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorLoadingAppConfiguration());
			}
		});
		
	}
}
