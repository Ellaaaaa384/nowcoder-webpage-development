package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***"; // 替换符

    // 初始化根节点
    private TrieNode rootNode = new TrieNode();

    // 构造树型
    @PostConstruct // 初始化方法，容器实例化bean后，调用构造器后，该方法被自动调用
    public void init() {
        try (
                // 读取敏感词文件中的字符
                // getClassLoader()获取类加载器（从类路径下加载资源，target/classes）
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words");
                // 把字符流转化成字节流，再转化成缓冲流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                // 在try中的语句会在finally中被自动关闭
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 敏感词添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }
    }

    // 将敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i=0; i<keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            // 指向子节点，进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    // 过滤敏感词(参数：待过滤文本，返回过滤后的文本)
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针一，指向树的节点
        TrieNode tempNode = rootNode;
        // 指针二、三
        int begin = 0;
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于root node，将此符号计入结果，让指针2 += 1
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                position = ++begin;
                // 指针1重新指向root node
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现了敏感词，替换begin～position的字符串
                sb.append(REPLACEMENT);
                // 指针2、3到position += 1处
                begin = ++position;
                tempNode = rootNode;
            } else {
                // 检查下一个字符（前面已经匹配了）
                position++;
            }
        }
        // 将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF); // 0x2E80～0x9FFF东亚文字
    }

    // 定义前缀树
    private class TrieNode { // 描述前缀树的某个节点

        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 描述树形结构（childNode）key是下级节点的字符，value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取此节点的办法
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }

}
