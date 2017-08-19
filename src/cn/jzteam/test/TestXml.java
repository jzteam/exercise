package cn.jzteam.test;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.jzteam.utils.JsonUtil;


public class TestXml {

    public static void main(String[] args) throws DocumentException {
        TestXml testXml = new TestXml();
        List<Person> persons = testXml.parsePersonXml();
        System.out.println(JsonUtil.toJson(persons));
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

}
