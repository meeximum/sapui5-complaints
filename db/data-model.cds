namespace egger.qm;
using { User, Country, cuid, managed } from '@sap/cds/common';

entity Orders {
  key AUFNR : String;
  name   : String;
  purchaseOrder: String;
  createdAt: Timestamp;
  items  : Association to many OrderItems on items.parent = $self.AUFNR;
}

entity OrderItems {
  key POSNR : String;
  key parent : String;
  matnr : String;
  makt: String;
  quantity: Double;
  unit: String;
}

entity Payments {
  key ID : String;
  complaint: UUID;
  amount: Double;
  currency: String;
  paymentAt: Date;
}

entity Comments : cuid, managed {
  complaint: UUID;
  comment : String;
}

entity Attachments : cuid, managed {
  complaint: UUID;
  filename : String;
  decription : String;
}

entity Codes {
  key CODE : String;
  name   : localized String;
}

entity Status {
  key STATUS : String;
  name   : localized String;
}

entity Complaints : cuid, managed {
  complaintNo: String;
  aufnr   : String;
  matnr   : String; 
  kmatnr  : String;
  makt    : String;
  delivery: String;
  kunnr   : String;
  werks   : String;
  menge   : Double;
  unit    : String;
  description : String;
  code : String;
  status : String;
  attachments : Association to many Attachments on attachments.complaint = $self.ID;
  comments : Association to many Comments on comments.complaint = $self.ID;
  payment : Association to Payments on payment.complaint = $self.ID;
}