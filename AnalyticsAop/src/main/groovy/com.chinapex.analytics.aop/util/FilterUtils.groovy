package com.chinapex.analytics.aop.util

import com.chinapex.analytics.aop.collect.AopMethodVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class FilterUtils {

    private static String[] START_WITH_IGNORE_LIST = [
            "android",
            "com/chinapex/android/monitor",
            "com/chinapex/android/datacollect"
    ]

    private static String[] SUPER_NAME_WHITE_LIST = [
            "android/support/v7/app/AppCompatActivity",
            "android/support/v4/app/Fragment",
            "android/app/Fragment",
            "android/support/v4/app/DialogFragment",
            "android/app/DialogFragment"
    ]

    private static String[] INTERFACE_NAME_WHITE_LIST = [
            'android/widget/ExpandableListView$OnGroupClickListener',
            'android/widget/ExpandableListView$OnChildClickListener',
            'android/widget/AdapterView$OnItemClickListener',
            'android/view/View$OnClickListener',
            'android/widget/AbsListView$OnScrollListener'
    ]

    static boolean isMatchClass(String className, String superName, String[] interfaces) {
        boolean isMatchClass = false

        // 剔除掉以android开头的类，即系统类，以避免出现不可预测的bug, 同时剔除可视化圈选的包
        for (String name : START_WITH_IGNORE_LIST) {
            if (className.startsWith(name)) {
                return isMatchClass
            }
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
                || (name == 'onGroupClick' && desc == '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z')
                || (name == 'onChildClick' && desc == '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z')
                || (name == 'onOptionsItemSelected' && desc == '(Landroid/view/MenuItem;)Z')
                || (name == 'onResume' && desc == '()V')
                || (name == 'onPause' && desc == '()V')
                || (name == 'setUserVisibleHint' && desc == '(Z)V')
                || (name == 'onHiddenChanged' && desc == '(Z)V')
                || (name == 'onScroll' && desc == '(Landroid/widget/AbsListView;III)V')
                || (name == 'onScrollStateChanged' && desc == '(Landroid/widget/AbsListView;I)V')
                || (name == 'onViewCreated' && desc == '(Landroid/view/View;Landroid/os/Bundle;)V')
        ) {
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
        } else if (name == "onGroupClick" && desc == '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 3)
                    methodVisitor.visitVarInsn(Opcodes.LLOAD, 4)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onGroupClick", "(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z", false)

                    methodVisitor.visitInsn(Opcodes.DUP)
                    Label label = new Label()
                    methodVisitor.visitJumpInsn(Opcodes.IFEQ, label)
                    methodVisitor.visitInsn(Opcodes.IRETURN)
                    methodVisitor.visitLabel(label)
                }

            }
        } else if (name == "onChildClick" && desc == '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 2)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 3)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 4)
                    methodVisitor.visitVarInsn(Opcodes.LLOAD, 5)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onChildClick", "(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z", false)

                    methodVisitor.visitInsn(Opcodes.DUP)
                    Label label = new Label()
                    methodVisitor.visitJumpInsn(Opcodes.IFEQ, label)
                    methodVisitor.visitInsn(Opcodes.IRETURN)
                    methodVisitor.visitLabel(label)
                }

            }
        } else if (name == "onOptionsItemSelected" && desc == '(Landroid/view/MenuItem;)Z') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)

                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onOptionsItemSelected", "(Landroid/support/v7/app/AppCompatActivity;Landroid/view/MenuItem;)Z", false)

                    methodVisitor.visitInsn(Opcodes.DUP)
                    Label label = new Label()
                    methodVisitor.visitJumpInsn(Opcodes.IFEQ, label)
                    methodVisitor.visitInsn(Opcodes.IRETURN)
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
                (superName.equals("android/app/Fragment") || superName.equals("android/app/DialogFragment"))) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode)

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "onFragmentHiddenChanged", "(Landroid/app/Fragment;Z)V", false)
                }
            }
        } else if (name == "onScroll" && desc == '(Landroid/widget/AbsListView;III)V') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 2)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 3)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 4)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "listOnScroll", "(Landroid/widget/AbsListView;III)V", false)
                }
            }

        } else if (name == "onScrollStateChanged" && desc == '(Landroid/widget/AbsListView;I)V') {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitVarInsn(Opcodes.ILOAD, 2)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "listOnScrollStateChanged", "(Landroid/widget/AbsListView;I)V", false)
                }
            }

        } else if (name == 'onViewCreated' && desc == '(Landroid/view/View;Landroid/os/Bundle;)V' && superName.equals('android/support/v4/app/Fragment')) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "v4fragmentOnViewCreated", "(Landroid/support/v4/app/Fragment;Landroid/view/View;)V", false)
                }
            }

        } else if (name == 'onViewCreated' && desc == '(Landroid/view/View;Landroid/os/Bundle;)V' && superName.equals('android/app/Fragment')) {
            adapter = new AopMethodVisitor(methodVisitor, access, name, desc) {

                @Override
                void visitCode() {
                    super.visitCode()

                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 1)
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "com/chinapex/android/datacollect/aop/AopHelper", "fragmentOnViewCreated", "(Landroid/app/Fragment;Landroid/view/View;)V", false)
                }
            }

        }/*else if (Controller.isUseAnotation()) {
            // 注解的话，使用指定方法
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        } else if (isMatchingSettingClass(className, interfaces)){
            adapter = getSettingMethodVisitor(methodVisitor, access, name, desc)
        }*/

        return adapter
    }
}