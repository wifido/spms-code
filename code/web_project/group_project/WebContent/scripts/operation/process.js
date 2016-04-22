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
							dataUrl : "../operation/groupOrgAction.action?textField=deptName&idField=id&leafField=&clsField=&childrenField="
						})
			}),
	  listeners : {
		  	beforeclick : function(node,e){		  			
			  		if(node!=null && node.id!=0){
	  				 	Ext.getCmp("query.deptId").setValue(node.text);
	  				 	//执行查询方法
	  				 	queryProcess(node);
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
			queryProcess();
	}
});
// 新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			 addProcess();		
	}
});
// 修改按钮
var btnEdit = new Ext.Button({
	text : "修 改",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			var record = grid.getSelectionModel().getSelected();
			updateProcess(record);
	}
});

//工序确认
var btnProcess = new Ext.Button({
	text : "工序确认",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		confirmProcess();
	}
});

//导入
var btnImport = new Ext.Button({
	text : "导入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			importProcess();
	}
});

// 导出
var btnExport = new Ext.Button({
	text : "导 出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			exportProcess();
	}
});

//顶部的Panel
var topPanel = new Ext.Panel({
				frame : true,
				layout:'column',
				height : 160,
				tbar:['-'
				      <app:isPermission code="/operation/processMgt_queryProcess.action">,btnSearch</app:isPermission>
				      	,'-'
				      <app:isPermission code="/operation/processMgt_saveProcess.action">,btnAdd</app:isPermission>
				       , '-'
				      <app:isPermission code="/operation/processMgt_updateProcess.action">,btnEdit</app:isPermission>		
				      	,'-'
				      <app:isPermission code="/operation/processMgt_confirmProcess.action">,btnProcess</app:isPermission>
				      	,'-'	
				     <app:isPermission code="/operation/processUploadFile.action">,btnImport</app:isPermission>
				      	,'-'		
				      <app:isPermission code="/operation/processMgt_exportProcess.action">,btnExport</app:isPermission>
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
								id:'query.processName',					
								fieldLabel:'工序名称',
								anchor : '90%'
							}]
				},{
					  columnWidth : .4,
					  labelWidth:120,
					  labelAlign:'right',
					  layout:'form',
					  items : [{
							xtype:'textfield',
							id:'query.processCode',
							fieldLabel:'工序代码',
							anchor : '90%'
						}]
			}]	
				}]
	});


//复选框
var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});
// 列头构建
var cm =  new Ext.grid.ColumnModel({
	 columns:  [
	 sm,
	 {
	 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
	 },{
	 			header: '网点名称', sortable: true, dataIndex: 'DEPT_NAME'
	 },{
	 			header: '工序名称', sortable: true, dataIndex: 'PROCESS_NAME'
	 },{
	 			header: '工序代码',  sortable: true, dataIndex: 'PROCESS_CODE'
	 },{
	 			header: '工序含金量',  sortable: true, dataIndex: 'DIFFICULTY_VALUE',renderer : function(value,metadata,record,rowIndex,colIndex,store ){
			 				if(record.get("DIFFICULTY_MODIFY_VALUE")!=""){
									return  record.get("DIFFICULTY_MODIFY_VALUE");
							}else{
									return record.get("DIFFICULTY_VALUE");
							}
	 			}
	 },{
	 		    header: '总部难度系数',  sortable: true, dataIndex: 'DIFFICULTY_VALUE'
	 },{
	 			header: '难度系数调整',  sortable: true, dataIndex: 'DIFFICULTY_MODIFY_VALUE'
	 },{
				header: '判断需求',  sortable: true, dataIndex: 'ESTIMATE_VALUE'
	 },{
				header: '强度需求',  sortable: true, dataIndex: 'INTENSITY_VALUE'
	 },{
		 		header: '技能需求',  sortable: true, dataIndex: 'SKILL_VALUE'
	}]
});

