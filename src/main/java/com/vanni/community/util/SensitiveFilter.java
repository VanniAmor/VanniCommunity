package com.vanni.community.util;

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

    // 敏感词替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init()
    {
        try (
                // 利用反射，获取敏感词列表
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ( (keyword = reader.readLine()) != null) {
                // 获取到敏感词，并添加到前缀树
                this.addKeyword(keyword);
            }


        } catch(IOException e) {
            logger.error("加载敏感词列表文件失败:" + e.getMessage());
        }

    }

    // 将一个敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i = 0; i< keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNodes(c);

            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNodes(c, subNode);
            }

            // 指向子节点， 进入下一次循环
            tempNode = subNode;

            // 设置结束标识
            if(i == (keyword.length() -1 )){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param   text 待过滤的文档
     * @return  String 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1, 指向敏感词前缀树
        TrieNode tempNode = rootNode;
        // 指针2, 指向待过滤文本中当前开始比较的字符的位置
        int begin = 0;
        // 指针3, 指向待过滤文本中当前比较的字符的位置
        int position = 0;

        // 结果
        StringBuilder sb = new StringBuilder();

        while(position < text.length()) {
            char c = text.charAt(position);

            // 跳过某些特殊符号
            if(isSymbol(c)){
                // 若指针1为根节点, 将此符号计入结果，让指针2向下走一步
                if (tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间，指针3都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNodes(c);
            if(tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 前缀树重新指向根节点
                tempNode = rootNode;
            } else if(tempNode.isKeyWordEnd) {
                // 匹配到敏感词, 则从begin到position的位置都替换为替换符
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }
        // 将最后一批字符写入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断是否为特殊符号
    private boolean isSymbol(Character c)
    {
        // 0x2E80 - 0x9FFF 是东亚文字范围（中，韩， 日等）
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 敏感词前赘树节点
    private class TrieNode{
        // 关键词结束标识
        private boolean isKeyWordEnd = false;

        // 子节点（key是下级字符， value是下级节点）
        private Map<Character, TrieNode> subNodes= new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        // 添加子节点
        public void addSubNodes(Character c, TrieNode node){
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNodes(Character c) {
            return subNodes.get(c);
        }
    }

}
