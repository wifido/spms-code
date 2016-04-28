//<%@ page language="java" contentType="text/html; charset=utf-8"%>
var filterDeptCodeType = '${filterDeptCodeType}';
var deptId = "";
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
				Ext.getCmp('query.deptId').setValue(dept_.deptCode + '/' + dept_.deptName);
				deptId = dept_.deptId;
				querySchedule();
				
			} else {
				Ext.Msg.alert('提示','该网点不存在！', function(){
					fieldDeptCode.selectText();
				}, this);
			}
		} ,scope : this
	}, false);
};

//左侧网点树
var treePanel = new Ext.tree.TreePanel({
	region:'west',
	margins:'1 1 1 1',
	width:245,
	title:'网点信息',
	collapsible:true,
	autoScroll:true,
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
	root :new Ext.tree.AsyncTreeNode(
			{
				id : '0',
				text : '顺丰速运',
				loader : new Ext.tree.TreeLoader(
						{
							dataUrl : "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
						})
			}),
	  listeners : {
		  	beforeclick : function(node,e){
		  			 if(node!=null && node.id!=0){
		  				 	Ext.getCmp("query.deptId").setValue(node.text);
		  				 	deptId = node.id;
		  				 	// 执行查询方法
		  				 	querySchedule();
		  			 }
		  	}
	  }
});

// 查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			querySchedule();
	}
});
// 新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			 addSchedule();
	}
});
// 删除按钮
var btnDelete = new Ext.Button({
	text : "删 除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();
		var deptId = "";
		var nodeCode = "";
		
		if(node!=null && (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)){
			deptId = node.id;
			nodeCode=node.attributes.code;
		}else{
			Ext.Msg.alert("提示","只有中转场和航空操作组才能删除班别信息！");
			return;
		}
		
		var records = grid.getSelectionModel().getSelections();
		if(records.length>0){
				var ids="";
				var obj = {};
				var jsonData = [];
				for(var i=0;i<records.length;i++){
						 var record = records[i];
						 ids += record.json["SCHEDULE_ID"]+"@@";
						 var jsonObj = {};
						 jsonObj["deptId"] = record.json["DEPT_ID"]
						 jsonObj["scheduleCode"] = record.json["SCHEDULE_CODE"]
						 jsonObj["ym"] = record.json["YM"]
						 obj[record.json["DEPT_ID"]+record.json["SCHEDULE_CODE"]]=record;
						 jsonData.push(jsonObj);
				}
			    Ext.Ajax.request({
	    			url : '../operation/schedule_isScheduling.action',
	    			method : 'POST',
	    			params : {
	    				jsonData :  Ext.encode(jsonData)
	    			},
	    			success : function(res,config){
		    				var data = Ext.decode(res.responseText);
		    				if(data.success==true){
		    						deleteSchedule(ids);
		    				}else{
		    						var msg = "";
		    						for(var j=0;j<data.root.length;j++){
		    							var id = data.root[j];
		    							msg += obj[id].json["SCHEDULE_NAME"]+";"
		    						}
		    						msg = msg +"已使用不能删除！"
		    						Ext.Msg.alert("提示",msg);
		    						return;
		    				}
	    			},
	    			failure : function(){
	    					Ext.Msg.alert("提示","班别删除出错！");
	    					return;
	    			}
			    });
		}else{
				Ext.Msg.alert("提示","请选择要删除的数据！");
		}
	}
});
// 导出按钮
var btnExport = new Ext.Button({
	text : "导 出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			exportSchedule();
	}
});
// 导入按钮
var btnImport = new Ext.Button({
	text : "导 入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		
		var node = treePanel.getSelectionModel ().getSelectedNode();
		var deptId = "";
		var nodeCode = "";
		
		if(node!=null && (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)){
			deptId = node.id;
			nodeCode=node.attributes.code;
		}else{
			Ext.Msg.alert("提示","只有中转场和航空操作组才能导入班别信息！");
			return;
		}
		
		importSchedule();
	}
});

