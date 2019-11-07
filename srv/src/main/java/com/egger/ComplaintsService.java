package com.egger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.Action;
import com.sap.cloud.sdk.service.prov.api.annotations.AfterCreate;
import com.sap.cloud.sdk.service.prov.api.exception.DatasourceException;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.request.OperationRequest;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponseAccessor;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.OperationResponse;

import org.slf4j.Logger;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class ComplaintsService {

	private static final Logger LOG = CloudLoggerFactory.getLogger(ComplaintsService.class.getName());

	@AfterCreate(entity = "Complaints", serviceName = "ComplaintsService")
	public CreateResponse afterCreateProduct2(CreateRequest cr, CreateResponseAccessor productResponse,
			ExtensionHelper helper) {

		EntityData ed = productResponse.getEntityData();
		// EntityData edNew = EntityData.getBuilder(ed).addElement("SupplierId",
		// 1).buildEntityData("Product"); //adding one more property if required. Eg:
		// Transient Fields
		LOG.error("After Create Trigger:" + ed.toString());
		// return productResponse.getOriginalResponse(); //use this API if no change is
		// required and the original response can be returned as output. Validation on
		// the response can be done. If validation passes, the original response can be
		// sent back.
		return CreateResponse.setSuccess().setData(ed).response(); // use this API if the response is modified. Can be
																	// used if some property is added or removed from
																	// the response.
		// return CreateResponse.setError(ErrorResponse.getBuilder().setMessage("Post
		// create error!!").response()); //use this API if there is an error in the
		// response that has to be returned. Validation can be done on the response, if
		// the validation fails, error can be returned.

	}

	@Action(Name = "setStatus", serviceName = "ComplaintsService")
	public OperationResponse setStatusAction(OperationRequest actionRequest, ExtensionHelper extensionHelper) {
		String status = (String) actionRequest.getParameters().get("status");
		String entityId = (String) actionRequest.getParameters().get("complaint");

		DataSourceHandler handler = extensionHelper.getHandler();
		List<String> complaintIDs = new ArrayList<String>();
		complaintIDs.add("ID");
		Map<String, Object> keysForCreate = new HashMap<String, Object>();
		keysForCreate.put("ID", entityId);
		keysForCreate.put("status", status);
		// EntityData entityDataComplaint = handler.executeRead("Complaints",
		// keysForUpdate, complaintAttributes);
		EntityData entityDataComplaint = EntityData.createFromMap(keysForCreate, complaintIDs, "Complaints");

		try {
			Map<String, Object> keysForUpdate = new HashMap<String, Object>();
			keysForUpdate.put("ID", entityId);
			handler.executeUpdate(entityDataComplaint, keysForUpdate, false);
		} catch (DatasourceException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			ErrorResponse err = ErrorResponse.getBuilder()
					.setMessage("Faild to update status. Check log for more details.").setStatusCode(500).response();
			return OperationResponse.setError(err);
		}
		return OperationResponse.setSuccess().response();
	}

	@Action(Name = "setCode", serviceName = "ComplaintsService")
	public OperationResponse setCodeAction(OperationRequest actionRequest, ExtensionHelper extensionHelper) {
		String useSapAsString = (String) actionRequest.getParameters().get("sap");
		Boolean useSap = "true".equalsIgnoreCase(useSapAsString);
		String text = (String) actionRequest.getParameters().get("text");
		String entityId = (String) actionRequest.getParameters().get("complaint");

		String code = null;
		if (useSap) {
			JSONObject json = Unirest.post(
					"https://mlfproduction-text-classifier.cfapps.eu10.hana.ondemand.com/api/v2/text/classification/models/complaint-01/versions/1")
					.header("authorization",
							"Bearer eyJhbGciOiJSUzI1NiIsImprdSI6Imh0dHBzOi8vbW9kZTJnYXJhZ2UuYXV0aGVudGljYXRpb24uZXUxMC5oYW5hLm9uZGVtYW5kLmNvbS90b2tlbl9rZXlzIiwia2lkIjoia2V5LWlkLTEiLCJ0eXAiOiJKV1QifQ.eyJqdGkiOiIyMTQ3Y2Q1Nzk2NjQ0YThmODdiMjNhMTI3NjhkMGMxYyIsImV4dF9hdHRyIjp7ImVuaGFuY2VyIjoiWFNVQUEiLCJ6ZG4iOiJtb2RlMmdhcmFnZSIsInNlcnZpY2VpbnN0YW5jZWlkIjoiZTBhNDcwY2MtNmZlNy00ODYzLWJkODctODhhNzYwZmQ4NzlhIn0sInN1YiI6InNiLWUwYTQ3MGNjLTZmZTctNDg2My1iZDg3LTg4YTc2MGZkODc5YSFiODA0MHxtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwIiwiYXV0aG9yaXRpZXMiOlsibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5kYXRhbWFuYWdlbWVudC5yZWFkIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5yZXRyYWluc2VydmljZS5yZWFkIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tb2RlbGRlcGxveW1lbnQuYWxsIiwidWFhLnJlc291cmNlIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tb2RlbHNlcnZpY2UucmVhZCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAuc3RvcmFnZWFwaS5hbGwiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1laC5hbGwiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLmRhdGFtYW5hZ2VtZW50LndyaXRlIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5mdW5jdGlvbmFsc2VydmljZS5hbGwiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLnJlc291cmNlcGxhbnNlcnZpY2UuYWxsIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tb2RlbHJlcG8ucmVhZCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAucmV0cmFpbnNlcnZpY2Uud3JpdGUiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1vZGVsbWV0ZXJpbmcucmVhZCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAubW9kZWxyZXBvLndyaXRlIl0sInNjb3BlIjpbIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAuZGF0YW1hbmFnZW1lbnQucmVhZCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAucmV0cmFpbnNlcnZpY2UucmVhZCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAubW9kZWxkZXBsb3ltZW50LmFsbCIsInVhYS5yZXNvdXJjZSIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAubW9kZWxzZXJ2aWNlLnJlYWQiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLnN0b3JhZ2VhcGkuYWxsIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tZWguYWxsIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5kYXRhbWFuYWdlbWVudC53cml0ZSIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAuZnVuY3Rpb25hbHNlcnZpY2UuYWxsIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5yZXNvdXJjZXBsYW5zZXJ2aWNlLmFsbCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAubW9kZWxyZXBvLnJlYWQiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLnJldHJhaW5zZXJ2aWNlLndyaXRlIiwibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tb2RlbG1ldGVyaW5nLnJlYWQiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1vZGVscmVwby53cml0ZSJdLCJjbGllbnRfaWQiOiJzYi1lMGE0NzBjYy02ZmU3LTQ4NjMtYmQ4Ny04OGE3NjBmZDg3OWEhYjgwNDB8bWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MCIsImNpZCI6InNiLWUwYTQ3MGNjLTZmZTctNDg2My1iZDg3LTg4YTc2MGZkODc5YSFiODA0MHxtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwIiwiYXpwIjoic2ItZTBhNDcwY2MtNmZlNy00ODYzLWJkODctODhhNzYwZmQ4NzlhIWI4MDQwfG1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAiLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwicmV2X3NpZyI6IjM0MTRiZTlhIiwiaWF0IjoxNTczMTM3MTE4LCJleHAiOjE1NzMxODAzMTgsImlzcyI6Imh0dHA6Ly9tb2RlMmdhcmFnZS5sb2NhbGhvc3Q6ODA4MC91YWEvb2F1dGgvdG9rZW4iLCJ6aWQiOiJmYzFiNTBkZC0wZWM3LTQ0MzYtYTdhMy02NzM0M2I0YWFkMjkiLCJhdWQiOlsibWwtZm91bmRhdGlvbi14c3VhYS1zdGQhYjU0MC5tb2RlbHNlcnZpY2UiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1laCIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAucmV0cmFpbnNlcnZpY2UiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1vZGVsZGVwbG95bWVudCIsInVhYSIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAuZnVuY3Rpb25hbHNlcnZpY2UiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLmRhdGFtYW5hZ2VtZW50Iiwic2ItZTBhNDcwY2MtNmZlNy00ODYzLWJkODctODhhNzYwZmQ4NzlhIWI4MDQwfG1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLnN0b3JhZ2VhcGkiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLnJlc291cmNlcGxhbnNlcnZpY2UiLCJtbC1mb3VuZGF0aW9uLXhzdWFhLXN0ZCFiNTQwLm1vZGVscmVwbyIsIm1sLWZvdW5kYXRpb24teHN1YWEtc3RkIWI1NDAubW9kZWxtZXRlcmluZyJdfQ.sWLpExw-kW77PmvRD63crYZkEyax36bSV4__npZHqiZmHKWOabPgPatDg96M9e0TJRtHDzp5skJ5emDadYMKISpjkAuRbY_suMpnv8MH-M1Xgu4x68RWKdeSs4K9-nqsnd8FEgl0r45jdxZaD7TONMJgXLipZuEOOt_VevfPo9GMVe-YyWV9KJmFhfi8s8lm-3vxxgbXmXR1W_s3yCAbJg3s0CnnWH2ZafZ8IunfYh5JYe9R9XCkRA67d1CJWAU6QFvW3FtQC1Xs5fWIDxtYnP67VodbfDH_vLwNpdJ4GCeyF6vKz3Vhu2bhTQONGCcHZCG-MsHuWWmD0BTm3bT91xjdWzP43GLy8AD0kVTdBMdXnBF-PsKrnR3-AWcrRYoEWWkSow1Irbc_EoH_8qIMF7rk3fCj4LXnLrC_-geFcMoDYwz1XIJy6nXQD0F5Nd3HVFlyphqgYnt0ejX0IWpgxQACSdPfHh01ZJvyxC0nW80R2I4Nz2l9AWTH-jEyKUVTKmu3PpyzE8MZSmdcAZ6_yd4NnhrziSuE49EX4o5pmSR0SUizDh0WvTWwfSJznxMmln-ArPCFJCcQYqsnRDMfmLobP5e9JkXGe3HnJR9nq1YhjeMzfTDquq30x4ZTdWTKx5ASLP3PwmSrABFgU-2TNVfgBqkRHP_FHDloRRxivfs")
					.header("cache-control", "no-cache").field("texts", text).asJson().getBody().getObject();

			code = (String) json.query("/predictions/0/results/0/label");
			// Double score = (Double)json.query("/predictions/0/results/0/score");
		} else {
			JSONObject json2 = Unirest.post(
					"https://automl.googleapis.com/v1beta1/projects/663868982523/locations/us-central1/models/TCN3178648533481816064:predict")
					.header("content-type", "application/json")
					.header("authorization",
							"Bearer ya29.c.Kl6vB_Cm97MLRUoE5C4ct7xAPMH0-7xP7rKPe-4c7JJ5pKPEy_csU0hw0uheeSNM-XcIe5BG3njwsxMbXs4Pb7FHKkQTLHs_Ynn8hN-P_uDeca5LK-cvuwnZ04er8oGe")
					.header("cache-control", "no-cache")
					.body("{ \"payload\": { \"textSnippet\": { \"content\": \"Preis falsch\", \"mime_type\": \"text/plain\" } } }")
					.asJson().getBody().getObject();

			code = (String) json2.query("/payload/0/displayName");
			//Double score = (Double) json2.query("/payload/0/classification/score");
		}

		DataSourceHandler handler = extensionHelper.getHandler();
		List<String> complaintIDs = new ArrayList<String>();
		complaintIDs.add("ID");
		Map<String, Object> keysForCreate = new HashMap<String, Object>();
		keysForCreate.put("ID", entityId);
		keysForCreate.put("code", code);
		// EntityData entityDataComplaint = handler.executeRead("Complaints",
		// keysForUpdate, complaintAttributes);
		EntityData entityDataComplaint = EntityData.createFromMap(keysForCreate, complaintIDs, "Complaints");

		try {
			Map<String, Object> keysForUpdate = new HashMap<String, Object>();
			keysForUpdate.put("ID", entityId);
			handler.executeUpdate(entityDataComplaint, keysForUpdate, false);
		} catch (DatasourceException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			ErrorResponse err = ErrorResponse.getBuilder()
					.setMessage("Faild to update code. Check log for more details.").setStatusCode(500).response();
			return OperationResponse.setError(err);
		}
		return OperationResponse.setSuccess().response();
	}

	/*
	 * @BeforeRead(entity = "Orders", serviceName = "CatalogService") public
	 * BeforeReadResponse beforeReadOrders(ReadRequest req, ExtensionHelper h) {
	 * LOG.error("##### Orders - beforeReadOrders ########"); return
	 * BeforeReadResponse.setSuccess().response(); }
	 * 
	 * @AfterRead(entity = "Orders", serviceName = "CatalogService") public
	 * ReadResponse afterReadOrders(ReadRequest req, ReadResponseAccessor res,
	 * ExtensionHelper h) { EntityData ed = res.getEntityData(); EntityData ex =
	 * EntityData.getBuilder(ed).addElement("amount",
	 * 1000).buildEntityData("Orders"); return
	 * ReadResponse.setSuccess().setData(ex).response(); }
	 * 
	 * @AfterQuery(entity = "Orders", serviceName = "CatalogService") public
	 * QueryResponse afterQueryOrders(QueryRequest req, QueryResponseAccessor res,
	 * ExtensionHelper h) { List<EntityData> dataList = res.getEntityDataList(); //
	 * original list List<EntityData> modifiedList = new
	 * ArrayList<EntityData>(dataList.size()); // modified list for (EntityData ed :
	 * dataList) { EntityData ex = EntityData.getBuilder(ed).addElement("amount",
	 * 1000).buildEntityData("Orders"); modifiedList.add(ex); } return
	 * QueryResponse.setSuccess().setData(modifiedList).response(); }
	 */
}