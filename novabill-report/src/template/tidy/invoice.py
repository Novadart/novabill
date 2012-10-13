# coding: utf-8

from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE


class TidyInvoiceBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">Invoice</font></b>" % MEDIUM_FONT_SIZE, style)],
                   ["Number:", data.getAccountingDocumentID()],
                   ["Date:", data.getAccountingDocumentDate()],
                   ["Payment date:", data.getPaymentDueDate() if data.getPaymentDueDate() else ""]],
                  colWidths=[width*0.2, width*0.3]
                  )
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT"),
                               ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                               ("TOPPADDING", (0, 0), (-1, -1), 1)]))
        return t
    
