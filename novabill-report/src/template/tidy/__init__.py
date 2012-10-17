# coding: utf-8
from reportlab.lib.colors import gray, lightgrey
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus.doctemplate import BaseDocTemplate, SimpleDocTemplate
from reportlab.platypus.flowables import Spacer, Flowable, Image
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_config import defaultPageSize
from template.core import AbstractDirector
from template.default.shared_components import CustomerBoxFlowable
from template.utils import FloatToEnd, instatiateCanvasMaker
from reportlab.lib.pagesizes import A4

BORDER_SIZE = 0.5
BORDER_COLOR = gray
LARGE_FONT_SIZE = 14
MEDIUM_FONT_SIZE = 12
SMALL_FONT_SIZE = 10 

class TidyDirector(AbstractDirector):
    
    def getDocumentDetailsBodyFlowables(self, builder, data, docWidth):
        toFrom = Table([[builder.getToBusinessEntityDetailsFlowable(data), builder.getFromBusinessEntityDetailsFlowable(data)]],
                       colWidths=[docWidth*0.5, docWidth*0.5])
        toFrom.setStyle(TableStyle([("VALIGN", (0, 0), (-1, -1), "TOP"),
                                    ("ALIGN", (0, 0), (-1, -1), "LEFT"),
                                    ("LEFTPADDING", (0, 0), (-1, -1), 1.75*cm)]))
        return [toFrom]
        
    def getDocumentDetailsBottomFlowables(self, builder, data, docWidth):
        return [builder.getNotesFlowable(data)]
        
    
    def construct(self):
        builder = self.getBuilder()
        doc, data = builder.getDocument(), self.getData()
        story = []
        if data.getBusiness().getLogo():
            story.append(builder.getLogoFlowable(2.5*cm, 2.5*cm)) #place logo at the top
#        else:
#            story.append(builder.getNoLogoFlowable(2.5*cm, 2.5*cm))
        
        header = Table([[builder.getBusinessNameFlowable(data.getBusiness()),
                         builder.getDocumentDetailsHeaderFlowable(data, doc.width*0.4)]], colWidths=[doc.width*0.6, doc.width*0.4])
        header.setStyle(TableStyle([("VALIGN", (0, 0), (-1, -1), "TOP"),
                                    ("ALIGN", (1, 0), (1, 0), "RIGHT")]))
        story.append(header)
        story.append(Spacer(1, 0.5*cm))
        for flowable in self.getDocumentDetailsBodyFlowables(builder, data, doc.width): 
            story.append(flowable)
        story.append(Spacer(1, 1*cm))
        story.append(builder.getDocumentItemsFlowable(data.getAccountingDocumentItems(), doc.width))
        story.append(Spacer(1, cm))
        totals = Table([["", builder.getDocumentTotals(data, doc.width*0.4)], ["",""]], colWidths=[doc.width*0.6, doc.width*0.4])
        totals.setStyle(TableStyle([("LINEBELOW", (0,-1), (-1,-1), BORDER_SIZE, BORDER_COLOR)]))
        story.append(totals)
        story.append(Spacer(1, cm))
        for flowable in self.getDocumentDetailsBottomFlowables(builder, data, doc.width):
            story.append(flowable)
        doc.build(story, canvasmaker=instatiateCanvasMaker(pagenumbers=self.getDispParams()["pagenumbers"] \
                                                           if "pagenumbers" in self.getDispParams() else None,
                                                           watermark=self.getDispParams()["watermark"] \
                                                           if "watermark" in self.getDispParams() else None,
                                                           metadata=self.getDispParams()["metadata"]))
        


class TidyDocumentBuilder(object):
    
    def __init__(self, outputFilePath, dispParams=dict(), translator=None):
        self.__doc = SimpleDocTemplate(outputFilePath, pagesize=A4, rightMargin=18,leftMargin=18, topMargin=18,bottomMargin=100)
        self.__displayParams = dispParams
        self._ = translator
        
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
        return Spacer(1, 3*cm);
    
    def getBusinessNameFlowable(self, business):
        style = getSampleStyleSheet()["Normal"]
        return Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, business.getName()), style)
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        pass
    
    def __getBusinessEntityDetailsFlowable(self, data, label):
        style = getSampleStyleSheet()["Normal"]
        return Paragraph(self._("entity pattern") % dict(size=MEDIUM_FONT_SIZE, name=data.getName(), label=label, 
                                                        address=data.getAddress(), city=data.getCity(),
                                                        province=data.getProvince(), country=data.getCountry()), style)
    
    def getToBusinessEntityDetailsFlowable(self, data):
        return self.__getBusinessEntityDetailsFlowable(data.getClient(), self._("To"))
    
    def getFromBusinessEntityDetailsFlowable(self, data):
        return self.__getBusinessEntityDetailsFlowable(data.getBusiness(), self._("From"))
    
    def getDocumentItemsFlowable(self, itemsData, width):
        style = getSampleStyleSheet()["Normal"]
        data = [[self._("Price"), self._("Qty"), self._("Description"), self._("Tax"), self._("Total")]]
        for item in itemsData:
            data.append([item.getPrice(), item.getQuantity(), Paragraph(item.getDescription(), style), item.getTax(), item.getTotal()])
        itemsFlowable = Table(data, colWidths=[0.2*width, 0.1*width, 0.4*width, 0.1*width, 0.2*width])
        itemsFlowable.setStyle(TableStyle([("ALIGN", (1,0), (1,-1), "RIGHT"),
                                           ("ALIGN", (-2,0), (-1,-1), "RIGHT"),
                                           ("BACKGROUND", (0,0), (-1,0), lightgrey),
                                           ("LINEBELOW", (0,1), (-1,-1), BORDER_SIZE, BORDER_COLOR)]))
        return itemsFlowable
    
    def getDocumentTotals(self, data, width):
        totals = Table([["%s:" % self._("Total before tax"), u"%s €" % data.getTotalBeforeTax()],
                        ["%s:" % self._("Total tax"), u"%s €" % data.getTotalTax()],
                        ["", ""],
                        ["%s:" % self._("Total"), u"%s €" % data.getTotal()]], colWidths=[0.5*width, 0.5*width])
        totals.setStyle(TableStyle([("ALIGN", (-1,0), (-1,-1), "RIGHT"),
                                    ("FONTSIZE", (0,0), (-1,-2), 12),
                                    ("FONTSIZE", (0,-1), (-1,-1), 12)]))
        return totals
    
    def getNotesFlowable(self, data):
        style = getSampleStyleSheet()["Normal"]
        return Paragraph("<b>%s:</b><br/>%s" %  (self._("Invoice notes"), data.getNote() if data.getNote() else "<i>%s</i>" % self._("No notes")), style)
    