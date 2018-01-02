package cn.jzteam.core.dom4j;

import cn.jzteam.test.Person;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlHandler {

    public static void main(String[] args) throws Exception {
        createTestXml();
    }


    private List<Person> parsePersonXml() throws DocumentException {

        SAXReader reader = new SAXReader();
        Document document = reader.read("src/person.xml");

        Element root = document.getRootElement();

        System.out.println("document:" + document.getText());
        List<Person> list = new ArrayList<>();

        List<Element> elements = root.elements("Person");
        for (Element ele : elements) {
            String name = ele.element("name").getText().trim();
            String age = ele.elementTextTrim("age");
            Person person = new Person();
            person.setName(name);
            System.out.println("name:" + name + ",  age:" + age);
            person.setAge(Integer.parseInt(age));
            list.add(person);
        }
        return list;
    }

    private static void createTestXml() throws DocumentException, IOException {

        Document document = DocumentHelper.createDocument();

        Element rootElement = document.addElement("root");
        rootElement.attributeValue("value","test");
        //  添加属性
        rootElement.addAttribute("selected","false");

        Element dependencies = rootElement.addElement("dependencies");
        Element plugins = rootElement.addElement("plugins");

        Element dependency = dependencies.addElement("dependency");
        dependency.setText("cn.jzteam.test");
        dependency.setData("what is it");

        Element plugin = plugins.addElement("plugin");
        plugin.setText("maven.plugin");
        // 修改属性值
//        plugin.attributeValue("version","1.0");


        //dom4j提供了专门写入文件的对象XMLWriter
        XMLWriter xmlWriter = new XMLWriter(new FileWriter("src/test.xml"));
        xmlWriter.write(document);
        xmlWriter.flush();
        xmlWriter.close();
        System.out.println("xml文档添加成功！");

    }
}
