# coding: utf-8
from lib.data import InvoiceData
from os import remove
from os.path import exists
from template.core import DocumentType, DirectorType, BuilderType
from template.default import DefaultDirector
from template.default.credit_note import DefaultCreditNoteBuilder
from template.default.estimation import DefaultEstimationBuilder
from template.default.transport_document import DefaultTransportDocumentBuilder
from template.tidy import TidyDirector
from template.tidy.invoice import TidyInvoiceBuilder

class Factory(object):
    
    @classmethod
    def createDirector(cls, dirType, *args, **kw):
        if(dirType == DirectorType.DEFAULT):
            return TidyDirector(*args, **kw)
        raise Exception("No such layout type!")
    
    @classmethod
    def createBuilder(cls, buildType, docType, *args, **kw):
        if buildType == BuilderType.DEFAULT:
            if(docType == DocumentType.INVOICE):
                return TidyInvoiceBuilder(*args, **kw)
            if(docType == DocumentType.ESTIMATION):
                return DefaultEstimationBuilder(*args, **kw)
            if(docType == DocumentType.CREDIT_NOTE):
                return DefaultCreditNoteBuilder(*args, **kw)
            if(docType == DocumentType.TRANSPORT_DOCUMENT):
                return DefaultTransportDocumentBuilder(*args, **kw)
        raise Exception("No such builder type!")



def create_doc(out, invoice, pathToLogo=None, logoWidth=None, logoHeight=None, docType=DocumentType.INVOICE, \
               tempType=DirectorType.DEFAULT, watermark=True):
    builderDisplayParams = dict(logo=dict(path=pathToLogo, width=logoWidth, height=logoWidth))
    builder = Factory.createBuilder(BuilderType.DEFAULT, docType, out, dispParams=builderDisplayParams)
    directorDisplayParams = dict(pagenumbers=True, watermark=watermark)
    director = Factory.createDirector(tempType, builder, InvoiceData(invoice), dispParams=directorDisplayParams) 
    director.construct()
        
        
if __name__ == '__main__':
    testInvoiceJSON  = """
        {"accountingDocumentDate":"19/09/2012","accountingDocumentItems":[{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"1","id":8,"price":"1.00","quantity":"1.00","tax":"21.00","total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","unitOfMeasure":"1","version":0}],"accountingDocumentYear":2012,"business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1348089409855,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":2,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"class":"com.novadart.novabill.domain.Invoice","client":{"address":"via Vezzi, 12","business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1348089409855,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":2,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"city":"Nervesa della Battaglia","class":"com.novadart.novabill.domain.Client","country":"Italia","email":"","fax":"","id":2,"mobile":"","name":"General Alarm Eredi sas di Borsato Andrea","phone":"","postcode":"31040","province":"TV","ssn":"","vatID":"04085780262","version":2,"web":""},"documentID":1,"id":7,"note":"1","payed":false,"paymentDueDate":null,"paymentNote":null,"paymentType":null,"total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","version":0}
    """
    outputFile = "/tmp/testInvoice.pdf"
    if exists(outputFile):
        remove(outputFile)
    import json
    create_doc("/tmp/testInvoice.pdf", json.loads(testInvoiceJSON))
    
    
