# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.lib.units import inch
from reportlab.platypus.doctemplate import SimpleDocTemplate, FrameBreak
from reportlab.platypus.flowables import Spacer, Flowable, KeepTogether,\
    PageBreak, _listWrapOn, _flowableSublist, Image
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from reportlab.rl_config import defaultPageSize
from reportlab.lib.colors import gray
from lib.data import InvoiceData
from template.core import DocumentType

__all__ = ["DefaultTemplate", "DocumentType"]

BORDER_SIZE = 0.5
BORDER_COLOR = gray
ENNOVA_VATID = "IT03773530278"

class InvoiceTemplate(object):
    
    def __init__(self, jsonData, outputFilePath):
        self.__data = InvoiceData(jsonData)
        self.__outputFilePath = outputFilePath
        
    def _getData(self):
        return self.__data;
    
    def _getOutputFilePath(self):
        return self.__outputFilePath;
    
    def build(self):
        pass


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
        
        
class FloatToEnd(KeepTogether):
    '''
    Float some flowables to the end of the current frame
    '''
    def __init__(self, flowables, maxHeight=None, brk='page', border=0, width=0):
        self._content = _flowableSublist(flowables)
        self._maxHeight = maxHeight
        self._state = 0
        self._brk = brk
        self._border = border
        self._W = width

    def wrap(self, aW, aH):
        return aW, aH + 1  #force a split

    def _makeBreak(self, h):
        if self._brk == 'page':
            return PageBreak()
        else:
            return FrameBreak()

    def split(self,aW,aH):
        dims = []
        H = _listWrapOn(self._content, aW, self.canv, dims=dims)[1]
        if self._state == 0:
            if H < aH:
#                t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
#                       style=[("GRID", (0, 0), (-1, -1), self._border, gray)])
                t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
                       style=[])
                return [t] + self._content
            else:
                S = self
                S._state = 1
                return [self._makeBreak(aH), S]
        else:
            if H > aH: return self._content
#            t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
#                   style=[("GRID", (0, 0), (-1, -1), self.border, gray)])
            t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H], style=[])
            return [t] + self._content


class DefaultTemplate(InvoiceTemplate):
    
    def __init__(self, jsonData, pathToLogo, logoWidth, logoHeight, outputFilePath, docType = DocumentType.INVOICE):
        InvoiceTemplate.__init__(self, jsonData, outputFilePath)
        self.docType = docType
        self.pathToLogo = pathToLogo
        self.logoWidth = float(logoWidth) if logoWidth is not None else logoWidth
        self.logoHeight = float(logoHeight) if logoHeight is not None else logoHeight
        
    def __get(self, prop, defaultValue):
