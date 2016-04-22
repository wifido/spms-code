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
  				 		queryEmployee(node);
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
			queryEmployee();
	}
});
// 新增按钮
var btnAdd = new Ext.Button({
	text : "新 增",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			 addEmployee();
	}
});
// 修改按钮
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
			updateEmployee(record);
	}
});
// 删除按钮
var btnDelete = new Ext.Button({
	text : "删 除",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
		var records = grid.getSelectionModel().getSelections();
		if(records.length>0){
			var codes="";
			var ids ="";
			var obj = {};
			for(var i=0;i<records.length;i++){
				 var record = records[i];
				 codes += record.json["EMP_CODE"]+"@@";
				 ids += record.json["EMP_ID"]+"@@";
				 obj[record.json["EMP_CODE"]]=record;
				 if(record){
						if(record.json["EMP_CODE"].length<9){
							Ext.Msg.alert("提示","内部人员不能删除！");
							return;
						}
				 }
			}
			Ext.Ajax.request({
	    			url : '../schedulingBase/outEmployeeMgt_isSchedulMgtbyEmpCode.action',
	    			method : 'POST',
	    			params : {
	    				empCodes : codes
	    			},
	    			success : function(res,config){
	    				var data = Ext.decode(res.responseText);
	    				if(data.success==true){
	    						deleteEmployee(ids);
	    				}else{
		    					var msg = "";
	    						for(var j=0;j<data.root.length;j++){
	    							var id = data.root[j];
	    							msg += obj[id].json["EMP_NAME"]+";"
	    						}
	    						msg = msg +"已排班不能删除！"
	    						Ext.Msg.alert("提示",msg);
	    						return;
	    				}
	    			},
	    			failure : function(){
	    					Ext.Msg.alert("提示","人员删除出错！");
	    					return;
	    			}
			 });
		}else{
			 Ext.Msg.alert("提示","请选择要删除的数据！");
			 return;
		}
	}
});

// 人员信息同步
var btnSynchronization = new Ext.Button({
	text : "人员信息同步",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			synchronizationEmployee();
	}
});

// 外包人员导入
var btnImport = new Ext.Button({
	text : "外包人员导入",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			importEmployee();
	}
});

// 导出按钮
var btnExport = new Ext.Button({
	text : "导 出",
	cls : "x-btn-normal",
	pressed : true,
	minWidth : 60,
	handler : function(){
			exportEmployee();
	}
});

// 顶部的Panel
var topPanel = new Ext.Panel({
				frame : true,
				layout:'column',
				height : 160,
				tbar:['-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt_queryOutEmployee.action">,btnSearch</app:isPermission>
				      	,'-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt_saveEmployee.action">,btnAdd</app:isPermission>
				       , '-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt_updateEmployee.action">,btnEdit</app:isPermission>
				        ,'-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt_deleteEmployee.action">,btnDelete</app:isPermission>
				      	,'-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt.action">,btnSynchronization</app:isPermission>
				      	,'-'
				     <app:isPermission code="/schedulingBase/employeeUploadFile.action">,btnImport</app:isPermission>
				      	,'-'
				      <app:isPermission code="/schedulingBase/outEmployeeMgt_exportEmployee.action">,btnExport</app:isPermission>
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
								id:'query.empcode',
								fieldLabel:'工号',
								anchor : '90%'
							}]
				},{
					  columnWidth : .4,
					  labelWidth:120,
					  labelAlign:'right',
					  layout:'form',
					  items : [{
							xtype:'textfield',
							id:'query.empname',
							fieldLabel:'姓名',
							anchor : '90%'
						}]
			},{
				  columnWidth : .4,
				  labelWidth:120,
				  labelAlign:'right',
				  layout:'form',
				  items : [{
						xtype:'combo',
						id:'query.empStatus',
						typeAhead: true,
					    triggerAction: 'all',
					    lazyRender:true,
					    mode: 'local',
						fieldLabel:'在职状态',
						store : [['1','在职'],['0','离职']],
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
	 sm,
	 {
	 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
	 },{
	 			header: '网点代码', sortable: true, dataIndex: 'DEPT_CODE'
	 },{
	 			header: '工号', sortable: true, dataIndex: 'EMP_CODE'
	 },{
	 			header: '姓名',  sortable: true, dataIndex: 'EMP_NAME'
	 },{
	 			header: '职位名称',  sortable: true, dataIndex: 'EMP_DUTY_NAME'
	 },{
	 			header: '在职状态',  sortable: true, dataIndex: 'EMP_STATUS',
	 			renderer : function(val){
	 					if(val==1)
	 						return "在职";
	 					else if(val==0)
	 						return "离职";
	 					else
	 						return val;
	 			}
	 }]
});

