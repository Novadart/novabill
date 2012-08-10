from reportlab.platypus.flowables import KeepTogether, PageBreak, _listWrapOn, _flowableSublist
from reportlab.platypus.doctemplate import FrameBreak
from reportlab.platypus.tables import Table

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
