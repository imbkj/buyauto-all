package com.buyauto.util.json;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import com.buyauto.util.method.DateUtil;


/** *//**
 * JSON工具类，反射的方式转换整个对象
 * @author 
 *
 */
public class InterFaceJSONUtil{

    private static InterFaceJSONUtil instance = null;
    
    public InterFaceJSONUtil(){
    	
    }
    
    /** *//**
     * 代理类时做的检查.返回应该检查的对象.
     * @param bean
     * @return
     */
    protected Object proxyCheck(Object bean){
        return bean;
    }

    static public String toJSONStringG(Object obj,String column) throws JSONException{
        return toJSONString(obj, false,column);
    }
    
    static public String toJSONString(Object obj, boolean useClassConvert,String column) throws JSONException{
        if(instance == null)
            instance = new InterFaceJSONUtil();
        return instance.getJSONObject(obj, useClassConvert,column).toString();
    }

    @SuppressWarnings("unchecked")
	private String getJSONArray(Object arrayObj, boolean useClassConvert,String column) throws JSONException{
        
        if(arrayObj == null)
            return "null";
        
        arrayObj = proxyCheck(arrayObj);
        
        JSONArray jSONArray = new JSONArray();
        if(arrayObj instanceof Collection){
            Iterator iterator = ((Collection)arrayObj).iterator();
            while(iterator.hasNext()){
                Object rowObj = iterator.next();
                if(rowObj == null)
                    jSONArray.put(new JSONStringObject(null));
                else if(rowObj.getClass().isArray() || rowObj instanceof Collection)
                    jSONArray.put(getJSONArray(rowObj, useClassConvert,column));
                else
                    jSONArray.put(getJSONObject(rowObj, useClassConvert,column));
            }
        }
        if(arrayObj.getClass().isArray()){
            int arrayLength = Array.getLength(arrayObj);
            for(int i = 0; i < arrayLength; i ++){
                Object rowObj = Array.get(arrayObj, i);
                if(rowObj == null)
                    jSONArray.put(new JSONStringObject(null));
                else if(rowObj.getClass().isArray() || rowObj instanceof Collection)
                    jSONArray.put(getJSONArray(rowObj, useClassConvert,column));
                else
                    jSONArray.put(getJSONObject(rowObj, useClassConvert,column));
            }
        }
        return jSONArray.toString();
    }

    @SuppressWarnings("unchecked")
	JSONStringObject getJSONObject(Object value, boolean useClassConvert,String column) throws JSONException{

        //处理原始类型

        if (value == null){
            return new JSONStringObject("null");
        }
        value = proxyCheck(value);
        if (value instanceof JSONString){
            Object o;
            try{
                o = ((JSONString)value).toJSONString();
            } catch (Exception e){
                throw new JSONException(e);
            }
            throw new JSONException("Bad value from toJSONString: " + o);
        }
        if (value instanceof Number){
            return new JSONStringObject(JSONObject.numberToString((Number) value));
        }
        if (value instanceof Boolean || value instanceof JSONObject ||
                value instanceof JSONArray){
            return new JSONStringObject(value.toString());
        }
        if (value instanceof Timestamp){
        	String str = DateUtil.getFormattedDateUtil((Timestamp)value, "yyyy-MM-dd HH:mm:ss");
            return new JSONStringObject(JSONObject.quote(str));
        }
        if (value instanceof Date){
        	String str = DateUtil.getFormattedDateUtil((Date)value, "yyyy-MM-dd HH:mm:ss");
            return new JSONStringObject(JSONObject.quote(str));
        }
        if (value instanceof String)
            return new JSONStringObject(JSONObject.quote(value.toString()));
        if (value instanceof Map){
            
            JSONObject jSONObject = new JSONObject();

            Iterator iterator = ((Map)value).keySet().iterator();
            while(iterator.hasNext()){
                String key = iterator.next().toString();
                Object valueObj = ((Map)value).get(key);
                jSONObject.put(key, getJSONObject(valueObj, useClassConvert,column));
            }
            return new JSONStringObject(jSONObject.toString());
        }

        //class

        if(value instanceof Class)
            return new JSONStringObject(JSONObject.quote(((Class)value).getSimpleName()));
        
        //数组

        if (value instanceof Collection || value.getClass().isArray()){
            return new JSONStringObject(getJSONArray(proxyCheck(value), useClassConvert,column));
        }

        return reflectObject(value, useClassConvert,column);
    }//value.equals(null)


    @SuppressWarnings("unchecked")
	private JSONStringObject reflectObject(Object bean, boolean useClassConvert,String column){
        JSONObject jSONObject = new JSONObject();

        Class klass = bean.getClass();
        Method[] methods = klass.getMethods();
        for (int i = 0; i < methods.length; i += 1){
            try{
                Method method = methods[i];
                String name = method.getName();
                String key = "";
                if (name.startsWith("get")){
                    key = name.substring(3);
                } else if (name.startsWith("is")){
                    key = name.substring(2);
                }
                if (key.length() > 0 &&
                        Character.isUpperCase(key.charAt(0)) &&
                        method.getParameterTypes().length == 0){
                    if (key.length() == 1){
                        key = key.toLowerCase();
                    } else if (!Character.isUpperCase(key.charAt(1))){
                        key = key.substring(0, 1).toLowerCase() +
                            key.substring(1);
                    }
                    Object elementObj = method.invoke(bean, null);
                    if(!useClassConvert && elementObj instanceof Class)
                        continue;
                    String col[]=column.split(",");
                    for(int c=0;c<col.length;c++){
                        if(key.equals(col[c])){
                        	jSONObject.put(key, getJSONObject(elementObj, useClassConvert,column));
                        	break;
                        }
                    }

                }
            } catch (Exception e){
                /**//* forget about it */
            }
        }
        return new JSONStringObject(jSONObject.toString());
    }
}