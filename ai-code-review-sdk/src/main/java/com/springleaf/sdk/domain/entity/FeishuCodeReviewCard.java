package com.springleaf.sdk.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 飞书卡片通知实体类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeishuCodeReviewCard {

    @JsonProperty("msg_type")
    private final String msgType = "interactive";

    private final Card card;

    public FeishuCodeReviewCard(String projectName, String author, String message, String repoUrl) {
        this.card = new Card(projectName, author, message, repoUrl);
    }

    @Data
    private static class Card {
        private final String schema = "2.0";
        private final Config config = new Config();
        private final Header header = new Header();
        private final Body body;

        public Card(String projectName, String author, String message,  String repoUrl) {
            this.body = new Body(projectName, author, message, repoUrl);
        }
    }

    @Data
    private static class Config {
        @JsonProperty("update_multi")
        private final Boolean updateMulti = true;
        private final Style style = new Style();
    }

    @Data
    private static class Style {
        @JsonProperty("text_size")
        private final TextSize textSize = new TextSize();
    }

    @Data
    private static class TextSize {
        @JsonProperty("normal_v2")
        private final NormalV2 normalV2 = new NormalV2();
    }

    @Data
    private static class NormalV2 {
        private final String pc = "normal";
        private final String mobile = "heading";
        @JsonProperty("default")
        private final String defaults = "normal";
    }

    @Data
    private static class Header {
        private final Title title = new Title("✅ 代码评审完成");
        private final String template = "blue";
        private final String padding = "12px 12px 12px 12px";
    }

    @Data
    private static class Title {
        private final String tag = "plain_text";
        private final String content;
    }

    @Data
    private static class Body {
        private final String direction = "vertical";
        private final String padding = "12px 12px 12px 12px";
        private final List<Element> elements;

        public Body(String projectName, String author, String message, String repoUrl) {
            this.elements = Arrays.asList(
                    createMarkdownElement(projectName, author, message, repoUrl),
                    createButtonElement(repoUrl)
            );
        }

        private Element createMarkdownElement(String projectName, String author, String message, String repoUrl) {
            Element element = new Element();
            element.setTag("markdown");
            element.setContent(String.format(
                    "**项目名称**: %s\n\n" +
                    "**作者**: %s\n\n" +
                    "**提交信息**: %s\n\n" +  // 新增行
                    "**仓库地址**: [%s](%s)",  // 调整格式
                    projectName, author, message, repoUrl, repoUrl
            ));
            element.setTextAlign("left");
            element.setTextSize("normal_v2");
            return element;
        }

        private Element createButtonElement(String repoUrl) {
            Element element = new Element();
            element.setTag("button");
            element.setText(new ButtonText("📖 查看仓库"));
            element.setType("default");
            element.setWidth("default");
            element.setSize("medium");
            element.setMargin("10px 0px 0px 0px");
            element.setBehaviors(Collections.singletonList(
                    new Behavior("open_url", repoUrl)
            ));
            return element;
        }
    }

    @Data
    private static class Element {
        private String tag;
        private String content;
        @JsonProperty("text_align")
        private String textAlign;
        @JsonProperty("text_size")
        private String textSize;
        private ButtonText text;
        private String type;
        private String width;
        private String size;
        private List<Behavior> behaviors;
        private String margin;
    }

    @Data
    private static class ButtonText {
        private final String tag = "plain_text";
        private final String content;
    }

    @Data
    private static class Behavior {
        private final String type;
        @JsonProperty("default_url")
        private final String defaultUrl;
    }
}
