{
	"contents": {
		"06a5f89c-190f-4477-b1e1-9646165f6450": {
			"classDefinition": "com.sap.bpm.wfs.Model",
			"id": "workflow",
			"subject": "workflow",
			"name": "workflow",
			"lastIds": "26e22595-2690-430f-9e98-b57d7d309740",
			"events": {
				"9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3": {
					"name": "StartEvent1"
				},
				"47451aa6-d0e7-4320-b455-fbc8b9ac3ed6": {
					"name": "EndEvent1"
				}
			},
			"activities": {
				"96f795d8-e222-4cc9-a32b-9db4e36a6692": {
					"name": "mailID"
				}
			},
			"sequenceFlows": {
				"67004cc4-68fc-40a3-8cb6-8bbe9051326d": {
					"name": "SequenceFlow2"
				},
				"1672b50f-a3ad-41e9-ac02-cb8cc7d03b3a": {
					"name": "SequenceFlow3"
				}
			},
			"diagrams": {
				"bbba17ce-51ae-4d96-82f3-7144b9680b3e": {}
			}
		},
		"9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3": {
			"classDefinition": "com.sap.bpm.wfs.StartEvent",
			"id": "startevent1",
			"name": "StartEvent1"
		},
		"47451aa6-d0e7-4320-b455-fbc8b9ac3ed6": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent1",
			"name": "EndEvent1"
		},
		"bbba17ce-51ae-4d96-82f3-7144b9680b3e": {
			"classDefinition": "com.sap.bpm.wfs.ui.Diagram",
			"symbols": {
				"6334b2cb-7ab8-454b-94df-71a80428f4b3": {},
				"f9148db8-3eec-410e-a12f-6d0067a31cfc": {},
				"82a343ae-97b1-4545-a54f-2e2dda3a3ea7": {},
				"624a514f-fe79-41d9-b012-195f7803a7a6": {},
				"790e87f9-7d74-419e-a442-6d9058d5980c": {}
			}
		},
		"6334b2cb-7ab8-454b-94df-71a80428f4b3": {
			"classDefinition": "com.sap.bpm.wfs.ui.StartEventSymbol",
			"x": 99,
			"y": 100,
			"width": 32,
			"height": 32,
			"object": "9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3"
		},
		"f9148db8-3eec-410e-a12f-6d0067a31cfc": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 340,
			"y": 100,
			"width": 35,
			"height": 35,
			"object": "47451aa6-d0e7-4320-b455-fbc8b9ac3ed6"
		},
		"26e22595-2690-430f-9e98-b57d7d309740": {
			"classDefinition": "com.sap.bpm.wfs.LastIDs",
			"maildefinition": 1,
			"sequenceflow": 3,
			"startevent": 1,
			"endevent": 1,
			"mailtask": 1
		},
		"96f795d8-e222-4cc9-a32b-9db4e36a6692": {
			"classDefinition": "com.sap.bpm.wfs.MailTask",
			"id": "mailtask1",
			"name": "mailID",
			"documentation": "Send mail to ID",
			"mailDefinitionRef": "0f3b7d25-a81f-481e-b1b1-b6220b565383"
		},
		"82a343ae-97b1-4545-a54f-2e2dda3a3ea7": {
			"classDefinition": "com.sap.bpm.wfs.ui.MailTaskSymbol",
			"x": 176,
			"y": 209,
			"width": 100,
			"height": 60,
			"object": "96f795d8-e222-4cc9-a32b-9db4e36a6692"
		},
		"67004cc4-68fc-40a3-8cb6-8bbe9051326d": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow2",
			"name": "SequenceFlow2",
			"sourceRef": "9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3",
			"targetRef": "96f795d8-e222-4cc9-a32b-9db4e36a6692"
		},
		"624a514f-fe79-41d9-b012-195f7803a7a6": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "115,116 115,170.75 198,170.75 198,241",
			"sourceSymbol": "6334b2cb-7ab8-454b-94df-71a80428f4b3",
			"targetSymbol": "82a343ae-97b1-4545-a54f-2e2dda3a3ea7",
			"object": "67004cc4-68fc-40a3-8cb6-8bbe9051326d"
		},
		"1672b50f-a3ad-41e9-ac02-cb8cc7d03b3a": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow3",
			"name": "SequenceFlow3",
			"sourceRef": "96f795d8-e222-4cc9-a32b-9db4e36a6692",
			"targetRef": "47451aa6-d0e7-4320-b455-fbc8b9ac3ed6"
		},
		"790e87f9-7d74-419e-a442-6d9058d5980c": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"points": "226,239 308.25,239 308.25,117.5 357.5,117.5",
			"sourceSymbol": "82a343ae-97b1-4545-a54f-2e2dda3a3ea7",
			"targetSymbol": "f9148db8-3eec-410e-a12f-6d0067a31cfc",
			"object": "1672b50f-a3ad-41e9-ac02-cb8cc7d03b3a"
		},
		"0f3b7d25-a81f-481e-b1b1-b6220b565383": {
			"classDefinition": "com.sap.bpm.wfs.MailDefinition",
			"name": "maildefinition1",
			"to": "stefan.mueller@egger.com",
			"cc": "markus.reich@egger.com",
			"subject": "Neue Reklamation",
			"text": "Neue Reklamation eingelangt!",
			"id": "maildefinition1"
		}
	}
}