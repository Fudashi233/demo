package cn.edu.jxau.controler.core;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.edu.jxau.util.ConfigManager;
import cn.edu.jxau.util.IOUtils;
import cn.edu.jxau.util.LogUtils;

public class Model {

    private Map<String, Object> paramMap;

    public Model(HttpServletRequest request) throws FileUploadException, IOException {

        if (ServletFileUpload.isMultipartContent(request)) { // 文件上传
            paramMap = upload(request);
        } else {
            paramMap = new HashMap<>();
            paramMap.putAll(request.getParameterMap());
        }

    }

    /**
     * 当请求涉及到文件上传时，使用{@code upload()}方法实例化Model,
     * 这个方法会将普通的表单域以name-value的形式放如{@code paramMap}中，
     * 每个文件域的文件本身、文件名、contentType类型三种数据也会放入Model中
     * @param paramMap
     * @throws FileUploadException 
     * @throws IOException 
     */
    private Map<String, Object> upload(HttpServletRequest request) throws FileUploadException, IOException {

        // 参数校验 //
        Objects.requireNonNull(request, "参数request不能为null");
        Map<String, Object> paramMap = new HashMap<>();

        // 文件上传的一些列准备工作 //
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(Integer.parseInt(ConfigManager.getConfig("sizeThreshold"))); // 设置缓存大小
        factory.setRepository(ConfigManager.tmpDir()); // 设置临时路径
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        fileUpload.setHeaderEncoding(ConfigManager.getConfig("encoding"));

        // 构造相应的数据放入paramMap中 //
        try {
            Iterator<FileItem> iterator = fileUpload.parseRequest(request).iterator();
            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                if (fileItem.isFormField()) { // 普通表单域
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString(ConfigManager.getConfig("encoding"));
                    List<String> list = (List<String>) paramMap.get(name);
                    addList(paramMap,name,value);
                } else { // 文件表单域
                    // 将文件放入临时目录 //
                    String fileName = fileItem.getName(); // 获取文件名
                    if (fileName == null || fileName.trim().equals("")) {
                        continue;
                    }
                    fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
                    String newFileName = getUUIDName(fileName); // 获取唯一的文件名
                    File uploadFile = new File(ConfigManager.tmpDir(), newFileName);
                    // 将文件数据从FileItem的流中读取到指定位置 //
                    try (InputStream in = fileItem.getInputStream();
                            FileOutputStream out = new FileOutputStream(uploadFile)) {
                        IOUtils.copyStream(in, out);
                    } catch (IOException e) {
                        throw new IOException(String.format("文件(%s)复制失败", fileName), e);
                    } finally {
                        fileItem.delete();
                    }
                    String fieldName = fileItem.getFieldName();
                    addList(paramMap,fieldName,uploadFile); //上传的文件
                    addList(paramMap,fieldName+"FileName",fileName);
                    addList(paramMap,fieldName+"ContentType",fileItem.getContentType());
                }
            }
        } catch (FileUploadException e) {
            throw new FileUploadException("文件上传失败", e);
        } catch (IOException e) {
            throw new IOException("文件上传失败", e);
        }

