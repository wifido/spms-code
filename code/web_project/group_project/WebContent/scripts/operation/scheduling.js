//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var modifyHistorySchedulingPermission = '<app:isPermission code = "/schedulingMgt/modify_historty_scheduling.action">a</app:isPermission>';
var historyMonthModifySchedulePermission = '<app:isPermission code = "/schedulingMgt/historyMonthModifySchedule.action">a</app:isPermission>';
var filterDeptCodeType = '${filterDeptCodeType}';
var filterAreaCodeType = '${filterAreaCodeType}';
var todayDt;
Ext.Ajax.request({
	timeout : 3000,
	url : 'getSysdate',
	success : function(response, config) {
		todayDt = Date.parseDate(response.responseText, 'Y-m-d');
	}
});

/*
 * 方法:Array.remove(dx) 功能:根据元素值删除数组元素. 参数:元素值 返回:在原数组上修改数组 作者：pxp
 */
Array.prototype.indexOf = function(val) {
	for ( var i = 0; i < this.length; i++) {
		if (this[i] == val) {
			return i;
		}
	}
	return -1;
};
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
// ----------------排班人员选择窗口------------------------------//
var empStore = new Ext.data.JsonStore({
	pruneModifiedRecords : true,
	url : '../operation/sechMgt_listEmp.action',
	root : 'emps',
	totalProperty : 'totalSize',
	fields : [ {
		name : 'empId',
		mapping : 'empId'
	}, {
		name : 'areaCode',
		mapping : 'areaCode'
	}, {
		name : 'deptName',
		mapping : 'deptName'
	}, {
		name : 'empCode',
		mapping : 'empCode'
	}, {
		name : 'empName',
		mapping : 'empName'
	} ],
	listeners : {
		beforeload : function(s) {
			s.baseParams["dto.deptId"] = Ext.getCmp("emp_deptid").getValue();
			s.baseParams["dto.empCode"] = Ext.getCmp('query_empcode').getValue();
			s.baseParams["dto.empName"] = Ext.getCmp("query_empName").getValue();
			s.baseParams["dto.teamId"] = Ext.getCmp('query_group').getValue();
			s.baseParams["limit"] = 20;
		}
	}
});
var _SM = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});
function showEmpWin() {
	var empWin = Ext.getCmp('empWin');
	if (!empWin) {
		var empWin = new Ext.Window({
			title : '选择排班人员',
			id : 'empWin',
			width : 500,
			height : 400,
			modal : true,
			layout : 'border',
			listeners : {
				hide : function() {
					empWin.items.each(function(item, i) {
						empWin.items.remove(item);
					});
				}
			}
		});
	}
	empWin.add({
		xtype : 'form',
		id : 'emp_form',
		region : 'north',
		layout : 'column',
		height : 100,
		buttonAlign : 'center',
		fbar : [ {
			xtype : 'button',
			text : '查询',
			cls : 'x-btn-normal',
			minWidth : 60,
			handler : function() {
				empStore.load({
					params : {
						start : 0
					},
					callback : Ext.ux.GridDataLoadCallback
				});
			}
		}, {
			xtype : 'button',
			text : '选择',
			cls : 'x-btn-normal',
			minWidth : 60,
			handler : function() {
				var vSelectionModel = Ext.getCmp('emp_grid').getSelectionModel();
				if (!vSelectionModel.hasSelection()) {
					Ext.Msg.alert('提示', '请选择一个或多个排班人员！');
					return false;
				}
				var selectRec = _SM.getSelections();
				Ext.each(selectRec, function(rec) {
					var inx = _Store.find('empCode', rec.data.empCode);
					if (inx == -1)
						_Store.add(rec);
				});
				empWin.close();
			}
		} ],
		frame : true,
		border : false,
		items : [ {
			columnWidth : .5,
			xtype : 'panel',
			layout : 'form',
			labelWidth : 40,
			labelAlign : 'right',
			items : [ {
				xtype : 'hidden',
				id : 'emp_deptid'
			}, {
				fieldLabel : '网点',
				xtype : 'textfield',
				id : 'emp_deptcode',
				width : 100,
				readOnly : true,
				submitValue : false
			}, {
				fieldLabel : '姓名',
				xtype : 'textfield',
				id : 'query_empName',
				width : 100
			} ]
		}, {
			columnWidth : .5,
			xtype : 'panel',
			layout : 'form',
			labelWidth : 40,
			labelAlign : 'right',
			items : [ {
				fieldLabel : '工号',
				xtype : 'textfield',
				name : 'empCode',
				id : 'query_empcode',
				width : 100
			}, {
				xtype : 'combo',
				fieldLabel : "组名",
				labelStyle : 'width:auto;',
				width : 100,
				hiddenName : "groupId",
				mode : 'remote',
				id : "query_group",
				displayField : "groupName",
				valueField : "groupId",
				typeAhead : true,
				selectOnFocus : true,
				triggerAction : "all",
				minChars : 0,
				handleHeight : 8,
				// pageSize :10,
				resizable : true,
				forceSelection : true,
				lastQuery : '',
				allQuery : '',
				editable : false,
				listWidth : 250,
				value : null,
				emptyText : '请选择',
				listeners : {
					beforequery : function(qe) {
						var deptCode = Ext.getCmp('q_deptCode').getValue();
						qe.combo.getStore().baseParams['deptId'] = (deptCode == '001' ? null : Ext.getCmp('q_deptId').getValue());
						delete qe.combo.lastQuery;
						return deptCode ? true : false;
					}
				},
				store : new Ext.data.JsonStore({
					pruneModifiedRecords : true,
					url : "../operation/queryGroup.action",
					fields : [ {
						name : 'groupName',
						mapping : 'GROUP_NAME'
					}, {
						name : 'groupId',
						mapping : 'GROUP_ID'
					} ],
					data : {
						groups : [ {
							groupName : '--',
							groupId : null
						} ]
					},
					listeners : {
						load : function(s) {
							if (!s.getCount() || s.getAt(0).data.groupName != '--')
								s.insert(0, new s.recordType({
									groupName : '--',
									groupId : null
								}));
						}
					}
				})
			} ]
		} ]
	}, {
		xtype : 'grid',
		id : 'emp_grid',
		region : 'center',
		border : false,
		store : empStore,
		loadMask : true,
		sm : _SM,
		tbar : new Ext.PagingToolbar({
			pageSize : 20,
			store : empStore,
			displayInfo : true,
			displayMsg : "当前显示 {0} - {1} 总记录数目 {2}",
			emptyMsg : "未检索到数据"
		}),
		columns : [ new Ext.grid.RowNumberer(), _SM, {
			header : '地区代码',
			dataIndex : 'areaCode',
			width : 100,
			align : 'center'
		}, {
			header : '网点',
			dataIndex : 'deptName',
			width : 100,
			align : 'center'
		}, {
			header : '工号',
			dataIndex : 'empCode',
			width : 100,
			align : 'center'
		}, {
			header : '姓名',
			dataIndex : 'empName',
			width : 100,
			align : 'center'
		} ]
	});
	Ext.getCmp('emp_deptid').setValue(Ext.getCmp('q_deptId').getValue());
	Ext.getCmp('emp_deptcode').setValue(Ext.getCmp('q_deptCode').getValue());
	var form = Ext.getCmp('emp_form').getForm();
	form.findField('empCode').setValue('');
	empStore.load();
	empWin.show();
}
/*----------------------------------=导入窗口-----------------------------------------*/

var downTemplate = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=排班导入模板.xls&moduleName=operation&entityName=schedulingMgt&isTemplate=true";
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
};

var uploadFileName = new Ext.form.Hidden({
	name : "dto.filePath"
});
var uploadFile = new Ext.form.TextField({
	width : 350,
	id : 'uploadFile',
	inputType : 'file',
	name : 'uploadFile',
	fieldLabel : '文件路径',
	labelStyle : 'width:auto'
});
var uploadform = new Ext.form.FormPanel(
		{
			id : 'uploadForm',
			height : 160,
			width : 600,
			frame : true,
			fileUpload : true,
			border : false,
			items : [
					new Ext.Panel({
						layout : 'form',
						border : false,
						items : [ {
							xtype : 'hidden',
							id : 'upload_deptid',
							name : "dto.deptId"
						}, uploadFileName, uploadFile ]
					}),
					new Ext.form.Label(
							{
								html : '<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">模板文件:</font> <a href="#" onclick="downTemplate()">排班导入模板.xls</a>'
							}) ]
		});
