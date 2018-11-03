package com.darklycoder.srouter.compiler;

import com.darklycoder.srouter.annotation.Pack;
import com.darklycoder.srouter.annotation.Route;
import com.darklycoder.srouter.info.IRouterTable;
import com.darklycoder.srouter.info.RouterMetaInfo;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 处理路由
 *
 * @author DarklyCoder 2018/10/24
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class RouteProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> sets = new LinkedHashSet<>();

        sets.add(Pack.class.getCanonicalName());
        sets.add(Route.class.getCanonicalName());

        return sets;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Set<? extends Element> packSet = roundEnv.getElementsAnnotatedWith(Pack.class);
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(Route.class);

        if (null == annotations || annotations.isEmpty() || null == set || set.isEmpty() || null == packSet || packSet.isEmpty()) {
            return false;
        }

        Element packElement = packSet.iterator().next();
        if (packElement.getKind() != ElementKind.CLASS) {
            printMessage("only support class");
            return false;
        }

        Pack pack = packElement.getAnnotation(Pack.class);
        String pkgName = pack.value();
        String tableName = pack.table();
        String assetsPath = pack.assetsPath();

        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(
                ClassName.get(HashMap.class),
                ClassName.get(String.class),
                ClassName.get(RouterMetaInfo.class));

        MethodSpec.Builder methodHandle = MethodSpec.methodBuilder("getRouterTable")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("$T map = new $T()", mapTypeName, mapTypeName)
                .returns(mapTypeName);

        Map<String, RouterMetaInfo> pathRecorder = new HashMap<>();

        for (Element element : set) {
            if (element.getKind() != ElementKind.CLASS) {
                printMessage("only support class");
                continue;
            }

            //生成界面映射
            Route route = element.getAnnotation(Route.class);

            TypeElement typeElement = (TypeElement) element;
            String path = route.value();
            int extra = route.extra();

            if (path.isEmpty() || path.trim().isEmpty() || pathRecorder.containsKey(path)) {
                //路径不合法
                continue;
            }

            String clazzName = typeElement.getQualifiedName().toString();

            RouterMetaInfo metaInfo = new RouterMetaInfo(path, clazzName, extra);

            methodHandle.addStatement("map.put($S, new $T($S,$S," + extra + "))", path, RouterMetaInfo.class, metaInfo.route, metaInfo.path);
            pathRecorder.put(path, metaInfo);
        }

        methodHandle.addStatement("return map");

        String tableNameTmp = tableName.trim().isEmpty() ? "RouteTable" : tableName;

//        MethodSpec.Builder constructMethodHandle = MethodSpec.methodBuilder(tableNameTmp)
//                .addModifiers(Modifier.PRIVATE);
//
//        FieldSpec fieldSpec = FieldSpec.builder(TypeVariableName.get(tableNameTmp),
//                "_instance",
//                Modifier.PRIVATE, Modifier.STATIC)
//                .initializer("new $T()", TypeVariableName.get(tableNameTmp)).build();
//
//        TypeSpec subType = TypeSpec.classBuilder("SingletonInner")
//                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
//                .addField(fieldSpec)
//                .build();
//
//        MethodSpec.Builder getInstance = MethodSpec.methodBuilder("getInstance")
//                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                .addStatement("return SingletonInner._instance")
//                .returns(TypeVariableName.get(tableNameTmp));

        TypeSpec type = TypeSpec.classBuilder(tableNameTmp)
                .addModifiers(Modifier.PUBLIC)
//                .addMethod(constructMethodHandle.build())
                .addSuperinterface(TypeName.get(IRouterTable.class))
//                .addType(subType)
//                .addMethod(getInstance.build())
                .addMethod(methodHandle.build())
                .build();

        try {
            createJson(tableName, assetsPath, pathRecorder);

        } catch (IOException e) {
            printMessage(e.getMessage());
            e.printStackTrace();
        }

        try {
            //生成代码
            String pkgNameTmp = pkgName.trim().isEmpty() ? "com.darklycoder.srouter" : pkgName;
            JavaFile.builder(pkgNameTmp, type).build().writeTo(processingEnv.getFiler());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 生成json格式路由表
     *
     * @param tableName 文件ming
     * @param map       内容
     */
    private void createJson(String tableName, String assetsPath, Map<String, RouterMetaInfo> map) throws IOException {
        Set<String> set = map.keySet();
        int size = set.size() - 1;
        int i = 0;
        StringBuilder sb = new StringBuilder("[");
        for (String key : set) {
            RouterMetaInfo metaInfo = map.get(key);

            sb.append("{\"");

            sb.append("route");
            sb.append("\":\"");
            sb.append(metaInfo.route);

            sb.append("\",\"path");
            sb.append("\":\"");
            sb.append(metaInfo.path);

            sb.append("\",\"extra");
            sb.append("\":");
            sb.append(metaInfo.extra);

            sb.append("}");

            if (i < size) {
                sb.append(",");
            }

            i++;
        }

        sb.append("]");

        String path = new File("").getCanonicalPath() + assetsPath + tableName + ".json";

        File file = new File(path);
        boolean isDelete = !file.exists() || file.delete();
        boolean isMkdirs = file.getParentFile().mkdirs();

        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            fw.write(sb.toString());

        } catch (IOException ex) {
            ex.printStackTrace();

        } finally {
            try {
                if (null != fw) {
                    fw.flush();
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void printMessage(String content) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, content);
    }

}