// 数据格构建
var record = Ext.data.Record.create([{
				name : 'AREA_CODE' ,
				mapping : 'AREA_CODE',
				type : 'string'
		},{
				name : 'DEPT_CODE'  ,
				mapping : 'DEPT_CODE',
				type : 'string'
		},{
				name : 'EMP_CODE' ,
				mapping : 'EMP_CODE',
				type : 'string'
		},{
				name : 'EMP_NAME' ,
				mapping : 'EMP_NAME',
				type : 'string'
		},{
				name : 'EMP_DUTY_NAME' ,
				mapping : 'EMP_DUTY_NAME',
				type : 'string'
		},{
				name : 'EMP_STATUS' ,
				mapping : 'EMP_STATUS',
				type : 'string'
		}]);

// 构建数据存储Store
var store = new Ext.data.Store({
		proxy : new  Ext.data.HttpProxy({
			 url : '../schedulingBase/outEmployeeMgt_queryOutEmployee.action'
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


// 查询方法
var queryEmployee = function(node){
			var node = node||treePanel.getSelectionModel ().getSelectedNode();
			var deptId = "";
			if(node!=null && node.id!=0){
				deptId = node.id;
			}else{
				Ext.Msg.alert("提示","请先选择网点！");
				return;
			}
			store.setBaseParam("deptId",deptId);
			store.setBaseParam("empcode",Ext.getCmp('query.empcode').getValue());
			store.setBaseParam("empname",Ext.getCmp('query.empname').getValue());
			store.setBaseParam("empStatus", Ext.getCmp('query.empStatus').getValue());
			store.load({
					params : {
							start: 0,
					        limit: 20
					}
			});
}

// 新增方法
var addEmployee = function(){
	var node = treePanel.getSelectionModel ().getSelectedNode();
	//工号
	var empCode = "";
	//网点代码
	var deptCode = "";
	if(node!=null && node.leaf){
		deptId = node.id;
		deptCode=node.attributes.code;
	}else{
		Ext.Msg.alert("提示","只有中转场和航空操作组才能新增外包人员信息！");
		return;
	}
	Ext.Ajax.request({
				url : '../schedulingBase/outEmployeeMgt_queryInsertEmpCode.action',
				method : 'POST',
				success : function(res,config){
						var data = Ext.decode(res.responseText);
						empCode = data.root[0]["NEXTVAL"];
						win.show();
						if(Ext.getCmp("empCodeId")){
								Ext.getCmp("empCodeId").setValue(empCode);
						}
				},
				failure : function(res,config){
						Ext.Msg.alert("提示","工号获取失败！");
				}
	});
	var groupStore = new Ext.data.Store({
				        proxy:new Ext.data.HttpProxy({
				            url:'../schedulingBase/queryGroup.action'
				        }),
				        reader:new Ext.data.JsonReader({
				            	fields:['GROUP_ID','GROUP_NAME']
				        }
				    )
		});
	groupStore.setBaseParam("deptId",deptId);

	var win = new Ext.Window({
		width:780,
		height:230,
		modal:true,
		border:false,
		bodyBorder:false,
		closable:false,
		resizable:false,
		layout:'fit',
		title : '外包人员新增',
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
					    readOnly : true,
					    name : 'empCode',
					    id : 'empCodeId',
					    allowBlank : false,
					    maxLength : 20,
					    fieldLabel : '<font color=red>工号*</font>'
				  }]
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',
					    allowBlank : false,
					    name : 'empName',
					    maxLength : 20,
					    fieldLabel : '<font color=red>姓名*</font>'
				  }]
			},{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'combo',
					    anchor : '95%',
					    name : 'groupName',
					    hiddenName : 'groupId',
					    typeAhead: true,
					    triggerAction: 'all',
					    lazyRender:true,
					    editable : false,
					    displayField : 'GROUP_NAME',
					    valueField  : 'GROUP_ID',
					    store : groupStore,
					    allowBlank : false,
					    fieldLabel : '<font color=red>小组名称*</font>'
				  }]
			},
