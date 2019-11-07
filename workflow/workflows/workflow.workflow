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
			"sequenceFlows": {
				"28ee5691-9d4b-41c7-86cd-4bd9287ba7e2": {
					"name": "SequenceFlow1"
				}
			},
			"diagrams": {
				"bbba17ce-51ae-4d96-82f3-7144b9680b3e": {}
			}
		},
		"bbba17ce-51ae-4d96-82f3-7144b9680b3e": {
			"classDefinition": "com.sap.bpm.wfs.ui.Diagram",
			"symbols": {
				"6334b2cb-7ab8-454b-94df-71a80428f4b3": {},
				"f9148db8-3eec-410e-a12f-6d0067a31cfc": {},
				"0b93b363-1787-439e-bacc-0269a7567212": {}
			}
		},
		"9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3": {
			"classDefinition": "com.sap.bpm.wfs.StartEvent",
			"id": "startevent1",
			"name": "StartEvent1"
		},
		"26e22595-2690-430f-9e98-b57d7d309740": {
			"classDefinition": "com.sap.bpm.wfs.LastIDs",
			"sequenceflow": 1,
			"startevent": 1,
			"endevent": 1
		},
		"47451aa6-d0e7-4320-b455-fbc8b9ac3ed6": {
			"classDefinition": "com.sap.bpm.wfs.EndEvent",
			"id": "endevent1",
			"name": "EndEvent1"
		},
		"28ee5691-9d4b-41c7-86cd-4bd9287ba7e2": {
			"classDefinition": "com.sap.bpm.wfs.SequenceFlow",
			"id": "sequenceflow1",
			"name": "SequenceFlow1",
			"sourceRef": "9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3",
			"targetRef": "47451aa6-d0e7-4320-b455-fbc8b9ac3ed6"
		},
		"6334b2cb-7ab8-454b-94df-71a80428f4b3": {
			"classDefinition": "com.sap.bpm.wfs.ui.StartEventSymbol",
			"x": 100,
			"y": 100,
			"object": "9f6d2e39-36a1-4f24-a2bd-88c7fcd0c6a3"
		},
		"f9148db8-3eec-410e-a12f-6d0067a31cfc": {
			"classDefinition": "com.sap.bpm.wfs.ui.EndEventSymbol",
			"x": 340,
			"y": 100,
			"object": "47451aa6-d0e7-4320-b455-fbc8b9ac3ed6"
		},
		"0b93b363-1787-439e-bacc-0269a7567212": {
			"classDefinition": "com.sap.bpm.wfs.ui.SequenceFlowSymbol",
			"sourceSymbol": "6334b2cb-7ab8-454b-94df-71a80428f4b3",
			"targetSymbol": "f9148db8-3eec-410e-a12f-6d0067a31cfc",
			"object": "28ee5691-9d4b-41c7-86cd-4bd9287ba7e2"
		}
	}
}