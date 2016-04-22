//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var CONDITION_EMPLOYEE_NAME = 'EMP_NAME';
var CONDITION_EMPLOYEE_TYPE = 'WORK_TYPE';
var CONDITION_POST_NAME = 'EMP_DUTY_NAME';

var LABEL_EMPLOYEE_NAME = '姓名';
var LABEL_EMPLOYEE_TYPE = '人员类型';
var LABEL_POST_NAME = '岗位名称';

var query_departmentCode = '';

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

var btnDeleteScheduling = new Ext.Button({
	text: "删除",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
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
					deleteScheduling();
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});

var tbar = [];

addBar('<app:isPermission code = "/warehouse/query_warehouseScheduling.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code = "/warehouse/add_warehouseScheduling.action">a</app:isPermission>', btnAddScheduling);
addBar('<app:isPermission code = "/warehouse/update_warehouseScheduling.action">a</app:isPermission>', btnUpdateScheduling);
addBar('<app:isPermission code = "/warehouse/delete_warehouseScheduling.action">a</app:isPermission>', btnDeleteScheduling);
addBar('<app:isPermission code = "/warehouse/import_warehouseScheduling.action">a</app:isPermission>', btnImport);
addBar('<app:isPermission code = "/warehouse/export_warehouseScheduling.action">a</app:isPermission>', btnExportScheduling);
addBar('<app:isPermission code = "/warehouse/export_warehouseScheduling.action">a</app:isPermission>', btnExportNoSchedulingEmployee);

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
				xtype: "textfield",
				fieldLabel: '<font color=red>网点代码*</font>',
				id: "query.branchCode",
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						callBackInput: "query.branchCode"
					});
					_window.show(this);
				}
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'query.monthId',
				fieldLabel: '<font color=red>*月份</font>',
				anchor: '90%',
				format: 'Y-m',
				value: new Date,
				plugins: 'monthPickerPlugin'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'field',
				id: 'query.empCode',
				fieldLabel: '员工工号',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'field',
				id: CONDITION_EMPLOYEE_NAME,
				fieldLabel: LABEL_EMPLOYEE_NAME,
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'combo',
				id: CONDITION_EMPLOYEE_TYPE,
				anchor: '90%',
				fieldLabel: LABEL_EMPLOYEE_TYPE,
				mode: 'local',
				emptyText: '请选择...',
				forceSelection: true,
				triggerAction: 'all',
				store: workType
			}]
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'field',
				id: CONDITION_POST_NAME,
				fieldLabel: LABEL_POST_NAME,
				anchor: '90%'
			}]
		}]
	}]
});
// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});

