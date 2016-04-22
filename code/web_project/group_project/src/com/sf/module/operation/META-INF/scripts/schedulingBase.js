//<%@ page language="java" contentType="text/html; charset=utf-8"%>

//左侧网点树
var treePanel = new Ext.tree.TreePanel({
	region:'west',
	margins:'1 1 1 1',
	width:245,
	title:'网点信息',
	collapsible:true,
	autoScroll:true,
	root :new Ext.tree.AsyncTreeNode(
			{
				id : '0',
				text : '顺丰速运',
				loader : new Ext.tree.TreeLoader(
						{
							dataUrl : "../common/deptTree.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
						})
			}),
	  listeners : {
		  	beforeclick : function(node,e){
		  			 if(node!=null && node.id!=0){
		  				 	Ext.getCmp("query.deptId").setValue(node.text);
		  				 	//执行查询方法
		  				 	querySchedule(node);
		  			 }
		  	}
	  }
});

//查询按钮
var btnSearch = new Ext.Button({
	text : "查 询",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			querySchedule();
	}
});
//新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			 addSchedule();
	}
});
//修改按钮
var btnEdit = new Ext.Button({
	text : "修 改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			var records = grid.getSelectionModel().getSelections();
			if(records.length>1){
				Ext.Msg.alert("提示","只能选择一条数据!");
				return ;
			}
			var record = grid.getSelectionModel().getSelected();
			updateSchedule(record);
	}
});
//删除按钮
var btnDelete = new Ext.Button({
	text : "删 除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
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
						 obj[record.json["DEPT_ID"]+record.json["SCHEDULE_CODE"]]=record;
						 jsonData.push(jsonObj);
				}
			    Ext.Ajax.request({
	    			url : '../schedulingBase/schedule_isScheduling.action',
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
//导出按钮
var btnExport = new Ext.Button({
	text : "导 出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			exportSchedule();
	}
});
//导入按钮
var btnImport = new Ext.Button({
	text : "导 入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			importSchedule();
	}
});

//顶部的Panel
var topPanel = new Ext.Panel({
	frame : true,
	layout:'column',
	height : 160,
	tbar:['-'
	      <app:isPermission code="/schedulingBase/schedule_querySchedule.action">,btnSearch</app:isPermission>
	      	,'-'
	      <app:isPermission code="/schedulingBase/schedule_saveSchedule.action">,btnAdd</app:isPermission>
	       , '-'
	      <app:isPermission code="/schedulingBase/schedule_updateSchedule.action">,btnEdit</app:isPermission>
	        ,'-'
	      <app:isPermission code="/schedulingBase/schedule_deleteSchedule.action">,btnDelete</app:isPermission>
	      	,'-'
	      <app:isPermission code="/schedulingBase/schedule_exportSchedule.action">,btnExport</app:isPermission>
	      	,'-'
	      <app:isPermission code="/schedulingBase/scheduleUploadFile.action">,btnImport</app:isPermission>
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
},{
	  columnWidth : .4,
	  labelWidth:120,
	  labelAlign:'right',
	  layout:'form',
	  items : [{
			xtype:'combo',
			id:'query.state',
			typeAhead: true,
		    triggerAction: 'all',
		    lazyRender:true,
		    mode: 'local',
			fieldLabel:'状态查询',
			store : [['10','全部'],['1','有效'],['0','失效']],
			anchor : '90%'
		}]
}]
	}]
});

//复选框
var sm = new Ext.grid.CheckboxSelectionModel({});
//列头构建
var cm =  new Ext.grid.ColumnModel({
	 columns:  [
	 sm,
	 {
	 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
	 },{
	 			header: '场地名称', sortable: true, dataIndex: 'DEPT_NAME'
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


 			 },{
	 			header: '班别生效时间',  sortable: true, dataIndex: 'ENABLE_DT',renderer:function(value,metadata,record,rowIndex,colIndex,store ){
		 			return record.json["ENABLE_DT"]==null?"":record.json["ENABLE_DT"].substr(0,record.json["ENABLE_DT"].length-9)
	 			}

 			 },{
	 			header: '班别失效时间',  sortable: true, dataIndex: 'DISABLE_DT',renderer:function(value,metadata,record,rowIndex,colIndex,store ){
		 			return record.json["DISABLE_DT"]==null?"":record.json["DISABLE_DT"].substr(0,record.json["DISABLE_DT"].length-9)
	 			}
	 }]
});

