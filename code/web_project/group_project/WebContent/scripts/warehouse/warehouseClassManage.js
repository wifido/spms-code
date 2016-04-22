//<%@ page language="java" contentType="text/html; charset=utf-8"%>
// 查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		querySchedule();
	}
});

// 删除
var btnDelete = new Ext.Button({
	text : "删除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		var departmentCode = Ext.getCmp("query.branchCode").getValue();
		var dynamicDeptIds = [ departmentCode ];
		Ext.Ajax.request({
			url : '../warehouse/validateAuthority.action',
			method : 'POST',
			params : {
				dept_code : dynamicDeptIds
			},
			success : function(res, config) {
				var data = Ext.decode(res.responseText);
				if (data.dataMap.success == false) {
					Ext.Msg.alert("提示", "暂无该网点权限！");
				}else {
					deleteClassInfomation();
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});

var btnReset = new Ext.Button({
	text : "重置",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		Ext.getCmp('query.branchCode').setValue("");
		Ext.getCmp('query.enableTime').setValue("");
		Ext.getCmp('query.disableTime').setValue("");
	}
});

function deleteClassInfomation() {
	var recordes = grid.getSelectionModel().getSelections();

	if (recordes.length == 0) {
		Ext.Msg.alert('提示', '必须选择一条数据，才能删除！');
		return;
	}
	var classIds = '';
	Ext.each(recordes, function(node, index) {
		classIds += node.data["SCHEDULE_ID"];
		if (index + 1 < recordes.length) {
			classIds += ",";
		}
	});
	Ext.Msg.confirm('提示', '确定要删除吗？', function(button) {
		if (button == "yes") {

			Ext.Ajax.request({
				url : '../warehouse/delete_warehouseClass.action',
				method : 'POST',
				params : {
					CLASSIDS : classIds
				},
				success : function(res, config) {
					var data = Ext.decode(res.responseText);
					Ext.Msg.alert('提示', data.dataMap["msg"]);
					querySchedule();
				},
				failure : function() {
					Ext.Msg.alert("提示", "人员删除出错！");
					return;
				}
			});
		}
	});
}

var tbar = [];

addBar('<app:isPermission code = "/warehouse/query_warehouseClass.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/warehouse/add_warehouseClass.action">a</app:isPermission>', btnAddClassInfo);
addBar('<app:isPermission code = "/warehouse/update_warehouseClass.action">a</app:isPermission>', btnUpdateClassInfo);
addBar('<app:isPermission code = "/warhouse/delete_warehouseClass.action">a</app:isPermission>', btnDelete);
addBar('<app:isPermission code = "/warhouse/btnReset.action">a</app:isPermission>', btnReset);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

// 顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout : 'column',
	height : 160,
	tbar : tbar,
	items : [{
		xtype : 'fieldset',
		title : '查询条件',
		layout : "column",
		columnWidth : 1,
		style : 'margin-top:5px;',
		frame : true,
		items : [{
			columnWidth : .3,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [{
				xtype : "textfield",
				fieldLabel : '<font color=red>网点代码*</font>',
				id : "query.branchCode",
				maxLength : 30,
				anchor : '90%',
				onTriggerClick : function() {
					var _window = new Ext.sf_dept_window({
						callBackInput : "query.branchCode"
					});
					_window.show(this);
				},
				validator : function(value) {

				}
			}]
		},{
			columnWidth : .3,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [{
				xtype : 'datefield',
				id : 'query.enableTime',
				format : 'Y-m-d',
				fieldLabel : '生效日期',
				anchor : '90%'
			}]
		},{
			columnWidth : .3,
			labelWidth : 120,
			labelAlign : 'right',
			layout : 'form',
			items : [{
				xtype : 'datefield',
				id : 'query.disableTime',
				format : 'Y-m-d',
				fieldLabel : '失效日期',
				anchor : '90%'
			}]
		}]
	}]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

var record_start = 0;
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns : [sm,{
		header : 'ID',
		hidden : true,
		sortable : true,
		dataIndex : 'SCHEDULE_ID',
		align : "center"
	},{
		header : '地区代码',
		sortable : true,
		dataIndex : 'AREA_CODE',
		align : "center;vertical-align:middle"
	},{
		header : '网点代码',
		sortable : true,
		dataIndex : 'DEPT_CODE',
		align : "center;vertical-align:middle"
	},{
		header : '上班时段代码',
		sortable : true,
		dataIndex : 'SCHEDULE_CODE',
		align : "center;vertical-align:middle"
	},{
		header : '开始时间',
		sortable : true,
		dataIndex : 'START1_TIME',
		align : "center;vertical-align:middle"
	},{
		header : '结束时间',
		sortable : true,
		dataIndex : 'END1_TIME',
		align : "center;vertical-align:middle"
	},{
		header : '工作时长(小时)',
		sortable : true,
		dataIndex : 'TIMELENGTH',
		align : "center;vertical-align:middle"
	},{
		header : '是否跨天',
		sortable : true,
		dataIndex : 'IS_CROSS_DAY',
		align : "center;vertical-align:middle"
	},{
		header : '生效日期',
		sortable : true,
		dataIndex : 'ENABLE_DT',
		align : "center;vertical-align:middle"
	},{
		header : '失效日期',
		sortable : true,
		dataIndex : 'DISABLE_DT',
		align : "center;vertical-align:middle",
		renderer : function(value, metaData, record, rowIndex, colIndex, store) {
			var currentDate = Ext.util.Format.date(new Date(), 'Ymd');
			if (Ext.util.Format.date(record.data["DISABLE_DT"], 'Ymd') <= currentDate) {
				return "<font color=red>" + value + "</font>";
			}
			return value;
		}
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name : 'SCHEDULE_ID',
	mapping : 'SCHEDULE_ID',
	type : 'string'
},{
	name : 'AREA_CODE',
	mapping : 'AREA_CODE',
	type : 'string'
},{
	name : 'DEPT_CODE',
	mapping : 'DEPT_CODE',
	type : 'string'
},{
	name : 'SCHEDULE_CODE',
	mapping : 'SCHEDULE_CODE',
	type : 'string'
},{
	name : 'START1_TIME',
	mapping : 'START1_TIME',
	type : 'string'
},{
	name : 'END1_TIME',
	mapping : 'END1_TIME',
	type : 'string'
},{
	name : 'TIMELENGTH',
	mapping : 'TIMELENGTH',
	type : 'string'
},{
	name : 'ENABLE_DT',
	mapping : 'ENABLE_DT',
	type : 'string'
},{
	name : 'DISABLE_DT',
	mapping : 'DISABLE_DT',
	type : 'string'
},{
	name : 'IS_CROSS_DAY',
	mapping : 'IS_CROSS_DAY',
	type : 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
		url : '../warehouse/query_warehouseClass.action'
	}),
	reader : new Ext.data.JsonReader({
		root : 'dataMap.root',
		totalProperty : 'dataMap.totalSize'
	}, record)
});

// 分页组件
var pageBar = new Ext.PagingToolbar({
	store : store,
	displayInfo : true,
	displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize : 20,
	emptyMsg : '未检索到数据'
});

// 表格构建
var grid = new Ext.grid.GridPanel({
	cm : cm,
	sm : sm,
	store : store,
	autoScroll : true,
	loadMask : true,
	tbar : pageBar,
	viewConfig : {
	// forceFit: true
	}
});

// 中部的Panel
var centerPanel = new Ext.Panel({
	region : 'center',
	margins : '1 1 1 0',
	items : [topPanel,grid],
	listeners : {
		resize : function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			grid.setWidth(adjWidth - 5);
			grid.setHeight(adjHeight - 165);
		}
	}
});