// 数据格构建
var record = Ext.data.Record.create([{
				name : 'AREA_CODE' ,
				mapping : 'AREA_CODE',
				type : 'string'
		},{
				name : 'DEPT_NAME'  ,
				mapping : 'DEPT_NAME',
				type : 'string'
		},{
				name : 'PROCESS_NAME' ,
				mapping : 'PROCESS_NAME',
				type : 'string'
		},{
				name : 'PROCESS_CODE' ,
				mapping : 'PROCESS_CODE',
				type : 'string'
		},{
				name : 'DIFFICULTY_VALUE' ,
				mapping : 'DIFFICULTY_VALUE',
				type : 'string'
		},{
				name : 'DIFFICULTY_VALUE' ,
				mapping : 'DIFFICULTY_VALUE',
				type : 'string'
		},{
				name : 'DIFFICULTY_MODIFY_VALUE' ,
				mapping : 'DIFFICULTY_MODIFY_VALUE',
				type : 'string'
		},{
				name : 'ESTIMATE_VALUE' ,
				mapping : 'ESTIMATE_VALUE',
				type : 'string'
		},{
				name : 'INTENSITY_VALUE' ,
				mapping : 'INTENSITY_VALUE',
				type : 'string'
		},{
				name : 'SKILL_VALUE' ,
				mapping : 'SKILL_VALUE',
				type : 'string'
		}
		]);

