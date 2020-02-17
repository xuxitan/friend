package com.hiynn.friend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xuxitan
 * @description
 * @date 2020/2/14 14:55
 **/
@Data
public class MomentDTO {

    private String userId;

    private String content;

    private String topic;

    private int isShow;

    private MultipartFile file;
}
