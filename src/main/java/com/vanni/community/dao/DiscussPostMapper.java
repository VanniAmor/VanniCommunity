package com.vanni.community.dao;

import com.vanni.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPostList(int userId, int offset, int limit);

    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且要在<if>里面使用，则必须添加别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
