# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, LARGE_FONT_SIZE


class TidyCreditNoteBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">Credit note %d</font></b>" % (LARGE_FONT_SIZE, data.getAccountingDocumentID()), style)],
                   ["Date of credit note:", data.getAccountingDocumentDate()],
                   ["P.O Number:", ""],
                   ["Payment date:", data.getPaymentDueDate() if data.getPaymentDueDate() else ""]],
                  colWidths=[width * 0.5, width * 0.5])
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT")]))
        return t