/*			{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'combo',
					    anchor : '95%',
					    typeAhead: true,
					    triggerAction: 'all',
					    lazyRender:true,
					    hiddenName : 'empStatus',
					    name : 'status',
					    store : [['1','在职'],['0','离职']],
					    allowBlank : false,
					    fieldLabel : '<font color=red>在职状态*</font>'
				  }]
			},*/
			{
			  columnWidth : .45,
			  border : false,
			  layout : 'form',
			  items : [{
					xtype : 'datefield',
				    anchor : '95%',
				    format : 'Y-m-d',
				    name : 'dimissionDt',
				    editable : false,
				    fieldLabel : '离职时间'
				   }]
			},
			{
				  columnWidth : .45,
				  border : false,
				  layout : 'form',
				  items : [{
					    xtype : 'textfield',
					    anchor : '95%',
					    name : 'empDutyName',
					    maxLength : 20,
					    fieldLabel : '职位名称'
				  }]
			}],
			fbar : [{
				text : '保存',
				handler : function(){
						var form = this.ownerCt.ownerCt;
						if(form.getForm().isValid ()){
								form.getForm().submit({
									url: '../schedulingBase/outEmployeeMgt_saveEmployee.action',
									success: function(form, action) {
										Ext.Msg.alert('提示', action.result.msg);
										win.close();
										queryEmployee();
									},
									failure : function(form, action) {
										Ext.Msg.alert('提示', action.result.msg);
										//win.close();
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
}

//修改方法
var updateEmployee = function(record){

	if(record==null){
		Ext.Msg.alert("提示","请选择要修改的数据！");
		return;
	}
	var deptId =  record.json["DEPT_ID"];
	var groupStore = new Ext.data.Store({
		        proxy:new Ext.data.HttpProxy({
		            url:'../schedulingBase/queryGroup.action'
		        }),
		        reader:new Ext.data.JsonReader({
		            	fields:['GROUP_ID','GROUP_NAME']
		        }
		    )
		});
		groupStore.setBaseParam("deptId",deptId);
		//是否外包人员
		var isOutEmployee = record.json["EMP_CODE"]	.length>=9?true:false;
		var win = new Ext.Window({
					width:780,
					height:230,
					modal:true,
					border:false,
					bodyBorder:false,
					closable:false,
					resizable:false,
					layout:'fit',
					title : '人员信息修改',
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
							name : 'isOutEmployee',
							value : isOutEmployee
						},{
							xtype : 'hidden',
							name : 'empId',
							value : record.json["EMP_ID"]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',
								    disabled  : true,
								    name : 'deptCode',
								    allowBlank : false,
								    value : record.json["DEPT_CODE"],
								    fieldLabel : '<font color=red>网点代码*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',
								    disabled : true,
								    name : 'empCode',
								    id : 'empCodeId',
								    allowBlank  : false,
								    value : record.json["EMP_CODE"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>工号*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',
								    allowBlank : false,
								    name : 'empName',
								    disabled  : true,
								    value : record.json["EMP_NAME"],
								    maxLength : 20,
								    fieldLabel : '<font color=red>姓名*</font>'
							  }]
						},{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'combo',
								    anchor : '95%',
								    name : 'groupName',
								    hiddenName : 'groupId',
								    typeAhead: true,
								    triggerAction: 'all',
								    lazyRender:true,
								    displayField : 'GROUP_NAME',
								    valueField  : 'GROUP_ID',
								    store : groupStore,
								    editable : false,
								    allowBlank : false,
								    fieldLabel : '<font color=red>小组名称*</font>',
								    maxLength : 20,
								    listeners : {
								    		render : function(combo){
								    					var s = combo.store;
								    					s.on("load",function(Store,records,options){
								    							combo.setValue(record.json["GROUP_ID"]);
								    							s.purgeListeners();
								    					});
								    					s.load();
								    		}
								    }
							  }]
						},
/*						{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'combo',
								    anchor : '95%',
								    typeAhead: true,
								    triggerAction: 'all',
								    lazyRender:true,
								    readOnly : !isOutEmployee,
								    allowBlank : false,
								    hiddenName : 'empStatus',
								    name : 'status',
								    store : [['1','在职'],['0','离职']],
								    value : record.json["EMP_STATUS"],
								    fieldLabel : '<font color=red>在职状态*</font>'
							  }]
						},*/
						{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
									xtype : 'datefield',
								    anchor : '95%',
								    format : 'Y-m-d',
								    name : 'dimissionDt',
								    disabled  : !isOutEmployee,
								    editable : false,
								    value : record.json["DIMISSION_DT"]==null?"":record.json["DIMISSION_DT"].substr(0,10),
								    fieldLabel : '离职时间'
								   }]
							},
						{
							  columnWidth : .45,
							  border : false,
							  layout : 'form',
							  items : [{
								    xtype : 'textfield',
								    anchor : '95%',
								    name : 'empDutyName',
								    disabled  : !isOutEmployee,
								    value : record.json["EMP_DUTY_NAME"],
								    maxLength : 20,
								    fieldLabel : '职位名称'
							  }]
						}],
						fbar : [{
							text : '保存',
							handler : function(){
									var form = this.ownerCt.ownerCt;
									if(form.getForm().isValid ()){
											form.getForm().submit({
												url: '../schedulingBase/outEmployeeMgt_updateEmployee.action',
												success: function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													win.close();
													queryEmployee();
												},
												failure : function(form, action) {
													Ext.Msg.alert('Success', action.result.msg);
													//win.close();
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
var deleteEmployee = function(ids){
			if(ids){
				Ext.Msg.confirm ("提示","是否确定删除该记录？",function(btn){
					if(btn=="yes"){
							Ext.Ajax.request({
										url : '../schedulingBase/outEmployeeMgt_deleteEmployee.action',
										method : 'POST',
										params : {
											empIds : ids
										},
										success : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
												queryEmployee();
										},
										failure : function(res,config){
												var obj = Ext.decode(res.responseText);
												Ext.Msg.alert("提示",obj.msg);
										}
							});
					}
				}, this)
			}else{
				 Ext.Msg.alert("提示","请选择要删除的数据！");
				 return;
			}
}

//导出方法
var exportEmployee=function(){
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
						url : "../schedulingBase/outEmployeeMgt_exportEmployee.action",
						method : 'POST',
						params : {
							deptId : deptId,
							empcode : Ext.getCmp('query.empcode').getValue(),
							empname : Ext.getCmp('query.empname').getValue(),
							empStatus : Ext.getCmp('query.empStatus').getValue()
						},
						success : function(res,config){
								myMask.hide();
								var obj = Ext.decode(res.responseText);
								if(!obj.success){
									Ext.Msg.alert("提示",obj.msg);
									return;
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
		url = "uniquePart=6adf1b32-31f8-4125-91ec-b50ba39bc4d9admin&fileName=外包人员导入模板.xls&moduleName=schedulingBase&entityName=SchedulingBase&isTemplate=true";
		window.location =  '../common/downloadReportFile.action?' + encodeURI(encodeURI(url));
		//window.location = "../pages/schedulingBase/template/外包人员导入模板.xls";
}

//导入功能校验没通过返回数据下载
var downError = function(objectA){
		window.location= objectA.attributes.url.nodeValue;
}

//导入方法
var importEmployee = function(){
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
									html : '<font size=3>模板文件： </font> <a href="#" onclick="downTemp()">外包人员导入模板下载</a>'
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
												url: '../schedulingBase/employeeUploadFile.action',
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
													Ext.Msg.alert('提示', action.result.msg);
													win.close();
												} ,
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

//同步人员信息查询
var querySynEmployee = null;

//人员信息同步
var synchronizationEmployee =function(){
				//同步信息查询
				var synBtnQuery = new Ext.Button({
					text : "查 询",
					cls : "x-btn-normal",
					pressed : true,
					minWidth : 60,
					handler : function(){
						querySynEmployee();
					}
				});

				//同步信息查询
				var synBtn = new Ext.Button({
					text : "同 步",
					cls : "x-btn-normal",
					pressed : true,
					minWidth : 60,
					handler : function(){
						var record = synGrid.getSelectionModel().getSelected();
						submitSynEmployee(record);
					}
				});
				// 复选框
				var synSm = new Ext.grid.CheckboxSelectionModel({singleSelect : true});
				// 列头构建
				var synCm =  new Ext.grid.ColumnModel({
					 columns:  [
					 synSm,
					 {
					 			header: '地区代码', sortable: true, dataIndex: 'AREA_CODE'
					 },{
					 			header: '场地代码', sortable: true, dataIndex: 'DEPT_CODE'
					 },{
					 			header: '工号', sortable: true, dataIndex: 'EMP_CODE'
					 },{
					 			header: '姓名',  sortable: true, dataIndex: 'EMP_NAME'
					 },{
					 			header: '职位名称',  sortable: true, dataIndex: 'POSITION_NAME'
					 },{
					 			header: '操作类型',  sortable: true, dataIndex: 'EMP_STUS',renderer : function(value,metadata,record,rowIndex,colIndex,store ){
					 					var title =value==1? "新增":"转网点";
					 					return title;
				 			}
					 }]
				});

				// 数据格构建
				var synRecord = Ext.data.Record.create([{
								name : 'AREA_CODE' ,
								mapping : 'AREA_CODE',
								type : 'string'
						},{
								name : 'DEPT_CODE'  ,
								mapping : 'DEPT_CODE',
								type : 'string'
						},{
								name : 'EMP_CODE' ,
								mapping : 'EMP_CODE',
								type : 'string'
						},{
								name : 'EMP_NAME' ,
								mapping : 'EMP_NAME',
								type : 'string'
						},{
								name : 'POSITION_NAME' ,
								mapping : 'POSITION_NAME',
								type : 'string'
						},{
							name : 'EMP_STUS' ,
							mapping : 'EMP_STUS',
							type : 'string'
					}]);

				// 构建数据存储Store
				var synStore = new Ext.data.Store({
						proxy : new  Ext.data.HttpProxy({
							 url : '../schedulingBase/outEmployeeMgt_queryHrEmp.action'
						}),
						reader: new  Ext.data.JsonReader({
							root: 'root',
							totalProperty: 'totalSize'
						},synRecord)
				});

				// 分页组件
				var synPageBar =   new Ext.PagingToolbar({
						    store: synStore,
						    displayInfo: true,
						    displayMsg : '当前显示 {0} - {1} 总记录数目 {2}',
						    pageSize: 20,
						   emptyMsg : '未检索到数据'
				})
				// 表格构建
				var synGrid = new Ext.grid.GridPanel({
						cm : synCm,
						sm : synSm,
						store : synStore,
						autoScroll : true,
						tbar : synPageBar,
						loadMask : true,
						 viewConfig: {
						        forceFit: true
						 }
				});

				var win = new Ext.Window({
						width:880,
						height:500,
						modal:true,
						border:false,
						bodyBorder:false,
						closable:true,
						closeAction : 'close',
						resizable:false,
						layout:'form',
						title : '人员同步',
						items : [{
								 xtype : 'panel',
								 border : false,
								 items : [{
												xtype:'fieldset',
												title:'查询条件',
												layout:"column",
												style : 'margin-top:5px;',
												autoHeight : true,
												autoWidth : true,
												items : [{
													  columnWidth : .4,
													  labelWidth:120,
													  labelAlign:'right',
													  layout:'form',
													  border : false,
													  items : [{
															xtype:'textfield',
															id:'syn.empName',
															fieldLabel:'姓名',
															anchor : '90%'
														}]
											},{
												  columnWidth : .4,
												  labelWidth:120,
												  labelAlign:'right',
												  layout:'form',
												  border : false,
												  items : [{
														xtype:'textfield',
														id:'syn.empCode',
														fieldLabel:'工号',
														anchor : '90%'
													}]
										}]
									}],
									tbar : ['-',
										      <app:isPermission code="/schedulingBase/outEmployeeMgt_queryHrEmp.action">synBtnQuery</app:isPermission>
										      	,'-'
										      <app:isPermission code="/schedulingBase/outEmployeeMgt_synEmployee.action">,synBtn</app:isPermission>]
							},{
								xtype : 'panel',
								border : false,
								items : [{
									xtype:'fieldset',
									title:'查询结果',
									style : 'margin-top:5px;',
									autoWidth : true,
									height : 340,
									layout : 'fit',
									items : [synGrid]
								}]
							}]
				});
			 win.show();


			querySynEmployee = function(){
			 		synStore.setBaseParam("empCode",Ext.getCmp('syn.empCode').getValue());
			 		synStore.setBaseParam("empName",Ext.getCmp('syn.empName').getValue());
			 		synStore.load({
			 				params : {
			 						start: 0,
			 				        limit: 20
			 				}
			 		});
			 }

}



//人员信息同步处理
var submitSynEmployee = function(record){
	//取小数点后1位
	function fomatFloat(src,pos){
		 return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
	}
			if(record==null){
				Ext.Msg.alert("提示","请选择选择需要同步的记录！");
				return;
			}
			var deptCode=  record.json["DEPT_CODE"];
			var groupStore = new Ext.data.Store({
				        proxy:new Ext.data.HttpProxy({
				            url:'../schedulingBase/queryGroup.action'
				        }),
				        reader:new Ext.data.JsonReader({
				            	fields:['GROUP_ID','GROUP_NAME']
				        }
				    )
				});
				groupStore.setBaseParam("deptCode",deptCode);
				//计算工龄
				var workAge = "";
				try{
						var sfDate = null;
						if(Ext.isIE6 || Ext.isIE7 || Ext.isIE8){
							 var dateStr = record.json["SF_DATE"].replace(/-/g,"/");
							 sfDate = new Date(Ext.util.Format.date(dateStr,'Y/m/d'));
						}else{
							  sfDate = Date.parseDate(Ext.util.Format.date(record.json["SF_DATE"],'Y-m'),'Y-m');
						}
						var quitDate = new Date();
						var year = quitDate.getFullYear() - sfDate.getFullYear();
						var month = quitDate.getMonth() - sfDate.getMonth();
						workAge = (year*12 + month)/12;
						workAge =fomatFloat(workAge, 1)
				}catch(e){}
				//用工类型数据转换
				var workType = "";
				switch(record.json["PERSON_TYPE"]){
					case "非全日制工" :
						workType =1
						break;
					case "基地见习生" :
						workType =2
						break;
					case "劳务派遣" :
						workType =3
						break;
					case "全日制员工" :
						workType =4
						break;
					case "实习生" :
						workType =5
						break;
					case "外包" :
						workType =6
						break;
				}
				var  type = record.json["EMP_STUS"];
				var title =type==1? "人资同步操作为新增":"人资同步操作为更新";
				var win = new Ext.Window({
							width:780,
							height:230,
							modal:true,
							border:false,
							bodyBorder:false,
							closable:false,
							resizable:false,
							layout:'fit',
							title : title,
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
										name : 'empStus',
										value : type
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'deptCode',
										    allowBlank : false,
										    readOnly : true,
										    value : deptCode,
										    fieldLabel : '<font color=red>网点代码*</font>'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'empCode',
										    id : 'empCodeId',
										    allowBlank : false,
										    readOnly : true,
										    value : record.json["EMP_CODE"],
										    fieldLabel : '<font color=red>工号*</font>'
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
										    name : 'empName',
										    value : record.json["EMP_NAME"],
										    fieldLabel : '<font color=red>姓名*</font>'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'PERSON_TYPE',
										    value : record.json["PERSON_TYPE"],
										    readOnly : true,
										    fieldLabel : '用工类型'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  hidden : true,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'workType',
										    value : workType,
										    readOnly : true,
										    fieldLabel : '用工类型'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'workAge',
										    value : workAge,
										    readOnly : true,
										    fieldLabel : '工龄'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'empDutyName',
										    value : record.json["POSITION_NAME"],
										    readOnly : true,
										    fieldLabel : '职位'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  layout : 'form',
									  items : [{
										    xtype : 'combo',
										    anchor : '95%',
										    name : 'groupName',
										    hiddenName : 'groupId',
										    typeAhead: true,
										    triggerAction: 'all',
										    lazyRender:true,
										    displayField : 'GROUP_NAME',
										    valueField  : 'GROUP_ID',
										    store : groupStore,
										    allowBlank : false,
										    editable : false,
										    fieldLabel : '<font color=red>小组名称*</font>',
										    listeners : {
										    	select : function (combo,record,index) {
										    			Ext.getCmp("disableDt").setValue(record.json["DISABLE_DT"]);
										    	}
										    }
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  hidden : true,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'disableDt',
										    id : 'disableDt',
										    fieldLabel : '小组失效日期'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  hidden : true,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'email',
										    value : record.json["EMP_EMAIL"],
										    fieldLabel : 'Email'
									  }]
								},{
									  columnWidth : .45,
									  border : false,
									  hidden : true,
									  layout : 'form',
									  items : [{
										    xtype : 'textfield',
										    anchor : '95%',
										    name : 'dimissionDt',
										    value : record.json["CANCEL_DATE"],
										    fieldLabel : '离职日期'
									  }]
								}
								],
								fbar : [{
									text : '保存',
									handler : function(){
											var form = this.ownerCt.ownerCt;
											if(form.getForm().isValid ()){
													form.getForm().submit({
														url: '../schedulingBase/outEmployeeMgt_synEmployee.action',
														success: function(form, action) {
															Ext.Msg.alert('Success', action.result.msg);
															win.close();
															querySynEmployee();
														},
														failure : function(form, action) {
															Ext.Msg.alert('提示', action.result.msg);
															//win.close();
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


/**
 * 右下角提示信息window  begin
 */
//通知界面Store
var noticeStore = new Ext.data.JsonStore({
	url:"../schedulingBase/outEmployeeMgt_queryHrEmp.action",
	totalProperty:'totalSize',
	 root : 'root'
	,fields: ["EMP_NAME","EMP_CODE","EMP_STUS"]
});
//通知界面表格
var noticeGrid = new Ext.grid.GridPanel({
	store: noticeStore,
	cm: new Ext.grid.ColumnModel([
	             {id:"id",header : '姓名',dataIndex:"EMP_NAME"},
	             {id:"id2",header : '工号',dataIndex:"EMP_CODE"},
	             {id:"id3",header : '操作类型',dataIndex:"EMP_STUS",
	            	 renderer : function(value,metadata,record,rowIndex,colIndex,store ){
	 					var title =value==1? "新增":"转网点";
	 					return title;
	 			   }
	             }
		]),
		viewConfig: {
	        forceFit: true
		}
	});
//通知弹出框
var noticeWindow = new Ext.Window({
			title:'通知',
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

// 初始化
Ext.onReady(function() {
		Ext.QuickTips.init();
		Ext.Ajax.timeout=300000;
		Ext.BLANK_IMAGE_URL="../ext-3.4.0/resources/images/default/s.gif";
		var viewreprot = new Ext.Viewport({
			layout : "border",
			items : [ treePanel,centerPanel]
		});
	});