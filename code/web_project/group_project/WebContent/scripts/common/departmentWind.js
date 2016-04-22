// <%@ page language="java" contentType="text/html; charset=utf-8"%>
var DEPARTMENT_INPUT_ID = "department_input_id";
var department_form = new Ext.form.FormPanel({
	region : "north",
	height : 40,
	frame : true,
	items : [{
		xtype : "hidden",
		id : DEPARTMENT_INPUT_ID
	},{
		xtype : "trigger",
		triggerClass : 'x-form-search-trigger',
		fieldLabel : '网点代码',
		name : "deptCode",
		onTriggerClick : function() {
			department_grid.getStore().load();
		}
	}]
});

var department_sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : true,
	header : ""
});

var department_store = new Ext.data.JsonStore({
	url : "../common/listAllDepartment.action",
	fields : ["id","deptCode","deptName","typeCode","areaDeptCode"],
	root : "departmentList",
	totalProperty : "totalSize",
	listeners : {
		'beforeload' : function() {
			department_grid.getStore().baseParams = department_form.getForm().getValues();
			department_grid.getStore().baseParams['limit'] = department_grid.getTopToolbar().pageSize;
		}
	}
});

var department_grid = new Ext.grid.GridPanel({
	sm : department_sm,
	tbar : new Ext.PagingToolbar({
		pageSize : 10,
		store : department_store,
		displayInfo : true
	}),
	store : department_store,
	loadMask : true,
	region : "center",
	autoExpandColumn : "autoDeptName",
	listeners : {
		'dblclick' : function() {
			handleSelect();
		}
	},
	columns : [department_sm,{
		header : '区域代码',
		dataIndex : "areaDeptCode"
	},{
		header : '网点代码',
		dataIndex : "deptCode"
	},{
		header : '网点名称',
		dataIndex : "deptName",
		id : "autoDeptName"
	},{
		header : '网点类型',
		dataIndex : "typeCode"
	}]
});

var department_wind = new Ext.Window({
	title : '选择网点',
	width : 500,
	height : 390,
	closeAction : "hided",
	modal : true,
	resizable : false,
	layout : "border",
	hided : function() {
		department_form.getForm().reset();
		department_sm.clearSelections();
		department_wind.hide();
	},
	tbar : [{
		text : '选择',
		minWidth : 60,
		handler : function() {
			handleSelect();
		}
	},{
		text : '取消',
		minWidth : 60,
		handler : function() {
			department_form.getForm().reset();
			department_sm.clearSelections();
			department_wind.hided();
		}
	}],
	items : [department_form,department_grid]
});

function handleSelect() {
	if (!department_sm.hasSelection()) {
		Ext.Msg.alert('提示', '请选择一个网点！');
		return false;
	}
	var department_input_id = Ext.getCmp(DEPARTMENT_INPUT_ID).getValue();
	Ext.getCmp(department_input_id).setValue(department_sm.getSelected().data.deptCode);
	department_sm.clearSelections();
	department_wind.hided();
}