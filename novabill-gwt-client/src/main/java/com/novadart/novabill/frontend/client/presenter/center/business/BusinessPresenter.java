package com.novadart.novabill.frontend.client.presenter.center.business;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.ExportUtils;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.business.BusinessView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.facade.LogoUploadStatus;

public class BusinessPresenter extends AbstractPresenter<BusinessView> implements BusinessView.Presenter {

	private boolean logoUpdateCompleted = true;
	
	public BusinessPresenter() {
	}
	
	public BusinessPresenter(PlaceController placeController, EventBus eventBus, BusinessView view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	protected void setPresenterInView(BusinessView view) {
		view.setPresenter(this);
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.INSTANCE.setStandardView();
		panel.setWidget(getView());
	}

	private boolean validate(){
		boolean validationOk = true;
		getView().getInlineNotification().hide();

		getView().getSsnOrVatIdValidation().validate();
		if(!getView().getSsnOrVatIdValidation().isValid()){
			getView().getInlineNotification().showMessage(getView().getSsnOrVatIdValidation().getErrorMessage());
			validationOk = false;
		}

		for (ValidatedTextBox v : new ValidatedTextBox[]{getView().getAddress(), getView().getCity(), 
				getView().getPostcode(), getView().getPhone(), getView().getEmail(), getView().getMobile(), getView().getFax(), getView().getWeb()}) {
			v.validate();
			validationOk = validationOk && v.isValid();
		}
		getView().getName().validate();
		validationOk = validationOk && getView().getName().isValid();
		getView().getProvince().validate();
		validationOk = validationOk && getView().getProvince().isValid();
		getView().getCountry().validate();
		validationOk = validationOk && getView().getCountry().isValid();
		return validationOk;
	}

	@Override
	public void onUpdateLogoClicked() {
		ServerFacade.INSTANCE.getBusinessService().generateLogoOpToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				getView().getFormPanel().setAction(ClientFactory.INSTANCE.getUpdateLogo()+result);
				getView().getUpdateLogo().setVisible(false);
				getView().getFormPanel().setVisible(true);
			}
		});
	}

	@Override
	public void onRemoveLogoClicked() {
		setLoaderInLogo();
		ServerFacade.INSTANCE.deleteLogo(new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				ClientFactory.INSTANCE.refeshLogoUrl();
				getView().getFormPanel().setVisible(false);
				getView().getFormPanel().reset();
				getView().getUpdateLogo().setVisible(true);
				getView().getLogo().setUrl(ClientFactory.INSTANCE.getLogoUrl());
			}

			@Override
			public void onFailure(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}

	@Override
	public void onSaveDataClicked() {
		if(!logoUpdateCompleted){
			Notification.showMessage(I18N.INSTANCE.errorLogoNotYetUploaded());
			return;
		}

		if(validate()){
			final BusinessDTO b = Configuration.getBusiness();
			b.setName(getView().getName().getText());
			b.setAddress(getView().getAddress().getText());
			b.setSsn(getView().getSsn().getText());
			b.setVatID(getView().getVatID().getText());
			b.setCity(getView().getCity().getText());
			b.setProvince(getView().getProvince().getSelectedItemText());
			b.setCountry(getView().getCountry().getSelectedItemValue());
			b.setPostcode(getView().getPostcode().getText());
			b.setPhone(getView().getPhone().getText());
			b.setEmail(getView().getEmail().getText());
			b.setMobile(getView().getMobile().getText());
			b.setFax(getView().getFax().getText());
			b.setWeb(getView().getWeb().getText());
			b.setPriceDisplayInDocsMonolithic(!getView().getDiscountInDocsExplicit().getValue());
			b.setEnabledIncognito(getView().getIncognitoEnabled().getValue());
			
			getView().getSaveData().showLoader(true);
			getView().setLocked(true);

			ServerFacade.INSTANCE.getBusinessService().update(b, new ManagedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					getView().getSaveData().showLoader(true);
					Configuration.setBusiness(b);
					goTo(new HomePlace());
//					getEventBus().fireEvent(new BusinessUpdateEvent(b));
					getView().setLocked(false);
					Window.Location.reload();
				}

				@Override
				public void onFailure(Throwable caught) {
					getView().getSaveData().showLoader(true);
					super.onFailure(caught);
					getView().setLocked(false);
				}
			});

		} 
	}

	public void onLoad() {
		BusinessDTO b = Configuration.getBusiness();

		getView().getName().setText(b.getName());
		getView().getSsn().setText(b.getSsn());
		getView().getVatID().setText(b.getVatID());
		getView().getAddress().setText(b.getAddress());
		getView().getCity().setText(b.getCity());
		getView().getProvince().setSelectedItem(b.getProvince());
		getView().getCountry().setSelectedItemByValue(b.getCountry());
		getView().getPostcode().setText(b.getPostcode());
		getView().getPhone().setText(b.getPhone());
		getView().getEmail().setText(b.getEmail());
		getView().getMobile().setText(b.getMobile());
		getView().getFax().setText(b.getFax());
		getView().getWeb().setText(b.getWeb());
		getView().getDiscountInDocsExplicit().setValue(!b.isPriceDisplayInDocsMonolithic());
		getView().getIncognitoEnabled().setValue(b.isIncognitoEnabled());
	}

	@Override
	public void onExportClientDataClicked() {
		ExportUtils.exportData(true, false, false, false, false);
	}

	@Override
	public void onExportInvoiceDataClicked() {
		ExportUtils.exportData(false, true, false, false, false);
	}

	@Override
	public void onExportEstimationDataClicked() {
		ExportUtils.exportData(false, false, true, false, false);
	}

	@Override
	public void onExportCreditNoteDataClicked() {
		ExportUtils.exportData(false, false, false, true, false);
	}

	@Override
	public void onExportTransportDocumentDataClicked() {
		ExportUtils.exportData(false, false, false, false, true);
	}

	@Override
	public void onLogoSubmit() {
		setLoaderInLogo();
		logoUpdateCompleted = false;
	}

	@Override
	public void onLogoSubmitComplete(String resultCode) {
		// for some reason Internet Explorer wraps the number in <pre></pre>, thus instead of 0 you get <pre>0</pre>
		if(resultCode.toLowerCase().contains("<pre>")){
			resultCode = String.valueOf(resultCode.charAt(5));
		}

		int code = Integer.parseInt(resultCode);
		
		LogoUploadStatus status = LogoUploadStatus.values()[code];
		switch(status){
		case ILLEGAL_PAYLOAD:
			Notification.showMessage(I18N.INSTANCE.errorLogoIllegalFile());

		case ILLEGAL_SIZE:
			Notification.showMessage(I18N.INSTANCE.errorLogoSizeTooBig());

		default:
		case ILLEGAL_REQUEST:
		case INTERNAL_ERROR:
			Notification.showMessage(I18N.INSTANCE.errorLogoIllegalRequest());
			getView().getLogo().setUrl(ClientFactory.INSTANCE.getLogoUrl());
			getView().getFormPanel().setVisible(false);
			getView().getFormPanel().reset();
			getView().getUpdateLogo().setVisible(true);
			break;

		case OK:
			ClientFactory.INSTANCE.refeshLogoUrl();
			getView().getLogo().setUrl(ClientFactory.INSTANCE.getLogoUrl());
			getView().getFormPanel().setVisible(false);
			getView().getFormPanel().reset();
			getView().getUpdateLogo().setVisible(true);
			break;

		}

		logoUpdateCompleted = true;
	}
	
	private void setLoaderInLogo() {
		getView().getLogo().setUrl(ImageResources.INSTANCE.largeLoader().getSafeUri());
	}

}
