package com.imaginea.ASMTemplate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import junit.framework.TestCase;

public class ModifyFieldAccessTest extends TestCase {

	public void testTransform() throws IOException {
		InputStream in = null;
		in = new FileInputStream(
				"target/classes/com/imaginea/ASMTemplate/A.class");
		ClassReader cr = new ClassReader(in);
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, 0);

		ModifyFieldAccess mfa = new ModifyFieldAccess("i", "I",
				Opcodes.ACC_PRIVATE, Opcodes.ACC_PUBLIC);
		mfa.transform(classNode);

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

		boolean isPresent = false;

		List<FieldNode> list = cn.fields;
		int access = 0;
		for (FieldNode fn : list) {
			if ("i".equals(fn.name)) {
				isPresent = true;
				access = fn.access;
				break;
			}
		}
		if (isPresent) {
			if (access == Opcodes.ACC_PUBLIC)
				assertTrue(true);
			else
				assertTrue(false);
		}
	}
}
