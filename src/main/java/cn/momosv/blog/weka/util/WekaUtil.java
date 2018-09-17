package cn.momosv.blog.weka.util;

import cn.momosv.blog.weka.annotation.MyInstances;
import org.springframework.util.StringUtils;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class WekaUtil {
    public static  <T> Instances generatePopularInstance(List<T> entities ,Class tClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        //set attributes
        if(entities==null||entities.size()==0)return null;
       Object obj = tClass.newInstance();
        MyInstances myInstances;

        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String,String> attributeMap = new HashMap<>();
        ArrayList<Attribute> attributes = new ArrayList<>();
        for(Field field :fields){
             if(field.isAnnotationPresent(MyInstances.class)){
                  myInstances =  field.getAnnotation(MyInstances.class);
                    if(Attribute.ARFF_ATTRIBUTE_DATE.equals(myInstances.type())){
                        attributes.add(new Attribute(myInstances.ATTRIBUTE(),myInstances.dateFormat(),myInstances.INDEX()));
                    }else if(null != Arrays.asList(myInstances.value()) && Arrays.asList(myInstances.value()).size()>0){
                        attributes.add(new Attribute(myInstances.ATTRIBUTE(),Arrays.asList(myInstances.value()),myInstances.INDEX()));
                    }else{
                        attributes.add(new Attribute(myInstances.ATTRIBUTE(),myInstances.INDEX()));
                    }
                  attributeMap.put(String.valueOf(myInstances.INDEX()),myInstances.ATTRIBUTE());
                }
        }

        //set instances
        myInstances = obj.getClass().getAnnotation(MyInstances.class);
        Instances instances = new Instances( myInstances.RELATION(),attributes,0);
        instances.setClassIndex(myInstances.CLASS_INDEX());
        //add instance
        for (Object ent: entities) {
            Instance instance = new DenseInstance(attributes.size());
            for (int i = 0;i<attributeMap.size();i++){
               String fieldName = attributeMap.get(String.valueOf(i));
                Object getObj = invokeGet(ent,fieldName);
                if(getObj.getClass().isInstance(String.class)){
                    instance.setValue(i, String.valueOf(getObj));
                }else instance.setValue(i, Double.parseDouble( getObj.toString()));

            }
            instances.add(instance);
        }
        return instances;
    }

    public static void generateArffFile(Instances instances, String path) {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(instances);
        try {
            saver.setFile(new File(path));
            saver.writeBatch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Instances getInstanceFromArffFile(String path) throws Exception {
        return ConverterUtils.DataSource.read(path);
    }




    /**
     * java反射bean的get方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * java反射bean的set方法
     *
     * @param objectClass
     * @param fieldName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行set方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     * @param value 值
     */
    public static void invokeSet(Object o, String fieldName, Object value) {
        Method method = getSetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o, new Object[] { value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行get方法
     *
     * @param o 执行对象
     * @param fieldName 属性
     */
    public static Object invokeGet(Object o, String fieldName) {
        Method method = getGetMethod(o.getClass(), fieldName);
        try {
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
