sap.ui.define([], function () {
	"use strict";
	var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({
		pattern: "dd.MM.YYYY"
	});

	return {
		/**
		 * Rounds the currency value to 2 digits
		 *
		 * @public
		 * @param {string} sValue value to be formatted
		 * @returns {string} formatted currency value with 2 digits
		 */
		currencyValue: function (sValue) {
			if (!sValue) {
				return "";
			}

			return parseFloat(sValue).toFixed(2);
		},

		dateFormatter: function (oDate) {
			return dateFormat.format(oDate);
		},

		statusFormatter: function (sStatus) {
			switch (sStatus) {
			case "E0000":
				return "None";
			case "E0001":
				return "Information";
			case "E0002":
				return "Warning";
			case "E0003":
				return "Success";
			default:
				return "None";
			}
		},
		
		iconStatusFormatter: function (sStatus) {
			switch (sStatus) {
			case "E0000":
				return "";
			case "E0001":
				return "sap-icon://paper-plane";
			case "E0002":
				return "sap-icon://message-error";
			case "E0003":
				return "sap-icon://accept";
			default:
				return "";
			}
		}
	};
});