// 顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout:'column',
	height : 160,
	tbar:['-'
	      <app:isPermission code="/operation/schedule_querySchedule.action">,btnSearch</app:isPermission>
	      	,'-'
	      <app:isPermission code="/operation/schedule_saveSchedule.action">,btnAdd</app:isPermission>
	       , '-'
	      <app:isPermission code="/operation/schedule_deleteSchedule.action">,btnDelete</app:isPermission>
	      ,'-'
	      <app:isPermission code="/operation/schedule_exportSchedule.action">,btnExport</app:isPermission>
	      	,'-'
	      <app:isPermission code="/operation/scheduleUploadFile.action">,btnImport</app:isPermission>
	],
	items : [{
		xtype:'fieldset',
		title:'查询条件',
		layout:"column",
		columnWidth : 1,
		style : 'margin-top:5px;',
		frame:true,
		items : [{
				  columnWidth : .4,
				  labelWidth:120,
				  labelAlign:'right',
				  layout:'form',
				  items : [{
						xtype:'textfield',
						id:'query.deptId',
						readOnly : true,
						fieldLabel:'<font color=red>网点代码*</font>',
						anchor : '90%'
					}]
		},{
			  columnWidth : .4,
			  labelWidth:120,
			  labelAlign:'right',
			  layout:'form',
			  items : [{
				  	xtype : 'datefield',
					fieldLabel : "月份",
					id : "query.ym",
					format : 'Y-m',
					value : new Date,
					anchor : '90%',
					plugins : 'monthPickerPlugin'
						
			  }]
	},{
			  columnWidth : .4,
			  labelWidth:120,
			  labelAlign:'right',
			  layout:'form',
			  items : [{
					xtype:'textfield',
					id:'query.scheduleName',
					fieldLabel:'班别名称',
					anchor : '90%'
				}]
	},{
		  columnWidth : .4,
		  labelWidth:120,
		  labelAlign:'right',
		  layout:'form',
		  items : [{
				xtype:'textfield',
				id:'query.scheduleCode',
				fieldLabel:'班别代码',
				anchor : '90%'
			}]
}]
	}]
});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel({});
// 列头构建
var cm =  new Ext.grid.ColumnModel({
	 columns:  [
	 sm,{
	 			header: '月份', sortable: true, dataIndex: 'YM'
	 },{
	 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
	 },{
	 			header: '网点代码', sortable: true, dataIndex: 'DEPT_CODE'
	 },{
	 			header: '网点名称', sortable: true, dataIndex: 'DEPT_NAME'
	 },{
	 			header: '班别名称', sortable: true, dataIndex: 'SCHEDULE_NAME'
	 },{
	 			header: '班别代码',  sortable: true, dataIndex: 'SCHEDULE_CODE'
	 },{
	 			header: '班别区间1',  sortable: true, dataIndex: 'TIME1',renderer : function(value,metadata,record,rowIndex,colIndex,store ){
	 					var beginTime = record.json["START1_TIME"]==null?"":record.json["START1_TIME"];
	 					var endTime = record.json["END1_TIME"]==null?"":record.json["END1_TIME"];

	 					return beginTime+"-"+endTime
	 			}
	 },{
	 			header: '班别区间2',  sortable: true, dataIndex: 'TIME2',renderer : function(value,metadata,record,rowIndex,colIndex,store ){
 					var beginTime = record.json["START2_TIME"]==null?"":record.json["START2_TIME"];
 					var endTime = record.json["END2_TIME"]==null?"":record.json["END2_TIME"];

 					return beginTime +"-"+endTime
 			}
	 },{
	 			header: '班别区间3',  sortable: true, dataIndex: 'TIME3',renderer : function(value,metadata,record,rowIndex,colIndex,store ){
	 				var beginTime = record.json["START3_TIME"]==null?"":record.json["START3_TIME"];
 					var endTime = record.json["END3_TIME"]==null?"":record.json["END3_TIME"];

 					return beginTime +"-"+endTime
 			}
 		}]
});
// 数据格构建
var record = Ext.data.Record.create([{
		name : 'YM' ,
		mapping : 'YM',
		type : 'string'
	},{
		name : 'AREA_CODE' ,
		mapping : 'AREA_CODE',
		type : 'string'
	},{
		name : 'DEPT_CODE'  ,
		mapping : 'DEPT_CODE',
		type : 'string'
	},{
			name : 'DEPT_NAME'  ,
			mapping : 'DEPT_NAME',
			type : 'string'
	},{
			name : 'SCHEDULE_NAME' ,
			mapping : 'SCHEDULE_NAME',
			type : 'string'
	},{
			name : 'SCHEDULE_CODE' ,
			mapping : 'SCHEDULE_CODE',
			type : 'string'
	}]);

	// 构建数据存储Store
	var store = new Ext.data.Store({
			proxy : new  Ext.data.HttpProxy({
				 url : '../operation/schedule_querySchedule.action'
			}),
			reader: new  Ext.data.JsonReader({
				root: 'root',
				totalProperty: 'totalSize'
			},record)
	});

	// 分页组件
	var pageBar =   new Ext.PagingToolbar({
	    store: store,
	    displayInfo: true,
	    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	    pageSize: 20,
	   emptyMsg : '未检索到数据'
	})
	// 表格构建
    var grid = new Ext.grid.GridPanel({
		cm : cm,
		sm : sm,
		store : store,
		autoScroll : true,
		loadMask : true,
		tbar : pageBar,
		 viewConfig: {
		        forceFit: true
		 }
	});

