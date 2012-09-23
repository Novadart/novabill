# coding: utf-8
from reportlab.lib.colors import gray
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus.doctemplate import BaseDocTemplate
from reportlab.platypus.flowables import Spacer, Flowable, Image
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_config import defaultPageSize
from template.core import AbstractDirector
from template.default.shared_components import CustomerBoxFlowable
from template.utils import FloatToEnd, instatiateCanvasMaker


class Palette:
    GRAY = gray;

BORDER_SIZE = 0.5
BORDER_COLOR = gray


class TidyDirector(AbstractDirector):
    
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
        story.append(builder.getDocumentItemsFlowable(data.getAccountingDocumentItems(), doc.width))
        story.append(builder.getFooterFlowable(data, doc.width))
        doc.build(story, canvasmaker=instatiateCanvasMaker(pagenumbers=self.getDispParams()["pagenumbers"] \
                                                           if "pagenumbers" in self.getDispParams() else None,
                                                           watermark=self.getDispParams()["watermark"] \
                                                           if "watermark" in self.getDispParams() else None))


class TidyDocumentBuilder(object):
    
    def __init__(self, outputFilePath, dispParams=dict()):
        self.__doc = BaseDocTemplate()
        self.__displayParams = dispParams
        
    def getDocument(self):
        return self.__doc
    
    def getLogoFlowable(self, height, width):
        logoParams = self.__displayParams["logo"]
        logoPath, logoWidth, logoHeight = logoParams["path"], logoParams["width"], logoParams["height"]  
        r = min(width / logoWidth, height / logoHeight)
        im = Image(logoPath, logoWidth * r, logoHeight * r)
        im.hAlign = 'LEFT'
        return im
    
    def getNoLogoFlowable(self, height, width):
        return Spacer(1, inch);
    
    def getDocumentDetailsFlowable(self, data, width, ratio):
        #TODO this has to be moved to sub implementations
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>FATTURA</b>", style), Paragraph("" if data.getInvoiceID() is None else str(data.getInvoiceID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getInvoiceDate() is None else data.getInvoiceDate(), style)],
            [Paragraph("<b>Pagamento</b>", style), Paragraph(data.getHumanReadablePaymentType(), style)],
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t
    
    def getToEndpointFlowable(self):
        pass
    
    def getFromEndpointFlowable(self):
        pass
    
    def getDocumentItemsFlowable(self, itemsData, width):
        pass
    
    def getDocumentTotals(self):
        pass
    
    def getNotesFlowable(self):
        pass
    