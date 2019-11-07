package com.egger;

import java.util.HashMap;
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

public class ComplaintsService {

    private static final Logger LOG = CloudLoggerFactory.getLogger(ComplaintsService.class.getName());
    
    @AfterCreate(entity = "Complaints", serviceName = "ComplaintsService")
    public CreateResponse afterCreateProduct2(CreateRequest cr, CreateResponseAccessor productResponse, ExtensionHelper helper) {

		EntityData ed = productResponse.getEntityData();
		//EntityData edNew = EntityData.getBuilder(ed).addElement("SupplierId", 1).buildEntityData("Product"); //adding one more property if required. Eg: Transient Fields
		LOG.error("After Create Trigger:" + ed.toString());
		//return productResponse.getOriginalResponse(); //use this API if no change is required and the original response can be returned as output. Validation on the response can be done. If validation passes, the original response can be sent back.
		return CreateResponse.setSuccess().setData(ed).response(); //use this API if the response is modified. Can be used if some property is added or removed from the response.
		//return CreateResponse.setError(ErrorResponse.getBuilder().setMessage("Post create error!!").response()); //use this API if there is an error in the response that has to be returned. Validation can be done on the response, if the validation fails, error can be returned.

	}
	
	@Action(Name = "setStatus", serviceName = "ComplaintsService")
	public OperationResponse setStatusAction(OperationRequest actionRequest, ExtensionHelper extensionHelper) {
		String status = (String)actionRequest.getParameters().get("status");
		EntityData entityDataComplaint = (EntityData)actionRequest.getParameters().get("complaint");
		DataSourceHandler handler = extensionHelper.getHandler();
		//List<String> complaintAttributes = new ArrayList<String>();
		//EntityData entityDataComplaint = handler.executeRead("Complaints", keysForProductsInBasket, complaintAttributes);
		
		Map<String, Object> keysForUpdate = new HashMap<String, Object>();
        keysForUpdate.put("status", status);
		
		try {
			handler.executeUpdate(entityDataComplaint, keysForUpdate, false);
		} catch (DatasourceException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			ErrorResponse err = ErrorResponse.getBuilder().setMessage("Faild to update status. Check log for more details.").setStatusCode(500).response();
			return OperationResponse.setError(err);
		}
		return OperationResponse.setSuccess().response();
	}

/*
    @BeforeRead(entity = "Orders", serviceName = "CatalogService")
    public BeforeReadResponse beforeReadOrders(ReadRequest req, ExtensionHelper h) {
        LOG.error("##### Orders - beforeReadOrders ########");
        return BeforeReadResponse.setSuccess().response();
    }

    @AfterRead(entity = "Orders", serviceName = "CatalogService")
    public ReadResponse afterReadOrders(ReadRequest req, ReadResponseAccessor res, ExtensionHelper h) {
        EntityData ed = res.getEntityData();
        EntityData ex = EntityData.getBuilder(ed).addElement("amount", 1000).buildEntityData("Orders");
        return ReadResponse.setSuccess().setData(ex).response();
    }

    @AfterQuery(entity = "Orders", serviceName = "CatalogService")
    public QueryResponse afterQueryOrders(QueryRequest req, QueryResponseAccessor res, ExtensionHelper h) {
        List<EntityData> dataList = res.getEntityDataList(); // original list
        List<EntityData> modifiedList = new ArrayList<EntityData>(dataList.size()); // modified list
        for (EntityData ed : dataList) {
            EntityData ex = EntityData.getBuilder(ed).addElement("amount", 1000).buildEntityData("Orders");
            modifiedList.add(ex);
        }
        return QueryResponse.setSuccess().setData(modifiedList).response();
    }
*/
}