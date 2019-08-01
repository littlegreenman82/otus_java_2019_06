package ru.otus.hw04.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class Agent {

    private static final String PROJECT_PATH = "ru/otus/hw04";

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {

                if (className.contains(PROJECT_PATH)) {
                    return logToConsoleAnnotatedMethod(classfileBuffer);
                }

                return classfileBuffer;
            }
        });

    }

    private static byte[] logToConsoleAnnotatedMethod(byte[] classfileBuffer) {
        ClassReader cr = new ClassReader(classfileBuffer);

        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {
            class LogAnnotationExistMethodVisitor extends GeneratorAdapter {
                private String name;

                boolean hasAnnotation;
                boolean isStatic;

                LogAnnotationExistMethodVisitor(MethodVisitor mv, int access, String name, String descriptor) {
                    super(Opcodes.ASM5, mv, access, name, descriptor);
                    this.name = name;
                    isStatic = (access & 8) != 0;
                }

                @Override
                public void visitCode() {
                    super.visitCode();
                    if (hasAnnotation) {
                        Handle handle = new Handle(
                                H_INVOKESTATIC,
                                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                                "makeConcatWithConstants",
                                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                                false);

                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                        String descString = makeDescString();
                        String argumentString = makeArgString();
                        loadArgs(mv);

                        mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + descString + ")Ljava/lang/String;", handle, argumentString);
                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    }
                }

                private void loadArgs(MethodVisitor mv) {
                    Type[] argumentTypes = getArgumentTypes();

                    for (int i = 0; i < argumentTypes.length; i++) {
                        if (isStatic) {
                            loadArg(mv, i, argumentTypes[i].toString());
                        } else {
                            loadArg(mv, i + 1, argumentTypes[i].toString());
                        }
                    }
                }

                private String makeDescString() {
                    StringBuilder builder = new StringBuilder();
                    for (Type argumentType : getArgumentTypes()) {
                        builder.append(argumentType);
                    }
                    return builder.toString();
                }

                private void loadArg(MethodVisitor mv, int i, String argType) {
                    switch (argType) {
                        case "C":
                        case "S":
                        case "B":
                        case "Z":
                        case "I":
                            mv.visitVarInsn(Opcodes.ILOAD, i);
                            break;
                        case "L":
                            mv.visitVarInsn(Opcodes.LLOAD, i);
                            break;
                        case "F":
                            mv.visitVarInsn(Opcodes.FLOAD, i);
                            break;
                        default:
                            mv.visitVarInsn(Opcodes.ALOAD, i);
                            break;
                    }
                }

                private String makeArgString() {
                    StringBuilder builder = new StringBuilder("method: ").append(name).append(", params: ");

                    for (int i = 0; i < getArgumentTypes().length; i++) {
                        builder.append("\u0001");

                        if (i != getArgumentTypes().length)
                            builder.append(", ");
                    }

                    return builder.toString();
                }

                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                    if (descriptor.equals("Lru/otus/hw04/annotations/Log;"))
                        hasAnnotation = true;

                    return super.visitAnnotation(descriptor, visible);
                }
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new LogAnnotationExistMethodVisitor(mv, access, name, descriptor);
            }
        };

        cr.accept(cv, 0);

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(cw.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cw.toByteArray();

    }
}
