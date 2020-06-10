package com.learn.test.asm;

import org.apache.ibatis.javassist.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class JavassistTest {
    public static void main(String[] args)
            throws NotFoundException,
            CannotCompileException,
            IllegalAccessException,
            InstantiationException,
            NoSuchMethodException,
            InvocationTargetException,
            IOException {
        // 先加载类的话,下面ctClass.toClass() 将报错
        // loader 'app' attempted duplicate class definition for com.learn.test.asm.Base. (com.learn.test.asm.Base is in unnamed module of loader 'app')
        // 'app' 加载器试图为 com.learn.test.asm.Base 重新定义类 ( com.learn.test.asm.Base 在 'app' 加载器的未命名模块中)
        // Base base = new Base();
        // 获取类
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.learn.test.asm.Base");
        // 获取方法
        CtMethod ctMethod = ctClass.getDeclaredMethod("process");
        // 插入代码
        ctMethod.insertBefore("{ System.out.println(\"new start\"); }");
        ctMethod.insertAfter("{ System.out.println(\"new end\"); }");
        // 转化为Class
        Class clazz = ctClass.toClass();
        // 文件存储路径
        ctClass.writeFile("target/classes/com/learn/test/asm");
        // 实例化
        Base h = (Base) (clazz.getDeclaredConstructor().newInstance());
        // 执行
        h.process();
    }
}
