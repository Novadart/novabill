# coding: utf-8
from os import remove
from os.path import exists
from template.default import DefaultTemplate




def create_invoice(out, invoice, pathToLogo=None, logoWidth=None, logoHeight=None):
    #TODO manage different templates
    doc = DefaultTemplate(invoice, pathToLogo, logoWidth, logoHeight, out)
    doc.build()



        
        
if False and __name__ == '__main__':
    testInvoiceJSON  = """
    {
    "business": {
        "address": "via Stradone, 51",
        "city": "Campo San Martino",
        "class": "com.novadart.novabill.domain.Business",
        "country": "Italia",
        "creationTime": null,
        "email": "giordano.battilana@novadart.com",
        "fax": "0498597898",
        "id": 1,
        "mobile": "3334927614",
        "name": "Novadart S.n.c. di Giordano Battilana &amp; C.",
        "password": "17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187",
        "phone": "3334927614",
        "postcode": "35010",
        "province": "PD",
        "ssn": "04534730280",
        "vatID": "04534730280",
        "version": 2,
        "web": ""
    },
    "class": "com.novadart.novabill.domain.Invoice",
    "client": {
        "address": "via Vezzi, 12",
        "business": {
            "address": "via Stradone, 51",
            "city": "Campo San Martino",
            "class": "com.novadart.novabill.domain.Business",
            "country": "Italia",
            "creationTime": null,
            "email": "giordano.battilana@novadart.com",
            "fax": "0498597898",
            "id": 1,
            "mobile": "3334927614",
            "name": "Novadart S.n.c. di Giordano Battilana &amp; C.",
            "password": "17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187",
            "phone": "3334927614",
            "postcode": "35010",
            "province": "PD",
            "ssn": "04534730280",
            "vatID": "04534730280",
            "version": 2,
            "web": ""
        },
        "city": "Nervesa della Battaglia",
        "class": "com.novadart.novabill.domain.Client",
        "country": "Italia",
        "email": "",
        "fax": "",
        "id": 2,
        "mobile": "",
        "name": "General Alarm Eredi sas di Borsato Andrea",
        "phone": "",
        "postcode": "31040",
        "province": "TV",
        "ssn": "",
        "vatID": "04085780262",
        "version": 2,
        "web": ""
    },
    "id": 1,
    "invoiceDate": "17/02/2012",
    "invoiceID": 1,
    "invoiceItems": [{
        "class": "com.novadart.novabill.domain.InvoiceItem",
        "creationTime": 1329497798273,
        "description": "Fixed tap that was leaking some water",
        "id": 7,
        "price": "22.00",
        "quantity": "1.0",
        "tax": 2100,
        "total": "26.62",
        "totalBeforeTax": "22.00",
        "totalTax": "4.62",
        "unitOfMeasure": "hour",
        "version": 0
    }, {
        "class": "com.novadart.novabill.domain.InvoiceItem",
        "creationTime": 1329497798274,
        "description": "Fixed the dog that was leaking some water",
        "id": 8,
        "price": "33.00",
        "quantity": "1.0",
        "tax": 2100,
        "total": "39.93",
        "totalBeforeTax": "33.00",
        "totalTax": "6.93",
        "unitOfMeasure": "dog",
        "version": 0
    }, {
        "class": "com.novadart.novabill.domain.InvoiceItem",
        "creationTime": 1329497798275,
        "description": "Fixed table that was not stable",
        "id": 9,
        "price": "50.00",
        "quantity": "3.0",
        "tax": 2100,
        "total": "181.50",
        "totalBeforeTax": "150.00",
        "totalTax": "31.50",
        "unitOfMeasure": "piece",
        "version": 0
    }, {
        "class": "com.novadart.novabill.domain.InvoiceItem",
        "creationTime": 1329497798275,
        "description": "Change lamp",
        "id": 10,
        "price": "10.00",
        "quantity": "1.0",
        "tax": 2100,
        "total": "12.10",
        "totalBeforeTax": "10.00",
        "totalTax": "2.10",
        "unitOfMeasure": "piece",
        "version": 0
    }],
    "invoiceYear": 2012,
    "note": "Yeah, I really mean what I wrote on the payment note!!! Let\'s also try some strange letters: &egrave;&ograve;&agrave;&ugrave;&igrave;!&quot;&#163;$%&amp;/()=?^*&ccedil;&eacute;&#176;&#167;_",
    "paymentDueDate": null,
    "paymentNote": "Well you should definitely pay me in time, unless you want me to get down there and beat the crap out of you",
    "paymentType": "CASH",
    "total": "260.15",
    "totalBeforeTax": "215.00",
    "totalTax": "45.15",
    "version": 0
}
    """
    outputFile = "/tmp/testInvoice.pdf"
    if exists(outputFile):
        remove(outputFile)
    import json
    create_invoice("/tmp/testInvoice.pdf", json.loads(testInvoiceJSON))
    
    
