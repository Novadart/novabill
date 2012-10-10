# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.default import DefaultDocumentBuilder, BORDER_SIZE, \
    BORDER_COLOR

class DefaultEstimationBuilder(DefaultDocumentBuilder):
    
    def getDocumentDetailsFlowable(self, data, width, ratio):
        style = getSampleStyleSheet()["Normal"]
        tableFlowables = [
            [Paragraph("<b>FATTURA</b>", style), Paragraph("" if data.getAccountingDocumentID() is None else str(data.getAccountingDocumentID()), style)],
            [Paragraph("<b>DATA</b>", style), Paragraph("" if data.getAccountingDocumentDate() is None else data.getAccountingDocumentDate(), style)],
            [Paragraph("<b>Note</b>", style), Paragraph("" if data.getNote() is None else data.getNote(), style)]
        ]
        t = Table(tableFlowables, colWidths=[width * ratio, width * (1 - ratio)])
        t.setStyle(TableStyle([("BOX", (0, 0), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return t