#        return self._getData()[prop] if prop in self._getData() and self._getData()[prop] is not None else defaultValue
        return prop if prop is not None else defaultValue
        
    def __business_box_flowable(self):
        '''
            Generates flowables for business box
        '''
        bus = self._getData().getBusiness()
        style = getSampleStyleSheet()["Normal"]
        flowables = []
        flowables.append(Paragraph(bus.getName() , style)) #name
        flowables.append(Paragraph(bus.getAddress(), style)) #address
        postcodeCityProvince = "%(postcode)s %(city)s (%(province)s)" % dict(postcode=bus.getPostcode(),
                    city=bus.getCity(), province=bus.getProvince())
        flowables.append(Paragraph(postcodeCityProvince, style)) # postcode, city, and province
        telFax = "Tel. %(phone)s / Fax %(fax)s" % dict(phone=bus.getPhone(),
                    fax=bus.getFax())
        flowables.append(Paragraph(telFax, style))
        flowables.append(Spacer(1, style.fontSize))
        vatSSN = "P.IVA %(vat)s - Cod. Fiscale %(ssn)s" % dict(vat=bus.getVatID(),
                    ssn=bus.getSSN())
        flowables.append(Paragraph("<para fontSize='9'>%s</para>" % vatSSN, style))
        return flowables
        
    def __business_customer_horizontal_flowable(self, width):
        bizz_flowables = self.__business_box_flowable()
        cust_flowable = CustomerBoxFlowable(width / 2, self._getData().getClient())
        t = Table([[bizz_flowables, cust_flowable]], colWidths=[width / 2] * 2)
        return t
    
    def __invoice_details_flowable(self, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>FATTURA</b>", style), Paragraph("%s" % self.__get(self._getData().getAccountingDocumentID(), ""), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("%s" % self.__get(self._getData().getAccountingDocumentDate(), ""), style)],
        ]
        if self.docType == DocumentType.INVOICE:
            tableFlowables +=[
            [Paragraph("<b>Pagamento</b>", style), Paragraph(self.__get(self._getData().getHumanReadablePaymentType(), ""), style)],
            [Paragraph("<b>Note Pagamento</b>", style), Paragraph(self.__get(self._getData().getPaymentNote(), ""), style)],
            [Paragraph("<b>Scadenza Pagamento</b>", style), Paragraph(self.__get(self._getData().getPaymentDueDate(), ""), style)],
        ]
        tableFlowables.append([Paragraph("<b>Note</b>", style), Paragraph(self.__get(self._getData().getNote(), ""), style)])
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t
    
    def __invoice_items_flowable(self, width):
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
        for item in self._getData().getAccountingDocumentItems():
            data.append([
                Paragraph(self.__get(item.getDescription(), ""), style),
                Paragraph("" if item.getUnitOfMeasure() is None else "%s" % item.getUnitOfMeasure(), style),
                Paragraph("" if item.getQuantity() is None else item.getQuantity(), style),
                Paragraph("" if item.getPrice() is None else u"€ %s" % item.getPrice(), style),
                Paragraph("" if item.getTotalBeforeTax() is None else u"€ %s" % item.getTotalBeforeTax(), style),
                Paragraph("" if item.getTax() is None else "%s %%" % str(item.getTax())[:-3], style),
            ])
        t = Table(data, colWidths=map(lambda x: x * width, [0.48, 0.1, 0.08, 0.12, 0.13, 0.09]))
        t.setStyle(TableStyle([
            ("BOX", (0, 0), (-1, 0), BORDER_SIZE, gray),
#            ("GRID", (0, 1), (-1, -1), BORDER_SIZE, gray),
            ("VALIGN", (0, 1), (-1, -1), "TOP")
        ]))
        return t
    
    def __invoice_footer(self, width):
        style = getSampleStyleSheet()["Normal"]
        data = [
            [Paragraph("Totale Imponibile", style),
             Paragraph("Totale Iva", style),
             Paragraph("<b>Totale</b>", style)
            ],
            [Paragraph("" if self._getData().getTotalBeforeTax() is None else u"€ %s" % self._getData().getTotalBeforeTax(), style),
             Paragraph("" if self._getData().getTotalTax() is None else u"€ %s" % self._getData().getTotalTax(), style),
             Paragraph("" if self._getData().getTotal() is None else u"<b>€ %s</b>" % self._getData().getTotal(), style)
            ]
        ]
        t = Table(data, colWidths=map(lambda x: x * width, [0.33, 0.33, 0.34]))
        t.setStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)])
        return FloatToEnd(t, width=width)
    
        
    def build(self):
        doc = SimpleDocTemplate(self._getOutputFilePath())
        story = []
        if self.pathToLogo is not None:
            w, h = inch, inch
            r = min(w / self.logoWidth, h / self.logoHeight)
            im = Image(self.pathToLogo, self.logoWidth * r, self.logoHeight * r)
            im.hAlign = 'CENTER'
            story.append(im)
        story.append(self.__business_customer_horizontal_flowable(doc.width))
        story.append(Spacer(1, 5))
        story.append(self.__invoice_details_flowable(doc.width, 0.3))
        story.append(Spacer(1, 10))
        story.append(self.__invoice_items_flowable(doc.width))
        story.append(self.__invoice_footer(doc.width))
        doc.build(story)
        
def create_invoice(out, docData, pathToLogo=None, logoWidth=None, logoHeight=None, docType=DocumentType.INVOICE):
    #TODO manage different templates
    doc = DefaultTemplate(docData, pathToLogo, logoWidth, logoHeight, out, docType)
    doc.build()
        
        