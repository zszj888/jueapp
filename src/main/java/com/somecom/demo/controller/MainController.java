package com.somecom.demo.controller;

import com.somecom.demo.entity.DemoT;
import com.somecom.demo.entity.Ontegral;
import com.somecom.demo.entity.Order;
import com.somecom.demo.entity.User;
import com.somecom.demo.model.ResultVo;
import com.somecom.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IntegralRepository integralRepository;
    @Autowired
    private DemoRepository demoRepository;

    @GetMapping(path = "/getDemo")
    public ResultVo getDemo() {
//        ArrayList<DemoT> list  = new ArrayList<>();
//        for (int i=0;i<30;i++){
//            DemoT demoT = new DemoT();
//            demoT.setName("demoName"+i);
//            list.add(demoT);
//        }
//        demoRepository.saveAll(list);
//        DemoT ex = new DemoT();
//        ex.setName("Jacky");
//        System.out.println(demoRepository.findAll(Example.of(ex),Sort.by(Sort.Direction.ASC,"id")));
        Optional<DemoT> byId = demoRepository.findById(1);
        DemoT ex = null;
        if (byId.isPresent()) ex = byId.get();
        System.out.println(ex);
        if (byId.isPresent()) ex.setName("jacky444");
        demoRepository.save(ex);

        return ResultVo.ok(demoRepository
                .findById(1));
    }

    @GetMapping(path = "/getGoodList")
    public ResultVo getGoodList() throws InterruptedException {
        return ResultVo.ok(goodsRepository.findAll());
    }

    @GetMapping(path = "/getOrderHistory")
    public ResultVo getOrderHistory() {
        Order order = new Order();
        order.setAddress("shanghai pudong chuangxinxilu");
        order.setCreat_time(LocalDateTime.now());
        orderRepository.save(order);
        return ResultVo.ok(orderRepository.findAll());
    }

    @GetMapping(path = "/getuserInfo")
    public ResultVo getuserInfo() {
        User n = new User();
        n.setIntegral(1);
        n.setBusiness_info("someinfo1231294145");
        n.setBusiness_phone("13921828341");
        n.setAddress("hubeisheng shiyanshi zhushanxian");
        n.setLevel("1");
        n.setUser_name("nihaohwohao");
        n.setPhone("13800138000");
        userRepository.save(n);
        return ResultVo.ok(userRepository.findAll().iterator().next());
    }

    @GetMapping(path = "/getOntegralList")
    public ResultVo getOntegralList() {
        Ontegral ontegral = new Ontegral();
        ontegral.setDesc("some desc for test 说点中文吧");
        ontegral.setGood_img_url("some good imag url");
        ontegral.setGood_name("火烧馍，我们都爱吃");
        ontegral.setIcon_url("http://www.111.com");
        ontegral.setIntegral_total(1000000);
        ontegral.setPromotion_price(BigDecimal.valueOf(200.909));
        ontegral.setIs_integral((byte) 1);
        ontegral.setPrice(BigDecimal.valueOf(1000.001));
        ontegral.setSale_num(23330);
        ontegral.setSpecifications("大杯',  商品规格 大杯,中杯,小杯");
        integralRepository.save(ontegral);
        ontegral.setPrice(BigDecimal.valueOf(555.88));
        return ResultVo.ok(integralRepository.findAll());
    }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewUser(@RequestParam String name
            , @RequestParam String email) {

        User n = new User();
        n.setAddress(name);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}