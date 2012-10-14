# coding: utf-8

class AbstractDirector(object):
    
    def __init__(self, builder, dataObject, dispParams=dict):
        self.__builder = builder
        self.__data = dataObject
        self.__displayParams = dispParams
    
    def getBuilder(self):
        return self.__builder
        
    def getData(self):
        return self.__data;
    
    def getDispParams(self):
        return self.__displayParams
    
    def construct(self):
        pass

    
class DocumentType(object):
    
    INVOICE = 0
    
    ESTIMATION = 1
    
    CREDIT_NOTE = 2
    
    TRANSPORT_DOCUMENT = 3
    
    @classmethod
    def toString(cls, docType):
        if cls.INVOICE == docType:
            return "Invoice"
        if cls.ESTIMATION == docType:
            return "Estimation"
        if cls.CREDIT_NOTE == docType:
            return "Credit note"
        if cls.TRANSPORT_DOCUMENT == docType:
            return "Transport document"
        raise Exception("No such document type")


class DirectorType(object):
    
    DEFAULT = 0
    
class BuilderType(object):
    
    DEFAULT = 0;