var uploadWindow = new Ext.Window({
	title : "导入",
	height : 180,
	width : 500,
	layout : 'column',
	closeAction : 'hide',
	plain : true,
	modal : true,
	tbar : [
			{
				text : '导入',
				pressed : true,
				height : 18,
				minWidth : 60,
				handler : function() {
					uploadFileName.setValue(uploadFile.getValue());
					if (uploadFileName.getValue().indexOf(".xls") > 0) {
						Ext.MessageBox.wait('正在保存数据...');
						uploadform.getForm().doAction(
								'submit',
								{
									failure : function(form, action) {
										Ext.MessageBox.show({
											title : '错误',
											msg : action.result.msg,
											buttons : Ext.Msg.OK,
											icon : Ext.MessageBox.ERROR
										});
									},
									success : function(form, action) {
										if (Ext.isEmpty(action.result.msg)) {
											Ext.MessageBox.alert("提示", action.result.tips, function() {
												uploadWindow.hide();
												gridView.getStore().load();
											});
											if (!Ext.isEmpty(action.result.fileName))
												window.location = '../common/downloadReportFile.action?'
														+ Ext.util.Format.htmlDecode(encodeURI(encodeURI(action.result.fileName)));
										} else {
											Ext.MessageBox.alert("提示", action.result.msg);
											return;
										}
										if (Ext.isEmpty(action.result.schedulMgts)) {
											return;
										}
										confirm_Store.proxy = new Ext.data.PagingMemoryProxy(action.result.schedulMgts);
										var confirmWin = Ext.getCmp("confirmWin");
										if (Ext.isEmpty(confirmWin, false)) {
											var confirmWin = new Ext.Window({
												id : "confirmWin",
												closable : true,
												title : '待确认记录',
												width : 600,
												height : 500,
												modal : true,
												border : false,
												bodyBorder : false,
												closable : true,
												resizable : false,
												buttonAlign : 'center',
												fbar : [ {
													text : '确认',
													handler : function() {
														if (!confirm_sm.hasSelection()) {
															Ext.Msg.alert('提示', '请选择要确认的记录!');
															return;
														}
														var confirmArr = confirm_sm.getSelections();
														var obj = [];
														for ( var i = 0; i < confirmArr.length; i++) {
															obj.push(confirmArr[i].data);
														}
														Ext.MessageBox.wait('正在保存数据...');
														var str = Ext.util.JSON.encode(obj);
														Ext.Ajax.request({
															url : 'sechMgt_saveImportConfirmData.action',
															params : {
																dataStr : str
															},
															success : function(response, config) {
																var res = Ext.decode(response.responseText);
																if (Ext.isEmpty(res.msg)) {
																	Ext.Msg.alert('提示', '操作成功');
																	confirmWin.hide();
																	uploadWindow.hide();
																	gridView.getStore().load();
																} else {
																	Ext.Msg.alert('提示', '操作失败');
																}
															}
														})
													}
												}, {
													text : '取消',
													handler : function() {
														confirmWin.hide();
													}
												} ],
												layout : 'fit',
												closeAction : 'hide',
												listeners : {
													hide : function() {
														this.items.each(function(item, i) {
															this.remove(this.getComponent(i), true);
														});
													}
												}
											});
										}
										confirm_Store.load({
											params : {
												start : 0,
												limit : 1000
											}
										});
										confirmWin.add(confirm_gridView);
										confirmWin.show();
									},
									url : "importRec.action"
								});

					} else {
						Ext.MessageBox.alert('提示', '导入时，请先选择Excel文件');
					}
				}
			}, '-', {
				text : '重置',
				pressed : true,
				height : 18,
				minWidth : 60,
				handler : function() {
					Ext.resetFileInput('uploadFile');
				}
			} ],
	items : [ uploadform ]
});

