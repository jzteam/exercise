package cn.jzteam.core.xml;

import com.alibaba.fastjson.JSON;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SaxXmlTest extends DefaultHandler {
    public static void main(String[] args) {
        try {
            // 获得工厂实例
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 获取解析器实例
            SAXParser parser = factory.newSAXParser();
            // 解析器解析文件，使用定制处理器
            parser.parse("src/person.xml", new SaxXmlTest());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("SAX解析开始");
    }
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("SAX解析结束");
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //调用DefaultHandler类的startElement方法
        super.startElement(uri, localName, qName, attributes);
        // 碰到一个开始标签<qName attr="11">
        System.out.println("startElement: uri="+uri+",localName="+localName+",qName="+qName+",attributes="+ JSON.toJSONString(attributes));
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //调用DefaultHandler类的endElement方法
        super.endElement(uri, localName, qName);
        // 碰到一个结束标签</qName>
        System.out.println("endElement: uri="+uri+",localName="+localName+",qName="+qName);
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        // 根标签开始，相连的两个标签之间的内容，包括换行
        System.out.println("characters: "+new String(ch,start,length));
    }
}
