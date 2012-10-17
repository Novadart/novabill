# coding: utf-8

from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE, TidyDirector,\
    BORDER_SIZE, BORDER_COLOR
from reportlab.lib.units import cm
from reportlab.platypus.flowables import Spacer
from reportlab.lib.colors import lightgrey

class TidyInvoiceDirector(TidyDirector):
    
    def getDocumentDetailsBodyFlowables(self, builder, data, docWidth):
        toFrom = Table([[builder.getToBusinessEntityDetailsFlowable(data), builder.getFromBusinessEntityDetailsFlowable(data)]],
                       colWidths=[docWidth*0.5, docWidth*0.5])
        toFrom.setStyle(TableStyle([("VALIGN", (0, 0), (-1, -1), "TOP"),
                                    ("ALIGN", (0, 0), (-1, -1), "LEFT"),
                                    ("LEFTPADDING", (0, 0), (-1, -1), 1.75*cm)]))
        return [toFrom]
    
    def getDocumentDetailsBottomFlowables(self, builder, data, docWidth):
        invDetailsF = builder.getInvoicePaymentDetailsFlowable(data, docWidth*0.4)
        invDetailsF.hAlign = "RIGHT"
        return [invDetailsF, Spacer(1, 0.25*cm), builder.getNotesFlowable(data)]


class TidyInvoiceBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, self._("Invoice")), style)],
                   ["%s:" % self._("Number"), data.getAccountingDocumentID()],
                   ["%s:" % self._("Date"), data.getAccountingDocumentDate()]],
                  colWidths=[width*0.2, width*0.3]
                  )
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT"),
                               ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                               ("TOPPADDING", (0, 0), (-1, -1), 1)]))
        return t
    
    def getInvoicePaymentDetailsFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        tbl = Table([["%s" % self._("Payment details"), ""],
                     [Paragraph("%s:" % self._("Date"), style), Paragraph(data.getPaymentDueDate() if data.getPaymentDueDate() else "", style)],
                     [Paragraph("%s:" % self._("Type"), style), Paragraph(data.getHumanReadablePaymentType(), style)],
                     [Paragraph("%s:" % self._("Note"), style), Paragraph(data.getPaymentNote() if data.getPaymentNote() else "", style)],
                    ], colWidths=[width * 0.2, width * 0.8])
        tbl.setStyle(TableStyle([("BACKGROUND", (0,0), (-1,0), lightgrey),
                                 ('VALIGN', (0, 0), (-1, -1), 'BOTTOM'),
                                 ("SPAN", (0, 0), (1, 0)),
                                 ("BOX", (0, 0), (1, 0), BORDER_SIZE, BORDER_COLOR),
                                 ("BOX", (0, 1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return tbl
    
    