// ----------------主界面查询结果表格------------------------------//
var gridStore = new Ext.data.JsonStore({
	pruneModifiedRecords : true,
	url : "sechMgt_search.action",
	root : "schedulMgts",
	totalProperty : "totalSize",
	fields : [ {
		name : 'id'
	}, {
		name : 'areaCode'
	}, {
		name : 'deptCode'
	}, {
		name : 'ym'
	}, {
		name : 'empCode'
	}, {
		name : 'empName'
	}, {
		name : 'workTypeStr'
	}, {
		name : 'commitStatus'
	}, {
		name : 'day1'
	}, {
		name : 'day2'
	}, {
		name : 'day3'
	}, {
		name : 'day4'
	}, {
		name : 'day5'
	}, {
		name : 'day6'
	}, {
		name : 'day7'
	}, {
		name : 'day8'
	}, {
		name : 'day9'
	}, {
		name : 'day10'
	}, {
		name : 'day11'
	}, {
		name : 'day12'
	}, {
		name : 'day13'
	}, {
		name : 'day14'
	}, {
		name : 'day15'
	}, {
		name : 'day16'
	}, {
		name : 'day17'
	}, {
		name : 'day18'
	}, {
		name : 'day19'
	}, {
		name : 'day20'
	}, {
		name : 'day21'
	}, {
		name : 'day22'
	}, {
		name : 'day23'
	}, {
		name : 'day24'
	}, {
		name : 'day25'
	}, {
		name : 'day26'
	}, {
		name : 'day27'
	}, {
		name : 'day28'
	}, {
		name : 'day29'
	}, {
		name : 'day30'
	}, {
		name : 'day31'
	}, {
		name : 'dimissionDt'
	}, {
		name : 'betweenDate'
	}, {
		name : 'version'
	} , {
		name : 'transferDate'
	}, {
		name : 'sfDate'
	}, {
		name : 'dateFrom'
	}, {
		name : 'lastZno'
	}, {
		name : 'empPostType'
	}, {
		name : 'empDutyName'
	}],
	listeners : {
		beforeLoad : function() {
			this.baseParams['dto.deptId'] = Ext.getCmp('q_deptId').getValue();
			this.baseParams['dto.status'] = Ext.getCmp('q_status').getValue();
			this.baseParams['dto.empName'] = Ext.getCmp('q_empName').getValue();
			this.baseParams['dto.empCode'] = Ext.getCmp('q_empCode').getValue();
			this.baseParams['dto.teamId'] = Ext.getCmp('q_groupId').getValue();
			this.baseParams['dto.ym'] = Ext.isEmpty(Ext.getCmp('q_ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('q_ym').getValue(), 'Y-m');
			this.baseParams['dto.className'] = Ext.getCmp('q_className').getValue();
			this.baseParams['limit'] = pagingBar.pageSize;
		},
		load : function(store, records) {
			var colSidx = 8;
			Ext.each(records, function(res, index) {
				var betweenDts = res.get('betweenDate');
				var ym = res.get('ym');
				if (!Ext.isEmpty(betweenDts)) {
					var beteenDt = betweenDts.split('~');
					var sdt = Date.parseDate(beteenDt[0], 'Y-m-d');
					var edt = Date.parseDate(beteenDt[1], 'Y-m-d');
					if (!Ext.isEmpty(sdt) && !Ext.isEmpty(edt)) {
						var fdt = Date.parseDate(ym + '-01', 'Y-m-d');
						var columct = fdt.getDaysInMonth() + colSidx;
						for ( var i = 9; i <= columct; i++) {
							var dnum = i - colSidx;
							var n = dnum.toString().length == 1 ? ('0' + dnum.toString()) : dnum.toString();
							var cdt = Date.parseDate(ym + '-' + n, 'Y-m-d');
							var cellvalue = res.get('day' + dnum);
							if (cdt.between(sdt, edt) && Ext.isEmpty(cellvalue)) {
								Ext.fly(gridView.getView().getCell(index, i)).addClass('unfinished_cell_bgcolor');
							}
						}
					}
				}
				var dimissionDt = res.get('dimissionDt');
				if (!Ext.isEmpty(dimissionDt) && dimissionDt.indexOf(ym) == 0) {
					var ddt = Date.parseDate(dimissionDt, 'Y-m-d');
					var colct = ddt.getDaysInMonth() + colSidx;// 38
					var colIndex = ddt.getDate() + colSidx; // 12
					for ( var i = colIndex; i <= colct; i++) {
						Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_bgcolor');
						var dnum = i - colSidx;
						var n = dnum.toString().length == 1 ? ('0' + dnum.toString()) : dnum.toString();
						var cellvalue = res.get('day' + dnum);
						if (Ext.isEmpty(cellvalue)) {
							Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_blue_bgcolor');
						}
					}
				}
				var transferDt = res.get('transferDate');
				var empPostType = res.get('empPostType');
				if (!Ext.isEmpty(transferDt) && transferDt.indexOf(ym) == 0) {
					var ddt = Date.parseDate(transferDt, 'Y-m-d');
					var colIndex = colSidx + 1;
					var colct = ddt.getDate() + colSidx - 1;
					if (empPostType != 1) {
						colct = ddt.getDaysInMonth() + colSidx;
						colIndex = ddt.getDate() + colSidx;
					}
					for ( var i = colIndex; i <= colct; i++) {
						Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_bgcolor');
						var dnum = i - colSidx;
						var n = dnum.toString().length == 1 ? ('0' + dnum.toString()) : dnum.toString();
						var cellvalue = res.get('day' + dnum);
						if (Ext.isEmpty(cellvalue)) {
							Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_blue_bgcolor');
						}
					}
				}
				var sfDate = res.get('sfDate');
				if (!Ext.isEmpty(sfDate) && sfDate.indexOf(ym) == 0) {
					var ddt = Date.parseDate(sfDate, 'Y-m-d');
					var colIndex = colSidx + 1;
					var colct = ddt.getDate() + colSidx - 1;
					for ( var i = colIndex; i <= colct; i++) {
						Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_bgcolor');
						var dnum = i - colSidx;
						var n = dnum.toString().length == 1 ? ('0' + dnum.toString()) : dnum.toString();
						var cellvalue = res.get('day' + dnum);
						if (Ext.isEmpty(cellvalue)) {
							Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_blue_bgcolor');
						}
					}
				}
				var dateFrom = res.get('dateFrom');
				var dept_code = res.get('deptCode');
				var lastZno = res.get('lastZno');
				if (!Ext.isEmpty(dateFrom) && dateFrom.indexOf(ym) == 0) {
					var ddt = Date.parseDate(dateFrom, 'Y-m-d');
					var colIndex = colSidx + 1;
					var colct = ddt.getDate() + colSidx - 1;
					if (dept_code == lastZno) {
						colct = ddt.getDaysInMonth() + colSidx;
						colIndex = ddt.getDate() + colSidx;
					}
					for ( var i = colIndex; i <= colct; i++) {
						Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_bgcolor');
						var dnum = i - colSidx;
						var n = dnum.toString().length == 1 ? ('0' + dnum.toString()) : dnum.toString();
						var cellvalue = res.get('day' + dnum);
						if (Ext.isEmpty(cellvalue)) {
							Ext.fly(gridView.getView().getCell(index, i)).addClass('cell_blue_bgcolor');
						}
					}
				}
				//
			});
		}
	}
});
var pagingBar = new Ext.PagingToolbar({
	displayInfo : true,
	store : gridStore,
	pageSize : 50,
	displayMsg : "当前显示 {0} - {1} 总记录数目 {2}",
	emptyMsg : "未检索到数据"
});
var checkboxSelectionModel = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});
var gridView = new Ext.grid.GridPanel({
	enableHdMenu : false,
	frame : true,
	loadMask : true,
	columnLines : true,
	region : "center",
	store : gridStore,
	tbar : pagingBar,
	sm : checkboxSelectionModel,
	cm : new Ext.grid.ColumnModel([ checkboxSelectionModel, {
		header : '年月',
		dataIndex : 'ym',
		align : 'center',
		width : 70
	}, {
		header : '地区代码',
		dataIndex : 'areaCode',
		align : 'center',
		width : 70
	}, {
		header : '网点代码',
		dataIndex : 'deptCode',
		align : 'center',
		width : 70
	}, {
		header : '工号',
		dataIndex : 'empCode',
		align : 'center',
		width : 70
	}, {
		header : '姓名',
		dataIndex : 'empName',
		align : 'center',
		width : 80
	}, {
		header : '用工类型',
		dataIndex : 'workTypeStr',
		align : 'center',
		width : 80
	}, {
		header : '职位名称',
		dataIndex : 'empDutyName',
		align : 'center',
		width : 80
	}, {
		header : '提交状态',
		dataIndex : 'commitStatus',
		align : 'center',
		width : 70,
		renderer: function(value) {
			if (value == 1) {
				return "<label style = 'color:green'>已提交</label>";
			}
			return "<label style = 'color:red'>未提交</label>";
		}
	}, {
		header : '1',
		dataIndex : 'day1',
		align : 'center',
		width : 30
	}, {
		header : '2',
		dataIndex : 'day2',
		align : 'center',
		width : 30
	}, {
		header : '3',
		dataIndex : 'day3',
		align : 'center',
		width : 30
	}, {
		header : '4',
		dataIndex : 'day4',
		align : 'center',
		width : 30
	}, {
		header : '5',
		dataIndex : 'day5',
		align : 'center',
		width : 30
	}, {
		header : '6',
		dataIndex : 'day6',
		align : 'center',
		width : 30
	}, {
		header : '7',
		dataIndex : 'day7',
		align : 'center',
		width : 30
	}, {
		header : '8',
		dataIndex : 'day8',
		align : 'center',
		width : 30
	}, {
		header : '9',
		dataIndex : 'day9',
		align : 'center',
		width : 30
	}, {
		header : '10',
		dataIndex : 'day10',
		align : 'center',
		width : 30
	}, {
		header : '11',
		dataIndex : 'day11',
		align : 'center',
		width : 30
	}, {
		header : '12',
		dataIndex : 'day12',
		align : 'center',
		width : 30
	}, {
		header : '13',
		dataIndex : 'day13',
		align : 'center',
		width : 30
	}, {
		header : '14',
		dataIndex : 'day14',
		align : 'center',
		width : 30
	}, {
		header : '15',
		dataIndex : 'day15',
		align : 'center',
		width : 30
	}, {
		header : '16',
		dataIndex : 'day16',
		align : 'center',
		width : 30
	}, {
		header : '17',
		dataIndex : 'day17',
		align : 'center',
		width : 30
	}, {
		header : '18',
		dataIndex : 'day18',
		align : 'center',
		width : 30
	}, {
		header : '19',
		dataIndex : 'day19',
		align : 'center',
		width : 30
	}, {
		header : '20',
		dataIndex : 'day20',
		align : 'center',
		width : 30
	}, {
		header : '21',
		dataIndex : 'day21',
		align : 'center',
		width : 30
	}, {
		header : '22',
		dataIndex : 'day22',
		align : 'center',
		width : 30
	}, {
		header : '23',
		dataIndex : 'day23',
		align : 'center',
		width : 30
	}, {
		header : '24',
		dataIndex : 'day24',
		align : 'center',
		width : 30
	}, {
		header : '25',
		dataIndex : 'day25',
		align : 'center',
		width : 30
	}, {
		header : '26',
		dataIndex : 'day26',
		align : 'center',
		width : 30
	}, {
		header : '27',
		dataIndex : 'day27',
		align : 'center',
		width : 30
	}, {
		header : '28',
		dataIndex : 'day28',
		align : 'center',
		width : 30
	}, {
		header : '29',
		dataIndex : 'day29',
		align : 'center',
		width : 30
	}, {
		header : '30',
		dataIndex : 'day30',
		align : 'center',
		width : 30
	}, {
		header : '31',
		dataIndex : 'day31',
		align : 'center',
		width : 30
	} ])
});
// ----------------新增窗口------------------------------//
var checkboxSelectionModel2 = new Ext.grid.CheckboxSelectionModel({});
var _Store = new Ext.data.ArrayStore({
	pruneModifiedRecords : true,
	fields : [ 'empCode', 'empName' ]
});




