<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ext" uri="/ext-tags"%>
<%@ taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ext:ui scripts="${scripts}/checkTreeExtend.js">
	<ext:viewport layout="border">
		<ext:items>
			<ext:treePanel region="west" autoScroll="true" width="210">
				<ext:treeLoader url="userDeptList.action" var="deptLoader" textField="deptName" idField="id" handler="selectDept"/>
				<ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRoot" id="0"/>
			</ext:treePanel>
			<ext:panel region="center" layout="border" frame="true">
				<ext:tbar>
					<app:isPermission code="/authorization/saveAccredit.action"><ext:button text="${app:i18n('accredit')}" cls="x-btn-text-icon" icon="${images}/../add.gif" handler="openAccreditWinHdl" var="accreditBut"/></app:isPermission>
					<app:isPermission code="/authorization/editUserAccSync.action"><ext:separator/><ext:button text="${app:i18n('editAccBut')}" cls="x-btn-text-icon" icon="${images}/../edit.gif" handler="editAccreditWinHdl" var="editaccreditBut"/></app:isPermission>
					<app:isPermission code="/authorization/cancelAccredit.action"><ext:separator/><ext:button text="${app:i18n('canelAcc')}" cls="x-btn-text-icon" icon="${images}/../delete.gif" handler="cancelAccredit" var="canelAccreditBut"/></app:isPermission>
					<app:isPermission code="/authorization/accreditQueryUserList.action"><ext:separator/><ext:button text="${app:i18n('search')}" cls="x-btn-text-icon" icon="${images}/../search.gif" handler="queryUserHdl" var="queryUserBut"/></app:isPermission>
				</ext:tbar>
				<ext:items>
					<ext:formPanel var="userQueryForm" region="north" height="120" frame="true">
						<ext:items>
							<ext:fieldSet layout="column" autoHeight="true" title="${app:i18n('queryCondition')}">
								<ext:items>
									<ext:panel width="250" layout="form">
										<ext:items>
											<!-- 下面一条不传到后台，只做中转用 -->
											<ext:hidden name="deptName" var="deptNameVar" />
											<ext:textField fieldLabel="${app:i18n('userLogin')}" name="username" var="usernameVar" />
											<ext:textField fieldLabel="${app:i18n('userName')}" name="empName" var="empNameVar" />
										</ext:items>
									</ext:panel>
									<ext:panel width="250" layout="form">
										<ext:items>
											<ext:textField fieldLabel="${app:i18n('userEmpId')}" name="empCode" var="empCodeVar" />
											<ext:textField fieldLabel="${app:i18n('netCode')}" name="deptCode" var="deptCodeVar" />
										</ext:items>
									</ext:panel>
									<ext:panel width="250" layout="form">
										<ext:items>
											<ext:textField fieldLabel="${app:i18n('empOfficephone')}" name="empOfficephone" var="empOfficephoneVar" />
											<ext:checkbox fieldLabel="${app:i18n('isNoAcc')}" checked="true" name="isAccredit" var="isAccreditVar"/>
										</ext:items>
									</ext:panel>
								</ext:items>
							</ext:fieldSet>
						</ext:items>
					</ext:formPanel>
					<ext:editorGridPanel var="userListGrid" region="center" frame="false" border="false" autoExpandColumn="employeeEmail">
						<ext:store url="accreditUserList.action" var="userListStoreVar" listeners="{beforeload:beforeUserListLoad}">
							<ext:jsonReader totalProperty="totalSize" root="queryUsers">
								<ext:fields type="com.sf.module.authorization.domain.User" />
							</ext:jsonReader>
						</ext:store>
						<ext:pagingToolbar pageSize="20" store="userListStoreVar" var="userListPagingBar" displayInfo="true"/>
						<ext:checkboxSelectionModel/>
						<ext:columnModel>
							<ext:propertyColumnModel dataIndex="id" hidden="true"/>
							<ext:propertyColumnModel dataIndex="username" header="${app:i18n('userLogin')}"/>
							<ext:propertyColumnModel dataIndex="employeeName" header="${app:i18n('userName')}"/>
							<ext:propertyColumnModel dataIndex="dept" width="150" header="${app:i18n('netCode')}" renderer="function(dept){return dept == null ? null : (dept.deptName + '/' + dept.deptCode + (dept.parentCode == null ? '' : ('(' + dept.parentCode + ')')))}" />
							<ext:propertyColumnModel dataIndex="employeeCode" hidden="true" header="${app:i18n('userEmpId')}"/>
							<ext:propertyColumnModel dataIndex="employeeDutyName" width="100" header="${app:i18n('userDutyName')}"/>
							<ext:propertyColumnModel id="employeeEmail" dataIndex="employeeEmail" header="${app:i18n('userEmail')}"/>
						</ext:columnModel>
					</ext:editorGridPanel>
				</ext:items>
			</ext:panel>
		</ext:items>
	</ext:viewport>

	<!-- 新增授权页面 -->
	<ext:window var="accreditWin" closeAction="hide" width="850"
		height="500" resizable="false" layout="border" modal="true"
		frame="true" title="${app:i18n('accredit')}">
		<ext:tbar>
			<ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveAccredit"/>
		</ext:tbar>
		<ext:items>
			<ext:formPanel region="north" height="140" frame="true" var="accreditForm">
				<ext:items>
					<ext:fieldSet layout="column" height="124" title="${app:i18n('accreditInfo')}">
						<ext:items>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:hidden var="accreditId" name="accredit.id" value="${accredit.id}" />
									<ext:hidden var="accreditUserId" name="accredit.userId" value="${accredit.userId}" />
									<!-- 是否系统管理员,包括超级管理员和子系统管理员 -->
									<ext:hidden var="isSysMgr" name="isSysMgr" value="${isSysMgr}" />
									<ext:textField fieldLabel="${app:i18n('accreditUserLogin')}" name="formUsername" var="formUsernameVar" disabled="true" />
									<ext:radio name="accredit.accreditUserTypeCode" var="subSysManagerVar" value="0" fieldLabel="${app:i18n('sysType')}"
										boxLabel="${app:i18n('subSysManager')}" labelSeparator="" disabled="true"/>
									<ext:dateField fieldLabel="${app:i18n('effDate')}" width="125" name="accredit.usedTm" var="accreditUsedTm" disabled="false" />
								</ext:items>
							</ext:panel>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:textField fieldLabel="${app:i18n('userName')}" name="formEmpName" var="formEmpNameVar" disabled="true" />
									<ext:radio name="accredit.accreditUserTypeCode" var="areaSysManagerVar"
										value="1" boxLabel="${app:i18n('areaSysManager')}"
										labelSeparator="" checked="true" />
									<ext:checkbox fieldLabel="${app:i18n('foreverAcc')}" value="true" var="accreditUsedTm2"/>
								</ext:items>
							</ext:panel>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:textField fieldLabel="${app:i18n('haveDeptCode')}" name="formDeptName" var="formDeptNameVar" disabled="true" />
									<ext:radio name="accredit.accreditUserTypeCode" var="epibolySysManagerVar"
										value="2" boxLabel="${app:i18n('epibolySysManager')}"
										labelSeparator="" checked="false" />
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:fieldSet>
				</ext:items>
			</ext:formPanel>

			<ext:tabPanel region="center" activeTab="rolePanelNew" var="tabPanelVar">
				<ext:items>
					<ext:panel id="rolePanelNew" title="${app:i18n('roleTit')}" layout="border" height="300">
						<ext:items>
							<ext:gridPanel var="roleListGrid" frame="false" border="false" region="center">
								<ext:store url="roleRoleList.action">
									<ext:jsonReader>
										<ext:fields type="com.sf.module.authorization.domain.Role" />
									</ext:jsonReader>
								</ext:store>
								<ext:checkboxSelectionModel/>
								<ext:columnModel>
									<ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="300"/>
									<ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="300"/>
									<ext:propertyColumnModel dataIndex="id" hidden="true"/>
								</ext:columnModel>
							</ext:gridPanel>
						</ext:items>
					</ext:panel>

					<ext:panel title="${app:i18n('net')}" layout="border" height="300" var="deptSelectPanel" autoScroll="true">
						<ext:items>
							<ext:treePanel autoScroll="true" region="center" checkModel="cascadeDown" onlyLeafCheckable="false" var="deptSelectTree">
								<ext:treeLoader url="loginUserCHKDept.action" clearOnLoad="false" var="deptLoader1" textField="deptName" idField="id" handler="selectUserDept" baseAttrs="{uiProvider:Ext.tree.TreeCheckNodeUI}"/>
								<ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRootVar" id="0"/>
							</ext:treePanel>
						</ext:items>
					</ext:panel>
				</ext:items>
			</ext:tabPanel>
		</ext:items>
	</ext:window>

	<!-- 编辑授权页面 -->
	<ext:window var="editAccreditWin" closeAction="hide" width="850"
		height="520" resizable="false" layout="border" modal="true"
		frame="true" title="${app:i18n('editAccBut')}">
		<ext:tbar>
			<ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveEditAccreditHdl"/>
		</ext:tbar>
		<ext:items>
			<ext:formPanel region="north" height="140" frame="true" var="editAccreditForm">
				<ext:items>
					<ext:fieldSet layout="column" height="124" title="${app:i18n('accreditInfo')}">
						<ext:items>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:hidden var="editAccreditId" name="editUser.id" value="${accredit.id}" />
									<ext:hidden var="editAccreditUserId" name="editUser.userId" value="${accredit.userId}" />
									<ext:textField fieldLabel="${app:i18n('accreditUserLogin')}" name="formUsername" var="editAccUsernameVar" readOnly="true" />
									<ext:radio name="editAccredit.accreditUserTypeCode" var="subSysManagerVar2"
										value="0" fieldLabel="${app:i18n('sysType')}" checked="" disabled="true"
										boxLabel="${app:i18n('subSysManager')}" labelSeparator="" />
									<ext:dateField fieldLabel="${app:i18n('effDate')}" width="125" name="editAccredit.usedTm" var="editAccUsedTm" disabled="true"/>
								</ext:items>
							</ext:panel>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:textField fieldLabel="${app:i18n('userName')}" name="formEmpName" var="editAccEmpNameVar" readOnly="true" />
									<ext:radio name="editAccredit.accreditUserTypeCode" var="areaSysManagerVar2"
										value="1" boxLabel="${app:i18n('areaSysManager')}" checked="" disabled="true"
										labelSeparator="" />
									<ext:checkbox fieldLabel="${app:i18n('foreverAcc')}" value="true" disabled="true" var="editAccUsedTm2"/>
								</ext:items>
							</ext:panel>
							<ext:panel width="250" layout="form">
								<ext:items>
									<ext:textField fieldLabel="${app:i18n('haveDeptCode')}" name="formDeptName" var="editAccDeptNameVar" readOnly="true" />
									<ext:radio name="editAccredit.accreditUserTypeCode" var="epibolySysManagerVar2" value="2" boxLabel="${app:i18n('epibolySysManager')}" checked="" disabled="true" labelSeparator="" />
								</ext:items>
							</ext:panel>
						</ext:items>
					</ext:fieldSet>
				</ext:items>
			</ext:formPanel>

			<ext:tabPanel region="center" activeTab="rolePanelEdit" var="tabPanelVarEdit">
				<ext:items>
					<ext:panel id="rolePanelEdit" title="${app:i18n('roleTit')}" layout="column" var="rolePanelVar">
						<ext:items>
							<ext:gridPanel var="roleListGrid2" border="true" frame="false" width="325" height="275" title="${app:i18n('noRoles')}">
								<ext:store url="roleLoginList.action">
									<ext:jsonReader>
										<ext:fields type="com.sf.module.authorization.domain.Role" />
									</ext:jsonReader>
								</ext:store>
								<ext:rowSelectionModel singleSelect="true"/>
								<ext:columnModel>
									<ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="100"/>
									<ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="180"/>
									<ext:propertyColumnModel dataIndex="id" hidden="true"/>
								</ext:columnModel>
							</ext:gridPanel>
							<ext:panel border="false" frame="false" height="280" width="175" layout="border">
								<ext:items>
									<ext:panel html="" region="west" width="62" border="false" frame="false"/>
									<ext:panel html="" region="east" width="62" border="false" frame="false"/>
									<ext:panel html="" region="north" height="100" border="false" frame="false"/>
									<ext:panel html="" region="south" height="100" border="false" frame="false"/>
									<ext:panel region="center" border="false" frame="false" layout="form">
										<ext:items>
											<ext:panel height="25" border="false">
												<ext:items>
													<ext:button text=">>" handler="rela" var="relaVar"/>
												</ext:items>
											</ext:panel>
											<ext:panel html="" height="30" border="false"/>
											<ext:panel height="25" border="false">
												<ext:items>
													<ext:button text="<<" handler="relaNo" var="relaNoVar"/>
												</ext:items>
											</ext:panel>
										</ext:items>
									</ext:panel>
								</ext:items>
							</ext:panel>
							<!-- 已分配角色面版 -->
							<ext:gridPanel var="roleListGrid1" title="${app:i18n('hasRoles')}" frame="false" border="true" width="325" height="275">
								<ext:store url="userHasRoleList.action">
									<ext:jsonReader>
										<ext:fields type="com.sf.module.authorization.domain.Role" />
									</ext:jsonReader>
								</ext:store>
								<!-- 同步已分配的角色 -->
								<ext:rowSelectionModel singleSelect="true"/>
								<ext:columnModel>
									<ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="100"/>
									<ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="180"/>
									<ext:propertyColumnModel dataIndex="id" hidden="true"/>
								</ext:columnModel>
							</ext:gridPanel>
						</ext:items>
					</ext:panel>

					<ext:panel title="${app:i18n('net')}" layout="border" height="275" var="editDeptSelectPanel" autoScroll="true">
						<ext:items>
							<ext:treePanel autoScroll="true" region="center" checkModel="cascadeDown" onlyLeafCheckable="false" var="editDeptSelectTree">
								<ext:treeLoader url="loginUserCHKDept.action" clearOnLoad="false" var="deptLoader2" textField="deptName" idField="id" clsField="" handler="selectUserAccDept" baseAttrs="{uiProvider:Ext.tree.TreeCheckNodeUI}"/>
								<ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRootVar2" id="0"/>
							</ext:treePanel>
						</ext:items>
					</ext:panel>
				</ext:items>
			</ext:tabPanel>
		</ext:items>
	</ext:window>

	<ext:script>
    var clickFlag = false;
    var treeClickFlag = false;
    var oldEditRoleIds = "";
    var oldEditDeptIds = "";
    
    // 第一次点击时判断是否有网点数据，
    deptLoader.on("load", checkNullNode);
    var accIsEmpty = false;
    function checkNullNode(obj, node) {
        if (node.childNodes == "" && accIsEmpty == false) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('accIsEmptyOrTimeOut')}');
        }
        accIsEmpty = true;
    }
    
    // 选择子系统管理员
    subSysManagerVar.on('check', subSysManagerCheck);
    function subSysManagerCheck() {
        if (subSysManagerVar.getValue()) {
            roleListGrid.disable();
            deptSelectPanel.disable();
        } else {
            roleListGrid.enable();
            deptSelectPanel.enable();
        }
    }
    
    function setAccreditUserTypeCode() {
        if (subSysManagerVar.getValue()) {
            // 子系统管理员
            return "0";
        } else if (areaSysManagerVar.getValue()) {
            // 区域管理员
            return "1";
        } else {
            // 外包管理员
            return "2";
        }
    }
    
    // 注册部门树的根
    deptRoot.on('click', selectDept);
    function selectDept(node) {
        if (typeof (node.attributes.deptCode) == "undefined") {
            deptCodeVar.setValue("");
            deptNameVar.setValue("");
            userListGrid.getStore().removeAll();
            return;
        }
        
        deptCodeVar.setValue(node.attributes.deptCode);
        
        var strs = node.text.split('/');
        deptNameVar.setValue(strs[1]);
        
        usernameVar.setValue("");
        empNameVar.setValue("");
        empCodeVar.setValue("");
        empOfficephoneVar.setValue("");
        isAccreditVar.setValue(true);
        
        queryUserHdl();
    }
    
    // 查询用户
    function queryUserHdl() {
        userListGrid.getStore().removeAll();
        if (isAccreditVar.checked) {
            <app:isPermission code="/authorization/saveAccredit.action">accreditBut.disable();</app:isPermission>
            <app:isPermission code="/authorization/editUserAccSync.action">editaccreditBut.enable();</app:isPermission>
            <app:isPermission code="/authorization/cancelAccredit.action">canelAccreditBut.enable();</app:isPermission>
        } else {
            <app:isPermission code="/authorization/saveAccredit.action">accreditBut.enable();</app:isPermission>
            <app:isPermission code="/authorization/editUserAccSync.action">editaccreditBut.disable();</app:isPermission>
            <app:isPermission code="/authorization/cancelAccredit.action">canelAccreditBut.disable();</app:isPermission>
        }
        userListGrid.getStore().load();
    }
    
    function beforeUserListLoad(store){
        if (deptCodeVar.getValue() == "" && usernameVar.getValue() == "" && empCodeVar.getValue() == ""
                && empNameVar.getValue() == "" && empOfficephoneVar.getValue() == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('accSearchUser')}');
            return false;
        }    	
    	store.baseParams = userQueryForm.getForm().getValues();
    	store.baseParams["limit"] = userListPagingBar.pageSize;
    }
    
    accreditUsedTm2.on('check', accreditUsedTm2Check);
    function accreditUsedTm2Check() {
        if (accreditUsedTm2.checked) {
            accreditUsedTm.setValue("");
            accreditUsedTm.disable();
        } else {
            accreditUsedTm.enable();
        }
    }
    
    var firstFlag = false;
    
    // 打开授权窗口
    function openAccreditWinHdl() {
        var selectUserList = userListGrid.getSelectionModel().getSelections();
        if (selectUserList[0] == null || typeof (selectUserList[0].get("id")) == "undefined" || selectUserList[0].get("id") == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('pleSel')}');
            return;
        }
        if (isSysMgr != null && isSysMgr != "" && isSysMgr.getValue() == "true") {
            // 系统管理员可编辑
            subSysManagerVar.setDisabled(false);
        } else {
            // 非系统管理员,不可编辑
            subSysManagerVar.setDisabled(true);
        }
        
        // 给授权页面上的控件赋值
        var userId = selectUserList[0].data.id;
        accreditId.setValue(userId);
        accreditUserId.setValue(userId);
        formUsernameVar.setValue(selectUserList[0].get("username"));
        formEmpNameVar.setValue(selectUserList[0].get("employeeName"));
        if (null != selectUserList[0].get("dept")) {
            formDeptNameVar.setValue(selectUserList[0].get("dept")['deptCode']);
        } else {
        	formDeptNameVar.setValue(selectUserList[0].get("deptId"));
        }
        
        roleListGrid.getStore().load();
        if (firstFlag == true && deptSelectTree.isVisible() == true)
            deptSelectTree.root.reload();
        
        firstFlag = true;
        accreditWin.show();
        
    }
    // 授权工作
    
    function saveAccredit() {
        if (accreditUsedTm.getValue() == "" && !accreditUsedTm2.checked) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selAccTime')}');
            return;
        }
        
        var sendData = accreditForm.getForm().getValues();
        var useTmDate = "";
        if (accreditUsedTm.getValue() != "" && accreditUsedTm.getValue() != "undefined") {
            useTmDate = accreditUsedTm.getValue().dateFormat('Y-m-d');
            var curDate = (new Date()).dateFormat('Y-m-d');
            if (useTmDate < curDate) {
                Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('accTimeError')}');
                return;
            }
        }
        var rolesParams = getRoles();
        var departmentsParams = getDepartments();
        var accreditUserTypeCode = setAccreditUserTypeCode();
        var userId = accreditUserId.getValue();
        
        Ext.MessageBox.wait('${app:i18n('saving')}');
        Ext.Ajax.request({
            params : {
                usedTm : useTmDate,
                accreditUserId : userId,
                accreditUserTypeCode : accreditUserTypeCode,
                roles : rolesParams,
                synDataCreditParams : departmentsParams
            },
            url : "saveAccredit.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.message == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}', '${app:i18n('successAccSub')}');
                    userListGrid.getStore().load();
                    accreditWin.hide();
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveFail')}');
                }
                
            }
        });
        
    }
    // 取消授权
    function cancelAccredit() {
        var selectUserList = userListGrid.getSelectionModel().getSelections();
        if (selectUserList[0] == null || typeof (selectUserList[0].get("id")) == "undefined" || selectUserList[0].get("id") == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('pleSel')}');
            return;
        }
        Ext.MessageBox.confirm('${app:i18n('prompt')}','${app:i18n('trueCannelUserAcc')}',doCancelAccredit);
    }
    
    // 执行取消授权功能
    function doCancelAccredit(result) {
        if (result == "yes") {
            var selectUserList = userListGrid.getSelectionModel().getSelections();
            var returnStr = "";
            for ( var i = 0; i < selectUserList.length; i++) {
                if (i == selectUserList.length - 1) {
                    returnStr += selectUserList[i].data.id;
                } else {
                    returnStr += selectUserList[i].data.id + ",";
                }
            }
            Ext.MessageBox.wait('${app:i18n('saving')}');
            Ext.Ajax.request({
                params : {
                    selectUserIds : returnStr
                },
                url : "cancelAccredit.action",
                success : function(response) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.message == "true") {
                        Ext.MessageBox.alert('${app:i18n('prompt')}', '${app:i18n('canelAccSub') }');
                        userListGrid.getStore().load();
                    } else {
                        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('canelAccSubFail')}');
                    }
                }
            });
        }
    }
    // 获取选中的角色
    function getRoles() {
        var selectRoleList = roleListGrid.getSelectionModel().getSelections();
        var arr = new Array();
        for ( var i = 0; i < selectRoleList.length; i++)
            arr.push(selectRoleList[i].data);
        return Ext.util.JSON.encode(arr);
    }
    // 获取选中的网点
    function getDepartments() {
        var returnStr = "";
        var selectDeptNodes = deptSelectTree.getChecked();
        for ( var i = 0; i < selectDeptNodes.length; i++) {
            if (i == selectDeptNodes.length - 1) {
                returnStr += selectDeptNodes[i].id;
            } else {
                returnStr += selectDeptNodes[i].id + ",";
            }
        }
        return returnStr;
    }
    // 数据权限, 选择网点
    function selectUserDept() {
    }
    function selectUserAccDept() {
    }
    
    function initRoleIdVar() {
        // 保存原角色属性值，
        var editRolesAll = roleListGrid1.getStore().getRange();
        
        for ( var i = 0; i < editRolesAll.length; i++) {
            if (i == 0) {
                oldEditRoleIds = editRolesAll[i].data.id;
            } else {
                oldEditRoleIds = oldEditRoleIds + "," + editRolesAll[i].data.id;
            }
        }
    }
    
    roleListGrid2.on("celldblclick", rela);
    roleListGrid1.on("celldblclick", relaNo);
    
    // 从未分配角色列表中移动选中的角色到已分配角色列表中
    function rela() {
        // 点击前先保存原值
        if (!clickFlag) {
            initRoleIdVar();
            clickFlag = true;
        }
        
        var r = roleListGrid2.getSelectionModel().getSelected();
        
        if (r == null) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('tsRelaRole')}');
            return;
        }
        
        roleListGrid2.getStore().remove(r);
        var n = roleListGrid1.getStore().getCount();
        roleListGrid1.stopEditing();
        roleListGrid1.getStore().insert(n, r);
    }
    // 从已分配角色列表中移除选中的角色到未分配角色列表中
    function relaNo() {
        // 点击前先保存原值
        if (!clickFlag) {
            initRoleIdVar();
            clickFlag = true;
        }
        
        var r = roleListGrid1.getSelectionModel().getSelected();
        if (r == null) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('tsRelaNoRole')}');
            return;
        }
        
        roleListGrid1.getStore().remove(r);
        var n = roleListGrid2.getStore().getCount();
        roleListGrid2.stopEditing();
        roleListGrid2.getStore().insert(n, r);
    }
    
    var oldEditUserId = "";
    var openEditFlag = false;
    // 编辑授权--打开窗口
    function editAccreditWinHdl() {
        roleListGrid1.getStore().removeAll();
        roleListGrid2.getStore().removeAll();
        
        var selectUserList = userListGrid.getSelectionModel().getSelections();
        if (selectUserList == null || selectUserList.length != 1) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('seleOnlyOne')}');
            return;
        }
        // 给编辑授权页面上的控件赋值
        var editUserId = selectUserList[0].data.id;
        editAccreditId.setValue(editUserId);
        editAccreditUserId.setValue(editUserId);
        editAccUsernameVar.setValue(selectUserList[0].get("username"));
        editAccEmpNameVar.setValue(selectUserList[0].get("employeeName"));
        if (null != selectUserList[0].get("dept")) {
            editAccDeptNameVar.setValue(selectUserList[0].get("dept")['deptCode']);
        } else {
        	editAccDeptNameVar.setValue(selectUserList[0].get("deptId"));
        }
        // 给单选框赋值
        Ext.Ajax.request({
            url : 'queryAccredit.action',
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.usedTm != null) {
                    var tempVar = Number(result.usedTm.substring(0, 4));
                    if (tempVar >= 2300) {
                        // 无限期授权
                        editAccUsedTm2.setValue(true);
                    } else {
                        editAccUsedTm2.setValue(false);
                        var ss = setDateFunction(result.usedTm);
                        editAccUsedTm.setValue(ss);
                    }
                }
                if (result.accreditUserTypeCode == '1') {
                    areaSysManagerVar2.setValue(true);
                } else if (result.accreditUserTypeCode == '2') {
                    epibolySysManagerVar2.setValue(true);
                } else if (result.accreditUserTypeCode == '0') {
                    subSysManagerVar2.setValue(true);
                }
                if (result.isExpired == true || result.isExpired == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n("accreditExpired")}');
                }
            },
            params : {
                accreditUserId : editUserId
            }
        });
        editAccreditWin.show();
        roleListGrid1.getStore().baseParams = editAccreditForm.getForm().getValues();
        roleListGrid2.getStore().baseParams = editAccreditForm.getForm().getValues();
        // 传入的参数是为了和用户管理区分,在后台调用不同的方法
        roleListGrid2.getStore().load({
            params : {
                accreditFlag2 : "accRoleNo"
            }
        });
        roleListGrid1.getStore().load({
            params : {
                accreditFlag1 : "accRole"
            }
        });
        var tempUrl = "";
        if (deptLoader2.clsField == "") {
            tempUrl = deptLoader2.dataUrl.replace("&clsField=&", "&clsField=" + editUserId + "@" + "&");
        } else {
            tempUrl = deptLoader2.dataUrl.replace("&clsField=" + oldEditUserId + "&", "&clsField=" + editUserId + "@" + "&");
        }
        deptLoader2.dataUrl = tempUrl;
        
        // 第一次进入编辑时不用重新加载树
        if (openEditFlag == true && editDeptSelectTree.isVisible() == true) {
            editDeptSelectTree.root.reload();
        }
        openEditFlag = true;
        
        // 给用户ＩＤ加一个"@"是为了在后台和用户管理的分开执行不同的方法
        oldEditUserId = editUserId + "@";
        tabPanelVarEdit.setActiveTab(rolePanelVar);
    }
    
    // 打开编辑页面时给时间控件赋值，对字符串进行转换
    function setDateFunction(userTime) {
        if (userTime != null && userTime.length > 9) {
            var str = userTime.substring(0, 10);
            var converted = Date.parse(str);
            var myDate = new Date(converted);
            if (isNaN(myDate)) {
                var arys = str.split('-');
                myDate = new Date(arys[0], --arys[1], arys[2]);
            }
            return myDate;
        }
        return "";
    }
    
    // 编辑授权--编辑界面隐藏前方法
    editAccreditWin.on('beforehide', editAccreditWinHide);
    function editAccreditWinHide() {
        // editDeptSelectTree.root.reload();
        
        editAccUsedTm2.setValue(false);
        editAccUsedTm.setValue("");
        
        clickFlag = true;
        oldEditRoleIds = "";
        oldEditDeptIds = "";
    }
    
    // 编辑授权--授权工作
    function saveEditAccreditHdl() {
        var editDataCreditParams = getSelectDeptCodes();
        var editDataUnCreditParams = getUnSelectDeptCodes();
        
        var rolesParams = getEditAccRoles();
        // var departmentsParams = getEditAccDept();
        var accreditUserTypeCode = setEditAccUserTypeCode();
        var userId = editAccreditId.getValue();
        var editUsername = editAccUsernameVar.getValue();
        
        var baseDataLogDescVar = "修改用户授权[" + editUsername + "],";
        if (rolesParams != oldEditRoleIds && clickFlag == true) {
            var roleChgTmp = "角色由[" + oldEditRoleIds + "]改为[" + rolesParams + "];";
            baseDataLogDescVar += roleChgTmp.length >= 400 ? "[角色信息被大量修改];" : roleChgTmp;
        }
        if (editDataCreditParams != oldEditDeptIds && treeClickFlag == false) {
            var deptChgTmp = "网点数据权限改为[" + editDataCreditParams + "];";
            baseDataLogDescVar += deptChgTmp.length >= 400 ? "[网点数据权限信息被大量修改];" : deptChgTmp;
        }
        Ext.MessageBox.wait('${app:i18n('saving')}');
        Ext.Ajax.request({
            params : {
                baseDataLogDesc : baseDataLogDescVar,
                roles : rolesParams,
                synDataCreditParams : editDataCreditParams,
                synDataUnCreditParams : editDataUnCreditParams,
                selectUserIds : userId
            },
            url : "editUserAccSync.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.message == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveSuccess')}');
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveFail')}');
                }
                editAccreditWin.hide();
            }
        });
        unChecked = new Array();
        
    }
    
    // 编辑授权-- 获取选中的角色
    function getEditAccRoles() {
        var rolesParams;
        var editRolesAll = roleListGrid1.getStore().getRange();
        for ( var i = 0; i < editRolesAll.length; i++) {
            if (i == 0) {
                rolesParams = editRolesAll[i].data.id;
            } else {
                rolesParams = rolesParams + "," + editRolesAll[i].data.id;
            }
        }
        return rolesParams;
    }
    
    // 编辑授权-- 获取选中的管理员类型
    function setEditAccUserTypeCode() {
        if (subSysManagerVar2.getValue()) {
            return "0";
        } else if (areaSysManagerVar2.getValue()) {
            return "1";
        } else {
            return "2";
        }
    }
    
    // 编辑授权-- 获取选中的管理员类型
    subSysManagerVar2.on('check', subSysManagerCheck2);
    function subSysManagerCheck2() {
        if (subSysManagerVar2.getValue()) {
            rolePanelVar.disable();
            editDeptSelectPanel.disable();
        } else {
            rolePanelVar.enable();
            editDeptSelectPanel.enable();
        }
    }
    
    areaSysManagerVar2.on('check', sysManagerCheck2);
    epibolySysManagerVar2.on('check', sysManagerCheck2);
    function sysManagerCheck2() {
        if (areaSysManagerVar2.getValue() || epibolySysManagerVar2.getValue()) {
            rolePanelVar.enable();
            editDeptSelectPanel.enable();
        } else {
            rolePanelVar.disable();
            editDeptSelectPanel.disable();
        }
    }
    // 得到选择的选择的网点权限
    var unChecked = new Array();
    function getSelectDeptCodes() {
        var returnStr = "";
        var nodes = editDeptSelectTree.getChecked();
        for (i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                returnStr += nodes[i].id;
            } else {
                returnStr += nodes[i].id + ",";
            }
        }
        return returnStr;
    }
    
    // 得到未选择的选择的网点权限
    function getUnSelectDeptCodes() {
        var treeNodes = deptRootVar2.childNodes;
        for ( var j = 0; j < treeNodes.length; j++) {
            if (!treeNodes[j].attributes.checked) {
                if (!checkIdIsExistInArray(unChecked, treeNodes[j].attributes.id))
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
                if (!checkIdIsExistInArray(unChecked, node.attributes.id))
                    unChecked.push(node.attributes.id);
            }
        } else {
            var childNodes = node.childNodes;
            if (childNodes !== null) {
                for ( var j = 0; j < childNodes.length; j++) {
                    if (!childNodes[j].getUI().checkbox.checked) {
                        if (!checkIdIsExistInArray(unChecked, childNodes[j].attributes.id))
                            unChecked.push(childNodes[j].attributes.id);
                    }
                    getNodeSubNode(childNodes[j]);
                }
            }
        }
        
    }
    
    function checkIdIsExistInArray(arrays, deptCode) {
        var size = arrays.length;
        for ( var i = 0; i < size; i++) {
            if (arrays[i] != null && deptCode == arrays[i]) {
                return true;
            }
        }
        return false;
    }
	</ext:script>
</ext:ui>