//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var ID_PARAMETER_DEPARTMENT = "query.deptId";
var ID_FOR_HAND_INPUT_ONLY = 'inputByHand';
var ID_FOR_OPTIMIZE_ONLY = 'inputByOptimize';
var ID_VEHICLE_NUMBER = 'inputByVehicleNumber';
var ID_VALID_STATUS = 'inputByValidStatus';

var TITLE_FOR_QUERY_CONDITION = '查询条件';
var TITLE_FOR_QUERY = "查 询";

var LABEL_STATUS_ENABLE = '有效';
var LABEL_STATUS_DISABLE = '无效';

var URL_QUERY_LINE_ACTION = '../driver/queryLine.action';
var MESSAGE_PLEASE_SELECT_SOME_DEPARTMENT = "请先选择网点！";
var ID_PARAMETER_DATA_INPUT_TYPE = 'status';

var CONDITION_DEPARTMENT_ID = "departmentId";
var CONDITION_INPUT_TYPE = "inputType";
var CONDITION_VEHICLE_NUMBER = "vehicleNumber";
var CONDITION_VALID_STATUS = "validStatus";

var TITLE_FOR_UPDATE = "修 改";
var COL_BELONG_ZONE_CODE = "归属网点";
var COL_LINE_ID = '场地ID';
var ID = "ID";
var PROMPT = "提示";
var PLEASE_SELECT_A_RECORD = "请选择一条记录！";
var UPDATE_SAVE = "保存";
var UPDATE_SUCCESS = "修改成功！";
var UPDATE_FAILURE = "修改失败！";
var SELECT_SOME_DEPARTMENT = "选择网点";
var DEPARTMENT_ID = "网点ID";
var DEPARTMENT_AREA_CODE = "所属地区";
var DEPARTMENT_CODE = "网点代码";
var DEPARTMENT_NAME = "网点名称";
var DEPARTMENT_TYPE_CODE = "网点类型";
var TITLE_FOR_DELETE = "删 除";
var EFFECTIVE_CANNOT_BE_DELETED = "有效的数据不能删除！";
var DELETE_CONFIRMATION_PROMPTS = "是否确定删除该记录？";
var DELETE_SUCCESS = "删除成功！";
var DELETE_FAILURE = "删除失败！";
var DELETE_FAILURE_BY_SYSTEM = "删除失败，系统异常！";

var TEMPLATE_NAME = "线路导入模板.xls";
var MSG_ARE_SUBMITTING_DATA = '正在提交数据';
var IMPORT_SUBMIT_PATH = "importLine.action";
var PROMPT_IMPORT_FAILURE = "导入失败！";
var TITLE_IMPORT = '导入';
var PROMPT_IMPORT_OPTION_EXCEL = '导入时，请先选择Excel文件';
var LINE_ID = "LINE_ID";
var ALREADY_WITH_THE_CLASS = "<label style = 'color:red'>已配班</label>";
var NOT_WITH_THE_CLASS = "<label style = 'color:green'>未配班</label>";

var KEY_CONFIGURE_STATUS = 'configureStatus';
var LABEL_CONFIGURE_STATUS = '配班状态';

function getSelectedDepartmentNode() {
	return treePanel.getSelectionModel().getSelectedNode();
}

function hasNotSelectedDepartment(node) {
	return node == null || node.id == 0;
}

var treePanel = new Ext.tree.TreePanel({
	region: 'west',
	margins: '1 1 1 1',
	width: 245,
	title: '网点信息',
	collapsible: true,
	autoScroll: true,
	root: new Ext.tree.AsyncTreeNode({
		id: '0',
		text: '顺丰速运',
		loader: new Ext.tree.TreeLoader({
			dataUrl: "../common/getSfDeptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
		})
	}),
	listeners: {
		beforeclick: function(node, e) {
			if (hasNotSelectedDepartment(node)) {
				return;
			}

			Ext.getCmp(ID_PARAMETER_DEPARTMENT).setValue(node.text);
			queryLine(node);
		}
	}
});

var searchButton = new Ext.Button({
	text: TITLE_FOR_QUERY,
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		queryLine();
	}
});

var uploadFileName = new Ext.form.Hidden({
	name: "filePath"
});

var uploadFile = new Ext.form.TextField({
	width: 350,
	id: 'uploadFile',
	inputType: 'file',
	name: 'uploadFile',
	fieldLabel: '文件路径',
	labelStyle: 'width:auto'
});

var importTemplateHtml = ['<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">下载模板文件:</font>','<a href="#" onclick="downTemplate()">' + TEMPLATE_NAME + '</a>'];