Ext.Ajax.timeout = 300000;
Ext.onReady(function() {
	Ext.QuickTips.init();
	//
	var fieldDeptCode = new Ext.form.TextField({width:160,name:"fieldDeptCode", xtype : 'textfield'
		, listeners: {
	        specialkey: function(field, e){
	            if (e.getKey() == e.ENTER) {
	            	queryDeptCode();
	            }
	        }
	    }});

	var queryDeptCode = function() {
		if (fieldDeptCode.getValue()=="") {
			Ext.Msg.alert('提示','网点代码不能为空', function(){
				fieldDeptCode.focus();
			});
			return;
		}
		
		Ext.Ajax.request({
			url:"../operation/userDeptAction_queryDeptCode.action",
			params:{fieldDeptCode:fieldDeptCode.getValue()},
			success:function(response){
				//debugger;
				var dept_ = Ext.util.JSON.decode(response.responseText);
				var path = dept_.path;
				if(path && path != '/0'){
					treePanel.root.reload();
					treePanel.selectPath(path);
					Ext.getCmp('q_deptId').setValue(dept_.deptId);
					Ext.getCmp('q_deptCode').setValue(dept_.deptCode + '/' + dept_.deptName);
					Ext.getCmp('upload_deptid').setValue(dept_.deptId);
					
				} else {
					Ext.Msg.alert('提示','该网点不存在！', function(){
						fieldDeptCode.selectText();
					}, this);
				}
			} ,scope : this
		}, false);
	};
	// ----------------机构树------------------------------//
	var treePanel = new Ext.tree.TreePanel({
		region : 'west',
		margins : '1 1 1 1',
		width : 245,
		title : '网点信息',
		collapsible : true,
		autoScroll : true,
		tbar : [
		        {text : "网点代码", xtype : 'label'}, 
		        fieldDeptCode ,
		        {
		        	icon : "../images/search.gif", 
		        	xtype : 'button', 
		        	style : 'margin-left:2px',
		        	scope : this,
		        	handler : queryDeptCode
		        }], 
		root : new Ext.tree.AsyncTreeNode({
			id : '0',
			text : '顺丰速运',
			loader : new Ext.tree.TreeLoader({
				dataUrl : "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
			})
		}),
		listeners : {
			click : function(n) {
				if (n.id != 0
						&& (filterAreaCodeType.indexOf(n.attributes.typeCode+',') != -1)) {
					Ext.getCmp('q_deptId').setValue(n.id);
					Ext.getCmp('q_deptCode').setValue(n.attributes.text);
					Ext.getCmp('upload_deptid').setValue(n.id);
				}
			}
		}
	});
	// ----------------查询------------------------------//
	var btnSearch = new Ext.Button({
		text : "查询",
		pressed : true,
		minWidth : 60,
		handler : function() {
			if (Ext.isEmpty(Ext.getCmp("q_deptId").getValue())) {
				Ext.Msg.alert('提示', '请选择网点代码');
				return false;
			}
			gridView.getStore().load();
			gridView.getSelectionModel().clearSelections();
		}
	});
	var queryView = new Ext.form.FormPanel({
		frame : true,
		region : "north",
		autoHeight : true,
		items : [ new Ext.form.FieldSet({
			labelWidth : 80,
			height : 100,
			layout : "column",
			title : "查询条件",
			items : [ new Ext.Panel({
				columnWidth : .25,
				layout : "form",
				items : [ {
					xtype : 'hidden',
					width : 100,
					id : 'q_deptId'
				}, {
					xtype : 'textfield',
					id : 'q_deptCode',
					width : 100,
					fieldLabel : "网点代码",
					submitValue : false,
					allowBlank : false,
					readOnly : true
				}, {
					xtype : 'combo',
					id : 'q_status',
					width : 100,
					fieldLabel : "状态",
					typeAhead : true,
					mode : 'local',
					displayField : "text",
					valueField : "key",
					triggerAction : 'all',
					editable : false,
					selectOnFocus : true,
					store : new Ext.data.SimpleStore({
						fields : [ "key", "text" ],
						data : [ [ 3, '全部' ], [ 1, '已完成' ], [ 2, '未完成' ] ]
					})
				} ]
			}), new Ext.Panel({
				columnWidth : .25,
				layout : "form",
				items : [ {
					xtype : 'textfield',
					width : 100,
					fieldLabel : "姓名",
					id : "q_empName"
				}, {
					xtype : 'textfield',
					width : 100,
					fieldLabel : "工号",
					id : "q_empCode"
				} ]
			}), new Ext.Panel({
				columnWidth : .25,
				layout : "form",
				items : [ {
					xtype : 'textfield',
					width : 100,
					fieldLabel : "班别代码",
					id : "q_className"
				}, {
					xtype : 'combo',
					fieldLabel : "小组名称",
					labelStyle : 'width:auto;',
					width : 100,
					hiddenName : "groupId",
					mode : 'remote',
					id : "q_groupId",
					displayField : "groupName",
					valueField : "groupId",
					typeAhead : true,
					selectOnFocus : true,
					triggerAction : "all",
					minChars : 0,
					handleHeight : 8,
					// pageSize :10,
					resizable : true,
					forceSelection : true,
					lastQuery : '',
					allQuery : '',
					editable : false,
					listWidth : 250,
					value : null,
					emptyText : '请选择',
					listeners : {
						beforequery : function(qe) {
							var deptCode = Ext.getCmp('q_deptCode').getValue();
							qe.combo.getStore().baseParams['deptId'] = (deptCode == '001' ? null : Ext.getCmp('q_deptId').getValue());
							delete qe.combo.lastQuery;
							return deptCode ? true : false;
						}
					},
					store : new Ext.data.JsonStore({
						pruneModifiedRecords : true,
						url : "../operation/queryGroup.action",
						fields : [ {
							name : 'groupName',
							mapping : 'GROUP_NAME'
						}, {
							name : 'groupId',
							mapping : 'GROUP_ID'
						} ],
						data : {
							groups : [ {
								groupName : '--',
								groupId : null
							} ]
						},
						listeners : {
							load : function(s) {
								if (!s.getCount() || s.getAt(0).data.groupName != '--')
									s.insert(0, new s.recordType({
										groupName : '--',
										groupId : null
									}));
							}
						}
					})

				} ]
			}), new Ext.Panel({
				columnWidth : .25,
				layout : "form",
				items : [ {
					xtype : 'datefield',
					width : 100,
					fieldLabel : "月份",
					id : "q_ym",
					format : 'Y-m',
					value : new Date,
					plugins : 'monthPickerPlugin'
				} ]
			}) ]
		}) ]
	});
	// ----------------新增------------------------------//
	var btnAdd = new Ext.Button({
		text : "新增",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var node = treePanel.getSelectionModel ().getSelectedNode();			
			if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能选择区部以下的网点！");
				return;
			}
			var formAddWin = Ext.getCmp("formAddWin");
			if (Ext.isEmpty(formAddWin, false)) {
				var formAddWin = new Ext.Window({
					id : "formAddWin",
					closable : true,
					title : '新增',
					width : 600,
					height : 500,
					modal : true,
					border : false,
					bodyBorder : false,
					closable : true,
					resizable : false,
					layout : 'fit',
					closeAction : 'hide',
					listeners : {
						hide : function() {
							_Store.removeAll();
							if (Ext.getCmp('datepicker').statusDays.length > 0) {
								Ext.getCmp('datepicker').statusDays.length = 0;
							}
							this.items.each(function(item, i) {
								this.remove(this.getComponent(i), true);
							});
						}
					}
				});
			}
			formAddWin.add({
				xtype : "form",
				layout : 'column',
				frame : true,
				id : "addForm",
				labelAlign : "right",
				labelWidth : 50,
				width : 600,
				height : 500,
				defaults : {
					width : 300,
					height : 450
				},
				tbar : [ {
					text : '保存',
					pressed : true,// 保存按钮
					minWidth : 60,
					handler : function() {
						if (!checkboxSelectionModel2.hasSelection()) {
							Ext.Msg.alert('提示', '请先选择一个或多个人员！');
							return;
						}
						if (Ext.getCmp('datepicker').statusDays.length == 0) {
							Ext.Msg.alert('提示', '请先选择一个或多个日期！');
							return;
						}
						// 获取班次代码
						var classCode = Ext.getCmp('classesName').getValue();
						if (Ext.isEmpty(classCode)) {
							Ext.Msg.alert('提示', '请选择班别代码！');
							return;
						}
						// 获取日期数组selDateArr
						var sel_dt = Ext.getCmp('datepicker').statusDays.join(',')
						// 获取人员列表 版本号
						var empArr = checkboxSelectionModel2.getSelections();
						var empStrArr = [];
						for ( var i = 0; i < empArr.length; i++) {
							empStrArr.push(empArr[i].get('empCode'));
						}
						var empCodes = empStrArr.join(',');
						beforeSaveUpdateValid('addForm', formAddWin, sel_dt, empCodes, classCode, 'sechMgt_saveDetail.action');

					}
				}, '-', {
					text : '取消',
					pressed : true,// 取消按钮
					minWidth : 60,
					handler : function() {
						formAddWin.hide();
					}
				} ],
				items : [ {
					columnWidth : .46,
					items : new Ext.form.FieldSet({
						height : 410,
						title : "人员选择",
						items : [ new Ext.grid.GridPanel({
							enableHdMenu : false,
							buttonAlign : 'center',
							id : 'empGird',
							fbar : [ {
								xtype : 'button',
								text : '添加',
								handler : function() {
									showEmpWin();
								}
							}, {
								xtype : 'button',
								text : '清空',
								handler : function() {
									_Store.removeAll();
								}
							} ],
							frame : true,
							width : 240,
							height : 370,
							loadMask : true,
							columnLines : true,
							region : "center",
							store : _Store,
							sm : checkboxSelectionModel2,
							cm : new Ext.grid.ColumnModel([ checkboxSelectionModel2, {
								header : '工号',
								dataIndex : 'empCode',
								align : 'center',
								width : 90
							}, {
								header : '姓名',
								dataIndex : 'empName',
								align : 'center',
								width : 90
							} ])
						}) ]
					})
				}, {
					columnWidth : .1
				}, {
					columnWidth : .44,
					items : [ new Ext.form.FieldSet({
						height : 245,
						title : "日期选择",
						items : [ new Ext.ux.DatePicker({
							id : 'datepicker',
							style : 'margin-left:17px;',
							minDate : Ext.getCmp('q_ym').getValue().getFirstDateOfMonth(),
							maxDate : Ext.getCmp('q_ym').getValue().getLastDateOfMonth(),
							showToday : false,
							listeners : {
								render : function() {
									var selectedMonth =Ext.getCmp('q_ym').getValue();
									var selectedday = new Date(moment(selectedMonth).add(6, 'days').format("YYYY-MM-DD"));
									var today = new Date(moment().add(1, 'days').format("YYYY-MM-DD"));
									var minDate = today;
									//如果有修改历史7天权限，或者 修改当前月之后的数据
									if(!Ext.isEmpty(modifyHistorySchedulingPermission)){
										if(!moment(selectedday).isAfter(moment())){
											minDate = new Date(moment().add(-6, 'days').format("YYYY-MM-DD"));
										} else {
											minDate = selectedMonth;
										}
									}
									//如果有修改历史整月天权限，或者 修改当前月之后的数据
									if(!Ext.isEmpty(historyMonthModifySchedulePermission)){
										minDate = selectedMonth;
									}
									if(moment(selectedMonth).isAfter(moment())) {
										minDate = selectedMonth;
									}
									
									this.setMinDate(minDate);
									this.update(selectedMonth, true);
								}
							}
						}) ]
					}), {
						layout : 'form',
						labelAlign : 'left',
						style : 'margin-right:10px',
						items : [ {
							xtype : 'combo',
							fieldLabel : "班别代码",
							labelStyle : 'width:auto;',
							width : 160,
							mode : 'remote',
							id : "classesName",
							displayField : "classCode",
							valueField : "classCode",
							typeAhead : true,
							selectOnFocus : true,
							triggerAction : "all",
							minChars : 0,
							handleHeight : 8,
							// pageSize :10,
							resizable : true,
							forceSelection : true,
							lastQuery : '',
							allQuery : '',
							editable : false,
							listWidth : 250,
							value : null,
							allowBlank : false,
							emptyText : '请选择',
							listeners : {
								beforequery : function(qe) {
									var deptCode = Ext.getCmp('q_deptCode').getValue();
									qe.combo.getStore().baseParams['deptId'] = (deptCode == '001' ? null : Ext.getCmp('q_deptId').getValue());
									delete qe.combo.lastQuery;
									return deptCode ? true : false;
								}
							},
							store : new Ext.data.JsonStore({
								pruneModifiedRecords : true,
								root : "root",
								totalProperty : "totalSize",
								url : "../operation/schedule_querySchedule.action",
								fields : [ {
									name : 'classCode',
									mapping : 'SCHEDULE_CODE'
								}, {
									name : 'classCode',
									mapping : 'SCHEDULE_CODE'
								} ],
								listeners : {
									beforeload : function(s, o) {
										s.setBaseParam("deptId", Ext.getCmp('q_deptId').getValue());
										s.setBaseParam("ym", Ext.isEmpty(Ext.getCmp('q_ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('q_ym').getValue(), 'Y-m'));
									},
									load : function(s) {
										if (!s.getCount() || s.getAt(0).data.classCode != '休')
											s.insert(0, new s.recordType({
												classCode : '休',
												classCode : '休'
											}));
										
										s.insert(1, new s.recordType({
											classCode : 'OFF',
											classCode : 'OFF'
										}));
										
										s.insert(2, new s.recordType({
											classCode : 'SW',
											classCode : 'SW'
										}));
									}
								}
							})
						} ]
					} ]
				}

				]

			});
			formAddWin.show();
		}
	});
	
	// ----------------修改------------------------------//
	var btnEdit = new Ext.Button({
		text : "修改",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var selectionModel = gridView.getSelectionModel();
			var node = treePanel.getSelectionModel ().getSelectedNode();
			if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能修改区部以下的网点的数据！");
				return;
			}
			if (!selectionModel.hasSelection()) {
				Ext.Msg.alert('提示', '请选择要修改的记录!');
				return;
			}
			_Store.add(selectionModel.getSelections());
			var formUpdateWin = Ext.getCmp("formUpdateWin");
			if (Ext.isEmpty(formUpdateWin, false)) {
				var formUpdateWin = new Ext.Window({
					id : "formUpdateWin",
					closable : true,
					title : '修改',
					width : 600,
					height : 500,
					modal : true,
					border : false,
					bodyBorder : false,
					closable : true,
					resizable : false,
					layout : 'fit',
					closeAction : 'hide',
					listeners : {
						hide : function() {
							_Store.removeAll();
							if (Ext.getCmp('datepicker_update').statusDays.length > 0) {
								Ext.getCmp('datepicker_update').statusDays.length = 0;
							}
							this.items.each(function(item, i) {
								this.remove(this.getComponent(i), true);
							});
						}
					}
				});
			}
			formUpdateWin.add({
				xtype : "form",
				layout : 'column',
				frame : true,
				id : "updateForm",
				labelAlign : "right",
				labelWidth : 50,
				width : 600,
				height : 500,
				defaults : {
					width : 300,
					height : 450
				},
				tbar : [ {
					text : '保存',
					pressed : true,// 保存按钮
					minWidth : 60,
					handler : function() {
						if (!checkboxSelectionModel2.hasSelection()) {
							Ext.Msg.alert('提示', '请先选择一个或多个人员！');
							return;
						}
						if (Ext.getCmp('datepicker_update').statusDays.length == 0) {
							Ext.Msg.alert('提示', '请先选择一个或多个日期！');
							return;
						}
						// 获取班次代码
						var classCode = Ext.getCmp('className_update').getValue();
						if (Ext.isEmpty(classCode)) {
							Ext.Msg.alert('提示', '请选择班别代码！');
							return;
						}
						// 获取日期数组selDateArr
						var sel_dt = Ext.getCmp('datepicker_update').statusDays.join(',');
						// 获取人员列表
						var empArr = checkboxSelectionModel2.getSelections();
						var empStrArr = [];
						for ( var i = 0; i < empArr.length; i++) {
							empStrArr.push(empArr[i].get('empCode'));
						}
						var empCodes = empStrArr.join(',');
						beforeSaveUpdateValid('updateForm', formUpdateWin, sel_dt, empCodes, classCode, 'sechMgt_update.action');

					}
				}, '-', {
					text : '取消',
					pressed : true,// 取消按钮
					minWidth : 60,
					handler : function() {
						formUpdateWin.hide();
					}
				} ],
				items : [ {
					columnWidth : .46,
					items : new Ext.form.FieldSet({
						height : 420,
						title : "人员选择",
						items : [ new Ext.grid.GridPanel({
							enableHdMenu : false,
							buttonAlign : 'center',
							id : 'empGird_update',
							fbar : [ {
								xtype : 'button',
								text : '添加',
								handler : function() {
									showEmpWin();
								}
							}, {
								xtype : 'button',
								text : '清空',
								handler : function() {
									_Store.removeAll();
								}
							} ],
							frame : true,
							width : 240,
							height : 390,
							loadMask : true,
							columnLines : true,
							region : "center",
							store : _Store,
							sm : checkboxSelectionModel2,
							cm : new Ext.grid.ColumnModel([ checkboxSelectionModel2, {
								header : '工号',
								dataIndex : 'empCode',
								align : 'center',
								width : 90
							}, {
								header : '姓名',
								dataIndex : 'empName',
								align : 'center',
								width : 90
							} ])
						}) ]
					})
				}, {
					columnWidth : .1
				}, {
					columnWidth : .4,
					items : [ new Ext.form.FieldSet({
						height : 245,
						title : "日期选择",
						items : [ new Ext.ux.DatePicker({
							id : 'datepicker_update',
							style : 'margin-left:17px;',
							minDate : Date.parseDate(gridView.getSelectionModel().getSelected().get('ym') + '-01', 'Y-m-d').getFirstDateOfMonth(),
							maxDate : Date.parseDate(gridView.getSelectionModel().getSelected().get('ym') + '-01', 'Y-m-d').getLastDateOfMonth(),
							showToday : false,
							listeners : {
								render : function() {
									var ym = gridView.getSelectionModel().getSelected().get('ym');
									var selectedMonth = Date.parseDate(ym + '-01', 'Y-m-d');
									var selectedday = Date.parseDate(ym + '-07', 'Y-m-d');
									var today = new Date(moment().add(1, 'days').format("YYYY-MM-DD"));
									var minDate = today;
									//如果有修改历史7天权限，或者 修改当前月之后的数据
									if(!Ext.isEmpty(modifyHistorySchedulingPermission)){
										if(!moment(selectedday).isAfter(moment())){
											minDate = new Date(moment().add(-6, 'days').format("YYYY-MM-DD"));
										} else {
											minDate = selectedMonth;
										}
									}
									//如果有修改历史整月天权限，或者 修改当前月之后的数据
									if(!Ext.isEmpty(historyMonthModifySchedulePermission)){
										minDate = selectedMonth;
									}
						
									if(moment(selectedMonth).isAfter(moment())) {
										minDate = selectedMonth;
									}
									
									this.setMinDate(minDate);
									this.update(selectedMonth, true);
								}
							}
						}) ]
					}), {
						layout : 'form',
						labelAlign : 'left',
						style : 'margin-right:10px',
						items : [ {
							xtype : 'combo',
							fieldLabel : "班别代码",
							labelStyle : 'width:auto;',
							width : 150,
							mode : 'remote',
							id : "className_update",
							displayField : "classCode",
							valueField : "classCode",
							typeAhead : true,
							selectOnFocus : true,
							triggerAction : "all",
							minChars : 0,
							handleHeight : 8,
							// pageSize :10,
							resizable : true,
							forceSelection : true,
							lastQuery : '',
							allQuery : '',
							editable : false,
							listWidth : 250,
							value : null,
							allowBlank : false,
							emptyText : '请选择',
							listeners : {
								beforequery : function(qe) {
									var deptCode = Ext.getCmp('q_deptCode').getValue();
									qe.combo.getStore().baseParams['deptId'] = (deptCode == '001' ? null : Ext.getCmp('q_deptId').getValue());
									delete qe.combo.lastQuery;
									return deptCode ? true : false;
								}
							},
							store : new Ext.data.JsonStore({
								pruneModifiedRecords : true,
								root : "root",
								totalProperty : "totalSize",
								url : "../operation/schedule_querySchedule.action",
								fields : [ {
									name : 'classCode',
									mapping : 'SCHEDULE_CODE'
								}, {
									name : 'classCode',
									mapping : 'SCHEDULE_CODE'
								} ],
								listeners : {
									beforeload : function(s, o) {
										s.setBaseParam("deptId", Ext.getCmp('q_deptId').getValue());
										s.setBaseParam("ym", Ext.isEmpty(Ext.getCmp('q_ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('q_ym').getValue(), 'Y-m'));
									},
									load : function(s) {
										if (!s.getCount() || s.getAt(0).data.classCode != '休')
											s.insert(0, new s.recordType({
												classCode : '休',
												classCode : '休'
											}));
										
										s.insert(1, new s.recordType({
											classCode : 'OFF',
											classCode : 'OFF'
										}));
										
										s.insert(2, new s.recordType({
											classCode : 'SW',
											classCode : 'SW'
										}));
									}
								}
							})
						} ]
					} ]
				} ]

			});
			formUpdateWin.show();

		}
	});
	// ----------------导出------------------------------//
	var btnExport = new Ext.Button({
		text : "导出",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var deptId = Ext.getCmp("q_deptId").getValue();
			if (Ext.isEmpty(deptId)) {
				Ext.Msg.alert('提示', '请选择网点代码');
				return false;
			}

			queryView.getForm().submit({
				url : "sechMgt_export.action",
				timeout : 15 * 60,
				waitTitle : '请稍后',
				waitMsg : '正在执行操作...',
				params : {
					'dto.deptId' : Ext.getCmp('q_deptId').getValue(),
					'dto.status' : Ext.getCmp('q_status').getValue(),
					'dto.empName' : Ext.getCmp('q_empName').getValue(),
					'dto.empCode' : Ext.getCmp('q_empCode').getValue(),
					'dto.teamId' : Ext.getCmp('q_groupId').getValue(),
					'dto.ym' : Ext.isEmpty(Ext.getCmp('q_ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('q_ym').getValue(), 'Y-m'),
					'dto.className' : Ext.getCmp('q_className').getValue()
				},

				method : "post",
				success : function(form, action) {
					if (action.result.errorMsg) {
						Ext.Msg.alert('提示', action.result.msg);
						return false;
					}
					window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(action.result.fileName));
				}
			});
		}
	});

	// ----------------导出------------------------------//
	var btnConfirmExport = new Ext.Button({
		text : "导出已确认提交信息",
		pressed : true,
		minWidth : 60,
		handler : function() {
			queryView.getForm().submit({
				url : "sechMgt_btnConfirmExport.action",
				timeout : 15 * 60,
				waitTitle : '请稍后',
				waitMsg : '正在执行操作...',
				method : "post",
				success : function(form, action) {
					if (action.result.msg) {
						Ext.Msg.alert('提示', action.result.msg);
						return false;
					}
					window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(action.result.fileName));
				}
			});
		}
	});
	// ----------------导出未排班信息------------------------------//
	var btnExportNotschedul = new Ext.Button({
		text : "导出未排班员工信息",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var deptId =Ext.getCmp("q_deptId").getValue();
			if(Ext.isEmpty(deptId)){
				Ext.Msg.alert('提示', '请选择网点');
				return false;
			}
			var ym = Ext.getCmp("q_ym").getValue();
			if (Ext.isEmpty(ym)) {
				Ext.Msg.alert('提示', '请选择月份');
				return false;
			}
			queryView.getForm().submit({
				url : "sechMgt_exportNotScheduling.action",
				timeout : 15 * 60,
				waitTitle : '请稍后',
				waitMsg : '正在执行操作...',
				method : "post",
				params : {
					'dto.deptId' : deptId,
					'dto.ym' : ym
				},
				success : function(form, action) {
					if (action.result.msg) {
						Ext.Msg.alert('提示', action.result.msg);
						return false;
					}
					window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(action.result.fileName));
				}
			});
		}
	});
	// ----------------导入------------------------------//
	var btnImport = new Ext.Button({
		text : "导入",
		pressed : true,
		minWidth : 60,
		handler : function() {			
			var node = treePanel.getSelectionModel ().getSelectedNode();
			if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能导入区部以下的网点的数据！");
				return false;
			}
			uploadWindow.show();
			Ext.resetFileInput('uploadFile');
		}
	});

	// ----------------提交确认------------------------------//
	var btnConfirm = new Ext.Button({
		text : "提交确认",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var node = treePanel.getSelectionModel ().getSelectedNode();
			if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能提交确认区部以下的网点的数据！");
				return false;
			}
			if (Ext.isEmpty(Ext.getCmp("q_ym").getValue())) {
				Ext.Msg.alert('提示', '请选择月份');
				return false;
			}

			Ext.Msg.confirm('提示', '确认提交吗？', function(b) {
				if ("no" == b) {
					return false;
				} else {
					Ext.Ajax.request({
						timeout : 120*1000,
						url : 'sechMgt_commitConfirm.action',
						params : {
							'dto.deptId' : Ext.getCmp("q_deptId").getValue(),
							'dto.ym' : Ext.isEmpty(Ext.getCmp('q_ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('q_ym').getValue(), 'Y-m')
						},
						success : function(response, config) {
							var rep = Ext.decode(response.responseText);
							if (Ext.isEmpty(rep.msg)) {
								Ext.Msg.alert('提示', '确认成功！');
							} else {
								Ext.Msg.alert('提示', rep.msg);
							}
						}
					});
				}
			});

		}
	});
	// ---------------删除------------------------------//
	var btndelete = new Ext.Button({
		text : "删除",
		pressed : true,
		minWidth : 60,
		handler : function() {
			var node = treePanel.getSelectionModel ().getSelectedNode();
			if(node!=null && filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能删除区部以下的网点的数据！");
				return;
			}
			if (gridView.getSelectionModel().getSelections().length < 1) {
				Ext.MessageBox.alert("提示", "请选择要删除的数据");
				return;
			}
			var records = gridView.getSelectionModel().getSelections();
			var schedulingIds = new Array();
			for ( var i = 0; i < records.length; i++) {
				schedulingIds.push(records[i].data.id);
			}

			Ext.Msg.confirm('提示', '确认删除吗？', function(b) {
				if ("no" == b) {
					return false;
				} else {
					Ext.Ajax.request({
						params : {
							'schedulingIds' : schedulingIds.toString(),
							'dto.deptId' : Ext.getCmp("q_deptId").getValue()
						},
						timeout : 3000,
						url : 'sechMgt_delete.action',
						success : function(response, config) {
							var rep = Ext.decode(response.responseText);
							if (Ext.isEmpty(rep.msg)) {
								Ext.Msg.alert('提示', '删除成功！');
								gridView.getStore().load();
							} else {
								Ext.Msg.alert('提示', '删除失败！');
							}
						}
					});
				}
			});

		}
	});

	
	var tbar = [];

	addBar('<app:isPermission code = "/schedulingMgt/search.action">a</app:isPermission>', btnSearch);
	addBar('<app:isPermission code = "/schedulingMgt/add.action">a</app:isPermission>', btnAdd);
	addBar('<app:isPermission code = "/schedulingMgt/edit.action">a</app:isPermission>', btnEdit);
	addBar('<app:isPermission code = "/schedulingMgt/export.action">a</app:isPermission>', btnExport);
	addBar('<app:isPermission code = "/schedulingMgt/import.action">a</app:isPermission>', btnImport);
	addBar('<app:isPermission code = "/schedulingMgt/confirm.action">a</app:isPermission>', btnConfirm);
	addBar('<app:isPermission code = "/schedulingMgt/delete.action">a</app:isPermission>', btndelete);
	addBar('<app:isPermission code = "/schedulingMgt/confirmExport.action">a</app:isPermission>', btnConfirmExport);
	addBar('<app:isPermission code = "/schedulingMgt/exportNotschedul.action">a</app:isPermission>', btnExportNotschedul);
	

	function addBar(permisson, button) {
		if (!Ext.isEmpty(permisson)) {
			tbar.push('-');
			tbar.push(button);
		}
	}
	
	// ----------------布局------------------------------//
	var viewreprot = new Ext.Viewport({
		layout : "border",
		items : [ treePanel, new Ext.Panel({
			layout : "border",
			region : "center",
			tbar : tbar,
			items : [ queryView, gridView ]
		}) ]
	});
	
	
});
// 通知界面
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
	html : '<font color=red>请尽快完成下一个月的预排班！</font>'
});

