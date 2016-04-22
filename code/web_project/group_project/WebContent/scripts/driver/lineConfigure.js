//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var INDEX_CODE = 'CODE';
var INDEX_VALID_STATUS = 'VALID_STATUS';
var INDEX_MONTH = 'MONTH';
var INDEX_START_TIME = 'START_TIME';
var INDEX_END_TIME = 'END_TIME';
var INDEX_SOURCE_CODE = 'SOURCE_CODE';
var INDEX_SOURCE_NAME = 'SOURCE_NAME';
var INDEX_DESTINATION_CODE = 'DESTINATION_CODE';
var INDEX_DESTINATION_NAME = 'DESTINATION_NAME';
var INDEX_CREATE_TM = 'CREATE_TM';
var INDEX_MODIFIED_TM = 'MODIFIED_TM';
var INDEX_CREATE_EMP_CODE = 'CREATE_EMP_CODE';
var INDEX_MODIFIED_EMP_CODE = 'MODIFIED_EMP_CODE';
var INDEX_DEPT_CODE = 'DEPT_CODE';
var INDEX_DEPT_ID = 'DEPT_ID';
var BELONG_DEPARTMENT_CODE = 'BELONG_ZONE_CODE';
var CREATOR = 'CREATOR';
var CREATED_TIME = 'CREATED_TIME';
var MODIFIER = 'MODIFIER';
var MODIFIED_TIME = 'MODIFY_TIME';
var LINE_ID = "LINE_ID";
var BELONG_NAME = "BELONG_NAME";
var MAPPING_ID = 'ID';

var COL_MODIFIER = '修改人';
var COL_CREATED_TIME = '创建时间';
var COL_CREATOR = '创建人';
var COL_VEHICLE_NUMBER = '车牌号';
var COL_INPUT_TYPE = "数据源";
var COL_VEHICLE_TYPE = '车型';
var COL_CODE = '班次代码';
var COL_MONTH = '月份';
var COL_VALID_STATUS = '有效性';
var COL_START_TIME = '出车时间';
var COL_END_TIME = '收车时间';
var COL_SOURCE_CODE = '始发网点';
var COL_DESTINATION_CODE = '目的网点';
var COL_CREATE_TM = '创建时间';
var COL_MODIFIED_TM = '修改时间';
var COL_CREATE_EMP_CODE = '创建人';
var COL_MODIFIED_EMP_CODE = '修改人';
var COL_AREA_CODE = '区域代码';

var TITLE_FOR_QUERY_CONDITION = '查询条件';
var TITLE_FOR_QUERY = "查 询";

var LABEL_STATUS_ENABLE = '有效';
var LABEL_STATUS_DISABLE = '无效';

var URL_QUERY_LINE_ACTION = '../driver/query_lineConfigure.action';
var MESSAGE_PLEASE_SELECT_SOME_DEPARTMENT = "请先选择网点！";

var SCHEDULING_STATE = '状态';
var NOT_SCHEDULING = '未排班';
var HAVE_SCHEDULING = '已排班';
var COLOR_DARK_GREEN = 'darkgreen';
var COLOR_RED = 'red';
var CONFIGURE_CODE = 'CONFIGURE_CODE';

var ID_PARAMETER_DEPARTMENT = "query.deptId";
var ID_CODE = "query.code";
var ID_VALID_STATUS = "query.status";
var ID_MONTH = "query.month";

var PROMPT = "提示";
var SELECT_SOME_DEPARTMENT = "选择网点";
var DEPARTMENT_AREA_CODE = "所属地区";
var DEPARTMENT_CODE = "网点代码";
var DEPARTMENT_NAME = "网点名称";
var DEPARTMENT_TYPE_CODE = "网点类型";
var MANEUVER = "机动";

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
			queryLineConfigure(node);
		}
	}
});

var searchButton = new Ext.Button({
	text: TITLE_FOR_QUERY,
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		queryLineConfigure();
	}
});

