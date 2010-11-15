/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.more.hypha.beans.support;
import java.util.Date;
import java.util.Map;
import org.more.LostException;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.DefineResource;
import org.more.hypha.beans.define.VariableBeanDefine;
import org.more.util.StringConvert;
/**
 * ���ڽ���/beans/varBean��ǩ
 * @version 2010-9-16
 * @author ������ (zyc@byshell.org)
 */
public class TagBeans_VarBean extends TagBeans_AbstractBeanDefine<VariableBeanDefine> {
    /**����{@link TagBeans_VarBean}����*/
    public TagBeans_VarBean(DefineResource configuration) {
        super(configuration);
    }
    /**����ö�ٻ�ȡ���������Class��*/
    protected Class<?> getBaseType(VariableType typeEnum) {
        if (typeEnum == null)
            return null;
        else if (typeEnum == VariableType.Boolean)
            return boolean.class;
        else if (typeEnum == VariableType.Byte)
            return byte.class;
        else if (typeEnum == VariableType.Short)
            return short.class;
        else if (typeEnum == VariableType.Int)
            return int.class;
        else if (typeEnum == VariableType.Long)
            return long.class;
        else if (typeEnum == VariableType.Float)
            return float.class;
        else if (typeEnum == VariableType.Double)
            return double.class;
        else if (typeEnum == VariableType.Char)
            return char.class;
        else if (typeEnum == VariableType.String)
            return String.class;
        else if (typeEnum == VariableType.Date)
            return Date.class;
        else
            return null;
    }
    /**����VariableBeanDefine���Ͷ���*/
    protected VariableBeanDefine createDefine() {
        return new VariableBeanDefine();
    }
    /**����ֵBean������*/
    public enum PropertyKey {
        value, type, format
    };
    /**����������xml�����Զ�Ӧ��ϵ��*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        return null;
    }
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        super.beginElement(context, xpath, event);
        //1.ȡ������
        String _value = event.getAttributeValue("value");
        String _type = event.getAttributeValue("type");
        String _format = event.getAttributeValue("format");
        //2.ת��type
        VariableType typeEnum = (VariableType) StringConvert.changeType(_type, VariableType.class, VariableType.Null);
        Class<?> classType = this.getBaseType(typeEnum);
        if (classType == null && _type != null)
            try {
                ClassLoader loader = this.getDefineResource().getClassLoader();
                classType = loader.loadClass(_type);
            } catch (Exception e) {
                throw new LostException("[" + _type + "]�������Զ�ʧ��", e);
            }
        //3.ȡ��ֵ
        Object value = null;
        if (typeEnum == VariableType.Date)
            value = StringConvert.parseDate(_value, _format);
        else
            value = StringConvert.changeType(_value, classType);
        //4.����ֵ
        VariableBeanDefine define = this.getDefine(context);
        define.setType(classType);
        define.setValue(value);
    }
}