var task = {
	run : function() {
		noticeStore.load({
			callback : function(r, op, success) {
				if (!Ext.isEmpty(todayDt)) {
					var lastDt = todayDt.getLastDateOfMonth();
					if ((todayDt > lastDt || lastDt.add(Date.DAY, -7) > todayDt) && r[0].get('flag')) {
						noticeWindow.hide();
					} else if (noticeWindow.hidden && lastDt.add(Date.DAY, -7) <= todayDt && todayDt <= lastDt && !r[0].get('flag')) {
						noticeWindow.show();
						noticeWindow.setPosition(document.body.clientWidth - 250, document.body.clientHeight - 120 - 5);
					}
				}
			}
		});
	},
	interval : 300000
};
var runner = new Ext.util.TaskRunner();
runner.start(task);
// //////////////////////
var recordIds = new Array();// 选中的Record主键列id列表
var recordsChecked = new Array();// 选中的Record列表

var confirm_sm = new Ext.grid.CheckboxSelectionModel({
	singleSelect : false
});


var confirm_Store = new Ext.data.Store({
	reader : new Ext.data.ArrayReader({
	/*
	 * totalProperty : '1', root : 'schedulMgts'
	 */
	}, [ {
		name : 'id',
		mapping : 'id'
	}, {
		name : 'deptId',
		mapping : 'deptId'
	}, {
		name : 'areaCode',
		mapping : 'areaCode'
	}, {
		name : 'deptCode',
		mapping : 'deptCode'
	}, {
		name : 'ym',
		mapping : 'ym'
	}, {
		name : 'empCode',
		mapping : 'empCode'
	}, {
		name : 'empName',
		mapping : 'empName'
	}, {
		name : 'workTypeStr',
		mapping : 'workTypeStr'
	}, {
		name : 'day1',
		mapping : 'day1'
	}, {
		name : 'day2',
		mapping : 'day2'
	}, {
		name : 'day3',
		mapping : 'day3'
	}, {
		name : 'day4',
		mapping : 'day4'
	}, {
		name : 'day5',
		mapping : 'day5'
	}, {
		name : 'day6',
		mapping : 'day6'
	}, {
		name : 'day7',
		mapping : 'day7'
	}, {
		name : 'day8',
		mapping : 'day8'
	}, {
		name : 'day9',
		mapping : 'day9'
	}, {
		name : 'day10',
		mapping : 'day10'
	}, {
		name : 'day11',
		mapping : 'day11'
	}, {
		name : 'day12',
		mapping : 'day12'
	}, {
		name : 'day13',
		mapping : 'day13'
	}, {
		name : 'day14',
		mapping : 'day14'
	}, {
		name : 'day15',
		mapping : 'day15'
	}, {
		name : 'day16',
		mapping : 'day16'
	}, {
		name : 'day17',
		mapping : 'day17'
	}, {
		name : 'day18',
		mapping : 'day18'
	}, {
		name : 'day19',
		mapping : 'day19'
	}, {
		name : 'day20',
		mapping : 'day20'
	}, {
		name : 'day21',
		mapping : 'day21'
	}, {
		name : 'day22',
		mapping : 'day22'
	}, {
		name : 'day23',
		mapping : 'day23'
	}, {
		name : 'day24',
		mapping : 'day24'
	}, {
		name : 'day25',
		mapping : 'day25'
	}, {
		name : 'day26',
		mapping : 'day26'
	}, {
		name : 'day27',
		mapping : 'day27'
	}, {
		name : 'day28',
		mapping : 'day28'
	}, {
		name : 'day29',
		mapping : 'day29'
	}, {
		name : 'day30',
		mapping : 'day30'
	}, {
		name : 'day31',
		mapping : 'day31'
	}, {
		name : 'errorDesc',
		mapping : 'errorDesc'
	}, {
		name : 'version',
		mapping : 'version'
	}, {
		name : 'workType',
		mapping : 'workType'
	} ]),
	pruneModifiedRecords : true
});
var pbar = new Ext.PagingToolbar({
	displayInfo : true,
	store : confirm_Store,
	pageSize : 1000,
	displayMsg : "当前显示 {0} - {1} 总记录数目 {2}",
	emptyMsg : "未检索到数据"
});

