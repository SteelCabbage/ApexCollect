package com.chinapex.analytics.aop.collect

import com.chinapex.analytics.aop.util.AopLog
import com.chinapex.analytics.aop.util.FilterUtils
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AopClassVisitor extends ClassVisitor {

    public boolean seeModifyMethod = false
    private boolean isMatchClass = false
    private String mClassName
    private String[] mInterfaces
    private String mSuperName

    AopClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        isMatchClass = FilterUtils.isMatchClass(name, superName, interfaces)
        mClassName = name
        mInterfaces = interfaces
        mSuperName = superName
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    void visitInnerClass(String name, String outerName, String innerName, int access) {
        super.visitInnerClass(name, outerName, innerName, access)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions)
        MethodVisitor adapter = null

        if ((isMatchClass && FilterUtils.isMatchMethod(name, desc))) {
            //指定方法名，根据满足的类条件和方法名
            AopLog.info("||-----------------开始修改方法${mClassName}#${name}${desc}--------------------------")
            try {
                adapter = FilterUtils.getMethodVisitor(mInterfaces, mClassName, mSuperName, methodVisitor, access, name, desc)
            } catch (Exception e) {
                e.printStackTrace()
                adapter = null
            }
        }
        if (adapter != null) {
            return adapter
        }
        return methodVisitor
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }
}