var inputTypes = [LABEL_INPUT_BY_HAND,LABEL_INPUT_BY_OPTIMIZE];

var lineCm = new Ext.grid.ColumnModel({
	defaults: {
		width: 70
	},
	columns: [new Ext.grid.RowNumberer(),{
		header: '数据源',
		sortable: true,
		dataIndex: 'INPUT_TYPE',
		renderer: function(value) {
			return inputTypes[value];
		}
	},{
		header: '路径优化线路编号',
		sortable: true,
		width: 110,
		dataIndex: 'OPTIMIZE_LINE_CODE'
	},{
		header: '车型',
		sortable: true,
		dataIndex: 'VEHICLE_TYPE'
	},{
		header: '出车时间',
		sortable: true,
		dataIndex: 'START_TIME',
		renderer: function(value) {
			return formattedTime(value);
		}
	},{
		header: '收车时间',
		sortable: true,
		dataIndex: 'END_TIME',
		renderer: function(value) {
			return formattedTime(value);
		}
	},{
		header: '始发网点',
		sortable: true,
		dataIndex: 'SOURCE_CODE',
		width: 140
	},{
		header: '目的网点',
		sortable: true,
		width: 140,
		dataIndex: 'DESTINATION_CODE'
	},{
		header: '归属网点',
		sortable: true,
		width: 140,
		dataIndex: 'BELONG_ZONE_CODE'
	},{
		header: '车牌号',
		sortable: true,
		dataIndex: 'VEHICLE_NUMBER'
	},{
		header: '有效状态',
		sortable: true,
		dataIndex: 'VALID_STATUS',
		renderer: function(value) {
			return getStatusTypes(value);
		}
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

function getStatusTypes(value) {
	if (1 == value) {
		return LABEL_STATUS_ENABLE;
	} else {
		return LABEL_STATUS_DISABLE;
	}
}

var lineRecord = Ext.data.Record.create([{
	name: 'INPUT_TYPE',
	mapping: 'INPUT_TYPE',
	type: 'string'
},{
	name: 'OPTIMIZE_LINE_CODE',
	mapping: 'OPTIMIZE_LINE_CODE',
	type: 'string'
},{
	name: 'VEHICLE_TYPE',
	mapping: 'VEHICLE_TYPE',
	type: 'string'
},{
	name: 'START_TIME',
	mapping: 'START_TIME',
	type: 'string'
},{
	name: 'END_TIME',
	mapping: 'END_TIME',
	type: 'string'
},{
	name: 'SOURCE_CODE',
	mapping: 'SOURCE_CODE',
	type: 'string'
},{
	name: 'SOURCE_NAME',
	mapping: 'SOURCE_NAME',
	type: 'string'
},{
	name: 'DESTINATION_CODE',
	mapping: 'DESTINATION_CODE',
	type: 'string'
},{
	name: 'DESTINATION_NAME',
	mapping: 'DESTINATION_NAME',
	type: 'string'
},{
	name: 'BELONG_ZONE_CODE',
	mapping: 'BELONG_ZONE_CODE',
	type: 'string'
},{
	name: 'BELONG_NAME',
	mapping: 'BELONG_NAME',
	type: 'string'
},{
	name: 'VEHICLE_NUMBER',
	mapping: 'VEHICLE_NUMBER',
	type: 'string'
},{
	name: 'VALID_STATUS',
	mapping: 'VALID_STATUS',
	type: 'string'
}]);

var lineStore = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../driver/queryLineByLineConfigureId.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root'
	}, lineRecord)
});

