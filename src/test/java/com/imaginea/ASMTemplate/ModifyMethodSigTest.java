package com.imaginea.ASMTemplate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import junit.framework.TestCase;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ModifyMethodSigTest extends TestCase {

	public void testTransform() throws IOException {
		InputStream in = null;
		in = new FileInputStream(
				"target/classes/com/imaginea/ASMTemplate/A.class");
		ClassReader cr = new ClassReader(in);
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, 0);

		ModifyMethodSig mma = new ModifyMethodSig("fun2", "()V", "(I)D");
		mma.transform(classNode);

		classNode.name = "B";
		ClassWriter cw = new ClassWriter(0);
		classNode.accept(cw);
		File outDir = new File("out/com/imaginea/ASM");
		Boolean file = outDir.mkdirs();
		if (!file) {
			System.out.println("Directory not created");
		}
		DataOutputStream dout = null;
		dout = new DataOutputStream(new FileOutputStream(new File(outDir,
				"B.class")));
		if (cw != null)
			dout.write(cw.toByteArray());
		dout.flush();
		dout.close();

		InputStream inn = null;
		inn = new FileInputStream("out/com/imaginea/ASM/B.class");
		ClassReader crr = new ClassReader(inn);
		ClassNode cn = new ClassNode();
		crr.accept(cn, 0);

		boolean isCorrect = false;

		List<MethodNode> list = cn.methods;
		for (MethodNode mn : list) {
			if ("fun2".equals(mn.name) && "(I)D".equals(mn.desc)) {
				isCorrect = true;
				break;
			}
		}
		if (isCorrect)
			assertTrue(true);
		else
			assertTrue(false);

	}
}