var confirm_gridView = new Ext.grid.GridPanel({
	enableHdMenu : false,
	frame : true,
	loadMask : true,
	columnLines : true,
	region : "center",
	store : confirm_Store,
	tbar : pbar,
	sm : confirm_sm,
	cm : new Ext.grid.ColumnModel([ confirm_sm, {
		header : '描述',
		dataIndex : 'errorDesc',
		align : 'center',
		width : 120
	}, {
		header : '年月',
		dataIndex : 'ym',
		align : 'center',
		width : 90
	}, {
		header : '地区代码',
		dataIndex : 'areaCode',
		align : 'center',
		width : 90
	}, {
		header : '网点代码',
		dataIndex : 'deptCode',
		align : 'center',
		width : 90
	}, {
		header : '工号',
		dataIndex : 'empCode',
		align : 'center',
		width : 90
	}, {
		header : '姓名',
		dataIndex : 'empName',
		align : 'center',
		width : 90
	}, {
		header : '用工类型',
		dataIndex : 'workTypeStr',
		align : 'center',
		width : 90
	}, {
		header : '1',
		dataIndex : 'day1',
		align : 'center',
		width : 30
	}, {
		header : '2',
		dataIndex : 'day2',
		align : 'center',
		width : 30
	}, {
		header : '3',
		dataIndex : 'day3',
		align : 'center',
		width : 30
	}, {
		header : '4',
		dataIndex : 'day4',
		align : 'center',
		width : 30
	}, {
		header : '5',
		dataIndex : 'day5',
		align : 'center',
		width : 30
	}, {
		header : '6',
		dataIndex : 'day6',
		align : 'center',
		width : 30
	}, {
		header : '7',
		dataIndex : 'day7',
		align : 'center',
		width : 30
	}, {
		header : '8',
		dataIndex : 'day8',
		align : 'center',
		width : 30
	}, {
		header : '9',
		dataIndex : 'day9',
		align : 'center',
		width : 30
	}, {
		header : '10',
		dataIndex : 'day10',
		align : 'center',
		width : 30
	}, {
		header : '11',
		dataIndex : 'day11',
		align : 'center',
		width : 30
	}, {
		header : '12',
		dataIndex : 'day12',
		align : 'center',
		width : 30
	}, {
		header : '13',
		dataIndex : 'day13',
		align : 'center',
		width : 30
	}, {
		header : '14',
		dataIndex : 'day14',
		align : 'center',
		width : 30
	}, {
		header : '15',
		dataIndex : 'day15',
		align : 'center',
		width : 30
	}, {
		header : '16',
		dataIndex : 'day16',
		align : 'center',
		width : 30
	}, {
		header : '17',
		dataIndex : 'day17',
		align : 'center',
		width : 30
	}, {
		header : '18',
		dataIndex : 'day18',
		align : 'center',
		width : 30
	}, {
		header : '19',
		dataIndex : 'day19',
		align : 'center',
		width : 30
	}, {
		header : '20',
		dataIndex : 'day20',
		align : 'center',
		width : 30
	}, {
		header : '21',
		dataIndex : 'day21',
		align : 'center',
		width : 30
	}, {
		header : '22',
		dataIndex : 'day22',
		align : 'center',
		width : 30
	}, {
		header : '23',
		dataIndex : 'day23',
		align : 'center',
		width : 30
	}, {
		header : '24',
		dataIndex : 'day24',
		align : 'center',
		width : 30
	}, {
		header : '25',
		dataIndex : 'day25',
		align : 'center',
		width : 30
	}, {
		header : '26',
		dataIndex : 'day26',
		align : 'center',
		width : 30
	}, {
		header : '27',
		dataIndex : 'day27',
		align : 'center',
		width : 30
	}, {
		header : '28',
		dataIndex : 'day28',
		align : 'center',
		width : 30
	}, {
		header : '29',
		dataIndex : 'day29',
		align : 'center',
		width : 30
	}, {
		header : '30',
		dataIndex : 'day30',
		align : 'center',
		width : 30
	}, {
		header : '31',
		dataIndex : 'day31',
		align : 'center',
		width : 30
	} , {
		header : 'workType',
		dataIndex : 'workType',
		align : 'center',
		width : 30
	} ])
});


