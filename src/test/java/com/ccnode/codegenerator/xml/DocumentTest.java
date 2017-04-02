package com.ccnode.codegenerator.xml;

import org.junit.Test;

/**
 * @Author bruce.ge
 * @Date 2017/3/23
 * @Description
 */
public class DocumentTest {
    @Test
    public void test() {
        String manageString = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                "<mapper namespace=\"com.codehelper.domain.MyUserDao\">\n" +
                "    <!--auto generated Code-->\n" +
                "    <resultMap id=\"AllColumnMap\" type=\"com.codehelper.domain.MyUser\">\n" +
                "        <result column=\"id\" property=\"id\"/>\n" +
                "        <result column=\"cookie\" property=\"cookie\"/>\n" +
                "        <result column=\"type\" property=\"type\"/>\n" +
                "        <result column=\"user_name\" property=\"userName\"/>\n" +
                "        <result column=\"password\" property=\"password\"/>\n" +
                "        <result column=\"age\" property=\"age\"/>\n" +
                "        <result column=\"remaining_amount\" property=\"remainingAmount\"/>\n" +
                "        <result column=\"add_time\" property=\"addTime\"/>\n" +
                "        <result column=\"serial_id\" property=\"serialId\"/>\n" +
                "        <result column=\"global_id\" property=\"globalId\"/>\n" +
                "        <result column=\"nimei\" property=\"nimei\"/>\n" +
                "    </resultMap>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <sql id=\"all_column\">\n" +
                "        `id`,\n" +
                "        `cookie`,\n" +
                "        `type`,\n" +
                "        `user_name`,\n" +
                "        `password`,\n" +
                "        `age`,\n" +
                "        `remaining_amount`,\n" +
                "        `add_time`,\n" +
                "        `serial_id`,\n" +
                "        `global_id`,\n" +
                "        `nimei`\n" +
                "    </sql>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"pojo.id\">\n" +
                "        INSERT INTO my_user (\n" +
                "            `id`,\n" +
                "            `cookie`,\n" +
                "            `type`,\n" +
                "            `user_name`,\n" +
                "            `password`,\n" +
                "            `age`,\n" +
                "            `remaining_amount`,\n" +
                "            `add_time`,\n" +
                "            `serial_id`,\n" +
                "            `global_id`,\n" +
                "            `nimei`\n" +
                "        ) VALUES (\n" +
                "            #{pojo.id},\n" +
                "            #{pojo.cookie},\n" +
                "            #{pojo.type},\n" +
                "            #{pojo.userName},\n" +
                "            #{pojo.password},\n" +
                "            #{pojo.age},\n" +
                "            #{pojo.remainingAmount},\n" +
                "            #{pojo.addTime},\n" +
                "            #{pojo.serialId},\n" +
                "            #{pojo.globalId},\n" +
                "            #{pojo.nimei}\n" +
                "        )\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insertSelective\" useGeneratedKeys=\"true\" keyProperty=\"pojo.id\">\n" +
                "        INSERT INTO my_user\n" +
                "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
                "            <if test=\"pojo.id!=null\"> `id`,</if>\n" +
                "            <if test=\"pojo.cookie!=null\"> `cookie`,</if>\n" +
                "            <if test=\"pojo.type!=null\"> `type`,</if>\n" +
                "            <if test=\"pojo.userName!=null\"> `user_name`,</if>\n" +
                "            <if test=\"pojo.password!=null\"> `password`,</if>\n" +
                "            <if test=\"pojo.age!=null\"> `age`,</if>\n" +
                "            <if test=\"pojo.remainingAmount!=null\"> `remaining_amount`,</if>\n" +
                "            <if test=\"pojo.addTime!=null\"> `add_time`,</if>\n" +
                "            <if test=\"pojo.serialId!=null\"> `serial_id`,</if>\n" +
                "            <if test=\"pojo.globalId!=null\"> `global_id`,</if>\n" +
                "            <if test=\"pojo.nimei!=null\"> `nimei`</if>\n" +
                "        </trim>\n" +
                "        VALUES\n" +
                "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
                "            <if test=\"pojo.id!=null\"> #{pojo.id},</if>\n" +
                "            <if test=\"pojo.cookie!=null\"> #{pojo.cookie},</if>\n" +
                "            <if test=\"pojo.type!=null\"> #{pojo.type},</if>\n" +
                "            <if test=\"pojo.userName!=null\"> #{pojo.userName},</if>\n" +
                "            <if test=\"pojo.password!=null\"> #{pojo.password},</if>\n" +
                "            <if test=\"pojo.age!=null\"> #{pojo.age},</if>\n" +
                "            <if test=\"pojo.remainingAmount!=null\"> #{pojo.remainingAmount},</if>\n" +
                "            <if test=\"pojo.addTime!=null\"> #{pojo.addTime},</if>\n" +
                "            <if test=\"pojo.serialId!=null\"> #{pojo.serialId},</if>\n" +
                "            <if test=\"pojo.globalId!=null\"> #{pojo.globalId},</if>\n" +
                "            <if test=\"pojo.nimei!=null\"> #{pojo.nimei}</if>\n" +
                "        </trim>\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insertList\">        \n" +
                "        INSERT INTO my_user(\n" +
                "        <include refid=\"all_column\"/>\n" +
                "        )VALUES\n" +
                "        <foreach collection=\"pojos\" item=\"pojo\" index=\"index\" separator=\",\">\n" +
                "            (\n" +
                "            #{pojo.id},\n" +
                "            #{pojo.cookie},\n" +
                "            #{pojo.type},\n" +
                "            #{pojo.userName},\n" +
                "            #{pojo.password},\n" +
                "            #{pojo.age},\n" +
                "            #{pojo.remainingAmount},\n" +
                "            #{pojo.addTime},\n" +
                "            #{pojo.serialId},\n" +
                "            #{pojo.globalId},\n" +
                "            #{pojo.nimei}\n" +
                "            )\n" +
                "        </foreach>\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <update id=\"update\">";

        String fileToDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                "<mapper namespace=\"com.codehelper.domain.MyUserDao\">\n" +
                "    <!--auto generated Code-->\n" +
                "    <resultMap id=\"AllColumnMap\" type=\"com.codehelper.domain.MyUser\">\n" +
                "        <result column=\"id\" property=\"id\"/>\n" +
                "        <result column=\"cookie\" property=\"cookie\"/>\n" +
                "        <result column=\"type\" property=\"type\"/>\n" +
                "        <result column=\"user_name\" property=\"userName\"/>\n" +
                "        <result column=\"password\" property=\"password\"/>\n" +
                "        <result column=\"age\" property=\"age\"/>\n" +
                "        <result column=\"remaining_amount\" property=\"remainingAmount\"/>\n" +
                "        <result column=\"add_time\" property=\"addTime\"/>\n" +
                "        <result column=\"serial_id\" property=\"serialId\"/>\n" +
                "        <result column=\"global_id\" property=\"globalId\"/>\n" +
                "        <result column=\"nimei\" property=\"nimei\"/>\n" +
                "    </resultMap>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <sql id=\"all_column\">\n" +
                "        `id`,\n" +
                "        `cookie`,\n" +
                "        `type`,\n" +
                "        `user_name`,\n" +
                "        `password`,\n" +
                "        `age`,\n" +
                "        `remaining_amount`,\n" +
                "        `add_time`,\n" +
                "        `serial_id`,\n" +
                "        `global_id`,\n" +
                "        `nimei`\n" +
                "    </sql>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insert\" useGeneratedKeys=\"true\" keyProperty=\"pojo.id\">\n" +
                "        INSERT INTO my_user (\n" +
                "            `id`,\n" +
                "            `cookie`,\n" +
                "            `type`,\n" +
                "            `user_name`,\n" +
                "            `password`,\n" +
                "            `age`,\n" +
                "            `remaining_amount`,\n" +
                "            `add_time`,\n" +
                "            `serial_id`,\n" +
                "            `global_id`,\n" +
                "            `nimei`\n" +
                "        ) VALUES (\n" +
                "            #{pojo.id},\n" +
                "            #{pojo.cookie},\n" +
                "            #{pojo.type},\n" +
                "            #{pojo.userName},\n" +
                "            #{pojo.password},\n" +
                "            #{pojo.age},\n" +
                "            #{pojo.remainingAmount},\n" +
                "            #{pojo.addTime},\n" +
                "            #{pojo.serialId},\n" +
                "            #{pojo.globalId},\n" +
                "            #{pojo.nimei}\n" +
                "        )\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insertSelective\" useGeneratedKeys=\"true\" keyProperty=\"pojo.id\">\n" +
                "        INSERT INTO my_user\n" +
                "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
                "            <if test=\"pojo.id!=null\"> `id`,</if>\n" +
                "            <if test=\"pojo.cookie!=null\"> `cookie`,</if>\n" +
                "            <if test=\"pojo.type!=null\"> `type`,</if>\n" +
                "            <if test=\"pojo.userName!=null\"> `user_name`,</if>\n" +
                "            <if test=\"pojo.password!=null\"> `password`,</if>\n" +
                "            <if test=\"pojo.age!=null\"> `age`,</if>\n" +
                "            <if test=\"pojo.remainingAmount!=null\"> `remaining_amount`,</if>\n" +
                "            <if test=\"pojo.addTime!=null\"> `add_time`,</if>\n" +
                "            <if test=\"pojo.serialId!=null\"> `serial_id`,</if>\n" +
                "            <if test=\"pojo.globalId!=null\"> `global_id`,</if>\n" +
                "            <if test=\"pojo.nimei!=null\"> `nimei`</if>\n" +
                "        </trim>\n" +
                "        VALUES\n" +
                "        <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
                "            <if test=\"pojo.id!=null\"> #{pojo.id},</if>\n" +
                "            <if test=\"pojo.cookie!=null\"> #{pojo.cookie},</if>\n" +
                "            <if test=\"pojo.type!=null\"> #{pojo.type},</if>\n" +
                "            <if test=\"pojo.userName!=null\"> #{pojo.userName},</if>\n" +
                "            <if test=\"pojo.password!=null\"> #{pojo.password},</if>\n" +
                "            <if test=\"pojo.age!=null\"> #{pojo.age},</if>\n" +
                "            <if test=\"pojo.remainingAmount!=null\"> #{pojo.remainingAmount},</if>\n" +
                "            <if test=\"pojo.addTime!=null\"> #{pojo.addTime},</if>\n" +
                "            <if test=\"pojo.serialId!=null\"> #{pojo.serialId},</if>\n" +
                "            <if test=\"pojo.globalId!=null\"> #{pojo.globalId},</if>\n" +
                "            <if test=\"pojo.nimei!=null\"> #{pojo.nimei}</if>\n" +
                "        </trim>\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <insert id=\"insertList\">\n" +
                "        INSERT INTO my_user(\n" +
                "        <include refid=\"all_column\"/>\n" +
                "        )VALUES\n" +
                "        <foreach collection=\"pojos\" item=\"pojo\" index=\"index\" separator=\",\">\n" +
                "            (\n" +
                "            #{pojo.id},\n" +
                "            #{pojo.cookie},\n" +
                "            #{pojo.type},\n" +
                "            #{pojo.userName},\n" +
                "            #{pojo.password},\n" +
                "            #{pojo.age},\n" +
                "            #{pojo.remainingAmount},\n" +
                "            #{pojo.addTime},\n" +
                "            #{pojo.serialId},\n" +
                "            #{pojo.globalId},\n" +
                "            #{pojo.nimei}\n" +
                "            )\n" +
                "        </foreach>\n" +
                "\t</insert>\n" +
                "\n" +
                "    <!--auto generated Code-->\n" +
                "    <update id=\"update\">";

        String a = "";
        String b = "";
        for (int i = 0; i < manageString.length() && i < fileToDocument.length(); i++) {
            if (manageString.charAt(i) != fileToDocument.charAt(i)) {
                a += manageString.charAt(i);
                b += fileToDocument.charAt(i);
                System.out.println(i);
            }
        }
        System.out.println("a is:" + a);
        System.out.println("b is:" + b);
    }
}
