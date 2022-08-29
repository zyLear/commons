package com.zylear.commons.controller;

import com.zylear.commons.bean.Result;
import com.zylear.commons.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiezongyu
 * @date 2021/4/7
 */
@Api(tags = "test module")
@RestController
@RequestMapping
public class TestController {


    @ApiOperation(value = "test")
    @GetMapping("/v1/pub/test")
    public Result test(@RequestParam String test) {
        return ResultUtil.success(test);
    }

}