// 中部的Panel
var centerPanel = new Ext.Panel({
	region:'center',
	margins:'1 1 1 0',
	items : [topPanel,grid],
	listeners : {
	  		resize : function(p,adjWidth,adjHeight,rawWidth,rawHeight) {
	  				grid.setWidth(adjWidth-5);
	  				grid.setHeight(adjHeight-165);
	  		}
	  }
});

// 标识班别代码和班别名称唯一性
var onlyScheduleCode=false;

// 新增界面
var addSchedule = function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();
		var deptId = "";
		var nodeCode = "";
		// 班别代码
		var scheduleCode = "";
		// 开始时间
		var beginTime = "";
		// 结束时间
		var endTime ="";

		if(node!=null && (filterDeptCodeType.indexOf(node.attributes.typeCode+',') != -1)){
			deptId = node.id;
			nodeCode=node.attributes.code;
		}else{
			Ext.Msg.alert("提示","只有中转场和航空操作组才能新增班别信息！");
			return;
		}

		var win = new Ext.Window({
						width:780,
						height:230,
						modal:true,
						border:false,
						bodyBorder:false,
						closable:false,
						resizable:false,
						layout:'fit',
						title : '班别新增',
						items : [{
								xtype : 'form',
								labelAlign : 'right',
								layout : 'column',
								buttonAlign : 'center',
								items : [{
										border : false,
										height : 10,
										columnWidth : 1
								},{
									xtype : 'hidden',
									name : 'deptId',
									value : deptId
								},{
									xtype : 'hidden',
									name : 'ym',
									value : Ext.isEmpty(Ext.getCmp('query.ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('query.ym').getValue(), 'Y-m')
								},{
										  columnWidth : .45,
										  border : false,
										  layout : 'form',
										  items : [{
											    xtype : 'textfield',
											    anchor : '95%',
											    allowBlank : false,
											    name : 'scheduleCode',
											    enableKeyEvents :  true,
											    maxLength : 5,
											    maxLengthText : '最多5个字符',
											    fieldLabel : '<font color=red>班别代码*</font>',
											    listeners : {
											    	keyup : function(t,e) {
											    			scheduleCode = t.getValue();
											    			var value = nodeCode+"-"+scheduleCode;
											    			if(beginTime)
											    				value+="-"+beginTime;
											    			if(endTime)
											    				value+="-"+endTime;
											    			Ext.getCmp("scheduleName").setValue(value);
											    			// t.setValue(scheduleCode);
											    	},
											    	change : function(textField,newValue,oldValue){
											    			/*if(!isNaN(newValue)){
											    				textField.markInvalid("需要包含至少一个字母");
											    				onlyScheduleCode = false;
											    				return
											    			}
											    			if(!(newValue.indexOf("W") == 0 || newValue.indexOf("Z") == 0 || newValue.indexOf("B") == 0)){
											    				textField.markInvalid("必须以W、Z、B字母开头");
											    				onlyScheduleCode = false;
											    				return
											    			}*/
											    			onlyScheduleCode = onlySchedule({'deptId' : deptId,'scheduleCode':newValue, 'ym':Ext.isEmpty(Ext.getCmp('query.ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('query.ym').getValue(), 'Y-m')},onlyScheduleCode,textField);
											    	}
											    }
										  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    allowBlank : false,
										    readOnly : true,
										    id : 'scheduleName',
										    name : 'scheduleName',
										    value : nodeCode,
										    fieldLabel : '<font color=red>班别名称*</font>'
									  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'timefield',
									    anchor : '95%',
									    allowBlank : false,
									    format : 'H:i',
									    increment : 30,
									    name : 'start1Time',
									    fieldLabel : '<font color=red>班别开始时间1*</font>',
									    listeners : {
									    	select : function(combo,record, index)  {
									    			beginTime = record.get("field1");
									    			beginTime = beginTime.replace(":","");
									    			var value = nodeCode;
									    			if(scheduleCode)
									    			    value += "-"+scheduleCode;
									    			value+="-"+beginTime;
									    			if(endTime)
									    				value+="-"+endTime;
									    			Ext.getCmp("scheduleName").setValue(value);
									    	}
									    }
								  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'timefield',
								    anchor : '95%',
								    allowBlank : false,
								    format : 'H:i',
								    increment : 30,
								    name : 'end1Time',
								    fieldLabel : '<font color=red>班别结束时间1*</font>',
								    listeners : {
								    	select : function(combo,record, index)  {
								    			endTime = record.get("field1");
								    			endTime = endTime.replace(":","");
								    			var value = nodeCode;
								    			if(scheduleCode)
								    			    value += "-"+scheduleCode;
								    			if(beginTime)
								    				value+="-"+beginTime;
								    			value+="-"+endTime;
								    			Ext.getCmp("scheduleName").setValue(value);
								    	}
								    }
							  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'timefield',
									    anchor : '95%',
									    increment : 30,
									    format : 'H:i',
									    name : 'start2Time',
									    id : 'start2Time',
									    fieldLabel : '班别开始时间2',
									    listeners : {
									    	change : function(timeField,newValue,oldValue){
									    				if(newValue=="")
									    					Ext.getCmp("end2Time").allowBlank=true;
									    				else
									    					Ext.getCmp("end2Time").allowBlank=false;
									    	}
									    }
								  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'timefield',
								    anchor : '95%',
								    increment : 30,
								    format : 'H:i',
								    name : 'end2Time',
								    id : 'end2Time',
								    fieldLabel : '班别结束时间2',
								    listeners : {
								    	select : function(combo,record, index)  {
								    			endTime = record.get("field1");
								    			endTime = endTime.replace(":","");
								    			var value = nodeCode;
								    			if(scheduleCode)
								    			    value += "-"+scheduleCode;
								    			if(beginTime)
								    				value+="-"+beginTime;
								    			value+="-"+endTime;
								    			Ext.getCmp("scheduleName").setValue(value);
								    	},
								    	change : function (timeField,newValue,oldValue){
						    				if(newValue=="")
						    					Ext.getCmp("start2Time").allowBlank=true;
						    				else
						    					Ext.getCmp("start2Time").allowBlank=false;
								    	}
								    }
							  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'timefield',
									    anchor : '95%',
									    increment : 30,
									    format : 'H:i',
									    name : 'start3Time',
									    id : 'start3Time',
									    fieldLabel : '班别开始时间3',
									    listeners : {
									    	change : function(timeField,newValue,oldValue){
									    				if(newValue=="")
									    					Ext.getCmp("end3Time").allowBlank=true;
									    				else
									    					Ext.getCmp("end3Time").allowBlank=false;
									    	}
									    }
								  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'timefield',
								    anchor : '95%',
								    increment : 30,
								    format : 'H:i',
								    name : 'end3Time',
								    id : 'end3Time',
								    fieldLabel : '班别结束时间3',
								    listeners : {
								    	select : function(combo,record, index)  {
								    			endTime = record.get("field1");
								    			endTime = endTime.replace(":","");
								    			var value = nodeCode;
								    			if(scheduleCode)
								    			    value += "-"+scheduleCode;
								    			if(beginTime)
								    				value+="-"+beginTime;
								    			value+="-"+endTime;
								    			Ext.getCmp("scheduleName").setValue(value);
								    	},
								    	change : function (timeField,newValue,oldValue){
						    				if(newValue=="")
						    					Ext.getCmp("start3Time").allowBlank=true;
						    				else
						    					Ext.getCmp("start3Time").allowBlank=false;
								    	}
								    }
							  }]
						}],
					fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if (Ext.isEmpty(Ext.getCmp('query.ym').getValue())){
										Ext.Msg.alert("提示","请选择月份！");
										return;
									}
									if(!onlyScheduleCode){
										Ext.Msg.alert("提示","班别代码不符合规范！");
										return;
									}
									if(form.getForm().isValid ()){
												form.getForm().submit({
													url: '../operation/schedule_saveSchedule.action',
													success: function(form, action) {
														Ext.Msg.alert('Success', action.result.msg);
														win.close();
														querySchedule();
													},
													failure : function(form, action) {
														Ext.Msg.alert('Success', action.result.msg);
														win.close();
													} ,
													waitTitle:"请稍后",
													waitMsg:"正在执行操作...",
													timeout : 300  // 5分钟
												});
									}
							}
					},{
							text : '取消',
							handler : function(){
									win.close();
							}
					}]
						}]

		});
	 win.show();
}

// 查询方法
var querySchedule = function(){
	if (deptId == "") {
		Ext.Msg.alert("提示","请选择网点代码！");
		return;
	} else {
		store.setBaseParam("deptId",deptId);
		store.setBaseParam("scheduleName",Ext.getCmp('query.scheduleName').getValue());
		store.setBaseParam("scheduleCode",Ext.getCmp('query.scheduleCode').getValue());
		store.setBaseParam("ym",Ext.isEmpty(Ext.getCmp('query.ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('query.ym').getValue(), 'Y-m'));
		store.load({
				params : {
						start: 0,
				        limit: 20
				}
		});
	}

}

// 删除方法
var deleteSchedule = function(ids){
			if(ids){
				Ext.Msg.confirm ("提示","是否确定删除该记录？",function(btn){
					if(btn=="yes"){
							Ext.Ajax.request({
										url : '../operation/schedule_deleteSchedule.action',
										method : 'POST',
										params : {
											ids : ids
										},
										success : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
												querySchedule();
										},
										failure : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
										}
							});
					}
				}, this)
			}
}

// 导出方法
var exportSchedule = function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();
		var deptId = "";
		if(node!=null && node.id!=0){
			deptId = node.id;
		}else{
			Ext.Msg.alert("提示","请先选择网点！");
			return;
		}
		var myMask = new Ext.LoadMask(centerPanel.getEl(), {msg:"正在导出..."});
		myMask.show();
		Ext.Ajax.request({
					url : "../operation/schedule_exportSchedule.action",
					method : 'POST',
					params : {
						deptId : deptId,
						scheduleName : Ext.getCmp('query.scheduleName').getValue(),
						ym :  Ext.isEmpty(Ext.getCmp('query.ym').getValue()) ? '' : Ext.util.Format.date(Ext.getCmp('query.ym').getValue(), 'Y-m'),
						scheduleCode :  Ext.getCmp('query.scheduleCode').getValue()
					},
					success : function(res,config){
							myMask.hide();
							var obj = Ext.decode(res.responseText);
							if(!obj.success){
									Ext.Msg.alert("提示",obj.msg);
									return ;
							}
							var url = obj.fileName;
							window.location= '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
					},
					failure : function(res,config){
							var obj = Ext.decode(res.responseText);
							Ext.Msg.alert("提示",obj.msg);
							myMask.hide();
					}
		});

}

// 模板下载
var downTemp = function(){
		url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=班别导入模板.xls&moduleName=operation&entityName=SchedulingBase&isTemplate=true";
		window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));// "../pages/schedulingBase/template/班别导入模板.xls";
}

