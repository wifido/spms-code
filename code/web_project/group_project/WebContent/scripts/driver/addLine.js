// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var BELONG_DEPARTMENT_CODE = 'BELONG_ZONE_CODE';
var START_TIME = 'START_TIME';
var END_TIME = 'END_TIME';
var SOURCE_CODE = 'SOURCE_CODE';
var DESTINATION_CODE = 'DESTINATION_CODE';
var INPUT_TYPE = "INPUT_TYPE";
var CREATOR = 'CREATOR';
var CREATED_TIME = 'CREATED_TIME';
var MODIFIER = 'MODIFIER';
var MODIFIED_TIME = 'MODIFY_TIME';
var SOURCE_NAME = "SOURCE_NAME";
var DESTINATION_NAME = "DESTINATION_NAME";
var BELONG_NAME = "BELONG_NAME";
var VALID_STATUS = "VALID_STATUS";
var VEHICLE_NUMBER = "VEHICLE_NUMBER";
var VEHICLE_TYPE = "VEHICLE_TYPE";
var OPTIMIZE_LINE_CODE = "OPTIMIZE_LINE_CODE";

var COL_AREA_CODE = '所属网点';
var COL_START_TIME = '出车时间';
var COL_END_TIME = '收车时间';
var COL_SOURCE_CODE = '始发网点';
var COL_DESTINATION_CODE = '目的网点';
var COL_INPUT_TYPE = "数据源";
var COL_CREATOR = '创建人';
var COL_CREATED_TIME = '创建时间';
var COL_MODIFIER = '修改人';
var COL_MODIFIED_TIME = '修改时间';
var COL_VALID_STATUS = '有效性';
var COL_VEHICLE_NUMBER = '车牌号';
var COL_VEHICLE_TYPE = '车型';
var COL_ATTRIBUTION_DEPARTMENT_CODE = '归属网点';

var LABEL_INPUT_BY_HAND = '手工录入';
var LABEL_INPUT_BY_OPTIMIZE = '路径优化';
var inputTypes = [LABEL_INPUT_BY_HAND,LABEL_INPUT_BY_OPTIMIZE];

var validTimeFormat = /^((0\d{1}|1\d{1}|2[0-3])([0-5]\d{1})|2400)$/;
var mustMark = '<font color=red>*</font>';
var KEY_MESSAGE = "msg";
var TEXT_SAVE = '保存';
var BE_SUBMIT_DATA = '正在提交数据';
var KEY_TIP = '提示';
var TIP_ADDED_SUCCESS = '新增成功！';
var TIP_ADDED_FAILURE = "新增失败！";
var ADDED_LINE = '新增线路';
var KEY_ADDED = '新增';

var ADDED_WINDOW_ID = 'add_window';
var ADDED_FORM_PANEL_ID = 'addFormPanel';

var ADDED_REQUEST_RUL = '../driver/addLine.action';
var VALID_DEPARTMENT_CODE_URL = '../driver/searchDepartmentValid.action';
var TIME_2400='2400';
var TIME_0000='0000';
var TIME_2359='2359';

function addMustMark(fieldLabel) {
	return fieldLabel + mustMark;
}

function validDepartmentCode(value) {
	var obj;
	if (window.ActiveXObject) {
		obj = new ActiveXObject('Microsoft.XMLHTTP');
	} else if (window.XMLHttpRequest) {
		obj = new XMLHttpRequest();
	}
	var url = VALID_DEPARTMENT_CODE_URL + '?departmentCode=' + value;

	obj.open('post', url, false);
	obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	obj.send(null);
	return obj.responseText;
}

function isAreaCode(departmentCode) {
	var flag = false;
	Ext.Ajax.request({
		url: "checkDepartment_scheduling.action",
		async: false,
		params: {
			departmentCode: departmentCode
		},
		success: function(response) {
			var result = Ext.util.JSON.decode(response.responseText);
			if (result.success) {
				flag = true;
			}
		},
		failure: function(response) {
		}
	});
	return flag;
}

