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
package org.more.submit;
/**
 * 该接口定义了Submit默认支持的变量作用域。
 * <br/>Date : 2009-12-28
 * @author 赵永春
 */
public interface ScopeEnum {
    /**ActionStack范围*/
    public static final String Scope_Stack   = "Stack";
    /**Session范围*/
    public static final String Scope_Session = "Session";
    /**SubmitContext范围*/
    public static final String Scope_Context = "Context";
}