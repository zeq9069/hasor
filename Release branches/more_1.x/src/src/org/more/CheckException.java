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
package org.more;
/**
 * 检查异常，当执行检查期间发生错误时会引发该异常。
 * @version 2010-9-23
 * @author 赵永春 (zyc@byshell.org)
 */
public class CheckException extends RuntimeException {
    /**  */
    private static final long serialVersionUID = -4958048464275640076L;
    /**
    * 检查异常，当执行检查期间发生错误时会引发该异常。
     * @param string 异常的描述信息
     */
    public CheckException(String string) {
        super(string);
    }
    /**
    * 检查异常，当执行检查期间发生错误时会引发该异常。
     * @param error 异常的描述信息
     */
    public CheckException(Throwable error) {
        super(error);
    }
    /**
    * 检查异常，当执行检查期间发生错误时会引发该异常。
     * @param string 异常的描述信息。
     * @param error 承接的上一个异常对象。
     */
    public CheckException(String string, Throwable error) {
        super(string, error);
    }
}