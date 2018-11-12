package com.chinapex.analytics.aop.util

import com.chinapex.analytics.aop.collect.AopMethodVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class FilterUtils {

    static boolean isMatchClass(String className, String[] interfaces) {
        boolean isMatchClass = false
        //剔除掉以android开头的类，即系统类，以避免出现不可预测的bug
        if (className.startsWith('android')) {
            return isMatchClass
        }
        // 是否满足实现的接口
        isMatchClass = isMatchInterfaces(interfaces, 'android/view/View$OnClickListener')
//        if (className.contains('Fragment')) {
//            isMeetClassCondition = true
//        } else if (isMatchingSettingClass(className, interfaces)) {
//            isMeetClassCondition = true
//        }
        return isMatchClass
    }

    private static boolean isMatchInterfaces(String[] interfaces, String interfaceName) {
        boolean isMatchInterface = false
        // 是否满足实现的接口
        interfaces.each {
            String interfaceTmp ->
                if (interfaceTmp == interfaceName) {
                    isMatchInterface = true
                }
        }
        return isMatchInterface
    }

    static boolean isMatchMethod(String name, String desc) {
        if ((name == 'onClick' && desc == '(Landroid/view/View;)V')
                || (name == 'onResume' && desc == '()V')
                || (name == 'onPause' && desc == '()V')
                || (name == 'setUserVisibleHint' && desc == '(Z)V')
                || (name == 'onHiddenChanged' && desc == '(Z)V')
                || isMatchingSettingMethod(name, desc)) {
            return true
        } else {
            return false
        }
    }

    private static boolean isMatchingSettingMethod(String name, String desc) {
//        String appMethodName = Controller.getMethodName()
//        String appMethodDes = Controller.getMethodDes()
//        if (name == appMethodName && desc == appMethodDes) {
//            return true
//        } else if (Controller.isUseAnotation()) {
//            //使用注解的方式，直接就方法匹配，因为注解的方法hook是自己在app module中
//            //控制的
//            return true
//        }
        return false
    }

    static MethodVisitor getMethodVisitor(String[] interfaces, String className,
                                          MethodVisitor methodVisitor, int access, String name, String desc) {
        MethodVisitor adapter = null

//        if (name == "onClick" && isMatchInterfaces(interfaces, 'android/view/View$OnClickListener')) {
        if (name == "onClick" && desc == '(Landroid/view/View;)V') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    // ALOAD 25
                    adapter.visitVarInsn(Opcodes.ALOAD, 1)
                    // INVOKESTATIC 184
                    adapter.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onClick", "(Landroid/view/View;)Z", false)

                    Label label = new Label()
                    adapter.visitJumpInsn(Opcodes.IFEQ, label)
                    adapter.visitInsn(Opcodes.RETURN)
                    adapter.visitLabel(label)

                }

                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter()

//                    // ALOAD 25
//                    adapter.visitVarInsn(Opcodes.ALOAD, 1)
//                    // INVOKESTATIC 184
//                    adapter.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/aopdemo/AopHelper", "onClick", "(Landroid/view/View;)Z", false)
//
//                    Label end = new Label()
//                    adapter.visitInsn(Opcodes.RETURN)


//                    adapter.visitJumpInsn(Opcodes.IFEQ, L0)

//                    adapter.visitInsn(Opcodes.RETURN)

//                    Label end = new Label()
//                    adapter.visitInsn(Opcodes.RETURN)
//
//                    adapter.visitJumpInsn(Opcodes.IFEQ, end)
//                    adapter.visitLabel(end)

                }
            }
        } /*else if (name == "onResume" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD
                    methodVisitor.visitVarInsn(25, 0)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(184, "com/xishuang/plugintest/AutoHelper", "onFragmentResume", "(Landroid/support/v4/app/Fragment;)V", false)
                }
            }
        } else if (name == "onPause" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD 25
                    methodVisitor.visitVarInsn(25, 0)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(184, "com/xishuang/plugintest/AutoHelper", "onFragmentPause", "(Landroid/support/v4/app/Fragment;)V", false)
                }
            }
        } else if (name == "setUserVisibleHint" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD 25
                    methodVisitor.visitVarInsn(25, 0)
                    // ILOAD 21
                    methodVisitor.visitVarInsn(21, 1)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(184, "com/xishuang/plugintest/AutoHelper", "setFragmentUserVisibleHint", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
        } else if (name == "onHiddenChanged" && className.contains("Fragment")) {
            adapter = new AutoMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)
                    // ALOAD 25
                    methodVisitor.visitVarInsn(25, 0)
                    // ILOAD 21
                    methodVisitor.visitVarInsn(21, 1)
                    // INVOKESTATIC 184
                    methodVisitor.visitMethodInsn(184, "com/xishuang/plugintest/AutoHelper", "onFragmentHiddenChanged", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
        } else if (Controller.isUseAnotation()) {
            // 注解的话，使用指定方法
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        } else if (isMatchingSettingClass(className, interfaces)){
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        }*/

        return adapter
    }
}