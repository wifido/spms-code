//<%@ page language="java" contentType="text/html; charset=utf-8"%>

function buildTips(importResult) {
	return '导入成功:' + importResult.successCount + '条!';
}

function downloadErrorData(obj) {
	window.location = obj.attributes.url.nodeValue;
}

function buildDownloadPath(errorFileName) {
	return '../common/downloadReportFile.action?' + Ext.util.Format.htmlDecode(encodeURI(encodeURI(errorFileName)));
}

function downTemplate(templateFileName, moduleName) {
	url = 'uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=' + templateFileName + '&moduleName=' + moduleName + '&entityName=schedulingMgt&isTemplate=true';
	window.location = '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
}

function submit(uploadform, url, store) {
	uploadform.form.submit({
		waitMsg : '正在提交数据',
		waitTitle : '提示',
		method : "POST",
		timeout : 500000,
		url : url,
		success : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);
			if (!Ext.isEmpty(result.error)) {
				Ext.Msg.alert('提示', "导入失败！" + result.error);
				return;
			}
			var downloadUrl = '';
			var importResult = result.resultMap.importResult;
			var tips = buildTips(importResult);
			var errorDataDownloadPath = importResult.errorDataDownloadPath;
			if (!Ext.isEmpty(errorDataDownloadPath)) {
				errorDataDownloadPath = buildDownloadPath(errorDataDownloadPath);
				downloadUrl = "  <a href = '#' url='" + errorDataDownloadPath + "' onclick = 'downloadErrorData(this)'>错误数据下载</a>";
				Ext.MessageBox.alert("提示", tips + downloadUrl, function() {
					uploadform.ownerCt.hide();
				});
				return;
			}
			Ext.Msg.alert('提示', tips);
			store.reload();
			uploadform.ownerCt.hide();
		},
		failure : function(form, action) {
			var result = Ext.util.JSON.decode(action.response.responseText);
			Ext.Msg.alert('提示', "导入失败！" + result.error);
		}
	});
}

Ext.import_window = Ext.extend(Ext.Window, {
	title : "导入窗口",
	height : 180,
	width : 500,
	layout : 'column',
	closeAction : 'hide',
	plain : true,
	modal : true,
	// 在组件初始化期间调用的代码
	initComponent : function() {
		// 初始化
		var templateFileName = this.templateFileName;
		var moduleName = this.moduleName;
		var url = this.url;
		var store = this.store;

		var importTemplateHtml = '<font style="margin-right:60px;font: 11px Verdana, Arial, Helvetica, sans-serif;">下载模板文件:</font><a href="#" onclick="downTemplate(\'' + templateFileName + '\',\''
				+ moduleName + '\')">' + templateFileName + '</a>';

		var uploadFileName = new Ext.form.Hidden({
			name : "dto.filePath"
		});

		var uploadFile = new Ext.form.TextField({
			height : 30,
			width : 350,
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
			items : [new Ext.Panel({
				layout : 'form',
				border : false,
				style : 'margin-top:20px;',
				items : [{
					xtype : 'hidden',
					name : "dto.deptId"
				},uploadFileName,uploadFile]
			}),new Ext.form.Label({
				html : importTemplateHtml
			})]
		});

		var btnImport = new Ext.Button({
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
				submit(uploadform, url, store);
			}
		});

		// 因为配置对象应用到了“this”，所以属性可以在这里被覆盖，或者添加新的属性
		// （如items,tools,buttons）
		Ext.apply(this, {
			buttonAlign : 'center',
			bbar : [btnImport],
			items : [uploadform]
		});

		// 调用父类代码之前

		// 调用父类构造函数（必须）

		Ext.import_window.superclass.initComponent.apply(this, arguments);

		// 调用父类代码之后 如：设置事件处理和渲染组件
	}
});
