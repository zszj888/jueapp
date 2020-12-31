package com.somecom.web;


import com.somecom.annotation.EntityParam;
import com.somecom.entity.Dict;
import com.somecom.enums.ResultEnum;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.exception.ResultException;
import com.somecom.service.DictService;
import com.somecom.thymeleaf.utility.DictUtil;
import com.somecom.utils.EntityBeanUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.StatusUtil;
import com.somecom.validator.DictValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Sam
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    public String index(Model model, Dict dict) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取字典列表
        Example<Dict> example = Example.of(dict, matcher);
        Page<Dict> list = dictService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "system/dict/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    public String toAdd() {
        return "system/dict/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Dict dict, Model model) {
        model.addAttribute("dict", dict);
        return "system/dict/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     */
    @PostMapping({"/add", "/edit"})
    @ResponseBody
    public SysResultVo save(@Validated DictValid valid, @EntityParam Dict dict) {
        // 清除字典值两边空格
        dict.setValue(dict.getValue().trim());

        // 判断字典标识是否重复
        if (dictService.repeatByName(dict)) {
            throw new ResultException(ResultEnum.DICT_EXIST);
        }

        // 复制保留无需修改的数据
        if (dict.getId() != null) {
            Dict beDict = dictService.getById(dict.getId());
            EntityBeanUtil.copyProperties(beDict, dict);
        }

        // 保存数据
        dictService.save(dict);
        if (dict.getId() != null) {
            DictUtil.clearCache(dict.getName());
        }
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") Dict dict, Model model) {
        model.addAttribute("dict", dict);
        return "system/dict/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @ResponseBody
    public SysResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
        if (dictService.updateStatus(systemDataStatusEnum, ids)) {
            return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
        }
    }
}
