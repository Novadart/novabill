# coding: utf-8
from lib.data import InvoiceData, EstimationData, CreditNoteData,\
    TransportDocumentData
from os import remove
from os.path import exists
from template.core import DocumentType, DirectorType, BuilderType
from template.tidy import TidyDirector
from template.tidy.invoice import TidyInvoiceBuilder, TidyInvoiceDirector
from template.tidy.estimation import TidyEstimationBuilder,\
    TidyEstimationDirector
from template.tidy.credit_note import TidyCreditNoteBuilder
from template.tidy.transport_document import TidyTransportDocumentBuilder,\
    TidyTransportDocumentDirector
from i18n import instantiate_translator

class Factory(object):
    
    @classmethod
    def createDirector(cls, dirType, docType, *args, **kw):
        if(dirType == DirectorType.DEFAULT):
            if docType == DocumentType.INVOICE:
                return TidyInvoiceDirector(*args, **kw)
            if docType == DocumentType.TRANSPORT_DOCUMENT:
                return TidyTransportDocumentDirector(*args, **kw)
            if docType == DocumentType.ESTIMATION:
                return TidyEstimationDirector(*args, **kw)
            return TidyDirector(*args, **kw)
        raise Exception("No such layout type!")
    
    @classmethod
    def createBuilder(cls, buildType, docType, *args, **kw):
        if buildType == BuilderType.DEFAULT:
            if(docType == DocumentType.INVOICE):
                return TidyInvoiceBuilder(*args, **kw)
            if(docType == DocumentType.ESTIMATION):
                return TidyEstimationBuilder(*args, **kw)
            if(docType == DocumentType.CREDIT_NOTE):
                return TidyCreditNoteBuilder(*args, **kw)
            if(docType == DocumentType.TRANSPORT_DOCUMENT):
                return TidyTransportDocumentBuilder(*args, **kw)
        raise Exception("No such builder type!")

    @classmethod
    def createDataWrapper(cls, data, docType):
        if docType == DocumentType.INVOICE:
            return InvoiceData(data)
        elif docType == DocumentType.ESTIMATION:
            return EstimationData(data)
        elif docType == DocumentType.CREDIT_NOTE:
            return CreditNoteData(data)
        elif docType == DocumentType.TRANSPORT_DOCUMENT:
            return TransportDocumentData(data)
        raise Exception("No such data wrapper type!") 


def create_doc(out, docData, pathToLogo=None, logoWidth=None, logoHeight=None, docType=DocumentType.INVOICE, \
               tempType=DirectorType.DEFAULT, watermark=True, locale="it_IT"):
    _ = instantiate_translator([locale])
    builderDisplayParams = dict(logo=dict(path=pathToLogo, width=logoWidth, height=logoWidth))
    builder = Factory.createBuilder(BuilderType.DEFAULT, docType, out, dispParams=builderDisplayParams, translator=_)
    dataWrapper = Factory.createDataWrapper(docData, docType)
    docMetadata = dict(title=DocumentType.toString(docType), author=dataWrapper.getBusiness().getName(),
                       creator="Novabill - http://novabill.it",
                       subject=_("%(docType)s for %(client)s") % dict(docType=DocumentType.toString(docType), client=dataWrapper.getClient().getName()),
                       translator=_)
    directorDisplayParams = dict(pagenumbers=True, watermark=watermark, metadata=docMetadata)
    director = Factory.createDirector(tempType, docType, builder, dataWrapper, dispParams=directorDisplayParams, translator=_) 
    director.construct()
        
        
