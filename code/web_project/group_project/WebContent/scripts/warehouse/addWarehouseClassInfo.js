//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var isUpdate;

function compareDate(d1, d2) {
	var date_start = new Date(d1);
	var date_end = new Date(d2);
	return date_start - date_end;
}

function getMyDate(tmpDate) {
	var date1, date2;
	// 定义两个变量
	date1 = tmpDate.getMonth() + 1 + "";
	// 获取当前月份+1的值
	if (date1.length < 2)
		// 判断当前月份是否是双位数，10以上
		date1 = "0" + date1;
	// 单位数的情况下，需要在月份前补0
	date2 = tmpDate.getDate() + "";
	// 获取当前日期
	if (date2.length < 2)
		// 判断日期的位数是否是双位
		date2 = "0" + date2;
	// 不足双位补0
	return tmpDate.getFullYear() + "/" + date1 + "/" + date2;
	// 返回完整的日期
}

var classFromPanel = new Ext.FormPanel({
	id : 'classFromPanel',
	labelAlign : 'left',
	buttonAlign : 'right',
	frame : true,
	layout : 'column',
	monitorValid : true,
	items : [ {
		layout : 'form',
		defaults : {
			width : 115
		},
		columnWidth : .42,
		items : [ {
			name : 'schedule_id',
			id : 'schedule_id',
			xtype : "hidden"
		}, {
			name : 'dept_id',
			id : 'dept_id',
			xtype : "textfield",
			allowBlank : false,
			fieldLabel : "网点代码",
			readOnly : true
		}, {
			xtype : 'timefield',
			id : 'start1_time',
			name : 'start1_time',
			allowBlank : false,
			increment : 30,
			format : 'G:i',
			fieldLabel : "上班时间"
		}, {
			xtype : "datefield",
			name : 'enable_dt',
			id : 'enable_dt',
			allowBlank : false,
			editable : false,
			format : 'Y-m-d',
			fieldLabel : "生效日期"
		} ]
	}, {
		layout : 'form',
		defaults : {
			width : 115
		},
		columnWidth : .42,
		items : [ {
			name : 'schedule_code',
			id : 'schedule_code',
			maxLength : 2,
			regex : /^[A-Z][0-9]*$/,
			regexText : '只能是1位的大写英文字母或1位大写英文字母加1位数字!',
			xtype : "textfield",
			allowBlank : false,
			fieldLabel : "班别代码"
		}, {
			xtype : "timefield",
			id : 'end1_time',
			name : 'end1_time',
			allowBlank : false,
			increment : 30,
			format : 'G:i',
			fieldLabel : "下班时间"
		}, {
			xtype : "datefield",
			name : 'disable_dt',
			id : 'disable_dt',
			allowBlank : false,
			editable : false,
			format : 'Y-m-d',
			fieldLabel : "失效日期"
		} ]
	}, {
		layout : 'form',
		columnWidth : .16,
		items : [ {
			xtype : "checkbox",
			id : 'is_cross_day',
			name : 'is_cross_day',
			style : {
				marginLeft : '-45px',
				marginTop : '33px'
			},
			labelStyle : 'width:60px; margin-Top: 28',
			fieldLabel : "是否跨天"
		} ]
	} ]
});

var classBaiscInfoPane = new Ext.Panel({
	id : 'classBaiscInfoPane',
	frame : true,
	region : 'center',
	items : [ classFromPanel ]
});

var add_class_info_window = new Ext.Window({
	title : '新增网点班别',
	width : 600,
	height : 200,
	closeAction : 'hide',
	layout : 'border',
	items : [ classBaiscInfoPane ],
	buttons : [ {
		id : 'savaButton',
		text : "保存",
		handler : function() {
			var isValid = Ext.getCmp('classFromPanel').form.isValid();
			if (!isValid || !validata())
				return false;
			if (isUpdate) {
				updateClassInfo();
				return false;
			}
			addClassInfo();
		}
	}, {
		id : 'resetButton',
		text : "重置",
		handler : function() {
			if (isUpdate) {
				add_class_info_window.hide();
			} else {
				var dept_id = Ext.getCmp("dept_id").getValue();
				Ext.getCmp('classFromPanel').form.reset();
				Ext.getCmp("dept_id").setValue(dept_id);
			}
		}
	} ]
});

function addClassInfo() {
	var is_cross_day = Ext.getCmp("is_cross_day").getValue();
	if (is_cross_day) {
		is_cross_day = '1';
	} else {
		is_cross_day = '0';
	}
	Ext.getCmp('classFromPanel').form.submit({
		url : 'add_warehouseClass.action',
		waitMsg : '正在提交数据',
		waitTitle : '提示',
		method : "POST",
		timeout : 30000,
		params : {
			is_cross_day : is_cross_day
		},
		success : function(form, action) {
			Ext.Msg.alert('提示', "新增成功！");
			store.reload({
				params : {
					start : 0,
					limit : 20
				}
			});
			add_class_info_window.hide();
		},
		failure : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);// 就可以取出来。如果是数组，那么很简单
			Ext.Msg.alert('提示', "新增失败！" + result.error);
		}
	});
}

