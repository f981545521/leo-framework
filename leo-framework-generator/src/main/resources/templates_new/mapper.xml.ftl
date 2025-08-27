<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        t.${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
<#if baseColumnList>
    <!-- 通用别名查询结果列 -->
    <sql id="Alisa_Column_List">
<#list table.commonFields as field>
    ${field.columnName},
</#list>
        <#list table.fieldNames?split(", ") as field>t.${field}<#if field_has_next>, </#if></#list>
    </sql>

</#if>


    <insert id="save" <#list table.fields as field><#if field.keyFlag>useGeneratedKeys="true" keyProperty="${field.propertyName}"</#if></#list>>
        INSERT INTO ${table.name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list table.fields as field>
            <if test="${field.propertyName} != null"> ${field.name}, </if>
            </#list>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
            <#list table.fields as field>
            <if test="${field.propertyName} != null"> ${r"#"}{${field.propertyName}}, </if>
            </#list>
        </trim>
    </insert>

    <insert id="saveBatch" <#list table.fields as field><#if field.keyFlag>useGeneratedKeys="true" keyProperty="${field.propertyName}"</#if></#list>>
        INSERT INTO ${table.name}
        ( <#list table.fields as field>${field.name}<#if field_has_next>, </#if></#list> )
        VALUES
        <foreach collection="list" item="item" separator=",">
            ( <#list table.fields as field>${r"#"}{item.${field.propertyName}}<#if field_has_next>, </#if></#list> )
        </foreach>
    </insert>

    <!-- 根据主键删除 -->
    <delete id="removeById">
        DELETE FROM ${table.name} WHERE <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list> = ${r"#"}{id}
    </delete>

    <!-- 根据主键批量删除 -->
    <delete id="removeByIds">
        DELETE FROM ${table.name} WHERE  <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list> IN
        <foreach collection="list" item="id" open="(" separator="," close=")">
            ${r"#"}{id}
        </foreach>
    </delete>

    <!-- 根据主键更新 -->
    <update id="updateById">
        UPDATE ${table.name}
        <set>
    <#list table.fields as field>
        <#if !field.keyFlag>
            <if test="${field.name} != null"> ${field.name} = ${r"#"}{${field.propertyName}}, </if>
        </#if>
    </#list>
        </set>
        WHERE  <#list table.fields as field><#if field.keyFlag>${field.propertyName} = ${r"#"}{${field.name}}</#if></#list>
    </update>

    <!-- 根据主键批量更新 -->
    <update id="updateBatchById">
        <foreach collection="list" item="item" separator=";">
            UPDATE ${table.name}
            <set>
        <#list table.fields as field>
            <#if !field.keyFlag>
                <if test="${field.name} != null"> ${field.name} = ${r"#"}{${field.propertyName}}, </if>
            </#if>
        </#list>
            </set>
            WHERE <#list table.fields as field><#if field.keyFlag>${field.name} = ${r"#"}{${field.propertyName}}</#if></#list>
        </foreach>
    </update>

    <!-- 根据主键查询 -->
    <select id="getById" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list> = ${r"#"}{id}
    </select>

    <!-- 根据主键批量查询 -->
    <select id="listByIds" resultMap="BaseResultMap">
        SELECT <include refid="Base_Column_List"/>
        FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list>  IN
        <foreach collection="idList" item="id" open="(" separator="," close=")">
            ${r"#"}{id}
        </foreach>
    </select>

</mapper>
