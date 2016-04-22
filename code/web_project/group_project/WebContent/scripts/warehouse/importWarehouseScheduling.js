//<%@ page language="java" contentType="text/html; charset=utf-8"%>

var importTemplateHtml = [
		'<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">下载模板文件:</font>',
		'<a href="#" onclick="downTemplate()">仓管排班导入模板.xls</a>' ];

var downTemplate = function() {
	url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=仓管排班导入模板.xls&"
			+ "moduleName=warehouse&entityName=schedulingMgt&isTemplate=true";
	window.location = '../common/downloadReportFile.action?'
			+ encodeURI(encodeURI(url));
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

var uploadform = new Ext.form.FormPanel({
	height : 160,
	width : 600,
	frame : true,
	fileUpload : true,
	border : false,
	items : [ new Ext.Panel({
		layout : 'form',
		border : false,
		items : [ {
			xtype : 'hidden',
			id : 'upload_deptid',
			name : "dto.deptId"
		}, uploadFileName, uploadFile ]
	}), new Ext.form.Label({
		html : importTemplateHtml.join('')
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
	items : [ uploadform ],
	tbar : [ {
		text : '导入',
		pressed : true,
		height : 18,
		minWidth : 60,
		handler : function() {
			uploadFileName.setValue(uploadFile.getValue());
			if (uploadFileName.getValue().indexOf(".xls") < 0) {
				Ext.MessageBox.alert('提示', '导入时，请先选择Excel文件');
				return false;
			}
			submit();
		}
	} ]
});

function submit() {
	uploadform.form
			.submit({
				waitMsg : '正在提交数据',
				waitTitle : '提示',
				method : "POST",
				timeout : 500000,
				url : "importRec_warehouseScheduling.action",
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

function downError(objectA) {
	window.location = objectA.attributes.url.nodeValue;
}

function getDownloadPathWhenUploadFail(errorFileName) {
	return '../common/downloadReportFile.action?'
			+ Ext.util.Format.htmlDecode(encodeURI(encodeURI(errorFileName)));
}

var btnImport = new Ext.Button({
	text : "导入",
	pressed : true,
	minWidth : 60,
	handler : function() {
		var departmentCodes = Ext.getCmp("query.branchCode").getValue();
		Ext.Ajax.request({
			url : '../warehouse/validateAuthority.action',
			method : 'POST',
			params : {
				dept_code : departmentCodes
			},
			success : function(res, config) {
				var data = Ext.decode(res.responseText);
				if (data.dataMap.success == false) {
					Ext.Msg.alert("提示", "暂无该网点权限！");
				} else {
					uploadWindow.show();
				}
			},
			failure : function() {
				Ext.Msg.alert("提示", "修改失败，暂时该网点权限！");
			}
		});
	}
});