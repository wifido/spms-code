// <%@ page language="java" contentType="text/html; charset=utf-8"%>
var filterDeptCodeType = '${filterDeptCodeType}';

// 查询按钮
var btnSearch = new Ext.Button({
	text: "查 询",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		querySchedule();
	}
});

// 查询方法
var querySchedule = function() {
	var deptCode = Ext.getCmp('DEPT_CODE').getValue().trim();
	if (Ext.getCmp('START_TIME').getValue() == "") {
		Ext.getCmp('START_TIME').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择开始时间！");
		return;
	}
	if (Ext.getCmp('END_TIME').getValue() == "") {
		Ext.getCmp('END_TIME').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择结束时间！");
		return;
	}
	if (deptCode == "") {
		Ext.Msg.alert('提示', '中转场代码不能为空！');
		return;
	}
	var startTime = Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Y/m/d');
	var endTime = Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Y/m/d');

	if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
		Ext.Msg.alert('提示', '开始时间不能大于结束时间!');
		return;
	}

	store.setBaseParam("START_TIME", startTime);
	store.setBaseParam("END_TIME", endTime);
	store.setBaseParam("GROUP_CODE", Ext.getCmp('GROUP_CODE').getValue().trim());
	store.setBaseParam("DEPT_CODE", Ext.getCmp('DEPT_CODE').getValue().trim());
	store.setBaseParam("EMP_CODE", Ext.getCmp('EMP_CODE').getValue().trim());
	store.setBaseParam("EMP_NAME", Ext.getCmp('EMP_NAME').getValue().trim());
	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};

var btnExport = new Ext.Button({
	text: "导 出",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportSchedule();
	}
});

// 导出方法
var exportSchedule = function() {
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: '../report/exportReport.action',
		method: 'POST',
		params: {
			START_TIME: Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Y/m/d'),
			END_TIME: Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Y/m/d'),
			GROUP_CODE: Ext.getCmp('GROUP_CODE').getValue().trim(),
			DEPT_CODE: Ext.getCmp('DEPT_CODE').getValue().trim(),
			EMP_CODE: Ext.getCmp('EMP_CODE').getValue().trim(),
			EMP_NAME: Ext.getCmp('EMP_NAME').getValue().trim()
		},
		success: function(res, config) {
			myMask.hide();
			var obj = Ext.decode(res.responseText);
			if (!obj.success) {
				Ext.Msg.alert("提示", obj.msg);
				return;
			}
			var url = obj.fileName;
			window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		},
		failure: function(res, config) {
			var obj = Ext.decode(res.responseText);
			Ext.Msg.alert("提示", obj.msg);
			myMask.hide();
		}
	});
};

var btnExportUserPermission = new Ext.Button({
	text: "导 出授权明细",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportUserPermission();
	}
});

// 导出方法
var exportUserPermission = function() {
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: '../report/exportUserPermissionReport.action',
		method: 'POST',
		success: function(res, config) {
			myMask.hide();
			var obj = Ext.decode(res.responseText);
			if (!obj.success) {
				Ext.Msg.alert("提示", obj.msg);
				return;
			}
			var url = obj.fileName;
			window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		},
		failure: function(res, config) {
			var obj = Ext.decode(res.responseText);
			Ext.Msg.alert("提示", obj.msg);
			myMask.hide();
		}
	});
};

// 重置
var btnReset = new Ext.Button({
	text: "重置",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		resetSearchCriteria();
	}
});

var resetSearchCriteria = function() {
	Ext.getCmp("START_TIME").setValue("");
	Ext.getCmp("END_TIME").setValue("");
	Ext.getCmp("GROUP_CODE").setValue("");
	Ext.getCmp("DEPT_CODE").setValue("");
	Ext.getCmp("EMP_CODE").setValue("");
	Ext.getCmp("EMP_NAME").setValue("");
	Ext.getCmp("DEPT_NAME").setValue("");
};

var treePanel = new Ext.tree.TreePanel({
	region: 'west',
	margins: '1 1 1 1',
	width: 300,
	title: '中转场信息',
	collapsible: true,
	autoScroll: true,
	root: new Ext.tree.AsyncTreeNode({
		id: '0',
		text: '顺丰速运',
		loader: new Ext.tree.TreeLoader({ // ../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField=
			dataUrl: "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
		})
	}),
	listeners: {
		beforeclick: function(node, e) {

			if (node != null && node.id != 0) {
				if (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1) {
					Ext.getCmp("DEPT_NAME").setValue(node.text);
					Ext.getCmp("DEPT_CODE").setValue(node.attributes.code);
				}
			}
		}
	}
});

var tbar = [];

