package org.csuc.Parser.Core.strategy;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author amartinez
 */
public class XPATH {

    private String xpath;
    private String name;
    private int count;

    public XPATH(){}

    public XPATH(String xpath, String name, int count){
        this.xpath = xpath;
        this.name = name;
        this.count = count;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        for(java.lang.reflect.Field f : getClass().getDeclaredFields()){
            f.setAccessible(true);
            try {
                Object value = f.get(this);
                builder.append(f.getName(), value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
