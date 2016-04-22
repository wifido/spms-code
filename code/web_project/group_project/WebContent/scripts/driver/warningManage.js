//<%@ page language="java" contentType="text/html; charset=utf-8"%>

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
		beforeclick: function(node) {
			Ext.getCmp('departmentCode').setValue(node.text);
		}
	}
});

function validQueryCondition(warningMonth, departmentCode) {
	if (departmentCode == '') {
		Ext.Msg.alert('提示', '请选择网点!');
		Ext.getCmp('departmentCode').isValid(false);
		return true;
	}

	if (warningMonth == '') {
		Ext.Msg.alert('提示', '预警月份不能为空!');
		Ext.getCmp('warningMonth').isValid(false);
		return true;
	}

	return false;
}

function resetGridPanel() {
	resultGridPanel.getStore().removeAll();
	pagingButton.updateInfo();

	resultGridPanel2.getStore().removeAll();
	pagingButton2.updateInfo();
}

var warningPath = ['../driver/exportWarning.action','../driver/exportContinuous.action'];

var searchButton = new Ext.Button({
	text: '查询',
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var warningMonth = Ext.getCmp('warningMonth').getValue();
		var departmentCode = Ext.getCmp('departmentCode').getValue();

		if (validQueryCondition(warningMonth, departmentCode)) {
			return;
		}

		var index = Ext.getCmp('warning').getValue();
		rightPanel.items.items[index].getStore().setBaseParam('DEPARTMENT_CODE', departmentCode.split('/')[0]);
		rightPanel.items.items[index].getStore().setBaseParam('WARNING_MONTH', Ext.util.Format.date(warningMonth, 'Ym'));
		rightPanel.items.items[index].getStore().load({
			params : {
				start : 0,
				limit : 20
			}
		});
	}
});

var exportButton = new Ext.Button({
	text: '导出',
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		var warningMonth = Ext.getCmp('warningMonth').getValue();
		var departmentCode = Ext.getCmp('departmentCode').getValue();

		if (validQueryCondition(warningMonth, departmentCode)) {
			return;
		}

		var isExport = new Ext.LoadMask(resultGridPanel.getEl(), {
			msg: "正在导出..."
		});
		isExport.show();

		var index = Ext.getCmp('warning').getValue();
		Ext.Ajax.request({
			url: warningPath[index - 1],
			method: 'POST',
			params: {
				DEPARTMENT_CODE: departmentCode.split('/')[0],
				WARNING_MONTH: Ext.util.Format.date(warningMonth, 'Ym')
			},
			success: function(response) {
				isExport.hide();
				var obj = Ext.decode(response.responseText);
				window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(obj.filePath));
			}
		});
	}
});

var tbar = [];

addBar('<app:isPermission code = "/driver/exportContinuousWarning.action">a</app:isPermission>', searchButton);
addBar('<app:isPermission code = "/driver/queryDriverContinuousWarningReport.action">a</app:isPermission>', exportButton);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

var queryPanel = new Ext.Panel({
	height: 120,
	frame: true,
	layout: 'column',
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		columnWidth: 1,
		title: '查询条件',
		style: {
			paddingTop: 5,
			paddingBottom: 5
		},
		layout: 'column',
		items: [{
			layout: 'form',
			columnWidth: .3,
			labelAlign: 'right',
			items: [{
				xtype: 'textfield',
				fieldLabel: '网点代码<label style="color: red">*</label>',
				readOnly: true,
				id: 'departmentCode',
				allowBlank: false,
				anchor: '90%'
			}]
		},{
			layout: 'form',
			columnWidth: .3,
			labelAlign: 'right',
			items: [{
				xtype: 'datefield',
				fieldLabel: '预警月份<label style="color: red">*</label>',
				id: 'warningMonth',
				format: 'Y-m',
				allowBlank: false,
				plugins: 'monthPickerPlugin',
				anchor: '90%'
			}]
		},{
			layout: 'form',
			columnWidth: .3,
			labelAlign: 'right',
			items: [{
				xtype: 'combo',
				fieldLabel: '预警类型<label style="color: red">*</label>',
				id: 'warning',
				editable: false,
				triggerAction: "all",
				mode: "local",
				value: '1',
				allowBlank: false,
				anchor: '90%',
				store: [['1','单月预警'],['2','季度预警']],
				listeners: {
					'select': function(object) {
						resetGridPanel();

						if (object.getValue() == '1') {

							resultGridPanel.show();
							resultGridPanel2.hide();
							resultGridPanel.setHeight(resultGridPanel2.ownerCt.getHeight() - 160);
							return;
						}

						resultGridPanel.hide();
						resultGridPanel2.show();
						resultGridPanel2.setHeight(resultGridPanel2.ownerCt.getHeight() - 160);
					}
				}
			}]
		}]
	}]
});

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: true
});

function getAreaCodeAndAreaName(value, record) {
	return value + "/" + record.data['areaName'];
}

