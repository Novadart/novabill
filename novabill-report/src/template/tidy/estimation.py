# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE, TidyDirector,\
    BORDER_SIZE, BORDER_COLOR
from reportlab.lib.colors import lightgrey


class TidyEstimationDirector(TidyDirector):
    
    def getDocumentDetailsBottomFlowables(self, builder, data, docWidth):
        tbl = Table([[builder.getLimitationsFlowables(data), builder.getEstimationPaymentDetailsFlowable(data, docWidth*0.5)],
                     [builder.getNotesFlowable(data), ""]], colWidths=[docWidth * 0.5, docWidth * 0.5])
        tbl.setStyle(TableStyle([("ALIGN", (0,0), (0,0), "LEFT"),
                                 ("ALIGN", (1,0), (1,0), "RIGHT"),
                                 ("VALIGN", (0,0), (0,0), "TOP"),
                                 ("VALIGN", (1,0), (1,0), "TOP"),
                                 ("RIGHTPADDING", (0,0), (1,0), 0)]))
        return [tbl]
        

class TidyEstimationBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, self._("Estimation")), style)],
                   ["%s:" % self._("Number"), data.getAccountingDocumentID()],
                   ["%s:" % self._("Date"), data.getAccountingDocumentDate()],
                   ["%s:" % self._("Valid until"), data.getValidTill()]],
                  colWidths=[width*0.2, width*0.3])
        t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                               ("ALIGN", (1, 0), (1, -1), "LEFT"),
                               ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                               ("TOPPADDING", (0, 0), (-1, -1), 1)]))
        return t
    
    def getLimitationsFlowables(self, data):
        style = getSampleStyleSheet()["Normal"]
        return [Paragraph("<b>%s:</b>" % self._("Limitations"), style),
                Paragraph(data.getLimitations() if data.getLimitations() else "<i>%s</i>" % self._("No limitations"), style)]
        
    def getEstimationPaymentDetailsFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        tbl = Table([["%s" % self._("Payment note"), ""],
                     ["", Paragraph(data.getPaymentNote() if data.getPaymentNote() else "", style)],
                    ], colWidths=[width * 0.01, width * 0.99])
        tbl.setStyle(TableStyle([("BACKGROUND", (0,0), (-1,0), lightgrey),
                                 ('VALIGN', (0, 0), (-1, -1), 'TOP'),
                                 ("LINEBELOW", (0, -1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return tbl
