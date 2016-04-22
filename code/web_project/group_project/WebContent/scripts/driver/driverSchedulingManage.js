// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var todayDt;
Ext.Ajax.request({
	timeout : 3000,
	url : '../operation/getSysdate',
	success : function(response, config) {
		todayDt = Date.parseDate(response.responseText, 'Y-m-d');
	}
});
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
		beforeclick: function(node, e) {
			if (hasNotSelectedDepartment(node)) {
				return;
			}

			Ext.getCmp("query.departmentCode").setValue(node.text.split("/")[0]);
			Ext.getCmp("query.departmentId").setValue(node.id);
			queryDriverScheduling();
		}
	}
});

function hasNotSelectedDepartment(node) {
	return node == null || node.id == 0;
}

function queryDriverScheduling() {
	var departmentCode = Ext.getCmp("query.departmentCode").getValue();
	if (departmentCode == '') {
		Ext.Msg.alert('提示', '请选择网点信息');
		return;
	}

	queryDriverScheduled();
}

function queryDriverScheduled() {
	var yearMonth = Ext.util.Format.date(Ext.getCmp("query.yeaMonth").getValue(), "Y-m");
	var departmentCode = Ext.getCmp("query.departmentCode").getValue();
	var queryType = Ext.getCmp("query.queryType").getValue();
	var weekOfYear = Ext.get("weekOfYear").getValue();
	var confirmStatus = Ext.get("query.confirmStatus").getValue();
	
	centerPanel.items.items[queryType].getStore().setBaseParam('departmentCode', departmentCode);
	centerPanel.items.items[queryType].getStore().setBaseParam("employeeCode", Ext.getCmp("query.employeeCode").getValue());
	//centerPanel.items.items[queryType].getStore().setBaseParam("schedulingType", Ext.getCmp('query.schedulingType').getValue());

	if ('1' == queryType) {
		centerPanel.items.items[queryType].getStore().setBaseParam("yearWeek", weekOfYear);
		if (confirmStatus == '全部') {
			confirmStatus = ""
		} else if (confirmStatus == "未确认") {
			confirmStatus = "0";
		} else {
			confirmStatus = "1";
		}
		centerPanel.items.items[queryType].getStore().setBaseParam("confirmStatus", confirmStatus);
	} else {
		centerPanel.items.items[queryType].getStore().setBaseParam("yearMonth", yearMonth);
	}

	centerPanel.items.items[queryType].getStore().load({
		params: {
			start: 0,
			limit: 20
		}
	});
}

function getSelectedDepartmentNode() {
	return treePanel.getSelectionModel().getSelectedNode();
}

function setTitleByWeek() {
	var weekOfYearAsString = Ext.get("weekOfYear").getValue();
	var year = weekOfYearAsString.split("-")[0];
	var week = weekOfYearAsString.split("-")[1];
	for ( var i = 1; i < 8; i++) {
		var dayOfWeek = moment().year(year).isoWeek(week).isoWeekday(i);
		cm_week.setColumnHeader(i + 10, dayOfWeek.format('MM-DD'));
	}
}

var searchButton = new Ext.Button({
	text: '查询',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		queryDriverScheduling();
	}
});

var btnImportScheduling = new Ext.Button({
	text: "导入",
	pressed: true,
	minWidth: 60,
	handler: function() {
		new Ext.import_window({
			moduleName: 'driver',
			templateFileName: '司机排班导入模板.xls',
			url: 'importScheduling_scheduling.action',
			store: centerPanel.items.items[Ext.getCmp("query.queryType").getValue()].getStore()
		}).show();
	}
});

