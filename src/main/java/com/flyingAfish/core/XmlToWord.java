package com.flyingAfish.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XmlToWord {
    private SAXReader saxReader;
    private Document document;
    private String msg;

    public XmlToWord() {
        saxReader = new SAXReader();
    }

    public String xmlToWord(File inFile,File outFile,String path) {
        try {
//            document = saxReader.read(new File("C:\\Users\\Administrator\\Desktop\\wordxml.xml"));
            document = saxReader.read(inFile);

            if (outFile == null) {
                outFile = new File(path+"\\youdaoWords.txt");
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        try (
//            BufferedWriter writer = new BufferedWriter(
//                new FileWriter(new File("C:\\Users\\Administrator\\Desktop\\youdaoWord.txt")))
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {

            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.elements("item");
            for (Element element : elements) {
                List<Element> words = element.elements("word");

                for (Element wordEle : words) {
                    String word = wordEle.getText();

                    if (word.charAt(0) > 'z' || word.charAt(0) < 'a') {
                        continue;
                    }
    //                System.out.println(wordEle.getText());

                    writer.write(word + "\t");
                }
                writer.flush();
            }

            msg = "你的xml文档成功转换成word单词！";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }
}
