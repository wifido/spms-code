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
	var startTime = Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Ymd');
	var endTime = Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Ymd');
	if (!Ext.isEmpty(startTime) && !Ext.isEmpty(endTime)) {
		if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
			Ext.Msg.alert('提示', '开始日期不能大于结束日期!');
			return;
		}
	}
	store.setBaseParam("dept_code", getDepartmentCodes());
	store.setBaseParam("emp_code", Ext.getCmp('query.empCode').getValue().trim());
	store.setBaseParam("start_time", startTime);
	store.setBaseParam("end_time", endTime);
	store.setBaseParam("emp_post_type",Ext.getCmp('query.dataSourcePostType').getValue());
	store.load({
		params: {
			start: 0,
			limit: 40
		}
	});
};

var btnExport = new Ext.Button({
    text: '导出',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function() {
        Ext.MessageBox.confirm('请确认', '导出数据过多时可能可能需要较长时间，是否导出', function(button) {
            if (button == 'yes') {
                var exportWaitTitle = new Ext.LoadMask(centerPanel.getEl(), {
                    msg: '正在导出...'
                });
                exportWaitTitle.show();
                Ext.Ajax.request({
                    method: 'POST',
                    url: '../common/exportSyncSchedulingData.action',
                    timeout: 60000,
                    params: {
                    	emp_code: Ext.getCmp('query.empCode').getValue().trim(),
                    	start_time: Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Ymd'),
                    	end_time : Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Ymd'),
                    	dept_code : getDepartmentCodes(),
                    	emp_post_type : Ext.getCmp('query.dataSourcePostType').getValue()
                    },success: function(response) {
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

var btnHandlePush = new Ext.Button({
	text: "手工推送排班数据",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		handlePush();
	}
});

function handlePush() {

	Ext.Msg.confirm('提示', '您确定今天没有推送排班数据或者推送排班数据失败？', function(btn) {
		if (btn == 'yes') {
			Ext.Msg.confirm('提示', '您确定要手工推送排班数据吗？', function(btn) {
				if (btn == 'yes') {
					Ext.Ajax.request({
								url : '../common/handlePush.action',
								success : function(response, opts) {
									var result = Ext.util.JSON
											.decode(response.responseText);
									if (result.msg != null && result.msg != '') {
										Ext.Msg.alert('提示', result.msg);
									} else {
										Ext.Msg.alert('提示', '操作成功');
									}
								},
								failure : ajaxRequestFailure
							});
				}
			});
		}
	});
}

function ajaxRequestFailure(response) {
	if (response.status == 0) {
	    Ext.Msg.alert('提示', '无法连接到服务器，请检查网络是否正常');
	} else if (response.status == -1) {
	    Ext.Msg.alert('提示', '服务器处理超时，请稍后再试');
	} else {
		Ext.Msg.alert('提示', '系统出现异常,请与管理员联系');
	}
};

function getDepartmentCodes() {
	return Ext.getCmp("query.branchCode").getValue().split("/")[0];
}
var tbar = [];
addBar('<app:isPermission code="/common/querySyncSchedulingData.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code="/common/exportSyncSchedulingData.action">a</app:isPermission>', btnExport);
addBar('<app:isPermission code="/common/handlePush.action">a</app:isPermission>', btnHandlePush);
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
				id: 'query.branchCode',
				fieldLabel: '网点代码',
				anchor: '90%',
				onTriggerClick: function() {
					var _window = new Ext.sf_dept_window({
						branchDepartmentEditable: true,
						isDispatch: true,
						callBackInput: "query.branchCode"
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
				xtype: 'textfield',
				id: 'query.empCode',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '工号',
				anchor: '90%'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'START_TIME',
				name: 'START_TIME',
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
				id: 'END_TIME',
				name: 'END_TIME',
				anchor: '90%',
				format: 'Y/m/d',
				fieldLabel: '结束日期'
			}]
		},{
			columnWidth: .4,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'combo',
				id: 'query.dataSourcePostType',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				forceSelection: true,
				emptyText: '请选择...',
				fieldLabel: '岗位',
				store: [['1','运作'],['2','一线'],['3','仓管'],['5','司机']],
				anchor: '90%'
			}]
		}]
	}]
});

var record_start = 0;

var sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});
// 列头构建
var cm = new Ext.grid.ColumnModel({
	columns: [sm,{
		header: '员工工号',
		sortable: true,
		dataIndex: 'EMP_CODE',
		align: "center",
		width:90
	},{
		header: '排班日期',
		sortable: true,
		dataIndex: 'BEGIN_DATE',
		align: "center",
		width:100
	},{
		header: '开始时间',
		sortable: true,
		dataIndex: 'BEGIN_TM',
		align: "center",
		width:80
	},{
		header: '结束时间',
		sortable: true,
		dataIndex: 'END_TM',
		align: "center",
		width:80
	},{
		header: 'X表示前一天排班',
		sortable: true,
		dataIndex: 'TMR_DAY_FLAG',
		align: "center",
		width:90
	},{
		header: 'OFF标识休息',
		sortable: true,
		dataIndex: 'OFF_DUTY_FLAG',
		align: "center",
		width:80
	},{
		header: '同步状态',
		sortable: true,
		dataIndex: 'STATE_FLG',
		align: "center",
		width:80,
		renderer: function(value) {
			if (Ext.isEmpty(value) || value == "0") {
				return "未推送";
			}
			if ( value == "1") {
				return "正在推送";
			}
			if ( value == "2") {
				return "推送成功";
			}
			if ( value == "3") {
				return "推送失败";
			}
		}
	},{
		header: '岗位',
		sortable: true,
		dataIndex: 'EMP_POST_TYPE',
		align: "center",
		width:80,
		renderer: function(value) {
			if ( value == "1"){
				return "运作";
			}
			if ( value == "2") {
				return "一线";
			}
			if ( value == "3") {
				return "仓管";
			} 
			if ( value == "5") {
				return "司机";
			} 
			
		}
	},{
		header: '插入接口表时间',
		sortable: true,
		dataIndex: 'CREATE_TM',
		align: "center",
		width:170
	},{
		header: '失败原因',
		sortable: true,
		dataIndex: 'ERROR_INFO',
		align: "center",
		width:140
	},{
		header: '同步时间',
		sortable: true,
		dataIndex: 'SYNC_TM',
		align: "center",
		width:170
	},{
		header: '转岗时间',
		sortable: true,
		dataIndex: 'TRANSFER_DATE',
		align: "center",
		width:170
	},{
		header: '入职时间',
		sortable: true,
		dataIndex: 'SF_DATE',
		align: "center",
		width:170
	},{
		header: '离职时间',
		sortable: true,
		dataIndex: 'DIMISSION_DT',
		align: "center",
		width:170
	},{
		header: '转网时间',
		sortable: true,
		dataIndex: 'DATE_FROM',
		align: "center",
		width:170
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'ID',
	mapping: 'ID',
	type: 'string'
},{
	name: 'EMP_CODE',
	mapping: 'EMP_CODE',
	type: 'String'
},{
	name: 'BEGIN_DATE',
	mapping: 'BEGIN_DATE',
	type: 'string'
},{
	name: 'BEGIN_TM',
	mapping: 'BEGIN_TM',
	type: 'string'
},{
	name: 'END_TM',
	mapping: 'END_TM',
	type: 'string'
},{
	name: 'TMR_DAY_FLAG',
	mapping: 'TMR_DAY_FLAG',
	type: 'string'
},{
	name: 'OFF_DUTY_FLAG',
	mapping: 'OFF_DUTY_FLAG',
	type: 'string'
},{
	name: 'STATE_FLG',
	mapping: 'STATE_FLG',
	type: 'string'
},{
	name: 'CREATE_TM',
	mapping: 'CREATE_TM',
	type: 'string'
},{
	name: 'ERROR_INFO',
	mapping: 'ERROR_INFO',
	type: 'string'
},{
	name: 'SYNC_TM',
	mapping: 'SYNC_TM',
	type: 'string'
},{
	name: 'EMP_POST_TYPE',
	mapping: 'EMP_POST_TYPE',
	type: 'string'
},{
	name: 'SF_DATE',
	mapping: 'SF_DATE',
	type: 'string'
},{
	name: 'DIMISSION_DT',
	mapping: 'DIMISSION_DT',
	type: 'string'
},{
	name: 'TRANSFER_DATE',
	mapping: 'TRANSFER_DATE',
	type: 'string'
},{
	name: 'DATE_FROM',
	mapping: 'DATE_FROM',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../common/querySyncSchedulingData.action'
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