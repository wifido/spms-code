// <%@ page language="java" contentType="text/html; charset=utf-8"%>

var filterDeptCodeType = '${filterDeptCodeType}';

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
				Ext.getCmp('query.branchCode').setValue(dept_.deptCode);
				
			} else {
				Ext.Msg.alert('提示','该网点不存在！', function(){
					fieldDeptCode.selectText();
				}, this);
			}
		} ,scope : this
	}, false);
};


var treePanel = new Ext.tree.TreePanel({
    region: 'west',
    margins: '1 1 1 1',
    width: 245,
    title: '网点信息',
    collapsible: true,
    autoScroll: true,
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
    root: new Ext.tree.AsyncTreeNode({
        id: '0',
        text: '顺丰速运',
        loader: new Ext.tree.TreeLoader({
            dataUrl: "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
        })
    }),
    listeners: {
        beforeclick: function(node, e) {
            Ext.getCmp("query.branchCode").setValue(node.text.split("/")[0]);
        }
    }
});


var btnSearch = new Ext.Button({
	text: "查 询",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		queryTraining();
	}
});

var queryTraining = function() {
	var deptCode = Ext.getCmp('query.branchCode').getValue().trim();
	if (deptCode == "") {
		Ext.Msg.alert('提示', '网点代码不能为空！');
		return;
	}
	
	var monthId = Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m');
	if (monthId == "") {
		Ext.Msg.alert('提示', '月份不能为空！');
		return;
	} 
	store.setBaseParam("DEPT_CODE", deptCode);
	store.setBaseParam("EMP_CODE", Ext.getCmp('query.empCode').getValue().trim());
	store.setBaseParam("EMP_NAME", Ext.getCmp('query.empName').getValue().trim());
	store.setBaseParam("MONTH_ID", monthId);
	store.setBaseParam("POST_TYPE","1")
	store.load({
		params: {
			start: 0,
			limit: 20
		}
	});
};


var btnDelete = new Ext.Button({
	text: "删 除",
	cls: "x-btn-normal",
	pressed: true,
	minWidth: 60,
	handler: function() {
		deleteTraining();
	}
});

var btnImport = new Ext.Button({
	text : "导入",
	pressed : true,
	minWidth : 60,
	handler : function() {
		if (Ext.getCmp('query.branchCode').getValue().trim() == "") {
			Ext.Msg.alert("提示", "请先选择网点！");
			return;
		}
		
		var node = treePanel.getSelectionModel ().getSelectedNode();
		if(node!=null && (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)){
			deptId = node.id;
			nodeCode=node.attributes.code;
		}else{
			Ext.Msg.alert("提示","只有中转场和航空操作组才能导入培训信息！");
			return;
		}
		
		uploadWindow.show();
	}
});