var exportDriverSchedulingButton = new Ext.Button({
	text: '导出',
	pressed: true,
	cls: 'x-btn-normal',
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp("query.departmentCode").getValue();
		var yearMonth = Ext.util.Format.date(Ext.getCmp("query.yeaMonth").getValue(), "Y-m");

		if (departmentCode == "") {
			Ext.Msg.alert('提示', '请选择网点信息');
			return;
		}

		Ext.MessageBox.confirm('请确认', '导出数据过多时可能可能需要较长时间，是否导出', function(button) {
			if (button == 'yes') {
				var isExport = new Ext.LoadMask(centerPanel.getEl(), {
					msg: "正在导出..."
				});
				isExport.show();

				Ext.Ajax.request({
					method: 'post',
					url: '../driver/exportDriverScheduling_scheduling.action',
					timeout: 200000,
					params: {
						departmentCode: departmentCode,
						yearMonth: yearMonth
					},
					success: function(response) {
						var result = Ext.decode(response.responseText);
						isExport.hide();
						if (result.success == true) {
							Ext.Msg.alert('提示', '导出成功');
							window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(result.resultMap.downloadPath));
							return;
						}
						Ext.Msg.alert('提示', result.error);
					},
					failure: function() {
						Ext.Msg.alert('提示', '导出发生异常,请联系管理员!');
						isExport.hide();
					}
				});

			}
		});
	}
});

var exportNotSchedulingButton = new Ext.Button({
	text: '导出未排班人员',
	cls: 'x-btn-normal',
	minWidth: 60,
	pressed: true,
	title: '根据查询条件导出',
	handler: function() {
		var departmentCode = Ext.getCmp("query.departmentCode").getValue();
		var weekOfYear = Ext.get("weekOfYear").getValue();
		
		var isExport = new Ext.LoadMask(centerPanel.getEl(), {
			msg: "正在导出..."
		});
		isExport.show();
		
		Ext.Ajax.request({
			url: 'exportNotSchedulingEmployee_scheduling.action',
			params: {
				departmentCode: departmentCode,
				year_week: weekOfYear
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				isExport.hide();
				if (result.success == true) {
					Ext.Msg.alert('提示', '导出成功');
					window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(result.resultMap.downloadPath));
					return;
				}
				Ext.Msg.alert('提示', result.error);
			}
		});
	}
});

var deleteButton = new Ext.Button({
	text: '删除',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var queryType = Ext.getCmp("query.queryType").getValue();
		var weekOfYear = null;
		var yearMonth = null;
		var selectedRows;
		if ('1' == queryType) {
			weekOfYear = Ext.get("weekOfYear").getValue();
			var year = parseInt(weekOfYear.split("-")[0]);
			var weeks = parseInt(weekOfYear.split("-")[1]);
			var currentWeek = moment().isoWeek();
			if (year < moment().year()) {
				Ext.Msg.alert('提示', '只能删除当周之后的排班数据！');
				return;
			}
			if (year == moment().year() && weeks <= currentWeek) {
				Ext.Msg.alert('提示', '只能删除当周之后的排班数据！');
				return;
			}
			selectedRows = resultGrid_week.getSelectionModel().getSelections();
			if (selectedRows.length < 1) {
				Ext.Msg.alert('提示', '请选择要删除的排班数据！');
				return;
			}

		} else {
			var selectedMoment = moment(Ext.getCmp("query.yeaMonth").getValue());
			var nextMonth = moment().add(1, 'months').startOf('month');
			if (selectedMoment.isBefore(nextMonth)) {
				Ext.Msg.alert('提示', '只能删除当前月之后的排班数据！');
				return;
			}
			selectedRows = resultGrid.getSelectionModel().getSelections();
			if (selectedRows.length < 1) {
				Ext.Msg.alert('提示', '请选择要删除的排班数据！');
				return;
			}
			yearMonth = selectedMoment.format('YYYY-MM');
		}

		Ext.MessageBox.confirm('警告', '确认要删除选择的排班数据吗?', function(button) {
			if (button != 'yes')
				return false;
			var employeeCodes = [];
			Ext.each(selectedRows, function(v, i) {
				employeeCodes.push(v.data['employeeCode']);
			});
			var myMask = new Ext.LoadMask(centerPanel.getEl(), {
				msg: "正在删除..."
			});
			myMask.show();
			Ext.Ajax.request({
				url: "deleteScheduling_scheduling.action",
				params: {
					employeeCode: employeeCodes.join(","),
					YEAR_WEEK: weekOfYear,
					yearMonth: yearMonth
				},
				success: function(response) {
					myMask.hide();
					result = Ext.util.JSON.decode(response.responseText);
					if (Ext.isEmpty(result.error)) {
						Ext.Msg.alert("提示", "删除成功!");
						addWindow.hide();
						queryDriverScheduling();
						return;
					}
					Ext.Msg.alert("提示", "删除失败!" + result.error);
				},
				failure: function(response) {
					myMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if (Ext.isEmpty(result.error)) {
						Ext.Msg.alert('提示', "删除失败！服务器错误！");
						return;
					}
					Ext.Msg.alert("提示", "删除失败!" + result.error);
				}
			});
		});
	}
});

