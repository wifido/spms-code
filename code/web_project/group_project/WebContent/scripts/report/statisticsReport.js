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
			dataUrl: "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
		})
	}),
	listeners: {
		beforeclick: function(node, e) {
			Ext.getCmp("query.departmentCode").setValue(node.text.split("/")[0]);
		}
	}
});

var validDepartmentCodeEmpty = function(departmentCode) {
	return Ext.isEmpty(departmentCode);
};

var btnSearch = new Ext.Button({
	text: '查询',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCod = Ext.getCmp('query.departmentCode').getValue();
		if (validDepartmentCodeEmpty(departmentCod)) {
			Ext.Msg.alert('提示', '网点代码不能为空！');
			return;
		}
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
		var startTime = Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Y/m/d');
		var endTime = Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Y/m/d');
		if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
			Ext.Msg.alert('提示', '开始时间不能大于结束时间!');
			return;
		}
		store.setBaseParam("departmentCode", departmentCod);
		store.setBaseParam("startTime", startTime);
		store.setBaseParam("endTime", endTime);
		store.load({
			params: {
				start: 0,
				limit: 20
			}
		});
	}
});

var btnExport = new Ext.Button({
	text: '导出',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCod = Ext.getCmp('query.departmentCode').getValue();
		if (validDepartmentCodeEmpty(departmentCod)) {
			Ext.Msg.alert('提示', '网点代码不能为空！');
			return;
		}
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
		var startTime = Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Y/m/d');
		var endTime = Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Y/m/d');
		if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
			Ext.Msg.alert('提示', '开始时间不能大于结束时间!');
			return;
		}
		Ext.MessageBox.confirm('请确认', '导出数据过多时可能可能需要较长时间，是否导出', function(button) {
			if (button == 'yes') {
				var exportWaitTitle = new Ext.LoadMask(centerPanel.getEl(), {
					msg: '正在导出...'
				});
				exportWaitTitle.show();

				Ext.Ajax.request({
					method: 'POST',
					url: '../report/export_statisticsReport.action',
					timeout: 60000,
					params: {
						departmentCode: departmentCod,
						startTime: startTime,
						endTime: endTime
					},
					success: function(response) {
						exportWaitTitle.hide();
						var result = Ext.decode(response.responseText);

						if (result.success == true) {
							Ext.Msg.alert('提示', '导出成功!');
							window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(result.fileName));
							return;
						}

						Ext.Msg.alert('提示', result.error);
					}
				});
			}
		});
	}
});

var tbar = [];

addBar('<app:isPermission code = "/report/query_statisticsReport.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/report/export_statisticsReport.action">a</app:isPermission>', btnExport);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		tbar.push('-');
		tbar.push(button);
	}
}

var conditionPanel = new Ext.Panel({
	region: 'north',
	height: 120,
	frame: true,
	tbar: tbar,
	items: [{
		xtype: 'fieldset',
		title: '查询条件',
		style: 'margin-top:5px;',
		columnWidth: 1,
		frame: true,
		layout: 'column',
		items: [{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.departmentCode',
				editable: false,
				triggerAction: "all",
				fieldLabel: '中转场代码<font color=red>*</font>',
				anchor: '90%'
			}]
		},{
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
				fieldLabel: '结束时间<font color=red>*</font>'
			}]
		}]
	}]
});

var record = Ext.data.Record.create([{
	name: 'DAY_OF_MONTH',
	mapping: 'DAY_OF_MONTH',
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
	name: 'TOTAL_EMP_NUM',
	mapping: 'TOTAL_EMP_NUM',
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
	name: 'OUT_EMP_NUM',
	mapping: 'OUT_EMP_NUM',
	type: 'int'
},{
	name: 'CLASS_NUM',
	mapping: 'CLASS_NUM',
	type: 'int'
},{
	name: 'GROUP_NUM',
	mapping: 'GROUP_NUM',
	type: 'int'
},{
	name: 'TOTAL_ATTENDANCE_NUM',
	mapping: 'TOTAL_ATTENDANCE_NUM',
	type: 'int'
},{
	name: 'FULLTIME_ATTENDANCE_NUM',
	mapping: 'FULLTIME_ATTENDANCE_NUM',
	type: 'int'
},{
	name: 'NOT_FULLTIME_ATTENDANCE_NUM',
	mapping: 'NOT_FULLTIME_ATTENDANCE_NUM',
	type: 'int'
},{
	name: 'OUT_ATTENDANCE_NUM',
	mapping: 'OUT_ATTENDANCE_NUM',
	type: 'int'
},{
	name: 'TOTAL_REST_NUM',
	mapping: 'TOTAL_REST_NUM',
	type: 'int'
},{
	name: 'FULLTIME_REST_NUM',
	mapping: 'FULLTIME_REST_NUM',
	type: 'int'
},{
	name: 'NOT_FULLTIME_REST_NUM',
	mapping: 'NOT_FULLTIME_REST_NUM',
	type: 'string'
},{
	name: 'OUT_REST_NUM',
	mapping: 'OUT_REST_NUM',
	type: 'string'
},{
	name: 'TOTAL_WORKTIME',
	mapping: 'TOTAL_WORKTIME',
	type: 'string'		
},{
	name: 'TOTAL_ATTENDANCE_PERCENT',
	mapping: 'TOTAL_ATTENDANCE_PERCENT',
	type: 'string'
},{
	name: 'FULLTIME_EMP_PERCENT',
	mapping: 'FULLTIME_EMP_PERCENT',
	type: 'string'
},{
	name: 'NOT_FULLTIME_EMP_PERCENT',
	mapping: 'NOT_FULLTIME_EMP_PERCENT',
	type: 'string'
},{
	name: 'OUT_EMP_PERCENT',
	mapping: 'OUT_EMP_PERCENT',
	type: 'string'
},{
	name: 'TOTAL_SCHEDULING_NUM',
	mapping: 'TOTAL_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'FULLTIME_SCHEDULING_NUM',
	mapping: 'FULLTIME_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'NOT_FULLTIME_SCHEDULING_NUM',
	mapping: 'NOT_FULLTIME_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'OUT_SCHEDULING_NUM',
	mapping: 'OUT_SCHEDULING_NUM',
	type: 'string'
},{
	name: 'SCHEDULING_RATIO',
	mapping: 'SCHEDULING_RATIO',
	type: 'string'
}
/*,{
	name: 'COM_FULL_ATTENDANCE',
	mapping: 'COM_FULL_ATTENDANCE',
	type: 'string'		
},{
	name: 'COM_NOT_FULL_ATTENDANCE',
	mapping: 'COM_NOT_FULL_ATTENDANCE',
	type: 'string'		
},{
	name: 'COM_OUT_ATTENDANCE',
	mapping: 'COM_OUT_ATTENDANCE',
	type: 'string'		
}*/
]);

