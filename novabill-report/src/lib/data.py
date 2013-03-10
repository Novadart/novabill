# coding: utf-8
"""
    BUSINESS DATA
"""
class BusinessData(object):
    
    def __init__(self, jsonData):
        self.__data = jsonData
        
    def getAddress(self):
        return self.__data["address"]
    
    def getCity(self):
        return self.__data["city"]
    
    def getCountry(self):
        return self.__data["country"]
    
    def getCreationTime(self):
        return self.__data["creationTime"]
    
    def getEmail(self):
        return self.__data["email"]
    
    def getFax(self):
        return self.__data["fax"]
    
    def getId(self):
        return self.__data["id"]
    
    def getMobile(self):
        return self.__data["mobile"]
    
    def getName(self):
        return self.__data["name"]
    
    def getPhone(self):
        return self.__data["phone"]
    
    def getPostcode(self):
        return self.__data["postcode"]
    
    def getProvince(self):
        return self.__data["province"]
    
    def getSSN(self):
        return self.__data["ssn"]
    
    def getVatID(self):
        return self.__data["vatID"]
    
    def getVersion(self):
        return self.__data["version"]
    
    def getWeb(self):
        return self.__data["web"]
    
"""
    CLIENT DATA
"""
class ClientData(object):
    
    def __init__(self, jsonData):
        self.__data = jsonData
        
    def getAddress(self):
        return self.__data["address"]
    
    def getCity(self):
        return self.__data["city"]
    
    def getCountry(self):
        return self.__data["country"]
    
    def getEmail(self):
        return self.__data["email"]
    
    def getFax(self):
        return self.__data["fax"]
    
    def getId(self):
        return self.__data["id"]
    
    def getMobile(self):
        return self.__data["mobile"]
    
    def getName(self):
        return self.__data["name"]
    
    def getPhone(self):
        return self.__data["phone"]
    
    def getPostcode(self):
        return self.__data["postcode"]
    
    def getProvince(self):
        return self.__data["province"]
    
    def getSSN(self):
        return self.__data["ssn"]
    
    def getVatID(self):
        return self.__data["vatID"]
    
    def getVersion(self):
        return self.__data["version"]
    
    def getWeb(self):
        return self.__data["web"]

"""
    ACCOUNTING DOCUMENT ITEM DATA
"""
class AccountingDocumentItem(object):
    
    def __init__(self, jsonData):
        self.__data = jsonData
        
    def getCreationTime(self):
        return self.__data["creationTime"]
    
    def getDescription(self):
        return self.__data["description"]
    
    def getId(self):
        return self.__data["id"]
    
    def getPrice(self):
        return self.__data["price"]
    
    def getQuantity(self):
        return self.__data["quantity"]
    
    def getTax(self):
        return self.__data["tax"]
    
    def getTotal(self):
        return self.__data["total"]
    
    def getTotalBeforeTax(self):
        return self.__data["totalBeforeTax"]
    
    def getTotalTax(self):
        return self.__data["totalTax"]
    
    def getUnitOfMeasure(self):
        return self.__data["unitOfMeasure"]
    
    def getVersion(self):
        return self.__data["version"]
    

"""
    ACCOUNTING DOCUMENT DATA
"""
class AccountingDocumentData(object):
    
    def __init__(self, jsonData):
        self.__data = jsonData
    
    def getBusiness(self):
        return BusinessData(self.__data["business"])
    
    def getClient(self):
        return ClientData(self.__data["client"])
    
    def getAccountingDocumentItems(self):
        items = []
        for item in self.__data["accountingDocumentItems"]:
            items.append(AccountingDocumentItem(item))
        return items
    
    def getAccountingDocumentDate(self):
        return self.__data["accountingDocumentDate"]
    
    def getId(self):
        return self.__data["id"]
    
    def getAccountingDocumentID(self):
        return self.__data["documentID"]

    def getAccountingDocumentYear(self):
        return self.__data["accountingDocumentYear"]
    
    def getTotal(self):
        return self.__data["total"]
    
    def getTotalBeforeTax(self):
        return self.__data["totalBeforeTax"]
    
    def getTotalTax(self):
        return self.__data["totalTax"]
    
    def getNote(self):
        return self.__data["note"]
    
    def getPaymentNote(self):
        return self.__data["paymentNote"]
    
    def getVersion(self):
        return self.__data["version"]