var weeklyExportButton =  new Ext.Button({
	text: '按周导出',
	cls: 'x-btn-normal',
	pressed: true,
	minWidth: 60,
	handler: function() {
		var departmentCode = Ext.getCmp("query.departmentCode").getValue();
		var weekOfYear = Ext.get("weekOfYear").getValue();
		var confirmStatus = Ext.get("query.confirmStatus").getValue();
		var employeeCode = Ext.getCmp("query.employeeCode").getValue();
		
		if (departmentCode == '') {
			Ext.Msg.alert('提示', '请选择网点信息');
			return;
		}
		
		var isExport = new Ext.LoadMask(centerPanel.getEl(), {
			msg: "正在导出..."
		});
		isExport.show();
		
		if (confirmStatus == '全部') {
			confirmStatus = ""
		} else if (confirmStatus == "未确认") {
			confirmStatus = "0";
		} else {
			confirmStatus = "1";
		}
		
		Ext.Ajax.request({
			url: 'weeklyExport.action',
			params: {
				departmentCode: departmentCode,
				year_week: weekOfYear,
				confirmStatus: confirmStatus,
				employeeCode: employeeCode
			},
			success: function(response) {
				var result = Ext.decode(response.responseText);
				isExport.hide();
				
				if (result.totalSize == 0) {
					Ext.Msg.alert('提示', '没有查找到数据!');
					return;
				}
				
				if (result.success == true) {
					Ext.Msg.alert('提示', '导出成功');
					window.location = '../common/downloadReportZipFile.action?fileName=' + encodeURI(encodeURI(result.downloadPath));
					return;
				}
				Ext.Msg.alert('提示', result.error);
			}
		});
	}
});

var operationButton = [];
addBar('<app:isPermission code = "/driver/queryDriverScheduling.action">a</app:isPermission>', searchButton);
addBar('<app:isPermission code = "/driver/addScheduling_scheduling.action">a</app:isPermission>', btnAddScheduling);
addBar('<app:isPermission code = "/driver/updateDriverScheduling_scheduling.action">a</app:isPermission>', updateDriverSchedulingButton);
addBar('<app:isPermission code = "/driver/deleteScheduling_scheduling.action">a</app:isPermission>', deleteButton);
addBar('<app:isPermission code = "/driver/importScheduling_scheduling.action">a</app:isPermission>', btnImportScheduling);
addBar('<app:isPermission code = "/driver/exportDriverScheduling_scheduling.action">a</app:isPermission>', exportDriverSchedulingButton);
addBar('<app:isPermission code = "/driver/exportNotSchedulingEmployee_scheduling.action">a</app:isPermission>', exportNotSchedulingButton);
addBar('<app:isPermission code = "/driver/weeklyExport.action">a</app:isPermission>', weeklyExportButton);

function addBar(permisson, button) {
	if (!Ext.isEmpty(permisson)) {
		operationButton.push('-');
		operationButton.push(button);
	}
}

//var schedulingType = [['1','排班'],['0','预排班']];
var queryType = [['1','按周'],['2','按月']];
var confirmType = [['0','全部'],['1','未确认'],['2','已确认']];

