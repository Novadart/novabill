'''
Created on Oct 21, 2012

@author: risto
'''
import random


def gen_alpha_str(n):
    letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
    return "".join(random.sample(letters * max(1, n / len(letters) + 1), n))

def gen_num_str(n):
    digits = "0123456789"
    return "".join(random.sample(digits * max(1, n / len(digits) + 1), n))


def gen_clients(num, start):
    ins_pattern = "insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile) values('%(address)s', '%(city)s', 'Italia', '', '', '', '%(name)s', '', '%(postcode)s', 'PD', '', '%(ssn)s', 1, '', 1, %(id)d, '', '', '', '', '', '');"
    return ([ins_pattern % dict(
            address="%s %s" % (gen_alpha_str(random.randint(1, 100)), gen_num_str(random.randint(1, 3))),
            city=gen_alpha_str(random.randint(1,50)),
            name=gen_alpha_str(random.randint(20,100)),
            postcode=gen_num_str(5),
            ssn="IT%s" % gen_num_str(11),
            id=start+i) for i in range(num)], start + num)
    

def gen_acc_doc_items(num, start, docId):
    ins_pattern = "insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (%(id)d, '%(des)s', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, %(docid)d);"
    return ([ins_pattern % dict(
        des=gen_alpha_str(random.randint(20, 500)), docid=docId, id=start+i
    ) for i in range(num)], start + num)
    
    
def gen_invoices(num, start, docIdStart, business, clients, numItems):
    acc_doc_ins_pattern = "insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, version) values (%(id)d, '2012-10-20', 2012, %(docId)d, '%(note)s', '', %(total).1f, %(totalBefTax).1f, %(tax).1f, 1);"
    abs_inv_ins_pattern = "insert into abstract_invoice (payed, payment_due_date, payment_type, id) values ('f', '2012-11-20', 0, %(id)d);"
    inv_ins_pattern = "insert into invoice (id, business, client) values (%(id)d, %(business)d, %(client)d);"
    ins_lines = []
    dbid = start
    for i in range(num):
        ins_lines.append(acc_doc_ins_pattern % dict(
            id=dbid,  docId=docIdStart+i, total=121.0 * numItems[i], totalBefTax=100.0 * numItems[i], tax=21.0 * numItems[i],
            note=gen_alpha_str(random.randint(10, 100))
        ))
        ins_lines.append(abs_inv_ins_pattern % dict(id=dbid))
        ins_lines.append(inv_ins_pattern % dict(id=dbid, business=business, client=random.sample(clients,1)[0]))
        dbid += 1
        items_ins_lines, dbid = gen_acc_doc_items(numItems[i], dbid, dbid-1) 
        ins_lines += items_ins_lines
    return (ins_lines, dbid)

def gen_sql_script(script_path, business, numClients, numInvs, numInvItems, start, docIdStart):
    client_inv_lines, dbid = gen_clients(numClients, start)
    clients = range(start, dbid)
    inv_ins_lines, dbid = gen_invoices(numInvs, dbid, docIdStart, business, clients, numInvItems)
    f = open(script_path, "w")
    f.write("\n".join(client_inv_lines + inv_ins_lines))
    inv_ins_lines, dbid = gen_invoices(1, dbid, docIdStart + numInvs, business, clients, [100]) #invoice with many items
    f.write("\n".join(inv_ins_lines))
    f.write("select setval('hibernate_sequence', %d);\n" % dbid)
    f.close()