//数据格构建
var record = Ext.data.Record.create([{
		name : 'AREA_CODE' ,
		mapping : 'AREA_CODE',
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

	//构建数据存储Store
	var store = new Ext.data.Store({
			proxy : new  Ext.data.HttpProxy({
				 url : '../schedulingBase/schedule_querySchedule.action'
			}),
			reader: new  Ext.data.JsonReader({
				root: 'root',
				totalProperty: 'totalSize'
			},record)
	});

	//分页组件
	var pageBar =   new Ext.PagingToolbar({
	    store: store,
	    displayInfo: true,
	    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
	    pageSize: 20,
	   emptyMsg : '未检索到数据'
	})
	//表格构建
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

//中部的Panel
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

//标识班别代码和班别名称唯一性
var onlyScheduleCode=false;

//新增界面
var addSchedule = function(){
		var node = treePanel.getSelectionModel ().getSelectedNode();
		var deptId = "";
		var nodeCode = "";
		//班别代码
		var scheduleCode = "";
		//开始时间
		var beginTime = "";
		//结束时间
		var endTime ="";

		if(node!=null && node.leaf){
			deptId = node.id;
			nodeCode=node.attributes.code;
		}else{
			Ext.Msg.alert("提示","只允许中转场和运力操作组 可以新增班别！");
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
										  columnWidth : .45,
										  border : false,
										  layout : 'form',
										  items : [{
											    xtype : 'textfield',
											    anchor : '95%',
											    allowBlank : false,
											    name : 'scheduleCode',
											    enableKeyEvents :  true,
											    maxLength : 3,
											    maxLengthText : '最多3个字符',
											    maskRe : /^[0-9a-zA-Z]+$/,
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
											    			//t.setValue(scheduleCode);
											    	},
											    	change : function(textField,newValue,oldValue){
											    			if(!isNaN(newValue)){
											    				textField.markInvalid("需要包含至少一个字母");
											    				onlyScheduleCode = false;
											    				return
											    			}
											    			onlyScheduleCode = onlySchedule({'deptId' : deptId,'scheduleCode':newValue},onlyScheduleCode,textField);
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
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'datefield',
								    anchor : '95%',
								    allowBlank : false,
								    format : 'Y-m-d',
								    name : 'enableDt',
								    minValue : new Date(),
								    fieldLabel : '<font color=red>班别生效日期*</font>',
								    listeners : {
								    	select : function(d,date){
								    		   var t=date.getTime()+1000*60*60*24;
								    		   var tor=new Date(t);
								    		   var dD = Ext.getCmp("disableDt");
								    		   dD.minValue =tor ;
								    		   dD.isValid();
								    	}
								    }
							  }]
					},{
						  columnWidth : .45,
						  border : false,
						  layout : 'form',
						  items : [{
							    xtype : 'datefield',
							    anchor : '95%',
							    id:'disableDt',
							    format : 'Y-m-d',
							    name : 'disableDt',
							    fieldLabel : '班别失效日期'
						  }]
					}],
					fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if(!onlyScheduleCode){
												Ext.Msg.alert("提示","请确保班别代码在网点内唯一且不能为纯数字类型！");
												return;
									}
									if(form.getForm().isValid ()){
												form.getForm().submit({
													url: '../schedulingBase/schedule_saveSchedule.action',
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
													timeout : 300  //5分钟
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

//查询方法
var querySchedule = function(node){
	var node = node || treePanel.getSelectionModel ().getSelectedNode();
	var deptId = "";
	if(node!=null && node.id!=0){
		deptId = node.id;
	}else{
		Ext.Msg.alert("提示","请先选择网点！");
		return;
	}
	store.setBaseParam("deptId",deptId);
	store.setBaseParam("scheduleName",Ext.getCmp('query.scheduleName').getValue());
	store.setBaseParam("scheduleCode",Ext.getCmp('query.scheduleCode').getValue());
	store.setBaseParam("state", Ext.getCmp('query.state').getValue());
	store.load({
			params : {
					start: 0,
			        limit: 20
			}
	});

}

//修改方法
var updateSchedule = function(record){
		if(record==null){
				Ext.Msg.alert("提示","请选择要修改的数据！");
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
							title : '班别修改',
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
									name : 'scheduleID',
									value :  record.json["SCHEDULE_ID"]
								},{
										  columnWidth : .45,
										  border : false,
										  layout : 'form',
										  items : [{
											    xtype : 'textfield',
											    anchor : '95%',
											    disabled  : true,
											    name : 'scheduleCode',
											    value : record.json["SCHEDULE_CODE"],
											    fieldLabel : '<font color=red>班别代码*</font>'
										  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    readOnly : true,
										    id : 'scheduleName',
										    name : 'scheduleName',
										    disabled  : true,
										    value : record.json["SCHEDULE_NAME"],
										    fieldLabel : '<font color=red>班别名称*</font>'
									  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'timefield',
									    anchor : '95%',
									    format : 'H:i',
									    increment : 30,
									    name : 'start1Time',
									    disabled  : true,
									    value : record.json["START1_TIME"],
									    fieldLabel : '<font color=red>班别开始时间1*</font>'
								  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'timefield',
								    anchor : '95%',
								    format : 'H:i',
								    increment : 30,
								    name : 'end1Time',
								    disabled  : true,
								    value : record.json["END1_TIME"],
								    fieldLabel : '<font color=red>班别结束时间1*</font>'
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
									    disabled  : true,
									    value : record.json["START2_TIME"],
									    fieldLabel : '班别开始时间2'
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
								    disabled  : true,
								    value : record.json["END2_TIME"],
								    fieldLabel : '班别结束时间2'
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
									    disabled  : true,
									    value : record.json["START3_TIME"],
									    fieldLabel : '班别开始时间3'
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
								    disabled  : true,
								    value : record.json["END3_TIME"],
								    fieldLabel : '班别结束时间3'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'datefield',
								    anchor : '95%',
								    allowBlank : false,
								    format : 'Y-m-d',
								    id : 'enableDt',
								    name : 'enableDt',
								    value : record.json["ENABLE_DT"].substr(0,record.json["ENABLE_DT"].length-9),
								    fieldLabel : '<font color=red>班别生效日期*</font>',
								    listeners : {
								    	select : function(dateField,date){
								    			if(date.format("yyyy-MM-dd")<new Date().format("yyyy-MM-dd")){
								    					Ext.Msg.alert("提示","班别生效日期不能为历史日期！");
								    					var v = record.json["ENABLE_DT"].substr(0,record.json["ENABLE_DT"].length-9);
								    					dateField.setValue(v);
								    			}
								    	}
								    }
							  }]
					},{
						  columnWidth : .45,
						  border : false,
						  layout : 'form',
						  items : [{
							    xtype : 'datefield',
							    anchor : '95%',
							    format : 'Y-m-d',
							    name : 'disableDt',
							    value : record.json["DISABLE_DT"]==null?'':record.json["DISABLE_DT"].substr(0,record.json["DISABLE_DT"].length-9),
							    fieldLabel : '班别失效日期',
							    listeners : {
							    	select : function(d,date){
							    		  var  enableDt = Ext.getCmp("enableDt").getValue();
							    		   var t=enableDt.getTime()+1000*60*60*24;
							    		   var tm = new Date(t);
							    		   if(date.format("yyyy-MM-dd")<tm.format("yyyy-MM-dd")){
							    			   d.minValue =tm ;
								    		   d.isValid();
							    		 }
							    	}
							    }
						  }]
					}],
					fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if(form.getForm().isValid ()){
												form.getForm().submit({
													url: '../schedulingBase/schedule_updateSchedule.action',
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
													timeout : 300  //5分钟
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
//删除方法
var deleteSchedule = function(ids){
			if(ids){
				Ext.Msg.confirm ("提示","是否确定删除该记录？",function(btn){
					if(btn=="yes"){
							Ext.Ajax.request({
										url : '../schedulingBase/schedule_deleteSchedule.action',
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

//导出方法
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
					url : "../schedulingBase/schedule_exportSchedule.action",
					method : 'POST',
					params : {
						deptId : deptId,
						scheduleName : Ext.getCmp('query.scheduleName').getValue(),
						scheduleCode :  Ext.getCmp('query.scheduleCode').getValue(),
						state : Ext.getCmp('query.state').getValue()
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

//模板下载
var downTemp = function(){
		url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=班别导入模板.xls&moduleName=schedulingBase&entityName=SchedulingBase&isTemplate=true";
		window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));//"../pages/schedulingBase/template/班别导入模板.xls";
}

//导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}
//导入方法
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
									border : false,
									height : 10
							},{
									border : false,
									xtype : 'label',
									style : 'margin-left : 40px;',
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">班别导入模板下载</a>'
							}],
							fbar : [{
									text : '上传',
									handler :function(){
											var form = this.ownerCt.ownerCt;
											var basicForm = form.getForm();
											var fileName = basicForm.findField("upload").getValue();
											var xls=fileName.substr(fileName.lastIndexOf(".")).toLowerCase();//获得文件后缀名
											if(xls!='.xls'){
												Ext.Msg.alert("提示","系统只支持xls类型文件上传，请下载模板！");
												return;
											}
											basicForm.submit({
												url: '../schedulingBase/scheduleUploadFile.action',
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
												timeout : 300  //5分钟
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

//查询班别代码的唯一性
var onlySchedule = function(params,opera,obj){
			Ext.Ajax.request({
						url : '../schedulingBase/schedule_querySchedule.action',
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

//初始化
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.Ajax.timeout=300000;
	Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
	var viewreprot = new Ext.Viewport({
		layout : "border",
		items : [ treePanel,centerPanel]
	});


	//禁用后退键
/*	function document.onkeydown() {
        if (event.keyCode == 8) {
            if (document.activeElement.type == "text") {
                if (document.activeElement.readOnly == false)
                    return true;
            }
            return false;
        }
    }*/
});



