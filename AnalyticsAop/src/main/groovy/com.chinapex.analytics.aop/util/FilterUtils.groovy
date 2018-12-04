package com.chinapex.analytics.aop.util

import com.chinapex.analytics.aop.collect.AopMethodVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class FilterUtils {

    private static String[] SUPER_NAME_WHITE_LIST = [
            "android/support/v4/app/Fragment",
            "android/app/Fragment",
            "android/support/v4/app/DialogFragment",
            "android/app/DialogFragment",
    ]

    private static String[] INTERFACE_NAME_WHITE_LIST = [
            'android/widget/AdapterView$OnItemClickListener',
            'android/view/View$OnClickListener'
    ]

    static boolean isMatchClass(String className, String superName, String[] interfaces) {
        boolean isMatchClass = false

        //剔除掉以android开头的类，即系统类，以避免出现不可预测的bug
        if (className.startsWith('android')) {
            return isMatchClass
        }

        // 是否满足实现的接口
        for (String name : INTERFACE_NAME_WHITE_LIST) {
            if (isMatchInterfaces(interfaces, name)) {
                AopLog.info("interface name: " + name)
                isMatchClass = true
                break
            }
        }

        for (String name : SUPER_NAME_WHITE_LIST) {
            if (name == superName) {
                AopLog.info("classname: " + name + " supername: " + superName)
                isMatchClass = true
                break
            }
        }
        /*if (superName.equals('android/support/v4/app/Fragment')) {
            isMatchClass = true
        } else if (superName.equals("android/app/Fragment")) {
            isMatchClass = true
        }/*else if (className.contains('RecyclerView')) {
            isMatchClass = true
        } /*else if (isMatchingSettingClass(className, interfaces)) {
            isMatchClass = true
        }*/

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
                || (name == 'onItemClick' && desc == '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V')
                || (name == 'onResume' && desc == '()V')
                || (name == 'onPause' && desc == '()V')
                || (name == 'setUserVisibleHint' && desc == '(Z)V')
                || (name == 'onHiddenChanged' && desc == '(Z)V')
//                || (name == "onScrollStateChanged" && desc == '(Landroid/support/v7/widget/RecyclerView;I)V')
        /*|| isMatchingSettingMethod(name, desc)*/) {
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

    static MethodVisitor getMethodVisitor(String[] interfaces, String className, String superName,
                                          MethodVisitor methodVisitor, int access, String name, String desc) {
        MethodVisitor adapter = null

        if (name == "onClick" && desc == '(Landroid/view/View;)V') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onClick", "(Landroid/view/View;)Z", false)

                    Label label = new Label()
                    methodVisitor.visitJumpInsn(Opcodes.IFEQ, label)
                    methodVisitor.visitInsn(Opcodes.RETURN)
                    methodVisitor.visitLabel(label)
                }
            }
        } else if (name == "onItemClick" && desc == '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V') {
            AopLog.info("FilterUtils" + "getMethodVisitor onItemClick ")

            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 3)
                    methodVisitor.visitVarInsn(Opcodes.LLOAD, 4)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onItemClick", "(Landroid/widget/AdapterView;Landroid/view/View;IJ)Z", false)

                    Label label = new Label()
                    methodVisitor.visitJumpInsn(Opcodes.IFEQ, label)
                    methodVisitor.visitInsn(Opcodes.RETURN)
                    methodVisitor.visitLabel(label)
                }

            }
        } else if (name == "onResume" && desc == '()V' &&
                (superName.equals('android/support/v4/app/Fragment') || superName.equals("android/support/v4/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentResume", "(Landroid/support/v4/app/Fragment;)V", false)
                }
            }
//        } else if (name == "onPause" && className.contains("Fragment")) {
        } else if (name == "onPause" && desc == '()V' &&
                (superName.equals('android/support/v4/app/Fragment') || superName.equals("android/support/v4/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentPause", "(Landroid/support/v4/app/Fragment;)V", false)
                }
            }
//        } else if (name == "setUserVisibleHint" && className.contains("Fragment")) {
        } else if (name == "setUserVisibleHint" && desc == '(Z)V' &&
                (superName.equals('android/support/v4/app/Fragment') || superName.equals("android/support/v4/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "setFragmentUserVisibleHint", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
//        } else if (name == "onHiddenChanged" && className.contains("Fragment")) {
        } else if (name == "onHiddenChanged" && desc == '(Z)V' &&
                (superName.equals('android/support/v4/app/Fragment') || superName.equals("android/support/v4/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentHiddenChanged", "(Landroid/support/v4/app/Fragment;Z)V", false)
                }
            }
        } else if (name == "onResume" && desc == '()V' &&
                (superName.equals("android/app/Fragment") || superName.equals("android/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentResume", "(Landroid/app/Fragment;)V", false)
                }
            }
        } else if (name == "onPause" && desc == '()V' &&
                (superName.equals("android/app/Fragment") || superName.equals("android/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentPause", "(Landroid/app/Fragment;)V", false)
                }
            }
        } else if (name == "setUserVisibleHint" && desc == '(Z)V' &&
                (superName.equals("android/app/Fragment") || superName.equals("android/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "setFragmentUserVisibleHint", "(Landroid/app/Fragment;Z)V", false)
                }
            }
        } else if (name == "onHiddenChanged" && desc == '(Z)V' &&
                (superName.equals("android/app/Fragment") || superName.equals("android/app/DialogFragment"))){
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentHiddenChanged", "(Landroid/app/Fragment;Z)V", false)
                }
            }
        }/*else if (name == "onScrollStateChanged" && desc == '(Landroid/support/v7/widget/RecyclerView;I)V') {
            AopLog.info("onScrollStateChanged desc == '(Landroid/support/v7/widget/RecyclerView;I)V' is match")
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "rvOnScrollStateChanged", "(Landroid/support/v7/widget/RecyclerView;I)V", false)
                }
            }

        }else if (Controller.isUseAnotation()) {
            // 注解的话，使用指定方法
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        } else if (isMatchingSettingClass(className, interfaces)){
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        }*/

        return adapter
    }
}