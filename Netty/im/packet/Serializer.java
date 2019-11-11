package cn.edu.jxau.im.packet;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    Integer getSerializerAlgorithm();

    byte[] serialize(Object obj);

    <T> T deserialize(Class<T> klass, byte[] bytes);
}
