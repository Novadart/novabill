# coding: utf-8

from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, BORDER_SIZE, \
    BORDER_COLOR, LARGE_FONT_SIZE, SMALL_FONT_SIZE


class TidyInvoiceBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">Invoice %d</font></b>" % (LARGE_FONT_SIZE, data.getAccountingDocumentID()), style)],
                   ["Date of Invoice:", data.getAccountingDocumentDate()],
                   ["P.O Number:", ""],
                   ["Payment date:", data.getPaymentDueDate() if data.getPaymentDueDate() else ""]],
                  colWidths=[width*0.5, width*0.5])
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT")]))
        return t
    
        #=======================================================================
        # style = getSampleStyleSheet()["Normal"]
        # tableFlowables = [
        #   [Paragraph("<b>FATTURA</b>", style), Paragraph("" if data.getAccountingDocumentID() is None else str(data.getAccountingDocumentID()), style)],
        #   [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getAccountingDocumentDate() is None else data.getAccountingDocumentDate(), style)],
        #   [Paragraph("<b>Pagamento</b>", style), Paragraph(data.getHumanReadablePaymentType(), style)],
        #   [Paragraph("<b>Note Pagamento</b>", style), Paragraph("" if data.getPaymentNote() is None else data.getPaymentNote(), style)],
        #   [Paragraph("<b>Scadenza Pagamento</b>", style), Paragraph("" if data.getPaymentDueDate() is None else data.getPaymentDueDate(), style)],
        #   [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        # ]
        # t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        # t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        # return t
        #=======================================================================
