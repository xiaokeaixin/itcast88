package com.itheima.web.coverters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串转换日期的转换器
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
public class StringToDateConverter implements Converter<String,Date> {

    private String pattern;

    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    @Override
    public Date convert(String source) {
        try {
            if(StringUtils.isEmpty(source)){
                throw new NullPointerException("日期不能为空");
            }
            if(StringUtils.isEmpty(pattern)){
                pattern = "yyyy-MM-dd";//默认值
            }
            DateFormat format = new SimpleDateFormat(pattern);
            return format.parse(source);
        }catch (Exception e){
            throw new IllegalArgumentException("日期的格式不对，请输入正确的日期格式");
        }
    }
}