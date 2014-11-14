/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package net.hasor.rsf.protocol.message;
import net.hasor.rsf.general.ProtocolStatus;
import net.hasor.rsf.protocol.toos.ProtocolUtils;
import net.hasor.rsf.serialize.Decoder;
import net.hasor.rsf.serialize.Encoder;
import net.hasor.rsf.serialize.SerializeFactory;
import org.more.UndefinedException;
/**
 * RSF 1.0-Response 协议数据.
 * @version : 2014年10月25日
 * @author 赵永春(zyc@hasor.net)
 */
public class ResponseMsg extends BaseMsg {
    private short  status        = ProtocolStatus.Unknown.shortValue();
    private String serializeType = "";
    private String returnType    = "";
    private byte[] returnData    = null;
    //
    /**设置协议版本。*/
    public void setVersion(byte version) {
        super.setVersion(ProtocolUtils.finalVersionForResponse(version));
    }
    /**获取响应状态*/
    public short getStatus() {
        return this.status;
    }
    /**设置响应状态*/
    public void setStatus(short status) {
        this.status = status;
    }
    /**设置响应状态*/
    public void setStatus(ProtocolStatus status) {
        this.status = status.shortValue();
    }
    /**获取序列化类型*/
    public String getSerializeType() {
        return this.serializeType;
    }
    /**设置序列化类型*/
    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }
    /**获取返回值类型*/
    public String getReturnType() {
        return this.returnType;
    }
    /**设置返回值类型*/
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    /**设置要返回的值*/
    public void setReturnData(byte[] rawReturnData) {
        this.returnData = rawReturnData;
    }
    /**获取要返回的值*/
    public byte[] getReturnData() {
        return this.returnData;
    }
    /**获取要返回的值*/
    public Object getReturnData(SerializeFactory serializeFactory) throws Throwable {
        String codeName = this.getSerializeType();
        Decoder decoder = serializeFactory.getDecoder(codeName);
        //
        if (decoder == null && this.returnData != null)
            throw new UndefinedException("Undefined ‘" + codeName + "’ serialize decoder ");
        //
        return decoder.decode(this.returnData);
    }
    /**设置要返回的值*/
    public void setReturnData(Object data, SerializeFactory serializeFactory) throws Throwable {
        String codeName = this.getSerializeType();
        Encoder encoder = serializeFactory.getEncoder(codeName);
        //
        this.returnData = encoder.encode(data);
    }
}