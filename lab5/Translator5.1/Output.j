.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 20
 ldc 2
 idiv 
 dup 
 istore 0
 dup 
 istore 1
 pop 
 goto L1
L1:
 iload 0
 invokestatic Output/print(I)V
 iload 1
 invokestatic Output/print(I)V
 goto L2
L2:
 invokestatic Output/read()I
 istore 2
 invokestatic Output/read()I
 istore 3
 invokestatic Output/read()I
 istore 4
 goto L3
L3:
 ldc 1
 invokestatic Output/print(I)V
 iload 2
 invokestatic Output/print(I)V
 ldc 2
 ldc 3
 ldc 2
 iadd 
 iadd 
 invokestatic Output/print(I)V
 goto L4
L4:
 iload 2
 iload 3
 if_icmpgt L6
 goto L7
L6:
 iload 2
 invokestatic Output/print(I)V
 goto L5
L7:
 iload 3
 invokestatic Output/print(I)V
 goto L5
L5:
 ldc 2
 ldc 3
 ldc 4
 imul 
 imul 
 ldc 2
 idiv 
 dup 
 istore 1
 pop 
 goto L8
L8:
 iload 1
 invokestatic Output/print(I)V
 goto L9
L9:
 goto L0
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