// 导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}
// 导入方法
var importSchedule = function(){
			var win = new Ext.Window({
						width:580,
						height:200,
						modal:true,
						border:false,
						bodyBorder:false,
						closable:false,
						resizable:false,
						layout:'fit',
						title : '导入',
						items : [{
								xtype : 'form',
								border : false,
								fileUpload : true,
								frame : true,
								labelAlign : 'right',
								items : [{
										border : false,
										height : 20
								},{
										border : false,
										xtype : 'label',
										style : 'margin-left : 60px;',
										html : '<font color=red size=4>最多导入1000条数据</font>'
								},{
									border : false,
									height : 10
								},{
										 xtype : 'textfield',
										 inputType:'file',
										 width : 290,
										 name : 'upload',
										 fieldLabel : '文件路径'
								},{
									 xtype : 'hidden',
									 id:'deptId',
									 name:'deptId'
									 
								}
								,{
									border : false,
									height : 10
							},{
									border : false,
									xtype : 'label',
									style : 'margin-left : 40px;',
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">导入文件模板下载</a>'
							}],
							fbar : [{
									text : '上传',
									handler :function(){
											var node = treePanel.getSelectionModel ().getSelectedNode();
                                            if (node == null) {
                                                Ext.Msg.alert('提示', '请选择对应的网点！');
                                                return;
                                            }
											Ext.getCmp("deptId").setValue(node.id);
											var form = this.ownerCt.ownerCt;
											var basicForm = form.getForm();
											var fileName = basicForm.findField("upload").getValue();
											var xls=fileName.substr(fileName.lastIndexOf(".")).toLowerCase();// 获得文件后缀名
											if(xls!='.xls'){
												Ext.Msg.alert("提示","系统只支持xls类型文件上传，请下载模板！");
												return;
											}
											basicForm.submit({
												url: '../operation/scheduleUploadFile.action',
												success: function(form, action) {
													if(!action.result.success){
															Ext.Msg.alert("提示",action.result.msg);
															return;
													}
													var url = "../common/downloadReportFile.action?"+ encodeURI(encodeURI(action.result.downLoadUrl));
													var aTag= "";
													if(action.result.downLoadUrl){
															aTag="<a href='#' url='"+url+"'     onclick='downError(this)'>错误数据下载</a>";
													}
													Ext.Msg.alert('提示', action.result.msg+aTag);
													win.close();
												},
												failure : function(form, action) {
													Ext.Msg.alert('提示', action.result.msg||action.result.error);
													win.close();
												} ,
												waitTitle:"请稍候",
												waitMsg:"正在执行操作...",
												timeout : 300  // 5分钟
											});
									}
							},{
								text : '取消',
								handler :function(){
										win.close();
								}
							}]
						}]
			}).show();

}

// 查询班别代码的唯一性
var onlySchedule = function(params,opera,obj){
			Ext.Ajax.request({
						url : '../operation/schedule_querySchedule.action',
						params : params,
						success : function(res,config){
									var returnObj = Ext.decode(res.responseText);
									if(returnObj.totalSize==0)
										onlyScheduleCode =  true;
									else
										obj.markInvalid("此值已存在！");
						}
			});
}

// 初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout=300000;
	Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
	var viewreprot = new Ext.Viewport({
		layout : "border",
		items : [ treePanel,centerPanel]
	});

	// 禁用后退键
/*
 * function document.onkeydown() { if (event.keyCode == 8) { if
 * (document.activeElement.type == "text") { if (document.activeElement.readOnly ==
 * false) return true; } return false; } }
 */
});



