// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var btnSearch = new Ext.Button({
	text: "查 询",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		query();
	}
});

var query = function() {
	var startTime = Ext.util.Format.date(Ext.getCmp('query.bigenTime').getValue(), 'Ymd');
	var endTime = Ext.util.Format.date(Ext.getCmp('query.endTime').getValue(), 'Ymd');
	if (!Ext.isEmpty(startTime) && Ext.isEmpty(endTime)) {
		Ext.Msg.alert('提示', '当开始日期不为空时，结束日期也不能为空!');
		return;
	}

	if (Ext.isEmpty(startTime) && !Ext.isEmpty(endTime)) {
		Ext.Msg.alert('提示', '当结束日期不为空时，开始日期也不能为空!');
		return;
	}
	
	if (!Ext.isEmpty(startTime) && !Ext.isEmpty(endTime)) {
		if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
			Ext.Msg.alert('提示', '开始日期不能大于结束日期!');
			return;
		}
	}
	
	store.setBaseParam("packageName", Ext.getCmp('query.packageName').getValue().trim());
	store.setBaseParam("procedureName", Ext.getCmp('query.procedureName').getValue().trim());
	store.setBaseParam("bigenTime", startTime);
	store.setBaseParam("endTime", endTime);
	store.load({
		params: {
			start: 0,
			limit: 40
		}
	});
};

var tbar = [];
addBar('<app:isPermission code="/common/procedureExecutionLog_query.action">a</app:isPermission>', btnSearch);

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
				xtype: 'textfield',
				id: 'query.procedureName',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '存储过程名称',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.packageName',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '包名称',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.bigenTime',
				name: 'query.bigenTime',
				anchor: '90%',
				format: 'Y/m/d',
				fieldLabel: '开始日期'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.endTime',
				name: 'query.endTime',
				anchor: '90%',
				format: 'Y/m/d',
				fieldLabel: '结束日期'
			}]
		}]
	}]
});

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '异常发生时有关流水号的值',
		sortable: true,
		dataIndex: 'seqNo',
		align: "center",
		width:150
	},{
		header: '过程名称',
		sortable: true,
		dataIndex: 'procedureName',
		align: "center",
		width:200
	},{
		header: '异常发生时间',
		sortable: true,
		dataIndex: 'exceptionTm',
		align: "center",
		width:150,
		renderer: function(value) {
			if (Ext.isEmpty(value)) {
				return "";
			}else{
				return value.replace("T", " ");
			}
		}
	},{
		header: '异常代码',
		sortable: true,
		dataIndex: 'exceptionCode',
		align: "center",
		width:120
	},{
		header: '异常描述',
		sortable: true,
		dataIndex: 'exceptionDesc',
		align: "center",
		width:150
	},{
		header: '异常备注:BEGIN表示开始,END表示结束,ERROR表示失败',
		sortable: true,
		dataIndex: 'exceptionRemk',
		align: "center",
		width:110
	},{
		header: '发生异常的位置(行号)',
		sortable: true,
		dataIndex: 'lineNo',
		align: "center",
		width:110
	},{
		header: '包名称',
		sortable: true,
		dataIndex: 'packageName',
		align: "center",
		width:110
	},{
		header: '调用序号',
		sortable: true,
		dataIndex: 'callSno',
		align: "center",
		width:110
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'seqNo',
	mapping: 'seqNo',
	type: 'string'
},{
	name: 'procedureName',
	mapping: 'procedureName',
	type: 'String'
},{
	name: 'exceptionTm',
	mapping: 'exceptionTm',
	type: 'string'
},{
	name: 'exceptionCode',
	mapping: 'exceptionCode',
	type: 'string'
},{
	name: 'exceptionDesc',
	mapping: 'exceptionDesc',
	type: 'string'
},{
	name: 'exceptionRemk',
	mapping: 'exceptionRemk',
	type: 'string'
},{
	name: 'lineNo',
	mapping: 'lineNo',
	type: 'string'
},{
	name: 'packageName',
	mapping: 'packageName',
	type: 'string'
},{
	name: 'callSno',
	mapping: 'callSno',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../common/procedureExecutionLog_query.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
	}, record)
});

// 分页组件
var pageBar = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 40,
	emptyMsg: '未检索到数据'
});

// 表格构建
var grid = new Ext.grid.GridPanel({
	cm: cm,
	sm: sm,
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