if __name__ == '__main__':
    testInvoiceJSON  = """
        {"accountingDocumentDate":"19/09/2012","accountingDocumentItems":[{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"1","id":8,"price":"1.00","quantity":"1.00","tax":"21.00","total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","unitOfMeasure":"1","version":0}],"accountingDocumentYear":2012,"business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1348089409855,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":2,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"class":"com.novadart.novabill.domain.Invoice","client":{"address":"via Vezzi, 12","business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1348089409855,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":2,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"city":"Nervesa della Battaglia","class":"com.novadart.novabill.domain.Client","country":"Italia","email":"","fax":"","id":2,"mobile":"","name":"General Alarm Eredi sas di Borsato Andrea","phone":"","postcode":"31040","province":"TV","ssn":"","vatID":"04085780262","version":2,"web":""},"documentID":1,"id":7,"note":"1","payed":false,"paymentDueDate":null,"paymentNote":null,"paymentType":null,"total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","version":0}
    """
    testTransportDocumentJSON = """
        {"accountingDocumentDate":"10/10/2012","accountingDocumentItems":[{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"Two bottles of good wine","id":8,"price":"33.00","quantity":"2.00","tax":"21.00","total":"79.86","totalBeforeTax":"66.00","totalTax":"13.86","unitOfMeasure":"bottle","version":0},{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"One cat","id":9,"price":"299.00","quantity":"1.00","tax":"21.00","total":"361.79","totalBeforeTax":"299.00","totalTax":"62.79","unitOfMeasure":"cat","version":0},{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"one set of knives","id":10,"price":"37.00","quantity":"32.00","tax":"21.00","total":"1432.64","totalBeforeTax":"1184.00","totalTax":"248.64","unitOfMeasure":"knife","version":0},{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"Some good time to spend at the seaside","id":11,"price":"7877.00","quantity":"1.00","tax":"21.00","total":"9531.17","totalBeforeTax":"7877.00","totalTax":"1654.17","unitOfMeasure":"week","version":0}],"accountingDocumentYear":2012,"business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":null,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":1,"nextTransportDocDocumentID":2,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":2,"web":""},"class":"com.novadart.novabill.domain.TransportDocumentData","client":{"address":"via Qualche Strada con Nome Lungo, 12","business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":null,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":1,"nextInvoiceDocumentID":1,"nextTransportDocDocumentID":2,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":2,"web":""},"city":"Nervesa della Battaglia","class":"com.novadart.novabill.domain.Client","country":"Italia","email":"","fax":"","id":2,"mobile":"","name":"The mighty company from this Young Entrepreneur","phone":"","postcode":"42837","province":"PD","ssn":"","vatID":"IT04235756211","version":2,"web":""},"documentID":1,"fromEndpoint":{"city":"Amsterdam","class":"com.novadart.novabill.domain.Endpoint","companyName":"Novadart S.n.c. di Giordano Battilana &amp; C.","country":null,"postcode":"72872","province":"AG","street":"via Gustav Mahlerplein"},"id":7,"note":"This package is the first of many that aer going to be sent on a weekly basis","numberOfPackages":2,"toEndpoint":{"city":"Nervesa della Battaglia","class":"com.novadart.novabill.domain.Endpoint","companyName":"The mighty company from this Young Entrepreneur","country":null,"postcode":"42837","province":"PD","street":"via Qualche Strada con Nome Lungo, 12"},"total":"11405.46","totalBeforeTax":"9426.00","totalTax":"1979.46","tradeZone":"","transportStartDate":"10/10/2012","transportationResponsibility":"Vettore","transporter":"Herry Poppins Poppins Poppins","version":1}
    """
    
    testEstimationJSON = """
        {"paymentNote":null, "limitations":null, "accountingDocumentDate":"17/10/2012","accountingDocumentItems":[{"class":"com.novadart.novabill.domain.AccountingDocumentItem","description":"1","id":8,"price":"1.00","quantity":"1.00","tax":"21.00","total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","unitOfMeasure":"1","version":0}],"accountingDocumentYear":2012,"business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1350481273698,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":2,"nextInvoiceDocumentID":1,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"class":"com.novadart.novabill.domain.Estimation","client":{"address":"via Qualche Strada con Nome Lungo, 12","business":{"address":"via Stradone, 51","city":"Campo San Martino","class":"com.novadart.novabill.domain.Business","country":"Italia","creationTime":null,"email":"giordano.battilana@novadart.com","fax":"0498597898","id":1,"lastLogin":1350481273698,"logo":null,"mobile":"3334927614","name":"Novadart S.n.c. di Giordano Battilana &amp; C.","nextCreditNoteDocumentID":1,"nextEstimationDocumentID":2,"nextInvoiceDocumentID":1,"nextTransportDocDocumentID":1,"nonFreeAccountExpirationTime":null,"password":"17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187","phone":"3334927614","postcode":"35010","province":"PD","ssn":"IT04534730280","vatID":"IT04534730280","version":3,"web":""},"city":"Nervesa della Battaglia","class":"com.novadart.novabill.domain.Client","country":"Italia","email":"","fax":"","id":2,"mobile":"","name":"The mighty company from this Young Entrepreneur","phone":"","postcode":"42837","province":"PD","ssn":"","vatID":"IT04235756211","version":2,"web":""},"documentID":1,"id":7,"note":"","total":"1.21","totalBeforeTax":"1.00","totalTax":"0.21","version":1}
    """
    
    #import locale
    #locale.setlocale(locale.LC_ALL, 'it_IT')
    
    outputFile = "/tmp/testInvoice.pdf"
    if exists(outputFile):
        remove(outputFile)
    import json
#    create_doc("/tmp/testInvoice.pdf", json.loads(testInvoiceJSON))
#    create_doc("/tmp/testInvoice.pdf", json.loads(testTransportDocumentJSON), docType=DocumentType.TRANSPORT_DOCUMENT)
    create_doc("/tmp/testInvoice.pdf", json.loads(testEstimationJSON), docType=DocumentType.ESTIMATION)
    
    

