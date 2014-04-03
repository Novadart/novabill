package com.novadart.novabill.test.suite;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.BankAccount;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.ClientAddress;
import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.domain.Transporter;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:empty-context.xml")
@Transactional
@ActiveProfiles("dev")
public class ToStringMethodsTest {

	@Test
	public void businessToStringTest(){
		Business business = TestUtils.createBusiness();
		business.setId(1l);
		assertEquals(String.format("<id: %d, name: %s>", 1l, business.getName()), business.toString());
	}

	@Test
	public void bankAccountToStringTest(){
		BankAccount bankAccount = new BankAccount();
		bankAccount.setIban("123456");
		bankAccount.setName("Test bank account");
		bankAccount.setId(1l);
		assertEquals(String.format("<id: %d, iban: %s, name: %s>", 1l, "123456", "Test bank account"), bankAccount.toString());
	}
	
	@Test
	public void accountingDocToStringTest() throws InstantiationException, IllegalAccessException{
		Invoice invoice = TestUtils.createInvOrCredNote(1l, Invoice.class);
		invoice.setId(1l);
		assertEquals(String.format("<id: %d, documentID: %d, type: %s>", 1l, 1l, "Invoice"), invoice.toString());
	}
	
	@Test
	public void accountingDocItemToStringTest(){
		AccountingDocumentItem item = new AccountingDocumentItem();
		item.setId(1l);
		item.setSku("1234");
		assertEquals(String.format("<id: %d, sku: %s>", 1l, Strings.nullToEmpty("1234")), item.toString());
	}
	
	@Test
	public void clientToStringTest(){
		Client client = TestUtils.createClient();
		client.setId(1l);
		assertEquals(String.format("<id: %d, name: %s>", 1l, client.getName()), client.toString());
	}
	
	@Test
	public void clientAddressToStringTest(){
		ClientAddress cAddress = TestUtils.createClientAddress();
		cAddress.setId(1l);
		assertEquals(String.format("<id: %d, name: %s>", 1l, cAddress.getName()), cAddress.toString());
	}
	
	@Test
	public void commodityToStringTest(){
		Commodity commmodity = TestUtils.createCommodity();
		commmodity.setId(1l);
		assertEquals(String.format("<id: %d, sku: %s>", 1l, commmodity.getSku()), commmodity.toString());
	}
	
	@Test
	public void paymentTypeToStringTest(){
		PaymentType paymentType = TestUtils.createPaymentType();
		paymentType.setId(1l);
		assertEquals(String.format("<id: %d, name: %s>", 1l, paymentType.getName()), paymentType.toString());
	}
	
	@Test
	public void priceListToStringTest(){
		PriceList priceList = TestUtils.createPriceList();
		priceList.setId(1l);
		assertEquals(String.format("<id: %d, name: %s>", 1l, priceList.getName()), priceList.toString());
	}
	
	@Test
	public void transporterToStringTest(){
		Transporter transporter = new Transporter();
		transporter.setId(1l);
		transporter.setDescription("Test desc.");
		assertEquals(String.format("<id: %d, desc: %s>", 1l, transporter.getDescription()), transporter.toString());
	}
	
	@Test
	public void principalToStringTest(){
		Principal principal = new Principal();
		principal.setUsername("giordano.battilana@novadart.com");
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_PREMIUM);
		assertEquals(String.format("<username: %s, roles: %s>", "giordano.battilana@novadart.com", "[ROLE_BUSINESS_PREMIUM]"), principal.toString());
	}
	
}