var addFormPanel = new Ext.form.FormPanel({
	id: ADDED_FORM_PANEL_ID,
	width: 500,
	height: 200,
	frame: true,
	labelWidth: 60,
	items: [{
		layout: 'column',
		items: [{
			layout: 'form',
			columnWidth: .5,
			labelWidth: 60,
			labelAlign: 'right',
			items: [{
				xtype: 'textfield',
				name: INPUT_TYPE,
				layout: 'form',
				fieldLabel: COL_INPUT_TYPE,
				value: LABEL_INPUT_BY_HAND,
				readOnly: true
			},{
				xtype: 'textfield',
				name: END_TIME,
				layout: 'form',
				allowBlank: false,
				maxLength: 4,
				regex: validTimeFormat,
				fieldLabel: addMustMark(COL_END_TIME),
				validator: function(value) {
					if (!validTimeFormat.test(value)) {
						return '输入值非法';
					}
					return true;
				}
			},{
				xtype: 'trigger',
				triggerClass: 'x-form-search-trigger',
				name: DESTINATION_CODE,
				id: DESTINATION_CODE,
				layout: 'form',
				allowBlank: false,
				minLength: 1,
				maxLength: 11,
				width: 147,
				fieldLabel: addMustMark(COL_DESTINATION_CODE),
				onTriggerClick: function() {
					department_wind.show();
					department_grid.getStore().load();
					Ext.getCmp(DEPARTMENT_INPUT_ID).setValue(DESTINATION_CODE);
				},
				validator: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				}
			},{
				xtype: 'textfield',
				name: VEHICLE_NUMBER,
				layout: 'form',
				allowBlank: false,
				fieldLabel: addMustMark(COL_VEHICLE_NUMBER)
			}]
		},{
			layout: 'form',
			columnWidth: .5,
			labelWidth: 60,
			labelAlign: 'right',
			items: [{
				xtype: 'textfield',
				name: START_TIME,
				layout: 'form',
				maxLength: 4,
				allowBlank: false,
				regex: validTimeFormat,
				fieldLabel: addMustMark(COL_START_TIME)
			},{
				xtype: 'trigger',
				triggerClass: 'x-form-search-trigger',
				name: SOURCE_CODE,
				id: SOURCE_CODE,
				allowBlank: false,
				minLength: 1,
				maxLength: 11,
				width: 147,
				fieldLabel: addMustMark(COL_SOURCE_CODE),
				onTriggerClick: function() {
					department_wind.show();
					department_grid.getStore().load();
					Ext.getCmp(DEPARTMENT_INPUT_ID).setValue(SOURCE_CODE);
				},
				validator: function(value) {
					var respText = Ext.decode(validDepartmentCode(value));
					return respText[KEY_MESSAGE];
				}
			},{
				xtype: 'textfield',
				name: BELONG_DEPARTMENT_CODE,
				id: BELONG_DEPARTMENT_CODE,
				layout: 'form',
				allowBlank: false,
				readOnly: true,
				fieldLabel: addMustMark(COL_ATTRIBUTION_DEPARTMENT_CODE)
			}]
		}]
	}]
});


var saveButton = new Ext.Button({
	text: TEXT_SAVE,
	minWidth: 60,
	frame: true,
	cls: 'x-btn-normal',
	pressed: true,
	align: 'right',
	handler: function() {
		if (!addFormPanel.getForm().isValid()) {
			return;
		}
		var startTimeField = addFormPanel.getForm().findField(START_TIME);
		var endTimeField = addFormPanel.getForm().findField(END_TIME);
		if(!validateTime(startTimeField, endTimeField)){
			return;
		}
		if (startTimeField.getValue() == endTimeField.getValue()) {
			Ext.Msg.alert('提示', '出车时间不能等于收车时间');
			return;
		}
		addFormPanel.getForm().submit({
			waitMsg: BE_SUBMIT_DATA,
			waitTitle: KEY_TIP,
			method: "POST",
			timeout: 30000,
			url: ADDED_REQUEST_RUL,
			success: function() {
				Ext.Msg.alert(KEY_TIP, TIP_ADDED_SUCCESS);
				addFormPanel.form.reset();
				add_window.hide();
			},
			failure: function(form, action) {
				if (!Ext.isEmpty(action.result.error)) {
					Ext.Msg.alert(KEY_TIP, action.result.error);
					return;
				}
				Ext.Msg.alert(KEY_TIP, TIP_ADDED_FAILURE);
			}
		});
	}
});

var add_window = new Ext.Window({
	title: ADDED_LINE,
	plain: true,
	id: ADDED_WINDOW_ID,
	width: 500,
	height: 200,
	modal: true,
	resizable: false,
	closeAction: 'hide',
	plain: true,
	items: [addFormPanel],
	buttons: [saveButton]
});

var addLineButton = new Ext.Button({
	text: KEY_ADDED,
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp(ID_PARAMETER_DEPARTMENT).getValue().split("/")[0];

		if (departmentCode == '') {
			Ext.Msg.alert('提示', '请选择网点!');
			return;
		}

		if (isAreaCode(departmentCode)) {
			Ext.Msg.alert('提示', "只能在区部以下网点新增线路！");
			return;
		}

		Ext.getCmp(BELONG_DEPARTMENT_CODE).setValue(departmentCode);
		add_window.show(this);
	}
});