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
package org.more.hypha.xml.support.beans;
import java.util.HashMap;
import java.util.Map;
import org.more.core.xml.XmlStackDecorator;
import org.more.hypha.define.beans.EL_ValueMetaData;
import org.more.hypha.xml.context.XmlDefineResource;
/**
 * 用于解析el标签
 * @version 2010-11-10
 * @author 赵永春 (zyc@byshell.org)
 */
public class TagBeans_EL extends TagBeans_AbstractValueMetaDataDefine<EL_ValueMetaData> {
    /**创建{@link TagBeans_EL}对象*/
    public TagBeans_EL(XmlDefineResource resource) {
        super(resource);
    }
    /**创建{@link EL_ValueMetaData}对象。*/
    protected EL_ValueMetaData createDefine(XmlStackDecorator context) {
        return new EL_ValueMetaData();
    }
    /**定义模板属性。*/
    public enum PropertyKey {
        elText
    }
    /**关联属性与xml的属性对应关系。*/
    protected Map<Enum<?>, String> getPropertyMappings() {
        HashMap<Enum<?>, String> propertys = new HashMap<Enum<?>, String>();
        propertys.put(PropertyKey.elText, "elText");
        return propertys;
    }
}