"""
    ESTIMATION DATA
"""
class EstimationData(AccountingDocumentData):
    
    def __init__(self, jsonData):
        super(EstimationData, self).__init__(jsonData)
        self.__data = jsonData
        
    def getLimitations(self):
        return self.__data["limitations"]
    
    def getValidTill(self):
        return self.__data["validTill"]
    

"""
    CREDIT NOTE DATA
"""
class CreditNoteData(AccountingDocumentData):
    
    def __init__(self, jsonData):
        super(CreditNoteData, self).__init__(jsonData)
        self.__data = jsonData

        
"""
    ABSTRACT INVOICE DATA
"""
class AbstractInvoiceData(AccountingDocumentData):
    
    def __init__(self, jsonData):
        super(AbstractInvoiceData, self).__init__(jsonData)
        self.__data = jsonData
    
    def getPaymentDueDate(self):
        return self.__data["paymentDueDate"]
    
    def getPaymentType(self):
        return self.__data["paymentType"]
    
    def getHumanReadablePaymentType(self):
        """
        public enum PaymentType {
		    CASH,
    
            BANK_TRANSFER,
            BANK_TRANSFER_30,
            BANK_TRANSFER_60,
            BANK_TRANSFER_90,
            BANK_TRANSFER_120,
            BANK_TRANSFER_150,
            BANK_TRANSFER_180,
            
            RIBA_30,
            RIBA_60,
            RIBA_90
		}
        """
        pt = self.getPaymentType()
        if(pt == "CASH"): return "Rimessa Diretta"
        
        elif(pt == "BANK_TRANSFER"): return "Bonifico Bancario"
        elif(pt == "BANK_TRANSFER_30"): return "Bonifico Bancario 30GG"
        elif(pt == "BANK_TRANSFER_60"): return "Bonifico Bancario 60GG"
        elif(pt == "BANK_TRANSFER_90"): return "Bonifico Bancario 90GG"
        elif(pt == "BANK_TRANSFER_120"): return "Bonifico Bancario 120GG"
        elif(pt == "BANK_TRANSFER_150"): return "Bonifico Bancario 150GG"
        elif(pt == "BANK_TRANSFER_180"): return "Bonifico Bancario 180GG"
        
        elif(pt == "RIBA_30"): return "Ri.Ba. 30GG"
        elif(pt == "RIBA_60"): return "Ri.Ba. 60GG"
        elif(pt == "RIBA_90"): return "Ri.Ba. 90GG"
        
        else: return ""

"""
    INVOICE DATA
"""
class InvoiceData(AbstractInvoiceData):
    
    def __init__(self, jsonData):
        super(InvoiceData, self).__init__(jsonData)
        self.__data = jsonData
        
"""
    ADDRESS DATA
"""
class EndpointData(object):
    
    def __init__(self, jsonData):
        self.__data = jsonData
        
    def getCompanyName(self):
        return self.__data["companyName"]
        
    def getStreet(self):
        return self.__data["street"]
    
    def getPostcode(self):
        return self.__data["postcode"]
    
    def getCity(self):
        return self.__data["city"]
    
    def getProvince(self):
        return self.__data["province"]
    
    def getCountry(self):
        return self.__data["country"]


"""
    TRANSPORT DOCUMENT DATA
"""      
class TransportDocumentData(AbstractInvoiceData):
    
    def __init__(self, jsonData):
        super(TransportDocumentData, self).__init__(jsonData)
        self.__data = jsonData
        
    def getNumberOfPackages(self):
        return self.__data["numberOfPackages"]
    
    def getFromLocation(self):
        return EndpointData(self.__data["fromEndpoint"])
    
    def getToLocation(self):
        return EndpointData(self.__data["toEndpoint"])
    
    def getTransporter(self):
        return self.__data["transporter"]
    
    def getTransportationResponsibility(self):
        return self.__data["transportationResponsibility"]
    
    def getTradeZone(self):
        return self.__data["tradeZone"]
    