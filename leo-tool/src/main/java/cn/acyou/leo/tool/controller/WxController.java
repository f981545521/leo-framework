package cn.acyou.leo.tool.controller;

import cn.acyou.leo.framework.util.SHAUtil;
import cn.acyou.leo.framework.util.XMLUtil;
import cn.acyou.leo.tool.dto.WxMpMessage;
import cn.acyou.leo.tool.dto.WxMpMsgResp;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author youfang
 * @version [1.0.0, 2023/5/25 10:27]
 **/
@Slf4j
@Api(tags = "微信接口")
@ApiSort(100)
@Controller
@RequestMapping("/wx")
public class WxController {

    private String token = "Guiji";

    @ApiOperation("微信服务器验证")
    @GetMapping("verifyServer")
    public void verifyServer(@RequestParam String signature,
                             @RequestParam String timestamp,
                             @RequestParam String nonce,
                             @RequestParam String echostr, HttpServletResponse response) {
        log.info("微信服务器验证 signature:{},token:{},timestamp:{},nonce:{}", signature, token, timestamp, nonce);
        String tmpStr = SHAUtil.getSHA1(token, timestamp, nonce);
        log.info("随机字符串echostr:{}", echostr);
        log.info("tmpStr:{}", tmpStr);
        if (tmpStr.equals(signature.toUpperCase())) {
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
                writer.write(echostr);
                writer.flush();
            } catch (IOException e) {
                log.error("微信服务器验证出错了", e);
            }
        }
    }

    @ApiOperation(value = "微信服务器 接收微信消息", notes = "" +
            "接收普通消息: https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Receiving_standard_messages.html")
    @PostMapping(value = "verifyServer", consumes = {MediaType.TEXT_XML_VALUE}, produces = MediaType.TEXT_XML_VALUE)
    public void verifyServerMessage(@RequestBody(required = false) WxMpMessage wxMpMsgXml, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("接收微信消息:{}", wxMpMsgXml);
        String content = wxMpMsgXml.getContent();
        String openid = wxMpMsgXml.getFromUserName();

        String res = "success";

        if ("我是老板".equals(content)) {
            WxMpMsgResp wxMpMsgResp = new WxMpMsgResp();
            wxMpMsgResp.setFromUserName(wxMpMsgXml.getToUserName());
            wxMpMsgResp.setToUserName(wxMpMsgXml.getFromUserName());
            wxMpMsgResp.setCreateTime(new Date().getTime() + "");
            wxMpMsgResp.setMsgType("text");
            wxMpMsgResp.setContent("我将为您服务！");
            res = XMLUtil.beanToXmlStr(wxMpMsgResp);
        }


        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(res);
            writer.flush();
        } catch (IOException e) {
            log.error("微信服务器验证出错了", e);
        }
    }

    @ApiOperation("微信服务器 接收微信消息2")
    @PostMapping(value = "verifyServer2", consumes = {MediaType.TEXT_XML_VALUE}, produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public Object verifyServerMessage2(@RequestBody(required = false) WxMpMessage wxMpMsgXml, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("接收微信消息:{}", wxMpMsgXml);
        String content = wxMpMsgXml.getContent();
        String openid = wxMpMsgXml.getFromUserName();

        String res = "success";

        if ("我是老板".equals(content)) {
            response.setCharacterEncoding("UTF-8");
            WxMpMsgResp wxMpMsgResp = new WxMpMsgResp();
            wxMpMsgResp.setFromUserName(wxMpMsgXml.getToUserName());
            wxMpMsgResp.setToUserName(wxMpMsgXml.getFromUserName());
            wxMpMsgResp.setCreateTime(new Date().getTime() + "");
            wxMpMsgResp.setMsgType("text");
            wxMpMsgResp.setContent("我将为您服务！");
            return wxMpMsgResp;
        }

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(res);
            writer.flush();
        } catch (IOException e) {
            log.error("微信服务器验证出错了", e);
        }
        return null;
    }

    @ApiOperation("微信服务器 接收微信消息3（非内容协商 建议使用）")
    @PostMapping(value = "verifyServer3", consumes = {MediaType.TEXT_XML_VALUE}, produces = MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public Object verifyServerMessage3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String wxMpMsgXmlStr = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        log.info("接收微信消息:{}", wxMpMsgXmlStr);
        WxMpMessage wxMpMsgXml = XMLUtil.xmlToBean(wxMpMsgXmlStr, WxMpMessage.class);
        String content = wxMpMsgXml.getContent();
        String openid = wxMpMsgXml.getFromUserName();

        String res = "success";
        if ("我是老板".equals(content)) {
            response.setCharacterEncoding("UTF-8");
            WxMpMsgResp wxMpMsgResp = new WxMpMsgResp();
            wxMpMsgResp.setFromUserName(wxMpMsgXml.getToUserName());
            wxMpMsgResp.setToUserName(wxMpMsgXml.getFromUserName());
            wxMpMsgResp.setCreateTime(new Date().getTime() + "");
            wxMpMsgResp.setMsgType("text");
            wxMpMsgResp.setContent("我将为您服务！");
            res = XMLUtil.beanToXmlStr(wxMpMsgResp);
        }
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(res);
            writer.flush();
        } catch (IOException e) {
            log.error("微信服务器验证出错了", e);
        }
        return null;
    }

}

