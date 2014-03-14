package com.novadart.novabill.frontend.client.i18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;

public interface I18N extends Constants {
	public static final I18N INSTANCE = GWT.create(I18N.class);
	
	public String legalAddress();
	public String selectAddress();
	public String additionalClientAddresses();
	public String mainClientData();
	public String commercialMonths();
	public String appearanceOfTheGoods();
	public String text();
	public String transportDocumentShort();
	public String paymentDelay2();
	public String incompleteClientAlert();
	public String pdfOptions();
	public String incognitoEnabled();
	public String weight();
	public String totalWeight();
	public String lockedItemsTableAlert(); 
	public String unlockItemsTable();
	public String moveUp();
	public String moveDown();
	public String discountInDocsExplicit();
	public String transportDocumentCauseDefaultText();
	public String discountLabel();
	public String discount();
	public String loadCommodity();
	public String defaultPriceList(); 
	public String transportDocument();
	public String items();
	public String errorAddAtLeastOneItem();
	public String other();
	public String textOnlyAccountingItem();
	public String makePaymentAsDefault();
	public String mandatory();
	public String decimalSeparatorInfo();
	public String immediate();
	public String addPayment();
	public String noPaymentSelected();
	public String overridePaymentNoteQuestion();
	public String changePayment();
	public String deletionConfirm();
	public String settingsGeneral();
	public String settingsPayments();
	public String createPaymentType();
	public String days();
	public String dateGeneration();
	public String dateGenerationImmediate();
	public String dateGenerationEndOfMonth();
	public String dateGenerationManual();
	public String paymentDelay();
	public String tipselectClientDialog1();
	public String tipselectClientDialog2();
	public String deleteAccountLabel();
	public String deleteAccount();
	public String yes();
	public String no();
	public String download();
	public String errorDataAccessException();
	public String updateLogo();
	public String deleteLogo();
	public String feedback(); 
	public String yourDocs(); 
	public String fillVatIdOrSsn();
	public String limitations();
	public String validTill();
	public String contact();
	public String name();
	public String surname();
	public String clientData();
	public String clientContactData();
	public String addNewClientTitle();
	public String modifyClientTitle();
	public String selectClient();
	public String helpClearFilter();
	public String helpSearchClient();
	public String productName();
	public String errorLogoIllegalFile(); 
	public String errorLogoSizeTooBig();
	public String errorLogoIllegalRequest();
	public String errorLogoNotYetUploaded();
	public String cancelModificationsConfirmation();
	public String missingVatIdAndSSNValidationError();
	public String notEmptyValidationError();
	public String notEmptyDateValidationError();
	public String vatidValidationError();
	public String ssnValidationError();
	public String submit();
	public String username();
	public String password();
	public String errorClientCancelation();
	public String confirmClientDeletion();
	public String cancelClient();
	public String modifyClient();
	public String invoiceIsReadonly();
	public String saveModifications();
	public String saveModificationsConfirm();
	public String modifyInvoice();
	public String modifyCreditNote();
	public String modifyEstimation();
	public String errorSessionExpired();
	public String openInvoice();
	public String errorServerCommunication();
	public String confirmInvoiceDeletion();
	public String confirmEstimationDeletion();
	public String confirmCreditNoteDeletion();
	public String errorClientData();
	public String cancel();
	public String createClient();
	public String ssn();
	public String web();
	public String email();
	public String companyEmail();
	public String fax();
	public String mobile();
	public String phone();
	public String postcode();
	public String country();
	public String province();
	public String city();
	public String address();
	public String companyName();
	public String note();
	public String paymentNote();
	public String errorLoadingAppConfiguration();
	public String invoiceCreationFailure();
	public String invoiceCreationSuccess();
	public String creditNoteCreationFailure();
	public String creditNoteCreationSuccess();
	public String estimationCreationSuccess();
	public String estimationCreationFailure();
	public String invoiceUpdateFailure();
	public String invoiceUpdateSuccess();
	public String estimationUpdateFailure();
	public String estimationUpdateSuccess();
	public String creditNoteUpdateFailure();
	public String creditNoteUpdateSuccess();
	public String errorDocumentData();
	public String createInvoice();
	public String clients();
	public String newClient();
	public String home();
	public String logout();
	public String newInvoice();
	public String totalClients();
	public String totalInvoicing();
	public String totalInvoices();
	public String welcome();
	public String new_();
	public String modify();
	public String remove();
	public String impossibleToLoadInvoice();
	public String pleaseSelectInvoice();
	public String client();
	public String addNewClient();
	public String date();
	public String invoiceNumber();
	public String estimationNumber();
	public String dueDate();
	public String payment();
	public String defaultPaymentType();
	public String[] vatItems();
	public String nameDescription();
	public String quantity();
	public String price();
	public String unityOfMeasure();
	public String unityOfMeasureExtended();
	public String vat();
	public String totalBeforeTaxesForItem();
	public String totalTaxForItem();
	public String totalAfterTaxesForItem();
	public String totalBeforeTaxes();
	public String totalTax();
	public String totalAfterTaxes();
	public String delete();
	public String add();
	public String vatID();
	public String bootstrapWelcomeMessage();
	public String numberValidationError();
	public String postcodeValidationError();
	public String emailValidationError();
	public String ssnOrVatIdValidationError();
	public String invoices();
	public String estimates();
	public String convertToInvoice();
	public String newEstimation();
	public String openEstimation();
	public String noClientsFound();
	public String myData();
	public String open();
	public String payed();
	public String notPayed();
	public String companyDataLabel();
	public String logoLabel();
	public String exportDataLabel();
	public String exportClientData();
	public String exportInvoiceData();
	public String exportEstimationData();
	public String exportCreditNoteData();
	public String exportTransportDocumentData();
	public String clone();
	public String showMore();
	public String welcomeMessage1();
	public String welcomeMessage2();
	public String welcomeMessage3();
	public String changePassword();
	public String creditNotes();
	public String creditNote();
	public String newCreditNote();
	public String newCreditNoteCreation();
	public String newTransportDocumentCreation();
	public String newInvoiceCreation();
	public String newEstimationCreation();
	public String modifyTransportDocument();
	public String creditNoteNumber();
	public String confirmTransportDocumentDeletion();
	public String transportDocumentCreationSuccess();
	public String transportDocumentCreationFailure();
	public String transportDocumentUpdateFailure();
	public String transportDocumentUpdateSuccess();
	public String transportDocuments();
	public String transportDocumentsTab();
	public String newTransportDocument();
	public String fromAddress();
	public String toAddress();
	public String loadFromAddress();
	public String loadToAddress();
	public String tradeZone();
	public String transportationResponsibility();
	public String transporter();
	public String numberOfPackages();
	public String cause();
	public String transportStartDate();
	public String time();
	public String transportDocumentNumber();
	public String welcomeMessageClients();
	public String welcomeMessageDocuments();
	public String sessionExpiredError();
	public String sessionExpiredError2();
	public String reloadPage();
	public String alternativeSsnOrVatIdValidationError();
	public String tipClientView1();
	public String tipClientView2();
	public String tipInvoicePayment();
	public String tipItemInsertionForm();
	public String paymentDeletionConfirm();
	public String of();
	public String sku();
	public String example();
}
