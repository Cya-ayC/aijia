package com.aijia.aijia;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class FastGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/aijia-pms?serverTimezone=GMT%2B8", "root", "1234")
                .globalConfig(builder -> {
                    builder.author("yongan") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.aijia.aijia") // 设置父包名
                            .entity("entity") // 实体类包名
                            .mapper("mapper") // Mapper 接口包名
                            .service("service") // Service 接口包名
                            .serviceImpl("service.impl") // Service 实现类包名
                            .controller("controller") // Controller 包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapper.xml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("bus_building","bus_unit","bus_room","bus_owner","bus_room_owner","bus_bill","bus_repair","bus_notice") // 设置需要生成的表名
                            .addTablePrefix("bus_") // 设置过滤表前缀，生成的类名就不会带 bus_
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .logicDeleteColumnName("is_deleted") // 逻辑删除字段名
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格 (@RestController)
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用Velocity模板引擎
                .execute();
    }
}
