package com.tp.xml;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.util.ExceptionUtil;
import netlog.tanping.com.i18nlib.MyI18NClass;
import netlog.tanping.com.i18nlib.util.DateUtils;

import java.io.File;
import java.util.Date;

public class InsertXML extends AnAction {
    private Project project;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        System.out.println("start ...");
        String filepath = System.getProperty("user.dir") ;
        filepath = project.getBasePath();
        System.out.println(filepath);

        MyI18NClass.rootRes = filepath +"\\app\\src\\main\\res";
        MyI18NClass.rootXLSX = filepath +"\\xml-data.xlsx";

        MyI18NClass.rootRes = MyI18NClass.rootRes.replace("\\","/");
        MyI18NClass.rootXLSX = MyI18NClass.rootXLSX.replace("\\","/");

        File res = new File(MyI18NClass.rootRes);
        File excel =  new File(MyI18NClass.rootXLSX);

        //目录存在
        if (res.exists() && excel.exists()){
            try {
                MyI18NClass.parse(DateUtils.format(new Date()));
                MessageDialogBuilder.yesNo("消息","已完成，保持项目 编码格式Utf-8 ，其他格式可能会出现乱码情况").show();
            } catch (Exception e) {
                e.printStackTrace();
                MessageDialogBuilder.yesNo("错误提醒", ExceptionUtil.getThrowableText(e)).show();
            }
        }else {
            remindDialog();
        }

    }

    public void remindDialog(){
        MessageDialogBuilder.yesNo("错误提醒","1、只支持 app module ；" +
                "\n2、Excel的文件必须在根目录并且名为 ：xml-data.xlsx" +
                "\n3、Excel 第一行为标记行，标记 name(key栏) 、 default(默认语言栏)、en/es/ar/fr...(其他语言简写栏)  ").show();
    }
}
