sap.ui.define([
	"./BaseController",
	"../model/formatter"
], function (BaseController, formatter) {
	"use strict";

	return BaseController.extend("com.egger.complaints.ui.controller.Create", {

		formatter: formatter,

		/**
		 * Called when a controller is instantiated and its View controls (if available) are already created.
		 * Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
		 * @memberOf com.egger.complaints.ui.view.Create
		 */
		onInit: function () {
			this.getRouter().getRoute("create").attachPatternMatched(this._onObjectMatched, this);
		},

		onOrderPress: function (oEvent) {
			var oContext = oEvent.getSource().getBindingContext();
			var oView = this.getView();
			var oWizard = oView.byId("wizard");
			oView.byId("step1").setValidated(true);
			oView.byId("itemsTable").setBindingContext(oContext);

			oEvent.getSource().setSelected(true);

			if (oWizard.getCurrentStep().indexOf("step1") >= 0) {
				oWizard.nextStep();
			} else {
				oWizard.goToStep("step2");
			}
		},

		onOrderItemPress: function (oEvent) {
			var oView = this.getView();
			var oWizard = oView.byId("wizard");
			oView.byId("step2").setValidated(true);

			oEvent.getSource().setSelected(true);

			if (oWizard.getCurrentStep().indexOf("step2") >= 0) {
				oWizard.nextStep();
			} else {
				oWizard.goToStep("step2");
			}
		},

		onInputChange: function (oEvent) {
			var oView = this.getView();
			if (oView.byId("id_description").getValue().length > 0 && oView.byId("id_quantity").getValue().length > 0) {
				oView.byId("step3").setValidated(true);
			}
		},

		onReasonValueHelp: function () {
			this._oValueHelpDialog = sap.ui.xmlfragment("com.egger.complaints.ui.view.ValueHelpDialog", this);
			this.getView().addDependent(this._oValueHelpDialog);
			this._oValueHelpDialog.getTableAsync().then(function (oTable) {
				if (oTable.bindItems) {
					oTable.bindAggregation("items", "/Codes", function () {
						return new sap.m.ColumnListItem({
							cells: [new sap.m.Label({
								text: "{CODE}"
							}), new sap.m.Label({
								text: "{name}"
							})]
						});
					});
				}
				this._oValueHelpDialog.update();
			}.bind(this));
			this._oValueHelpDialog.open();
		},

		onValueHelpCancelPress: function () {
			this._oValueHelpDialog.close();
		},

		onValueHelpAfterClose: function () {
			this._oValueHelpDialog.destroy();
		},

		onWizardComplete: function () {
			var oView = this.getView();
			var oOrderObject = oView.byId("ordersTable").getSelectedItem().getBindingContext().getObject();
			var oOrderItemObject = oView.byId("itemsTable").getSelectedItem().getBindingContext().getObject();

			var quantity = oView.byId("id_quantity").getValue();

			if (parseFloat(quantity) > oOrderItemObject.quantity) {
				sap.m.MessageToast.show(this.getResourceBundle().getText("quantityTooBig"));
				return;
			}
			
			var sDescription = oView.byId("id_description").getValue();

			this.getModel().create("/Complaints", {
				"complaintNo": "DUMMY",
				"aufnr": oOrderObject.AUFNR,
				"matnr": oOrderItemObject.matnr,
				"kmatnr": oOrderItemObject.matnr + "_kunde",
				"makt": oOrderItemObject.makt,
				"delivery": oOrderObject.purchaseOrder,
				"kunnr": oOrderObject.name,
				"werks": "EGGER Wismar",
				"menge": quantity,
				"unit": "STK",
				"description": sDescription,
				"code": null,
				"status": "E0000"
			}, {
				success: function (oData) {
					sap.ui.getCore()._startMLProcessing({
						complaint: oData.ID,
						text: sDescription,
						sap: false
					}, this.getModel());
					this.getRouter().navTo("object", {
						objectId : oData.ID
					}, true);
				}.bind(this),
				error: function (oError) {
					sap.m.MessageToast.show(oError);
				}
			});
		},

		_onObjectMatched: function () {
			var oWizard = this.byId("wizard");
			var oFirstStep = oWizard.getSteps()[0];
			var oSecondStep = oWizard.getSteps()[1];
			var oThirdStep = oWizard.getSteps()[2];
			oWizard.discardProgress(oThirdStep);
			oWizard.discardProgress(oSecondStep);
			oWizard.discardProgress(oFirstStep);
			// scroll to top
			oWizard.goToStep(oFirstStep);
			// invalidate first step
			oFirstStep.setValidated(false);
			this.byId("id_description").setValue("");
			this.byId("id_quantity").setValue("");
		}

		/**
		 * Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
		 * (NOT before the first rendering! onInit() is used for that one!).
		 * @memberOf com.egger.complaints.ui.view.Create
		 */
		//	onBeforeRendering: function() {
		//
		//	},

		/**
		 * Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
		 * This hook is the same one that SAPUI5 controls get after being rendered.
		 * @memberOf com.egger.complaints.ui.view.Create
		 */
		//	onAfterRendering: function() {
		//
		//	},

		/**
		 * Called when the Controller is destroyed. Use this one to free resources and finalize activities.
		 * @memberOf com.egger.complaints.ui.view.Create
		 */
		//	onExit: function() {
		//
		//	}

	});

});