var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../report/query_statisticsReport.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
	}, record)
});

var pageButton = new Ext.PagingToolbar({
	store: store,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});

var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '日期',
		dataIndex: 'DAY_OF_MONTH',
		renderer: function(value) {
			return value.substring(0, 10);
		}
	},{
		header: '地区代码',
		dataIndex: 'AREA_CODE'
	},{
		header: '网点代码',
		dataIndex: 'DEPT_CODE'
	},{
		header: '在职总人数',
		dataIndex: 'TOTAL_EMP_NUM'
	},{
		header: '全日制在职',
		dataIndex: 'FULLTIME_EMP_NUM'
	},{
		header: '非全日制在职',
		dataIndex: 'NOT_FULLTIME_EMP_NUM'
	},{
		header: '外包在职',
		dataIndex: 'OUT_EMP_NUM'
	},{
		header: '总排班数',
		align: 'left',
		dataIndex: 'TOTAL_SCHEDULING_NUM'
	},{
		header: '全日制排班数',
		align: 'left',
		dataIndex: 'FULLTIME_SCHEDULING_NUM'
	},{
		header: '非全日制排班数',
		align: 'left',
		dataIndex: 'NOT_FULLTIME_SCHEDULING_NUM'
	},{
		header: '外包排班数',
		align: 'left',
		dataIndex: 'OUT_SCHEDULING_NUM'
	},{
		header: '排休总数',
		align: 'left',
		dataIndex: 'TOTAL_REST_NUM'
	},{
		header: '全日制排休',
		align: 'left',
		dataIndex: 'FULLTIME_REST_NUM'
	},{
		header: '非全日制排休',
		align: 'left',
		dataIndex: 'NOT_FULLTIME_REST_NUM'
	},{
		header: '外包排休',
		align: 'left',
		dataIndex: 'OUT_REST_NUM'
	},{
		header: '出勤总数',
		align: 'left',
		dataIndex: 'TOTAL_ATTENDANCE_NUM'
	},{
		header: '全日制出勤',
		align: 'left',
		dataIndex: 'FULLTIME_ATTENDANCE_NUM'
	},{
		header: '非全日制出勤',
		align: 'left',
		dataIndex: 'NOT_FULLTIME_ATTENDANCE_NUM'
	},{
		header: '外包出勤',
		align: 'left',
		dataIndex: 'OUT_ATTENDANCE_NUM'
	},{
		header: '考勤时长',
		align: 'left',
		dataIndex: 'TOTAL_WORKTIME'
	},{
		header: '班次数',
		align: 'left',
		dataIndex: 'CLASS_NUM'
	},{
		header: '小组数',
		align: 'left',
		dataIndex: 'GROUP_NUM'
	},{
		header: '出勤总占比',
		align: 'left',
		dataIndex: 'TOTAL_ATTENDANCE_PERCENT'
	},{
		header: '全日制出勤占比',
		align: 'left',
		dataIndex: 'FULLTIME_EMP_PERCENT'
	},{
		header: '非全日制出勤占比',
		align: 'left',
		dataIndex: 'NOT_FULLTIME_EMP_PERCENT'
	},{
		header: '外包出勤占比',
		align: 'left',
		dataIndex: 'OUT_EMP_PERCENT'
	},{
		header: '排班占比',
		align: 'left',
		dataIndex: 'SCHEDULING_RATIO'
	}
	/*,{
		header: '全日制考勤匹配度',
		align: 'left',
		dataIndex: 'COM_FULL_ATTENDANCE'
	},{
		header: '非全日制考勤匹配度',
		align: 'left',
		dataIndex: 'COM_NOT_FULL_ATTENDANCE'
	},{
		header: '外包考勤匹配度',
		align: 'left',
		dataIndex: 'COM_OUT_ATTENDANCE'
	}*/]
});

var resultGrid = new Ext.grid.GridPanel({
	cm: cm,
	sm: sm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pageButton
});

var centerPanel = new Ext.Panel({
	region: 'center',
	items: [conditionPanel,resultGrid],
	listeners: {
		resize: function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			resultGrid.setWidth(adjWidth - 5);
			resultGrid.setHeight(adjHeight - 165);
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