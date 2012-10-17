# coding: utf-8
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.para import Paragraph
from reportlab.platypus.tables import Table, TableStyle
from template.tidy import TidyDocumentBuilder, MEDIUM_FONT_SIZE, TidyDirector
from reportlab.lib.units import cm
from reportlab.lib.colors import lightgrey
from template.tidy import BORDER_SIZE, BORDER_COLOR
from reportlab.platypus.flowables import Spacer


class TidyTransportDocumentDirector(TidyDirector):
    
    def getDocumentDetailsBodyFlowables(self, builder, data, docWidth):
        toFrom = Table([[builder.getToBusinessEntityDetailsFlowable(data), builder.getFromBusinessEntityDetailsFlowable(data)],
                        ["",""],
                        [builder.getToEndpointFlowable(data, 7*cm), builder.getFromEndpointFlowable(data, 7*cm)]],
                       colWidths=[docWidth*0.5, docWidth*0.5])
        toFrom.setStyle(TableStyle([("VALIGN", (0, 0), (-1, -1), "TOP"),
                                    ("ALIGN", (0, 0), (-1, -1), "LEFT"),
                                    ("LEFTPADDING", (0, 0), (-1, -1), 1.75*cm)]))
        transDetFlow = builder.getTransportDetailsFlowable(data, docWidth*0.45)
        transDetFlow.hAlign = "RIGHT"
        return [toFrom, Spacer(1, 0.5*cm), transDetFlow]

class TidyTransportDocumentBuilder(TidyDocumentBuilder):
    
        def getDocumentDetailsHeaderFlowable(self, data, width):
            style = getSampleStyleSheet()["Normal"]
            t = Table([["", Paragraph("<b><font size=\"%d\">%s</font></b>" % (MEDIUM_FONT_SIZE, self._("Transport document")), style)],
                       ["%s:" % self._("Number"), data.getAccountingDocumentID()],
                       ["%s:" % self._("Date"), data.getAccountingDocumentDate()]],
                  colWidths=[width * 0.2, width * 0.7])
            t.setStyle(TableStyle([("ALIGN", (0, 0), (0, -1), "RIGHT"),
                                   ("ALIGN", (1, 0), (1, -1), "LEFT"),
                                   ("BOTTOMPADDING", (0, 0), (-1, -1), 0),
                                   ("TOPPADDING", (0, 0), (-1, -1), 0)]))
            return t
        
        def __getEndPointFlowable(self, data, width, label):
            style = getSampleStyleSheet()["Normal"]
            tbl = Table([[Paragraph(label, style)],
                         [Paragraph(data.getCompanyName(), style)],
                         [Paragraph(data.getStreet(), style)],
                         [Paragraph(self._("city pattern") % dict(city=data.getCity(), province=data.getProvince()), style)],
                         #[Paragraph(data.getCountry(), style)]
                         ], colWidths=[width])
            tbl.setStyle(TableStyle([("BACKGROUND", (0,0), (-1,0), lightgrey),
                                     ("LEFTPADDING", (0, 0), (-1, -1), 3),
                                     ("TOPPADDING", (0, 1), (-1, -1), 0),
                                     ("BOTTOMPADDING", (0, 1), (0, -2), 0),
                                     ("BOX", (0, 0), (0, 0), BORDER_SIZE, BORDER_COLOR),
                                     ("BOX", (0, 1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
            return tbl
        
        def getToEndpointFlowable(self, data, width):
            return self.__getEndPointFlowable(data.getToLocation(), width, self._("Receiving address"))
        
        def getFromEndpointFlowable(self, data, width):
            return self.__getEndPointFlowable(data.getFromLocation(), width, self._("Sending address"))
        
        def getTransportDetailsFlowable(self, data, width):
            style = getSampleStyleSheet()["Normal"]
            tbl = Table([[Paragraph("%s:" % self._("Transporter"), style), Paragraph(data.getTransporter(), style)],
                         [Paragraph("%s:" % self._("Transportation responsibility"), style), Paragraph(data.getTransportationResponsibility(), style)],
                         [Paragraph("%s:" % self._("Num. of packages"), style), Paragraph(str(data.getNumberOfPackages()), style)],
                         [Paragraph("%s:" % self._("Trade zone"), style), Paragraph(data.getTradeZone(), style)]
                         ], colWidths=[width*0.3, width*0.7])
            tbl.setStyle(TableStyle([('VALIGN',(0,0),(-1,-1),'BOTTOM'),
                                     ("LINEABOVE", (0, 0), (-1, 0), BORDER_SIZE, BORDER_COLOR),
                                     ("LINEBELOW", (0, -1), (-1, -1), BORDER_SIZE, BORDER_COLOR)]))
            return tbl