var viewConfigurePanel = new Ext.Panel({
	title: '配班信息',
	layout: 'column',
	height: 100,
	frame: true,
	items: [{
		layout: 'form',
		columnWidth: .5,
		labelAlign: 'right',
		defaults: {
			xtype: 'label',
			labelStyle: 'margin-top: -4'
		},
		items: [{
			fieldLabel: '网点代码',
			id: 'view_Department_Code'
		},{
			fieldLabel: '始发网点',
			id: 'view_Source_Code'
		},{
			fieldLabel: '月份',
			id: 'view_Year_Month'
		}]
	},{
		layout: 'form',
		columnWidth: .5,
		labelAlign: 'right',
		defaults: {
			xtype: 'label',
			labelStyle: 'margin-top: -4'
		},
		items: [{
			fieldLabel: '班次代码',
			id: 'view_Classed_Code'
		},{
			fieldLabel: '目的网点',
			id: 'view_Destination_Code'
		},{
			fieldLabel: '有效状态',
			id: 'view_Valid_State'
		}]
	}]
});

var lineGridPanel = new Ext.grid.GridPanel({
	title: '已选的线路',
	cm: lineCm,
	store: lineStore,
	autoScroll: true,
	loadMask: true,
	height: 370,
	width: 600
});

var lineDetailWindow = new Ext.Window({
	title: '查看明细',
	width: 600,
	height: 500,
	closeAction: 'hide',
	closable: true,
	modal: true,
	border: false,
	bodyBorder: false,
	resizable: false,
	items: [viewConfigurePanel,lineGridPanel]
});

var queryLineButton = new Ext.Button({
	text: '查看明细',
	cls: 'x-btn-normal',
	minWidth: 60,
	pressed: true,
	handler: function() {
		var record = grid.getSelectionModel().getSelected();

		if (record == undefined) {
			Ext.Msg.alert('提示', '请选择一条数据!');
			return;
		}

		Ext.getCmp('view_Department_Code').setText(record.data[INDEX_DEPT_CODE]);
		Ext.getCmp('view_Source_Code').setText(formatDepartName(record.data[INDEX_SOURCE_CODE], record.data[INDEX_SOURCE_NAME]));
		Ext.getCmp('view_Classed_Code').setText(formatConfigureCode(record.data[INDEX_CODE], record.data[INDEX_DEPT_CODE], record.data[INDEX_SOURCE_CODE]));
		Ext.getCmp('view_Destination_Code').setText(formatDepartName(record.data[INDEX_DESTINATION_CODE], record.data[INDEX_DESTINATION_NAME]));
		Ext.getCmp('view_Year_Month').setText(record.data[INDEX_MONTH]);
		Ext.getCmp('view_Valid_State').setText(getStatusTypes(record.data[INDEX_VALID_STATUS]));

		lineDetailWindow.show();

		lineStore.setBaseParam('lineConfigureId', record.data[MAPPING_ID]);

		lineStore.load();
	}
});

var updateConfigureStateWindow = new Ext.Window({
	title: '修改有效性',
	width: 260,
	height: 160,
	closeAction: 'hide',
	layout: 'column',
	tools: [{
		id: 'close',
		handler: function() {
			Ext.getCmp('validState').reset();
			updateConfigureStateWindow.hide();
		}
	}],
	tbar: [{
		text: '保存',
		cls: 'x-btn-normal',
		minWidth: 60,
		pressed: true,
		handler: function() {
			var validState = Ext.getCmp('validState').getValue();

			if (validState == '') {
				Ext.Msg.alert('提示', '请选择有效类型！');
				return;
			}

			Ext.Ajax.request({
				url: 'batchUpdateValidState_lineConfigure.action',
				params: {
					validState: validState,
					updateIds: Ext.getCmp('updateIds').getValue()
				},
				success: function(response) {
					var record = Ext.decode(response.responseText);

					if (record.error != '' && record.error != undefined) {
						Ext.Msg.alert('提示', '修改失败，错误：' + record.error);
						return;
					}
					Ext.Msg.alert('提示', '修改成功！');
					updateConfigureStateWindow.hide();

					store.load({
						params: {
							start: 0,
							limit: 20
						}
					});
				}
			});

		}
	}],
	items: [{
		layout: 'form',
		columnWidth: 1,
		frame: true,
		labelAlign: 'right',
		style: 'text-align: left',
		items: [{
			xtype: 'combo',
			fieldLabel: '有效性<lable style="color: red">*</lable>',
			id: 'validState',
			mode: 'local',
			width: 100,
			triggerAction: "all",
			border: false,
			forceSelection: true,
			store: [['1','有效'],['0','无效']]
		},{
			xtype: 'textfield',
			fieldLabel: 'ID',
			hidden: true,
			id: 'updateIds',
			width: 100
		}]
	}]
});

