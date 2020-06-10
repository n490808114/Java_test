package com.learn.test.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Generator {
    public static void main(String[] args) throws IOException {
        // 读取
        // 读取需要修改的类
        ClassReader classReader = new ClassReader("com/learn/test/asm/Base");
        // COMPUTE_MAXS
        // 用于自动计算方法的最大堆栈大小和最大局部变量数的标志。如果设置了这个标志，
        // 那么由visitMethod方法返回的MethodVisitor.visitMaxs方法的参数将被忽略，
        // 并根据每个方法的签名和字节码自动计算。
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        classReader.accept(classVisitor,ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        //输出
        File file = new File("target/classes/com/learn/test/asm/Base.class");
        System.out.println(file.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
        System.out.println("now generator cc success!!!!!");


    }
}
