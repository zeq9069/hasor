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
package org.test.more.core.util;
import java.util.Date;
import org.junit.Test;
import org.more.util.StringConvertUtils;
public class StringConvertTest {
    @Test
    public void toBoolean() {
        System.out.println(StringConvertUtils.parseBoolean("true"));
        System.out.println(StringConvertUtils.parseBoolean("false"));
        System.out.println(StringConvertUtils.parseBoolean("0"));
        System.out.println(StringConvertUtils.parseBoolean("1"));
    };
    @Test
    public void toByte() {
        System.out.println(StringConvertUtils.parseByte("123", (byte) 56));
        System.out.println(StringConvertUtils.parseByte("321", (byte) 56));
    };
    @Test
    public void toShort() {
        System.out.println(StringConvertUtils.parseShort("123", (short) 56));
        System.out.println(StringConvertUtils.parseShort("32100000", (short) 56));
    };
    @Test
    public void toInteger() {
        System.out.println(StringConvertUtils.parseInt("123", (int) 56));
        System.out.println(StringConvertUtils.parseInt("321000000000000000000000", (int) 56));
    };
    @Test
    public void toLong() {
        System.out.println(StringConvertUtils.parseLong("123", (long) 56));
        System.out.println(StringConvertUtils.parseLong("321000000000000000000000", (long) 56));
    };
    @Test
    public void toFloat() {
        System.out.println(StringConvertUtils.parseFloat("123.23", (float) 56));
        System.out.println(StringConvertUtils.parseFloat("321000000000000000.000", (float) 56.6));
    };
    @Test
    public void toDouble() {
        System.out.println(StringConvertUtils.parseDouble("123.23", (double) 56));
        System.out.println(StringConvertUtils.parseDouble("3210000000000000000000000000000000000000000.000", (double) 56.6));
    };
    @Test
    public void toDate() {
        System.out.println(StringConvertUtils.parseDate("2007/05/05", "yyyy/MM/dd"));
    };
    @Test
    public void toEnum() {
        System.out.println(StringConvertUtils.parseEnum("call", TestEnum.class));
    };
    @Test
    public void toArray() {
        System.out.println(StringConvertUtils.parseArray("a;b;c;3;4;5", ";"));
    };
    @Test
    public void toList() {
        System.out.println(StringConvertUtils.parseList("a;b;c;3;4;5", ";"));
    };
    @Test
    public void changeType() {
        System.out.println(StringConvertUtils.changeType("yes", boolean.class));
        System.out.println(StringConvertUtils.changeType("23", byte.class));
        System.out.println(StringConvertUtils.changeType("321", short.class));
        System.out.println(StringConvertUtils.changeType("123", int.class));
        System.out.println(StringConvertUtils.changeType("1221", long.class));
        System.out.println(StringConvertUtils.changeType("12.5", float.class));
        System.out.println(StringConvertUtils.changeType("56.23", double.class));
        System.out.println(StringConvertUtils.changeType("2007/05/05", Date.class));
        System.out.println(StringConvertUtils.changeType("take", TestEnum.class));
    };
}
enum TestEnum {
    take, call
}