function updateClassInfo() {
	var is_cross_day = Ext.getCmp("is_cross_day").getValue();
	if (is_cross_day) {
		is_cross_day = '1';
	} else {
		is_cross_day = '0';
	}
	Ext.getCmp('classFromPanel').form.submit({
		url : 'update_warehouseClass.action',
		waitMsg : '正在提交数据',
		waitTitle : '提示',
		method : "POST",
		timeout : 30000,
		params : {
			is_cross_day : is_cross_day
		},
		success : function(form, action) {
			Ext.Msg.alert('提示', "修改成功！");
			store.reload({
				params : {
					start : 0,
					limit : 20
				}
			});
			add_class_info_window.hide();
		},
		failure : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);// 就可以取出来。如果是数组，那么很简单
			Ext.Msg.alert('提示', "修改失败！" + result.error);
		}
	});
}

function validata() {

	var enable_dt = Ext.getCmp("enable_dt").getValue().format("Y/m/d");
	var disable_dt = Ext.getCmp("disable_dt").getValue().format("Y/m/d");
	var today = getMyDate(new Date());
	if ((new Date(disable_dt) - new Date(today)) < 0) {
		Ext.Msg.alert('提示', "失效日期需大于等于今天!");
		return false;
	}

	var i = compareDate(enable_dt, disable_dt);
	if (i >= 0) {
		Ext.Msg.alert('提示', "生效日期必须小于失效日期!");
		return false;
	}

	var is_cross_day = Ext.getCmp("is_cross_day").getValue();
	var start1_time = Ext.getCmp("start1_time").getValue();
	var end1_time = Ext.getCmp("end1_time").getValue();
	start1_time = '2014/12/24 ' + start1_time;
	end1_time = '2014/12/24 ' + end1_time;
	i = compareDate(start1_time, end1_time);

	if (!is_cross_day) {
		if (i >= 0) {
			Ext.Msg.alert('提示', "上班时间不能大于下班时间!");
			return false;
		}
	}

	return true;

}

var btnAddClassInfo = new Ext.Button({
	text : "新增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		isUpdate = false;
		if (Ext.getCmp("query.branchCode").getValue() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
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
					add_class_info_window.show();
					add_class_info_window.setTitle('新增网点班别');
					Ext.getCmp("resetButton").setText('重置');

					Ext.getCmp('classFromPanel').form.reset();
					Ext.getCmp("dept_id").setValue(Ext.getCmp("query.branchCode").getValue().split(',')[0]);
					Ext.getCmp("dept_id").enable();
					Ext.getCmp("start1_time").setReadOnly(false);
					Ext.getCmp("end1_time").setReadOnly(false);
					Ext.getCmp("schedule_code").enable();
					Ext.getCmp("enable_dt").enable();
					document.getElementById("is_cross_day").disabled = false;
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});

	}
});

var btnUpdateClassInfo = new Ext.Button({
	text : "修改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function() {
		isUpdate = true;
		var records = grid.getSelectionModel().getSelections();
		if (records.length > 1) {
			Ext.Msg.alert("提示", "只能选择一条数据!");
			return false;
		}
		if (records.length < 1) {
			Ext.Msg.alert("提示", "请能选择一条数据!");
			return false;
		}
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
					add_class_info_window.show();
					add_class_info_window.setTitle('修改网点班别');
					Ext.getCmp("resetButton").setText('取消');

					var record = grid.getSelectionModel().getSelected();

					Ext.getCmp("schedule_id").setValue(record.data['SCHEDULE_ID']);

					Ext.getCmp("schedule_code").setValue(record.data['SCHEDULE_CODE']);
					Ext.getCmp("schedule_code").disable();

					Ext.getCmp("start1_time").setValue(record.data['START1_TIME']);
					Ext.getCmp("start1_time").setReadOnly(true);

					Ext.getCmp("end1_time").setValue(record.data['END1_TIME']);
					Ext.getCmp("end1_time").setReadOnly(true);

					Ext.getCmp("enable_dt").setValue(record.data['ENABLE_DT']);
					Ext.getCmp("enable_dt").disable();

					Ext.getCmp("disable_dt").setValue(record.data['DISABLE_DT']);

					Ext.getCmp("dept_id").setValue(record.data['DEPT_CODE']);
					Ext.getCmp("dept_id").disable();

					if (record.data['IS_CROSS_DAY'] == '是') {
						document.getElementById("is_cross_day").checked = true;
					}
					if (record.data['IS_CROSS_DAY'] == '否') {
						document.getElementById("is_cross_day").checked = false;
					}
					document.getElementById("is_cross_day").disabled = true;
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});
