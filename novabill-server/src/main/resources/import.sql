insert into business (id, name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatid, ssn, default_layout_type, version) values (1, 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', '35010', 'Campo San Martino', 'PD', 'IT', 'giordano.battilana@novadart.com', '3334927614', '3334927614', '0498597898', '', 'IT04534350280', 'IT04534350280', 1, 1);
insert into principal (id, username, password, version, business, enabled, notes_bit_mask) values (1, 'giordano.battilana@novadart.com', '17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187', 1, 1, 't', -1);
insert into principal_granted_roles (principal, granted_roles) values (1, 0);
insert into business (id, name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatid, ssn, default_layout_type, version) values (2, 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', '35010', 'Campo San Martino', 'PD', 'IT', 'risto.gligorov@novadart.com', '3334927614', '3334927614', '0498597898', '', 'IT04534350280', 'IT04534350280', 0, 1);
insert into principal (id, username, password, version, business, enabled, notes_bit_mask) values (2, 'risto.gligorov@novadart.com', '17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187', 1, 2, 't', -1)
insert into principal_granted_roles (principal, granted_roles) values (2, 0);

--creating price lists
insert into price_list (id, name, version, business) values (161, '::default', '1', 1);
update client set default_price_list=161 where business=1;
insert into price_list (id, name, version, business) values (162, '::default', '1', 2);

insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Toby Margot', '', '42837', 'PD', '', 'IT04231156211', 1, '', 1, 3, '', '', '', '', '', '', 161);

--creating bunch of clients
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Albert Buch', '', '42837', 'PD', '', 'IT04235746211', 1, '', 1, 6, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Bob Corse', '', '42837', 'PD', '', 'IT04243756211', 1, '', 1, 7, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Carmen Devon', '', '42837', 'PD', '', 'IT04235756211', 1, '', 1, 8, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Donald Grosà', '', '42837', 'PD', '', 'IT04231236211', 1, '', 1, 9, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Erwin Hum', '', '42837', 'PD', '', 'IT04543756211', 1, '', 1, 10, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Francis Delavor', '', '42837', 'PD', '', 'IT042357565671', 1, '', 1, 11, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Gregor Tuum', '', '42837', 'PD', '', 'IT04238676211', 1, '', 1, 12, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Horatio Nate', '', '42837', 'PD', '', 'IT04235234211', 1, '', 1, 13, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Ivan Moche', '', '42837', 'PD', '', 'IT04235763211', 1, '', 1, 14, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Liu Khan', '', '42837', 'PD', '', 'IT04236436211', 1, '', 1, 15, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Marshall Kitch', '', '42837', 'PD', '', 'IT04275756211', 1, '', 1, 16, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Alexander Kopè', '', '42837', 'PD', '', 'IT04235444411', 1, '', 1, 17, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Meron Theron', '', '42837', 'PD', '', 'IT04235754311', 1, '', 1, 18, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Nathan Brave', '', '42837', 'PD', '', 'IT04239656211', 1, '', 1, 19, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Ian Donovan', '', '42837', 'PD', '', 'IT04235446211', 1, '', 1, 20, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Hermes Feed', '', '42837', 'PD', '', 'IT04235766611', 1, '', 1, 21, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Solomon Kane', '', '42837', 'PD', '', 'IT044345756211', 1, '', 1, 22, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Zerai Poorte', '', '42837', 'PD', '', 'IT044435756211', 1, '', 1, 23, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Strump Koor', '', '42837', 'PD', '', 'IT04233756211', 1, '', 1, 24, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Erin Jager', '', '42837', 'PD', '', 'IT04237756211', 1, '', 1, 25, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Cathy Reeve', '', '42837', 'PD', '', 'IT04235000211', 1, '', 1, 26, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Parker Bowls', '', '42837', 'PD', '', 'IT04235987211', 1, '', 1, 27, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Jim Nate', '', '42837', 'PD', '', 'IT04235876211', 1, '', 1, 28, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Steve Potter', '', '42837', 'PD', '', 'IT04235956211', 1, '', 1, 29, '', '', '', '', '', '', 161);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Zachary Kool', '', '42837', 'PD', '', 'IT04239876211', 1, '', 1, 30, '', '', '', '', '', '', 161);

--creating invoice with many items
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (31, '2014-10-20', 2014, 1, '', '', 6050.0, 5000.0, 1050.0, 0, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 31);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (31, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (32, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (33, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (34, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (35, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (36, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (37, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (38, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (39, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (40, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (41, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (42, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (43, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (44, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (45, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (46, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (47, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (48, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (49, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (50, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (51, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (52, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (53, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (54, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (55, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (56, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (57, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (58, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (59, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (60, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (61, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (62, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (63, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (64, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (65, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (66, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (67, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (68, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (69, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (70, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (71, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (72, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (73, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (74, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (75, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (76, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (77, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (78, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (79, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (80, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (81, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 31);

-- creating many invoices
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (82, '2014-10-20', 2014, 3, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 82);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (82, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (83, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 82);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (84, '2014-10-20', 2014, 4, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 84);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (84, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (85, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 84);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (86, '2014-10-20', 2014, 5, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 86);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (86, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (87, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 86);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (88, '2014-10-20', 2014, 6, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 88);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (88, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (89, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 88);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (90, '2014-10-20', 2014, 7, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 90);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (90, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (91, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 90);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (92, '2014-10-20', 2014, 8, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 92);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (92, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (93, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 92);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (94, '2014-10-20', 2014, 9, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 94);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (94, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (95, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 94);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (96, '2014-10-20', 2014, 10, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 96);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (96, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (97, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 96);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (98, '2014-10-20', 2014, 11, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 98);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (98, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (99, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 98);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (100, '2014-10-20', 2014, 12, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 100);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (100, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (101, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 100);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (102, '2014-10-20', 2014, 13, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 102);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (102, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (103, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 102);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (104, '2014-10-20', 2014, 14, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 104);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (104, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (105, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 104);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (106, '2014-10-20', 2014, 15, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 106);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (106, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (107, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 106);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (108, '2014-10-20', 2014, 16, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 108);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (108, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (109, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 108);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (110, '2014-10-20', 2014, 17, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 110);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (110, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (111, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 110);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (112, '2014-10-20', 2014, 18, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 112);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (112, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (113, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 112);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (114, '2014-10-20', 2014, 19, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 114);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (114, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (115, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 114);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (116, '2014-10-20', 2014, 20, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 116);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (116, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (117, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 116);


insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (118, '2014-10-20', 2014, 21, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 118);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (118, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (119, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 118);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (120, '2014-10-20', 2014, 22, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 120);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (120, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (121, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 120);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (122, '2014-10-20', 2014, 23, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 122);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (122, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (123, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 122);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (124, '2014-10-20', 2014, 24, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 124);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (124, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (125, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 124);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (126, '2014-10-20', 2014, 25, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 126);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (126, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (127, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 126);

--creating one client for second registered business
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_price_list) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Groovy Butcher', '', '42837', 'PD', '', 'IT04235906211', 1, '', 2, 128, '', '', '', '', '', '', 162);

--creating one invoice for second registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (129, '2014-10-20', 2014, 25, '', '', 121.0, 100.0, 21.0, 0, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 129);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (129, 0, 0, 'default', 2, 128);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (129, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 129);

--creating creditnote for first registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (130, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 130);
insert into credit_note (id, business, client) values (130, 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (131, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 130);

--creating creditnote for second registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (132, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 0, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2014-11-20', 132);
insert into credit_note (id, business, client) values (132, 2, 128);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (133, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 132);

--creating estimation for first registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (134, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into estimation (limitations, valid_till, id, business, client) values ('', '2014-11-20', 134, 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (135, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 134);

--creating estimation for second registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (136, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 0, 1);
insert into estimation (limitations, valid_till, id, business, client) values ('', '2014-11-20', 136, 2, 128);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (137, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 136);

--creating transport document for first registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (138, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into transport_document (cause, from_company_name, from_street, from_city, from_postcode, from_province, from_country, to_company_name, to_street, to_city, to_postcode, to_province, to_country, number_of_packages, trade_zone, transport_start_date, transportation_responsibility, transporter, id, business, client) values ('', 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', 'Campo San Martino', '35010', 'PD', 'IT', 'The mighty company from this Young Entrepreneur', 'via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', '42837', 'PD', 'IT', 1 , '', '2014-10-20', '', '', 138, 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (139, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 138);

--creating transport document for the second registered business
insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (140, '2014-10-20', 2014, 1, '', '', 121.0, 100.0, 21.0, 0, 1);
insert into transport_document (cause, from_company_name, from_street, from_city, from_postcode, from_province, from_country, to_company_name, to_street, to_city, to_postcode, to_province, to_country, number_of_packages, trade_zone, transport_start_date, transportation_responsibility, transporter, id, business, client) values ('', 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', 'Campo San Martino', '35010', 'PD', 'IT', '	 company from this Young Entrepreneur', 'via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', '42837', 'PD', 'IT', 1 , '', '2014-10-20', '', '', 140, 2, 128);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (141, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 140);

--creating payment types
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (142, 'Pagamento in Rimessa Diretta', 'Rimessa Diretta', 0, 0, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (143, 'Pagamento con bonifico bancario entro 30 giorni', 'Bonifico Bancario 30GG', 1, 0, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (144, 'Pagamento con bonifico bancario entro 60 giorni', 'Bonifico Bancario 60GG', 2, 0, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (145, 'Pagamento con bonifico bancario entro 90 giorni', 'Bonifico Bancario 90GG', 3, 0, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (146, 'Pagamento con bonifico bancario entro 30 giorni d.f. f.m.', 'Bonifico Bancario 30GG d.f. f.m.', 1, 1, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (147, 'Pagamento con bonifico bancario entro 60 giorni d.f. f.m.', 'Bonifico Bancario 60GG d.f. f.m.', 2, 1, 0, 1, 1);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (148, 'Pagamento con bonifico bancario entro 90 giorni d.f. f.m.', 'Bonifico Bancario 90GG d.f. f.m.', 3, 1, 0, 1, 1);

insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (149, 'Pagamento in Rimessa Diretta', 'Rimessa Diretta', 0, 0, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (150, 'Pagamento con bonifico bancario entro 30 giorni', 'Bonifico Bancario 30GG', 1, 0, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (151, 'Pagamento con bonifico bancario entro 60 giorni', 'Bonifico Bancario 60GG', 2, 0, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (152, 'Pagamento con bonifico bancario entro 90 giorni', 'Bonifico Bancario 90GG', 3, 0, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (153, 'Pagamento con bonifico bancario entro 30 giorni d.f. f.m.', 'Bonifico Bancario 30GG d.f. f.m.', 1, 1, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (154, 'Pagamento con bonifico bancario entro 60 giorni d.f. f.m.', 'Bonifico Bancario 60GG d.f. f.m.', 2, 1, 0, 1, 2);
insert into payment_type (id, default_payment_note, name, payment_date_delta, payment_date_generator, payment_delta_type, version, business) values (155, 'Pagamento con bonifico bancario entro 90 giorni d.f. f.m.', 'Bonifico Bancario 90GG d.f. f.m.', 3, 1, 0, 1, 2);

--creating commodities
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (156, 'sku1', 'Computer network configuration', 't', 22, 'hour', 1, 1);
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (157, 'sku2', 'Website building', 't', 22, 'hour', 1, 1);
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (158, 'sku3', 'Mobile apps development', 't', 22, 'hour', 1, 1);
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (159, 'sku4', 'Database administration', 't', 22, 'hour', 1, 1);
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (160, 'sku5', 'Cisco network router', 'f', 22, 'piece', 1, 1);

--creating default prices
insert into price (id, price_type, price_value, version, commodity, price_list) values (163, 1, 24.95, 1, 156, 161);
insert into price (id, price_type, price_value, version, commodity, price_list) values (164, 1, 19.95, 1, 157, 161);
insert into price (id, price_type, price_value, version, commodity, price_list) values (165, 1, 24.95, 1, 158, 161);
insert into price (id, price_type, price_value, version, commodity, price_list) values (167, 1, 24.95, 1, 159, 161);
insert into price (id, price_type, price_value, version, commodity, price_list) values (168, 1, 39.95, 1, 160, 161);

--creating more commodities
insert into commodity (id, sku, description, service, tax, unit_of_measure, business, version) values (169, '12345', 'Website building', 't', 22, 'hour', 2, 1);

--creating default prices
insert into price (id, price_type, price_value, version, commodity, price_list) values (170, 1, 19.95, 1, 169, 162);

--creating custom price lists
insert into price_list (id, name, version, business) values (171, 'custom pricelist', '1', 1);
insert into price_list (id, name, version, business) values (172, 'custom pricelist', '1', 2);

--creating prices for the custom price list
insert into price (id, price_type, price_value, version, commodity, price_list) values (173, 1, 20.95, 1, 156, 171);

insert into price (id, price_type, price_value, version, commodity, price_list) values (174, 1, 20.95, 1, 169, 172);


insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (175, '2013-10-20', 2013, 3, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2013-11-20', 175);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (175, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (176, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 175);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (177, '2012-10-20', 2012, 4, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2012-11-20', 177);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (177, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (178, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 177);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (179, '2010-10-20', 2010, 5, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2010-11-20', 179);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (179, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (180, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 179);

insert into accounting_document (id, accounting_document_date, accounting_document_year, documentid, note, payment_note, total, total_before_tax, total_tax, layout_type, version) values (181, '2009-10-20', 2009, 6, '', '', 121.0, 100.0, 21.0, 1, 1);
insert into abstract_invoice (payed, payment_due_date, id) values ('f', '2009-11-20', 181);
insert into invoice (id, payment_date_generator, payment_date_delta, payment_type_name, business, client) values (181, 0, 0, 'default', 1, 3);
insert into accounting_document_item (id, description, price, quantity, tax, total, total_before_tax, total_tax, unit_of_measure, version, accounting_document) values (182, 'description', 100.0, 1.0, 21.0, 121.0, 100.0, 21.0, 'piece', 1, 181);

alter sequence hibernate_sequence restart with 183;