var conditionPanel = new Ext.Panel(
		{
			region: 'north',
			layout: 'column',
			height: 120,
			frame: true,
			tbar: operationButton,
			items: [{
				xtype: 'fieldset',
				style: 'margin-top:5px;',
				title: '查询条件',
				columnWidth: 1,
				frame: true,
				layout: 'column',
				items: [				        
						{
							columnWidth: .3,
							layout: 'form',
							labelAlign: 'right',
							items: [{
								xtype: 'textfield',
								fieldLabel: '网点代码<font color=red>*</font>',
								id: 'query.departmentCode',
								readOnly: true,
								anchor: '90%'
							},{
								xtype: 'hidden',
								id: 'query.departmentId',
								readOnly: true,
								anchor: '90%'
							},{
								layout: 'form',
								columnWidth: .3,
								labelAlign: 'right',
								items: [{
									xtype: 'textfield',
									fieldLabel: '员工工号',
									id: 'query.employeeCode',
									anchor: '90%'
								}]
							}]
						},{
							layout: 'form',
							columnWidth: .3,
							labelAlign: 'right',
							items: [
									{
										xtype: 'datefield',
										fieldLabel: '月份<font color=red>*</font>',
										id: 'query.yeaMonth',
										format: 'Y-m',
										editable: false,
										value: new Date,
										plugins: 'monthPickerPlugin',
										anchor: '90%'
									},
									{
										xtype: 'label',
										id: 'query.yearWeek',
										fieldLabel: '周数<font color=red>*</font>',
										editable: false,
										html: '<input type="text" class="Wdate" readonly = true onclick="dp();"  id="weekPicker" /><input type="hidden" size="2" id="selectedDay" name="selectedDay" /><input type="hidden" size="2" id="weekOfYear" name="weekOfYear" />',
										anchor: '90%'
									},{
										xtype: 'combo',
										fieldLabel: '查询方式',
										id: 'query.queryType',
										editable: false,
										store: queryType,
										triggerAction: "all",
										mode: "local",
										value: '1',
										allowBlank: false,
										columnWidth: .3,
										anchor: '90%',
										listeners: {
											'select': function(object) {
												if (object.getValue() == '1') {
													resultGrid.hide();
													resultGrid_week.show();
													
													Ext.getCmp('query.yeaMonth').hide();
													Ext.getCmp('query.yearWeek').show();
													Ext.getCmp('query.confirmStatus').show();
													btnAddScheduling.show();
													updateDriverSchedulingButton.show();
													deleteButton.show();
													
													exportDriverSchedulingButton.hide();
													exportNotSchedulingButton.show();
													btnImportScheduling.show();
													weeklyExportButton.show();
													
													resultGrid_week.setHeight(resultGrid.ownerCt.getHeight() - 160);
												} else {
													resultGrid.show();
													resultGrid_week.hide();
													
													Ext.getCmp('query.yeaMonth').show();
													Ext.getCmp('query.yearWeek').hide();
													Ext.getCmp('query.confirmStatus').hide();
													btnAddScheduling.hide();
													updateDriverSchedulingButton.hide();
													deleteButton.hide();
													
													exportDriverSchedulingButton.show();
													exportNotSchedulingButton.hide();
													btnImportScheduling.hide();
													weeklyExportButton.hide();
													
													resultGrid.setHeight(resultGrid_week.ownerCt.getHeight() - 160);
												}
											}
										}
									}]
						},{
							layout: 'form',
							columnWidth: .3,
							labelAlign: 'right',
							items: [{
								xtype : 'combo',
								fieldLabel : '确认状态',
								id : 'query.confirmStatus',
								anchor : '90%',
								mode : 'local',
								typeAhead : true,
								triggerAction : "all",
								value: '0',
								editable: false,
								forceSelection : true,
								store : confirmType
							}]
						}]
			}]
		});

