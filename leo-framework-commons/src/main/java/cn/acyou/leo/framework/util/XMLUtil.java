package cn.acyou.leo.framework.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * @author youfang
 * @version [1.0.0, 2023/5/25 16:01]
 **/
@Slf4j
public class XMLUtil {
    public static XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
    }

    /**
     * 将xml转为bean对象
     *
     * @param input XML字符串
     * @return Bean对象
     * @throws IOException 异常
     */
    public static <T> T xmlToBean(String input, Class<T> cls) throws IOException {
        return xmlMapper.readValue(input, cls);
    }

    /**
     * 将bean转为xml字符串，bean需要配置注解@JacksonXmlProperty等。
     *
     * @param input Bean对象
     * @return XML字符串
     */
    public static String beanToXmlStr(Object input) throws IOException {
        return xmlMapper.writeValueAsString(input);
    }

    /**
     * 将bean的xml字符串转为map，bean需要配置注解@JacksonXmlProperty等。
     *
     * @param input 输入
     * @return {@link Map}<{@link String}, {@link Object}>
     * @throws IOException 异常
     */
    public static Map<String, Object> beanToXmlStrToMap(Object input) throws IOException {
        String xmlStr = xmlMapper.writeValueAsString(input);
        return xmlMapper.readValue(xmlStr, new TypeReference<Map<String, Object>>() {
        });
    }


    /**
     * 读取文件成XML格式字符串
     *
     * @param fileName 文件
     * @return {@link String}
     */
    public static String xmlFileToString(String fileName) {
        try {
            //新建一个解析类
            SAXReader saxReader = new SAXReader();
            //读入一个文件
            org.dom4j.Document tempDocument = saxReader.read(fileName);
            return tempDocument.asXML();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 字符串输出到XML文件
     *
     * @param str      字符串
     * @param fileName 文件
     */
    public static void strToXmlFile(String str, File fileName) throws IOException {
        SAXReader saxReader = new SAXReader();
        org.dom4j.Document document;
        XMLWriter writer = null;
        try {
            document = saxReader.read(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)));
            OutputFormat format = OutputFormat.createPrettyPrint();
            /* 将document中的内容写入文件中 */
            writer = new XMLWriter(new FileWriter(fileName), format);
            writer.write(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }
}
