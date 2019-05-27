package com.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 邮件发送测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainServiceTest {

    @Autowired
    private MailService mailService;
    @Autowired
    private TemplateEngine templateEngine;

    private static final String SEND_TO_EMAIL = "your66666@163.com";


    /**
     * 发送普通文本邮件
     */
    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail(SEND_TO_EMAIL, "普通邮件测试", "hello ，这是【普通邮件】发送测试");
    }

    /**
     * 发送HTML邮件
     */
    @Test
    public void sendHtmlMail() {
        String content = "<html><body><h2><font color=\"red\">" + "hello ，这是【HTML邮件】我我标题" + "</font></h2>我是内容。。。</body></html>";
        mailService.sendHtmlMail(SEND_TO_EMAIL, "HTML邮件发送测试", content);
    }

    /**
     * 发送带附件的邮件
     */
    @Test
    public void sendAttachmentMail() {
        String content = "<html><body><h3><font color=\"red\">" + "hello ，这是【附件邮件】发送测试，附件已到请注意查收" + "</font></h3></body></html>";
        //附件地址
        String attachmentPath = "/Users/Temp/001.jpg";
        mailService.sendAttachmentMail(SEND_TO_EMAIL, "附件邮件发送测试", content, attachmentPath);
    }

    /**
     * 发送带附件的邮件 -多个
     */
    @Test
    public void sendAttachmentListMail() {
        String content = "<html><body><h3><font color=\"red\">" + "hello ，这是【附件邮件】发送测试，附件已到请注意查收" + "</font></h3></body></html>";
        //附件地址
        List<String> attachmentPathList = new ArrayList<>();
        attachmentPathList.add("/Users/Temp/001.jpg");
        attachmentPathList.add("/Users/Temp/002.jpg");
        mailService.sendAttachmentMail(SEND_TO_EMAIL, "附件多个邮件发送测试", content, attachmentPathList);
    }

    /**
     * 发送带图片的邮件
     */
    @Test
    public void sendInlineResourceMail() {
        //图片地址
        String picturePath = "/Users/Temp/001.jpg";
        //图片自定义id
        String pictureId = "1001";
        //html内容
        String content = "<html><body><h3><font color=\"red\">" + "大家好，这是springboot发送的HTML邮件，有图片哦" + "</font></h3>"
                + "<img src=\'cid:" + pictureId + "\'></body></html>";
        mailService.sendPictureMail(SEND_TO_EMAIL, "图片邮件发送测试", content, picturePath, pictureId);
    }

    /**
     * 指定HTML模板邮件
     */
    @Test
    public void testTemplateMail() {
        //向Thymeleaf模板传值
        Context context = new Context();
        context.setVariable("username", "张三");
        context.setVariable("id", "666666");

        //模板文件名称
        String templateName = "email_template";
        //解析模板生成html字符串
        String emailContent = templateEngine.process(templateName, context);

        mailService.sendHtmlMail(SEND_TO_EMAIL, "模板邮件发送测试", emailContent);
    }
}