var downTemplate = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=" + TEMPLATE_NAME + "&moduleName=driver&entityName=DriveLine&isTemplate=true";
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
};

var uploadForm = new Ext.form.FormPanel({
	height: 160,
	width: 600,
	frame: true,
	fileUpload: true,
	border: false,
	items: [new Ext.Panel({
		layout: 'form',
		border: false,
		items: [{
			xtype: 'hidden',
			id: 'uploadFormId',
			name: "uploadFormId"
		},uploadFileName,uploadFile]
	}),new Ext.form.Label({
		html: importTemplateHtml.join('')
	})]
});

function getDownloadPathWhenUploadFail(errorFileName) {
	return '../common/downloadReportFile.action?' + Ext.util.Format.htmlDecode(encodeURI(encodeURI(errorFileName)));
}

function downloadClick(value) {
	window.location = value.attributes.url.nodeValue;
}

function submit() {
	uploadForm.form.submit({
		waitMsg: MSG_ARE_SUBMITTING_DATA,
		waitTitle: PROMPT,
		method: "POST",
		timeout: 500000,
		url: IMPORT_SUBMIT_PATH,
		success: function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);
			var fileName = result.resultMap["fileName"];
			var msg = result.resultMap["msg"];

			if (fileName != null) {
				var downloadUrl = getDownloadPathWhenUploadFail(fileName);
				downloadUrl = "<a href = '#' url='" + downloadUrl + "' onclick = 'downloadClick(this)'>错误数据下载</a>";

				Ext.MessageBox.alert(PROMPT, msg + downloadUrl, function() {
					uploadWindow.hide();
				});
				return;
			}
			Ext.MessageBox.alert(PROMPT, msg, function() {
				uploadWindow.hide();
			});
		},
		failure: function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);

			Ext.Msg.alert(PROMPT, PROMPT_IMPORT_FAILURE + result.error);
		}
	});
}

var uploadWindow = new Ext.Window({
	title: TITLE_IMPORT,
	height: 180,
	width: 500,
	layout: 'column',
	closeAction: 'hide',
	plain: true,
	modal: true,
	items: [uploadForm],
	tbar: [{
		text: TITLE_IMPORT,
		pressed: true,
		height: 18,
		minWidth: 60,
		handler: function() {
			uploadFileName.setValue(uploadFile.getValue());
			if (uploadFileName.getValue().indexOf(".xls") < 0) {
				Ext.MessageBox.alert(PROMPT, PROMPT_IMPORT_OPTION_EXCEL);
				return;
			}
			submit();
		}
	}]
});

var importButton = new Ext.Button({
	text: TITLE_IMPORT,
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		uploadWindow.show();
	}
});

var updateButton = new Ext.Button({
	text: TITLE_FOR_UPDATE,
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length != 1) {
			Ext.Msg.alert(PROMPT, PLEASE_SELECT_A_RECORD);
			return;
		}
		if (records[0].data.LINE_ID) {
			Ext.Msg.alert(PROMPT, "该线路已配班无法修改！");
			return;
		}
		updateLine(records[0]);
	}
});

function getLineIds(records) {
	var ids = "";
	for ( var i = 0; i < records.length; i++) {
		var record = records[i];
		ids = ids + record.data.ID + ",";
	}
	return ids;
}

var deleteButton = new Ext.Button({
	text: TITLE_FOR_DELETE,
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length == 0) {
			Ext.Msg.alert(PROMPT, PLEASE_SELECT_A_RECORD);
			return;
		}

		for ( var i = 0; i < records.length; i++) {
			var record = records[i];
			if (record.data.VALID_STATUS == 1) {
				Ext.Msg.alert(PROMPT, EFFECTIVE_CANNOT_BE_DELETED);
				return;
			}
		}

		Ext.Msg.confirm(PROMPT, DELETE_CONFIRMATION_PROMPTS, function(btn) {
			if (btn == "yes") {
				Ext.Ajax.request({
					url: '../driver/deleteLine.action',
					method: 'POST',
					params: {
						ID: getLineIds(records)
					},
					success: function(res, config) {
						var obj = Ext.decode(res.responseText);
						if (obj.success) {
							Ext.Msg.alert(PROMPT, DELETE_SUCCESS);
						} else {
							Ext.Msg.alert(PROMPT, DELETE_FAILURE);
						}
						queryLine();
					},
					failure: function(res, config) {
						Ext.Msg.alert(PROMPT, DELETE_FAILURE_BY_SYSTEM);
					}
				});
			}
		}, this);
	}
});

