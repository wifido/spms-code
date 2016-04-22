<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ext" uri="/ext-tags"%>
<%@ taglib prefix="app" uri="/app-tags"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ext:ui scripts="${scripts}/checkTreeExtend.js">
    <ext:viewport layout="border">
        <ext:items>
            <ext:treePanel region="west" autoScroll="true" width="210">
                <ext:treeLoader url="userDeptList.action" var="deptLoader" textField="deptName" idField="departmentId" handler="selectDept"/>
                <ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRoot" id="0" />
            </ext:treePanel>
            <ext:panel region="center" layout="border" frame="true">
                <ext:tbar>
                    <app:isPermission code="/authorization/userListByPage.action"><ext:button text="${app:i18n('search')}" cls="x-btn-text-icon" icon="${images}/../search.gif" handler="onUserSearch" var="queryUserBut"/></app:isPermission>
                    <app:isPermission code="/authorization/userSaveUser.action"><ext:separator/><ext:button text="${app:i18n('add')}" cls="x-btn-text-icon" icon="${images}/../add.gif" handler="addUserHdl" var="addUserBut"/></app:isPermission>
                    <app:isPermission code="/authorization/editRoleSync.action"><ext:separator/><ext:button text="${app:i18n('edit')}" cls="x-btn-text-icon" icon="${images}/../edit.gif" handler="onEditUser"/></app:isPermission>
                    <app:isPermission code="/authorization/userDelUser.action"><ext:separator/><ext:button text="${app:i18n('delete')}" cls="x-btn-text-icon" icon="${images}/../delete.gif" handler="preDelUserHdl" var="delUserBut"/></app:isPermission>
                    <app:isPermission code="/authorization/modUserPassword.action"><ext:separator/><ext:button text="${app:i18n('modifyPassword')}" cls="x-btn-text-icon" icon="${images}/password.gif" handler="modifyPasswordHdl" var="modifyPasswordBut"/></app:isPermission>
                </ext:tbar>
                <ext:items>
                    <ext:formPanel var="userQueryForm" region="north" autoHeight="true" frame="true">
                        <ext:items>
                            <ext:fieldSet layout="column" autoHeight="true" title="${app:i18n('queryCondition')}">
                                <ext:items>
                                    <ext:panel width="250" layout="form">
                                        <ext:items>
                                            <ext:textField fieldLabel="${app:i18n('userLogin')}" name="username" var="usernameVar"/>
                                        </ext:items>
                                    </ext:panel>
                                    <ext:panel width="250" layout="form">
                                        <ext:items>
                                            <ext:textField fieldLabel="${app:i18n('userEmpId')}" name="empCode" var="empCodeVar"/>
                                        </ext:items>
                                    </ext:panel>        
                                    <ext:panel width="250" layout="form">
                                        <ext:items>
                                            <ext:textField fieldLabel="${app:i18n('netCode')}" name="deptCode" var="deptCodeVar"/>
                                        </ext:items>
                                    </ext:panel>
                                </ext:items>
                            </ext:fieldSet>
                        </ext:items>
                    </ext:formPanel>
                    <ext:gridPanel var="userListGrid" region="center" frame="false" border="false">
                        <ext:store url="userListByPage.action" var="userListStoreVar" remoteSort="true" listeners="{beforeload:beforeUserListLoad}">
                            <ext:jsonReader totalProperty="userLiseSize" root="allUser">
                                <ext:fields type="com.sf.module.authorization.domain.User"/>
                            </ext:jsonReader>
                        </ext:store>
                        <ext:pagingToolbar pageSize="20" store="userListStoreVar" var="userListPagingBar" displayInfo="true"/>
                        <ext:checkboxSelectionModel var="userList1"/>
                        <ext:columnModel>
                            <ext:propertyColumnModel dataIndex="id" hidden="true"/>
                            <ext:propertyColumnModel dataIndex="username" sortable="true" header="${app:i18n('userLogin')}"/>
                            <ext:propertyColumnModel dataIndex="employeeName" header="${app:i18n('userName')}"/>
                            <ext:propertyColumnModel dataIndex="dept" width="150" header="${app:i18n('netCode')}" renderer="function(dept){return dept == null ? null : (dept.deptName + '/' + dept.deptCode + (dept.parentCode == null ? '' : ('(' + dept.parentCode + ')')))}" />
                            <ext:propertyColumnModel dataIndex="employeeDutyName" header="${app:i18n('userDutyName')}"/>
                            <ext:propertyColumnModel dataIndex="statusFlag" header="${app:i18n('statusFlag')}" renderer="displayFlag"/>
                        </ext:columnModel>
                    </ext:gridPanel>
                </ext:items>
            </ext:panel>
        </ext:items>
    </ext:viewport>

    <!-- 新增用户-选择雇员：选择用户类型为外部用户时需要选择雇员 -->
    <ext:window var="selEmpWindow" closeAction="hide" width="600" height="530" resizable="false"
        layout="border" modal="true" frame="true" title="${app:i18n('selectEmpTitle')}">
        <ext:tbar>
            <ext:button text="${app:i18n('search')}" cls="x-btn-text-icon" icon="${images}/../search.gif" handler="queryEmp"/>
            <ext:button text="${app:i18n('selectTitle')}" handler="selectEmp"/>
        </ext:tbar>
        <ext:items>
            <ext:formPanel region="north" height="104" frame="true" layout="column" var="selEmpForm">
                <ext:items>
                    <ext:panel width="250" layout="form">
                        <ext:items>
                            <ext:textField fieldLabel="${app:i18n('userEmpId')}" name="selEmp.empCode" var="selEmpCodeVar"/>
                            <ext:textField fieldLabel="${app:i18n('userName')}" name="selEmp.empName" var="selEmpNameVar"/>
                            <ext:textField fieldLabel="${app:i18n('deptName')}" name="showDept" disabled="true" var="selEmpShowDeptVar"/>
                            <ext:hidden name="selEmp.deptCode" var="selEmpDeptCodeVar"/>
                        </ext:items>
                    </ext:panel>
                    <ext:panel width="250" layout="form">
                        <ext:items>
                            <ext:textField fieldLabel="${app:i18n('userDutyName')}" name="selEmp.empDutyName" var="selEmpDutyNameVar"/>
                            <ext:textField fieldLabel="${app:i18n('empOfficephone')}" name="selEmp.empOfficephone" var="selEmpOfficephoneVar"/>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:formPanel>
            <ext:gridPanel region="center" var="empQueryList" frame="false" border="false" autoExpandColumn="empEmail">
                <ext:store url="userEmpList.action" var="empQueryListStore" listeners="{beforeload:beforeEmpListLoad}">
                    <ext:jsonReader totalProperty="empLiseSize" root="allEmp">
                        <ext:fields type="com.sf.module.organization.domain.Employee"/>
                    </ext:jsonReader>
                </ext:store>
                <ext:pagingToolbar pageSize="15" store="empQueryListStore" var="empQueryPagingBar" displayInfo="true"/>
                <ext:rowSelectionModel singleSelect="true" var="empQuery1"/>
                <ext:columnModel>
                    <ext:propertyColumnModel dataIndex="empCode" header="${app:i18n('empId')}"/>
                    <ext:propertyColumnModel dataIndex="empName" header="${app:i18n('userName')}"/>
                    <ext:propertyColumnModel dataIndex="empDutyName" header="${app:i18n('userDutyName')}"/>
                    <ext:propertyColumnModel dataIndex="empOfficephone" header="${app:i18n('empOfficephone')}" hidden="true"/>
                    <ext:propertyColumnModel id="empEmail" dataIndex="empEmail" header="${app:i18n('userEmail')}"/>
                </ext:columnModel>
            </ext:gridPanel>
        </ext:items>
    </ext:window>

    <!-- 新增用户 -->
    <ext:window var="addUserWindow" closeAction="hide" width="830" height="500" resizable="false"
        layout="border" modal="true" frame="true" title="${app:i18n('addUserTitle')}">
        <ext:tbar>
            <ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveUserHdl" var="saveAddBut"/>
            <ext:button text="${app:i18n('reset')}" cls="x-btn-text-icon" icon="${images}/../reset.gif" handler="addReset"/>
        </ext:tbar>
        <ext:items>
            <ext:formPanel region="north" height="120" frame="true" layout="column" var="saveUserFormVar" labelWidth="120">
                <ext:items>
                    <ext:panel width="260" layout="form">
                        <ext:items>
                            <!-- 选择雇员用存储选择雇员Id -->
                            <ext:hidden name="selectEmpId" var="selectEmpIdVar"/>
                            <ext:comboBox tabIndex="1" name="userTypeCode" var="userTypeCodeVar" width="128" fieldLabel="${app:i18n('userTypeCode')}" displayField="typeValue" valueField="typeCode" hiddenName="addUser.type_code" allowBlank="false" mode="local" triggerAction="all" editable="false" data="['1','${app:i18n('internalUser')}'],['0','${app:i18n('externalUser')}']" />
                            <ext:textField tabIndex="3" fieldLabel="${app:i18n('userLogin')}<font color=red>*</font>" allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('userLogin'))}" name="addUser.username" var="addUsernameVar"/>
                            <ext:textField tabIndex="6" fieldLabel="${app:i18n('empOfficephone')}" name="addUser.employee.empOfficephone" var="addUserEmpOfficephoneVar" disabled="true"/>
                            <ext:checkbox tabIndex="9" fieldLabel="${app:i18n('isDeptAcc')}" checked="true" name="isAccredit2" var="isAccreditVar2"/>
                        </ext:items>
                    </ext:panel>
                    <ext:panel width="260" layout="form">
                        <ext:items>
                            <ext:textField tabIndex="2" fieldLabel="${app:i18n('userEmpId')}<font color=red>*</font>" name="addUser.employee.empCode" var="addUserEmpCodeVar" disabled="true"/>
                            <ext:textField tabIndex="4" allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('password'))}" maxLength="8" minLength="8" fieldLabel="${app:i18n('password')}<font color=red>*</font>" name="addUser.password" inputType="password" var="addPasswordVar"/>
                            <ext:textField tabIndex="7" fieldLabel="${app:i18n('userName')}<font color=red>*</font>" allowBlank="false"  blankText="${app:i18n_arg1('error.notNull',app:i18n('userLogin'))}" name="addUser.employee.empName" var="addUserEmpNameVar" disabled="true"/>
                            <ext:checkbox tabIndex="10" fieldLabel="${app:i18n('isCheckModel')}" checked="false" name="checkModel" var="checkModelVar"/>
                        </ext:items>
                    </ext:panel>
                    <ext:panel width="280" layout="form">
                        <ext:items>
                            <ext:button text="..." handler="selectInternalUser"/>
                            <ext:textField tabIndex="5" allowBlank="false" blankText="${app:i18n_arg1('error.notNull',app:i18n('configPassword'))}" maxLength="8" minLength="8" fieldLabel="${app:i18n('configPassword')}<font color=red>*</font>" name="newPasswordConfirm" inputType="password" var="newPasswordConfirmVar"/>
                            <ext:textField tabIndex="8" fieldLabel="${app:i18n('userDutyName')}" name="addUser.employee.empDutyName" var="addUserEmpDutyNameVar" disabled="true"/>
                            <ext:checkbox tabIndex="11" fieldLabel="${app:i18n('assignOwnerDeptRight')}" checked="true" name="checkOwnerDeptRight" var="checkOwnerDeptRightVar"/>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:formPanel>
            <ext:tabPanel region="center" activeTab="panAddRoleListGrid" var="tabPanelVar">
                <ext:items>
                    <ext:panel id="panAddRoleListGrid" title="${app:i18n('roleTit')}" layout="border">
                        <ext:items>
                            <ext:gridPanel var="roleListGrid" region="center" frame="false" border="false">
                                <ext:store url="loginUserHasRoleList.action">
                                    <ext:jsonReader>
                                        <ext:fields type="com.sf.module.authorization.domain.Role"/>
                                    </ext:jsonReader>
                                </ext:store>
                                <ext:checkboxSelectionModel var="roleSelect1"/>
                                <ext:columnModel>
                                    <ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="360"/>
                                    <ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="400"/>
                                    <ext:propertyColumnModel dataIndex="id" hidden="true"/>
                                </ext:columnModel>
                            </ext:gridPanel>
                        </ext:items>
                    </ext:panel>
