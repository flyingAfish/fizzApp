package com.flyingAfish.core;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class TransferChar {
    private String msg;

    public String transferChar(File inFile,File outFile,String path) {

        if (outFile == null) {
            outFile = new File(path + "\\newWord.txt");
        }

        try (
//                BufferedReader reader = new BufferedReader(new FileReader(new File(
//                        "C:\\Users\\Administrator\\Desktop\\代码打字练习词库.txt")));
//                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
//                        "C:\\Users\\Administrator\\Desktop\\newFileChar.txt")))
                BufferedReader reader = new BufferedReader(new FileReader(inFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))
        ) {

            String ch;
            StringBuffer wordBuffer = new StringBuffer();
            while ((ch = reader.readLine()) != null) {
                wordBuffer.append(ch);
//            writer.write(ch);
            }
            String stringWord = wordBuffer.toString();
            String[] words = stringWord.split("\\s+");//匹配一个或者多个空格

            ArrayList<String> wordList = new ArrayList<>();
            for (String word : words) {
                wordList.add(word);
            }

            ArrayList<String> newWords = new ArrayList<>();
            Random random = new Random();

            do {
                int index = random.nextInt(wordList.size() - 1);
                newWords.add(wordList.get(index));
                wordList.remove(index);
            } while (wordList.size() > 1);
//            String[] words = new String[]

            for (String newWord : newWords) {
                writer.write(newWord + "\t");
                System.out.println(newWord);
            }

            msg = "你的txt文档单词已乱序成功！";

        } catch (FileNotFoundException e) {
            System.out.println("没有找到文件！！");
        } catch (IOException e) {
            System.out.println("io写入异常！！");
        }

        return msg;
    }
}
