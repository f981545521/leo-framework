package cn.acyou.leo.tool.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;

/**
 * @author youfang
 * @version [1.0.0, 2023/5/25 11:09]
 **/
@Data
@JacksonXmlRootElement(localName = "xml")
public class WxMpMsgResp implements Serializable {

    private static final long serialVersionUID = -853048174238279746L;
    @JacksonXmlProperty(localName = "ToUserName")
    private String ToUserName;
    @JacksonXmlProperty(localName = "FromUserName")
    private String FromUserName;
    @JacksonXmlProperty(localName = "CreateTime")
    private String CreateTime;
    @JacksonXmlProperty(localName = "MsgType")
    private String MsgType;
    @JacksonXmlProperty(localName = "Content")
    private String Content;
}
