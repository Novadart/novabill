import json
import tempfile
import os
import webob
import legacy
import base64
from core import create_doc

def process_input_parameters(request):
    form = request.POST
    params = dict()
    params["docData"] = json.loads(form["docData"].decode("unicode-escape")) #decode json
    if "logoData" in form:
        _, logoFPath = tempfile.mkstemp()
        with open(logoFPath) as f:
            f.write(base64.b64decode(form["logoData"]))
        params["pathToLogo"] = logoFPath
    if "logoWidth" in form:
        params["logoWidth"] = form["logoWidth"]
    if "logoHeight" in form:
        params["logoHeight"] = form["logoHeight"]
    if "docType" in form:
        params["docType"] = int(form["docType"])
    if "tempType" in form:
        params["tempType"] = int(form["tempType"])
    if "watermark" in form:
        params["watermark"] = form["watermark"].lower() in ["yes", "true", "1"]
    if "locale" in form:
        params["locale"] = form["locale"]
    return params

BLOCK_SIZE = 100*1024 #100Kb

def send_file(fpath):
    try:
        with open(fpath) as f:
            block = f.read(BLOCK_SIZE)
            while block:
                yield block
                block = f.read(BLOCK_SIZE)
    finally:
        os.remove(fpath)

def application(environ, start_response):
    request = webob.Request(environ)
    params = process_input_parameters(request)
    _, out = tempfile.mkstemp()
    if params["docData"]["client"]["vatID"] == legacy.ENNOVA_VATID:
        legacy.create_invoice(out, **params)
    else:
        create_doc(out, **params)
    if "pathToLogo" in params:
        os.remove(params["pathToLogo"])
    size = os.path.getsize(out)
    status = "200 OK"
    response_headers = [("Content-type", "application/pdf"), ("Content-length", str(size))]
    start_response(status, response_headers)
    return send_file(out)