package com.somecom.web;

import com.somecom.action.SaveAction;
import com.somecom.action.StatusAction;
import com.somecom.annotation.ActionLog;
import com.somecom.annotation.EntityParam;
import com.somecom.entity.Menu;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.service.SysMenuService;
import com.somecom.thymeleaf.utility.DictUtil;
import com.somecom.utils.EntityBeanUtil;
import com.somecom.utils.HttpServletUtil;
import com.somecom.utils.ResultVoUtil;
import com.somecom.utils.StatusUtil;
import com.somecom.validator.MenuValid;
import com.somecom.vo.SysResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
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
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("admin/system/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 跳转到列表页面
     */
    @GetMapping("/index")
    public String index(Model model) {
        String search = HttpServletUtil.getRequest().getQueryString();
        model.addAttribute("search", search);
        return "system/menu/index";
    }

    /**
     * 菜单数据列表
     */
    @GetMapping("/list")
    @ResponseBody
    public SysResultVo list(Menu menu) {
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取菜单列表
        Example<Menu> example = Example.of(menu, matcher);
        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        List<Menu> list = sysMenuService.getListByExample(example, sort);

        // TODO: 2019/2/25 菜单类型处理方案
        list.forEach(editMenu -> {
            String type = String.valueOf(editMenu.getType());
            editMenu.setRemark(DictUtil.keyValue("MENU_TYPE", type));
        });
        return ResultVoUtil.success(list);
    }

    /**
     * 获取排序菜单列表
     */
    @GetMapping("/sortList/{pid}/{notId}")
    @ResponseBody
    public Map<Integer, String> sortList(
            @PathVariable(value = "pid", required = false) Long pid,
            @PathVariable(value = "notId", required = false) Long notId) {
        // 本级排序菜单列表
        notId = notId != null ? notId : (long) 0;
        List<Menu> levelMenu = sysMenuService.getListByPid(pid, notId);
        Map<Integer, String> sortMap = new TreeMap<>();
        for (int i = 1; i <= levelMenu.size(); i++) {
            sortMap.put(i, levelMenu.get(i - 1).getTitle());
        }
        return sortMap;
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping({"/add", "/add/{pid}"})
    public String toAdd(@PathVariable(value = "pid", required = false) Menu pMenu, Model model) {
        model.addAttribute("pMenu", pMenu);
        return "system/menu/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") Menu menu, Model model) {
        Menu pMenu = sysMenuService.getById(menu.getPid());
        model.addAttribute("menu", menu);
        model.addAttribute("pMenu", pMenu);
        return "system/menu/add";
    }

    /**
     * 保存添加/修改的数据
     *
     * @param valid 验证对象
     * @param menu  实体对象
     */
    @PostMapping("/save")
    @ResponseBody
    @ActionLog(name = "菜单管理", message = "菜单：${title}", action = SaveAction.class)
    public SysResultVo save(@Validated MenuValid valid, @EntityParam Menu menu) {
        if (menu.getId() == null) {
            // 排序为空时，添加到最后
            if (menu.getSort() == null) {
                Integer sortMax = sysMenuService.getSortMax(menu.getPid());
                menu.setSort(sortMax != null ? sortMax - 1 : 0);
            }
        }

        // 添加/更新全部上级序号
        Menu pMenu = sysMenuService.getById(menu.getPid());
        menu.setPids(pMenu.getPids() + ",[" + menu.getPid() + "]");

        // 复制保留无需修改的数据
        if (menu.getId() != null) {
            Menu beMenu = sysMenuService.getById(menu.getId());
            EntityBeanUtil.copyProperties(beMenu, menu);
        }

        // 排序功能
        Integer sort = menu.getSort();
        Long notId = menu.getId() != null ? menu.getId() : 0;
        List<Menu> levelMenu = sysMenuService.getListByPid(menu.getPid(), notId);
        levelMenu.add(sort, menu);
        for (int i = 1; i <= levelMenu.size(); i++) {
            levelMenu.get(i - 1).setSort(i);
        }

        // 保存数据
        sysMenuService.save(levelMenu);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    public String toDetail(@PathVariable("id") Menu menu, Model model) {
        model.addAttribute("menu", menu);
        return "system/menu/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @ResponseBody
    @ActionLog(name = "菜单状态", action = StatusAction.class)
    public SysResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        SystemDataStatusEnum systemDataStatusEnum = StatusUtil.getStatusEnum(param);
        if (sysMenuService.updateStatus(systemDataStatusEnum, ids)) {
            return ResultVoUtil.success(systemDataStatusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(systemDataStatusEnum.getMessage() + "失败，请重新操作");
        }
    }


}
