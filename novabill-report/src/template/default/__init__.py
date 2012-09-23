# coding: utf-8
from reportlab.lib.colors import gray
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus.doctemplate import SimpleDocTemplate
from reportlab.platypus.flowables import Spacer, Flowable, Image
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_config import defaultPageSize
from template.core import AbstractDirector
from template.default.shared_components import CustomerBoxFlowable
from template.utils import FloatToEnd, instatiateCanvasMaker


BORDER_SIZE = 0.5
BORDER_COLOR = gray


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
        story.append(builder.getDocumentItemsFlowable(data.getAccountingDocumentItems(), doc.width))
        story.append(builder.getFooterFlowable(data, doc.width))
        doc.build(story, canvasmaker=instatiateCanvasMaker(pagenumbers=self.getDispParams()["pagenumbers"] \
                                                           if "pagenumbers" in self.getDispParams() else None,
                                                           watermark=self.getDispParams()["watermark"] \
                                                           if "watermark" in self.getDispParams() else None))


class DefaultDocumentBuilder(object):
    
    def __init__(self, outputFilePath, dispParams=dict()):
        self.__doc = SimpleDocTemplate(outputFilePath)
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
    
    def getBusinessFlowable(self, businessData, width):
        style = getSampleStyleSheet()["Normal"]
        flowables = []
        flowables.append(Paragraph(businessData.getName() , style)) #name
        flowables.append(Paragraph(businessData.getAddress(), style)) #address
        postcodeCityProvince = "%(postcode)s %(city)s (%(province)s)" % dict(postcode=businessData.getPostcode(),
                    city=businessData.getCity(), province=businessData.getProvince())
        flowables.append(Paragraph(postcodeCityProvince, style)) # postcode, city, and province
        telFax = "Tel. %(phone)s / Fax %(fax)s" % dict(phone=businessData.getPhone(),
                    fax=businessData.getFax())
        flowables.append(Paragraph(telFax, style))
        flowables.append(Spacer(1, style.fontSize))
        vatSSN = "P.IVA %(vat)s - Cod. Fiscale %(ssn)s" % dict(vat=businessData.getVatID(),
                    ssn=businessData.getSSN())
        flowables.append(Paragraph("<para fontSize='9'>%s</para>" % vatSSN, style))
        return flowables
    
    def getCustomerFlowable(self, customerData, width):
        return CustomerBoxFlowable(width, customerData)
    
    def getVerticalSpacerFlowable(self, height):
        return Spacer(1, height)
    
    def getDocumentDetailsFlowable(self, data, width, ratio): 
        pass
    
    def getDocumentItemsFlowable(self, itemsData, width):
        style = getSampleStyleSheet()["Normal"]
        data = []
        data.append([ #Column headers
            Paragraph("<b>Descrizione</b>", style),
            Paragraph("<b>U.M.</b>", style),
            Paragraph("<b>Qtà</b>", style),
            Paragraph("<b>Prezzo</b>", style),
            Paragraph("<b>Importo</b>", style),
            Paragraph("<b>IVA</b>", style)
        ]);
        for item in itemsData:
            data.append([
                Paragraph("" if item.getDescription() is None else "%s" % item.getDescription(), style),
                Paragraph("" if item.getUnitOfMeasure() is None else "%s" % item.getUnitOfMeasure(), style),
                Paragraph("" if item.getQuantity() is None else item.getQuantity(), style),
                Paragraph("" if item.getPrice() is None else u"€ %s" % item.getPrice(), style),
                Paragraph("" if item.getTotalBeforeTax() is None else u"€ %s" % item.getTotalBeforeTax(), style),
                Paragraph("" if item.getTax() is None else "%s %%" % str(item.getTax())[:-3], style),
            ])
        t = Table(data, colWidths=map(lambda x: x * width, [0.48, 0.1, 0.08, 0.12, 0.13, 0.09]))
        t.setStyle(TableStyle([
            ("BOX", (0, 0), (-1, 0), BORDER_SIZE, gray),
            ("GRID", (0, 1), (-1, -1), BORDER_SIZE, gray),
            ("VALIGN", (0, 1), (-1, -1), "TOP")
        ]))
        return t
    
    def getFooterFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        data = [
            [Paragraph("Totale Imponibile", style),
             Paragraph("Totale Iva", style),
             Paragraph("<b>Totale</b>", style)
            ],
            [Paragraph("" if data.getTotalBeforeTax() is None else u"€ %s" % data.getTotalBeforeTax(), style),
             Paragraph("" if data.getTotalTax() is None else u"€ %s" % data.getTotalTax(), style),
             Paragraph("" if data.getTotal() is None else u"<b>€ %s</b>" % data.getTotal(), style)
            ]
        ]
        t = Table(data, colWidths=map(lambda x: x * width, [0.33, 0.33, 0.34]))
        t.setStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)])
        return FloatToEnd(t, width=width)