package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 查询有关
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
    // userId用于未来个人主页发帖记录显示，offset用于分页起始行，limit表示每页最多行数

    // 用@Param注解给参数取别名
    // 如果只有一个参数，且在<if>中使用（动态拼接sql），必须取别名（也可以写和原名一样的名）
    int selectDiscussPostRows(@Param("userId") int userId);

    // 增加帖子
    int insertDiscussPost(DiscussPost discussPost);

    // 查询帖子（根据id查询详细信息）
    DiscussPost selectDiscussPostById(int id);
}
