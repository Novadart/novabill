# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus.doctemplate import SimpleDocTemplate
from reportlab.platypus.flowables import Spacer, Flowable, Image
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_config import defaultPageSize
from reportlab.lib.colors import gray
from template.core import AbstractDefaultBuilder
from template.utils import FloatToEnd


BORDER_SIZE = 0.5
BORDER_COLOR = gray


class CustomerBoxFlowable(Flowable):
    '''
        Flowable for customer box
    '''
    
    def __init__(self, width, clientData):
        self.__clientData = clientData
        self.flowable = self.__assamble_flowable(width)
        
        
    def __assamble_flowable(self, width):
        style = getSampleStyleSheet()["Normal"]
        cl = self.__clientData
        flowables = []
        flowables.append(Paragraph("<b><i>Cliente</i></b>", style)) #label
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % cl.getName(), style)) #name
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % cl.getAddress(), style)) #address
        postcodeCityProvince = "%(postcode)s %(city)s (%(province)s)" % dict(postcode=cl.getPostcode(),
                city=cl.getCity(), province=cl.getProvince())
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % postcodeCityProvince, style)) # postcode, city, and province
        flowables.append(Paragraph("<para leftIndent='10'>P.IVA %s</para>"% cl.getVatID(), style))
        return Table([[flowables]], colWidths=[width])


    def wrap(self, *args):
        return self.flowable.wrap(*args)

    def draw(self):
        canvas = self.canv
        self.flowable.canv = canvas
        self.flowable.draw()
        #drawing the box
        canvas.saveState()
        canvas.setStrokeColor(gray)
        width, height = self.flowable.wrap(defaultPageSize[0], defaultPageSize[1])
        canvas.roundRect(0, 0, width, height, 3)
        canvas.restoreState()

        
class DefaultDocumentBuilder(AbstractDefaultBuilder):
    
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
        
    
        
class DefaultInvoiceBuilder(DefaultDocumentBuilder):
    
    def getDocumentDetailsFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>FATTURA</b>", style), Paragraph("" if data.getInvoiceID() is None else str(data.getInvoiceID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getInvoiceDate() is None else data.getInvoiceDate(), style)],
            [Paragraph("<b>Pagamento</b>", style), Paragraph(data.getHumanReadablePaymentType(), style)],
            [Paragraph("<b>Note Pagamento</b>", style), Paragraph("" if data.getPaymentNote() is None else data.getPaymentNote(), style)],
            [Paragraph("<b>Scadenza Pagamento</b>", style), Paragraph("" if data.getPaymentDueDate() is None else data.getPaymentDueDate(), style)],
            [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t
    
    
class DefaultEstimationBuilder(DefaultDocumentBuilder):
    def getDocumentDetailsFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>FATTURA</b>", style), Paragraph("" if data.getInvoiceID() is None else str(data.getInvoiceID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getInvoiceDate() is None else data.getInvoiceDate(), style)],
            [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t
    
class DefaultCreditNoteBuilder(DefaultDocumentBuilder):
    
    def getDocumentDetailsFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>CREDIT NOTE</b>", style), Paragraph("" if data.getInvoiceID() is None else str(data.getInvoiceID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getInvoiceDate() is None else data.getInvoiceDate(), style)],
            [Paragraph("<b>Pagamento</b>", style), Paragraph(data.getHumanReadablePaymentType(), style)],
            [Paragraph("<b>Note Pagamento</b>", style), Paragraph("" if data.getPaymentNote() is None else data.getPaymentNote(), style)],
            [Paragraph("<b>Scadenza Pagamento</b>", style), Paragraph("" if data.getPaymentDueDate() is None else data.getPaymentDueDate(), style)],
            [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t
    