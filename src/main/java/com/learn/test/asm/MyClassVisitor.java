package com.learn.test.asm;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

public class MyClassVisitor extends ClassVisitor implements Opcodes{
    public MyClassVisitor(ClassVisitor classVisitor){
        super(ASM5,classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);

        if(!"<init>".equals(name) && methodVisitor != null){
            methodVisitor = new MyMethodVisitor(methodVisitor);
        }
        return methodVisitor;
    }

    class MyMethodVisitor extends MethodVisitor implements Opcodes{
        public MyMethodVisitor(MethodVisitor methodVisitor){
            super(Opcodes.ASM5,methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();

        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
        }
    }
}
