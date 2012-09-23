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


class DirectorType(object):
    
    DEFAULT = 0
    
class BuilderType(object):
    
    DEFAULT = 0;