function parseDepartment(departmentCodes) {
	var codes = [];
	Ext.each(departmentCodes.split(','), function(v, i) {
		codes.push("'" + (v.split('/')[0]) + "'");
	});
	return codes.join(",");
}

// 查询方法
var querySchedule = function(node) {
	var departmentCodes = Ext.getCmp("query.branchCode").getValue();
	if (Ext.isEmpty(departmentCodes)) {
		Ext.Msg.alert("提示", "请先选择网点！");
		return;
	}
	var enableTime = Ext.getCmp('query.enableTime').getValue();
	if (!Ext.isEmpty(enableTime)) {
		enableTime = enableTime.format("Ymd");
	}
	var disableTime = Ext.getCmp('query.disableTime').getValue();
	if (!Ext.isEmpty(disableTime)) {
		disableTime = disableTime.format("Ymd");
	}
	store.setBaseParam("DEPT_CODE", parseDepartment(departmentCodes));
	store.setBaseParam("ENABLE_DT", enableTime);
	store.setBaseParam("DISABLE_DT", disableTime);
	store.load({
		params : {
			start : 0,
			limit : 20
		}
	});
};

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout = 300000;
	Ext.BLANK_IMAGE_URL = "../ext-3.4.0/resources/images/default/s.gif";
	new Ext.Viewport({
		layout : "border",
		items : [centerPanel]
	});

});