var updateValidStatusButton = new Ext.Button({
	text: "批量修改有效性",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length == 0) {
			Ext.Msg.alert(PROMPT, PLEASE_SELECT_A_RECORD);
			return;
		}
		for ( var i = 0; i < records.length; i++) {
			if (records[i].data.LINE_ID) {
				Ext.Msg.alert(PROMPT, "该线路已配班无法修改！");
				return;
			}
		}
		updateValidStatus(getLineIds(records));
	}
});

var tbar = [];

addBar('<app:isPermission code = "/driver/queryLine.action">a</app:isPermission>', searchButton);
addBar('<app:isPermission code = "/driver/addDriver.action">a</app:isPermission>', addLineButton);
addBar('<app:isPermission code = "/driver/updateLine.action">a</app:isPermission>', updateButton);
addBar('<app:isPermission code = "/driver/exportLine.action">a</app:isPermission>', exportButton);
addBar('<app:isPermission code = "/driver/deleteLine.action">a</app:isPermission>', deleteButton);
addBar('<app:isPermission code = "/driver/importLine.action">a</app:isPermission>', importButton);
addBar('<app:isPermission code = "/driver/updateValidStatus.action">a</app:isPermission>', updateValidStatusButton);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

var topPanel = new Ext.Panel({
	frame: true,
	layout: 'column',
	height: 120,
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		columnWidth: 1,
		title: TITLE_FOR_QUERY_CONDITION,
		style: 'margin-top:5px;',
		frame: true,
		items: [{
			layout: 'column',
			items: [{
				columnWidth: .3,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					id: ID_PARAMETER_DEPARTMENT,
					readOnly: true,
					fieldLabel: '<font color=red>网点代码*</font>',
					anchor: '90%'
				},{
					xtype: 'checkboxgroup',
					id: ID_VALID_STATUS,
					fieldLabel: '有效性',
					anchor: '90%',
					items: [{
						boxLabel: LABEL_STATUS_ENABLE,
						name: CONDITION_VALID_STATUS,
						inputValue: "1"
					},{
						boxLabel: LABEL_STATUS_DISABLE,
						name: CONDITION_VALID_STATUS,
						inputValue: "0"
					}]
				}]
			},{
				columnWidth: .3,
				layout: 'form',
				items: [{
					xtype: 'checkboxgroup',
					id: ID_PARAMETER_DATA_INPUT_TYPE,
					fieldLabel: COL_INPUT_TYPE,
					anchor: '90%',
					items: [{
						boxLabel: LABEL_INPUT_BY_HAND,
						id: ID_FOR_HAND_INPUT_ONLY,
						checked: true
					},{
						boxLabel: LABEL_INPUT_BY_OPTIMIZE,
						id: ID_FOR_OPTIMIZE_ONLY,
						checked: true
					}]
				},{
					xtype: 'combo',
					id: KEY_CONFIGURE_STATUS,
					fieldLabel: LABEL_CONFIGURE_STATUS,
					triggerAction: 'all',
					forceSelection: true,
					store: [['1', '已配班'],['0', '未配班']],
					anchor: '90%'
				}]
			},{
				columnWidth: .3,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					fieldLabel: COL_VEHICLE_NUMBER,
					id: ID_VEHICLE_NUMBER,
					anchor: '90%'
				}]
			}]
		}]

	}]
});

function formattedTime(value) {
	var left = value.substr(0, 2);
	var right = value.substr(2);

	return left + ":" + right;
}

function formatSourceName(value, record) {
	var name = record.get(SOURCE_NAME);
	return formatName(value, name);
}

function formatDestinationName(value, record) {
	var name = record.get(DESTINATION_NAME);
	return formatName(value, name);
}

function formatBelongName(value, record) {
	var name = record.get(BELONG_NAME);
	return formatName(value, name);
}

