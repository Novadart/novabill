package com.novadart.novabill.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;


@Service
public class TempService {

	private boolean run = false;
	
	private PaymentType[] paymentTypes = new PaymentType[]{
			new PaymentType("Rimessa Diretta", "Pagamento in Rimessa Diretta", PaymentDateType.IMMEDIATE, 0),
			new PaymentType("Bonifico Bancario 30GG", "Pagamento con bonifico bancario entro 30 giorni", PaymentDateType.IMMEDIATE, 30),
			new PaymentType("Bonifico Bancario 60GG", "Pagamento con bonifico bancario entro 60 giorni", PaymentDateType.IMMEDIATE, 60),
			new PaymentType("Bonifico Bancario 90GG", "Pagamento con bonifico bancario entro 90 giorni", PaymentDateType.IMMEDIATE, 90),
			new PaymentType("Bonifico Bancario 30GG d.f. f.m.", "Pagamento con bonifico bancario entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 30),
			new PaymentType("Bonifico Bancario 60GG d.f. f.m.", "Pagamento con bonifico bancario entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 60),
			new PaymentType("Bonifico Bancario 90GG d.f. f.m.", "Pagamento con bonifico bancario entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 90),
			new PaymentType("Ri.Ba. 30GG", "Pagamento tramite ricevuta bancaria entro 30 giorni", PaymentDateType.IMMEDIATE, 30),
			new PaymentType("Ri.Ba. 60GG", "Pagamento tramite ricevuta bancaria entro 60 giorni", PaymentDateType.IMMEDIATE, 60),
			new PaymentType("Ri.Ba. 90GG", "Pagamento tramite ricevuta bancaria entro 90 giorni", PaymentDateType.IMMEDIATE, 90),
			new PaymentType("Ri.Ba. 30GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 30 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 30),
			new PaymentType("Ri.Ba. 60GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 60 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 60),
			new PaymentType("Ri.Ba. 90GG d.f. f.m.", "Pagamento tramite ricevuta bancaria entro 90 giorni d.f. f.m.", PaymentDateType.END_OF_MONTH, 90)	
	};
	
	private void addPayments() throws CloneNotSupportedException{
		for(Business business: Business.findAllBusinesses()){
			for(PaymentType pType: paymentTypes){
				PaymentType paymentType = pType.clone();
				paymentType.setBusiness(business);
				business.getPaymentTypes().add(paymentType);
				business.merge();
			}
		}
	}
	
	private void updateInvoicePaymentInfo(){
		for(Invoice inv: Invoice.findAllInvoices()){
			if(inv.getId() == 35){ //0
				inv.setPaymentTypeName("Rimessa Diretta");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(0);
			}else if(inv.getId() == 42){ //3
				inv.setPaymentTypeName("Bonifico Bancario 60GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(60);
			}else if(inv.getId() == 44){//3
				inv.setPaymentTypeName("Bonifico Bancario 60GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(60);
			}else if(inv.getId() == 46){//3
				inv.setPaymentTypeName("Bonifico Bancario 60GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(60);
			}else if(inv.getId() == 52){//10
				inv.setPaymentTypeName("Ri.Ba. 90GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(90);
			}else if(inv.getId() == 58){//0
				inv.setPaymentTypeName("Rimessa Diretta");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(0);
			}else if(inv.getId() == 65){//2
				inv.setPaymentTypeName("Bonifico Bancario 30GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(30);
			}else if(inv.getId() == 73){//2
				inv.setPaymentTypeName("Bonifico Bancario 30GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(30);
			}else if(inv.getId() == 77){//4
				inv.setPaymentTypeName("Bonifico Bancario 90GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(90);
			}else if(inv.getId() == 93){//0
				inv.setPaymentTypeName("Rimessa Diretta");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(0);
			}else if(inv.getId() == 95){//2
				inv.setPaymentTypeName("Bonifico Bancario 30GG");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(30);
			}else if(inv.getId() == 102){//0
				inv.setPaymentTypeName("Rimessa Diretta");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(0);
			}else if(inv.getId() == 110){//0
				inv.setPaymentTypeName("Rimessa Diretta");
				inv.setPaymentDateGenerator(PaymentDateType.IMMEDIATE);
				inv.setPaymentDateDelta(0);
			}
			inv.merge();
		}
	}
	
	@Scheduled(fixedDelay = 10000)
	@Transactional(readOnly = false)
	public void update() throws CloneNotSupportedException{
		if(!run){
			System.out.println("Runnint temp bean task...");
			addPayments();
			updateInvoicePaymentInfo();
			System.out.println("Finished...");
			run = true;
		}else
			System.out.println("Skipping...");
		
	}
	
}