function dp() {
	WdatePicker({
		dateFmt: 'yyyy年-WW周   MM-dd',
		isShowWeek: true,
		isShowToday: false,
		isShowClear: false,
		errDealMode: 8,
		onpicked: function(dp) {
			$dp.$('selectedDay').value = $dp.cal.getP('y', 'yyyy') + "-" + $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd');
			var month = "";
			if( moment($dp.cal.getP('y', 'yyyy') + "-" + $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd')).isoWeek() < 10){
				month = "0" + moment($dp.cal.getP('y', 'yyyy') + "-" + $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd')).isoWeek();
			}
			else {
				month = moment($dp.cal.getP('y', 'yyyy') + "-" + $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd')).isoWeek();
			}
			$dp.$('weekOfYear').value = moment($dp.cal.getP('y', 'yyyy') + "-" + $dp.cal.getP('M', 'MM') + "-" + dp.cal.getP('d', 'dd')).isoWeekYear() + "-" + month;
			var weekOfYear = Ext.get("weekOfYear").getValue();
			setTitleByWeek();
			queryDriverScheduling();
		}
	});
}

var record = Ext.data.Record.create([{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'departmentId',
	mapping: 'departmentId',
	type: 'string'
},{
	name: 'departmentCode',
	mapping: 'departmentCode',
	type: 'string'
},{
	name: 'employeeName',
	mapping: 'employeeName',
	type: 'string'
},{
	name: 'deptDesc',
	mapping: 'deptDesc',
	type: 'string'
},{
	name: 'employeeCode',
	mapping: 'employeeCode',
	type: 'string'
},{
	name: 'yearMonth',
	mapping: 'yearMonth',
	type: 'string'
},{
	name: 'createdTimeStr',
	mapping: 'createdTimeStr',
	type: 'String'
},{
	name: 'creator',
	mapping: 'creator',
	type: 'string'
},{
	name: 'modifiedTimeStr',
	mapping: 'modifiedTimeStr',
	type: 'String'
},{
	name: 'modifier',
	mapping: 'modifier',
	type: 'string'
},{
	name: 'configureCode',
	mapping: 'configureCode',
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
	name: 'thirtyFirstDay',
	mapping: 'thirtyFirstDay',
	type: 'string'
},{
	name: 'totalRestDays',
	mapping: 'totalRestDays',
	type: 'string'
},{
	name: 'workType',
	mapping: 'workType',
	type: 'string'
}]);

var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../driver/queryDriverScheduling.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
	}, record)
});

