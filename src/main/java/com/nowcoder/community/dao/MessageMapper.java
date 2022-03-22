package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话总行数
    int selectConversationCount(int userId);

    // 查询某个会话（详情页）包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读消息数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 发送私信
    int insertMessage(Message message);

    // 修改消息状态（把未读消息设置为已读/删除）
    int updateStatus(List<Integer> ids, int status);

}
