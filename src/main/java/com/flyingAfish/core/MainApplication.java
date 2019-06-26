package com.flyingAfish.core;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.security.util.Password;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FlowPane flowPane = new FlowPane();

        //设置控件
        Text userName = new Text("用户名：");
        Text password = new Text("密码：");
        TextField textField = new TextField();
        TextField textField2 = new PasswordField();
        TextArea textArea = new TextArea("第一次选取'.xml'格式的待转换单词文件，" +
                "第二次选取保存好的单词文件（.txt）\r\n存放位置并命名文件名。" +
                "\r\n\t保存位置不选默在源文件同目录下生成youdaoWords.txt文件并覆盖同名文件。");
        TextArea textArea2 = new TextArea("第一次选取需要乱序的txt文件，第二次选取转换好的单词文件（.txt）" +
                "\r\n的存放位置并命名。\r\n\t注意：不能与源文件同名！" +
                "\r\n\t保存位置不选默在源文件同目录下生成newWord.txt文件并覆盖同名文件。");

        //选择文件
        Button button = new Button("选择文件");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = direChoose(primaryStage);
            }
        });


        //按钮
        Button wordBtn = new Button("word乱序");
        tansferCharHandle(wordBtn, primaryStage);

        Button xmlBtn = new Button("xml提取单词");
        XmlToWordHandle(xmlBtn, primaryStage);


        //添加 控件 到流失布局
        flowPane.setPadding(new Insets(10, 10, 10, 10));
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.getChildren().addAll(userName,textField,password,textField2, xmlBtn,  textArea, wordBtn,textArea2,button);

        //设置场景参数，并添加流到场景
        Scene scene = new Scene(flowPane, 600, 600);

        //设置舞台标题，添加场景到舞台，显示舞台
        primaryStage.setTitle("fizz");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * 将xml格式的单词提取出来
     *
     * @param xmlBtn
     */
    private void XmlToWordHandle(Button xmlBtn, Window mainWin) {
        xmlBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File inFile = fileChoose(mainWin);
                if (inFile == null) return;
                if (!isGiveFileExt(inFile, "xml")) return;
                String path = inFile.getParent();

                File outFile = fileChoose(mainWin);
                if (outFile != null)
                    if (!isGiveFileExt(outFile, "txt")) return;

                XmlToWord xmlToWord = new XmlToWord();
                String msg = xmlToWord.xmlToWord(inFile, outFile, path);

                popUpWindow(msg);
            }
        });
    }

    /**
     * 单词乱序处理函数
     *
     * @param button
     */
    private void tansferCharHandle(Button button, Window mainWin) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File inFile = fileChoose(mainWin);
                if (inFile == null) return;
                if (!isGiveFileExt(inFile, "txt")) return;
                String path = inFile.getParent();

                File outFile = fileChoose(mainWin);
                if (outFile != null)
                    if (!isGiveFileExt(outFile, "txt")) return;

                TransferChar transferChar = new TransferChar();
                String msg = transferChar.transferChar(inFile, outFile, path);

                popUpWindow(msg);
            }
        });
    }

    /**
     * 选择目录
     *
     * @param primaryStage
     * @return 返回选择的目录
     */
    private File direChoose(Window primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("文件选择");
        directoryChooser.setInitialDirectory(new File(
                "C:\\Users\\Administrator\\Desktop"));
        return directoryChooser.showDialog(primaryStage);
    }

    /**
     * 选择文件
     *
     * @param mainWin
     * @return 返回选择的目录
     */
    private File fileChoose(Window mainWin) {
        FileChooser fileChooser = new FileChooser();
        //设置默认文件位置
        fileChooser.setInitialDirectory(new File("C:\\Users\\Administrator\\Desktop"));
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Text Files", "*.xml"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(mainWin);

        if (selectedFile != null) {
            String absolutePath = selectedFile.getAbsolutePath();
            System.out.println("内部类里面" + absolutePath);
        }
        return selectedFile;
    }

    /**
     * @param selectFile 选择的文件
     * @param extName    给定的文件扩展名（如：text，xml...）
     * @return 如果是给定的扩展名则返回true，否则返回false
     */
    private boolean isGiveFileExt(File selectFile, String extName) {
        String FileName = selectFile.getName();
        if (FileName.length() <= extName.length() + 1) {
            String msg = "选择的文件有误，请选择 xx." + extName + " 格式文件！";
            System.out.println(msg);
            popUpWindow(msg);
            return false;
        }
        String isTrueFile = FileName.substring(FileName.length() - ("." + extName).length());
        System.out.println(isTrueFile);
        if (!("." + extName).equals(isTrueFile)) {
            String msg = "选择的文件有误，请选择 xx." + extName + " 格式文件！";
            System.out.println(msg);
            popUpWindow(msg);
            return false;
        }

        return true;
    }

    /**
     * 弹出提示窗口
     * @param msg 展示的提示信息
     */
    private void popUpWindow(String msg) {
        Stage stage = new Stage();
        Text msgText = new Text(msg);
        BorderPane pane = new BorderPane();
        pane.setCenter(msgText);
        Scene scene = new Scene(pane,400,100);

        stage.setTitle("提示");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

}