var updateSchedulingStateButton = new Ext.Button({
	text: '修改有效性',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		if (records.length == 0) {
			Ext.Msg.alert('提示', '请选择需要修改的数据，至少一条！');
			return;
		}

		var updateIds = [];

		Ext.each(records, function(record) {
			if (record.data[CONFIGURE_CODE] != '') {
				return;
			}
			updateIds.push(record.data[MAPPING_ID]);
		});

		if (updateIds.length != records.length) {
			Ext.Msg.alert('提示', '只能修改未排班数据');
			return;
		}
		Ext.getCmp('updateIds').setValue(updateIds.join(','));

		updateConfigureStateWindow.show();
	}
});

var deleteButton = new Ext.Button({
	text: '删除',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var records = grid.getSelectionModel().getSelections();
		
		if (records.length == 0) {
			Ext.Msg.alert('提示', '请选择需要删除的数据，至少一条！');
			return;
		}
		
		var deleteIds = [];
		Ext.each(records, function(record) {
			if (record.data.VALID_STATUS == 1) {
				return;
			}
			
			deleteIds.push(record.data.ID);
		});

		if (records.length != deleteIds.length) {
			Ext.Msg.alert('提示', '只能删除无效状态的配班，请确认数据!');
			return;
		}
		
		Ext.Ajax.request({
			url: 'batchDelete_lineConfigure.action',
			params: {
				deleteIds: deleteIds.join(',')
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				
				if (Ext.isEmpty(result.error)) {
					Ext.Msg.alert('提示' , '删除成功!');
					
					store.load({
						params: {
							start: 0,
							limit: 20
						}
					});
					return;
				}
				Ext.Msg.alert('提示', '删除失败,' + result.error);
			}
		});
	}
});

var tbar = [];

addBar('<app:isPermission code = "/driver/query_lineConfigure.action">a</app:isPermission>', searchButton);
addBar('<app:isPermission code = "/driver/addConfigureClassesInformation.action">a</app:isPermission>', addButton);
addBar('<app:isPermission code = "/driver/update_lineConfigure.action">a</app:isPermission>', moidfyButton);
addBar('<app:isPermission code = "/driver/delete_lineConfigure.action">a</app:isPermission>', deleteButton);
addBar('<app:isPermission code = "/driver/export_lineConfigure.action">a</app:isPermission>', btnExportLineConfigure);
addBar('<app:isPermission code = "/driver/importLineConfigure_lineConfigure.action">a</app:isPermission>', btnImportLineConfigure);
addBar('<app:isPermission code = "/driver/queryConfigureSchedulingAllLine_lineConfigure.action">a</app:isPermission>', queryLineButton);
addBar('<app:isPermission code = "/driver/updateSchedulingState_lineConfigure.action">a</app:isPermission>', updateSchedulingStateButton);
addBar('<app:isPermission code = "/driver/addMobileNetwork.action">a</app:isPermission>', addMobileNetwork);
addBar('<app:isPermission code = "/driver/updateMobileNetwork.action">a</app:isPermission>', updateMobileNetwork);
addBar('<app:isPermission code = "/driver/importDynamicLineConfigure_lineConfigure.action">a</app:isPermission>', btnImportDynamicLineConfigure);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

