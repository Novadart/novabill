# coding: utf-8

from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE, TidyDirector,\
    BORDER_SIZE, BORDER_COLOR
from reportlab.lib.units import cm
from reportlab.platypus.flowables import Spacer

class TidyInvoiceDirector(TidyDirector):
    
    def getDocumentDetailsBodyFlowables(self, builder, data, docWidth):
        toFrom = Table([[builder.getToBusinessEntityDetailsFlowable(data), builder.getFromBusinessEntityDetailsFlowable(data)]],
                       colWidths=[docWidth*0.5, docWidth*0.5])
        toFrom.setStyle(TableStyle([("VALIGN", (0, 0), (-1, -1), "TOP"),
                                    ("ALIGN", (0, 0), (-1, -1), "LEFT"),
                                    ("LEFTPADDING", (0, 0), (-1, -1), 1.75*cm)]))
        invDetailsF = builder.getInvoiceDetailsFlowable(data, docWidth*0.4)
        invDetailsF.hAlign = "RIGHT"
        return [invDetailsF, Spacer(1, cm), toFrom]


class TidyInvoiceBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">Invoice</font></b>" % MEDIUM_FONT_SIZE, style)],
                   ["Number:", data.getAccountingDocumentID()],
                   ["Date:", data.getAccountingDocumentDate()]],
                  colWidths=[width*0.2, width*0.3]
                  )
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT"),
                               ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                               ("TOPPADDING", (0, 0), (-1, -1), 1)]))
        return t
    
    def getInvoiceDetailsFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        tbl = Table([[Paragraph("Payment date:", style), Paragraph(data.getPaymentDueDate() if data.getPaymentDueDate() else "", style)],
                     [Paragraph("Payment type:", style), Paragraph(data.getHumanReadablePaymentType(), style)],
                     [Paragraph("Payment note:", style), Paragraph(data.getPaymentNote() if data.getPaymentNote() else "", style)],
                    ], colWidths=[width * 0.4, width * 0.6])
        tbl.setStyle(TableStyle([('VALIGN', (0, 0), (-1, -1), 'BOTTOM'),
                                 ("LINEABOVE", (0, 0), (-1, 0), BORDER_SIZE, BORDER_COLOR),
                                 ("LINEBELOW", (0, -1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return tbl
    
    