var pagingButton = new Ext.PagingToolbar({
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
	defaults: {
		width: 90
	},
	columns: [sm,{
		header: '地区代码',
		sortable: true,
		width: 60,
		dataIndex: 'areaCode'
	},{
		header: '网点ID',
		sortable: true,
		hidden: true,
		width: 60,
		dataIndex: 'departmentId'
	},{
		header: '网点代码',
		sortable: true,
		width: 60,
		dataIndex: 'departmentCode',
		renderer: function(value, metaData, record) {
			return value + "/" + record.data["deptDesc"];
		}
	},{
		header: '员工名称',
		sortable: true,
		width: 60,
		dataIndex: 'employeeName'
	},{
		header: '员工工号',
		sortable: true,
		width: 60,
		dataIndex: 'employeeCode'
	},{
		header: '月份',
		sortable: true,
		width: 60,
		dataIndex: 'yearMonth'
	},{
		header: '类型',
		sortable: true,
		width: 60,
		dataIndex: 'workType'
	},{
		header: '1',
		sortable: true,
		dataIndex: 'firstDay',
		align: "center"
	},{
		header: '2',
		sortable: true,
		dataIndex: 'secondDay',
		align: "center"
	},{
		header: '3',
		sortable: true,
		dataIndex: 'thirdDay',
		align: "center"
	},{
		header: '4',
		sortable: true,
		dataIndex: 'fourthDay',
		align: "center"
	},{
		header: '5',
		sortable: true,
		dataIndex: 'fifthDay',
		align: "center"
	},{
		header: '6',
		sortable: true,
		dataIndex: 'sixthDay',
		align: "center"
	},{
		header: '7',
		sortable: true,
		dataIndex: 'seventhDay',
		align: "center"
	},{
		header: '8',
		sortable: true,
		dataIndex: 'eighthDay',
		align: "center"
	},{
		header: '9',
		sortable: true,
		dataIndex: 'ninthDay',
		align: "center"
	},{
		header: '10',
		sortable: true,
		dataIndex: 'tenthDay',
		align: "center"
	},{
		header: '11',
		sortable: true,
		dataIndex: 'eleventhDay',
		align: "center"
	},{
		header: '12',
		sortable: true,
		dataIndex: 'twelfthDay',
		align: "center"
	},{
		header: '13',
		sortable: true,
		dataIndex: 'thirteenthDay',
		align: "center"
	},{
		header: '14',
		sortable: true,
		dataIndex: 'fourteenthDay',
		align: "center"
	},{
		header: '15',
		sortable: true,
		dataIndex: 'fifteenthDay',
		align: "center"
	},{
		header: '16',
		sortable: true,
		dataIndex: 'sixteenthDay',
		align: "center"
	},{
		header: '17',
		sortable: true,
		dataIndex: 'seventeenthDay',
		align: "center"
	},{
		header: '18',
		sortable: true,
		dataIndex: 'eighteenthDay',
		align: "center"
	},{
		header: '19',
		sortable: true,
		dataIndex: 'nineteenthDay',
		align: "center"
	},{
		header: '20',
		sortable: true,
		dataIndex: 'twentiethDay',
		align: "center"
	},{
		header: '21',
		sortable: true,
		dataIndex: 'twentyFirstDay',
		align: "center"
	},{
		header: '22',
		sortable: true,
		dataIndex: 'twentySecondDay',
		align: "center"
	},{
		header: '23',
		sortable: true,
		dataIndex: 'twentyThirdDay',
		align: "center"
	},{
		header: '24',
		sortable: true,
		dataIndex: 'twentyFourthDay',
		align: "center"
	},{
		header: '25',
		sortable: true,
		dataIndex: 'twentyFifthDay',
		align: "center"
	},{
		header: '26',
		sortable: true,
		dataIndex: 'twentySixthDay',
		align: "center"
	},{
		header: '27',
		sortable: true,
		dataIndex: 'twentySeventhDay',
		align: "center"
	},{
		header: '28',
		sortable: true,
		dataIndex: 'twentyEighthDay',
		align: "center"
	},{
		header: '29',
		sortable: true,
		dataIndex: 'twentyNinthDay',
		align: "center"
	},{
		header: '30',
		sortable: true,
		dataIndex: 'thirtiethDay',
		align: "center"
	},{
		header: '31',
		sortable: true,
		dataIndex: 'thirtyFirstDay',
		align: "center"
	},{
		header: '计划休息总天数',
		sortable: true,
		dataIndex: 'totalRestDays',
		align: "center"
	},{
		header: '创建时间',
		sortable: true,
		width: 60,
		dataIndex: 'createdTimeStr'
	},{
		header: '创建人',
		sortable: true,
		width: 60,
		dataIndex: 'creator'
	},{
		header: '修改时间',
		sortable: true,
		width: 60,
		dataIndex: 'modifiedTimeStr'
	},{
		header: '修改人',
		width: 60,
		sortable: true,
		dataIndex: 'modifier'
	}]
});

var resultGrid = new Ext.grid.GridPanel({
	title: '查询结果',
	cm: cm,
	sm: sm,
	store: store,
	autoScroll: true,
	loadMask: true,
	tbar: pagingButton
});