var topPanel = new Ext.Panel({
	frame: true,
	layout: 'column',
	height: 125,
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		columnWidth: 1,
		title: TITLE_FOR_QUERY_CONDITION,
		style: 'margin-top:5px;',
		frame: true,
		items: [{
			layout: 'column',
			style: 'margin-left:25px;margin-top:5px;',
			items: [{
				columnWidth: .4,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					id: ID_PARAMETER_DEPARTMENT,
					readOnly: true,
					fieldLabel: '<font color=red>网点代码*</font>',
					anchor: '90%'
				}]
			},{
				columnWidth: .4,
				layout: 'form',
				items: [{
					xtype: 'textfield',
					id: ID_CODE,
					fieldLabel: '班次代码',
					anchor: '90%'
				}]
			}]
		},{
			layout: 'column',
			style: 'margin-left:25px;margin-top:5px;',
			items: [{
				columnWidth: .4,
				layout: 'form',
				items: [{
					xtype: 'datefield',
					id: ID_MONTH,
					fieldLabel: '<font color=red>月份*</font>',
					anchor: '90%',
					format: 'Y-m',
					editable: false,
					value: new Date,
					plugins: 'monthPickerPlugin'
				}]
			},{
				columnWidth: .4,
				layout: 'form',
				items: [{
					xtype: 'combo',
					fieldLabel: COL_VALID_STATUS,
					id: ID_VALID_STATUS,
					emptyText: "请选择",
					editable: false,
					store: [[1,LABEL_STATUS_ENABLE],[0,LABEL_STATUS_DISABLE]],
					triggerAction: "all",
					mode: "local",
					allowBlank: false
				}]
			}]
		}]
	}]
});

function formatDepartName(code, name) {
	if (code == '' && name == '') {
		return '';
	}
	return code + "/" + name;
}

function formatConfigureCode(code, departmentName, sourceCode) {
	if(sourceCode)
		return departmentName + "-" + code;
	return MANEUVER + departmentName + "-" + code;
}

function formattedTime(value) {
	var left = value.substr(0, 2);
	var right = value.substr(2);
	return left + ":" + right;
}

function getStatusTypes(value) {
	if (1 == value) {
		return LABEL_STATUS_ENABLE;
	} else {
		return LABEL_STATUS_DISABLE;
	}
}

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});

function addFontColor(color, value) {
	return "<label style='color: " + color + "'>" + value + "</label>";
}

var cm = new Ext.grid.ColumnModel({
	columns: [new Ext.grid.RowNumberer(),sm,{
		header: COL_CODE,
		sortable: true,
		dataIndex: INDEX_CODE,
		width: 125,
		rendererCall: function(value, metaData, record) {
			return formatConfigureCode(value, record.get(INDEX_DEPT_CODE), record.get(INDEX_SOURCE_CODE));
		}
	},{
		header: COL_VALID_STATUS,
		sortable: true,
		dataIndex: INDEX_VALID_STATUS,
		rendererCall: function(value) {
			return getStatusTypes(value);
		}
	},{
		header: MAPPING_ID,
		sortable: true,
		dataIndex: MAPPING_ID,
		hidden: true
	},{
		header: COL_MONTH,
		sortable: true,
		dataIndex: INDEX_MONTH
	},{
		header: COL_START_TIME,
		sortable: true,
		dataIndex: INDEX_START_TIME,
		rendererCall: function(value) {
			return formattedTime(value);
		}
	},{
		header: COL_END_TIME,
		sortable: true,
		dataIndex: INDEX_END_TIME,
		rendererCall: function(value) {
			return formattedTime(value);
		}
	},{
		header: COL_SOURCE_CODE,
		sortable: true,
		dataIndex: INDEX_SOURCE_CODE,
		width: 200,
		rendererCall: function(value, metaData, record, rowIndex, colIndex, store) {
			return formatDepartName(value, record.get(INDEX_SOURCE_NAME));
		}
	},{
		header: COL_DESTINATION_CODE,
		sortable: true,
		dataIndex: INDEX_DESTINATION_CODE,
		width: 200,
		rendererCall: function(value, metaData, record, rowIndex, colIndex, store) {
			return formatDepartName(value, record.get(INDEX_DESTINATION_NAME));
		}
	},{
		header: SCHEDULING_STATE,
		sortable: true,
		dataIndex: CONFIGURE_CODE,
		rendererCall: function(value) {
			if (value == '')
				return addFontColor(COLOR_DARK_GREEN, NOT_SCHEDULING);
			return addFontColor(COLOR_RED, HAVE_SCHEDULING);
		}
	},{
		header: COL_CREATE_EMP_CODE,
		sortable: true,
		dataIndex: INDEX_CREATE_EMP_CODE
	},{
		header: COL_CREATE_TM,
		sortable: true,
		dataIndex: INDEX_CREATE_TM
	},{
		header: COL_MODIFIED_EMP_CODE,
		sortable: true,
		dataIndex: INDEX_MODIFIED_EMP_CODE
	},{
		header: COL_MODIFIED_TM,
		sortable: true,
		width: 170,
		dataIndex: INDEX_MODIFIED_TM
	}]
});