// 构建数据存储Store
var store = new Ext.data.Store({
		proxy : new  Ext.data.HttpProxy({
			 url : '../operation/processMgt_queryProcess.action'		        		
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
});
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

//初始化
Ext.onReady(function() {
		Ext.QuickTips.init();	
		Ext.Ajax.timeout=300000;
		Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
		var viewreprot = new Ext.Viewport({
			layout : "border",
			items : [ treePanel,centerPanel]
		});
	});	


//查询方法
var queryProcess = function(node){
			var node = node||treePanel.getSelectionModel ().getSelectedNode();
			var deptId = "";
			if(node!=null && node.id!=0){
				deptId = node.id;
			}else{
				Ext.Msg.alert("提示","请先选择网点！");
				return;
			}
			store.setBaseParam("deptId",deptId);
			store.setBaseParam("processCode",Ext.getCmp('query.processCode').getValue());
			store.setBaseParam("processName",Ext.getCmp('query.processName').getValue());
			store.load({
					params : {
							start: 0,          
					        limit: 20
					}		
			});	
	
}

//判断工序代码和名称是否唯一 标识
var onlyProcessName = false;
var onlyProcessCode  = false;

//新增方法
var addProcess = function(){
			var node = treePanel.getSelectionModel ().getSelectedNode();
			//工号
			var empCode = "";
			//网点代码
			var deptCode = "";
			if(node!=null && node.attributes.code=='001'){
				deptId = node.id;
				deptCode=node.attributes.code;
			}else{
				Ext.Msg.alert("提示","只能选择总部网点！");
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
					title : '工序新增',
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
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'textfield',
									    anchor : '95%',		
									    readOnly : true,
									    name : 'deptCode',
									    value :  deptCode,
									    allowBlank : false,
									    fieldLabel : '<font color=red>网点代码*</font>'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'textfield',
									    anchor : '95%',
									    name : 'processArea',								    
									    allowBlank : false,
									    maxLength : 50,
									    maxLengthText : '最多只能输入50个字符',
									    fieldLabel : '<font color=red>区域*</font>'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'textfield',
									    anchor : '95%',
									    name : 'processCode',								    
									    allowBlank : false,
									    maxLength : 6,
									    maxLengthText : '最多只能输入6个字符',
									    fieldLabel : '<font color=red>工序代码*</font>',
									    listeners : {
									    	change : function(textfield,newValue,oldValue){
									    			var value = newValue.replace(/[, ]/g,'');
									    			textfield.setValue(value);	
									    			isOnlyProcess("processCode",value,textfield);
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
									    name : 'processTool',
									    maxLength : 15,
									    maxLengthText : '最多只能输入15个字符',
									    fieldLabel : '工具使用'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'textfield',
									    anchor : '95%',
									    name : 'processName',								    
									    allowBlank : false,
									    maxLength : 15,
									    maxLengthText : '最多只能输入15个字符',
									    fieldLabel : '<font color=red>工序名称*</font>',
									    listeners : {
									    	change : function(textfield,newValue,oldValue){
									    			var value = newValue.replace(/[, ]/g,'');
									    			textfield.setValue(value);	
									    			isOnlyProcess("processName",value,textfield);
									    	} 
									    }
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'numberfield',
									    anchor : '95%',
									    name : 'intensityValue',
									    minValue : 0.01,
									    maxValue : 10,
									    maxText  : '强度需求最大值为10',
									    minText : '强度需求最小值为0.01',
									    fieldLabel : '强度需求'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'numberfield',
									    anchor : '95%',
									    name : 'estimateValue',
									    minValue : 0,
									    maxValue : 5,
									    maxText  : '判断需求最大值为5',
									    minText : '判断需求最小值为0',
									    fieldLabel : '判断需求'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'numberfield',
									    anchor : '95%',
									    name : 'difficultyValue',
									    minValue : 0.01,
									    maxValue : 10,
									    maxText  : '技能需求最大值为10',
									    minText : '技能需求最小值为0.01',
									    fieldLabel : '难度系数'
								  }]
							},{
								  columnWidth : .45,
								  border : false,
								  layout : 'form',
								  items : [{
									    xtype : 'numberfield',
									    anchor : '95%',
									    name : 'skillValue',
									    minValue : 0.01,
									    maxValue : 10,
									    maxText  : '技能需求最大值为10',
									    minText : '技能需求最小值为0.01',
									    fieldLabel : '技能需求'
								  }]
							}],
							fbar : [{
								text : '保存',
								handler : function(){
										var form = this.ownerCt.ownerCt;
												if(form.getForm().isValid ()){
													if(!onlyProcessName){
														Ext.Msg.alert("提示","工序名称已存在！");
														return;
												}
												if(!onlyProcessCode){
													Ext.Msg.alert("提示","工序代码已存在！");
													return;
												}
												form.getForm().submit({
													url: '../operation/processMgt_saveProcess.action',
													success: function(form, action) {
														Ext.Msg.alert('提示', action.result.msg);
														win.close();
														queryProcess();
													},
													failure : function(form, action) {
														Ext.Msg.alert('提示', action.result.msg);
														win.close();
													},
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

//更新方法
var updateProcess = function(record){	
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
								title : '工序修改',
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
											name : 'processId',
											value : record.json["PROCESS_ID"]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'textfield',
												    anchor : '95%',
												    name : 'deptCode',
												    disabled : true,
												    value :  record.json["DEPT_CODE"],
												    allowBlank : false,
												    fieldLabel : '<font color=red>网点代码*</font>'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'textfield',
												    anchor : '95%',
												    name : 'processArea',								    
												    allowBlank : false,
												    maxLength : 50,
												    maxLengthText : '最多只能输入50个字符',
												    value :  record.json["PROCESS_AREA"],
												    fieldLabel : '<font color=red>区域*</font>'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'textfield',
												    anchor : '95%',
												    name : 'processCode',								    
												    allowBlank : false,
												    disabled : true,
												    value :  record.json["PROCESS_CODE"],
												    fieldLabel : '<font color=red>工序代码*</font>'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'textfield',
												    anchor : '95%',
												    name : 'processTool',
												    maxLength : 15,
												    maxLengthText : '最多只能输入15个字符',
												    value :  record.json["PROCESS_TOOL"],
												    fieldLabel : '工具使用'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'textfield',
												    anchor : '95%',
												    name : 'processName',								    
												    allowBlank : false,
												    disabled : true,
												    value :  record.json["PROCESS_NAME"],
												    fieldLabel : '<font color=red>工序名称*</font>'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'numberfield',
												    anchor : '95%',
												    name : 'intensityValue',
												    minValue : 0.01,
												    maxValue : 10,
												    decimalPrecision:2,
												    value :  record.json["INTENSITY_VALUE"],
												    maxText  : '强度需求最大值为10',
												    minText : '强度需求最小值为0.01',
												    fieldLabel : '强度需求'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'numberfield',
												    anchor : '95%',
												    name : 'estimateValue',
												    minValue : 0,
												    maxValue : 5,
												    decimalPrecision:1,			
												    value :  record.json["ESTIMATE_VALUE"],
												    maxText  : '判断需求最大值为5',
												    minText : '判断需求最小值为0',
												    fieldLabel : '判断需求'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'numberfield',
												    anchor : '95%',
												    name : 'difficultyValue',
												    minValue : 0.01,
												    maxValue : 10,
												    decimalPrecision:2,
												    disabled : true,
												    value :  record.json["DIFFICULTY_VALUE"],
												    maxText  : '难度系数最大值为10',
												    minText : '难度系数最小值为0.01',
												    fieldLabel : '总部难度系数'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'numberfield',
												    anchor : '95%',
												    name : 'skillValue',
												    minValue : 0.01,
												    maxValue : 10,
												    decimalPrecision:2,
												    value :  record.json["SKILL_VALUE"],
												    maxText  : '技能需求最大值为10',
												    minText : '技能需求最小值为0.01',
												    fieldLabel : '技能需求'
											  }]
										},{
											  columnWidth : .45,
											  border : false,
											  layout : 'form',
											  items : [{
												    xtype : 'numberfield',
												    anchor : '95%',
												    name : 'difficultyModifyValue',
												    minValue : 0.01,
												    maxValue : 10,
												    decimalPrecision:2,
												    value :  record.json["DIFFICULTY_MODIFY_VALUE"],
												    maxText  : '难度系数调整最大值为10',
												    minText : '难度系数调整最小值为0.01',
												    fieldLabel : '难度系数调整'
											  }]
										}],
										fbar : [{
											text : '保存',
											handler : function(){
													var form = this.ownerCt.ownerCt;
													if(form.getForm().isValid ()){
															form.getForm().submit({
																url: '../operation/processMgt_updateProcess.action',
																success: function(form, action) {
																	Ext.Msg.alert('提示', action.result.msg);
																	win.close();
																	queryProcess();
																},
																failure : function(form, action) {
																	Ext.Msg.alert('提示', action.result.msg);
																	win.close();
																},
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

//导出
var exportProcess = function(){
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
			url : "../operation/processMgt_exportProcess.action",
			method : 'POST',
			params : {
				deptId : deptId,
				processCode : Ext.getCmp('query.processCode').getValue(),
				processName : Ext.getCmp('query.processName').getValue()
			},
			success : function(res,config){
					myMask.hide();
					var obj = Ext.decode(res.responseText);
					if(!obj.success){
						Ext.Msg.alert("提示",obj.msg);
						return;
					}
					var url = obj.fileName;								
					window.location= '../common/downloadReportFile.action?'  + encodeURI(encodeURI(url));
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
		url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=工序导入模板.xls&moduleName=operation&entityName=SchedulingBase&isTemplate=true";
		window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		//window.location = "../pages/schedulingBase/template/工序导入模板.xls";
}

//导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}

//导入
var importProcess = function(){
	var node = treePanel.getSelectionModel ().getSelectedNode();
	if(node == null || node.attributes.code !='001'){
		Ext.Msg.alert("提示","只能选择总部网点！");
		return;
	}
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
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">导入文件模板下载</a>'
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
												url: '../operation/processUploadFile.action',												
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
												},
												waitTitle:"请稍后",
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

//工序确认
var confirmProcess = function(){
	//取当前用户所属网点的工序信息		
	Ext.Ajax.request({
					url : '../operation/processMgt_queryUserProcess.action',
					method : 'POST',
					success : function(res,config){
							var dataObj = Ext.decode(res.responseText);
							dataObj = dataObj.data;
							var processObj = {};
							for(var i=0;i<dataObj.length;i++){
									var obj  =  dataObj[i];
									processObj[obj.processName]=obj.processCode;
							}
							winShow(processObj);
					},
					failure : function(res,config){
							Ext.Msg.alert("提示","找不到用户网点信息！");
					}
			});
	//显示工序确认窗口			
	var winShow = function (processObj){
			// 列头构建
			var cm =  new Ext.grid.ColumnModel({
				 columns:  [
				 new Ext.grid.RowNumberer({header : '序号',width : 35}),
				 {
			 			header: '网点工序', align: 'center',width : 100,sortable: true, dataIndex: 'W',
			 			renderer : function(value,metadata,record,rowIndex,colIndex,store){
		 					var id = "ck_"+ rowIndex+colIndex;						
		 					if(record.json.processName!= null && processObj[record.json.processName]){
		 						record.data.W = true;
		 						return "<input type='checkbox'  id='"+id+"' checked = true />";					 						
		 					}				 						
		 					else{
		 						return "<input type='checkbox'  id='"+id+"' />";
		 					}
		 			}
				 },{
			 				header: '工序代码', align: 'center', width : 100, sortable: true, dataIndex: 'processCode'
				 },{	
					 		header : '工序区域', align: 'center',width : 200,sortable : true, dataIndex : 'processArea'
				 },{
				 			header: '工序名称', align: 'center',width : 150,sortable: true, dataIndex: 'processName'
				 },{
				 			header: '工具使用', align: 'center',width : 150,sortable: true, dataIndex: 'processTool'
				 },{
				 			header: '判断需求', align: 'center',width : 100,sortable: true, dataIndex: 'estimateValue'
				 },{
				 			header: '强度需求', align: 'center',width : 100,sortable: true, dataIndex: 'intensityValue'
				 },{
				 			header: '技能需求', align: 'center',width : 100,sortable: true, dataIndex: 'skillValue'
				 },{
				 			header: '总部难度系数', align: 'center',width : 100,sortable: true, dataIndex: 'difficultyValue'
				 },{
				 			header: '难度系数调整',align: 'center', width : 100,sortable: true, dataIndex: 'difficultyModifyValue',
				 			editor :  new Ext.form.NumberField({minValue : 0.01,maxValue : 10,decimalPrecision:2,maxText  : '难度系数最大值为10',minText : '难度系数最小值为0.01'})
				 },{
				 			header: '工序含金量', align: 'center',width : 100,sortable: true, dataIndex: 'VALUE',renderer : function(value,metadata,record,rowIndex,colIndex,store){
				 						if(record.get("difficultyModifyValue")!=""){
				 								return  record.get("difficultyModifyValue");
				 						}else{
				 								return record.get("difficultyValue");
				 						}
				 			}
				 }]
			});
		
			// 数据格构建
			var record = Ext.data.Record.create([{
							name : 'processCode' ,
							mapping : 'processCode',
							type : 'string'
					},{
							name : 'processArea'  ,
							mapping : 'processArea',
							type : 'string'
					},{
							name : 'processName' ,
							mapping : 'processName',
							type : 'string'
					},{
							name : 'processTool' ,
							mapping : 'processTool',
							type : 'string'
					},{
							name : 'estimateValue' ,
							mapping : 'estimateValue',
							type : 'string'
					},{
							name : 'intensityValue' ,
							mapping : 'intensityValue',
							type : 'string'
					},{
						name : 'skillValue' ,
						mapping : 'skillValue',
						type : 'string'
					},{
						name : 'difficultyValue' ,
						mapping : 'difficultyValue',
						type : 'string'
					},{
						name : 'difficultyModifyValue' ,
						mapping : 'difficultyModifyValue',
						type : 'string'
				},{
					name : 'W' ,
					mapping : 'W',
					type : 'string'
			}]);
		
			// 构建数据存储Store
			var store = new Ext.data.Store({
					proxy : new  Ext.data.HttpProxy({
						 url : '../operation/processMgt_findByDeptId.action'		        		
					}),
					reader: new  Ext.data.JsonReader({
						root: 'root',
						totalProperty: 'totalSize'	    		
					},record)					
			}); 
			store.setBaseParam("deptCode","001");
			
			// 表格构建
			var grid = new Ext.grid.EditorGridPanel({
					cm : cm,
					store : store,	    		
					autoScroll : true,
					loadMask : true,
					listeners : {
						cellclick : function(Grid,rowIndex,columnIndex,e){
									if(columnIndex==1){//网点工序
										var r = store.getAt(rowIndex);
										r.data.W = document.getElementById("ck_"+rowIndex+columnIndex).checked;								
									}
						} 
					}
			});
			//工序确认弹出框
			var win = new Ext.Window({
					width:780,
					height:400,
					modal:true,
					border:false,
					bodyBorder:false,
					closable:false,
					resizable:false,
					layout:'fit',
					title : '工序确认',
					items : [grid],
					buttonAlign : 'center',
					fbar : [{text : '确认',handler : function(){
								submitProcess(win);
					}},{text : '取消',handler : function(){
							win.close();
					}}]
			});
			//显示弹出框
			win.show();
			
			//加载数据
			store.load();
			
			//提交工序确认数据
			var submitProcess = function(win){
					var myMask = new Ext.LoadMask(win.getId(), {msg:"Please wait..."});
					myMask.show();
					var dataList = [];
					var count = 0 ;			
					var node = treePanel.getSelectionModel ().getSelectedNode();
					var deptId = node.id;
					//遍历表格数据
					store.each(function(r){						
						if(r.data.W){
								dataList.push({
										'processCode' : r.get("processCode"),
										'processName' : r.get("processName"),
										'processArea' : r.get("processArea"),
										'processTool' : r.get("processTool"),
										'estimateValue' : r.get("estimateValue"),
										'intensityValue' : r.get("intensityValue"),
										'skillValue' : r.get("skillValue"),
										'difficultyValue' : r.get("difficultyValue"),
										'difficultyModifyValue' : r.get("difficultyModifyValue"),
										'deptId':deptId
								})								
							}
						count++;
						if(count==store.getTotalCount()){
									if(dataList.length==0){
										Ext.Msg.alert("提示","请选择工序");
										myMask.hide();
										return ;
									}
									Ext.Ajax.request({
												url : '../operation/processMgt_confirmProcess.action',
												method : 'POST',
												params : {
														dataList : Ext.encode(dataList)
												},
												success : function(res,config){
														Ext.Msg.alert("提示",Ext.decode(res.responseText).msg);														
														myMask.hide();
														win.close();
														var node = node||treePanel.getSelectionModel ().getSelectedNode();													
														if(node!=null && node.id!=0){
																queryProcess();
														}														
												},
												failure : function(res,config){
														Ext.Msg.alert("提示",Ext.decode(res.responseText).msg);
														myMask.hide();
												}
									});
						}
					});					
			}	
	}	
}

//确认工序名称代码的唯一性
var  isOnlyProcess = function(key,value,obj){
		var params ={};
		params[key] = value;
		Ext.Ajax.request({
						url : '../operation/processMgt_isOnlyProcess.action',
						method : 'POST',
						params : params,
						success : function(res,config){
								var dataObj = Ext.decode(res.responseText);
								if(dataObj.success){
										if(key=="processCode")
											onlyProcessCode = dataObj.isOnly;
										if(key=="processName")
											onlyProcessName = dataObj.isOnly;
										if(!dataObj.isOnly)
											obj.markInvalid("此值已存在！");
								}									 
						}
			});
}

/**
 * 右下角提示信息window  begin
 */
//通知界面Store
var noticeStore = new Ext.data.JsonStore({
	url:"../operation/processMgt_pushMsg.action",
	totalProperty:'totalSize',
	root : 'root'
	,fields: ["processName","createTm","modifiedTm"]
});
//通知界面表格
var noticeGrid = new Ext.grid.GridPanel({
	store: noticeStore,
	cm: new Ext.grid.ColumnModel([
			{header : '工序名称',dataIndex:"processName"},
			{header : '创建时间',dataIndex:"createTm", dateFormat: 'n/j h:ia' },//dateFormat : 'Y-m-d\\TH:i:s'  },
			{header : '修改时间',dataIndex:"modifiedTm", dateFormat: 'n/j h:ia' }
		]),
	viewConfig: {
		        forceFit: true
		 }  
	});
//通知弹出框
var noticeWindow = new Ext.Window({
			title:'最近总部新工序提醒通知',
			height:180,
			width:450,
			closeAction:"hide",
			plain:true,
			modal:false,
			resizable : false,
			layout : 'fit',
			items: [noticeGrid]
});

//移动window通知框
var h =0;
var moveWindow = function(){
		noticeWindow.setPosition(document.body.clientWidth-450,document.body.clientHeight-h);
		h=h+5;
		if(185==h){
			runner.stop(moveTask);
			h=0;
		}
}

//移动window的任务
var moveTask = {
			 run : function(){
				 moveWindow();
			 },
			  interval: 10//0.01秒
}

//显示通知框
var isShow = function(){
		if(noticeStore.getCount()>0){
				noticeWindow.show();		
				runner.start(moveTask);
		}		
}

//定义数据查询任务
var task = {
	    run: function(){
	    	noticeStore.removeAll();
	    	noticeStore.load({callback:isShow});	    	
	    },
	    interval: 300000//5分钟
}

//定义多线程任务
var runner = new Ext.util.TaskRunner();
//执行任务task
runner.start(task);

/**
 * 右下角提示信息window  end
 */