confirm_gridView.on("render",function(){     
	confirm_gridView.getSelectionModel().selectAll();   
},this,{delay:300});  

function beforeSaveUpdateValid(fm, win, sel_dt, empCodes, classCode, action_url) {
	Ext.Ajax.request({
		timeout : 3000,
		params : {
			'dto.deptId' : Ext.getCmp('q_deptId').getValue(),
			'dto.sheduleDts' : sel_dt,
			'dto.empCodes' : empCodes,
			'dto.sheduleCode' : classCode
		},
		url : 'sechMgt_checkSaveValid.action',
		success : function(response, config) {
			var res = Ext.decode(response.responseText);
			if (!Ext.isEmpty(res.msg)) {
				Ext.Msg.alert('提示', res.msg);
				return;
			}
			if (res.mp.flag1) {
				Ext.Msg.confirm('提示', res.mp.msg1 + ',确认继续吗？', function(b) {
					if ("no" == b) {
						return false;
					} else {
						Ext.getCmp(fm).getForm().submit({
							timeout : 15 * 60,
							url : action_url,
							params : {
								'dto.deptId' : Ext.getCmp('q_deptId').getValue(),
								'dto.sheduleDts' : sel_dt,
								'dto.empCodes' : empCodes,
								'dto.sheduleCode' : classCode
							},
							// clientValidation:true,
							method : "post",
							success : function(form, action) {
								if (action.result.msg) {
									Ext.Msg.alert('提示', action.result.msg);
									return false;
								} else {
									Ext.Msg.alert('提示', '保存成功！');
									gridView.store.load();
									win.hide();
									gridView.getSelectionModel().clearSelections();
								}

							}
						});
					}
				});

			} else if (res.mp.flag2) {
				Ext.Msg.confirm('提示', res.mp.msg2 + ',确认继续吗？', function(b) {
					if ("no" == b) {
						return false;
					} else {
						Ext.getCmp(fm).getForm().submit({
							timeout : 15 * 60,
							url : action_url,
							params : {
								'dto.deptId' : Ext.getCmp('q_deptId').getValue(),
								'dto.sheduleDts' : sel_dt,
								'dto.empCodes' : empCodes,
								'dto.sheduleCode' : classCode
							},
							// clientValidation:true,
							method : "post",
							success : function(form, action) {
								if (action.result.msg) {
									Ext.Msg.alert('提示', action.result.msg);
									return false;
								}
								Ext.Msg.alert('提示', '保存成功！');
								gridView.store.load();
								win.hide();
								gridView.getSelectionModel().clearSelections();
							}
						});
					}
				});
			} else {
				Ext.getCmp(fm).getForm().submit({
					timeout : 15 * 60,
					url : action_url,
					waitTitle : '请稍后',
					waitMsg : '正在执行操作...',
					params : {
						'dto.deptId' : Ext.getCmp('q_deptId').getValue(),
						'dto.sheduleDts' : sel_dt,
						'dto.empCodes' : empCodes,
						'dto.sheduleCode' : classCode
					},
					// clientValidation:true,
					method : "post",
					success : function(form, action) {
						if (action.result.msg) {
							Ext.Msg.alert('提示', action.result.msg);
							return false;
						}
						Ext.Msg.alert('提示', '保存成功！');
						gridView.store.load();
						win.hide();
						gridView.getSelectionModel().clearSelections();
					}
				});
			}
		}
	});

}