var record_week = Ext.data.Record.create([{
	name: 'areaCode',
	mapping: 'areaCode',
	type: 'string'
},{
	name: 'departmentId',
	mapping: 'departmentId',
	type: 'string'
},{
	name: 'departmentCode',
	mapping: 'departmentCode',
	type: 'string'
},{
	name: 'employeeName',
	mapping: 'employeeName',
	type: 'string'
},{
	name: 'deptDesc',
	mapping: 'deptDesc',
	type: 'string'
},{
	name: 'employeeCode',
	mapping: 'employeeCode',
	type: 'string'
},{
	name: 'yearMonth',
	mapping: 'yearMonth',
	type: 'string'
},{
	name: 'schedulingType',
	mapping: 'schedulingType',
	type: 'int'
},{
	name: 'createdTimeStr',
	mapping: 'createdTimeStr',
	type: 'String'
},{
	name: 'creator',
	mapping: 'creator',
	type: 'string'
},{
	name: 'modifiedTimeStr',
	mapping: 'modifiedTimeStr',
	type: 'String'
},{
	name: 'modifier',
	mapping: 'modifier',
	type: 'string'
},{
	name: 'configureCode',
	mapping: 'configureCode',
	type: 'string'
},{
	name: 'monday',
	mapping: 'monday',
	type: 'string'
},{
	name: 'tuesday',
	mapping: 'tuesday',
	type: 'string'
},{
	name: 'wednesday',
	mapping: 'wednesday',
	type: 'string'
},{
	name: 'thursday',
	mapping: 'thursday',
	type: 'string'
},{
	name: 'friday',
	mapping: 'friday',
	type: 'string'
},{
	name: 'saturday',
	mapping: 'saturday',
	type: 'string'
},{
	name: 'sunday',
	mapping: 'sunday',
	type: 'string'
},{
	name: 'totalRestDays',
	mapping: 'totalRestDays',
	type: 'string'
},{
	name: 'workType',
	mapping: 'workType',
	type: 'string'
},{
	name: 'yearWeek',
	mapping: 'yearWeek',
	type: 'string'
},{
	name: 'confirmStatus',
	mapping: 'confirmStatus',
	type: 'string'
},{
	name: 'confirmDateAsString',
	mapping: 'confirmDateAsString',
	type: 'string'
}]);

var store_week = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../driver/queryDriverScheduledByWeek.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'resultMap.root',
		totalProperty: 'resultMap.totalSize'
	}, record_week)
});

var pagingButton_week = new Ext.PagingToolbar({
	store: store_week,
	displayInfo: true,
	displayMsg: '当前显示 {0} - {1} 总记录数目 {2}',
	pageSize: 20,
	emptyMsg: '未检索到数据'
});

var sm_week = new Ext.grid.CheckboxSelectionModel({
	singleSelect: false
});

