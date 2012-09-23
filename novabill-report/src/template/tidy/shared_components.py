from reportlab.lib.colors import gray
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus.flowables import Flowable
from reportlab.platypus.paragraph import Paragraph
from reportlab.platypus.tables import Table
from reportlab.rl_config import defaultPageSize


class CustomerBoxFlowable(Flowable):
    '''
        Flowable for customer box
    '''
    
    def __init__(self, width, clientData):
        self.__clientData = clientData
        self.flowable = self.__assamble_flowable(width)
        
        
    def __assamble_flowable(self, width):
        style = getSampleStyleSheet()["Normal"]
        cl = self.__clientData
        flowables = []
        flowables.append(Paragraph("<b><i>Cliente</i></b>", style)) #label
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % cl.getName(), style)) #name
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % cl.getAddress(), style)) #address
        postcodeCityProvince = "%(postcode)s %(city)s (%(province)s)" % dict(postcode=cl.getPostcode(),
                city=cl.getCity(), province=cl.getProvince())
        flowables.append(Paragraph("<para leftIndent='10'>%s</para>" % postcodeCityProvince, style)) # postcode, city, and province
        flowables.append(Paragraph("<para leftIndent='10'>P.IVA %s</para>"% cl.getVatID(), style))
        return Table([[flowables]], colWidths=[width])


    def wrap(self, *args):
        return self.flowable.wrap(*args)

    def draw(self):
        canvas = self.canv
        self.flowable.canv = canvas
        self.flowable.draw()
        #drawing the box
        canvas.saveState()
        canvas.setStrokeColor(gray)
        width, height = self.flowable.wrap(defaultPageSize[0], defaultPageSize[1])
        canvas.roundRect(0, 0, width, height, 3)
        canvas.restoreState()