function formatName(code, name) {
	return code + "/" + name;
}

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});
var cm = new Ext.grid.ColumnModel({
	defaults: {
		width: 70
	},
	columns: [new Ext.grid.RowNumberer(),sm,{
		header: COL_AREA_CODE,
		sortable: true,
		width: 140,
		dataIndex: BELONG_DEPARTMENT_CODE,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return formatBelongName(value, record);
		}
	},{
		header: "配班状态",
		sortable: true,
		dataIndex: LINE_ID,
		renderer: function(value) {
			if (value) {
				return ALREADY_WITH_THE_CLASS;
			}
			return NOT_WITH_THE_CLASS;
		}
	},{
		header: COL_START_TIME,
		sortable: true,
		dataIndex: START_TIME,
		renderer: function(value) {
			return formattedTime(value);
		}
	},{
		header: COL_END_TIME,
		sortable: true,
		dataIndex: END_TIME,
		renderer: function(value) {
			return formattedTime(value);
		}
	},{
		header: COL_SOURCE_CODE,
		sortable: true,
		dataIndex: SOURCE_CODE,
		width: 140,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return formatSourceName(value, record);
		}
	},{
		header: COL_DESTINATION_CODE,
		sortable: true,
		width: 140,
		dataIndex: DESTINATION_CODE,
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return formatDestinationName(value, record);
		}
	},{
		header: COL_VEHICLE_TYPE,
		sortable: true,
		dataIndex: VEHICLE_TYPE
	},{
		header: COL_VEHICLE_NUMBER,
		sortable: true,
		dataIndex: VEHICLE_NUMBER
	},{
		header: COL_VALID_STATUS,
		sortable: true,
		dataIndex: VALID_STATUS,
		renderer: function(value) {
			return getStatusTypes(value);
		}
	},{
		header: COL_INPUT_TYPE,
		sortable: true,
		dataIndex: INPUT_TYPE,
		renderer: function(value) {
			return inputTypes[value];
		}
	},{
		header: COL_CREATOR,
		sortable: true,
		dataIndex: CREATOR
	},{
		header: COL_CREATED_TIME,
		sortable: true,
		width: 130,
		dataIndex: CREATED_TIME
	},{
		header: COL_MODIFIER,
		sortable: true,
		dataIndex: MODIFIER
	},{
		header: COL_MODIFIED_TIME,
		sortable: true,
		width: 150,
		dataIndex: MODIFIED_TIME
	},{
		header: COL_LINE_ID,
		hidden: true,
		sortable: true,
		dataIndex: ID
	}]
});

function getStatusTypes(value) {
	if (1 == value) {
		return LABEL_STATUS_ENABLE;
	} else {
		return LABEL_STATUS_DISABLE;
	}
}

var record = Ext.data.Record.create([{
	name: BELONG_DEPARTMENT_CODE,
	mapping: BELONG_DEPARTMENT_CODE,
	type: 'string'
},{
	name: BELONG_NAME,
	mapping: BELONG_NAME,
	type: 'string'
},{
	name: START_TIME,
	mapping: START_TIME,
	type: 'string'
},{
	name: END_TIME,
	mapping: END_TIME,
	type: 'string'
},{
	name: SOURCE_CODE,
	mapping: SOURCE_CODE,
	type: 'string'
},{
	name: SOURCE_NAME,
	mapping: SOURCE_NAME,
	type: 'string'
},{
	name: DESTINATION_NAME,
	mapping: DESTINATION_NAME,
	type: 'string'
},{
	name: DESTINATION_CODE,
	mapping: DESTINATION_CODE,
	type: 'string'
},{
	name: VEHICLE_TYPE,
	mapping: VEHICLE_TYPE,
	type: 'string'
},{
	name: VEHICLE_NUMBER,
	mapping: VEHICLE_NUMBER,
	type: 'string'
},{
	name: VALID_STATUS,
	mapping: VALID_STATUS,
	type: 'int'
},{
	name: INPUT_TYPE,
	mapping: INPUT_TYPE,
	type: 'int'
},{
	name: CREATOR,
	mapping: CREATOR,
	type: 'string'
},{
	name: CREATED_TIME,
	mapping: CREATED_TIME,
	type: 'string'
},{
	name: MODIFIER,
	mapping: MODIFIER,
	type: 'string'
},{
	name: MODIFIED_TIME,
	mapping: MODIFIED_TIME,
	type: 'string'
},{
	name: ID,
	mapping: ID,
	type: 'string'
},{
	name: LINE_ID,
	mapping: LINE_ID,
	type: 'string'
}]);

var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: URL_QUERY_LINE_ACTION
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
	}, record)
});

var pageBar = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

var grid = new Ext.grid.GridPanel({
	cm: cm,
	sm: sm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pageBar
});

// 中部的Panel
var centerPanel = new Ext.Panel({
	region: 'center',
	margins: '1 1 1 0',
	items: [topPanel,grid],
	listeners: {
		resize: function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			grid.setWidth(adjWidth - 5);
			grid.setHeight(adjHeight - 165);
		}
	}
});

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout: "border",
		items: [treePanel,centerPanel]
	});
});

