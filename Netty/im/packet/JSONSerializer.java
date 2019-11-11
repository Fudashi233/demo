package cn.edu.jxau.im.packet;

import com.alibaba.fastjson.JSON;

/**
 * Desc:
 * ------------------------------------
 * Author:fulei04@meituan.com
 * Date:2019/11/3
 * Time:下午3:44
 */
public class JSONSerializer implements Serializer {

    @Override
    public Integer getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON_SERIALIZER;
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(Class<T> klass, byte[] bytes) {
        return JSON.parseObject(bytes,klass);
    }
}
