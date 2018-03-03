package analyze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.regex.Pattern;

/**
 * 解析字节码文件,获取类的源码
 * @author 付大石
 */
public class AnalyzeClass {
    
    private Class<?> clazz;
    private final static String NEW_LINE = System.getProperty("line.separator");
    private final static Pattern PATTERN = Pattern.compile("\\w+\\.");
    public AnalyzeClass(Class<?> clazz) {
        if(clazz.isEnum()) {
            throw new IllegalArgumentException("暂不支持枚举类型");
        }
        this.clazz = clazz;
    }

    public File getSrc(String path) {
        
        File file = new File(path);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            
            // analyze package info //
            String packageInfo = analyzePackage();
            if(packageInfo!=null && !"".equals(packageInfo.trim())) {
                writer.write(packageInfo);
                writer.newLine();
                writer.newLine();
            }
            
            // ananlyze class info //
            String classInfo = analyzeClass();
            writer.write(classInfo);
            writer.write(" {");
            writer.newLine();
            writer.newLine();
            
            // analuze field info //
            String fieldsInfo = analyzeFields();
            writer.write(fieldsInfo);
            writer.write(NEW_LINE);
            
            // analyze constructor info //
            String constructorsInfo = analyzeConstructors();
            writer.write(constructorsInfo);
            writer.write(NEW_LINE);
            
            // analyze method info //
            String methodsInfo = analyzeMethods();
            writer.write(methodsInfo);
            writer.write(NEW_LINE);
            
            writer.write("}");
        } catch(IOException ex) {
            throw new RuntimeException("文件写入失败",ex);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    //=========================================
    // 获取相关包信息
    //=========================================
    private String analyzePackage() {
        
        StringBuilder packageInfo = new StringBuilder();
        Package packagee = clazz.getPackage();
        
        // 注解 //
        //{@code Package}虽然有{@code getAnnotations()}方法，
        //但是这个注解是存在于package-info.java文件中的，不应该在此输出
        
        // 包名 //
        String packageName = packagee.getName();
        if(packageName!=null && !"".equals(packageName.trim())) {
            packageInfo.append("package").append(" ").append(packageName).append(";");
        }
        return packageInfo.toString();
    }
    
    //=========================================
    // 获取类相关信息（类名、类修饰符、实现的接口）
    //=========================================
    private String analyzeClass() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder classInfo = new StringBuilder();
        
        // 注解 //
        Annotation[] annotationArr = clazz.getDeclaredAnnotations();
        for(Annotation annotation : annotationArr) {
           classInfo.append(getAnnotationInfo(annotation)).append(NEW_LINE);
        }
        
        // 修饰符 //
        classInfo.append(Modifier.toString(clazz.getModifiers())).append(" ");
        // 类名 //
        classInfo.append("class").append(" ")
            .append(clazz.getSimpleName());
        
        // 获取类型变量 //
        classInfo.append(getTypeVariableInfo(clazz.getTypeParameters()));
        
        // 父类 //
        Class<?> superClass = clazz.getSuperclass();
        if(superClass!=null || superClass!=Object.class) {
            classInfo.append(" ").append("extends").append(" ").append(superClass.getSimpleName()); //父类类名
            classInfo.append(getTypeVariableInfo(superClass.getTypeParameters())); //父类的类型变量
        }
        
        // 接口 //
        Type[] interfaces = clazz.getGenericInterfaces();
        if(interfaces.length!=0) {
             classInfo.append(" ").append("implements").append(" ");
             for(Type item : interfaces) {
                 if(item instanceof Class) { //普通接口
                     classInfo.append(((Class<?>)item).getSimpleName());
                 } else if(item instanceof ParameterizedType) { //泛型接口
                     ParameterizedType paramType = (ParameterizedType)item;
                     classInfo.append(((Class<?>)(paramType.getRawType())).getSimpleName());
                     Type[] actualTypeArguments = paramType.getActualTypeArguments();
                     classInfo.append("<");
                     for(Type type : actualTypeArguments) {
                         if(type instanceof Class) {
                             classInfo.append(((Class<?>) type).getSimpleName());
                         } else if(type instanceof GenericArrayType) {
                             throw new UnsupportedOperationException("暂不支持");
                         } else if(type instanceof ParameterizedType) {
                             throw new UnsupportedOperationException("暂不支持");
                         } else if(type instanceof TypeVariable) {
                             classInfo.append(((TypeVariable<?>) type).getName());
                         } else if(type instanceof WildcardType) {
                             throw new UnsupportedOperationException("暂不支持");
                         }
                         classInfo.append(",");
                     }
                     classInfo.deleteCharAt(classInfo.length()-1);
                     classInfo.append(">");
                 }
               classInfo.append(",");
             }
             classInfo.deleteCharAt(classInfo.length()-1);
        }
        return classInfo.toString();
    }
    
