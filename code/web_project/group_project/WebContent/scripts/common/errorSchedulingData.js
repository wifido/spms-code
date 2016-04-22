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
	if (Ext.getCmp('START_TIME').getValue() == "") {
		Ext.getCmp('START_TIME').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择开始日期！");
		return;
	}
	if (Ext.getCmp('END_TIME').getValue() == "") {
		Ext.getCmp('END_TIME').focus(false, 100);
		Ext.Msg.alert("提示", "请先选择结束日期！");
		return;
	}
	var startTime = Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Ymd');
	var endTime = Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Ymd');
	if (startTime.replace(/\//g, '') > endTime.replace(/\//g, '')) {
		Ext.Msg.alert('提示', '开始日期不能大于结束日期!');
		return;
	}
	
	store.setBaseParam("dept_code", getDepartmentCodes());
	store.setBaseParam("emp_code", Ext.getCmp('query.empCode').getValue().trim());
	store.setBaseParam("emp_name", Ext.getCmp('query.empName').getValue().trim());
	store.setBaseParam("start_time", startTime);
	store.setBaseParam("end_time", endTime);
	store.setBaseParam("error_time", Ext.util.Format.date(Ext.getCmp('ERROR_TIME').getValue(), 'Y-m-d'));
	store.setBaseParam("emp_post_type",Ext.getCmp('query.dataSourcePostType').getValue());
	store.setBaseParam("error_info","%"+Ext.getCmp('query.errorInfo').getValue()+"%");
	store.load({
		params: {
			start: 0,
			limit: 20
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
                    url: '../common/export.action',
                    timeout: 60000,
                    params: {
                    	dept_code: getDepartmentCodes(),
                    	emp_code: Ext.getCmp('query.empCode').getValue().trim(),
                    	emp_name: Ext.getCmp('query.empName').getValue().trim(),
                    	start_time: Ext.util.Format.date(Ext.getCmp('START_TIME').getValue(), 'Ymd'),
                    	end_time : Ext.util.Format.date(Ext.getCmp('END_TIME').getValue(), 'Ymd'),
                    	error_time : Ext.util.Format.date(Ext.getCmp('ERROR_TIME').getValue(), 'Y-m-d'),
                    	emp_post_type : Ext.getCmp('query.dataSourcePostType').getValue(),
                    	error_info : Ext.getCmp('query.errorInfo').getValue()
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

function getDepartmentCodes() {
	return Ext.getCmp("query.branchCode").getValue().split("/")[0];
}

var tbar = [];
addBar('<app:isPermission code="/common/queryErrorSchedulingData.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code="/common/exportErrorSchedulingData.action">a</app:isPermission>', btnExport);
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
	height: 235,
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
				fieldLabel: '开始日期'
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
				fieldLabel: '结束日期'
			}]
		},{
			columnWidth: .3,
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
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'datefield',
				id: 'ERROR_TIME',
				name: 'ERROR_TIME',
				anchor: '90%',
				format: 'Y/m/d',
				fieldLabel: '错误返回日期'
			}]
		},{
			columnWidth: .3,
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
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.empName',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '姓名',
				anchor: '90%'
			}]
		},{
			columnWidth: .3,
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
		},{
			columnWidth: .3,
			labelWidth: 120,
			labelAlign: 'right',
			layout: 'form',
			items: [{
				xtype: 'textfield',
				id: 'query.errorInfo',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '错误主题',
				anchor: '90%'
			}]
		},{
			layout: 'form',
			labelAlign: 'right',
			columnWidth: 1,
			labelWidth: 120,
			items: [{
				html: '<font color=red>注意事项：</br>' 
					+'1.查询条件中的网点为员工的当前最新的所属网点！ </br>'
					+'2.常见的错误主题有：人员号不能被锁住 、排班系统标识不一致、工作班次信息未维护、请将替换定义得更详细、'
					+'存在重复的数据、因冲突不能插入、请指定允许数据范围内的时间，如要按错误主题查询，请复制上述类型查询。</br>'
					+'3.常见错误主题解决方案：人员号不能被锁住 、排班系统标识不一致、工作班次信息未维护(这三种是需要SAP系统维护员工信息后系统重新推送解决)，</br>'
					+'请将替换定义得更详细【开始时间和结束时间为：000000】、因冲突不能插入【时间有交叉】、请指定允许数据范围内的时间【时间格式不正确】(修改排班，重新推送)，</br>'
					+'存在重复的数据【有相同记录】(删除重复，重新推送)。</font>'
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
		header: '当前所属网点代码',
		sortable: true,
		dataIndex: 'DEPT_CODE',
		align: "center",
		width:110
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'EMP_CODE',
		align: "center",
		width:120
	},{
		header: '员工姓名',
		sortable: true,
		dataIndex: 'EMP_NAME',
		align: "center",
		width:110
	},{
		header: '排班日期',
		sortable: true,
		dataIndex: 'BEGIN_DATE',
		align: "center",
		width:110
	},{
		header: '开始时间',
		sortable: true,
		dataIndex: 'BEGIN_TM',
		align: "center",
		width:120
	},{
		header: '结束时间',
		sortable: true,
		dataIndex: 'END_TM',
		align: "center",
		width:120
	},{
		header: 'OFF为休息',
		sortable: true,
		dataIndex: 'OFF_DUTY_FLAG',
		align: "center",
		width:110
	},{
		header: 'X表示前一天',
		sortable: true,
		dataIndex: 'TMR_DAY_FLAG',
		align: "center",
		width:110
	},{
		header: '错误主题',
		sortable: true,
		dataIndex: 'ERROR_INFO',
		align: "center",
		width:180
	},{
		header: '错误返回时间',
		sortable: true,
		dataIndex: 'LASTUPDATE',
		align: "center",
		width:130
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'ID',
	mapping: 'ID',
	type: 'string'
},{
	name: 'DEPT_CODE',
	mapping: 'DEPT_CODE',
	type: 'String'
},{
	name: 'EMP_CODE',
	mapping: 'EMP_CODE',
	type: 'string'
},{
	name: 'EMP_NAME',
	mapping: 'EMP_NAME',
	type: 'string'
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
	name: 'ERROR_INFO',
	mapping: 'ERROR_INFO',
	type: 'string'
},{
	name: 'LASTUPDATE',
	mapping: 'LASTUPDATE',
	type: 'string'
},{
	name: 'TMR_DAY_FLAG',
	mapping: 'TMR_DAY_FLAG',
	type: 'string'
},{
	name: 'OFF_DUTY_FLAG',
	mapping: 'OFF_DUTY_FLAG',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../common/queryErrorSchedulingData.action'
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