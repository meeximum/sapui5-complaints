sap.ui.define([
	"./BaseController",
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"../model/formatter",
	"sap/m/library"
], function (BaseController, JSONModel, Filter, FilterOperator, formatter, mobileLibrary) {
	"use strict";

	// shortcut for sap.m.URLHelper
	var URLHelper = mobileLibrary.URLHelper;

	return BaseController.extend("com.egger.complaints.ui.controller.Detail", {

		formatter: formatter,

		/* =========================================================== */
		/* lifecycle methods                                           */
		/* =========================================================== */

		onInit: function () {
			// Model used to manipulate control states. The chosen values make sure,
			// detail page is busy indication immediately so there is no break in
			// between the busy indication for loading the view's meta data
			var oViewModel = new JSONModel({
				busy: false,
				delay: 0,
				lineItemListTitle: this.getResourceBundle().getText("detailLineItemTableHeading")
			});

			this.getRouter().getRoute("object").attachPatternMatched(this._onObjectMatched, this);

			this.setModel(oViewModel, "detailView");

			this.getOwnerComponent().getModel().metadataLoaded().then(this._onMetadataLoaded.bind(this));
			
			if (sap.ui.getCore()._mlProcessingPromise) {
				var oReasonInput = this.byId("id_reasonInput");
				oReasonInput.setBusy(true);
				sap.ui.getCore()._mlProcessingPromise.then(function(oData) {
					oModel.refresh();
					oReasonInput.setBusy(false);
				}.bind(this)).catch(function(oError) {
					oReasonInput.setBusy(false);
				}.bind(this));
			}
		},

		/* =========================================================== */
		/* event handlers                                              */
		/* =========================================================== */

		/**
		 * Event handler when the share by E-Mail button has been clicked
		 * @public
		 */
		onSendEmailPress: function () {
			var oViewModel = this.getModel("detailView");

			URLHelper.triggerEmail(
				null,
				oViewModel.getProperty("/shareSendEmailSubject"),
				oViewModel.getProperty("/shareSendEmailMessage")
			);
		},

		/**
		 * Updates the item count within the line item table's header
		 * @param {object} oEvent an event containing the total number of items in the list
		 * @private
		 */
		onListUpdateFinished: function (oEvent) {
			var sTitle,
				iTotalItems = oEvent.getParameter("total"),
				oViewModel = this.getModel("detailView");

			// only update the counter if the length is final
			if (this.byId("lineItemsList").getBinding("items").isLengthFinal()) {
				if (iTotalItems) {
					sTitle = this.getResourceBundle().getText("detailLineItemTableHeadingCount", [iTotalItems]);
				} else {
					//Display 'Line Items' instead of 'Line items (0)'
					sTitle = this.getResourceBundle().getText("detailLineItemTableHeading");
				}
				oViewModel.setProperty("/lineItemListTitle", sTitle);
			}
		},

		onReasonValueHelpRequest: function () {
			this._oValueHelpDialog = sap.ui.xmlfragment("com.egger.complaints.ui.view.ValueHelpDialog", this);
			this.getView().addDependent(this._oValueHelpDialog);

			this._oValueHelpDialog.open();
		},

		onValueHelpOkPress: function (oEvent) {
			var oContextObject = oEvent.getParameters().selectedItem.getBindingContext().getObject();

			var sPath = this.getView().getBindingContext() + "/code";
			this.getModel().setProperty(sPath, oContextObject.CODE);

			//this.byId("id_reasonInput").setValue(oContextObject.CODE);
			this.byId("id_reasonText").setText(oContextObject.name);
		},

		onValueHelpCancelPress: function () {
			this._oValueHelpDialog.close();
		},

		onValueHelpAfterClose: function () {
			this._oValueHelpDialog.destroy();
		},

		onChangeDescription: function (oEvent) {
			var sPath = oEvent.getSource().getBindingContext().getPath() + "/description";
			var sValue = oEvent.getParameter("value");
			this.getModel().setProperty(sPath, sValue);
		},

		onSendToEgger: function () {
			var sStatus = this.getView().getBindingContext().getObject().status;
			if (sStatus !== "E0000" && sStatus !== "E0002") {
				sap.m.MessageToast.show(this.getResourceBundle().getText("sendOnlyStatusX"));
				return;
			}

			var sDescription = this.byId("id_reclaDescription").getValue();
			var sReason = this.byId("id_reasonInput").getValue();
			if (sDescription.length <= 0 || sReason.length <= 0) {
				sap.m.MessageToast.show(this.getResourceBundle().getText("fillDescAndReason"));
				return;
			}

			sap.m.MessageBox.confirm(this.getResourceBundle().getText("confirmSend"), {
				onClose: function (oAction) {
					if (oAction === sap.m.MessageBox.Action.OK) {
						// implemented via action in CDS
						this.getModel().callFunction("/setStatus", {
							method: "POST",
							urlParameters: {
								complaint: this.getView().getBindingContext().getObject().ID,
								status: "E0001"
							},
							refreshAfterChange: true,
							success: function () {
								this.getModel().refresh();
								sap.m.MessageToast.show(this.getResourceBundle().getText("statusChangeSuccess"));
							}.bind(this),
							error: function (oError) {
								console.log(oError);
								sap.m.MessageToast.show(this.getResourceBundle().getText("statusChangeError"));
							}.bind(this)
						});
					}
				}.bind(this)
			});
		},

		onSearchValueHelp: function (oEvent) {
			var oParameters = oEvent.getParameters();
			if (oParameters.clearButtonPressed) {
				oParameters.itemsBinding.filter([]);
			} else {
				oParameters.itemsBinding.filter(new Filter({
					filters: [
						new Filter({
							path: 'CODE',
							operator: FilterOperator.Contains,
							value1: oParameters.value
						}),
						new Filter({
							path: 'name',
							operator: FilterOperator.Contains,
							value1: oParameters.value
						})
					],
					and: false
				}));
			}
		},

		onSaveChanges: function () {
			var oModel = this.getModel();
			if (!oModel.hasPendingChanges()) {
				sap.m.MessageToastshow(this.getResourceBundle().getText("noPendingChanges"));
			} else {
				oModel.submitChanges();
			}
		},

		onResetChanges: function () {
			var oModel = this.getModel();
			if (oModel.hasPendingChanges()) {
				oModel.resetChanges();
			}
		},

		onPostComment: function (oEvent) {
			var sValue = oEvent.getParameter("value");
			var sComplaint = this.getView().getBindingContext().getObject().ID;

			var oEntity = {
				"complaint": sComplaint,
				"comment": sValue
			};

			this.getModel().create("/Comments", oEntity, {
				error: function () {
					sap.m.MessageToast.show(this.getResourceBundle().getText("commentError"));
				}.bind(this)
			});
		},
		
		

		/* =========================================================== */
		/* begin: internal methods                                     */
		/* =========================================================== */

		/**
		 * Binds the view to the object path and expands the aggregated line items.
		 * @function
		 * @param {sap.ui.base.Event} oEvent pattern match event in route 'object'
		 * @private
		 */
		_onObjectMatched: function (oEvent) {
			var sObjectId = oEvent.getParameter("arguments").objectId;
			this.getModel("appView").setProperty("/layout", "TwoColumnsMidExpanded");
			this.getModel().metadataLoaded().then(function () {
				var sObjectPath = this.getModel().createKey("Complaints", {
					ID: sObjectId
				});
				this._bindView("/" + sObjectPath);
			}.bind(this));
			if (sap.ui.getCore()._mlProcessingPromise) {
				var oReasonInput = this.byId("id_reasonInput");
				oReasonInput.setBusy(true);
				sap.ui.getCore()._mlProcessingPromise.then(function(oData) {
					oModel.refresh();
					oReasonInput.setBusy(false);
				}.bind(this)).catch(function(oError) {
					oReasonInput.setBusy(false);
				}.bind(this));
			}
		},

		/**
		 * Binds the view to the object path. Makes sure that detail view displays
		 * a busy indicator while data for the corresponding element binding is loaded.
		 * @function
		 * @param {string} sObjectPath path to the object to be bound to the view.
		 * @private
		 */
		_bindView: function (sObjectPath) {
			// Set busy indicator during view binding
			var oViewModel = this.getModel("detailView");

			// If the view was not bound yet its not busy, only if the binding requests data it is set to busy again
			oViewModel.setProperty("/busy", false);

			this.getView().bindElement({
				path: sObjectPath,
				//D068045
				parameters: {
					expand: "payment,to_code"
				},
				//D068045 - end
				events: {
					change: this._onBindingChange.bind(this),
					dataRequested: function () {
						oViewModel.setProperty("/busy", true);
					},
					dataReceived: function () {
						oViewModel.setProperty("/busy", false);
					}

				}
			});
		},

		_onBindingChange: function () {
			var oView = this.getView(),
				oElementBinding = oView.getElementBinding();

			// No data for the binding
			if (!oElementBinding.getBoundContext()) {
				this.getRouter().getTargets().display("detailObjectNotFound");
				// if object could not be found, the selection in the master list
				// does not make sense anymore.
				this.getOwnerComponent().oListSelector.clearMasterListSelection();
				return;
			}

			var sPath = oElementBinding.getPath(),
				oResourceBundle = this.getResourceBundle(),
				oObject = oView.getModel().getObject(sPath),
				sObjectId = oObject.ID,
				sObjectName = oObject.ID,
				oViewModel = this.getModel("detailView");

			this.getOwnerComponent().oListSelector.selectAListItem(sPath);

			oViewModel.setProperty("/shareSendEmailSubject",
				oResourceBundle.getText("shareSendEmailObjectSubject", [sObjectId]));
			oViewModel.setProperty("/shareSendEmailMessage",
				oResourceBundle.getText("shareSendEmailObjectMessage", [sObjectName, sObjectId, location.href]));
		},

		_onMetadataLoaded: function () {
			// Store original busy indicator delay for the detail view
			var iOriginalViewBusyDelay = this.getView().getBusyIndicatorDelay(),
				oViewModel = this.getModel("detailView"),
				oLineItemTable = this.byId("lineItemsList"),
				iOriginalLineItemTableBusyDelay = oLineItemTable.getBusyIndicatorDelay();

			// Make sure busy indicator is displayed immediately when
			// detail view is displayed for the first time
			oViewModel.setProperty("/delay", 0);
			oViewModel.setProperty("/lineItemTableDelay", 0);

			oLineItemTable.attachEventOnce("updateFinished", function () {
				// Restore original busy indicator delay for line item table
				oViewModel.setProperty("/lineItemTableDelay", iOriginalLineItemTableBusyDelay);
			});

			// Binding the view will set it to not busy - so the view is always busy if it is not bound
			oViewModel.setProperty("/busy", true);
			// Restore original busy indicator delay for the detail view
			oViewModel.setProperty("/delay", iOriginalViewBusyDelay);
		},

		/**
		 * Set the full screen mode to false and navigate to master page
		 */
		onCloseDetailPress: function () {
			this.getModel("appView").setProperty("/actionButtonsInfo/midColumn/fullScreen", false);
			// No item should be selected on master after detail page is closed
			this.getOwnerComponent().oListSelector.clearMasterListSelection();
			this.getRouter().navTo("master");
		},

		/**
		 * Toggle between full and non full screen mode.
		 */
		toggleFullScreen: function () {
			var bFullScreen = this.getModel("appView").getProperty("/actionButtonsInfo/midColumn/fullScreen");
			this.getModel("appView").setProperty("/actionButtonsInfo/midColumn/fullScreen", !bFullScreen);
			if (!bFullScreen) {
				// store current layout and go full screen
				this.getModel("appView").setProperty("/previousLayout", this.getModel("appView").getProperty("/layout"));
				this.getModel("appView").setProperty("/layout", "MidColumnFullScreen");
			} else {
				// reset to previous layout
				this.getModel("appView").setProperty("/layout", this.getModel("appView").getProperty("/previousLayout"));
			}
		}
	});

});