package com.hiynn.friend.controller;

import com.hiynn.friend.dto.CommentDTO;
import com.hiynn.friend.dto.MomentDTO;
import com.hiynn.friend.service.MomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 朋友圈评论、点赞
 *
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:38
 **/
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
    @PostMapping("sendMoment")
    public String sendMoment(MomentDTO momentDTO) {
        momentService.saveMomentAndTimeLine(momentDTO);
        return "ok";
    }

    /**
     * 给朋友圈点赞
     *
     * @param momentId
     * @param userId   点赞的用户
     * @return
     */
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
    @PostMapping("comment")
    public String praise(@RequestBody CommentDTO comment){
        momentService.commentMoment(comment);
        return "ok";
    }
}
