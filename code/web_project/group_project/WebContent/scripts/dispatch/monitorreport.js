//<%@ page language="java" contentType="text/html; charset=utf-8"%>

//左侧网点树

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

// 导出方法
var btnExport = new Ext.Button({
	text: "导 出",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		exportSchedule();
	}
});

// 重置
var btnReset = new Ext.Button({
	text: "重 置",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		resetSearchCriteria();
	}
});

var resetSearchCriteria = function() {
	Ext.getCmp('query.deptCode').reset();
	Ext.getCmp('query.displayMode').reset();
	Ext.getCmp('query.startTime').reset();
	Ext.getCmp('query.endTime').reset();
};

var tbar = [];

addBar('<app:isPermission code = "/dispatch/dispatch_querySchedule.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/dispatch/dispatch_exportSchedule.action">a</app:isPermission>', btnExport);
addBar('<app:isPermission code = "/dispatch/dispatch_reset.action">a</app:isPermission>', btnReset);

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
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'trigger',
				triggerClass: 'x-form-search-trigger',
				id: 'query.deptCode',
				fieldLabel: '<font color=red>网点代码*</font>',
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						branchDepartmentEditable: true,
						isDispatch: true,
						callBackInput: "query.deptCode"
					});
					_window.show(this);
				}
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'combo',
				id: 'query.displayMode',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				forceSelection: true,
				emptyText: '请选择...',
				fieldLabel: '展示方式<font color=red>*</font>',
				store: [['0','全网'],['1','经营本部'],['2','地区'],['4','网点']],
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.startTime',
				name: 'startTime',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				fieldLabel: '开始时间<font color=red>*</font>'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.endTime',
				name: 'endTime',
				anchor: '90%',
				allowBlank: false,
				format: 'Y/m/d',
				fieldLabel: '结束时间<font color=red>*</font>'
			}]
		}]
	}]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '日期',
		sortable: true,
		dataIndex: 'DAY_OF_MONTH',
		align: "center"
	},{
		header: '经营本部',
		sortable: true,
		dataIndex: 'HQ_CODE',
		align: "center"
	},{
		header: '地区代码',
		sortable: true,
		dataIndex: 'AREA_CODE',
		align: "center"
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'DEPT_CODE',
		align: "center"
	},{
		header: '全日制需排班数',
		sortable: true,
		dataIndex: 'FULLTIME_EMP_NUM',
		align: "center"
	},{
		header: '非全日制需排班数',
		sortable: true,
		dataIndex: 'NOT_FULLTIME_EMP_NUM',
		align: "center"
	},{
		header: '全日制排上人数',
		sortable: true,
		dataIndex: 'FULLTIME_SCHEDULING_NUM',
		align: "center"
	},{
		header: '全日制排休人数',
		sortable: true,
		dataIndex: 'FULLTIME_REST_NUM',
		align: "center"
	},{
		header: '非全日制排上人数',
		sortable: true,
		dataIndex: 'NOT_FULLTIME_SCHEDULING_NUM',
		align: "center"
	},{
		header: '非全日制排休人数',
		sortable: true,
		dataIndex: 'NOT_FULLTIME_REST_NUM',
		align: "center"
	},{
		header: '全日制未排班数',
		sortable: true,
		dataIndex: 'FULLTIME_NOT_SCHEDULING',
		align: "center"
	},{
		header: '非全日制未排班数',
		sortable: true,
		dataIndex: 'NOT_FULLTIME_NOT_SCHEDULING',
		align: "center"
	},{
		header: '全日制排班及时率',
		sortable: true,
		dataIndex: 'FULLTIME_SCHEDULING_RATE',
		align: "center"
	},{
		header: '非全日制排班及时率',
		sortable: true,
		dataIndex: 'NOT_FULLTIME_SCHEDULING_RATE',
		align: "center"
	},{
		header: '全日制规划上班率',
		sortable: true,
		dataIndex: 'FULLTIME_PLANNING_WORK_RATE',
		align: "center"
	},{
		header: '非全日制规划上班率',
		sortable: true,
		dataIndex: 'NO_FULLTIME_PLAN_WORK_RATE',
		align: "center"
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'DAY_OF_MONTH',
	mapping: 'DAY_OF_MONTH',
	type: 'string'
},{
	name: 'HQ_CODE',
	mapping: 'HQ_CODE',
	type: 'string'
},{
	name: 'AREA_CODE',
	mapping: 'AREA_CODE',
	type: 'string'
},{
	name: 'DEPT_CODE',
	mapping: 'DEPT_CODE',
	type: 'string'
},{
	name: 'FULLTIME_EMP_NUM',
	mapping: 'FULLTIME_EMP_NUM',
	type: 'string'
},{
	name: 'NOT_FULLTIME_EMP_NUM',
	mapping: 'NOT_FULLTIME_EMP_NUM',
	type: 'string'
},{
	name: 'FULLTIME_SCHEDULING_NUM',
	mapping: 'FULLTIME_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'FULLTIME_REST_NUM',
	mapping: 'FULLTIME_REST_NUM',
	type: 'string'
},{
	name: 'NOT_FULLTIME_SCHEDULING_NUM',
	mapping: 'NOT_FULLTIME_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'NOT_FULLTIME_REST_NUM',
	mapping: 'NOT_FULLTIME_REST_NUM',
	type: 'string'
},{
	name: 'FULLTIME_NOT_SCHEDULING',
	mapping: 'FULLTIME_NOT_SCHEDULING',
	type: 'string'
},{
	name: 'NOT_FULLTIME_NOT_SCHEDULING',
	mapping: 'NOT_FULLTIME_NOT_SCHEDULING',
	type: 'string'
},{
	name: 'FULLTIME_SCHEDULING_RATE',
	mapping: 'FULLTIME_SCHEDULING_RATE',
	type: 'string'
},{
	name: 'NOT_FULLTIME_SCHEDULING_RATE',
	mapping: 'NOT_FULLTIME_SCHEDULING_RATE',
	type: 'string'
},{
	name: 'FULLTIME_PLANNING_WORK_RATE',
	mapping: 'FULLTIME_PLANNING_WORK_RATE',
	type: 'string'
},{
	name: 'NO_FULLTIME_PLAN_WORK_RATE',
	mapping: 'NO_FULLTIME_PLAN_WORK_RATE',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../dispatch/monitorReport_query.action'
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

// 查询方法
var querySchedule = function(node) {
	
	if (Ext.getCmp("query.deptCode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	if (Ext.getCmp("query.displayMode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择展示方式！");
		return;
	}
	
	if (Ext.getCmp("query.startTime").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择开始时间！");
		return;
	}
	
	if (Ext.getCmp("query.endTime").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择结束时间！");
		return;
	}

	store.setBaseParam("deptCode", getDepartmentCodes());// checkedDepartmentListForSearching.toString()/Ext.getCmp('query.deptCode').getValue().split('/')[0]
	store.setBaseParam("startTime", Ext.util.Format.date(Ext.getCmp('query.startTime').getValue(), 'Ymd'));
	store.setBaseParam("endTime", Ext.util.Format.date(Ext.getCmp('query.endTime').getValue(), 'Ymd'));
	store.setBaseParam("displayMode", Ext.getCmp('query.displayMode').getValue());

	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};

function getDepartmentCodes() {
	return Ext.getCmp("query.deptCode").getValue().split("/")[0];
}

// 导出方法
var exportSchedule = function() {
	if (Ext.getCmp("query.deptCode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	if (Ext.getCmp("query.displayMode").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择展示方式！");
		return;
	}
	
	if (Ext.getCmp("query.startTime").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择开始时间！");
		return;
	}
	
	if (Ext.getCmp("query.endTime").getValue() == "") {
		Ext.Msg.alert("提示", "请先选择结束时间！");
		return;
	}
	
	var myMask = new Ext.LoadMask(centerPanel.getEl(), {
		msg: "正在导出..."
	});
	myMask.show();
	Ext.Ajax.request({
		url: "monitorReport_export.action",
		method: 'POST',
		params: {
			'startTime': Ext.util.Format.date(Ext.getCmp('query.startTime').getValue(), 'Ymd'),
			'endTime': Ext.util.Format.date(Ext.getCmp('query.endTime').getValue(), 'Ymd'),
			'displayMode': Ext.getCmp('query.displayMode').getValue(),
			'deptCode': getDepartmentCodes()
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

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout: "border",
		items: [centerPanel]
	});
});
