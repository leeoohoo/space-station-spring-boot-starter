package com.oohoo.spacestationspringbootstarter.dto.query.lambda;

import com.oohoo.spacestationspringbootstarter.dto.query.DTO;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.EntityName;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.From;
import com.oohoo.spacestationspringbootstarter.dto.query.annotation.JoinColumn;
import com.oohoo.spacestationspringbootstarter.dto.query.exception.DtoQueryException;
import lombok.SneakyThrows;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;


/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 20 October 2022
 */
public class ClassUtils {

    private static final char PACKAGE_SEPARATOR = '.';

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList("net.sf.cglib.proxy.Factory"
            // cglib
            , "org.springframework.cglib.proxy.Factory"
            , "javassist.util.proxy.ProxyObject"
            // javassist
            , "org.apache.ibatis.javassist.util.proxy.ProxyObject");

    private ClassUtils() {
    }

    /**
     * 判断传入的类型是否是布尔类型
     *
     * @param type 类型
     * @return 如果是原生布尔或者包装类型布尔，均返回 true
     */
    public static boolean isBoolean(Class<?> type) {
        return type == boolean.class || Boolean.class == type;
    }

    /**
     * 判断是否为代理对象
     *
     * @param clazz 传入 class 对象
     * @return 如果对象class是代理 class，返回 true
     */
    public static boolean isProxy(Class<?> clazz) {
        if (clazz != null) {
            for (Class<?> cls : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(cls.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>
     * 获取当前对象的 class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        return isProxy(clazz) ? clazz.getSuperclass() : clazz;
    }

    /**
     * <p>
     * 获取当前对象的class
     * </p>
     *
     * @param object 对象
     * @return 返回对象的 user class
     */
    public static Class<?> getUserClass(Object object) {
        Assert.notNull(object, "Error: Instance must not be null");
        return getUserClass(object.getClass());
    }

    /**
     * <p>
     * 根据指定的 class ， 实例化一个对象，根据构造参数来实例化
     * </p>
     * <p>
     * 在 java9 及其之后的版本 Class.newInstance() 方法已被废弃
     * </p>
     *
     * @param clazz 需要实例化的对象
     * @param <T>   类型，由输入类型决定
     * @return 返回新的实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw ExceptionUtils.mpe("实例化对象时出现错误,请尝试给 %s 添加无参的构造方法", e, clazz.getName());
        }
    }

    /**
     * 实例化对象.
     *
     * @param clazzName 类名
     * @param <T>       类型
     * @return 实例
     * @since 3.3.2
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazzName) {
        return (T) newInstance(toClassConfident(clazzName));
    }


    /**
     * <p>
     * 请仅在确定类存在的情况下调用该方法
     * </p>
     *
     * @param name 类名称
     * @return 返回转换后的 Class
     */
    public static Class<?> toClassConfident(String name) {
        try {
            return Class.forName(name, false, getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(name);
            } catch (ClassNotFoundException ex) {
                throw ExceptionUtils.mpe("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
            }
        }
    }


    /**
     * Determine the name of the package of the given class,
     * e.g. "java.lang" for the {@code java.lang.String} class.
     *
     * @param clazz the class
     * @return the package name, or the empty String if the class
     * is defined in the default package
     */
    public static String getPackageName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return getPackageName(clazz.getName());
    }

    /**
     * Determine the name of the package of the given fully-qualified class name,
     * e.g. "java.lang" for the {@code java.lang.String} class name.
     *
     * @param fqClassName the fully-qualified class name
     * @return the package name, or the empty String if the class
     * is defined in the default package
     */
    public static String getPackageName(String fqClassName) {
        Assert.notNull(fqClassName, "Class name must not be null");
        int lastDotIndex = fqClassName.lastIndexOf(PACKAGE_SEPARATOR);
        return (lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "");
    }

    /**
     * Return the default ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p>Call this method if you intend to use the thread context ClassLoader
     * in a scenario where you clearly prefer a non-null ClassLoader reference:
     * for example, for class path resource loading (but not necessarily for
     * {@code Class.forName}, which accepts a {@code null} ClassLoader
     * reference as well).
     *
     * @return the default ClassLoader (only {@code null} if even the system
     * ClassLoader isn't accessible)
     * @see Thread#getContextClassLoader()
     * @see ClassLoader#getSystemClassLoader()
     * @since 3.3.2
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }


    /**
     * 检查是否是实体类
     *
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        try {
            String tableNameByEntity = getTableNameByEntity(clazz);
            if(StringUtils.hasLength(tableNameByEntity)) {
                return tableNameByEntity;
            }
        }catch (DtoQueryException dtoQueryException) {
            return getTableNameByDto(clazz);
        }
        return getTableNameByDto(clazz);
    }

    private static String getTableNameByDto(Class<?> clazz) {
        if(null == clazz) {
            throw new DtoQueryException("请选择要查询的表");
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        boolean isDto = interfaces.length > 0 && interfaces[0].equals(DTO.class);
        if(isDto) {
            From declaredAnnotation = clazz.getDeclaredAnnotation(From.class);
            if(null != declaredAnnotation){
                return getTableNameByEntity(declaredAnnotation.value());
            }else {
                throw new DtoQueryException("获取查询的类发生错误，DTO 未添加“@From 注解”");
            }
        }
        throw new DtoQueryException("获取查询的类发生错误，请继承DTO”");
    }

    private static String getTableNameByEntity(Class<?> clazz) {
        if(null == clazz) {
            throw new DtoQueryException("请确定要查询的表");
        }
        EntityName entityName = clazz.getDeclaredAnnotation(EntityName.class);
        Entity entity = clazz.getDeclaredAnnotation(Entity.class);
        String tableName = "";
        boolean flag = false;
        if (null != entity) {
            flag = true;
            tableName = entity.name();
        }
        if (null != entityName) {
            flag = true;
            tableName = entityName.name();
        }

        if(flag) {
            return StringUtils.hasLength(tableName) ? tableName : camelToUnderline(clazz.getSimpleName());
        }else {
            throw new DtoQueryException("查询的对象不是实体类，className:[" + clazz.getName() + "]");
        }

    }


    /**
     * 获取字段名
     *
     * @param str
     * @return
     */
    public static String getFiledName(String str) {
        if (!StringUtils.hasLength(str) || str.length() <= 3) {
            throw new DtoQueryException("查询字段异常,fieldName:[" + str + "]");
        }
        String getString = str.substring(0, 3);
        if (!"get".equals(getString)) {
            throw new DtoQueryException("查询字段异常,fieldName:[" + str + "]");
        }
        String substring = str.substring(3);
        char[] cs = substring.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

    public static List<Column> fieldsToColumns(Class<?> dtoClass, List<Field> fields) {
        List<Column> columns = new ArrayList<>();
        fields.forEach(it -> {
            columns.add(fieldToColumn(it, dtoClass));
        });
        return columns;
    }

    public static Column fieldToColumn(Field field, Class<?> dtoClass) {
        Column column = Column.create(dtoClass, field);
        JoinColumn joinColumn = field.getDeclaredAnnotation(JoinColumn.class);
        if (null != joinColumn) {
            column.setField(ClassUtils.camelToUnderline(joinColumn.columnName()));
            column.setClazz(joinColumn.joinClass());
            column.setAlias(field.getName());
            column.setTableName(ClassUtils.getTableName(column.getClazz()));
        }
        return column;
    }

    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    @SneakyThrows
    public static <T> T mapToObj(Map source, Class<T> target) {
        Assert.notNull(target,"转换的实例类型不能为null");
        Assert.notNull(source,"转换的Map不能为null");
        Field[] fields = target.getDeclaredFields();
        T o = target.newInstance();
        for (Field field : fields) {
            Object val;
            if ((val = source.get(field.getName())) != null) {
                // 针对jpa 的转换问题做的单独处理，同样适用于类似的其他情况
                if (val instanceof BigInteger && field.getType().getName().equals("java.lang.Long")) {
                    val = Long.valueOf(String.valueOf(val));
                }

                field.setAccessible(true);
                field.set(o, val);

            }
        }
        return o;
    }


}