// 查询方法
function showMessage(message) {
	Ext.Msg.alert(PROMPT, message);
}

function getInputType() {
	var QUERY_ALL = 0;
	var QUERY_HAND_INPUT_ONLY = 1;
	var QUERY_OPTIMIZE_ONLY = 2;

	var selectedCheckBox = Ext.getCmp(ID_PARAMETER_DATA_INPUT_TYPE).getValue();
	if (selectedCheckBox.length == 2 || selectedCheckBox.length == 0) {
		return QUERY_ALL;
	}

	if (selectedCheckBox[0].id == ID_FOR_HAND_INPUT_ONLY) {
		return QUERY_HAND_INPUT_ONLY;
	}

	return QUERY_OPTIMIZE_ONLY;
}

function getSelectedValidStatus() {
	var validStatus = [];
	Ext.each(Ext.getCmp(ID_VALID_STATUS).getValue(), function(v, i) {
		validStatus.push(v.inputValue);
	});
	return validStatus.join(',');
}

var queryLine = function(node) {
	var node = node || getSelectedDepartmentNode();

	if (hasNotSelectedDepartment(node)) {
		showMessage(MESSAGE_PLEASE_SELECT_SOME_DEPARTMENT);
		return;
	}

	var currentSelected = node || treePanel.getSelectionModel().getSelectedNode();
	var departmentId = currentSelected.id;
	var inputType = getInputType();
	var vehicleNumber = Ext.getCmp(ID_VEHICLE_NUMBER).getValue();
	var validStatus = getSelectedValidStatus();

	store.setBaseParam(CONDITION_VEHICLE_NUMBER, vehicleNumber);
	store.setBaseParam(CONDITION_DEPARTMENT_ID, departmentId);
	store.setBaseParam(CONDITION_INPUT_TYPE, inputType);
	store.setBaseParam(CONDITION_VALID_STATUS, validStatus);
	store.setBaseParam(KEY_CONFIGURE_STATUS, Ext.getCmp(KEY_CONFIGURE_STATUS).getValue());

	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};

