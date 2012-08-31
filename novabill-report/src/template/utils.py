from reportlab.platypus.flowables import KeepTogether, PageBreak, _listWrapOn, _flowableSublist
from reportlab.platypus.doctemplate import FrameBreak
from reportlab.platypus.tables import Table
from reportlab.pdfgen import canvas
from reportlab.lib.units import mm
from reportlab.lib.colors import lightgrey
from reportlab.lib.pagesizes import A4


class FloatToEnd(KeepTogether):
    '''
    Float some flowables to the end of the current frame
    '''
    def __init__(self, flowables, maxHeight=None, brk='page', border=0, width=0):
        self._content = _flowableSublist(flowables)
        self._maxHeight = maxHeight
        self._state = 0
        self._brk = brk
        self._border = border
        self._W = width

    def wrap(self, aW, aH):
        return aW, aH + 1  #force a split

    def _makeBreak(self, h):
        if self._brk == 'page':
            return PageBreak()
        else:
            return FrameBreak()

    def split(self,aW,aH):
        dims = []
        H = _listWrapOn(self._content, aW, self.canv, dims=dims)[1]
        if self._state == 0:
            if H < aH:
#                t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
#                       style=[("GRID", (0, 0), (-1, -1), self._border, gray)])
                t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
                       style=[])
                return [t] + self._content
            else:
                S = self
                S._state = 1
                return [self._makeBreak(aH), S]
        else:
            if H > aH: return self._content
#            t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H],
#                   style=[("GRID", (0, 0), (-1, -1), self.border, gray)])
            t = Table([[""]], colWidths=[self._W], rowHeights=[aH - H], style=[])
            return [t] + self._content

def instatiateCanvasMaker(pagenumbers=True, watermark=True):
    class CanvasProcessor(canvas.Canvas):
        def __init__(self, *args, **kwargs):
            canvas.Canvas.__init__(self, *args, **kwargs)
            self._saved_page_states = []
            self.__pagenumbers = pagenumbers
            self.__watermark = watermark
    
        def showPage(self):
            self._saved_page_states.append(dict(self.__dict__))
            self._startPage()
    
        def save(self):
            """add page info to each page (page x of y)"""
            num_pages = len(self._saved_page_states)
            for state in self._saved_page_states:
                self.__dict__.update(state)
                if self.__pagenumbers:
                    self.draw_page_number(num_pages)
                if self.__watermark:
                    self.draw_watermark()
                canvas.Canvas.showPage(self)
            canvas.Canvas.save(self)
    
        def draw_page_number(self, page_count):
            self.setFont("Helvetica", 7)
            self.drawRightString(200*mm, 20*mm,
                "Page %d of %d" % (self._pageNumber, page_count))
            
        def draw_watermark(self):
            self.setFont("Helvetica", 7)
            self.setFillColor(lightgrey)
            w, _ = A4
            self.drawCentredString(w/2, 20*mm, "Powered by Novabill - www.novadart.com/novabill")
        
    return CanvasProcessor