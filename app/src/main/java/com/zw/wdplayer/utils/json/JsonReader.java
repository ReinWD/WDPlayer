package com.zw.wdplayer.utils.json;

import com.zw.wdplayer.utils.json.value.JsonArray;
import com.zw.wdplayer.utils.json.value.JsonBase;
import com.zw.wdplayer.utils.json.value.JsonObject;
import com.zw.wdplayer.utils.json.value.JsonValue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZW on 2017/5/13.
 */

public class JsonReader {
    private String jsonText;
    private JsonObject root;

    public JsonReader() {
    }

    public <T> T readJson(String  json, Class<T> targetClass) throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Reflector reflector = new Reflector();
        JsonObject jsonObject = readJson(json);
        T result = reflector.reflect(jsonObject, targetClass);

        return result;
    }

    public <T> T readStream(InputStream is,Class<T> targetClass) throws IOException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        T result = readJson(decodeStream(is),targetClass);

        return result;
    }

    public JsonObject readJson(String json) {
        jsonText = json;
        root = readObject(json);
        return root;
    }

    private JsonObject readObject(String object) {
        String keyTemp;
        JsonBase valueTemp;
        JsonObject item = new JsonObject();
        int[] pointer = {1};
        int[] nextPointer = {0}, thirdPointer = {0};
        //插入第一个键值对
        nextPointer[0] = object.indexOf(":");
        keyTemp = object.substring(pointer[0] + 1, nextPointer[0] - 1);
        pointer[0] = nextPointer[0] + 1;
        valueTemp = readValue(object, pointer, nextPointer, thirdPointer);
        item.put(keyTemp, valueTemp);
        nextPointer[0] = object.indexOf(",", nextPointer[0]);
        pointer[0] = nextPointer[0];

        while (pointer[0] != -1) {
            nextPointer[0] = object.indexOf(":", pointer[0]);
            keyTemp = object.substring(pointer[0] + 2, nextPointer[0] - 1);
            pointer[0] = nextPointer[0] + 1;
            valueTemp = readValue(object, pointer, nextPointer, thirdPointer);
            item.put(keyTemp, valueTemp);
            nextPointer[0] = object.indexOf(",", nextPointer[0]);
            pointer[0] = nextPointer[0];

        }
        return item;
    }

    private JsonBase readValue(String object, int[] pointer, int[] nextPointer, int[] thirdPointer) {
        JsonBase result;

        switch (object.charAt(pointer[0])) {
            case '{':
                subObject(object, "{", "}", pointer, nextPointer, thirdPointer);//寻找对象结束点
                result = readObject(object.substring(pointer[0], nextPointer[0] + 1));
                break;
            case '[':
                subObject(object, "[", "]", pointer, nextPointer, thirdPointer);//寻找数组结束点
                result = readArray(object, pointer, nextPointer, thirdPointer);
                break;
            case '\"':
                if ((nextPointer[0] = object.indexOf(",", pointer[0] + 1)) == -1) {
                    nextPointer[0] = object.indexOf("}", pointer[0]);
                }
                result = new JsonValue(object.substring(pointer[0] + 1, nextPointer[0] - 1));
                break;
            default:
                if ((nextPointer[0] = object.indexOf(",", pointer[0] + 1)) == -1) {
                    nextPointer[0] = object.indexOf("}", pointer[0]);
                }
                result = new JsonValue(object.substring(pointer[0], nextPointer[0]));
                break;
        }
        return result;
    }

    private void subObject(String json, String origin, String target, int[] pointer, int[] nextPointer, int[] thirdPointer) {
        String keyTemp;
        JsonBase valueTemp;
        String cache;
        nextPointer[0] = json.indexOf(target, pointer[0]);//向下查找"]"
        cache = json.substring(pointer[0] + 1, nextPointer[0] + 1);//将[中]内容分离出来
        while (cache.contains(origin)) {                             //如果[中]含有[
            thirdPointer[0] = json.indexOf(target, nextPointer[0] + 1);           //再次向下查找"]"
            cache = json.substring(nextPointer[0] + 1, thirdPointer[0]);
            nextPointer[0] = thirdPointer[0];
        }
    }

    private JsonArray readArray(String array, int[] pointer, int[] nextPointer, int[] thirdPointer) {
        JsonBase valueTemp;
        JsonArray result = new JsonArray();
        array = array.substring(0, nextPointer[0]);
        pointer[0]++;
        while ((nextPointer[0] = array.indexOf(",", pointer[0])) != -1) {
            switch (array.charAt(pointer[0])) {
                case '{':
                    subObject(array, "{", "}", pointer, nextPointer, thirdPointer);
                    valueTemp = readObject(array.substring(pointer[0], nextPointer[0] + 1));
                    result.add(valueTemp);
                    break;
                case '[':
                    subObject(array, "[", "]", pointer, nextPointer, thirdPointer);
                    valueTemp = readArray(array.substring(pointer[0], nextPointer[0]), pointer, nextPointer, thirdPointer);
                    result.add(valueTemp);
                    break;
                case '\"':
                    if (nextPointer[0] == -1) {
                        nextPointer[0] = array.indexOf("}", pointer[0]);
                    }
                    if (nextPointer[0] == -1) {
                        nextPointer[0] = array.indexOf("]", pointer[0]);
                    }
                    valueTemp = new JsonValue(array.substring(pointer[0] + 1, nextPointer[0] - 1));
                    result.add(valueTemp);
                    break;
                default:
                    if ((nextPointer[0] = array.indexOf(",", pointer[0])) == -1) {
                        nextPointer[0] = array.indexOf("}", pointer[0]);
                    }
                    if (nextPointer[0] == -1) {
                        nextPointer[0] = array.indexOf("]", pointer[0]);
                    }
            }
            pointer[0] = nextPointer[0] + 1;
        }
        pointer[0] = array.length() - 1;
        nextPointer[0] = pointer[0];
        return result;
    }

    private String decodeStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int cache;
        while ((cache = reader.read()) != -1) {
            outputStream.write(cache);
        }
        String result = outputStream.toString();
        is.close();
        outputStream.close();
        return result;
    }

    class Reflector {

        Class targetClass;

        Reflector() {
        }

        <T> T reflect(JsonObject jsonObject, Class<T> targetClass) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
            this.targetClass = targetClass;
            ArrayList<Method> setters = new ArrayList<>();
            HashMap<String,Class>innerclasses = new HashMap<>();
            HashMap<String, String> serializedNames = new HashMap<>();


            T target = targetClass.newInstance();
            Field[] fields = targetClass.getDeclaredFields();
            Method[] methods = targetClass.getDeclaredMethods();
            getSetters(setters, methods);
            for (Class innerClass:

                    targetClass.getClasses()){
                innerclasses.put(innerClass.getSimpleName(),innerClass);
            }
            for (Field f :
                    fields) {
                SerializedName serializedName = f.getAnnotation(SerializedName.class);
                if (serializedName != null) serializedNames.put(f.getName(), serializedName.value());
            }
            for (Method setter :
                    setters) {
                String name = setter.getName();
                name = name.replaceFirst("set.", String.valueOf(name.charAt(3)).toLowerCase());
                if (serializedNames.get(name) != null) {
                    setValue(setter, target, generateSpecificType(jsonObject, serializedNames, name
                            ,innerclasses));
                } else {
                    setValue(setter, target, generateSpecificType(jsonObject, null, name
                            ,innerclasses));
                }
            }
            return target;
        }

        private <M> M generateSpecificType(JsonObject jsonObject, HashMap<String, String> serializedNames, String name
                ,HashMap<String,Class> innerClassMap)
                throws InvocationTargetException
                , NoSuchMethodException, InstantiationException, IllegalAccessException {
            JsonBase jsonBase;
            if (serializedNames==null){
                jsonBase = jsonObject.getContent().get(name);
            }else jsonBase = jsonObject.getContent().get(serializedNames.get(name));
            M target;
            switch (jsonBase.getType()) {
                case JsonBase.TYPE_VALUE:
                    target = (M) jsonBase.getContent();
                    break;
                case JsonBase.TYPE_OBJECT:
                    String  newName = name.replaceFirst(".",String.valueOf(name.charAt(0)).toUpperCase());
                    target = reflect(((JsonObject) jsonBase), ((Class<M>)innerClassMap.get(newName)));
                    break;
                default:
                    ArrayList<Object> newArray= new ArrayList<>();
                    newName = name.replaceFirst(".",String.valueOf(name.charAt(0)).toUpperCase());

                    for (Object value :
                            (ArrayList<Object>) jsonBase.getContent()) {
                        JsonBase base = ((JsonBase) value);
                        if(base.getType()==JsonBase.TYPE_OBJECT){
                            Object obj = reflect(((JsonObject) base), ((innerClassMap.get(newName))));
                            newArray.add(obj);
                        }
                    }
                    target = ((M) newArray);
                    break;
            }
            return target;
        }

        private <T, M> void setValue(Method setter, T target, M parameter) throws InvocationTargetException, IllegalAccessException {
            setter.invoke(target, parameter);
        }

        void getSetters(ArrayList<Method> setters, Method[] methods) {
            for (Method me :
                    methods) {
                if (me.getName().startsWith("set")) setters.add(me);
            }
        }
    }

}