package com.hiynn.friend.controller;

import com.hiynn.friend.dto.CommentDTO;
import com.hiynn.friend.dto.MomentDTO;
import com.hiynn.friend.dto.ShowDTO;
import com.hiynn.friend.service.MomentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 朋友圈评论、点赞
 *
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:38
 **/
@Api(tags = "朋友圈评论、点赞控制器")
@RestController
public class MomentController {

    @Autowired
    private MomentService momentService;

    /***
     * 描述 发朋友圈
     * @author xuxitan
     * @date 2020/2/14 14:46
     * @param momentDTO
     * @return java.lang.String
     */
    @ApiOperation(value = "发布朋友圈", notes = "发布朋友圈")
    @PostMapping("sendMoment")
    public String sendMoment(@RequestBody MomentDTO momentDTO) {
        momentService.saveMomentAndTimeLine(momentDTO);
        return "ok";
    }

    /**
     * 给朋友圈点赞
     *
     * @param momentId 朋友圈id
     * @param userId   点赞的用户
     * @return
     */
    @ApiOperation(value = "给朋友圈点赞", notes = "给朋友圈点赞")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "点赞的用户", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "momentId", value = "朋友圈id", dataType = "String", paramType = "path", required = true
            )})
    @GetMapping("praise/{userId}/{momentId}")
    public String praise(@PathVariable String userId, @PathVariable String momentId) {
        momentService.praiseMoment(momentId, userId);
        return "ok";
    }

    /***
     * 描述 给朋友圈评论
     * @author xuxitan
     * @date 2020/2/17 10:47
     * @param comment
     * @return java.lang.String
     */
    @ApiOperation(value = "给朋友圈评论", notes = "给朋友圈评论")
    @PostMapping("comment")
    public String praise(@RequestBody CommentDTO comment) {
        momentService.commentMoment(comment);
        return "ok";
    }

    /***
     * 描述 设置朋友圈的公开、隐私
     * @author xuxitan
     * @date 2020/2/18 15:57
     * @param show
     * @return java.lang.String
     */
    @ApiOperation(value = "设置朋友圈的公开、隐私", notes = "设置朋友圈的公开、隐私")
    @PostMapping("isShow")
    public String isShow(@RequestBody ShowDTO show) {
        momentService.momentIsShow(show);
        return "OK";
    }
}