var downTemplate = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=培训信息导入模板.xls&moduleName=training&entityName=training&isTemplate=true";
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
}

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
	{id : 'uploadForm',
	height : 160,
	width : 600,
	frame : true,
	fileUpload : true,
	border : false,
	items : [new Ext.Panel({
				layout : 'form',
				border : false,
				items : [ {
					xtype : 'hidden',
					id : 'upload_deptCode',
					name : "upload_deptCode"
				},{
					xtype : 'hidden',
					id : 'upload_postType',
					name : "upload_postType"
				}, uploadFileName, uploadFile ]
			}),
			new Ext.form.Label({
				html : '<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">模板文件:</font> <a href="#" onclick="downTemplate()">培训信息导入模板.xls</a>'
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
	tbar : [{
		text : '导入',
		pressed : true,
		height : 18,
		minWidth : 60,
		handler : function() {
			uploadFileName.setValue(uploadFile.getValue());
			Ext.getCmp('upload_deptCode').setValue(Ext.getCmp('query.branchCode').getValue().trim());
			Ext.getCmp('upload_postType').setValue("1");
			if (uploadFileName.getValue().indexOf(".xls") > 0) {
				uploadform.form
				.submit({
					waitMsg : '正在提交数据',
					waitTitle : '提示',
					method : "POST",
					timeout : 500000,
					url : "../training/importTraining.action",
					success : function(form, action) {
						var result = Ext.util.JSON
								.decode(action.response.responseText);
						var downloadUrl = '';
						if (!Ext.isEmpty(result.dataMap.errorFileName)) {
							downloadUrl = getDownloadPathWhenUploadFail(result.dataMap.errorFileName);
							downloadUrl = "  <a href = '#' url='" + downloadUrl
									+ "' onclick = 'downError(this)'>错误数据下载</a>";
						}
						Ext.MessageBox.alert("提示", result.dataMap.tips
								+ downloadUrl, function() {
							uploadWindow.hide();
							// querySchedule();
						});
					},
					failure : function(form, action) {
						var result = Ext.util.JSON
								.decode(action.response.responseText);
						Ext.Msg.alert('提示', "导入失败！" + result.error);
					}

				});
			}
		}
	}],
	items : [ uploadform ]
});

function getDownloadPathWhenUploadFail(errorFileName) {
	return '../common/downloadReportFile.action?'
			+ Ext.util.Format.htmlDecode(encodeURI(encodeURI(errorFileName)));
}

function downError(objectA) {
	window.location = objectA.attributes.url.nodeValue;
}

var deleteTraining = function() {
	var records = grid.getSelectionModel().getSelections();
	if (records.length > 0) {
		var ids = "";
		for ( var i = 0; i < records.length; i++) {
			var record = records[i];
			ids += record.json["ID"]+",";
		}
		if (ids) {
			Ext.Msg.confirm("提示", "是否确定删除该记录？", function(btn) {
				if (btn == "yes") {
					Ext.Ajax.request({
						url : '../training/training_delete.action',
						method : 'POST',
						params : {
							ids : ids
						},
						success : function(res, config) {
							var obj = Ext.decode(res.responseText);
							Ext.Msg.alert("提示", obj.msg);
							queryTraining();
						},
						failure : function(res, config) {
							var obj = Ext.decode(res.responseText);
							Ext.Msg.alert("提示", obj.msg);
						}
					});
				}
			}, this)
		}
	} else {
		Ext.Msg.alert("提示", "请选择要删除的数据！");
	}
}

var btnExport = new Ext.Button({
    text: '导出',
    cls: 'x-btn-normal',
    pressed: true,
    minWidth: 60,
    handler: function() {
        var departmentCode = Ext.getCmp('query.branchCode').getValue().trim();
        if (Ext.isEmpty(departmentCode)) {
            Ext.Msg.alert('提示', '网点代码不能为空！');
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
                    url: '../training/export.action',
                    timeout: 60000,
                    params: {
                    	DEPT_CODE: departmentCode,
                    	MONTH_ID: Ext.util.Format.date(Ext.getCmp('query.monthId').getValue(), 'Y-m'),
                        EMP_CODE: Ext.getCmp('query.empCode').getValue().trim(),
                        EMP_NAME: Ext.getCmp('query.empName').getValue().trim(),
                        POST_TYPE :1
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


var tbar = [];
addBar('<app:isPermission code="/training/training_query.action">a</app:isPermission>', btnSearch);
addBar('<app:isPermission code="/training/training_delete.action">a</app:isPermission>', btnDelete);
addBar('<app:isPermission code="/training/training_import.action">a</app:isPermission>', btnImport);
addBar('<app:isPermission code="/training/training_export.action">a</app:isPermission>', btnExport);
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
				xtype: "textfield",
				fieldLabel: '<font color=red>网点代码*</font>',
				id: "query.branchCode",
				anchor: '90%',
				readOnly: true
			}]
		},{
			columnWidth: .4,
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
				xtype: 'textfield',
				id: 'query.empName',
				typeAhead: true,
				triggerAction: 'all',
				lazyRender: true,
				mode: 'local',
				fieldLabel: '姓名',
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
	columns: [sm,new Ext.grid.RowNumberer({
		header: "序号",
		width: 40,
		renderer: function(value, metadata, record, rowIndex) {
			return record_start + 1 + rowIndex;
		}
	}),{
		header: '年月',
		sortable: true,
		dataIndex: 'YEARS_MONTH',
		align: "center",
		width:90
	},{
		header: '培训日期',
		sortable: true,
		dataIndex: 'DAY_OF_MONTH',
		align: "center",
		width:120
	},{
		header: '网点代码',
		sortable: true,
		dataIndex: 'DEPARTMENT_CODE',
		align: "center",
		width:100
	},{
		header: '工号',
		sortable: true,
		dataIndex: 'EMPLOYEE_CODE',
		align: "center",
		width:120
	},{
		header: '姓名',
		sortable: true,
		dataIndex: 'EMPLOYEE_NAME',
		align: "center",
		width:120
	},{
		header: '人员类型',
		sortable: true,
		dataIndex: 'WORK_TYPE',
		align: "center",
		width:120
	},{
		header: '在职状态',
		sortable: true,
		dataIndex: 'DIMISSION_DT',
		align: "center",
		width:120,
		renderer : function(value){
				if(value = 1)
					return "在职";
				return "离职";
		}
	},{
		header: '培训代码',
		sortable: true,
		dataIndex: 'TRAINING_CODE',
		align: "center",
		width:100
	},{
		header: '岗位',
		sortable: true,
		dataIndex: 'EMP_DUTY_NAME',
		align: "center",
		width:120
	},{
		header: '创建时间',
		sortable: true,
		dataIndex: 'CREATE_TM',
		align: "center",
		width:120,
		renderer: function(value) {
			return value.substring(0, 10);
		}
	},{
		header: 'ID',
		sortable: true,
		hidden: true,
		dataIndex: 'ID',
		align: "center",
		width:10
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
	name: 'ID',
	mapping: 'ID',
	type: 'string'
},{
	name: 'YEARS_MONTH',
	mapping: 'YEARS_MONTH',
	type: 'String'
},{
	name: 'DAY_OF_MONTH',
	mapping: 'DAY_OF_MONTH',
	type: 'string'
},{
	name: 'DEPARTMENT_CODE',
	mapping: 'DEPARTMENT_CODE',
	type: 'string'
},{
	name: 'EMPLOYEE_CODE',
	mapping: 'EMPLOYEE_CODE',
	type: 'string'
},{
	name: 'EMPLOYEE_NAME',
	mapping: 'EMPLOYEE_NAME',
	type: 'string'
},{
	name: 'WORK_TYPE',
	mapping: 'WORK_TYPE',
	type: 'string'
},{
	name: 'DIMISSION_DT',
	mapping: 'DIMISSION_DT',
	type: 'string'
},{
	name: 'TRAINING_CODE',
	mapping: 'TRAINING_CODE',
	type: 'string'
},{
	name: 'EMP_DUTY_NAME',
	mapping: 'EMP_DUTY_NAME',
	type: 'string'
},{
	name: 'CREATE_TM',
	mapping: 'CREATE_TM',
	type: 'string'
},{
	name: 'ID',
	mapping: 'ID',
	type: 'string'
}]);

// 构建数据存储Store
var store = new Ext.data.Store({
	proxy: new Ext.data.HttpProxy({
		url: '../training/training_query.action'
	}),
	reader: new Ext.data.JsonReader({
		root: 'root',
		totalProperty: 'totalSize'
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
		items: [treePanel,centerPanel]
	});
	// grid.render(Ext.getBody())
	// grid.show();
});