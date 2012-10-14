# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE
import i18n

_ = i18n.language.ugettext


class TidyCreditNoteBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, _("Credit note")), style)],
                   ["%s:" % _("Number"), data.getAccountingDocumentID()]
                   ["%s:" % _("Date"), data.getAccountingDocumentDate()],
                   ["%s:" % _("Payment date"), data.getPaymentDueDate() if data.getPaymentDueDate() else ""]],
                  colWidths=[width * 0.5, width * 0.5])
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT"),
                               ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                               ("TOPPADDING", (0, 0), (-1, -1), 1)]))
        return t
