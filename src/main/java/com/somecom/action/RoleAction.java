package com.somecom.action;

import com.somecom.action.base.BaseActionMap;
import com.somecom.action.base.ResetLog;
import com.somecom.action.model.BusinessMethod;
import com.somecom.entity.SysRole;

import javax.persistence.Table;

/**
 * 角色日志行为
 *
 * @author Sam
 * @date 2018/10/14
 */
public class RoleAction extends BaseActionMap {

    public static final String ROLE_SAVE = "role_save";
    public static final String ROLE_AUTH = "role_auth";

    @Override
    public void init() {
        // 保存日志行为
        putMethod(ROLE_SAVE, new BusinessMethod("日志管理", "roleSave"));
        // 角色授权行为
        putMethod(ROLE_AUTH, new BusinessMethod("角色授权", "roleAuth"));
    }

    /**
     * 保存用户行为方法
     */
    public void roleSave(ResetLog resetLog) {
        resetLog.getActionLog().setMessage("日志成功：${title}");
        SaveAction.defaultMethod(resetLog);
    }

    /**
     * 角色授权行为方法
     */
    public void roleAuth(ResetLog resetLog) {
        SysRole sysRole = (SysRole) resetLog.getParam("sysRole");
        Table table = SysRole.class.getAnnotation(Table.class);
        resetLog.getActionLog().setModel(table.name());
        resetLog.getActionLog().setRecordId(sysRole.getId());
        if (resetLog.isSuccess()) {
            resetLog.getActionLog().setMessage("角色授权成功：" + sysRole.getTitle());
        } else {
            resetLog.getActionLog().setMessage("角色授权失败：" + sysRole.getTitle());
        }
    }
}
