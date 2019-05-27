package com.example.service;

import java.util.List;

/**
 * 邮件发送接口
 *
 * @author 码农猿
 */
public interface MailService {

    /**
     * 发送普通文本邮件
     *
     * @param to      收件人
     * @param subject 邮件主题(标题)
     * @param content 内容
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 邮件主题(标题)
     * @param content 内容（可以包含<html>标签）
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送带附件的邮件 - 单个附件
     *
     * @param to             收件人
     * @param subject        邮件主题(标题)
     * @param content        内容
     * @param attachmentPath 附件路径
     */
    void sendAttachmentMail(String to, String subject, String content, String attachmentPath);


    /**
     * 发送带附件的邮件 - 支持多个
     *
     * @param to                 收件人
     * @param subject            邮件主题(标题)
     * @param content            内容
     * @param attachmentPathList 附件路径集
     */
    void sendAttachmentMail(String to, String subject, String content, List<String> attachmentPathList);


    /**
     * 发送带图片的邮件
     *
     * @param to          收件人
     * @param subject     邮件主题(标题)
     * @param content     文本
     * @param picturePath 图片路径
     * @param pictureId   图片ID，用于在<img>标签中使用，从而显示图片
     */
    void sendPictureMail(String to, String subject, String content, String picturePath, String pictureId);

}
