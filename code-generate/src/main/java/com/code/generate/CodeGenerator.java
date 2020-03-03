package com.code.generate;


import com.bbin.utils.project.MyCodeGenUtils;

public class CodeGenerator {
    public static void main(String[] args) {
        //此类所在的项目名称
        String sourceProject = "code-generate";
        //要生到的项目的名称
        String destProject = "income-robot-service";

        //游戏路径
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String projectPath = path.substring(0, path.indexOf("/" + sourceProject)) + "/" + destProject;
        //父package
        String parentName="com.income.robot";
        //模块名
        String modelName = "code";

        //配置数据库信息
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://mysql.com/pdd_robot?characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "0PKpK0JJD?mM";

        //生成表的名称
        String[] tables={
                "pdd_dict",
                "pdd_track",
                "pdd_track_order",
                "pdd_merge_rule",
                "pdd_konbbao_info"
        };
        MyCodeGenUtils.genCode(projectPath, driverClassName,url, username, password, modelName, parentName, tables);
    }
}