<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ext" uri="/ext-tags"%>
<%@ taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ext:ui scripts="${scripts}/checkTreeExtend.js">
	<ext:viewport layout="border">
		<ext:items>
			<ext:panel region="west" layout="border" width="200">
				<ext:items>
					<ext:formPanel region="north" height="40" border="false" frame="true" var="consigntypeForm1" layout="column">
						<ext:items>
							<ext:panel width="155">
								<ext:items>
									<ext:textField name="roleFil" var="roleNameQuery" width="155"/>
								</ext:items>
							</ext:panel>
							<ext:panel width="30">
								<ext:items>
									<app:isPermission code="/authorization/roleRoleList.action"><ext:button cls="x-btn-icon" icon="${images}/search.gif" handler="searchRole"/></app:isPermission>
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:formPanel>
					<ext:gridPanel var="roleListGrid" region="center" frame="false" border="false" autoScroll="true">
						<ext:store url="roleRoleList.action">
							<ext:jsonReader>
								<ext:fields type="com.sf.module.authorization.domain.Role"/>
							</ext:jsonReader>
						</ext:store>
						<ext:rowSelectionModel singleSelect="true"/>
						<ext:columnModel>
							<ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleList')}" width="300"/>
							<ext:propertyColumnModel dataIndex="id" hidden="true"/>
							<ext:propertyColumnModel dataIndex="typeFlag" hidden="true"/>
							<ext:propertyColumnModel dataIndex="usedTm" hidden="true"/>
							<ext:propertyColumnModel dataIndex="unusedTm" hidden="true"/>
							<ext:propertyColumnModel dataIndex="description" hidden="true"/>
						</ext:columnModel>
					</ext:gridPanel>
				</ext:items>
			</ext:panel>

			<ext:panel region="center" layout="border" frame="true">
				<ext:items>
					<ext:formPanel region="north" height="150" border="true" layout="border" frame="true" timeOut="180000" var="consigntypeForm2">
						<ext:tbar>
							<app:isPermission code="/authorization/roleSaveRole.action"><ext:button text="${app:i18n('add')}" cls="x-btn-text-icon" icon="${images}/../add.gif" handler="addRole" var="btnAdd"/></app:isPermission>
							<app:isPermission code="/authorization/roleUpdateRole.action"><ext:separator/><ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="updateRole" var="btnSave"/></app:isPermission>
							<app:isPermission code="/authorization/roleDelRole.action"><ext:separator/><ext:button text="${app:i18n('delete')}" cls="x-btn-text-icon" icon="${images}/../delete.gif" handler="preDelRole" var="btndel"/></app:isPermission>
							<app:isPermission code="/authorization/roleCopyRole.action"><ext:separator/><ext:button text="${app:i18n('copy')}" cls="x-btn-text-icon" icon="${images}/../copy.png" handler="copyRole" var="btncopy"/></app:isPermission>
						</ext:tbar>
						<ext:submitAction name="updateRoleAction" url="roleUpdateRole.action" success="updateSucess" failure="updateFailure"/>
						<ext:items>
							<ext:panel region="center" frame="true" layout="form">
								<ext:items>
									<ext:hidden var="modulSel" name="modulSel"/>
									<ext:hidden var="modulNoSel" name="modulNoSel"/>
									<ext:hidden var="roleId" name="role.id"/>
									<ext:hidden var="roleSysFlg" name="role.sysInitFlg"/>
									<!-- 下面的属性不做保存时使用，但在复制时会使用 -->
									<ext:hidden var="typeFlag" name="typeFlagHidden"/>
									<ext:hidden var="usedTm" name="usedTmHidden"/>
									<ext:hidden var="unusedTm" name="unusedTmHidden"/>
									<ext:textField fieldLabel="${app:i18n('roleName')}" width="200" name="role.name" maxLength="100" var="roleName" readOnly="true"/>
									<ext:textArea fieldLabel="${app:i18n('roleDef')}" width="200" name="role.description" maxLength="100" var="roleDef"/>
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:formPanel>
					<ext:tabPanel region="center" activeItem="panRoleTree" var="mainPain">
						<ext:items>
							<!-- 权限面版树 -->
							<ext:panel id="panRoleTree" title="${app:i18n('rec')}" layout="border">
								<ext:items>
									<ext:treePanel region="center" autoScroll="true" rootVisible="false" checkModel="cascade" onlyLeafCheckable="false" var="moudleSelectTree">
										<ext:treeLoader url="roleModuleList.action" var="moduleLoader" clsField="-1" textField="name" idField="id" baseAttrs="{uiProvider:Ext.tree.TreeCheckNodeUI}"/>
										<ext:asyncTreeNode text="${app:i18n('sysPriTree')}" var="moduleRoot" checked="false" id="-1" />
									</ext:treePanel>
								</ext:items>
							</ext:panel>
							<!-- 用户面版 -->
							<ext:panel title="${app:i18n('use')}" layout="border" var="userRolePanelVar">
								<ext:items>
									<ext:treePanel region="west" autoScroll="true" width="200">
										<ext:treeLoader url="userDeptList.action" var="deptLoader" textField="deptName" idField="id" handler="selectDept"/>
										<ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRoot" id="0"/>
									</ext:treePanel>

									<ext:panel region="center" layout="border">
										<ext:tbar>
											<ext:button text="${app:i18n('search')}" cls="x-btn-text-icon" icon="${images}/../search.gif" handler="searchData" var="queryUsersBut"/>
											<ext:button text="${app:i18n('relaStr')}" cls="x-btn-text-icon" icon="${images}/../add.gif" handler="updateUsersForRole" var="btnRoleAttach"/>
											<ext:button text="${app:i18n('norelaStr')}" cls="x-btn-text-icon" icon="${images}/../delete.gif" handler="updateUsersForRoleNo" var="btnRoleRelease"/>
										</ext:tbar>
										<ext:items>
											<ext:formPanel var="userQueryForm" region="north" height="90" frame="true" layout="form">
												<ext:items>
													<ext:textField fieldLabel="${app:i18n('userLogin')}" width="220" name="username" var="queryUserName"/>
													<ext:textField fieldLabel="${app:i18n('netCode')}" width="220" name="deptCode" var="queryUserDeptCode"/>
													<ext:checkbox fieldLabel="${app:i18n('isRela')}" width="220" name="roleRelated" var="queryUserRealte" checked="false"/>
													<!-- 保存查询角色 -->
													<ext:hidden name="roleId" var="queryUserRole"/>
													<ext:hidden name="queryUserRoleOld" var="queryUserRoleOld" value="-1"/>
													<!-- 保存查询部门 -->
												</ext:items>
											</ext:formPanel>
											<ext:gridPanel var="userListGrid" region="center" frame="true" border="true" autoExpandColumn="dept">
												<ext:store var="userListStore" url="roleUserList.action" remoteSort="true" listeners="{beforeload:beforeUserListLoad, load:afterUserListLoad}">
													<ext:jsonReader totalProperty="totalSize" root="allUser">
														<ext:fields type="com.sf.module.authorization.domain.User"/>
													</ext:jsonReader>
												</ext:store>
												<ext:pagingToolbar pageSize="20" store="userListStore" var="userListPagingBar" displayInfo="true" />
												<ext:checkboxSelectionModel/>
												<ext:columnModel>
													<ext:propertyColumnModel dataIndex="id" hidden="true"/>
													<ext:propertyColumnModel dataIndex="username" width="80" sortable="true" header="${app:i18n('userLogin')}"/>
													<ext:propertyColumnModel dataIndex="employeeName" width="100" header="${app:i18n('userName')}"/>
													<ext:propertyColumnModel dataIndex="employeeDutyName" width="150" header="${app:i18n('userDutyName')}"/>
													<ext:propertyColumnModel id="dept" dataIndex="dept" width="150" header="${app:i18n('netCode')}" renderer="function(dept){return dept == null ? null : (dept.deptName + '/' + dept.deptCode + (dept.parentCode == null ? '' : ('(' + dept.parentCode + ')')))}" />
												</ext:columnModel>
											</ext:gridPanel>
										</ext:items>
									</ext:panel>
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:tabPanel>
				</ext:items>
			</ext:panel>
		</ext:items>
	</ext:viewport>

	<ext:window var="addWindow" closeAction="hide" width="400" height="240" layout="border" modal="true" frame="true">
		<ext:tbar>
			<ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveRole" var="saveAddBut"/>
			<ext:button text="${app:i18n('copy')}" cls="x-btn-text-icon" icon="${images}/../copy.png" handler="copyRoleDo" var="copyAddBut"/>
			<ext:button text="${app:i18n('reset')}" cls="x-btn-text-icon" icon="${images}/../reset.gif" handler="reSet"/>
		</ext:tbar>
		<ext:items>
			<ext:formPanel region="center" timeout="180000" var="consigntypeForm3" frame="true">
				<ext:submitAction name="saveRoleAction" url="roleSaveRole.action" success="returnSucess" failure="returnFailure"/>
				<ext:submitAction name="copyRoleAction" url="roleCopyRole.action" success="copySucess" failure="copyFailure"/>
				<ext:submitAction name="existRole" url="existRole.action" success="existRoleFlags"/>
				<ext:items>
					<ext:hidden var="roleIdAdd" name="role.id"/>
					<ext:hidden var="typeFlagAdd" name="role.typeFlag"/>
					<ext:textField
						maxLengthText="${app:i18n_arg2('error.maxLength',app:i18n('roleName'),20)}"
						blankText="${app:i18n_arg1('error.notNull',app:i18n('roleName'))}"
						fieldLabel="${app:i18n('roleName')}" allowBlank="false" width="250"
						name="role.name" maxLength="20" var="roleNameAdd">
					</ext:textField>
					<ext:textArea fieldLabel="${app:i18n('roleDef')}"  width="250" height="100"
						name="role.description" maxLength="150" var="descriptionAdd">
					</ext:textArea>
				</ext:items>
			</ext:formPanel>
		</ext:items>
	</ext:window>

	<ext:script>
    userRolePanelVar.disable();
    moudleSelectTree.disable();
    
    // 关联
    function updateUsersForRole() {
        var queryUserRealteValue = true;
        var roleIdFilt = roleId.value;
        var arr = new Array();
        var modifies = userListGrid.getSelectionModel().getSelections();
        if (modifies.length == 0) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('pleSel')}');
            return;
        }
        
        var userIds = "";
        for ( var i = 0; i < modifies.length; i++) {
            arr.push(modifies[i].data);
            if (i == modifies.length - 1) {
                userIds += modifies[i].get("id");
            } else {
                userIds += modifies[i].get("id") + ",";
            }
        }

        var baseDataLogDescVar = "修改用户关联[" + userIds + "]";
        var params = Ext.util.JSON.encode(arr);
        Ext.Ajax.request({
            params : {
                baseDataLogDesc : baseDataLogDescVar,
                userIds : userIds,
                selected : params,
                queryUserRealte : queryUserRealteValue,
                roleFil : roleIdFilt
            },
            url : "roleUpdateUsers.action",
   			success : function(response) {
   			    var resp = Ext.util.JSON.decode(response.responseText);
   				if (resp.success) {
   					userListGrid.getStore().load();
   				    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveSuccess')}');
   				} else {
   				    Ext.MessageBox.alert('${app:i18n('prompt')}', resp.msg);
   				}
   			},
   			failure : function(response) {
   			    if(Ext.util.JSON.encode(response.status) == 0){
   			        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.connection.exception')}');
   			    } else {
   			        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.exception')}');
   			    }
   			}            
        });
    }
    
    //不关联
    function updateUsersForRoleNo() {
        var queryUserRealteValue = false;
        var roleIdFilt = roleId.value;
        var arr = new Array();
        var modifies = userListGrid.getSelectionModel().getSelections();
        if (modifies.length == 0) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('pleSel')}');
            return;
        }
        
        var userIds = "";
        for ( var i = 0; i < modifies.length; i++) {
            arr.push(modifies[i].data);
            if (i == modifies.length - 1) {
                userIds += modifies[i].get("id");
            } else {
                userIds += modifies[i].get("id") + ",";
            }
        }
        
        var baseDataLogDescVar = "修改用户不关联[" + userIds + "]";
        var params = Ext.util.JSON.encode(arr);
        Ext.Ajax.request({
            params : {
                baseDataLogDesc : baseDataLogDescVar,
                userIds : userIds,
                selected : params,
                queryUserRealte : queryUserRealteValue,
                roleFil : roleIdFilt
            },
            url : "roleUpdateUsers.action",
   			success : function(response){
   			    var resp = Ext.util.JSON.decode(response.responseText);
   				if (resp.success) {
   					userListGrid.getStore().load();
   				    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveSuccess')}');
   				} else {
   				    Ext.MessageBox.alert('${app:i18n('prompt')}', resp.msg);
   				}
   			},
   			failure : function(response) {
   			    if(Ext.util.JSON.encode(response.status) == 0){
   			        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.connection.exception')}');
   			    } else {
   			        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.exception')}');
   			    }
   			}            
        });
    }
    
    function selectDept(node) {
        /* 当选择部门后把帐号清空,查询用户 */
        // 设置部门code，是跟据employ的deptcodt查询，不能跟据用户表的部门查询
        if (typeof (node.attributes.deptCode) == "undefined") {
            queryUserDeptCode.setValue("");
            queryUserName.setValue("");
            userListGrid.getStore().removeAll();
            return;
        }
        queryUserDeptCode.setValue(node.attributes.deptCode);
        queryUserName.setValue("");
        searchData();
    }
    
    deptRoot.on('click',selectDept);    
    
    btnRoleAttach.show();
    btnRoleRelease.hide();
    
    function beforeUserListLoad(store){
    	if (queryUserDeptCode.getValue() == "" && queryUserName.getValue() == ""){
    		Ext.MessageBox.alert('${app:i18n('prompt')}', '${app:i18n('accSearchUser')}');
    		return false;
    	}
    	store.baseParams = userQueryForm.getForm().getValues();
    	store.baseParams["limit"] = userListPagingBar.pageSize;
    }
    
    function afterUserListLoad(store, recs){
        if (queryUserRealte.checked) {
            // 关联按钮隐藏，不关联显示
            btnRoleAttach.hide();
            btnRoleRelease.show();
        } else {
            btnRoleAttach.show();
            btnRoleRelease.hide();
        }
    }
    
    function searchData() {
        userListGrid.getStore().load();
    }
    
   	moudleSelectTree.root.on("append", appendChildNode);
   	function appendChildNode(tree, node, nextNode, index) {
   		if(roleSysFlg.value == 1) {
   			nextNode.disable();
   		} else {
   			nextNode.enable();
   		}
   		nextNode.on("append", appendChildNode);
   	}
    
    roleListGrid.getStore().load();
    
    roleListGrid.addListener("cellclick", onSelectRole);
    function onSelectRole() {
        var arrCust = roleListGrid.getSelectionModel().getSelections();
        roleName.setValue(arrCust[0].data.name);
        roleDef.setValue(arrCust[0].data.description);
        roleId.setValue(arrCust[0].data.id);
      	roleSysFlg.setValue(arrCust[0].data.sysInitFlg);
        
        // 复制 要用的属性
        typeFlag.setValue(arrCust[0].data.typeFlag);
        usedTm.setValue(arrCust[0].data.usedTm);
        unusedTm.setValue(arrCust[0].data.unusedTm);
        
        userRolePanelVar.enable();
        moudleSelectTree.enable();
        
        queryUserRole.setValue(arrCust[0].data.id);
        
        // 对权限树的操作
        var temp = moduleLoader.dataUrl.replace("&clsField=" + queryUserRoleOld.value + "&", 
        	"&clsField=" + queryUserRole.value + "&");
        moduleLoader.dataUrl = temp;
        moudleSelectTree.root.reload();
        queryUserRoleOld.setValue(arrCust[0].data.id);
        
        // 清空全局变量
        unChecked = new Array();
    }
    
    // 新增重置
    function addReset() {
        roleNameAdd.setValue("");
        descriptionAdd.setValue("");
        typeFlagAdd.setValue("");
    }
    
    function searchRole() {
        roleListGrid.getStore().removeAll();
        roleListGrid.getStore().baseParams = consigntypeForm1.getForm().getValues();
        roleListGrid.getStore().load();
    }
    
    // 保存角色成功
    function returnSucess() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveRoleTrue')}');
        addWindow.hide();
        addReset();
        roleListGrid.getStore().load();
    }
    
    // 保存角色失败
    function returnFailure() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveRoleFalse')}');
        addWindow.hide();
        addReset();
        roleListGrid.getStore().load();
    }
    
    // 修改角色成功
    function updateSucess() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('updateRoleTrue')}');
        roleListGrid.getStore().load();
    }
    // 修改角色失败
    function updateFailure() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('updateRoleFalse')}');
        roleListGrid.getStore().load();
    }
    
    // 复制角色成功
    function copySucess() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('copyRoleTrue')}');
        roleListGrid.getStore().load();
    }
    // 复制角色失败
    function copyFailure() {
        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('copyRoleFalse')}');
        roleListGrid.getStore().load();
    }
    
    // 修改角色
    function updateRole() {
        if (roleId.value == null || typeof (roleId.value) == "undefined" || roleId.value == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selectMust')}');
            return;
        }
        var checkedModules = getSelectMoudles();
        var upCheckedModules = getUnSelectMoudles();
        modulNoSel.setValue(upCheckedModules.join());
        modulSel.setValue(checkedModules);
        
        Ext.MessageBox.wait('${app:i18n('saving')}');
        consigntypeForm2.updateRoleAction();
        unChecked = new Array();
        
    }
    
    // 得到选择的菜单权限
    function getSelectMoudles() {
        var returnStr = "";
        var nodes = moudleSelectTree.getChecked();
        for (i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                returnStr += nodes[i].id;
            } else {
                returnStr += nodes[i].id + ",";
            }
        }
        return returnStr;
    }
    
    var unChecked = new Array();
    function getUnSelectMoudles() {
        var treeNodes = moduleRoot.childNodes;
        for ( var j = 0; j < treeNodes.length; j++) {
            if (!treeNodes[j].attributes.checked) {
                unChecked.push(treeNodes[j].attributes.id);
            }
            if (!treeNodes[j].expanded && !treeNodes[j].childrenRendered) {
            } else {
                getNodeSubNode(treeNodes[j]);
            }
        }
        return unChecked;
    }
    
    function getNodeSubNode(node) {
        if (node !== null && !node.expanded && !node.childrenRendered) {
            if (!node.getUI().checkbox.checked) {
                unChecked.push(node.attributes.id);
            }
        } else {
            var childNodes = node.childNodes;
            if (childNodes !== null) {
                for ( var j = 0; j < childNodes.length; j++) {
                    if (!childNodes[j].getUI().checkbox.checked) {
                        unChecked.push(childNodes[j].attributes.id);
                    }
                    getNodeSubNode(childNodes[j]);
                }
            }
        }
    }
    
    // 新增保存角色--判断是否存在
    function saveRole() {
        var arrCust = roleListGrid.getSelectionModel().getSelections();
        if (arrCust.lenth == 0) {
            return false;
        }
        if (roleNameAdd.getValue() == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('emptyRoleName')}');
            return;
        }
        if (!checkRoleName(roleNameAdd.getValue())) {
            return;
        }
        consigntypeForm3.existRole();
        
    }
    
    // 新增保存角色--不存在则保存
    function existRoleFlags(form, action) {
        if (action.result.existRoleFlag) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('existRole')}');
            roleNameAdd.setValue("");
            return;
        } else {
            Ext.MessageBox.wait('${app:i18n('saving')}');
            
            // 复制角色时执行
            if (copyRoleFlag == true) {
                copyRoleFlag = false;
                consigntypeForm3.copyRoleAction();
            } else {
                // 新增时执行
                consigntypeForm3.saveRoleAction();
            }
        }
    }
    
    // 判断时间是否合法
    function checkRoleTime() {
        if (usedTmAdd1 > usedTmAdd2) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveRoleTimeError')}');
            return;
        }
        return true;
    }
    
    // 判断角色名是否合法
    function checkRoleName(roleName) {
        var regStr = "～！＠＃￥％……＆×（）——＋｛｝［］《》？，。、：“;'~!@#$%^&*()_+|{}[]<>?/,. 　";
        for ( var i = 0; i < roleName.length; i++) {
            for ( var j = 0; j < regStr.length; j++) {
                if (roleName.charAt(i) == regStr.charAt(j)) {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('checkRoleName')}');
                    roleNameAdd.setValue("");
                    return;
                }
            }
        }
        return true;
    }
    
    function preDelRole() {
        if (roleId.value == null || typeof (roleId.value) == "undefined" || roleId.value == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selectMust')}');
            return;
        }
    	if (roleSysFlg.value) {
    		Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('role.systemDelete')}');
    		return;
    	}
        // 判断角色和用户或网点是否有关联
        Ext.Ajax.request({
            url : 'roleRelation.action',
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.hasSuccess) {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('roleRelation')}');
                    return;
                } else {
                    // 执行删除操作
                    Ext.MessageBox.confirm('${app:i18n('prompt')}','${app:i18n('preDelRoleTrue')}',delRole);
                }
            },
            params : {
                roleFil : roleId.getValue()
            }
        });
        
    }
    
    function delRole(result) {
        if (result == 'yes') {
            var roleIds = roleId.getValue();
            var baseDataLogDescVar = "删除[" + roleIds + "]";
            Ext.Ajax.request({
                params : {
                    baseDataLogDesc : baseDataLogDescVar,
                    roleFil : roleIds
                },
                url : "roleDelRole.action",
                success : function() {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('delSuccess')}');
			        roleListGrid.getStore().load({
			            callback : function() {
			                if (roleListGrid.getSelectionModel().getCount() == 0) {
			                    roleListGrid.getSelectionModel().selectFirstRow();
			                    onSelectRole();
			                }
			            }
			        });
                }
            });
            roleName.setValue("");
            roleDef.setValue("");
        }
    }
    
    function addRole() {
    	addWindow.setTitle("${app:i18n('add')}");
        addWindow.show();
        copyAddBut.hide();
        saveAddBut.show();
    }
    
    function copyRole() {
        if (roleId.value == null || typeof (roleId.value) == "undefined" || roleId.value == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selectMust')}');
            return;
        }
        addWindow.setTitle("${app:i18n('copy')}");
        addWindow.show();
        roleIdAdd.setValue(roleId.value);
        roleNameAdd.setValue(roleName.value);
        descriptionAdd.setValue(roleDef.value);
        typeFlagAdd.setValue(typeFlag.value);
        
        saveAddBut.hide();
        copyAddBut.show();
    }
    
    // 角色复制功能
    var copyRoleFlag = false;
    function copyRoleDo() {
        copyRoleFlag = true;
        consigntypeForm3.existRole();
        addWindow.hide();
    }
    
    function reSet() {
        roleNameAdd.setValue("");
        descriptionAdd.setValue("");
        
    }
    
	</ext:script>
</ext:ui>
