package com.vanni.community.controller;

import com.vanni.community.entity.Event;
import com.vanni.community.entity.LikeService;
import com.vanni.community.entity.User;
import com.vanni.community.event.EventProducer;
import com.vanni.community.util.CommunityConstant;
import com.vanni.community.util.CommunityUtil;
import com.vanni.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
        User user = hostHolder.getUser();

        // 点赞操作
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 用户点赞状态
        int status = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        // 响应信息到客户端
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", status);

        // 触发点赞事件
        if( status == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(user.getId())
                    .setEntityType(entityType)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);

            eventProducer.fireEvent(event);
        }

        return CommunityUtil.getJSONString(0, null, map);

    }
}

