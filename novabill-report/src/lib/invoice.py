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
    INVOICE ITEM DATA
"""
class InvoiceItem(object):
    
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
    INVOICE DATA
"""
class InvoiceData(object):

    def __init__(self, jsonData):
        self.__data = jsonData
        
    def getBusiness(self):
        return BusinessData(self.__data["business"])
    
    def getClient(self):
        return ClientData(self.__data["client"])
    
    def getInvoiceItems(self):
        items = []
        for item in self.__data["invoiceItems"]:
            items.append(InvoiceItem(item))
        return items
    
    def getId(self):
        return self.__data["id"]
    
    def getInvoiceDate(self):
        return self.__data["invoiceDate"]
    
    def getInvoiceID(self):
        return self.__data["invoiceID"]
    
    def getInvoiceYear(self):
        return self.__data["invoiceYear"]
    
    def getNote(self):
        return self.__data["note"]
    
    def getPaymentDueDate(self):
        return self.__data["paymentDueDate"]
    
    def getPaymentNote(self):
        return self.__data["paymentNote"]
    
    def getPaymentType(self):
        return self.__data["paymentType"]
    
    def getHumanReadablePaymentType(self):
        """
        public enum PaymentType {
            CASH,
            
            BANK_TRANSFER,
            BANK_TRANSFER_30,
            BANK_TRANSFER_60,
            
            RIBA_30,
            RIBA_30_FM,
            RIBA_60,
            RIBA_60_FM,
            RIBA_90,
            RIBA_90_FM,
            RIBA_120,
            RIBA_120_FM,
            RIBA_150,
            RIBA_150_FM,
            RIBA_180,
            RIBA_180_FM
        }
        """
        pt = self.getPaymentType()
        if(pt == "CASH"): return "Rimessa Diretta"
        
        elif(pt == "BANK_TRANSFER"): return "Bonifico Bancario"
        elif(pt == "BANK_TRANSFER_30"): return "Bonifico Bancario 30GG"
        elif(pt == "BANK_TRANSFER_60"): return "Bonifico Bancario 60GG"
        
        elif(pt == "RIBA_30"): return "Ri.Ba 30GG"
        elif(pt == "RIBA_30_FM"): return "Ri.Ba 30GG fine mese"
        elif(pt == "RIBA_60"): return "Ri.Ba 60GG"
        elif(pt == "RIBA_60_FM"): return "Ri.Ba 60GG fine mese"
        elif(pt == "RIBA_90"): return "Ri.Ba 90GG"
        elif(pt == "RIBA_90_FM"): return "Ri.Ba 90GG fine mese"
        elif(pt == "RIBA_120"): return "Ri.Ba 120GG"
        elif(pt == "RIBA_120_FM"): return "Ri.Ba 120GG fine mese"
        elif(pt == "RIBA_150"): return "Ri.Ba 150GG"
        elif(pt == "RIBA_150_FM"): return "Ri.Ba 150GG fine mese"
        elif(pt == "RIBA_180"): return "Ri.Ba 180GG"
        elif(pt == "RIBA_180_FM"): return "Ri.Ba 180GG fine mese"
        
        else: return ""
    
    def getTotal(self):
        return self.__data["total"]
    
    def getTotalBeforeTax(self):
        return self.__data["totalBeforeTax"]
    
    def getTotalTax(self):
        return self.__data["totalTax"]
    
    def getVersion(self):
        return self.__data["version"]
    
        

class InvoiceTemplate(object):
    
    def __init__(self, jsonData, outputFilePath):
        self.__data = InvoiceData(jsonData)
        self.__outputFilePath = outputFilePath
        
    def _getData(self):
        return self.__data;
    
    def _getOutputFilePath(self):
        return self.__outputFilePath;
    
    def build(self):
        pass