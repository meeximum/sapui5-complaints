using { egger.qm, sap.common } from '../db/data-model';

service ComplaintsService {
  entity Codes @readonly as projection on qm.Codes;
  entity Orders @readonly as projection on qm.Orders;
  entity OrderItems @readonly as projection on qm.OrderItems;
  entity Complaints as projection on qm.Complaints;
  entity Attachments as projection on qm.Attachments;
  entity Payments as projection on qm.Payments;
  entity Comments as projection on qm.Comments;
  
  
  action setStatus (complaint:Complaints.ID, status:String);
}