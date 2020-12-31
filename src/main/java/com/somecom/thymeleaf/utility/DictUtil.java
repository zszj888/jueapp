package com.somecom.thymeleaf.utility;


import com.somecom.entity.Dict;
import com.somecom.entity.Role;
import com.somecom.entity.SysUser;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.repo.RoleRepository;
import com.somecom.repository.SysUserRepository;
import com.somecom.service.DictService;
import com.somecom.utils.SpringContextUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.data.domain.Example;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典提取工具对象
 *
 * @author Sam
 * @date 2018/8/14
 */
public class DictUtil {

    private static Cache dictCache = EhCacheUtil.getDictCache();

    /**
     * 获取字典值集合
     *
     * @param label 字典标识
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> value(String label) {
        Map<String, String> value = null;
        Element dictEle = dictCache.get(label);
        if (dictEle != null) {
            value = (Map<String, String>) dictEle.getObjectValue();
        } else {
            DictService dictService = SpringContextUtil.getBean(DictService.class);
            Dict dict = dictService.getByNameOk(label);
            if (dict != null) {
                String dictValue = dict.getValue();
                String[] outerSplit = dictValue.split(",");
                value = new LinkedHashMap<>();
                for (String osp : outerSplit) {
                    String[] split = osp.split(":");
                    if (split.length > 1) {
                        value.put(split[0], split[1]);
                    }
                }
                dictCache.put(new Element(dict.getName(), value));
            }else {
                if ("BROKER_LIST".equalsIgnoreCase(label)) {
                    SysUserRepository userRepository = SpringContextUtil.getBean(SysUserRepository.class);//BROKER_LIST
                    SysUser ex = new SysUser();
                    ex.setBroker(true);
                    List<SysUser> all = userRepository.findAll(Example.of(ex));
                    value = all.stream().collect(Collectors.toMap(k -> k.getId().toString(), SysUser::getNickname));
                    dictCache.put(new Element(label, value));
                }
            }
        }
        return value;
    }

    /**
     * 根据选项编码获取选项值
     *
     * @param label 字典标识
     * @param code  选项编码
     */
    public static String keyValue(String label, String code) {
        Map<String, String> list = DictUtil.value(label);
        if (list != null) {
            return list.get(code);
        } else {
            return "";
        }
    }

    /**
     * 封装数据状态字典
     *
     * @param status 状态
     */
    public static String dataStatus(Byte status) {
        String label = "DATA_STATUS";
        return DictUtil.keyValue(label, String.valueOf(status));
    }

    /**
     * 清除缓存中指定的数据
     *
     * @param label 字典标识
     */
    public static void clearCache(String label) {
        Element dictEle = dictCache.get(label);
        if (dictEle != null) {
            dictCache.remove(label);
        }
    }
}