var updateLine = function(record) {
	var form = new Ext.FormPanel({
		layout: "column",
		xtype: "form",
		id: "updateForm",
		frame: true,
		defaults: {
			xtype: 'panel',
			columnWidth: .4,
			layout: "form",
			labelAlign: "right",
			labelWidth: 120
		},
		items: [{
			items: [{
				xtype: "hidden",
				name: "updateId",
				value: record.data.ID
			},{
				xtype: "textfield",
				disabled: true,
				width: 130,
				fieldLabel: COL_INPUT_TYPE,
				name: "updateInputType",
				value: inputTypes[record.data.INPUT_TYPE]
			}]
		},{
			items: [{
				xtype: "combo",
				hiddenName: "updateValid",
				width: 130,
				fieldLabel: addMustMark(COL_VALID_STATUS),
				mode: 'local',
				displayField: "text",
				valueField: "value",
				triggerAction: 'all',
				editable: false,
				allowBlank: false,
				value: record.data.VALID_STATUS,
				store: new Ext.data.SimpleStore({
					fields: ["text","value"],
					data: [[LABEL_STATUS_ENABLE,1],[LABEL_STATUS_DISABLE,0]]
				})
			}]
		},{
			items: [{
				xtype: "textfield",
				fieldLabel: addMustMark(COL_START_TIME),
				name: "updateStartTime",
				allowBlank: false,
				readOnly: true,
				editable: false,
				disabled: true,
				width: 130,
				minLength: 4,
				maxLength: 4,
				value: record.data.START_TIME
			}]
		},{
			items: [{
				xtype: "textfield",
				fieldLabel: addMustMark(COL_END_TIME),
				name: "updateEndTime",
				allowBlank: false,
				readOnly: true,
				editable: false,
				disabled: true,
				width: 130,
				minLength: 4,
				maxLength: 4,
				value: record.data.END_TIME
			}]
		},{
			items: [{
				xtype: "trigger",
				triggerClass: 'x-form-search-trigger',
				fieldLabel: addMustMark(COL_SOURCE_CODE),
				name: "updateSourceCode",
				id: "updateSourceCode",
				allowBlank: false,
				width: 130,
				maxLength: 30,
				value: record.data.SOURCE_CODE,
				valid: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				},
				onTriggerClick: function() {
					if (this.readOnly) {
						return false;
					}
					department_wind.show();
					department_grid.getStore().load();
					Ext.getCmp(DEPARTMENT_INPUT_ID).setValue("updateSourceCode");
				},
				validator: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				}
			}]
		},{
			items: [{
				xtype: "trigger",
				triggerClass: 'x-form-search-trigger',
				fieldLabel: addMustMark(COL_DESTINATION_CODE),
				name: "updateDestinationCode",
				id: "updateDestinationCode",
				allowBlank: false,
				width: 130,
				maxLength: 30,
				value: record.data.DESTINATION_CODE,
				onTriggerClick: function() {
					if (this.readOnly) {
						return false;
					}
					department_wind.show();
					department_grid.getStore().load();
					Ext.getCmp(DEPARTMENT_INPUT_ID).setValue("updateDestinationCode");
				},
				validator: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				}
			}]
		},{
			items: [{
				xtype: "trigger",
				triggerClass: 'x-form-search-trigger',
				fieldLabel: addMustMark(COL_BELONG_ZONE_CODE),
				name: "updateBelongZoneCode",
				id: "updateBelongZoneCode",
				allowBlank: false,
				width: 130,
				maxLength: 30,
				value: record.data.BELONG_ZONE_CODE,
				onTriggerClick: function() {
					department_wind.show();
					department_grid.getStore().load();
					Ext.getCmp(DEPARTMENT_INPUT_ID).setValue("updateBelongZoneCode");
				},
				validator: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				}
			},{
				xtype: 'textfield',
				name: 'vehicleNumber',
				layout: 'form',
				width: 130,
				value: record.data.VEHICLE_NUMBER,
				allowBlank: false,
				fieldLabel: addMustMark(COL_VEHICLE_NUMBER)
			}]
		}],
		fbar: [{
			text: UPDATE_SAVE,
			handler: function() {
				var form = this.ownerCt.ownerCt;
				if (form.getForm().isValid()) {
					form.getForm().submit({
						url: '../driver/updateLineById.action',
						method: 'POST',
						success: function(form, action) {
							if (action.result.success) {
								Ext.Msg.alert(PROMPT, UPDATE_SUCCESS);
								updateWin.close();
								queryLine();
							} else {
								Ext.Msg.alert(PROMPT, UPDATE_FAILURE + action.result.error);
							}
						},
						failure: function(form, action) {
							Ext.Msg.alert(PROMPT, UPDATE_FAILURE + action.result.error);
						},
						waitTitle: "请稍后",
						waitMsg: "正在执行操作...",
						timeout: 300
					// 5分钟
					});
				}
			}
		}]
	});

	var updateWin = new Ext.Window({
		width: 780,
		height: 230,
		closable: true,
		modal: true,
		border: false,
		bodyBorder: false,
		resizable: false,
		layout: 'fit',
		title: TITLE_FOR_UPDATE,
		items: [form]
	});

	updateWin.show();
};

var updateValidStatus = function(lineids) {
	var updateValidStatusWin = new Ext.Window({
		width: 300,
		height: 200,
		modal: true,
		border: false,
		bodyBorder: false,
		closable: false,
		resizable: false,
		layout: 'fit',
		title: '批量修改有效性',
		items: [{
			xtype: 'form',
			frame: true,
			items: [{
				xtype: "combo",
				hiddenName: "valid_status",
				width: 120,
				fieldLabel: COL_VALID_STATUS,
				mode: 'local',
				displayField: "text",
				valueField: "value",
				triggerAction: 'all',
				editable: false,
				allowBlank: false,
				forceSelection: true,
				value: null,
				store: new Ext.data.SimpleStore({
					fields: ["text","value"],
					data: [["有效",1],["无效",0]]
				})
			}],
			fbar: [{
				text: '保存',
				handler: function() {
					var form = this.ownerCt.ownerCt;
					if (form.getForm().isValid()) {
						form.getForm().submit({
							url: '../driver/updateValidStatus.action',
							params: {
								ID: lineids
							},
							success: function(form, action) {
								if (action.result.success) {
									Ext.Msg.alert(PROMPT, UPDATE_SUCCESS);
									updateValidStatusWin.close();
									queryLine();
									return;
								}
								Ext.Msg.alert(PROMPT, UPDATE_FAILURE);
							},
							failure: function(form, action) {
								Ext.Msg.alert(PROMPT, UPDATE_FAILURE);
							},
							waitTitle: "请稍后",
							waitMsg: "正在执行操作..."
						});
					}
				}
			},{
				text: '取消',
				handler: function() {
					updateValidStatusWin.close();
				}
			}]
		}]
	});
	updateValidStatusWin.show();
};
