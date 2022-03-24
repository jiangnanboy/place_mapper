
### 简介

一个用于提取市、县或区的级联地名及编码的java模块。

举个例子，输入市、县或区的名称，会直接映射出其对应的父级地名。

    1.江阴市 ->   province -> 江苏省 -> 320000000000
                 city -> 无锡市 -> 320200000000
                 county -> 江阴市 -> 320281000000
      
    2.邯郸经济技术开发区 ->   province -> 河北省 -> 130000000000
                            city -> 邯郸市 -> 130400000000
                            county -> 邯郸经济技术开发区 -> 130471000000
    
    3.阿克塞哈萨克族自治县 ->   province -> 甘肃省 -> 620000000000
                              city -> 酒泉市 -> 620900000000
                              county -> 阿克塞哈萨克族自治县 -> 620924000000



### 数据
1.中国行政区划分数据：resources/new_adcodes.txt

2.所有的地名数据（用于最大化匹配）：resources/user_dict.txt

### 使用
见sy/Example

```
Map<String, String> placeMap = PlaceMapper.check("阿克塞哈萨克族自治县");
```
输出的结果为“省市县/区”(后面的编码为官方地址编码)：

       province -> 甘肃省 -> 620000000000
       city -> 酒泉市 -> 620900000000
       county -> 阿克塞哈萨克族自治县 -> 620924000000

### 参考
https://github.com/DQinYuan/chinese_province_city_area_mapper
