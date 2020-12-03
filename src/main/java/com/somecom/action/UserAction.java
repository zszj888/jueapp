package com.somecom.action;

import com.somecom.action.base.BaseActionMap;
import com.somecom.action.base.ResetLog;
import com.somecom.action.model.BusinessMethod;
import com.somecom.action.model.LoginMethod;
import com.somecom.entity.ActionLog;
import com.somecom.entity.SysUser;
import com.somecom.service.ActionLogService;
import com.somecom.utils.SpringContextUtil;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.BeanUtils;

import javax.persistence.Table;
import java.util.List;

/**
 * 用户日志行为
 *
 * @author 小懒虫
 * @date 2018/10/14
 */
public class UserAction extends BaseActionMap {

    public static final String USER_LOGIN = "user_login";
    public static final String USER_SAVE = "user_save";
    public static final String EDIT_PWD = "edit_pwd";
    public static final String EDIT_ROLE = "edit_role";

    @Override
    public void init() {
        // 用户登录行为
        putMethod(USER_LOGIN, new LoginMethod("用户登录", "userLogin"));
        // 保存用户行为
        putMethod(USER_SAVE, new BusinessMethod("用户管理", "userSave"));
        // 修改用户密码行为
        putMethod(EDIT_PWD, new BusinessMethod("用户密码", "editPwd"));
        // 角色分配行为
        putMethod(EDIT_ROLE, new BusinessMethod("角色分配", "editRole"));
    }

    /**
     * 用户登录行为方法
     */
    public void userLogin(ResetLog resetLog) {
        ActionLog actionLog = resetLog.getActionLog();
        if (resetLog.isSuccess()) {
            actionLog.setMessage("后台登录成功");
        } else {
            String username = (String) resetLog.getParam("username");
            SysResultVo sysResultVo = (SysResultVo) resetLog.getRetValue();
            actionLog.setOperName(username);
            actionLog.setMessage(String.format("后台登录失败：[%s]%s", username, sysResultVo.getMsg()));
        }
    }

    /**
     * 保存用户行为方法
     */
    public void userSave(ResetLog resetLog) {
        resetLog.getActionLog().setMessage("用户成功：${username}");
        SaveAction.defaultMethod(resetLog);
    }

    /**
     * 修改用户密码行为方法
     */
    public void editPwd(ResetLog resetLog) {
        @SuppressWarnings("unchecked") List<SysUser> users = (List<SysUser>) resetLog.getParam("users");
        Table table = SysUser.class.getAnnotation(Table.class);
        String message = "修改用户密码成功";
        if (!resetLog.isSuccess()) {
            message = "修改用户密码失败";
        }
        ActionLogService actionLogService = SpringContextUtil.getBean(ActionLogService.class);

        String finalMessage = message;
        users.forEach(user -> {
            ActionLog actionLog = new ActionLog();
            BeanUtils.copyProperties(resetLog.getActionLog(), actionLog);
            actionLog.setModel(table.name());
            actionLog.setRecordId(user.getId());
            actionLog.setMessage(finalMessage + user.getUsername());

            // 保存日志
            actionLogService.save(actionLog);
        });
        resetLog.setRecord(false);
    }

    /**
     * 角色分配行为方法
     */
    public void editRole(ResetLog resetLog) {
        SysUser user = (SysUser) resetLog.getParam("user");
        Table table = SysUser.class.getAnnotation(Table.class);
        resetLog.getActionLog().setModel(table.name());
        resetLog.getActionLog().setRecordId(user.getId());
        if (resetLog.isSuccess()) {
            resetLog.getActionLog().setMessage("角色分配成功：" + user.getUsername());
        } else {
            resetLog.getActionLog().setMessage("角色分配失败：" + user.getUsername());
        }
    }
}
