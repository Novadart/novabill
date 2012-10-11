# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, LARGE_FONT_SIZE

class TidyEstimationBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">Estimation %d</font></b>" % (LARGE_FONT_SIZE, data.getAccountingDocumentID()), style)],
                   ["Date of Estimation:", data.getAccountingDocumentDate()],
                   ["P.O Number:", ""]],
                  colWidths=[width*0.5, width*0.5])
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT")]))
        return t
