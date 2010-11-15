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
package org.more.hypha.aop.support;
import org.more.RepeateException;
import org.more.core.xml.XmlElementHook;
import org.more.core.xml.XmlStackDecorator;
import org.more.core.xml.stream.EndElementEvent;
import org.more.core.xml.stream.StartElementEvent;
import org.more.hypha.DefineResource;
import org.more.hypha.aop.AopDefineResourcePlugin;
import org.more.hypha.aop.define.AbstractPointcutDefine;
import org.more.hypha.aop.define.AopConfigDefine;
import org.more.hypha.aop.define.AopPointcutGroupDefine;
import org.more.hypha.configuration.Tag_Abstract;
/**
 * ���ڽ����е��ǩ�Ļ��࣬��������name���ԡ�
 * @version 2010-9-24
 * @author ������ (zyc@byshell.org)
 */
@SuppressWarnings("unchecked")
public abstract class TagAop_AbstractPointcut<T extends AbstractPointcutDefine> extends Tag_Abstract implements XmlElementHook {
    private static final String PointcutDefine = "$more_Aop_PointcutDefine";
    /**����{@link TagAop_AbstractPointcut}���Ͷ���*/
    public TagAop_AbstractPointcut(DefineResource configuration) {
        super(configuration);
    }
    /**����һ��{@link AbstractPointcutDefine}�������*/
    protected abstract T createDefine();
    /**��ȡ������{@link AbstractPointcutDefine}�������*/
    protected final T getDefine(XmlStackDecorator context) {
        return (T) context.getAttribute(PointcutDefine);
    };
    /**��ʼ������ǩ*/
    public void beginElement(XmlStackDecorator context, String xpath, StartElementEvent event) {
        context.createStack();
        T define = this.createDefine();
        String name = event.getAttributeValue("name");
        if (name != null)
            define.setName(name);// or this.putAttribute(define, "name", name);
        context.setAttribute(PointcutDefine, define);
    }
    /**����������ǩ*/
    public void endElement(XmlStackDecorator context, String xpath, EndElementEvent event) {
        T define = this.getDefine(context);
        boolean isReg = false;
        //1.Pointcut������Group����
        T parentDefine = (T) context.getParentStack().getAttribute(PointcutDefine);
        if (parentDefine != null) {
            if (parentDefine instanceof AopPointcutGroupDefine) {
                ((AopPointcutGroupDefine) parentDefine).addPointcutDefine(define);
                isReg = true;
            }
        }
        //2.Pointcut������Config���� 
        AopConfigDefine parentConfig = (AopConfigDefine) context.getAttribute(TagAop_Config.ConfigDefine);
        if (isReg == false && parentConfig != null) {
            if (parentConfig != null) {
                if (parentConfig.getDefaultPointcutDefine() != null)
                    throw new RepeateException("���ܶ�AopConfigDefine���͵�[" + parentConfig.getName() + "]���еڶ��ζ���aop�е㡣");
                parentConfig.setDefaultPointcutDefine(define);
                isReg = true;
            }
        }
        //3.ע�ᵽ������
        if (isReg == false && define.getName() != null) {
            AopDefineResourcePlugin plugin = (AopDefineResourcePlugin) this.getDefineResource().getPlugin(AopDefineResourcePlugin.AopDefineResourcePluginName);
            if (plugin.containPointcutDefine(define.getName()) == true)
                throw new RepeateException("�����ظ�����[" + define.getName() + "]��������");
            plugin.addPointcutDefine(define);
        }
        //
        context.removeAttribute(PointcutDefine);
        context.dropStack();
    }
}