    private String getAnnotationInfo(Annotation annotation) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder annotationInfo = new StringBuilder();
        Class<?> annotationType = annotation.annotationType();
        annotationInfo.append("@").append(annotationType.getSimpleName());
        Method[] methodArr = annotationType.getDeclaredMethods();
        if(methodArr.length!=0) {
            annotationInfo.append("(");
            for(Method method : methodArr) {
                annotationInfo.append(method.getName()).append("=");
                if(method.getReturnType()==String.class) {
                    annotationInfo.append("\"\""); //插入双引号
                    annotationInfo.insert(annotationInfo.length()-1,method.invoke(annotation));
                } else {
                    annotationInfo.append(method.invoke(annotation));
                }
                annotationInfo.append(" ");
            }
            annotationInfo.deleteCharAt(annotationInfo.length()-1);
            annotationInfo.append(")");
        }
        return annotationInfo.toString();
    }
    
    private String getTypeVariableInfo(TypeVariable<?>[] typeVariableArr) {
        
        StringBuilder typeVariableInfo = new StringBuilder();
        if(typeVariableArr.length!=0) {
            typeVariableInfo.append("<");
            for(TypeVariable<?> typeVariable : typeVariableArr) {
                typeVariableInfo.append(typeVariable.getName());
                Type[] typeArr = typeVariable.getBounds();
                for(Type type : typeArr) {
                    if(type!=Object.class) {
                        typeVariableInfo.append(" ").append("extends "+((Class<?>) type).getSimpleName());
                    }
                }
                typeVariableInfo.append(",");
            }
            typeVariableInfo.deleteCharAt(typeVariableInfo.length()-1);
            typeVariableInfo.append(">");
        }
        return typeVariableInfo.toString();
    }
    
    //=========================================
    // 解析数据域
    //=========================================
    private String analyzeFields() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder fieldsInfo = new StringBuilder();
        Field[] fieldArr = clazz.getDeclaredFields();
        for(Field field : fieldArr) {
            fieldsInfo.append("    ") //四空格缩进
                .append(getFieldInfo(field)).append(NEW_LINE);
        }
        return fieldsInfo.toString();
    }
    
    private String getFieldInfo(Field field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder fieldInfo = new StringBuilder();
        
        // 注解信息 //
        Annotation[] annotationArr = field.getDeclaredAnnotations();
        for(Annotation annotation : annotationArr) {
            fieldInfo.append(getAnnotationInfo(annotation)).append(NEW_LINE).append("    ");
        }
        
        // 修饰符 //
        fieldInfo.append(Modifier.toString(field.getModifiers())).append(" ");
        
        // 数据域类型与泛型 //
        fieldInfo.append(PATTERN.matcher(field.getGenericType().toString()).replaceAll("")).append(" ");
        
        // 数据域名称 //
        fieldInfo.append(field.getName()).append(";");
        return fieldInfo.toString();
    }
    
    //=========================================
    // 解析构造方法
    //=========================================
    private String analyzeConstructors() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder constructorInfo = new StringBuilder();
        Constructor<?>[] constructorArr = clazz.getDeclaredConstructors();
        for(Constructor<?> constructor : constructorArr) {
            constructorInfo.append("    ") //四格缩进
                .append(getConstructorInfo(constructor)).append(NEW_LINE);
        }
        return constructorInfo.toString();
    }
    
    private String getConstructorInfo(Constructor<?> constructor) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder constructorInfo = new StringBuilder();
        
        // 注解 //
        Annotation[] annotationArr = constructor.getDeclaredAnnotations();
        for(Annotation annotation : annotationArr) {
            constructorInfo.append(getAnnotationInfo(annotation)).append(NEW_LINE).append("    ");
        }
        
        // 修饰符 //
        constructorInfo.append(Modifier.toString(constructor.getModifiers())).append(" ");
        
        // 构造方法名 //
        constructorInfo.append(clazz.getSimpleName());
        
        // 构造方法参数 //
        constructorInfo.append("(");
        Type[] parameterArr = constructor.getGenericParameterTypes();
        if(parameterArr.length!=0) {
            for(Type parameter : parameterArr) {
                if(parameter instanceof Class) {
                    constructorInfo.append(((Class<?>) parameter).getSimpleName());
                } else {
                    constructorInfo.append(PATTERN.matcher(parameter.toString()).replaceAll(""));
                }
                constructorInfo.append(",");
            }
            constructorInfo.deleteCharAt(constructorInfo.length()-1);
        }
        constructorInfo.append(")");
        
        // 构造方法异常 //
        Type[] exceptionArr = constructor.getGenericExceptionTypes();
        if(exceptionArr.length!=0) {
            constructorInfo.append(" ").append("throws").append(" ");
            for(Type exception : exceptionArr) {
                if(exception instanceof Class) {
                    constructorInfo.append(((Class<?>) exception).getSimpleName());
                } else {
                    constructorInfo.append(PATTERN.matcher(exception.toString()).replaceAll(""));
                }
                constructorInfo.append(",");
            }
            constructorInfo.deleteCharAt(constructorInfo.length()-1);
        }
        
        //构造方法体
        constructorInfo.append(" ").append("{").append(NEW_LINE)
            .append(NEW_LINE).append("    ") //四行缩进
            .append("}");
        return constructorInfo.toString();
    }
    
    //=========================================
    // 解析方法
    //=========================================
    private String analyzeMethods() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder methodInfo = new StringBuilder();
        Method[] methodArr = clazz.getDeclaredMethods();
        for(Method method : methodArr) {
            methodInfo.append("    ") //四空格缩进
                .append(getMethodInfo(method)).append(NEW_LINE);
        }
        return methodInfo.toString();
    }
    
    private String getMethodInfo(Method method) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        
        StringBuilder methodInfo = new StringBuilder();
       // 注解 //
        Annotation[] annotationArr = method.getDeclaredAnnotations();
        for(Annotation annotation : annotationArr) {
            methodInfo.append(getAnnotationInfo(annotation)).append(NEW_LINE).append("    ");
        }
        
        // 修饰符 //
        methodInfo.append(Modifier.toString(method.getModifiers())).append(" ");
        
        // 方法名 //
        methodInfo.append(method.getName());
        
        // 方法参数 //
        methodInfo.append("(");
        Type[] parameterArr = method.getGenericParameterTypes();
        if(parameterArr.length!=0) {
            for(Type parameter : parameterArr) {
                if(parameter instanceof Class) {
                    methodInfo.append(((Class<?>) parameter).getSimpleName());
                } else {
                    methodInfo.append(PATTERN.matcher(parameter.toString()).replaceAll(""));
                }
                methodInfo.append(",");
            }
            methodInfo.deleteCharAt(methodInfo.length()-1);
        }
        methodInfo.append(")");
        
        // 方法异常 //
        Type[] exceptionArr = method.getGenericExceptionTypes();
        if(exceptionArr.length!=0) {
            methodInfo.append("throws").append(" ");
            for(Type exception : exceptionArr) {
                if(exception instanceof Class) {
                    methodInfo.append(((Class<?>) exception).getSimpleName());
                } else {
                    methodInfo.append(PATTERN.matcher(exception.toString()).replaceAll(""));
                }
                methodInfo.append(",");
            }
            methodInfo.deleteCharAt(methodInfo.length()-1);
        }
        
        //方法体
        if(Modifier.isAbstract(method.getModifiers())) {
            methodInfo.append(";");
        } else {
            methodInfo.append(" ").append("{").append(NEW_LINE)
            .append(NEW_LINE).append("    ") //四行缩进
            .append("}");
        }
        return methodInfo.toString();
    }
    
}
