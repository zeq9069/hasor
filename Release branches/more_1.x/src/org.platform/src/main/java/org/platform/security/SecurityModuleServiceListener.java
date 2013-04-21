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
package org.platform.security;
import static org.platform.PlatformConfigEnum.Security_Enable;
import static org.platform.PlatformConfigEnum.Security_EnableMethod;
import java.lang.reflect.Method;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.more.global.Global;
import org.platform.Platform;
import org.platform.binder.ApiBinder;
import org.platform.context.AbstractModuleListener;
import org.platform.context.AppContext;
import org.platform.context.SettingListener;
import org.platform.security.Power.Level;
import com.google.inject.matcher.AbstractMatcher;
/**
 * ֧��Service��ע�⹦�ܡ�
 * @version : 2013-4-8
 * @author ������ (zyc@byshell.org)
 */
//@InitListener(displayName = "SecurityModuleServiceListener", description = "org.platform.security����������֧�֡�", startIndex = 1)
public class SecurityModuleServiceListener extends AbstractModuleListener {
    private boolean                 enable       = false; //���ý���
    private boolean                 enableMethod = true; //����Ȩ�޼��
    private SecurityContext         secService   = null;
    private SecuritySessionListener secListener  = null;
    /**��ʼ��.*/
    @Override
    public void initialize(ApiBinder event) {
        /*HttpSession����������֪ͨ����*/
        this.secListener = new SecuritySessionListener();
        event.sessionListener().bind(this.secListener);
        /*request������������*/
        event.filter("*").through(SecurityFilter.class);
        /*aop������ִ��Ȩ��֧��*/
        event.getGuiceBinder().bindInterceptor(new ClassPowerMatcher(), new MethodPowerMatcher(), new SecurityInterceptor());/*ע��Aop*/
        /*�����ļ�������*/
        SettingListener listener = new SettingListener() {
            @Override
            public void reLoadConfig(Global oldConfig, Global newConfig) {
                enable = newConfig.getBoolean(Security_Enable, false);
                enableMethod = newConfig.getBoolean(Security_EnableMethod, false);
                if (enable == false)
                    enableMethod = false;
            }
        };
        event.getInitContext().getConfig().addSettingsListener(listener);
        listener.reLoadConfig(null, event.getInitContext().getConfig().getSettings());
        /*�󶨺��Ĺ���ʵ���ࡣ*/
        event.getGuiceBinder().bind(SecurityContext.class).to(DefaultSecurityService.class);
        event.getGuiceBinder().bind(SecurityQuery.class).to(DefaultSecurityQuery.class);
    }
    @Override
    public void initialized(AppContext appContext) {
        this.secService = appContext.getBean(SecurityContext.class);
    }
    /*-------------------------------------------------------------------------------------*/
    /*���������Ƿ�ƥ�䡣����ֻҪ���ͻ򷽷��ϱ����@Power��*/
    private class ClassPowerMatcher extends AbstractMatcher<Class<?>> {
        @Override
        public boolean matches(Class<?> matcherType) {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (enableMethod == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            Method[] m1s = matcherType.getMethods();
            Method[] m2s = matcherType.getDeclaredMethods();
            for (Method m1 : m1s) {
                if (m1.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            for (Method m2 : m2s) {
                if (m2.isAnnotationPresent(Power.class) == true)
                    return true;
            }
            return false;
        }
    }
    /*�����ⷽ���Ƿ�ƥ�䡣���򣺷����򷽷��������ϱ����@Power��*/
    private class MethodPowerMatcher extends AbstractMatcher<Method> {
        @Override
        public boolean matches(Method matcherType) {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (enableMethod == false)
                return false;
            /*----------------------------*/
            if (matcherType.isAnnotationPresent(Power.class) == true)
                return true;
            if (matcherType.getDeclaringClass().isAnnotationPresent(Power.class) == true)
                return true;
            return false;
        }
    }
    /*������*/
    private class SecurityInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            /*������ڽ���״̬�����Ȩ�޼��*/
            if (enableMethod == false)
                return invocation.proceed();
            /*----------------------------*/
            //1.��ȡȨ������
            Power powerAnno = invocation.getMethod().getAnnotation(Power.class);
            if (powerAnno == null)
                powerAnno = invocation.getMethod().getDeclaringClass().getAnnotation(Power.class);
            //2.����Ȩ��
            boolean passPower = true;
            if (Level.PassLogin == powerAnno.level()) {
                passPower = this.doPassLogin(powerAnno, invocation.getMethod());
            } else if (Level.PassPolicy == powerAnno.level()) {
                passPower = this.doPassPolicy(powerAnno, invocation.getMethod());
            } else if (Level.Free == powerAnno.level()) {
                passPower = true;
            }
            //3.ִ�д���
            if (passPower)
                return invocation.proceed();
            String msg = "has no permission Level=" + powerAnno.level().name() + " Code : " + Platform.logString(powerAnno.value());
            throw new PermissionException(msg);
        }
        private boolean doPassLogin(Power powerAnno, Method method) {
            AuthSession authSession = secService.getCurrentAuthSession();
            return authSession.isLogin();
        }
        private boolean doPassPolicy(Power powerAnno, Method method) {
            AuthSession authSession = secService.getCurrentAuthSession();
            String[] powers = powerAnno.value();
            SecurityQuery query = secService.newSecurityQuery();
            for (String anno : powers)
                query.and(anno);
            return query.testPermission(authSession);
        }
    }
    /*HttpSession��̬����*/
    private class SecuritySessionListener implements HttpSessionListener {
        @Override
        public void sessionCreated(HttpSessionEvent se) {
            if (enable == false)
                return;
            secService.getAuthSession(se.getSession(), true);
        }
        @Override
        public void sessionDestroyed(HttpSessionEvent se) {
            if (enable == false)
                return;
            AuthSession authSession = secService.getAuthSession(se.getSession(), true);
            authSession.close();
        }
    }
}