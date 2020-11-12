package com.vanni.community.service;

import com.vanni.community.dao.DiscussPostMapper;
import com.vanni.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPostList(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPostList(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId)
    {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
