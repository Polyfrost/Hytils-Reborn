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

package club.sk1er.hytilities.asm;

import club.sk1er.hytilities.forge.transformer.HytilitiesTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ListIterator;

public class LayerArmorBaseTransformer implements HytilitiesTransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.entity.layers.LayerArmorBase"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            final String methodName = mapMethodName(classNode, method);

            if (methodName.equals("renderLayer") || methodName.equals("func_177182_a")) {
                final ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();

                while (iterator.hasNext()) {
                    final AbstractInsnNode next = iterator.next();

                    if (next instanceof MethodInsnNode && next.getOpcode() == Opcodes.INVOKESPECIAL) {
                        final String methodInsnName = mapMethodNameFromNode(next);
                        if (methodInsnName.equals("isSlotForLeggings") || methodInsnName.equals("func_177180_b")) {
                            method.instructions.insertBefore(next.getPrevious().getPrevious(), checkRender());
                            break;
                        }
                    }

                }
            }
        }
    }

    // if (!LayerArmorBaseHook.shouldRenderArmour(itemstack)) return;
    private InsnList checkRender() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 10));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
            "club/sk1er/hytilities/asm/hooks/LayerArmorBaseHook",
            "shouldRenderArmour",
            "(Lnet/minecraft/item/ItemStack;)Z",
            false));
        final LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, ifeq));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);
        return list;
    }
}