　　　　　　　　　　   <ext:panel title="${app:i18n('dataCredit')}" layout="border">
                        <ext:items>
                            <ext:treePanel var="dataCreditSelectTree" autoScroll="true" region="center" checkModel="multiple" onlyLeafCheckable="false">
                                <ext:treeLoader url="loginUserCHKDept.action" var="deptLoader2" textField="deptName" idField="id" baseAttrs="{uiProvider:Ext.tree.TreeCheckNodeUI}"/>
                                <ext:asyncTreeNode text="${app:i18n('comName')}" var="deptRootVar" id="0"/>
                            </ext:treePanel>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:tabPanel>
        </ext:items>
    </ext:window>

    <!-- 修改密码 -->
    <ext:window var="modPasswordWindow" closeAction="hide" width="300" height="200" resizable="false"
        layout="border" modal="true" frame="true" title="${app:i18n('alertUserTitle')}">
        <ext:tbar>
            <ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveUserPasswordHdl" var="modifyUserPasswordVar"/>
        </ext:tbar>
        <ext:items>
            <ext:formPanel region="center" height="100" frame="true" layout="column" var="modPassFormVar" labelWidth="80">
                <ext:items>
                    <ext:panel width="290" layout="form" >
                        <ext:items>
                            <ext:hidden name="modUserId" var="modUserIdVar"/>
                            <ext:textField fieldLabel="${app:i18n('userLogin')}" name="modUserName" var="modUserNameVar" readOnly="true"/>
                            <ext:textField fieldLabel="${app:i18n('password')}" name="modPass1" var="modPass1Var" inputType="password"/>
                            <ext:textField fieldLabel="${app:i18n('configPassword')}" name="modPass2" var="modPass2Var" inputType="password"/>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:formPanel>
        </ext:items>
    </ext:window>
    <!-- 编辑用户 -->
    <ext:window var="editUserWindow" closeAction="hide" width="850" height="510" resizable="false"
        layout="border" modal="true" frame="true" title="${app:i18n('alertUserTitle')}">
        <ext:tbar>
            <ext:button text="${app:i18n('save')}" cls="x-btn-text-icon" icon="${images}/../save.gif" handler="saveEditUserHdl" var="editAddBut"/>
        </ext:tbar>
        <ext:items>
            <ext:formPanel region="north" height="100" frame="true" layout="column" var="editUserFormVar" labelWidth="120">
                <ext:items>
                    <ext:panel width="260" layout="form">
                        <ext:items>
                            <!-- 选择雇员用存储选择雇员Id -->
                            <ext:hidden name="editEmpId" var="editEmpIdVar"/>
                            <ext:hidden name="editUser.id" var="editUserIdVar"/>
                            <ext:comboBox name="editUserTypeCode" var="editUserTypeCodeVar" width="125"  fieldLabel="${app:i18n('userTypeCode')}" displayField="typeValue" valueField="typeCode" hiddenName="addUser.type_code" mode="local" triggerAction="all" editable="false" data="['1','${app:i18n('internalUser')}'],['0','${app:i18n('externalUser')}']" disabled="true"/>
                            <ext:textField fieldLabel="${app:i18n('userName')}" name="addUser.employee.empName" var="editUserEmpNameVar" disabled="true"/>
                            <ext:checkbox fieldLabel="${app:i18n('isDeptAcc')}" checked="true" name="editAccredit" var="editAccreditVar"/>
                        </ext:items>
                    </ext:panel>
                    <ext:panel width="260" layout="form">
                        <ext:items>
                            <ext:textField fieldLabel="${app:i18n('userEmpId')}" name="addUser.employee.empCode" var="editUserEmpCodeVar" disabled="true"/>
                            <ext:textField fieldLabel="${app:i18n('userLogin')}" blankText="${app:i18n_arg1('error.notNull',app:i18n('userLogin'))}" name="editUser.username" var="editUsernameVar" disabled="true"/>
                            <ext:checkbox fieldLabel="${app:i18n('isCheckModel')}" checked="false" name="editCheckModel" var="editCheckModelVar"/>
                        </ext:items>
                    </ext:panel>
                    <ext:panel width="280" layout="form">
                        <ext:items>
                            <ext:textField fieldLabel="${app:i18n('userDutyName')}" name="addUser.employee.empDutyName" var="editUserEmpDutyNameVar" disabled="true"/>
                            <ext:textField fieldLabel="${app:i18n('netCode')}" name="editDeptCode" var="editDeptCodeVar" disabled="true"/>
                            <ext:checkbox fieldLabel="${app:i18n('statusFlag')}" checked="false" name="editStatus" var="editStatusVar"/>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:formPanel>
            <ext:tabPanel region="center" activeTab="panEditRoleListGrid" var="editTabPanel">
                <ext:items>
                    <ext:panel id="panEditRoleListGrid" title="${app:i18n('roleTit')}" layout="column" var="editRoleListVar">
                        <ext:items>
                            <!-- 可分配角色面版 -->
                            <ext:gridPanel var="editRoleListGrid2" title="${app:i18n('noRoles')}" frame="false" border="true" height="300" width="350">
                                <ext:store url="roleLoginList.action">
                                    <ext:jsonReader>
                                        <ext:fields type="com.sf.module.authorization.domain.Role"/>
                                    </ext:jsonReader>
                                </ext:store>
                                <ext:rowSelectionModel singleSelect="true" var="rowSelect2"/>
                                <ext:columnModel>
                                    <ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="100"/>
                                    <ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="160"/>
                                    <ext:propertyColumnModel dataIndex="id" hidden="true"/>
                                </ext:columnModel>
                            </ext:gridPanel>
                            <ext:panel border="false" frame="false" height="300" width="175" layout="border">
                                <ext:items>
                                    <ext:panel html="" region="west" width="62" border="false" frame="false"/>
                                    <ext:panel html="" region="east" width="62" border="false" frame="false"/>
                                    <ext:panel html="" region="north" height="100" border="false" frame="false"/>
                                    <ext:panel html="" region="south" height="100" border="false" frame="false"/>
                                    <ext:panel region="center" border="false" frame="false" layout="form">
                                        <ext:items>
                                            <ext:panel height="30" border="false">
                                                <ext:items>
                                                    <ext:button text=">>" handler="rela" var="relaVar"/>
                                                </ext:items>
                                            </ext:panel>
                                            <ext:panel html="" height="40" border="false">
                                            </ext:panel>
                                            <ext:panel height="30" border="false">
                                                <ext:items>
                                                    <ext:button text="<<" handler="relaNo" var="relaNoVar"/>
                                                </ext:items>
                                            </ext:panel>
                                        </ext:items>
                                    </ext:panel>
                                </ext:items>
                            </ext:panel>
                            <!-- 已分配角色面版 -->
                            <ext:gridPanel var="editRoleListGrid1" title="${app:i18n('hasRoles')}" frame="false" border="true" width="300" height="300">
                                <ext:store url="userHasRoleList.action">
                                    <ext:jsonReader>
                                        <ext:fields type="com.sf.module.authorization.domain.Role"/>
                                    </ext:jsonReader>
                                </ext:store>
                                <!-- 同步已分配的角色 -->
                                <ext:rowSelectionModel singleSelect="true" var="rowSelect3"/>
                                <ext:columnModel>
                                    <ext:propertyColumnModel dataIndex="name" header="${app:i18n('roleTit')}" width="100"/>
                                    <ext:propertyColumnModel dataIndex="description" header="${app:i18n('roleDetail')}" width="160"/>
                                    <ext:propertyColumnModel dataIndex="id" hidden="true"/>
                                </ext:columnModel>
                            </ext:gridPanel>
                        </ext:items>
                    </ext:panel>
                    <ext:panel title="${app:i18n('dataCredit')}" layout="border" id="ChkDeptTree">
                        <ext:items>
                            <ext:treePanel var="editDataCreditSelectTree" autoScroll="true" region="center" checkModel="multiple" onlyLeafCheckable="false">
                                <ext:treeLoader url="loginUserCHKDept.action" clearOnLoad="false" var="editDeptLoader" textField="deptName" idField="id" clsField="" baseAttrs="{uiProvider:Ext.tree.TreeCheckNodeUI}"/>
                                <ext:asyncTreeNode text="${app:i18n('comName')}" var="editDeptRootVar" id="0"/>
                            </ext:treePanel>
                        </ext:items>
                    </ext:panel>
                </ext:items>
            </ext:tabPanel>
        </ext:items>
    </ext:window>

    <ext:script>
    function editUserHandle(value, cellmeta, record, rowIndex, columnIndex, store) {
        var str = "<a href='#' onclick='onEditUser();return false;'>${app:i18n('edit')}</a>";
        return str;
    }
    
    function displayFlag(value) {
        var varStr = value;
        if (varStr != null) {
            if (varStr == "enable" || varStr == "root") {
                varStr = '${app:i18n('isEnable')}';
            }
            if (varStr == "disable") {
                varStr = '${app:i18n('disable')}';
            }
        }
        
        return varStr;
    }
    
    var oldEditUserId = "";
    var oldEditRoleIds = "";
    var oldEditDeptIds = "";
    var oldEditAccredit = "";
    var oldEditCheckModelParams = "";
    var clickFlag = false;
    var treeClickFlag = false;
    
    editTabPanel.on("tabchange", setActiveTab);
    function setActiveTab() {
        // 哪个是活动的面板就去查询哪些数据
        if ("ChkDeptTree" == editTabPanel.getActiveTab().id) {
            if (!treeClickFlag) {
                initDeptIdVar();
                treeClickFlag = true;
            }
        }
    }
    
    // 第一次点击时判断是否有网点数据，
    deptLoader.on("load", checkNullNode);
    var accIsEmpty = false;
    function checkNullNode(obj, node) {
        if (node.childNodes == "" && accIsEmpty == false) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('accIsEmptyOrTimeOut')}');
        }
        accIsEmpty = true;
    }
    
    // 编辑：保存用户修改信息
    function saveEditUserHdl() {
        
        var editUserId = editUserIdVar.getValue();
        var editUserName = editUsernameVar.getValue();
        var editAccreditValue = "0";
        
        if (editAccreditVar.checked) {
            editAccreditValue = "1";
        }
        
        var syncRoleIds;
        var editRolesAll = editRoleListGrid1.getStore().getRange();
        for ( var i = 0; i < editRolesAll.length; i++) {
            if (i == 0) {
                syncRoleIds = editRolesAll[i].data.id;
            } else {
                syncRoleIds = syncRoleIds + "," + editRolesAll[i].data.id;
            }
        }
        
        var editDataCreditParams = getSelectDeptCodes();
        var editDataUnCreditParams = getUnSelectDeptCodes();
        
        var editCheckModelParams = "0";
        if (editCheckModelVar.checked) {
            editCheckModelParams = "1";
        }
        
        var editUserStatusVar = "";
        if (!editStatusVar.checked) {
            editUserStatusVar = "disable";
        } else {
            editUserStatusVar = "enable";
        }
        
        // 基础数据修改描述
        var baseDataLogDescVar = "修改用户[" + editUserName + "],";
        if (editDataCreditParams != oldEditDeptIds && treeClickFlag == true) {
            var deptChgTmp = "[网点数据权限]改为[" + editDataCreditParams + "];";
            baseDataLogDescVar += deptChgTmp.length >= 400 ? "[网点数据权限信息被大量修改];" : deptChgTmp;
        }
        if (oldEditRoleIds != syncRoleIds && clickFlag == true) {
            var roleChgTmp = "[角色]由[" + oldEditRoleIds + "]改为[" + syncRoleIds + "];";
            baseDataLogDescVar += roleChgTmp.length >= 400 ? "[角色信息被大量修改];" : roleChgTmp;
        }
        if (oldEditAccredit != editAccreditValue) {
            baseDataLogDescVar += "[是否授予数据权限]由[" + oldEditAccredit + "]改为[" + editAccreditValue + "];";
        }
        if (oldEditCheckModelParams != editCheckModelParams) {
            baseDataLogDescVar += "[是否继承子数据权限]由[" + oldEditCheckModelParams + "]改为[" + editCheckModelParams + "];";
        }
        
        Ext.MessageBox.wait('${app:i18n('saving')}');
        Ext.Ajax.request({
            params : {
                baseDataLogDesc : baseDataLogDescVar,
                editUserStatus : editUserStatusVar,
                editAccredit : editAccreditValue,
                editCheckModelParams : editCheckModelParams,
                synDataCreditParams : editDataCreditParams,
                synDataUnCreditParams : editDataUnCreditParams,
                synRoleIds : syncRoleIds,
                synUserId : editUserId
            },
            url : "editRoleSync.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.message == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveSuccess')}');
                    editReset();
                    onUserSearch();
                    editUserWindow.hide();
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveFail')}');
                }
                
            }
        });
        
        unChecked = new Array();
    }
    
    var openEditUserFlag = false;
    // 打开编辑用户界面
    function onEditUser() {
        editTabPanel.setActiveTab(editRoleListVar);
        
        editRoleListGrid1.getStore().removeAll();
        editRoleListGrid2.getStore().removeAll();
        var arrCur = userListGrid.getSelectionModel().getSelections();
        if (arrCur == null || arrCur.length == 0) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('onlyOneUser')}');
            return;
        } else if (arrCur.length > 1) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('seleOnlyOne')}');
            return;
        }

        editUsernameVar.setValue(arrCur[0].data.username);
        editUserEmpNameVar.setValue(arrCur[0].data.employeeName);
        editUserEmpCodeVar.setValue(arrCur[0].data.employeeCode);
        editUserEmpDutyNameVar.setValue(arrCur[0].data.employeeDutyName);
        editUserIdVar.setValue(arrCur[0].data.id);
        if (deptCodeVar.getValue() == '') {
            if(arrCur[0].data.dept != null){
                editDeptCodeVar.setValue(arrCur[0].data.dept.deptCode);
            }
        } else {
            editDeptCodeVar.setValue(deptCodeVar.getValue());
        }
        
        // 用户是否有效
        var editStatusVarTemp = arrCur[0].data.statusFlag;
        if ("disable" == editStatusVarTemp) {
            editStatusVar.setValue(false);
        } else {
            editStatusVar.setValue(true);
        }
        
        if (arrCur[0].data.type_code != "null"){
            editUserTypeCodeVar.setValue(arrCur[0].data.type_code.trim());
        }
        
        if (arrCur[0].data.dataRightFlg == "1") {
            editAccreditVar.setValue(true);
        } else {
            editAccreditVar.setValue(false);
        }
        
        editUserWindow.show();
        editRoleListGrid1.getStore().baseParams = editUserFormVar.getForm().getValues();
        editRoleListGrid1.getStore().load();
        editRoleListGrid2.getStore().baseParams = editUserFormVar.getForm().getValues();
        editRoleListGrid2.getStore().load();
        
        var tempUrl = "";
        if (editDeptLoader.clsField == "") {
            tempUrl = editDeptLoader.dataUrl.replace("&clsField=&", "&clsField=" + arrCur[0].data.id + "&");
        } else {
            tempUrl = editDeptLoader.dataUrl.replace("&clsField=" + oldEditUserId + "&", "&clsField=" + arrCur[0].data.id + "&");
        }
        editDeptLoader.dataUrl = tempUrl;
        
        // 第一次进来不重载树
        if (openEditUserFlag == true && editDataCreditSelectTree.isVisible() == true) {
            editDataCreditSelectTree.root.reload();
        }
        openEditUserFlag = true;
        
        // 保存原属性值，
        oldEditUserId = arrCur[0].data.id;
        
        if (editCheckModelVar.checked) {
            oldEditCheckModelParams = "1";
        } else {
            oldEditCheckModelParams = "0";
        }
        
        if (editAccreditVar.checked) {
            oldEditAccredit = "1";
        } else {
            oldEditAccredit = "0";
        }
        
        var editUserId = editUserIdVar.getValue();
        
        // 用户级联方式赋值
        Ext.Ajax.request({
            params : {
                synUserId : editUserId
            },
            url : "findCheckModel.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                editCheckModelVar.setValue(false);
                if (result.message != "") {
                    if (result.message == "1") {
                        editCheckModelVar.setValue(true);
                    }
                }
            }
        });
    }
    
    // 注册编辑界面隐藏前方法
    editUserWindow.on('beforehide', editUserWindowHide);
    function editUserWindowHide() {
        // editDataCreditSelectTree.root.reload();
        
        oldEditRoleIds = "";
        oldEditDeptIds = "";
        oldEditAccredit = "";
        oldEditCheckModelParams = "";
        clickFlag = false;
        treeClickFlag = false;
    }
    
    editRoleListGrid2.on("celldblclick", rela);
    editRoleListGrid1.on("celldblclick", relaNo);
    
    function initRoleIdVar() {
        // 保存原角色属性值，
        var editRolesAll = editRoleListGrid1.getStore().getRange();
        
        for ( var i = 0; i < editRolesAll.length; i++) {
            if (i == 0) {
                oldEditRoleIds = editRolesAll[i].data.id;
            } else {
                oldEditRoleIds = oldEditRoleIds + "," + editRolesAll[i].data.id;
            }
        }
    }
    
    function initDeptIdVar() {
        // 保存原网点属性值，
        var nodes = editDataCreditSelectTree.getChecked();
        for (i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                oldEditDeptIds += nodes[i].id;
            } else {
                oldEditDeptIds += nodes[i].id + ",";
            }
        }
    }
    
    // 从未分配角色列表中移动选中的角色到已分配角色列表中
    function rela() {
        // 点击前先保存原值
        if (!clickFlag) {
            initRoleIdVar();
            clickFlag = true;
        }
        var r = editRoleListGrid2.getSelectionModel().getSelected();
        
        if (r == null) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('tsRelaRole')}');
            return;
        }
        
        editRoleListGrid2.getStore().remove(r);
        var n = editRoleListGrid1.getStore().getCount();
        editRoleListGrid1.stopEditing();
        editRoleListGrid1.getStore().insert(n, r);
    }
    // 从已分配角色列表中移除选中的角色到未分配角色列表中
    function relaNo() {
        // 点击前先保存原值
        if (!clickFlag) {
            initRoleIdVar();
            clickFlag = true;
        }
        var r = editRoleListGrid1.getSelectionModel().getSelected();
        if (r == null) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('tsRelaNoRole')}');
            return;
        }
        
        editRoleListGrid1.getStore().remove(r);
        var n = editRoleListGrid2.getStore().getCount();
        editRoleListGrid2.stopEditing();
        editRoleListGrid2.getStore().insert(n, r);
    }
    
    function selectDept(node) {
        if (typeof (node.attributes.deptCode) == "undefined") {
            userListGrid.getStore().removeAll();
            deptCodeVar.setValue("");
            return;
        }
        
        deptCodeVar.setValue(node.attributes.deptCode);
        
        /* 当选择部门后把其它选择清空查询用户 */
        usernameVar.setValue("");
        onUserSearch();
    }
    
    // 注册部门树的根
    deptRoot.on('click', selectDept);
        
    function beforeUserListLoad(store){
        if (deptCodeVar.getValue() == "" && usernameVar.getValue() == ""){
            Ext.Msg.alert('${app:i18n('prompt')}', '${app:i18n('searchUser')}');
            return false;
        }
        store.baseParams = userQueryForm.getForm().getValues();
        store.baseParams["limit"] = userListPagingBar.pageSize;
    }
    
    // 查询用户
    function onUserSearch() {
        userListGrid.getStore().removeAll();
        if (deptCodeVar.getValue() != "") {
            checkAuthAndCall(deptCodeVar.getValue(), function(){
                userListGrid.getStore().load();
            });
        } else {
            userListGrid.getStore().load();
        }
    }
    
    // 判断登录用户是否有数据权限
    function checkAuthAndCall(deptCode, callback){
        // 判断登录用户是否有数据权限进行查询
        Ext.Ajax.request({
            url : 'existAcc.action',
            params : {
                deptCode : deptCode
            },            
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (!result.existAccFlag) {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('noNetAcc')}');
                } else {
                    callback();
                }
            },
            failure : function (response){
                if(Ext.JSON.encode(response.status) == 0){
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.connection.exception')}');
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('prompt.exception')}');
                }
            }            
        });     
    }
    
    var openAddUserFlag = false;
    // 点击新增按钮,弹出新增页面
    function addUserHdl() {
        if (deptCodeVar.getValue() == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selectDepMust')}');
            return;
        } else {
            checkAuthAndCall(deptCodeVar.getValue(), function(){
                userTypeCodeVar.setValue('1');
                selEmpDeptCodeVar.setValue(deptCodeVar.getValue());
                addUserWindow.show();
                // 展示角色列表
                roleListGrid.getStore().load();
                
                // 第一次进来不重新加载树
                if (openAddUserFlag == true && dataCreditSelectTree.isVisible() == true){
                    dataCreditSelectTree.root.reload();
                }
                openAddUserFlag = true;         
            });         
        }
    }
    
    // 选择用户类型为外部用户则弹出雇员选择页
    userTypeCodeVar.on('select', showSelectEmp);
    
    function selectInternalUser() {
        if (userTypeCodeVar.getValue() == "undefined" || userTypeCodeVar.getValue() == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('firstSelectUserType')}');
            return;
        }
        addReset();
        if (deptCodeVar.getValue() != "undefined" || deptCodeVar.getValue() != "") {
            addUsernameVar.enable();
        }
        selEmpShowDeptVar.setValue(deptCodeVar.getValue());
        
        selEmpWindow.show();
        queryEmp();
    }
    
    selEmpWindow.on('beforehide', selEmpWindowHide);
    function selEmpWindowHide() {
        if (userTypeCodeVar.getValue() == 1)
            addUsernameVar.disable();
        addUserEmpOfficephoneVar.disable();
        addUserEmpNameVar.disable();
        addUserEmpDutyNameVar.disable();
        
        selEmpCodeVar.setValue("");
        selEmpNameVar.setValue("");
        selEmpShowDeptVar.setValue("");
        selEmpDutyNameVar.setValue("");
        selEmpOfficephoneVar.setValue("");
        
        selectEmpFlag = false;
    }
    
    function showSelectEmp() {
        // 用户类型为外部用户
        if (userTypeCodeVar.getValue() == "0") {
            addUsernameVar.enable();
            addUserEmpOfficephoneVar.enable();
            addUserEmpNameVar.enable();
            addUserEmpDutyNameVar.enable();
            
        }
        // 用户类型为内部用户
        if (userTypeCodeVar.getValue() == "1") {
            addUsernameVar.disable();
            addUserEmpOfficephoneVar.disable();
            addUserEmpNameVar.disable();
            addUserEmpDutyNameVar.disable();
        }
        addReset();
    }
    
    // 是否设置数据权限
    isAccreditVar2.on('check', isAccreditVar2Check);
    function isAccreditVar2Check() {
        if (isAccreditVar2.checked) {
            dataCreditSelectTree.enable();
            checkModelVar.enable();
        } else {
            dataCreditSelectTree.disable();
            checkModelVar.disable();
        }
    }
    
    // 新增：是否继承子数据权限,选择时修改权限树属性
    checkModelVar.on('check', checkModelVarCheck);
    function checkModelVarCheck() {
        if (checkModelVar.checked) {
            dataCreditSelectTree.checkModel = 'cascadeDown';
        } else {
            dataCreditSelectTree.checkModel = 'multiple';
        }
    }
    // 编辑：是否继承子数据权限,选择时修改权限树属性
    editCheckModelVar.on('check', editCheckModelVarCheck);
    function editCheckModelVarCheck() {
        if (editCheckModelVar.checked) {
            editDataCreditSelectTree.checkModel = 'cascadeDown';
        } else {
            editDataCreditSelectTree.checkModel = 'multiple';
        }
    }
    
    // 编辑时点击是否设置数据权限
    editAccreditVar.on('check', editAccreditVarCheck);
    function editAccreditVarCheck() {
        if (editAccreditVar.checked) {
            editCheckModelVar.enable();
            editDataCreditSelectTree.enable();
        } else {
            editCheckModelVar.disable();
            editDataCreditSelectTree.disable();
        }
    }
    
    // 用户账号工号变化同步
    addUsernameVar.on('change', addUsernameVarChange);
    function addUsernameVarChange() {
        if (selectEmpFlag == true && userTypeCodeVar.getValue() == "0") {
        } else {
            addUserEmpCodeVar.setValue(addUsernameVar.getValue());
        }
        selectEmpFlag = false;
        
        // 如果用户类型是外部用户，并且没有从已有用户中选择用户，而是输入的用户信息，
        // 则后台给tm_billing_employee表增加一条记录
        // selectEmpIdVar.setValue(addUsernameVar.getValue());
    }
    
    // 新增窗口关闭时重置
    addUserWindow.on('close', addUserWindowClose);
    function addUserWindowClose() {
        addReset();
    }
    
    // 新增重置按钮
    function addReset() {
        addPasswordVar.setValue("");
        addUsernameVar.setValue("");
        newPasswordConfirmVar.setValue("");
        addUserEmpNameVar.setValue("");
        addUserEmpCodeVar.setValue("");
        addUserEmpOfficephoneVar.setValue("");
        addUserEmpDutyNameVar.setValue("");
        selectEmpIdVar.setValue("");
    }
    // 修改重置按钮
    function editReset() {
        
        editUsernameVar.setValue("");
        editUserEmpNameVar.setValue("");
        editUserEmpCodeVar.setValue("");
        editUserEmpDutyNameVar.setValue("");
        editUserIdVar.setValue("");
    }
    
    function beforeEmpListLoad(store){
        store.baseParams = selEmpForm.getForm().getValues();
        store.baseParams['selEmp.innerFlg'] = userTypeCodeVar.getValue() == 1;
        store.baseParams["limit"] = empQueryPagingBar.pageSize;
    }
    
    // 选择雇员页面－－－查询按钮
    function queryEmp() {
        empQueryList.getStore().load();
    }
    
    // 为true标识已经从雇员表中选择了内部用户
    var selectEmpFlag = false;
    
    // 选择雇员页面－－－选择按钮
    function selectEmp() {
        var selectTempEmp = empQueryList.getSelectionModel().getSelections();
        if (selectTempEmp[0] == null || typeof (selectTempEmp[0].get("id")) == "undefined" || selectTempEmp[0].get("id") == "") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('selectEmpTitle')}');
            return;
        }
        
        // 用户账号需与雇员工号一致，textfield改为不可用
        if (userTypeCodeVar.getValue() == 1)
            addUsernameVar.disable();
        addUserEmpOfficephoneVar.disable();
        addUserEmpCodeVar.disable();
        addUserEmpNameVar.disable();
        addUserEmpDutyNameVar.disable();
        
        selEmpWindow.hide();
        
        // 清空缓存
        empQueryList.getStore().removeAll();
        
        selectEmpFlag = true;
        
    }
    
    empQueryList.addListener("cellclick", onSelectEmp);
    function onSelectEmp() {
        var selectTempEmp = empQueryList.getSelectionModel().getSelections();
        selectEmpIdVar.setValue(selectTempEmp[0].get("id"));
        /* 设置新增页面的值 */
        addUserEmpNameVar.setValue(selectTempEmp[0].get("empName"));
        addUserEmpCodeVar.setValue(selectTempEmp[0].get("empCode"));
        addUserEmpOfficephoneVar.setValue(selectTempEmp[0].get("empOfficephone"));
        addUserEmpDutyNameVar.setValue(selectTempEmp[0].get("empDutyName"));
        
        // 用户账号需要和雇员工号一致
        addUsernameVar.setValue(selectTempEmp[0].get("empCode"));
    }
    
    // 注册编辑界面隐藏前方法
    addUserWindow.on('beforehide', addUserWindowHide);
    var beforehideFlag = false;
    function addUserWindowHide() {
        addReset();
        if (beforehideFlag == true && dataCreditSelectTree.isVisible() == true)
            dataCreditSelectTree.root.reload();
        
        beforehideFlag = true;
    }
    
    // 新增页面－－新增按钮,保存用户判断用户是否存在,返回判断结果
    function saveUserHdl() {
        if (newPasswordConfirmVar.getValue().trim() == '' || addUsernameVar.getValue().trim() == ''
                || addUserEmpNameVar.getValue().trim() == '') {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('namePassMust')}');
            return;
        }
        
        // 对密码和确认密码的检验，包括长度为八位，字符不重复
        if (!checkPassword(addPasswordVar.getValue(), newPasswordConfirmVar.getValue())) {
            return;
        }
        
        // 判断用户是否存在
        var addUsernameVarValue = addUsernameVar.getValue();
        Ext.Ajax.request({
            url : 'existUser.action',
            params : {
                addUserName : addUsernameVarValue
            },
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.existUserFlag) {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('existUser')}');
                    addReset();
                } else {
                    doSaveUserHdl();
                }
            }
        });
    }
    
    // 新增页面－－新增按钮,保存用户
    function doSaveUserHdl() {
        var newPasswordVarValue = addPasswordVar.getValue();
        var newPasswordConfirmVarValue = newPasswordConfirmVar.getValue();
        
        var addPasswordVarValue = addPasswordVar.getValue();
        var addUsernameVarValue = addUsernameVar.getValue();
        var addSelectEmpIdVarValue = selectEmpIdVar.getValue();
        var addEmpName = addUserEmpNameVar.getValue();
        var addEmpDuty = addUserEmpDutyNameVar.getValue();
        var addEmpPhone = addUserEmpOfficephoneVar.getValue();
        
        // 用户类型
        var addUserTypeCodeVar = userTypeCodeVar.getValue();
        if (addUserTypeCodeVar != "0" && addUserTypeCodeVar != "1") {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('confirmUserType')}');
            return;
        }
        
        // 是否设置数据权限
        var isAccreditValue;
        if (isAccreditVar2.checked) {
            isAccreditValue = "1";
        } else {
            isAccreditValue = "0";
        }
        
        // 给给用户数据授权
        var roleArray = new Array();
        
        var roleSelects = roleListGrid.getSelectionModel().getSelections();
        for ( var i = 0; i < roleSelects.length; i++)
            roleArray.push(roleSelects[i].data);
        var roleParams = Ext.util.JSON.encode(roleArray);
        
        // 得到选择的部门
        var nodes = dataCreditSelectTree.getChecked();
        var returnStr = "";
        for (i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                returnStr += nodes[i].id;
            } else {
                returnStr += nodes[i].id + ",";
            }
        }
        var dataCreditParams = returnStr;
        
        // 是否继承子权限
        var checkModelParams = "0";
        if (checkModelVar.checked) {
            checkModelParams = "1";
        }
        // added own dept data right
        var ownDeptDataRightFlg = "0";
        if (checkOwnerDeptRightVar.checked) {
            ownDeptDataRightFlg = "1";
        }
        
        Ext.MessageBox.wait('${app:i18n('saving')}');
        Ext.Ajax.request({
            params : {
                empName : addEmpName,
                empDutyName : addEmpDuty,
                empOfficephone : addEmpPhone,
                checkModelParams : checkModelParams,
                addDataCreditParams : dataCreditParams,
                addDataRightFlg : isAccreditValue,
                addUserTypeCode : addUserTypeCodeVar,
                selRoles : roleParams,
                deptCode : deptCodeVar.getValue(),
                addUserName : addUsernameVarValue,
                addUserPassword : addPasswordVarValue,
                selectEmpId : addSelectEmpIdVarValue,
                ownDeptRight : ownDeptDataRightFlg
            },
            url : "userSaveUser.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.message == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveSuccess')}');
                    onUserSearch();
                    addUserWindow.hide();
                    addReset();
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveFail')}');
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
        
        // 清空缓存
        empQueryList.getStore().removeAll();
        
    }
    
    // 对密码和确认密码的检验，包括长度为八位
    function checkPassword(password, confirmPassword) {
        if (password != confirmPassword) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('error.notEqu')}');
            return;
        }
        if (password.length != 8 || confirmPassword.length != 8) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('checkPasswordLength')}');
            return;
        }
        var pwdPtn = /^[\x21-\x7e]*$/;
        if(!pwdPtn.test(password)){
            Ext.MessageBox.alert('${app:i18n('ts')}', name + '${app:i18n('checkPasswordBuild')}');
            return false;
        }
        // 判断是否数字和英文组成
        var numFlag = 'false';
        var charFlag = 'false';
        for ( var i = 0; i < password.length; i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                numFlag = 'true';
            }
            if ((password.charAt(i) >= 'A' && password.charAt(i) <= 'Z')
                    || (password.charAt(i) >= 'a' && password.charAt(i) <= 'z')) {
                charFlag = 'true';
            }
        }
        
        if (charFlag != 'true' || numFlag != 'true') {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('checkPasswordBuild')}');
            return;
        }
        return true;
    }
    
    // 删除用户
    function preDelUserHdl() {
        var delUserIdsArray = new Array();
        delUserIdsArray = userListGrid.getSelectionModel().getSelections();
        if (delUserIdsArray.length == 0) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('seleUserTitle')}');
            return;
        }
        var delUserIds;
        for ( var i = 0; i < delUserIdsArray.length; i++) {
            if (i == 0) {
                delUserIds = delUserIdsArray[i].id;
            } else {
                delUserIds = delUserIds + "," + delUserIdsArray[i].id;
            }
        }
        
        Ext.MessageBox.confirm('${app:i18n('prompt')}','${app:i18n('trueDelUser')}',delUserHdl);
    }
    function delUserHdl(result) {
        var delUserIdsArray = userListGrid.getSelectionModel().getSelections();
        var delUserIds;
        for ( var i = 0; i < delUserIdsArray.length; i++) {
            if (i == 0) {
                delUserIds = delUserIdsArray[i].data.id;
            } else {
                delUserIds = delUserIds + "," + delUserIdsArray[i].data.id;
            }
        }
        
        if (result == 'yes') {
            Ext.Ajax.request({
                params : {
                    delUserIds : delUserIds
                },
                url : "userDelUser.action",
                success : function(response) {
                    var resp = Ext.util.JSON.decode(response.responseText);
                    if (resp.success) {
                        onUserSearch();
                        Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('delSuccess')}');
                    } else {
                        Ext.MessageBox.alert('${app:i18n('prompt')}', '${app:i18n('delUserFailed')}');
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
    }
    
    // 修改密码--打开窗口
    function modifyPasswordHdl() {
        var arrCur = userListGrid.getSelectionModel().getSelections();
        if (arrCur == null || arrCur.length != 1) {
            Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('onlyOneUser')}');
            return;
        }
        modUserIdVar.setValue(arrCur[0].data.id);
        modUserNameVar.setValue(arrCur[0].data.username);
        modPasswordWindow.show();
    }
    // 修改密码－－保存
    function saveUserPasswordHdl() {
        var newPasswordVarValue = modPass1Var.getValue();
        var newPasswordConfirmVarValue = modPass2Var.getValue();
        
        // 对密码和确认密码的检验，包括长度为八位，字符不重复
        if (!checkPassword(newPasswordVarValue, newPasswordConfirmVarValue)) {
            return;
        }
        
        var curUserId = modUserIdVar.getValue();
        var curUserName = modUserNameVar.getValue();
        Ext.Ajax.request({
            params : {
                username : curUserName,
                selectUserId : curUserId,
                modPassword : newPasswordVarValue
            },
            url : "modUserPassword.action",
            success : function(response) {
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.message == "true") {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('modPassSuccess')}');
                    modPasswordWindow.hide();
                    modReset();
                } else {
                    Ext.MessageBox.alert('${app:i18n('prompt')}','${app:i18n('saveFail')}');
                }
            }
        });
    }
    
    function modReset() {
        modPass1Var.setValue("");
        modPass2Var.setValue("");
    }
    
    // 得到选择的选择的网点权限
    var unChecked = new Array();
    function getSelectDeptCodes() {
        var returnStr = "";
        var nodes = editDataCreditSelectTree.getChecked();
        for (i = 0; i < nodes.length; i++) {
            if (i == nodes.length - 1) {
                returnStr += nodes[i].id;
            } else {
                returnStr += nodes[i].id + ",";
            }
        }
        return returnStr;
    }
    
    function getUnSelectDeptCodes() {
        var treeNodes = editDeptRootVar.childNodes;
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