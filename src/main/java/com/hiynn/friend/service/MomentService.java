package com.hiynn.friend.service;

import com.hiynn.friend.dto.CommentDTO;
import com.hiynn.friend.dto.MomentDTO;

public interface MomentService {

    /***
     * 描述 发朋友圈并添加到时间线里
     * @author xuxitan
     * @date 2020/2/14 14:49
     * @param momentDTO
     * @return void
     */
    void saveMomentAndTimeLine(MomentDTO momentDTO);

    /***
     * 描述 给朋友圈点赞
     * @author xuxitan
     * @date 2020/2/14 16:53
     * @param momentId
     * @param userId
     * @return void
     */
    void praiseMoment(String momentId,String userId);

    /***
     * 描述 给朋友圈评论回复
     * @author xuxitan
     * @date 2020/2/17 10:48
     * @param comment
     * @return void
     */
    void commentMoment(CommentDTO comment);
}
