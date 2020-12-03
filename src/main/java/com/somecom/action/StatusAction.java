package com.somecom.action;

import com.somecom.action.base.BaseActionMap;
import com.somecom.action.base.ResetLog;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.StatusUtil;

import java.util.List;

/**
 * 通用：记录数据状态的行为
 *
 * @author 小懒虫
 * @date 2018/10/14
 */
public class StatusAction extends BaseActionMap {

    /**
     * 重新包装保存的数据行为方法
     *
     * @param resetLog ResetLog对象数据
     */
    @SuppressWarnings("unchecked")
    public static void defaultMethod(ResetLog resetLog) {
        if (resetLog.isSuccessRecord()) {
            String param = (String) resetLog.getParam("param");
            SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
            List<Long> ids = (List<Long>) resetLog.getParam("ids");
            resetLog.getActionLog().setMessage(systemDataStatusEnum.getMessage() + "ID：" + ids.toString());
        }
    }

    @Override
    public void init() {
        // 记录数据状态改变日志
        putMethod("default", "defaultMethod");
    }
}