function getDepartmentCodeAndDepartmentName(value, record) {
	return value + "/" + record.data['departmentName'];
}

var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '地区',
		sortable: true,
		dataIndex: 'areaCode',
		renderer: function(value, metaData, record) {
			return getAreaCodeAndAreaName(value, record);
		}
	},{
		header: '网点',
		sortable: true,
		dataIndex: 'departmentCode',
		renderer: function(value, metaData, record) {
			return getDepartmentCodeAndDepartmentName(value, record);
		}
	},{
		header: '员工姓名',
		sortable: true,
		dataIndex: 'employeeName'
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'employeeCode'
	},{
		header: '预警时间',
		sortable: true,
		dataIndex: 'driveDay'
	},{
		header: '最大连续出勤天数',
		sortable: true,
		dataIndex: 'warningDay'
	}]
});

var reader = new Ext.data.Record.create([{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'areaName',
	mapping: 'areaName',
	type: 'string'
},{
	name: 'departmentCode',
	mapping: 'departmentCode',
	type: 'string'
},{
	name: 'departmentName',
	mapping: 'departmentName',
	type: 'string'
},{
	name: 'employeeName',
	mapping: 'employeeName',
	type: 'string'
},{
	name: 'employeeCode',
	mapping: 'employeeCode',
	type: 'string'
},{
	name: 'warningDay',
	mapping: 'warningDay',
	type: 'string'
},{
	name: 'driveDay',
	mapping: 'driveDay',
	type: 'string'
},{
	name: 'firstMaxContinuousWorkingDay',
	mapping: 'firstMaxContinuousWorkingDay',
	type: 'string'
},{
	name: 'secondMaxContinuousWorkingDay',
	mapping: 'secondMaxContinuousWorkingDay',
	type: 'string'
},{
	name: 'thirdMaxContinuousWorkingDay',
	mapping: 'thirdMaxContinuousWorkingDay',
	type: 'string'
},{
	name: 'twoMonthContinuous',
	mapping: 'twoMonthContinuous',
	type: 'string'
},{
	name: 'threeMonthContinuous',
	mapping: 'threeMonthContinuous',
	type: 'string'
}]);

var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../driver/queryDriverWarningReport.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'result.root',
		totalProperty: 'result.totalSize'
	}, reader)
});

var pagingButton = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

var resultGridPanel = new Ext.grid.GridPanel({
	title: '查询结果',
	sm: sm,
	cm: cm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pagingButton
});

var sm2 = new Ext.grid.CheckboxSelectionModel({
	singleSelect: true
});

var store2 = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../driver/queryDriverContinuousWarningReport.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'result.root',
		totalProperty: 'result.totalSize'
	}, reader)
});

var cm2 = new Ext.grid.ColumnModel({
	columns: [sm2,{
		header: '地区',
		sortable: true,
		dataIndex: 'areaCode',
		renderer: function(value, metaData, record) {
			return getAreaCodeAndAreaName(value, record);
		}
	},{
		header: '网点',
		sortable: true,
		dataIndex: 'departmentCode',
		renderer: function(value, metaData, record) {
			return getDepartmentCodeAndDepartmentName(value, record);
		}
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'employeeCode'
	},{
		header: '员工姓名',
		sortable: true,
		dataIndex: 'employeeName'
	},{
		header: '一月连续出勤最大天数',
		sortable: true,
		dataIndex: 'firstMaxContinuousWorkingDay'
	},{
		header: '二月连续出勤最大天数',
		sortable: true,
		dataIndex: 'secondMaxContinuousWorkingDay'
	},{
		header: '三月连续出勤最大天数',
		sortable: true,
		dataIndex: 'thirdMaxContinuousWorkingDay'
	},{
		header: '是否连续2个月出勤超过6天',
		sortable: true,
		dataIndex: 'twoMonthContinuous',
		renderer: function(value) {
			return value == 'true' ? '是' : '否';
		}
	},{
		header: '是否连续3个月出勤超过6天',
		sortable: true,
		dataIndex: 'threeMonthContinuous',
		renderer: function(value) {
			return value == 'true' ? '是' : '否';
		}
	}]
});

var pagingButton2 = new Ext.PagingToolbar({
	store: store2,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

var resultGridPanel2 = new Ext.grid.GridPanel({
	title: '查询结果',
	sm: sm2,
	cm: cm2,
	store: store2,
	autoScroll: true,
	loadMask: true,
	tbar: pagingButton2,
	hidden: true
});

var rightPanel = new Ext.Panel({
	region: 'center',
	items: [queryPanel,resultGridPanel,resultGridPanel2],
	listeners: {
		resize: function(p, adjWidth, adjHeight) {
			resultGridPanel.setWidth(adjWidth - 5);
			resultGridPanel.setHeight(adjHeight - 165);
		}
	}
});

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout: 'border',
		items: [treePanel,rightPanel]
	});
});