var record_start = 0;
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: 'ID',
		hidden: true,
		sortable: true,
		dataIndex: 'ID',
		align: "center"
	},{
		header: '排班月份',
		sortable: true,
		dataIndex: 'monthId',
		align: "center;vertical-align:middle"
	},{
		header: '地区代码',
		sortable: true,
		dataIndex: 'areaCode',
		align: "center;vertical-align:middle"
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'departmentCode',
		align: "center;vertical-align:middle"
	},{
		header: '工号',
		sortable: true,
		dataIndex: 'employeeCode',
		align: "center;vertical-align:middle"
	},{
		header: '姓名',
		sortable: true,
		dataIndex: 'employeeName',
		align: "center;vertical-align:middle"
	},{
		header: '人员类型',
		sortable: true,
		dataIndex: 'workType',
		align: "center;vertical-align:middle"
	},{
		header: '岗位',
		sortable: true,
		dataIndex: 'empDutyName',
		align: "center;vertical-align:middle"
	},{
		header: '1',
		sortable: true,
		dataIndex: 'firstDay',
		align: "center;vertical-align:middle"
	},{
		header: '2',
		sortable: true,
		dataIndex: 'secondDay',
		align: "center;vertical-align:middle"
	},{
		header: '3',
		sortable: true,
		dataIndex: 'thirdDay',
		align: "center;vertical-align:middle"
	},{
		header: '4',
		sortable: true,
		dataIndex: 'fourthDay',
		align: "center;vertical-align:middle"
	},{
		header: '5',
		sortable: true,
		dataIndex: 'fifthDay',
		align: "center;vertical-align:middle"
	},{
		header: '6',
		sortable: true,
		dataIndex: 'sixthDay',
		align: "center;vertical-align:middle"
	},{
		header: '7',
		sortable: true,
		dataIndex: 'seventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '8',
		sortable: true,
		dataIndex: 'eighthDay',
		align: "center;vertical-align:middle"
	},{
		header: '9',
		sortable: true,
		dataIndex: 'ninthDay',
		align: "center;vertical-align:middle"
	},{
		header: '10',
		sortable: true,
		dataIndex: 'tenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '11',
		sortable: true,
		dataIndex: 'eleventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '12',
		sortable: true,
		dataIndex: 'twelfthDay',
		align: "center;vertical-align:middle"
	},{
		header: '13',
		sortable: true,
		dataIndex: 'thirteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '14',
		sortable: true,
		dataIndex: 'fourteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '15',
		sortable: true,
		dataIndex: 'fifteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '16',
		sortable: true,
		dataIndex: 'sixteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '17',
		sortable: true,
		dataIndex: 'seventeenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '18',
		sortable: true,
		dataIndex: 'eighteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '19',
		sortable: true,
		dataIndex: 'nineteenthDay',
		align: "center;vertical-align:middle"
	},{
		header: '20',
		sortable: true,
		dataIndex: 'twentiethDay',
		align: "center;vertical-align:middle"
	},{
		header: '21',
		sortable: true,
		dataIndex: 'twentyFirstDay',
		align: "center;vertical-align:middle"
	},{
		header: '22',
		sortable: true,
		dataIndex: 'twentySecondDay',
		align: "center;vertical-align:middle"
	},{
		header: '23',
		sortable: true,
		dataIndex: 'twentyThirdDay',
		align: "center;vertical-align:middle"
	},{
		header: '24',
		sortable: true,
		dataIndex: 'twentyFourthDay',
		align: "center;vertical-align:middle"
	},{
		header: '25',
		sortable: true,
		dataIndex: 'twentyFifthDay',
		align: "center;vertical-align:middle"
	},{
		header: '26',
		sortable: true,
		dataIndex: 'twentySixthDay',
		align: "center;vertical-align:middle"
	},{
		header: '27',
		sortable: true,
		dataIndex: 'twentySeventhDay',
		align: "center;vertical-align:middle"
	},{
		header: '28',
		sortable: true,
		dataIndex: 'twentyEighthDay',
		align: "center;vertical-align:middle"
	},{
		header: '29',
		sortable: true,
		dataIndex: 'twentyNinthDay',
		align: "center;vertical-align:middle"
	},{
		header: '30',
		sortable: true,
		dataIndex: 'thirtiethDay',
		align: "center;vertical-align:middle"
	},{
		header: '31',
		sortable: true,
		dataIndex: 'thirtyFirstDay',
		align: "center;vertical-align:middle"
	},{
		header: '修改人',
		sortable: true,
		dataIndex: 'modifiedEmployeeCode',
		align: "center;vertical-align:middle"
	},{
		header: '修改时间',
		sortable: true,
		dataIndex: 'modifiedTime',
		width: 170,
		align: "center;vertical-align:middle"
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'Id',
	mapping: 'Id',
	type: 'string'
},{
	name: 'monthId',
	mapping: 'monthId',
	type: 'string'
},{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'departmentCode',
	mapping: 'departmentCode',
	type: 'string'
},{
	name: 'employeeCode',
	mapping: 'employeeCode',
	type: 'string'
},{
	name: 'employeeName',
	mapping: 'employeeName',
	type: 'string'
},{
	name: 'workType',
	mapping: 'workType',
	type: 'string'
},{
	name: 'empDutyName',
	mapping: 'empDutyName',
	type: 'string'
},{
	name: 'firstDay',
	mapping: 'firstDay',
	type: 'string'
},{
	name: 'secondDay',
	mapping: 'secondDay',
	type: 'string'
},{
	name: 'thirdDay',
	mapping: 'thirdDay',
	type: 'string'
},{
	name: 'fourthDay',
	mapping: 'fourthDay',
	type: 'string'
},{
	name: 'fifthDay',
	mapping: 'fifthDay',
	type: 'string'
},{
	name: 'sixthDay',
	mapping: 'sixthDay',
	type: 'string'
},{
	name: 'seventhDay',
	mapping: 'seventhDay',
	type: 'string'
},{
	name: 'eighthDay',
	mapping: 'eighthDay',
	type: 'string'
},{
	name: 'ninthDay',
	mapping: 'ninthDay',
	type: 'string'
},{
	name: 'tenthDay',
	mapping: 'tenthDay',
	type: 'string'
},{
	name: 'eleventhDay',
	mapping: 'eleventhDay',
	type: 'string'
},{
	name: 'twelfthDay',
	mapping: 'twelfthDay',
	type: 'string'
},{
	name: 'thirteenthDay',
	mapping: 'thirteenthDay',
	type: 'string'
},{
	name: 'fourteenthDay',
	mapping: 'fourteenthDay',
	type: 'string'
},{
	name: 'fifteenthDay',
	mapping: 'fifteenthDay',
	type: 'string'
},{
	name: 'sixteenthDay',
	mapping: 'sixteenthDay',
	type: 'string'
},{
	name: 'seventeenthDay',
	mapping: 'seventeenthDay',
	type: 'string'
},{
	name: 'eighteenthDay',
	mapping: 'eighteenthDay',
	type: 'string'
},{
	name: 'nineteenthDay',
	mapping: 'nineteenthDay',
	type: 'string'
},{
	name: 'twentiethDay',
	mapping: 'twentiethDay',
	type: 'string'
},{
	name: 'twentyFirstDay',
	mapping: 'twentyFirstDay',
	type: 'string'
},{
	name: 'twentySecondDay',
	mapping: 'twentySecondDay',
	type: 'string'
},{
	name: 'twentyThirdDay',
	mapping: 'twentyThirdDay',
	type: 'string'
},{
	name: 'twentyFourthDay',
	mapping: 'twentyFourthDay',
	type: 'string'
},{
	name: 'twentyFifthDay',
	mapping: 'twentyFifthDay',
	type: 'string'
},{
	name: 'twentySixthDay',
	mapping: 'twentySixthDay',
	type: 'string'
},{
	name: 'twentySeventhDay',
	mapping: 'twentySeventhDay',
	type: 'string'
},{
	name: 'twentyEighthDay',
	mapping: 'twentyEighthDay',
	type: 'string'
},{
	name: 'twentyNinthDay',
	mapping: 'twentyNinthDay',
	type: 'string'
},{
	name: 'thirtiethDay',
	mapping: 'thirtiethDay',
	type: 'string'
},{
	name: 'modifiedEmployeeCode',
	mapping: 'modifiedEmployeeCode',
	type: 'string'
},{
	name: 'modifiedTime',
	mapping: 'modifiedTime',
	type: 'string'
},{
	name: 'thirtyFirstDay',
	mapping: 'thirtyFirstDay',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../warehouse/query_warehouseScheduling.action'
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

function parseDepartment(departmentCodes) {
	var codes = [];
	Ext.each(departmentCodes.split(','), function(v, i) {
		codes.push(v.split('/')[0]);
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

	if (Ext.getCmp('query.monthId').getValue() == "") {
		Ext.Msg.alert("提示", "请先选择月份！");
		return;
	}
	query_departmentCode = parseDepartment(departmentCodes);

	store.setBaseParam("in_departmentCode", query_departmentCode);
	store.setBaseParam("MONTH_ID", Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Ym'));
	store.setBaseParam("EMP_CODE", Ext.getCmp('query.empCode').getValue());
	store.setBaseParam(CONDITION_EMPLOYEE_NAME, Ext.getCmp(CONDITION_EMPLOYEE_NAME).getValue());
	store.setBaseParam(CONDITION_EMPLOYEE_TYPE, Ext.getCmp(CONDITION_EMPLOYEE_TYPE).getValue());
	store.setBaseParam(CONDITION_POST_NAME, Ext.getCmp(CONDITION_POST_NAME).getValue());
	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};

var deleteScheduling = function() {
	var records = grid.getSelectionModel().getSelections();
	if (records.length > 1) {
		Ext.Msg.alert("提示", "只能选择一条数据!");
		return false;
	}
	if (records.length < 1) {
		Ext.Msg.alert("提示", "请选择一条数据!");
		return false;
	}
	var record = grid.getSelectionModel().getSelected();
	var employeeCode = record.data['employeeCode'];
	var monthId = record.data['monthId'];
	if (moment(monthId, 'YYYYMM').isBefore(moment().startOf('month'))) {
		Ext.Msg.alert("提示", "只能删除当前月及之后的排班!");
		return false;
	}
	var departmentCode = Ext.getCmp("query.branchCode").getValue().split('/')[0];
	Ext.MessageBox.confirm("提示", "确认要删除选中的排班数据吗?", function(btn) {
		if ('yes' == btn) {
			Ext.Ajax.request({
				url: "delete_warehouseScheduling.action",
				timeout: 30000,
				params: {
					empCode: employeeCode,
					deptCode: departmentCode,
					monthId: monthId
				},
				success: function(response) {
					var result = Ext.util.JSON.decode(response.responseText);
					if (Ext.isEmpty(result.error)) {
						Ext.Msg.alert("提示", "删除成功!");
						querySchedule();
					} else {
						Ext.Msg.alert("提示", "删除失败!" + result.error);
					}
				},
				failure: function(response) {
					var result = Ext.util.JSON.decode(response.responseText);
					Ext.Msg.alert('提示', "删除失败！" + result.error);
				}
			});
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