        // list to array,将所有类型是List的值转为数组//
        Iterator<Entry<String, Object>> iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            List<Object> list = (List<Object>) entry.getValue();
            paramMap.put(key, list.toArray());
        }
        return paramMap;
    }

    private void addList(Map<String, Object> map, String name, Object value) {

        List<Object> list = (List<Object>) map.get(name);
        if (list == null) {
            list = new ArrayList<>();
            list.add(value);
            map.put(name, list);
        } else {
            list.add(value);
        }
    }

    private String getUUIDName(String fileName) {
        return String.format("%s_%s", UUID.randomUUID().toString(), fileName);
    }

    public void put(String key, Object value) {
        paramMap.put(key, value);
    }

    public String[] getValues(String key) {

        Objects.requireNonNull(key, "参数key不能为null");
        Object value = paramMap.get(key);
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) { // 判断是否是数组
            Object[] arr = (Object[]) value;
            String[] result = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                result[i] = arr[i].toString();
            }
            return result;
        } else {
            String[] result = new String[1];
            result[0] = value.toString();
            return result;
        }
    }

    public String get(String key) {

        Objects.requireNonNull(key, "参数key不能为null");
        Object value = paramMap.get(key);
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            Object[] arr = (Object[]) value;
            return arr[0].toString();
        } else {
            return value.toString();
        }
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(get(key));
    }

    public char getChar(String key) {
        return (char) getInt(key);
    }

    public byte getByte(String key) {
        return Byte.valueOf(get(key));
    }

    public short getShort(String key) {
        return Short.valueOf(get(key));
    }

    public int getInt(String key) {
        return Integer.valueOf(get(key));
    }

    public long getLong(String key) {
        return Long.valueOf(get(key));
    }

    public float getFloat(String key) {
        return Float.valueOf(get(key));
    }

    public double getDouble(String key) {
        return Double.valueOf(get(key));
    }

    public Date getDate(String key) throws ParseException {
        return getDate(key, new SimpleDateFormat(ConfigManager.getConfig("dateFormat")));
    }

    public Date getDate(String key, DateFormat format) throws ParseException {

        String date = get(key);
        return format.parse(date);
    }

    public <T> T getBean(Class<T> clazz) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, IntrospectionException, ParseException {

        Objects.requireNonNull(clazz, "参数clazz不能为null");
        String simpleName = clazz.getSimpleName();
        StringBuilder prefixBuilder = new StringBuilder();
        prefixBuilder.append(Character.toLowerCase(simpleName.charAt(0))); // 第一个字母小写
        prefixBuilder.append(simpleName.substring(1));
        return getBean(clazz, prefixBuilder.toString());
    }

    public <T> T getBean(Class<T> clazz, String prefix) throws InstantiationException, IllegalAccessException,
            IntrospectionException, IllegalArgumentException, InvocationTargetException, ParseException {

        Objects.requireNonNull(clazz, "参数clazz不能为null");
        Objects.requireNonNull(prefix, "参数prefix不能为null");
        T bean = clazz.newInstance();
        PropertyDescriptor[] PDArr = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        for (PropertyDescriptor PD : PDArr) {
            Method method = PD.getWriteMethod();
            if (method != null) {
                Class<?> fieldType = PD.getPropertyType();
                String fieldName = PD.getName();
                String key = null;
                if ("".equals(prefix.trim())) {
                    key = fieldName;
                } else {
                    key = String.format("%s.%s", prefix, fieldName);
                }
                if (get(key) == null) { // 键值对不存在
                    continue;
                }
                if (fieldType == Boolean.class || fieldType == boolean.class) {
                    method.invoke(bean, getBoolean(key));
                } else if (fieldType == Character.class || fieldType == char.class) {
                    method.invoke(bean, getChar(key));
                } else if (fieldType == Byte.class || fieldType == byte.class) {
                    method.invoke(bean, getByte(key));
                } else if (fieldType == Short.class || fieldType == short.class) {
                    method.invoke(bean, getShort(key));
                } else if (fieldType == Integer.class || fieldType == int.class) {
                    method.invoke(bean, getInt(key));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    method.invoke(bean, getLong(key));
                } else if (fieldType == Float.class || fieldType == float.class) {
                    method.invoke(bean, getFloat(key));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    method.invoke(bean, getDouble(key));
                } else if (fieldType == Date.class) {
                    method.invoke(bean, getDate(key));
                } else if (fieldType == String[].class) {
                    method.invoke(bean, (Object) getValues(key));
                } else if (fieldType == String.class) {
                    method.invoke(bean, get(key));
                } else {
                    LogUtils.info(String.format("%s获取失败", fieldName));
                }
            }
        }
        return bean;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Object getObj(String key) {
        return paramMap.get(key);
    }
}