addBar('<app:isPermission code="/report/report_querySchedule.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code="/report/report_exportSchedule.action">a</app:isPermission>', btnExport);
addBar('<app:isPermission code="/report/report_exportUserPermissionReport.action">a</app:isPermission>', btnExportUserPermission);
addBar('<app:isPermission code = "">a</app:isPermission>', btnReset);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}
// 顶部的Panel
var topPanel = new Ext.Panel({
	frame: true,
	layout: 'column',
	height: 160,
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		title: '查询条件',
		layout: "column",
		columnWidth: 1,
		style: 'margin-top:5px;',
		frame: true,
		items: [{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'START_TIME',
				name: 'START_TIME',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				// minValue : new Date(),
				fieldLabel: '开始时间<font color=red>*</font>'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'END_TIME',
				name: 'END_TIME',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				// minValue : new Date(),
				fieldLabel: '结束时间<font color=red>*</font>'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'DEPT_NAME',
				editable: false,
				triggerAction: "all",
				fieldLabel: '中转场代码<font color=red>*</font>',
				anchor: '90%',
				listeners: {
					focus: function() {
						var treeWindow = new Ext.Window({
							plain: true,
							layout: 'form',
							resizable: true, // 改变大小
							draggable: true, // 不允许拖动
							closeAction: 'hide',// 可被关闭 close or
							modal: true, // 模态窗口
							width: 320,
							height: 400,
							title: '中转场代码',
							items: [treePanel], // 包含tree
							buttonAlign: 'right',
							loadMask: true,
							autoScroll: true,
							buttons: [{
								xtype: 'button',
								align: 'right',
								text: '确定',
								handler: function() {
									treeWindow.hide();
								}
							},{
								xtype: 'button',
								text: '取消',
								handler: function() {
									Ext.getCmp("DEPT_NAME").setValue("");
									Ext.getCmp("DEPT_CODE").setValue("");
									treeWindow.hide();
								}
							}]
						});
						treeWindow.show(this);
					}
				}
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'hidden',
				id: 'DEPT_CODE',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '小组代码',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'GROUP_CODE',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '小组代码',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'EMP_CODE',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '工号',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'EMP_NAME',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '姓名',
				anchor: '90%'
			}]
		}]
	}]
});

var record_start = 0;
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [new Ext.grid.RowNumberer({
		header: "序号",
		width: 40,
		renderer: function(value, metadata, record, rowIndex) {
			return record_start + 1 + rowIndex;
		}
	}),{
		header: '日期',
		sortable: true,
		dataIndex: 'SHEDULE_DT',
		align: "center",
		renderer: function(value) {
			return value.substring(0, 10);
		}

	},{
		header: '中转场代码',
		sortable: true,
		dataIndex: 'DEPT_CODE',
		align: "center"
	},{
		header: '小组代码',
		sortable: true,
		dataIndex: 'GROUP_CODE',
		align: "center"
	},{
		header: '工号',
		sortable: true,
		dataIndex: 'EMP_CODE',
		align: "center"
	},{
		header: '姓名',
		sortable: true,
		dataIndex: 'EMP_NAME',
		align: "center"
	},{
		header: '班别代码',
		sortable: true,
		dataIndex: 'CLASS_CODE',
		align: "center"
	},{
		header: '工序代码',
		sortable: true,
		dataIndex: 'PROCESS_CODE',
		align: "center"
	},{
		header: '工序含金量',
		sortable: true,
		dataIndex: 'DIFFICULTY_MODIFY_VALUE',
		align: "center"
	},{
		header: '排班时长(小时)',
		sortable: true,
		dataIndex: 'TIMELENGTH',
		align: "center"
	},{
		header: '考勤小时数',
		sortable: true,
		dataIndex: 'ATTENDANCE_HOURS',
		align: "center"
	},{
		header: '加班时长',
		sortable: true,
		dataIndex: 'OVERTIME_HOURS',
		align: "center"
	},{
		header: '请假类型',
		sortable: true,
		dataIndex: 'LEAVE_TYPE',
		align: "center"
	},{
		header: '考勤系数',
		sortable: true,
		dataIndex: 'WORK_TIME',
		align: "center"
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'ID',
	mapping: 'ID',
	type: 'string'
},{
	name: 'SHEDULE_DT',
	mapping: 'SHEDULE_DT',
	type: 'String'
},{
	name: 'DEPT_CODE',
	mapping: 'DEPT_CODE',
	type: 'string'
},{
	name: 'GROUP_CODE',
	mapping: 'GROUP_CODE',
	type: 'string'
},{
	name: 'EMP_CODE',
	mapping: 'EMP_CODE',
	type: 'string'
},{
	name: 'EMP_NAME',
	mapping: 'EMP_NAME',
	type: 'string'
},{
	name: 'PROCESS_CODE',
	mapping: 'PROCESS_CODE',
	type: 'string'
},{
	name: 'ESTIMATE_VALUE',
	mapping: 'ESTIMATE_VALUE',
	type: 'string'
},{
	name: 'INTENSITY_VALUE',
	mapping: 'INTENSITY_VALUE',
	type: 'string'
},{
	name: 'DIFFICULTY_VALUE',
	mapping: 'DIFFICULTY_VALUE',
	type: 'string'
},{
	name: 'EMP_POST_TYPE',
	mapping: 'EMP_POST_TYPE',
	type: 'string'
},{
	name: 'DIFFICULTY_MODIFY_VALUE',
	mapping: 'DIFFICULTY_MODIFY_VALUE',
	type: 'string'
},{
	name: 'TIMELENGTH',
	mapping: 'TIMELENGTH',
	type: 'string'
},{
	name: 'ATTENDANCE_HOURS',
	mapping: 'ATTENDANCE_HOURS',
	type: 'String'
},{
	name: 'WORK_TIME',
	mapping: 'WORK_TIME',
	type: 'String'
},{
	name: 'OVERTIME_HOURS',
	mapping: 'OVERTIME_HOURS',
	type: 'String'
},{
	name: 'CLASS_CODE',
	mapping: 'CLASS_CODE',
	type: 'String'
},{
	name: 'LEAVE_TYPE',
	mapping: 'LEAVE_TYPE',
	type: 'String'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../report/queryDetailReport.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'dataMap.root',
		totalProperty: 'dataMap.totalSize'
	}, record)
});

// 分页组件
var pageBar = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

// 表格构建
var grid = new Ext.grid.GridPanel({
	cm: cm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pageBar,
	viewConfig: {
	// forceFit: true
	}
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
		items: [centerPanel]
	});
	// grid.render(Ext.getBody())
	// grid.show();
});