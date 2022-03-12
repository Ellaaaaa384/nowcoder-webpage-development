package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 需要实现分页
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit); // 评论/帖子/用户/课程的评论

    int selectCountByEntity(int entityType, int entityId);

}
