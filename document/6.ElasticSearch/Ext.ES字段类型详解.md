---
title: ES Mapping、字段类型Field type详解
date: 2020-09-13 20:38:59
tags: ELK
---
## ES Mapping、字段类型Field type详解

字段类型概述

- 核心类型	字符串类型	string,text,keyword
- 整数类型	integer,long,short,byte
- 浮点类型	double,float,half_float,scaled_float
- 逻辑类型	boolean
- 日期类型	date
- 范围类型	range
- 二进制类型	binary
- 复合类型	数组类型	array
- 对象类型	object
- 嵌套类型	nested
- 地理类型	地理坐标类型	geo_point
- 地理地图	geo_shape
- 特殊类型	IP类型	ip
- 范围类型	completion
- 令牌计数类型	token_count
- 附件类型	attachment
- 抽取类型	percolato

### 1.1string类型

ELasticsearch 5.X之后的字段类型不再支持string，由text或keyword取代。 如果仍使用string，会给出警告。

### 1.2 text类型

text取代了string，当一个字段是要被全文搜索的，比如Email内容、产品描述，应该使用text类型。设置text类型以后，字段内容会被分析，在生成倒排索引以前，字符串会被分析器分成一个一个词项。text类型的字段不用于排序，很少用于聚合（termsAggregation除外）。

### 1.3 keyword类型
keyword类型适用于索引结构化的字段，比如email地址、主机名、状态码和标签。如果字段需要进行过滤(比如查找已发布博客中status属性为published的文章)、排序、聚合。keyword类型的字段只能通过精确值搜索到。


## 参考文档
- [ES Mapping、字段类型Field type详解](https://blog.csdn.net/ZYC88888/article/details/83059040)





