insert into business (id, name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatid, ssn, version) values (1, 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', '35010', 'Campo San Martino', 'PD', 'IT', 'giordano.battilana@novadart.com', '3334927614', '3334927614', '0498597898', '', 'IT04534350280', 'IT04534350280', 1);
insert into principal (id, username, password, version, business, enabled, notes_bit_mask) values (1, 'giordano.battilana@novadart.com', '17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187', 1, 1, 't', -1);
insert into principal_granted_roles (principal, granted_roles) values (1, 0);
insert into business (id, name, address, postcode, city, province, country, email, phone, mobile, fax, web, vatid, ssn, version) values (2, 'Novadart S.n.c. di Giordano Battilana & C.', 'via Stradone, 51', '35010', 'Campo San Martino', 'PD', 'IT', 'risto.gligorov@novadart.com', '3334927614', '3334927614', '0498597898', '', 'IT04534350280', 'IT04534350280', 1);
insert into principal (id, username, password, version, business, enabled, notes_bit_mask) values (2, 'risto.gligorov@novadart.com', '17f3fdc0520bbf0588b41bf45c0d68ad0da26c80d3dc466a96a8215b2a4de187', 1, 2, 't', -1)
insert into principal_granted_roles (principal, granted_roles) values (2, 0);
insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id, contact_first_name, contact_last_name, contact_email, contact_phone, contact_fax, contact_mobile, default_layout_type) values ('via Qualche Strada con Nome Lungo, 12', 'Nervesa della Battaglia', 'IT', '', '', '', 'Toby Margot', '', '42837', 'PD', '', 'IT04231156211', 1, '', 1, 3, '', '', '', '', '', '', 1);
--insert into client (address, city, country, email, fax, mobile, name, phone, postcode, province, ssn, vatid, version, web, business, id) values ('via Oca, 22', 'La Grande Città', 'Francia', '', '', '', 'Perin S.r.l.', '', '31040', 'PD', '', '0408523132', 1, '', 1, 3);
alter sequence hibernate_sequence restart with 6;