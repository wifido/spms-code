//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var validDepartmentCodeEmpty = function(departmentCode) {
	return Ext.isEmpty(departmentCode);
};

var queryButton = new Ext.Button({
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
		store.setBaseParam("departmentCode", departmentCod);
		store.setBaseParam("yearMonth", Ext.util.Format.date(Ext.getCmp("query.yearMonth").getValue(), "Y-m"));

		store.load({
			params: {
				start: 0,
				limit: 20
			}
		});
	}
});

var exportButton = new Ext.Button({
	text: '导出',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp('query.departmentCode').getValue();
		if (validDepartmentCodeEmpty(departmentCode)) {
			Ext.Msg.alert('提示', '网点代码不能为空！');
			return;
		}
		var exportWaitTitle = new Ext.LoadMask(centerPanel.getEl(), {
			msg: '正在导出...'
		});
		exportWaitTitle.show();
		Ext.Ajax.request({
			method: 'POST',
			url: 'export_schedulingAnalysisReport.action',
			timeout: 60000,
			params: {
				start: 0,
				limit: 60000,
				departmentCode: departmentCode,
				yearMonth: Ext.util.Format.date(Ext.getCmp("query.yearMonth").getValue(), "Y-m")
			},
			success: function(response) {
				exportWaitTitle.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (Ext.isEmpty(result.error)) {
					Ext.Msg.alert("提示", "导出成功!");
					var url = result.resultMap.downloadPath;
					window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
				} else {
					Ext.Msg.alert("提示", "导出失败!" + result.error);
				}
			},
			failure: function(response) {
				exportWaitTitle.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				Ext.Msg.alert('提示', "导出失败！" + result.error);
			}
		});

	}
});

var operationButtons = [];
addButton('<app:isPermission code = "/warehouse/query_schedulingAnalysisReport.action">a</app:isPermission>', queryButton);
addButton('<app:isPermission code = "/warehouse/export_schedulingAnalysisReport.action">a</app:isPermission>', exportButton);

function addButton(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		operationButtons.push('-');
		operationButtons.push(button);
	}
}

var conditionPanel = new Ext.Panel({
	region: 'north',
	height: 120,
	frame: true,
	tbar: operationButtons,
	items: [{
		xtype: 'fieldset',
		title: '查询条件',
		style: 'margin-top:5px;',
		columnWidth: 1,
		frame: true,
		layout: 'column',
		items: [{
			layout: 'form',
			columnWidth: .3,
			labelAlign: 'right',
			items: [{
				xtype: 'textfield',
				fieldLabel: '网点ID',
				hidden: true,
				id: 'query.departmentId',
				readOnly: true,
				anchor: '90%'
			},{
				xtype: 'textfield',
				fieldLabel: '网点代码<font color=red>*</font>',
				id: 'query.departmentCode',
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						callBackInput: "query.branchCode"
					});
					_window.show(this);
				}
			}]
		},{
			layout: 'form',
			columnWidth: .3,
			labelAlign: 'right',
			items: [{
				xtype: 'datefield',
				fieldLabel: '月份<font color=red>*</font>',
				id: 'query.yearMonth',
				allowBlank: false,
				plugins: 'monthPickerPlugin',
				editable: false,
				format: 'Y-m',
				value: new Date(),
				anchor: '90%'
			}]
		}]
	}]
});

var record = Ext.data.Record.create([{
	name: 'SCHE_MONTH',
	mapping: 'SCHE_MONTH',
	type: 'string'
},{
	name: 'AREA_CODE',
	mapping: 'AREA_CODE',
	type: 'string'
},{
	name: 'DEPARTMENT_CODE',
	mapping: 'DEPARTMENT_CODE',
	type: 'string'
},{
	name: 'SCHE_TOTAL_EMP_NUM',
	mapping: 'SCHE_TOTAL_EMP_NUM',
	type: 'string'
},{
    name: 'A_SCHEDULING_DAYS_AVG',
    mapping : 'A_SCHEDULING_DAYS_AVG',
    type: 'string'
},{
	name: 'A_SCHEDULING_HOURS_AVG',
	mapping: 'A_SCHEDULING_HOURS_AVG',
	type: 'string'
},{
	name: 'C_SCHEDULING_DAYS_AVG',
	mapping: 'C_SCHEDULING_DAYS_AVG',
	type: 'string'
},{
	name: 'C_SCHEDULING_HOURS_AVG',
	mapping: 'C_SCHEDULING_HOURS_AVG',
	type: 'string'
}]);

var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../warehouse/query_schedulingAnalysisReport.action'
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
		header: '月份',
		dataIndex: 'SCHE_MONTH'
	},{
		header: '区部代码',
		dataIndex: 'AREA_CODE'
	},{
		header: '网点代码',
		dataIndex: 'DEPARTMENT_CODE'
	},{
		header: '上班人数',
		dataIndex: 'SCHE_TOTAL_EMP_NUM'
	},{
		header: '全日制排班平均天数<br>(剔除异动人员后)',
		width: 130,
		dataIndex: 'A_SCHEDULING_DAYS_AVG'
	},{
		header: '全日制排班平均时长',
		width: 130,
		dataIndex: 'A_SCHEDULING_HOURS_AVG'
	},{
        header: '非全日制排班平均天数<br>(剔除异动人员后)',
        width: 130,
        dataIndex: 'C_SCHEDULING_DAYS_AVG'
    },{
        header: '非全日制排班平均时长',
    	width: 130,
        dataIndex: 'C_SCHEDULING_HOURS_AVG'
    }],
	defaults: {
		sortable: true,
		width: 60,
		align: 'center'
	}
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