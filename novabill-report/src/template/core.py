# coding: utf-8

from reportlab.platypus.tables import Table
from reportlab.lib.units import inch
from template.utils import instatiateCanvasMaker

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


class DefaultDirector(AbstractDirector):
    
    def construct(self):
        builder = self.getBuilder()
        doc, data = builder.getDocument(), self.getData()
        story = []
        if data.getBusiness().getLogo():
            story.append(builder.getLogoFlowable(inch, inch)) #place logo at the top
        else:
            story.append(builder.getNoLogoFlowable(inch, inch))
        
        story.append(Table([[builder.getBusinessFlowable(data.getBusiness(), doc.width / 2),
                             builder.getCustomerFlowable(data.getClient(), doc.width / 2)]],
                           colWidths=[doc.width / 2] * 2))
        story.append(builder.getVerticalSpacerFlowable(5))
        story.append(builder.getDocumentDetailsFlowable(data, doc.width, 0.3))
        story.append(builder.getVerticalSpacerFlowable(10))
        story.append(builder.getDocumentItemsFlowable(data.getInvoiceItems(), doc.width))
        story.append(builder.getFooterFlowable(data, doc.width))
        doc.build(story, canvasmaker=instatiateCanvasMaker(pagenumbers=self.getDispParams()["pagenumbers"] if "pagenumbers" in self.getDispParams() else None,
                                                           watermark=self.getDispParams()["watermark"] if "watermark" in self.getDispParams() else None))
        
class AbstractDefaultBuilder(object):
    
    def getDocument(self): pass
    
    def getLogoFlowable(self, width, height): pass
    
    def getNoLogoFlowable(self, width , height): pass
    
    def getBusinessFlowable(self, businessData, width): pass
    
    def getCustomerFlowable(self, customerData, width): pass
    
    def getVerticalSpacerFlowable(self, height): pass
    
    def getDocumentDetailsFlowable(self, data, width, ratio): pass
    
    def getDocumentItemsFlowable(self, itemsData, width): pass
    
    def getFooterFlowable(self, data, width): pass
    
    
class DocumentType(object):
    
    INVOICE = 0
    
    ESTIMATION = 1
    
    CREDIT_NOTE = 2
    
    TRANSPORT_DOCUMENT = 3


class DirectorType(object):
    
    DEFAULT = 0
    
class BuilderType(object):
    
    DEFAULT = 0;