var cm_week = new Ext.grid.ColumnModel({
	defaults: {
		width: 60
	},
	columns: [sm_week,{
		header: '地区代码',
		sortable: true,
		dataIndex: 'areaCode'
	},{
		header: '网点ID',
		sortable: true,
		hidden: true,
		dataIndex: 'departmentId'
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'departmentCode',
		renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			return value + "/" + record.data["deptDesc"];
		}
	},{
		header: '员工名称',
		sortable: true,
		dataIndex: 'employeeName'
	},{
		header: '员工工号',
		sortable: true,
		dataIndex: 'employeeCode'
	},{
		header: '排班周数',
		sortable: true,
		dataIndex: 'yearWeek'
	},{
		header: '排班类型',
		sortable: true,
		hidden:true,
		dataIndex: 'schedulingType',
		renderer: function(value) {
			var schedulingTypes = new Array('预排班', '排班');
			return schedulingTypes[value];
		}
	},{
		header: '状态',
		sortable: true,
		dataIndex: 'confirmStatus',
		renderer: function(value) {
			if(value=='0'){
				return '<span style="color:red"><b>未确认<b></span>';
			}
			return '已确认';
		}
	},{
		header: '确认日期',
		sortable: true,
		dataIndex: 'confirmDateAsString'
	},{
		header: '类型',
		sortable: true,
		dataIndex: 'workType'
	}
	,{
		header: '星期一',
		sortable: true,
		id: 'week_1',
		dataIndex: 'monday',
		width: 90,
		align: "center"
	},{
		header: '星期二',
		sortable: true,
		id: 'week_2',
		dataIndex: 'tuesday',
		width: 90,
		align: "center"
	},{
		header: '星期三',
		sortable: true,
		id: 'week_3',
		dataIndex: 'wednesday',
		width: 90,
		align: "center"
	},{
		header: '星期四',
		sortable: true,
		dataIndex: 'thursday',
		id: 'week_4',
		width: 90,
		align: "center"
	},{
		header: '星期五',
		sortable: true,
		dataIndex: 'friday',
		id: 'week_5',
		width: 90,
		align: "center"
	},{
		header: '星期六',
		sortable: true,
		dataIndex: 'saturday',
		id: 'week_6',
		width: 90,
		align: "center"
	},{
		header: '星期天',
		sortable: true,
		id: 'week_7',
		dataIndex: 'sunday',
		width: 90,
		align: "center"
	}
	,{
		header: '创建时间',
		sortable: true,
		dataIndex: 'createdTimeStr'
	},{
		header: '创建人',
		sortable: true,
		dataIndex: 'creator'
	},{
		header: '修改时间',
		sortable: true,
		dataIndex: 'modifiedTimeStr'
	},{
		header: '修改人',
		sortable: true,
		dataIndex: 'modifier'
	}]
});

var resultGrid_week = new Ext.grid.GridPanel({
	title: '查询结果',
	cm: cm_week,
	sm: sm_week,
	store: store_week,
	autoScroll: true,
	loadMask: true,
	tbar: pagingButton_week
});

var centerPanel = new Ext.Panel({
	region: 'center',
	items: [conditionPanel,resultGrid_week,resultGrid],
	listeners: {
		resize: function(p, adjWidth, adjHeight, rawWidth, rawHeight) {
			resultGrid.setWidth(adjWidth - 5);
			resultGrid.setHeight(adjHeight - 165);
			resultGrid_week.setWidth(adjWidth - 5);
			resultGrid_week.setHeight(adjHeight - 165);
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
		items: [treePanel,centerPanel]
	});
	Ext.getCmp('query.yeaMonth').hide();
	resultGrid.hide();
	exportDriverSchedulingButton.hide();
	var year = moment().year();
	var weeks = moment().isoWeek();
	
	if (weeks < 10) {
		weeks = '0'+ weeks;
	}
	Ext.get('weekOfYear').set({
		value: year + '-' + weeks
	});
	Ext.get('selectedDay').set({
		value: moment().format('YYYY-MM-DD')
	});
	Ext.get('weekPicker').set({
		value: year + '年-' + weeks + '周'
	});
	setTitleByWeek();

});



var noticeStore = new Ext.data.JsonStore({
	url : "sechMgt_searchNoticesCount.action",
	root : 'schedulMgts',
	totalProperty : 'totalSize',
	fields : [ {
		name : 'flag',
		mapping : 'flag'
	} ]
});
var noticeWindow = new Ext.Window({
	title : "通知",
	height : 120,
	width : 250,
	closeAction : "hide",
	plain : true,
	modal : false,
	resizable : false,
	html : '<font color=red>请尽快完成下一个周的预排班！</font>'
});

var task = {
	run : function() {
		noticeStore.load({
			callback : function(r, op, success) {
				if (!Ext.isEmpty(todayDt)) {
					if (noticeWindow.hidden && (todayDt.getDay() == 0 || todayDt.getDay() == 5 || todayDt.getDay() == 6)) {
						noticeWindow.show();
						noticeWindow.setPosition(document.body.clientWidth - 250, document.body.clientHeight - 120 - 5);
					} else {
						noticeWindow.hide();
					}
				}
			}
		});
	},
	interval : 300000
};
var runner = new Ext.util.TaskRunner();
runner.start(task);