var record = Ext.data.Record.create([{
	name: INDEX_CODE,
	mapping: INDEX_CODE,
	type: 'string'
},{
	name: INDEX_VALID_STATUS,
	mapping: INDEX_VALID_STATUS,
	type: 'int'
},{
	name: MAPPING_ID,
	mapping: MAPPING_ID,
	type: 'string'
},{
	name: INDEX_MONTH,
	mapping: INDEX_MONTH,
	type: 'string'
},{
	name: INDEX_DEPT_CODE,
	mapping: INDEX_DEPT_CODE,
	type: 'string'
},{
	name: INDEX_START_TIME,
	mapping: INDEX_START_TIME,
	type: 'string'
},{
	name: INDEX_END_TIME,
	mapping: INDEX_END_TIME,
	type: 'string'
},{
	name: INDEX_SOURCE_CODE,
	mapping: INDEX_SOURCE_CODE,
	type: 'string'
},{
	name: INDEX_SOURCE_NAME,
	mapping: INDEX_SOURCE_NAME,
	type: 'string'
},{
	name: INDEX_DESTINATION_CODE,
	mapping: INDEX_DESTINATION_CODE,
	type: 'string'
},{
	name: INDEX_DESTINATION_NAME,
	mapping: INDEX_DESTINATION_NAME,
	type: 'string'
},{
	name: INDEX_CREATE_EMP_CODE,
	mapping: INDEX_CREATE_EMP_CODE,
	type: 'string'
},{
	name: INDEX_CREATE_TM,
	mapping: INDEX_CREATE_TM,
	type: 'string'
},{
	name: INDEX_MODIFIED_EMP_CODE,
	mapping: INDEX_MODIFIED_EMP_CODE,
	type: 'string'
},{
	name: INDEX_MODIFIED_TM,
	mapping: INDEX_MODIFIED_TM,
	type: 'string'
},{
	name: INDEX_DEPT_ID,
	mapping: INDEX_DEPT_ID,
	type: 'string'
},{
	name: CONFIGURE_CODE,
	mapping: CONFIGURE_CODE,
	type: 'string'
},{
	name: 'TYPE',
	mapping: 'TYPE',
	type: 'int'
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

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout: "border",
		items: [treePanel,centerPanel]
	});
});

function showMessage(message) {
	Ext.Msg.alert(PROMPT, message);
}

var queryLineConfigure = function(node) {
	var node = node || getSelectedDepartmentNode();

	if (hasNotSelectedDepartment(node)) {
		showMessage(MESSAGE_PLEASE_SELECT_SOME_DEPARTMENT);
		return;
	}
	var currentSelected = node || treePanel.getSelectionModel().getSelectedNode();
	var departmentId = currentSelected.id;
	var code = Ext.getCmp(ID_CODE).getValue();
	var validStatus = Ext.getCmp(ID_VALID_STATUS).getValue();
	var month = Ext.util.Format.date(Ext.getCmp(ID_MONTH).getValue(), "Y-m");

	store.setBaseParam(INDEX_DEPT_ID, departmentId);
	store.setBaseParam(INDEX_CODE, code);
	store.setBaseParam(INDEX_VALID_STATUS, validStatus);
	store.setBaseParam(INDEX_MONTH, month);

	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};