# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.default import DefaultDocumentBuilder, BORDER_SIZE, \
    BORDER_COLOR


class DefaultCreditNoteBuilder(DefaultDocumentBuilder):
    
    def getDocumentDetailsFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>CREDIT NOTE</b>", style), Paragraph("" if data.getAccountingDocumentID() is None else str(data.getAccountingDocumentID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getAccountingDocumentDate() is None else data.getAccountingDocumentDate(), style)],
            [Paragraph("<b>Pagamento</b>", style), Paragraph(data.getHumanReadablePaymentType(), style)],
            [Paragraph("<b>Note Pagamento</b>", style), Paragraph("" if data.getPaymentNote() is None else data.getPaymentNote(), style)],
            [Paragraph("<b>Scadenza Pagamento</b>", style), Paragraph("" if data.getPaymentDueDate() is None else data.getPaymentDueDate(), style)],
            [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t