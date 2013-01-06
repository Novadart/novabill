# coding: utf-8
from lib.data import InvoiceData, EstimationData, CreditNoteData,\
    TransportDocumentData
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
    director = Factory.createDirector(tempType, docType, builder, dataWrapper, hasLogo=(pathToLogo is not None), dispParams=directorDisplayParams, translator=_) 
    director.construct()
