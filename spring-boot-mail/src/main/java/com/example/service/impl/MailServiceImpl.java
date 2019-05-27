package com.example.service.impl;

import com.example.config.MailConfigProperty;
import com.example.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件发送接口 - 实现
 *
 * @author 码农猿
 */
@Service
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private MailConfigProperty mailConfigProperty;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送普通文本邮件
     *
     * @param to      收件人
     * @param subject 邮件主题(标题)
     * @param content 内容
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        logger.info("[邮件发送] >> 普通邮件发送开始 to : {} , subject : {} ", to, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        //发信人
        message.setFrom(mailConfigProperty.getUsername());
        //收信人
        message.setTo(to);
        //邮件主题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        try {
            mailSender.send(message);
            logger.info("[邮件发送] >> 普通邮件发送成功 >> to : {} , subject : {}", to, subject);
        } catch (MailAuthenticationException e) {
            logger.info("[邮件发送] >> 发件人账户认证异常 >> 请检查发件人账户配置 to : {} , subject : {} , stack ：{}", to, subject, e);
        } catch (Exception e) {
            logger.info("[邮件发送] >> 普通邮件未知异常 >> to : {} , subject : {} , stack ：{}", to, subject, e);
        }

    }

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 邮件主题(标题)
     * @param content 内容（可以包含<html>标签）
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        logger.info("[邮件发送] >> HTML邮件发送开始 to : {} , subject : {} ", to, subject);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailConfigProperty.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            //true代表支持html
            helper.setText(content, true);
            mailSender.send(message);
            logger.info("[邮件发送] >> HTML邮件发送成功 to : {} , subject : {}", to, subject);
        } catch (MailAuthenticationException e) {
            logger.info("[邮件发送] >> 发件人账户认证异常 >> 请检查发件人账户配置 to : {} , subject : {} , stack ：{}", to, subject, e);
        } catch (Exception e) {
            logger.info("[邮件发送] >> HTML邮件未知异常 >> to : {} , subject : {} , stack ：{}", to, subject, e);
        }
    }

    /**
     * 发送带附件的邮件 - 单个附件
     *
     * @param to             收件人
     * @param subject        邮件主题(标题)
     * @param content        内容
     * @param attachmentPath 附件路径
     */
    @Override
    public void sendAttachmentMail(String to, String subject, String content, String attachmentPath) {
        List<String> attachmentPathList = new ArrayList<>(1);
        attachmentPathList.add(attachmentPath);
        sendAttachmentMail(to, subject, content, attachmentPathList);
    }

    /**
     * 发送带附件的邮件 - 支持多个
     *
     * @param to                 收件人
     * @param subject            邮件主题(标题)
     * @param content            内容
     * @param attachmentPathList 附件路径集
     */
    @Override
    public void sendAttachmentMail(String to, String subject, String content, List<String> attachmentPathList) {
        logger.info("[邮件发送] >> 带附件邮件发送开始 to : {} , subject : {} , attachmentPath : {}", to, subject, attachmentPathList);
        if (CollectionUtils.isEmpty(attachmentPathList)) {
            logger.info("[邮件发送] >> 带附件邮件发送结束 >> 附件信息为空 >> to : {} , subject : {}", to, subject);
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailConfigProperty.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            for (String attachmentPath : attachmentPathList) {
                FileSystemResource attachmentFile = new FileSystemResource(new File(attachmentPath));
                //添加多个附件
                helper.addAttachment(attachmentFile.getFilename(), attachmentFile);
            }
            mailSender.send(message);
            logger.info("[邮件发送] >> 带附件邮件发送成功 >> to : {} , subject : {}", to, subject);
        } catch (MailAuthenticationException e) {
            logger.info("[邮件发送] >> 发件人账户认证异常 >> 请检查发件人账户配置 to : {} , subject : {} , stack ：{}", to, subject, e);
        } catch (Exception e) {
            logger.info("[邮件发送] >> 带附件邮件未知异常 >> to : {} , subject : {} , stack ：{}", to, subject, e);
        }
    }


    /**
     * 发送带图片的邮件
     *
     * @param to          收件人
     * @param subject     邮件主题(标题)
     * @param content     文本
     * @param picturePath 图片路径
     * @param pictureId   图片ID，用于在<img>标签中使用，从而显示图片
     */
    @Override
    public void sendPictureMail(String to, String subject, String content, String picturePath, String pictureId) {
        logger.info("[邮件发送] >> 带图片邮件发送开始 to : {} , subject : {} , picturePath : {}", to, subject, picturePath);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailConfigProperty.getUsername());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            FileSystemResource pictureFile = new FileSystemResource(new File(picturePath));
            //支持追加多个
            helper.addInline(pictureId, pictureFile);
            mailSender.send(message);
            logger.info("[邮件发送] >> 带图片邮件发送成功 >> to : {} , subject : {} , picturePath : {}", to, subject, picturePath);
        } catch (MailAuthenticationException e) {
            logger.info("[邮件发送] >> 发件人账户认证异常 >> 请检查发件人账户配置 to : {} , subject : {} , picturePath : {} , stack ：{}", to, subject, picturePath, e);
        } catch (Exception e) {
            logger.info("[邮件发送] >> 带图片邮件未知异常 >> to : {} , subject : {} , picturePath : {}  , stack ：{}", to, subject, picturePath, e);
        }
    }

}
