package com.somecom.service.impl;//package com.dmrz.dm.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.somecom.dao.SysUserDao;
//import com.somecom.entity.SysUser;
//import com.somecom.enums.SystemDataStatusEnum;
//import com.somecom.repository.SysUserRepository;
//import com.somecom.service.DeptService;
//import com.somecom.service.SysUserService;
//import com.somecom.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.util.Collections;
//import java.util.List;
//
//@Service
//public class SysUserServiceImpl implements UserService {
//    @Autowired
//    private SysUserRepository userRepository;
//
//    @Autowired
//    private DeptService deptService;
//
//
//    public SysUser getUser(SysUser user) {
//        return userDao.selectById(user.getId());
//    }
//
//    public SysUser login(SysUser example) {
//        return userDao.selectOne(new QueryWrapper<>(example));
//    }
//
//    /**
//     * 根据用户名查询用户数据
//     *
//     * @param username 用户名
//     * @return 用户数据
//     */
//    @Override
//    public SysUser getByName(String username) {
//        return userDao.selectOne(new QueryWrapper<>(SysUser.ofName(username)));
//    }
//
//    /**
//     * 用户名是否存在
//     *
//     * @param user 用户对象
//     * @return 用户数据
//     */
//    @Override
//    public Boolean repeatByUsername(SysUser user) {
//        Long id = user.getId() != null ? user.getId() : Long.MIN_VALUE;
//        return null;
//    }
//
//    /**
//     * 根据用户ID获取用户信息
//     *
//     * @param id 用户ID
//     */
//    @Override
//    public SysUser getById(Integer id) {
//        return userDao.selectById(id);/*userRepository.findById(id).orElse(null);*/
//    }
//
//    /**
//     * 获取分页列表数据
//     *
//     * @param user 实体对象
//     * @return 返回分页数据
//     */
//    @Override
//    public Page<SysUser> getPageList(SysUser user) {
//        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
//        if (StringUtils.hasText(user.getUsername())) {
//            wrapper.like("username", user.getUsername());
//        }
//        if (StringUtils.hasText(user.getUsername())) {
//            wrapper.like("phone", user.getPhone());
//        }
//        if (StringUtils.hasText(user.getUsername())) {
//            wrapper.like("status", user.getStatus());
//        }
//        List<SysUser> users = userDao.selectList(wrapper);
//
//        return new PageImpl<>(users);
//        /*// 创建分页对象
//        PageRequest page = PageSort.pageRequest(SpringDataWebProperties.Sort.Direction.ASC);
//
//        // 使用Specification复杂查询
//        return userRepository.findAll(new Specification<SysUser>(){
//
//            @Override
//            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> preList = new ArrayList<>();
//                if(user.getId() != null){
//                    preList.add(cb.equal(root.get("id").as(Long.class), user.getId()));
//                }
//                if(user.getUsername() != null){
//                    preList.add(cb.equal(root.get("username").as(String.class), user.getUsername()));
//                }
//                if(user.getNickname() != null){
//                    preList.add(cb.like(root.get("nickname").as(String.class), "%"+ user.getNickname() + "%"));
//                }
//                if(user.getDept() != null){
//                    // 联级查询部门
//                    Dept dept = user.getDept();
//                    List<Long> deptIn = new ArrayList<>();
//                    deptIn.add(dept.getId());
//                    List<Dept> deptList = deptService.getListByPidLikeOk(dept.getId());
//                    deptList.forEach(item -> deptIn.add(item.getId()));
//
//                    Join<SysUser, Dept> join = root.join("dept", JoinType.INNER);
//                    CriteriaBuilder.In<Long> in = cb.in(join.get("id").as(Long.class));
//                    deptIn.forEach(in::value);
//                    preList.add(in);
//                }
//
//                // 数据状态
//                if(user.getStatus() != null){
//                    preList.add(cb.equal(root.get("status").as(Byte.class), user.getStatus()));
//                }
//
//                Predicate[] pres = new Predicate[preList.size()];
//                return query.where(preList.toArray(pres)).getRestriction();
//            }
//
//        }, page);*/
//    }
//
//    /**
//     * 保存用户
//     *
//     * @param user 用户实体类
//     */
//    @Override
//    public SysUser save(SysUser user) {
//        userDao.insert(user);
//        return user;
//    }
//
//    /**
//     * 保存用户列表
//     *
//     * @param userList 用户实体类
//     */
//    @Override
//    @Transactional
//    public List<SysUser> save(List<SysUser> userList) {
//        userDao.insert(userList.get(0));
//        return Collections.emptyList();
//    }
//
//    /**
//     * 状态(启用，冻结，删除)/批量状态处理
//     */
//    @Override
//    @Transactional
//    public Boolean updateStatus(SystemDataStatusEnum statusEnum, List<Long> ids) {
//        // 联级删除与角色之间的关联
//        if (statusEnum == SystemDataStatusEnum.DELETE) {
//            return true/*userRepository.deleteByIdIn(ids) > 0*/;
//        }
//        return false;//userRepository.updateStatus(statusEnum.getCode(), ids) > 0;
//    }
//}
