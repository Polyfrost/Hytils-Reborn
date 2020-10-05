/*
 * Hytilities - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020  Sk1er LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.sk1er.hytilities.tweaker.transformer;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public interface HytilitiesTransformer {
    /**
     * The class name that's being transformed
     *
     * @return the class name
     */
    String[] getClassName();

    /**
     * Perform any asm in order to transform code
     *
     * @param classNode the transformed class node
     * @param name      the transformed class name
     */
    void transform(ClassNode classNode, String name);

    /**
     * Map the method name from notch names
     *
     * @param classNode  the transformed class node
     * @param methodNode the transformed classes method node
     * @return a mapped method name
     */
    default String mapMethodName(ClassNode classNode, MethodNode methodNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);
    }

    /**
     * Map the field name from notch names
     *
     * @param classNode the transformed class node
     * @param fieldNode the transformed classes field node
     * @return a mapped field name
     */
    default String mapFieldName(ClassNode classNode, FieldNode fieldNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(classNode.name, fieldNode.name, fieldNode.desc);
    }

    /**
     * Map the method desc from notch names
     *
     * @param methodNode the transformed method node
     * @return a mapped method desc
     */
    default String mapMethodDesc(MethodNode methodNode) {
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(methodNode.desc);
    }

    /**
     * Map the method name from notch names
     *
     * @param methodInsnNode the transformed method insn node
     * @return a mapped insn method
     */
    default String mapMethodNameFromNode(AbstractInsnNode node) {
        MethodInsnNode methodInsnNode = (MethodInsnNode) node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }

    /**
     * Map the field name from notch names
     *
     * @param fieldInsnNode the transformed field insn node
     * @return a mapped insn field
     */
    default String mapFieldNameFromNode(AbstractInsnNode node) {
        FieldInsnNode fieldInsnNode = (FieldInsnNode) node;
        return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
    }

    /**
     * Remove instructions to this method
     *
     * @param methodNode the method being cleared
     */
    default void clearInstructions(MethodNode methodNode) {
        methodNode.instructions.clear();

        // dont waste time clearing local variables if they're empty
        if (!methodNode.localVariables.isEmpty()) {
            methodNode.localVariables.clear();
        }

        // dont waste time clearing try-catches if they're empty
        if (!methodNode.tryCatchBlocks.isEmpty()) {
            methodNode.tryCatchBlocks.clear();
        }
    }
}
