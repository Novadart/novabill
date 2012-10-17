# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE, TidyDirector,\
    BORDER_SIZE, BORDER_COLOR
from reportlab.platypus.flowables import Spacer
from reportlab.lib.units import cm
from reportlab.lib.colors import lightgrey


class TidyEstimationDirector(TidyDirector):
    
    def getDocumentDetailsBottomFlowables(self, builder, data, docWidth):
        estDetailsF = builder.getEstimationPaymentDetailsFlowable(data, docWidth*0.4)
        estDetailsF.hAlign = "RIGHT"
        return [estDetailsF, Spacer(1, 0.25*cm)] + builder.getLimitationsFlowables(data) + [Spacer(1, 0.25*cm), builder.getNotesFlowable(data)]


class TidyEstimationBuilder(TidyDocumentBuilder):
    
    def getDocumentDetailsHeaderFlowable(self, data, width):
        style = getSampleStyleSheet()["Normal"]
        t = Table([["", Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, self._("Estimation")), style)],
                   ["%s:" % self._("Number"), data.getAccountingDocumentID()],
                   ["%s:" % self._("Date"), data.getAccountingDocumentDate()]],
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
                                 ('VALIGN', (0, 0), (-1, -1), 'BOTTOM'),
                                 ("SPAN", (0, 0), (1, 0)),
                                 ("BOX", (0, 0), (1, 0), BORDER_SIZE, BORDER_COLOR),
                                 ("BOX", (0, 1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
        return tbl
